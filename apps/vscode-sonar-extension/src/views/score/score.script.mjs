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
    const priorityRuleNameElem = document.querySelector('.priority-rule-name');
    const priorityRuleContentElem = document.querySelector('.priority-rule-content');
    const branchFields = document.forms['branch-selection'].elements;
    const branchesSelect = branchFields['branch'];

    try {
        const oldState = vscode.getState() || { score: '', priorityRule: '', branch: '' };
        let { score, priorityRule, branch } = oldState;

        const messageHandler = addMessageHandler({
            updateScore: data => updateScore(data.value),
            updateBranches: data => updateBranches(data.value),
            updatePriorityRule: data => updatePriorityRule(data.value),
            clearScore: () => updateScore(''),
        }, logError)

        // Handle messages sent from the extension to the webview
        window.addEventListener('message', ({ data }) => messageHandler(data));

        /**
         * @param {string} newScore
         */
        function updateScore(newScore) {
            score = newScore;
            scoreOutput.textContent = `Score: ${score || 'Cleared'}`;
            vscode.setState({ score, priorityRule, branch });
        }

        /**
         * @param {any} priorityRuleData
         */
        function updatePriorityRule(priorityRuleData) {
            priorityRule = priorityRuleData;
            console.log('Updating priority rule', priorityRule);
            priorityRuleNameElem.textContent = priorityRule ? priorityRule.name : 'None';
            priorityRuleContentElem.innerHTML = priorityRule ? priorityRule.descriptionSections[0].content : 'None';
            vscode.setState({ score, priorityRule, branch });
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
            vscode.setState({ score, priorityRule, branch });
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