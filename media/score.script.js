/* global acquireVsCodeApi */
//@ts-check

// It cannot access the main VS Code APIs directly.
(function () {
    // Global provided by VS Code
    // @ts-ignore
    const vscode = acquireVsCodeApi();
    let errorCount = 0;

    const refreshButton = document.querySelector('.refresh-score-button');
    const clearButton = document.querySelector('.clear-score-button');
    const scoreOutput = document.querySelector('.score');
    const configFields = document.forms['config'].elements;

    try {
        const oldState = vscode.getState() || { score: '' };
        let { score } = oldState;

        refreshButton.addEventListener('click', () => {
            vscode.postMessage({ type: 'refreshScore' });
        });

        clearButton.addEventListener('click', () => {
            updateScore('');
        });

        // Handle messages sent from the extension to the webview
        window.addEventListener('message', ({ data }) => {
            switch (data.type) {
                case 'updateScore':
                    updateScore(data.value);
                    break;
                case 'updateConfiguration':
                    updateConfiguration(data.value);
                    break;
                case 'clearScore':
                    updateScore('');
                    break;
                default:
                    logError(`Unexpected message type: ${data.type}`);
            }
        });

        /**
         * @param {string} newScore
         */
        function updateScore(newScore) {
            score = newScore;
            scoreOutput.textContent = `Score: ${score || 'Cleared'}`;
            vscode.setState({ score });
        }

        /**
         * @param {Object} configuration
         * @param {string} configuration.server
         * @param {string} configuration.token
         * @param {string} configuration.organization
         * @param {string} configuration.projectKey
         */
        function updateConfiguration(configuration) {
            configFields['server'].value = configuration.server;
            configFields['token'].value = configuration.token;
            configFields['organization'].value = configuration.organization;
            configFields['projectKey'].value = configuration.projectKey;
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
        //errorMessages.scrollTop = errorMessages.scrollHeight;
        vscode.postMessage({ type: 'error', message });
    }
}());