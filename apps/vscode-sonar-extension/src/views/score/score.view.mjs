import vscode from '../../compat/vscode.cjs'

import { sync, getSonarConfiguration } from '../../services/configuration.service.mjs';
import { WebViewService } from '../../services/view.service.mjs';
import SonarAPI from '@creedengo/sonar-services'
import core from '@creedengo/core-services';

const { window, Uri } = vscode
const { getProjectBranches } = SonarAPI
const { api, calculateProjectScore, getPriorityRule, getFootprintEstimation } = core

api.init(SonarAPI)

// @see https://github.com/microsoft/vscode-extension-samples/blob/main/webview-view-sample/src/extension.ts

/**
 * @implements {vscode.WebviewViewProvider}
 */
export class CreedengoScoreViewProvider {
    #service;
    #currentBranch;

    /**
     * @param {vscode.Uri} extensionUri 
     */
    constructor(extensionUri) {
        this.#service = new WebViewService(import.meta, extensionUri);
    }

    /**
     * @param {vscode.WebviewView} webviewView 
     */
	async resolveWebviewView(webviewView) {
        const messageHandlers = {
            updateBranches: () => this.updateScore(),
            updatePriorityRule: () => this.updatePriorityRule(),
            updateFootPrint: () => this.updateFootPrint(),
            updateScore: () => this.updateScore(),
            selectBranch: data => {
                this.#currentBranch = data.message;
                this.updateScore();
            }
        }

        await this.#service.setWebviewView({
            webviewView,
            htmlFilePath: './score.view.html',
            scriptFilePath: './score.script.mjs',
            messageHandlers
        })
        
        await this.#updateBranches();
        await this.updatePriorityRule();
        await this.updateFootPrint();

        webviewView.onDidChangeVisibility(async () => {
            if (!webviewView.visible) {
                return;
            }
            await this.#updateBranches();
            await this.updateScore();
            await this.updatePriorityRule();
            await this.updateFootPrint();
        })
	}

    async updatePriorityRule() {
        if (!this.#service) {
            return;
        }
        let priorityRule;
        try {
            const { ready, project } = await sync();
            if (!ready || !project) {
                return false
            }
            const branch = this.#currentBranch
            priorityRule = await getPriorityRule({ project, branch });
        } catch(error) {
            window.showErrorMessage(error.message)
        }
        const { server } = await getSonarConfiguration();
        this.#service.show(true);
        const url = `${server}coding_rules?languages=${priorityRule.lang}&q=${priorityRule?.name.split(' ').join('+')}&open=java%3AS2187`
        this.#service.postMessage({ type: 'updatePriorityRule', value: { 
            priorityRule: priorityRule, 
            url: url, 
        } });
    }

    async updateFootPrint() {
        if (!this.#service) {
            return;
        }
        let footPrint;
        try {
            const { ready, project } = await sync();
            if (!ready || !project) {
                return false
            }
            const branch = this.#currentBranch
            footPrint = await getFootprintEstimation({ project, branch });
        } catch(error) {
            window.showErrorMessage(error.message)
        }
        this.#service.show(true);
        this.#service.postMessage({ type: 'updateFootPrint', value: footPrint });
    }

    /**
     * Request data from the Sonar Server to recalculate the sustainability score
     * @returns 
     */
	async updateScore() {
        if (!this.#service) {
            return;
        }
        let score;
        try {
            const { ready, project } = await sync();
            if (!ready || !project) {
                return false
            }
            const branch = this.#currentBranch
            score = await calculateProjectScore({ project, branch });
        } catch(error) {
            window.showErrorMessage(error.message)
        }
        this.#service.show(true); 
		this.#service.postMessage({ type: 'updateScore', value: score });
	}

    /**
     * Reset the currently Displayed Score
     * @returns 
     */
	async clearScore() {
        if (!this.#service) {
            return;
        }
        this.#service.show(true); 
		this.#service.postMessage({ type: 'clearScore', value: '' });
	}

    async refresh() {}

    /**
     * Retrieves the project git branches for which Sonar Analyses are available 
     */
    async #updateBranches() {
        let branches;
        const { ready, project } = await sync();
        if (!ready) {
            return false
        }
        try {
            branches = await getProjectBranches(project);
        } catch(error) {
            window.showErrorMessage(`Error while getting branches for ${project}: ${error.message}`)
        }
        console.log('Sonar Branches:', branches);
        this.#service.postMessage({ type: 'updateBranches', value: branches });
    }
}
