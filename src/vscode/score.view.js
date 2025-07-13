import { window, Uri } from 'vscode';

import { getSonarConfiguration, getNonce } from './utils.js';

// @see https://github.com/microsoft/vscode-extension-samples/blob/main/webview-view-sample/src/extension.ts

export class CreedengoScoreViewProvider {
    #view;
    #extensionUri;
    #show;
    #postMessage;

    constructor(extensionUri) {
        this.#extensionUri = extensionUri;
    }

	resolveWebviewView(webviewView) {
        const { webview } = webviewView;
        
		this.#view = webview;
        this.#postMessage = webview.postMessage.bind(webview);
        // `show` is not implemented in 1.49 but is for 1.50 insiders
        this.#show = webviewView.show ? webviewView.show.bind(webviewView) : () => {};

        Object.assign(webview, {
            options: {
                enableScripts: true,
                localResourceRoots: [this.#extensionUri]
	    	},
            html: this.#getHtmlForWebview(webview)
        });

        this.#showConfiguration();

		webview.onDidReceiveMessage(data => {
            console.log('message from webview', data);
			switch (data.type) {
                case 'updateScore': {
                    //this.updateScore();
                    break;
                }
                case 'error': {
                    window.showErrorMessage(`Error: ${data.message}`);
                    break;
                }
	    	}
        });
	}

	async updateScore() {
        if (!this.#view) {
            return;
        }
        const score = await window.showQuickPick(['A', 'B', 'C', 'D', 'E'], {
            placeHolder: 'Select a score',
        });
        this.#show(true); 
		this.#postMessage({ type: 'updateScore', value: score });
	}

	async clearScore() {
        if (!this.#view) {
            return;
        }
        this.#show(true); 
		this.#postMessage({ type: 'clearScore', value: '' });
	}

    async #showConfiguration() {
        const value = await getSonarConfiguration();
        console.log('Creedengo configuration:', value);
        this.#postMessage({ type: 'updateConfiguration', value });
    }

    #getHtmlForWebview(webview) {
		// Get the local path to main script run in the webview, then convert it to a uri we can use in the webview.
		const scriptUri = webview.asWebviewUri(Uri.joinPath(this.#extensionUri, 'media', 'score.script.js'));

		// Do the same for the stylesheet.
		const styleResetUri = webview.asWebviewUri(Uri.joinPath(this.#extensionUri, 'media', 'reset.css'));
		const styleVSCodeUri = webview.asWebviewUri(Uri.joinPath(this.#extensionUri, 'media', 'vscode.css'));
		const styleMainUri = webview.asWebviewUri(Uri.joinPath(this.#extensionUri, 'media', 'main.css'));

		// Use a nonce to only allow a specific script to be run.
		const nonce = getNonce();

		return `<!DOCTYPE html>
			<html lang="en">
			<head>
				<meta charset="UTF-8">
				<meta http-equiv="Content-Security-Policy" content="default-src 'none'; style-src ${webview.cspSource}; script-src 'nonce-${nonce}';">
				<meta name="viewport" content="width=device-width, initial-scale=1.0">
				<link href="${styleResetUri}" rel="stylesheet">
				<link href="${styleVSCodeUri}" rel="stylesheet">
				<link href="${styleMainUri}" rel="stylesheet">
				<title>Creendengo Score</title>
			</head>
			<body>
                <h1>Creedengo Score</h1>
                <div class="score">Score: Not Loaded</div>

				<button class="refresh-score-button">Refresh Score</button>
				<button class="clear-score-button">Clear Score</button>

                <form name="config"><ul>
                    <li>
                        <label for="server">Sonar Server URL:</label>
                        <input type="text" id="server" value="" placeholder="Enter the Sonar Server URL here">
                    </li>
                    <li>
                        <label for="token">Sonar Token:</label>
                        <input type="text" id="token" value="" placeholder="Enter the Sonar Authentication Token here">
                    </li>
                    <li>
                        <label for="organisation">Sonar Organisation Key:</label>
                        <input type="text" id="organization" value="" placeholder="Enter the Sonar Project Organisation here">
                    </li>
                    <li>
                        <label for="project">Sonar Project Key:</label>
                        <input type="text" id="projectKey" value="" placeholder="Enter the Sonar Project Key here">
                    </li>
                </ul>

                <div>
                    <textarea class="error-messages"></textarea>
                </div>

				<script nonce="${nonce}" src="${scriptUri}"></script>
			</body>
			</html>`;
    }

}
