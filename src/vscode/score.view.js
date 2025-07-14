import { window, Uri } from 'vscode';

import { getSonarConfiguration, getNonce } from './utils.js';
import { setConnectionConfiguration } from './sonar-request.js'
import { getProjectBranches } from './sonar.project-branches.list.api.js'
import { calculateProjectScore } from './score.service.js';

// @see https://github.com/microsoft/vscode-extension-samples/blob/main/webview-view-sample/src/extension.ts

export class CreedengoScoreViewProvider {
    #view;
    #extensionUri;
    #show;
    #currentBranch;
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

        this.#updateBranches();

		webview.onDidReceiveMessage(data => {
            console.log('message from webview', data);
			switch (data.type) {
                case 'updateBranches': {
                    this.updateScore();
                    break;
                }
                case 'updateScore': {
                    this.updateScore();
                    break;
                }
                case 'selectBranch': {
                    this.#currentBranch = data.message;
                    this.updateScore();
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

        let score;

        try {
            const configuration = await getSonarConfiguration();
            score = await calculateProjectScore(
                configuration, this.#currentBranch
            );
        } catch(error) {
            window.showErrorMessage(error.message)
        }

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

    async #updateBranches() {
        let branches;
        const { server, token, projectKey } = await getSonarConfiguration();
        try {
            setConnectionConfiguration({ server, token });
            branches = await getProjectBranches(projectKey);
        } catch(error) {
            window.showErrorMessage('get branches for ' + projectKey + ' ' + error.message)
        }
        console.log('Sonar Branches:', branches);
        this.#postMessage({ type: 'updateBranches', value: branches });
    }

    #getHtmlForWebview(webview) {
        const baseUrl = `${this.#view.asWebviewUri(
            Uri.joinPath(this.#extensionUri, 'media')
        )}/`;
		const scriptFile = 'score.script.js';
		const nonce = getNonce();

		return `<!DOCTYPE html>
			<html lang="en">
			<head>
				<meta charset="UTF-8">
                <base href="${baseUrl}">
				<meta http-equiv="Content-Security-Policy" content="default-src 'none';img-src ${webview.cspSource} style-src ${webview.cspSource}; script-src 'nonce-${nonce}';">
				<meta name="viewport" content="width=device-width, initial-scale=1.0">
				<title>Creendengo Score</title>
			</head>
			<body>
                <header>
                  <form name="branch-selection">
                    <select name="branch"></select>
                  </form>
                </header>
                <main>
                  <score class="score">Score: Not Loaded</strong>
                </main>
                <footer>
                  <div>
                    Provided by 
                    <img src="favicon.png" width="16" height="16" align="middle">
                    <a href="https://github.com/green-code-initiative">Creedengo</a>
                  </div>
                  <hr>
                  <textarea class="error-messages"></textarea>
                </footer>
				<script nonce="${nonce}" src="${scriptFile}"></script>
			</body>
			</html>`;
    }

}
