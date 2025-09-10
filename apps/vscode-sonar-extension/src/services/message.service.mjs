
/**
 * @typedef {Object} Message
 * @property {string} type
 * @property {string} message
 */

/**
 * @typedef {(data: Message) => void} MessageHandler
 */

/**
 * @typedef {(message: string) => void} ErrorLogger
 */

/**
 * @param {Message} data 
 * @param {ErrorLogger} errorLogger 
 */
function error(data, errorLogger) {
    const logMessage = `Error: ${data.message}`
    console.error(logMessage)
    errorLogger(logMessage)
}

/**
 * @param {Object<string, MessageHandler>} messageHandlers 
 * @param {ErrorLogger} errorLogger 
 * @returns {MessageHandler}
 */
export function addMessageHandler(messageHandlers, errorLogger) {

    const handlers = {
        ...messageHandlers, 
        error
    }

    /**
     * @param {Message} data 
     */
    function handleMessage(data) {
        const action = handlers[data.type];
        if (!action) {
            const logMessage = `Unexpected "${data.type}" Message: ${data.message}`
            console.warn(logMessage)
            errorLogger(logMessage)
            return;
        }
        action(data);
    }

    return handleMessage;
}