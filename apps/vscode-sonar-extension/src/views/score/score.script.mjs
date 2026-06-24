/* global acquireVsCodeApi */
//@ts-check

import { addMessageHandler } from '../../services/message.service.mjs'

// It cannot access the main VS Code APIs directly.
(function () {
    // Global provided by VS Code
    // @ts-ignore
    const vscode = acquireVsCodeApi();
    let errorCount = 0;

    const scoreOutput = document.querySelector('.score');
    const priorityRuleNameElem = document.querySelector('.priority-rule');
    const footPrintElem = document.querySelector('.footprint');
    const branchFields = document.forms['branch-selection'].elements;
    const branchesSelect = branchFields['branch'];

    try {
        const oldState = vscode.getState() || { score: '', priorityRule: '', footPrint: '', branch: '' };
        let { score, priorityRule, footPrint, branch } = oldState;

        const messageHandler = addMessageHandler({
            updateScore: data => updateScore(data.value),
            updateBranches: data => updateBranches(data.value),
            updatePriorityRule: data => updatePriorityRule(data.value),
            updateFootPrint: data => updateFootPrint(data.value),
            clearScore: () => updateScore(''),
        }, logError)

        // Handle messages sent from the extension to the webview
        window.addEventListener('message', ({ data }) => messageHandler(data));

        /**
         * @param {string} newScore
         */
        function updateScore(newScore) {
            score = newScore;
            scoreOutput.textContent = `${score || 'Cleared'}`;
            vscode.setState({ score, priorityRule, footPrint, branch });
        }

        /**
         * @param {any} priorityRuleData
         */
        function updatePriorityRule(priorityRuleData) {
            priorityRule = priorityRuleData;
            if (priorityRule) {
                const link = document.createElement('a');
                link.href = priorityRule.url;
                link.textContent = priorityRule.priorityRule.name;
                priorityRuleNameElem.replaceChildren(link);
            } else {
                priorityRuleNameElem.textContent = 'None';
            }
            vscode.setState({ score, priorityRule, footPrint, branch });
        }

        function updateFootPrint(footPrintData) {
            footPrint = footPrintData;
            footPrintElem.textContent = footPrint ? `${footPrint} kgCO2eq` : 'None';
            vscode.setState({ score, priorityRule, footPrint, branch });
        }

        /**
         * @param {any[]} branches
         */
        function updateBranches(branches) {
            const previousSelected = branchesSelect?.selectedOptions[0]?.value;
            const { options } = branchesSelect;
            const { length } = options;
            for (let i = 0; i < length; i += 1) {
                options.remove(0);
            }
            /**
             * @type HTMLOptionElement
             */
            let newSelected;
            branches.forEach(branchData => {
                const { name, isMain } = branchData;
                const option = new Option(name, name);
                Object.assign(option.dataset, branchData);
                options.add(option);
                if ((isMain && !newSelected) || (name === previousSelected)) {
                    newSelected = option;
                }
            });
            if (newSelected) {
                newSelected.selected = true;
            }
            branch = newSelected.dataset.name;
            vscode.setState({ score, priorityRule, footPrint, branch });
            vscode.postMessage({ type: 'selectBranch', message: branch });
        }
        
    } catch({ message }) {
        logError(message);
    }

    /**
     * @param {string} message
     */
    function logError(message) {
        errorCount += 1;
        const errorMessages = document.querySelector('.error-messages');
        errorMessages.textContent += `${errorCount}: ${message}\n`;
        errorMessages.scrollTop = errorMessages.scrollHeight;
        vscode.postMessage({ type: 'error', message });
    }
}());