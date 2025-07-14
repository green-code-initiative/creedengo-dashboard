/**
 * Re-implementation of the Sonarqube `design-system` Toast API
 *
 * Provides a compatible API to be usable from external UIs (VScode, Confluence, ...)
 * In this context, a Sonar TOKEN is required for the Web API authentication
 *
 * @see https://github.com/SonarSource/sonarqube/blob/master/server/sonar-web/design-system/src/components/toast-message/toast-utils.tsx
 * @see https://github.com/SonarSource/sonarqube/blob/master/server/sonar-web/src/main/js/app/components/extensions/exposeLibraries.ts
 *
 * @see https://vue3-toastify.js-bridge.com/api/toast.html
 * @see https://fkhadra.github.io/react-toastify/api/toast
 **/

import { toast } from 'vue3-toastify'

/**
 * @typedef {Object} ToastOptions
 * @property {string} role ARIA Role ('alert', 'status', 'log')
 * @property {number} delay Let you delay the toast appearance. Pass a value in ms
 * @property {number|boolean} autoClose Delay in ms to close the toast. If set to false, the notification needs to be closed manually
 */

const RESTRICTED_OPTIONS = ['role', 'delay', 'autoClose']

function filter(overrides) {
  const options = {}
  for (const key of RESTRICTED_OPTIONS) {
    if (key in overrides) {
      options[key] = overrides[key]
    }
  }
  return options
}

/**
 * @param {string} message
 * @param {ToastOptions} [options]
 * @returns
 */
export function addGlobalErrorMessage(message, options) {
  return options ? toast.error(message, filter(options)) : toast.error(message)
}

/**
 * @param {string} message
 * @param {ToastOptions} [options]
 * @returns
 */
export function addGlobalSuccessMessage(message, options) {
  return options ? toast.success(message, filter(options)) : toast.success(message)
}

