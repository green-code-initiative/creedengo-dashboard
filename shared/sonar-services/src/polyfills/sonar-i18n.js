/**
 * Sonar Internationalization
 *
 * @todo Make a wrapper between this Sonar API and the Vue I18n or Petite Vue I18n
 * @see https://vue-i18n.intlify.dev/
 * @see https://github.com/intlify/vue-i18n/tree/master/packages/petite-vue-i18n
 */

const IS_DEV = globalThis?.process.env.NODE_ENV === 'development'

const DEFAULT_MESSAGES = {
   
  default_error_message: 'The request cannot be processed. Try again later.'
}

const sonarQubeL10nBundle = { messages: {} }

export function clearCache() {
  sonarQubeL10nBundle.messages = {}
}

/**
 * @param {L10nBundle} bundle
 */
export function addToCache(bundle) {
  Object.assign(sonarQubeL10nBundle.messages, bundle)
}

function getMessages() {
  return sonarQubeL10nBundle.messages ?? DEFAULT_MESSAGES
}

/**
 * @param  {...string} keys
 * @returns string
 */
function translate(...keys) {
  const messageKey = keys.join('.')
  const l10nMessages = getMessages()

  if (IS_DEV && !l10nMessages[messageKey]) {
     
    console.error(`No message for: ${messageKey}`)
  }

  return l10nMessages[messageKey] || messageKey
}

/**
 *
 * @param {string} messageKey
 * @param  {...Array<string | number>} parameters
 * @returns string
 */
export function translateWithParameters(messageKey, ...parameters) {
  const messages = getMessages()
  const message = messages[messageKey]

  if (message) {
    return parameters
      .map((parameter) => String(parameter))
      .reduce((acc, parameter, index) => acc.replaceAll(`{${index}}`, () => parameter), message)
  }

  if (IS_DEV) {
     
    console.error(`No message for: ${messageKey}`)
  }
  //return JSON.stringify(messages)
  return `${messageKey}.${parameters.join('.')}`
}

export const t = translate
export const tp = translateWithParameters
