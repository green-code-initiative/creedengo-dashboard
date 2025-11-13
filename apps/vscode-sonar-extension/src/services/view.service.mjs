import { readFile } from 'node:fs/promises'

import vscode from '../compat/vscode.cjs'
import { addMessageHandler } from './message.service.mjs'


const { window, Uri } = vscode

function getNonce(length = 32) {
    const typedArray = crypto.getRandomValues(new Uint8Array(length));
    return Array.prototype.map.call(
        typedArray,
        value => value.toString(16).padStart(2, "0")
    ).join('');
}

function formatCspContent(properties) {
    return Object
        .entries(properties)
        .map(([key, value]) => `${key}-src ${value}`)
        .join(';')
}

/**
 * @typedef {Object} ViewConfiguration
 * @property {vscode.WebviewView} webviewView 
 * @property {string} htmlFilePath
 * @property {string} [scriptFilePath]
 * @property {string} [styleFilePath]
 * @property {Object} [messageHandlers]
 */

/**
 * @typedef {Object} ViewContext
 * @property {ImportMeta} context.meta
 * @property {vscode.Uri} context.extensionUri
 */

/**
 * Generates a Webview baseUrl using extensionUri and the received import.meta
 * 
 * @param {vscode.Webview} webview 
 * @param {ImportMeta} meta 
 * @param {vscode.Uri} extensionUri 
 * @returns {vscode.Uri}
 */
function generateWebviewBaseUrl(webview, meta, extensionUri) {
    const baseUrl = new URL('.', meta.url)
    // TODO: Test on windows as it may have some "\" separators instead of "/" ones
    //  In that case, an additionnal replace should help to fix it
    const viewRelativeBasePath = baseUrl.pathname.replace(extensionUri.path, '') 
    const viewBaseUrl = Uri.joinPath(extensionUri, ...viewRelativeBasePath.split('/'))
    return webview.asWebviewUri(viewBaseUrl);
}

/**
 * @param {ViewConfiguration} configuration 
 * @param {ViewContext} context 
 */
async function loadPage(configuration, context) {
    const { webviewView, htmlFilePath, scriptFilePath, styleFilePath } = configuration
    const { meta, extensionUri } = context
    const { webview } = webviewView;

    const htmlSourceUrl = new URL(htmlFilePath, meta.url)
    const htmlSource = await readFile(htmlSourceUrl, { encoding: 'utf8' });
    const webViewBaseUrl = generateWebviewBaseUrl(webview, meta, extensionUri)
    const extraHtml = []
    const options = { localResourceRoots: [extensionUri] }

    const { cspSource } = webview
    const cspOptions = {
        default: "'none'",
        img: cspSource,
        style: cspSource
    }

    if (scriptFilePath) {
        options.enableScripts = true
        const nonce = getNonce()
        cspOptions.script = `'nonce-${nonce}'`
        extraHtml.push(`
    <script src="${scriptFilePath}" type="module" nonce="${nonce}" defer></script>`)
    }

    if (styleFilePath) {
        cspOptions.style = cspSource
        extraHtml.push(`
    <link rel="stylesheet" src="${styleFilePath}" />`)
    }

    const html = htmlSource.replace('</head>', `
    <base href="${webViewBaseUrl}/">
    <meta http-equiv="Content-Security-Policy" content="${formatCspContent(cspOptions)}">
    ${extraHtml}
</head>`
    )

    // Apply to prepared data to the Webview
    Object.assign(webview, { options, html });
}

export class WebViewService {
    #context;

    /**
     * @param {ImportMeta} meta 
     * @param {vscode.Uri} extensionUri 
     */
    constructor(meta, extensionUri) {
        this.#context = { meta, extensionUri }
    }

    /**
     * @param {ViewConfiguration} configuration 
     */
    async setWebviewView(configuration) {
        const { webviewView, messageHandlers } = configuration
        const { webview } = webviewView;

        this.postMessage = webview.postMessage.bind(webview);
        // `show` is not implemented in 1.49 but is for 1.50 insiders
        this.show = webviewView.show ? webviewView.show.bind(webviewView) : () => {};

        webview.onDidReceiveMessage(
            addMessageHandler(
                messageHandlers, 
                (message) => window.showErrorMessage(message)
            )
        )

        await loadPage(configuration, this.#context)
    }

}