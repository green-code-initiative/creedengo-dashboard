/**
 * Re-implementation of the Sonarqube `global.SonarRequest` API
 *
 * Provides a compatible API to be usable from external UIs (VScode, Confluence, ...)
 * In this context, a Sonar TOKEN is required for the Web API authentication
 *
 * @see https://github.com/SonarSource/sonarqube/blob/master/server/sonar-web/src/main/js/app/components/extensions/exposeLibraries.ts
 * @see https://github.com/SonarSource/sonarqube/blob/master/server/sonar-web/src/main/js/helpers/cookies.ts
 * @see https://github.com/SonarSource/sonarqube/blob/master/server/sonar-web/src/main/js/helpers/l10n.ts
 * @see https://github.com/SonarSource/sonarqube/blob/master/server/sonar-web/src/main/js/helpers/l10nBundle.ts
 * @see https://github.com/SonarSource/sonarqube/blob/master/server/sonar-web/src/main/js/helpers/request.ts
 * @see https://github.com/SonarSource/sonarqube/blob/master/server/sonar-web/src/main/js/sonar-aligned/helpers/request.ts
 **/

// TODO: identify the correct way to retrieve the Sonar authentication token
let SONAR_TOKEN = import.meta?.env?.VITE_SONAR_TOKEN;
let SONAR_BASE_URL = '';

import { memoize, omitBy, isNil } from 'lodash-es'

import { t } from './sonar-i18n'
import { addGlobalErrorMessage, addGlobalSuccessMessage } from './sonar-toast'

function initSettings({ token, server }) {
  SONAR_TOKEN = token;
  SONAR_BASE_URL = server;
}

// Adapted from https://nodejs.org/api/http.html#http_http_HTTP_STATUS
const HttpStatus = {
  Ok: 200,
  Created: 201,
  NoContent: 204,
  MultipleChoices: 300,
  MovedPermanently: 301,
  Found: 302,
  BadRequest: 400,
  Unauthorized: 401,
  Forbidden: 403,
  NotFound: 404,
  InternalServerError: 500,
  NotImplemented: 501,
  BadGateway: 502,
  ServiceUnavailable: 503,
  GatewayTimeout: 504
}

const SonarRequest = {
  initSettings,
  request,
  get,
  getJSON,
  getText,
  omitNil,
  parseError,
  post,
  postJSON,
  postJSONBody,
  throwGlobalError,
  addGlobalSuccessMessage,
  HttpStatus
}

export default SonarRequest

/**
 * @typedef {Object.<string, string>} StringDict
 */

/**
 * @typedef {Object.<string, *>} RequestData
 */

/**
 * @param {string} documentCookie
 * @returns StringDict
 */
const parseCookies = memoize((documentCookie) => {
  const rawCookies = documentCookie.split('; ')
  const cookies = {}
  rawCookies.forEach((candidate) => {
    const [key, value] = candidate.split('=')
    cookies[key] = value
  })
  return cookies
})

/**
 * @warning Current implementation is browser dependent
 * @param {string} name
 * @returns string | undefined
 */
function getCookie(name) {
  return globalThis.document && parseCookies(globalThis.document.cookie)[name]
}

/**
 * @returns string
 */
function getCSRFTokenName() {
  return 'X-XSRF-TOKEN'
}

/**
 * @returns string
 */
function getCSRFTokenValue() {
  const cookieName = 'XSRF-TOKEN'
  const cookieValue = getCookie(cookieName)
  if (!cookieValue) {
    return ''
  }
  return cookieValue
}

/**
 * Return an object containing a special http request header used to prevent CSRF attacks.
 *
 * @returns StringDict
 */
function getCSRFToken() {
  // Fetch API in Edge doesn't work with empty header,
  // so we ensure non-empty value
  const value = getCSRFTokenValue()
  return value ? { [getCSRFTokenName()]: value } : {}
}

/**
 *
 * @param {Object} obj
 * @returns Object
 */
function omitNil(obj) {
  return omitBy(obj, isNil)
}

const HEADER_ACCEPT = 'Accept'
const HEADER_AUTHORISATION = 'Authorization'
const HEADER_CONTENT_TYPE = 'Content-Type'

const MIME_FORM_URLENCODED = 'application/x-www-form-urlencoded'
const MIME_JSON = 'application/json'

function encodeArrayURIComponent(name, value) {
  return value.map((v) => `${name}=${encodeURIComponent(v)}`).join('&')
}
/**
 * @class Request
 *
 * @property {RequestData} [data]
 * @property {boolean} isJSON
 */
class Request {
  #data = undefined
  #isJSON = false
  #options = {
    credentials: 'same-origin',
    method: 'GET',
    headers: { [HEADER_ACCEPT]: MIME_JSON }
  }
  #setHeader(name, value) {
    this.#options.headers[name] = value
  }
  #setHeaders(headers) {
    Object.assign(this.#options.headers, headers)
  }
  #setAuthorization() {
    if (SONAR_TOKEN) {
      this.#setHeader(HEADER_AUTHORISATION, `Bearer ${SONAR_TOKEN}`)
    }
  }
  #setBody(body, type) {
    if (type) {
      this.#setHeader(HEADER_CONTENT_TYPE, type)
    }
    this.#options.body = body
  }

  /**
   * @constructor
   * @param {string} url
   * @param {Object} [options]
   * @param {string} [options.method] HTTP method
   */
  constructor(url, { method = 'GET' } = {}) {
    this.url = url
    this.setMethod(method)
    this.#setAuthorization()
  }

  /**
   * @param {string} method
   * @returns Request
   */
  setMethod(method) {
    this.#options.method = method
    return this
  }

  /**
   * @param {RequestData} [data]
   * @param {boolean} [isJSON]
   * @returns
   */
  setData(data, isJSON = false) {
    if (!data) {
      return this
    }
    this.#data = data
    this.#isJSON = isJSON
    return this
  }

  /**
   *
   * @param {*} [customHeaders]
   * @returns {{ options: RequestInit, url: string }}
   */
  getSubmitData(customHeaders = {}) {
    const result = { options: this.#options, url: this.url }
    this.#setHeaders(customHeaders)

    const data = this.#data
    if (!data) {
      return result
    }

    if (this.#data instanceof FormData) {
      this.#setBody(data)
      return result
    }

    if (this.#isJSON) {
      this.#setBody(JSON.stringify(data), MIME_JSON)
      return result
    }

    let urlEncodedData = []
    for (let [name, value] of Object.entries(omitNil(data))) {
      urlEncodedData.push(
        Array.isArray(value)
          ? encodeArrayURIComponent(name, value)
          : `${name}=${encodeURIComponent(value)}`
      )
    }
    urlEncodedData = urlEncodedData.join('&')

    if (this.#options.method === 'GET') {
      result.url += `?${urlEncodedData}`
      return result
    }

    this.#setBody(urlEncodedData, MIME_FORM_URLENCODED)
    return result
  }

  /**
   * @returns Promise<Response>
   */
  submit() {
    const { url, options } = this.getSubmitData({ ...getCSRFToken() })
    return globalThis.fetch(`${getBaseUrl()}${url}`, options)
  }
}

/**
 * Make a request
 *
 * @param {string} url
 * @returns Request
 */
function request(url) {
  return new Request(url)
}

/**
 * @returns 
 */
function getBaseUrl() {
  return SONAR_BASE_URL || globalThis.baseUrl || ''
}

/**
 * @warning this function handles authentication through a web page redirection
 */
function handleRequiredAuthentication() {
  const { location, URLSearchParams } = globalThis;
  const { pathname, search, hash } = location;
  const searchParams = new URLSearchParams({ return_to: `${pathname}${search}${hash}` })
  location.replace(`${getBaseUrl()}/sessions/new?${searchParams.toString()}`)
}

/**
 * Check that response status is ok
 *
 * If not connected and no bypass
 *
 * @param {Response} response
 * @param {boolean} [bypassRedirect]
 * @returns Response
 */
function checkStatus(response, bypassRedirect = false) {
  if (response.status === HttpStatus.Unauthorized && !bypassRedirect) {
    handleRequiredAuthentication() // reload the page to create a new session
  }
  if (isSuccessStatus(response.status)) {
    return response
  }
  throw response
}

/**
 * Parse response as JSON
 *
 * @param {Response} response
 * @returns Promise<any>
 */
function parseJSON(response) {
  return response.json()
}

/**
 * Parse response as Text
 *
 * @param {Response} response
 * @returns Promise<string>
 */
function parseText(response) {
  return response.text()
}

/**
 * Parse error response of failed request
 *
 * @param {Reponse} response
 * @returns Promise<string>
 */
async function parseError(response) {
  const DEFAULT_MESSAGE = t('default_error_message')
  try {
    return parseErrorResponse(await parseJSON(response))
  } catch (_error) {
    console.error('Error parsing error response:', _error)
    return DEFAULT_MESSAGE
  }
}

/**
 *
 * @param {AxiosResponse | Response} [json]
 * @returns string
 */
function parseErrorResponse(json) {
  const DEFAULT_MESSAGE = t('default_error_message')
  let data
  if (!json) {
    return DEFAULT_MESSAGE
  }
  data = 'data' in json ? json.data : json
  const { message, errors } = data
  return message ?? errors?.map((error) => error.msg).join('. ') ?? DEFAULT_MESSAGE
}

/**
 * Shortcut to do a GET request and return a Response
 *
 * @param {string} url
 * @param {RequestData} [data]
 * @param {boolean} [bypassRedirect]
 * @returns Promise<Response>
 */
async function get(url, data, bypassRedirect = false) {
  const response = await request(url).setData(data).submit()
  return checkStatus(response, bypassRedirect)
}

/**
 * Shortcut to do a GET request and return response text
 *
 * @param {string} url
 * @param {RequestData} [data]
 * @param {boolean} [bypassRedirect]
 * @returns Promise<string>
 */
function getText(url, data, bypassRedirect = false) {
  return get(url, data, bypassRedirect).then(parseText)
}

/**
 * Shortcut to do a POST request and return response json
 *
 * @param {string} url
 * @param {RequestData} [data]
 * @param {boolean} [bypassRedirect]
 * @returns Promise<*>
 */
async function postJSON(url, data, bypassRedirect = false) {
  const response = await request(url).setMethod('POST').setData(data).submit()
  return parseJSON(checkStatus(response, bypassRedirect))
}

/**
 * Shortcut to do a POST request with a json body and return response json
 *
 * @param {string} url
 * @param {RequestData} [data]
 * @param {boolean} [bypassRedirect]
 * @returns Promise<*>
 */
async function postJSONBody(url, data, bypassRedirect = false) {
  const response = await request(url).setMethod('POST').setData(data, true).submit()
  return parseJSON(checkStatus(response, bypassRedirect))
}

/**
 * Shortcut to do a POST request
 *
 * @param {string} url
 * @param {RequestData} [data]
 * @param {boolean} [bypassRedirect]
 * @returns Promise<void>
 * @throws
 */
async function post(url, data, bypassRedirect = false) {
  const response = await request(url).setMethod('POST').setData(data).submit()
  checkStatus(response, bypassRedirect)
}

/**
 * @param {number} status
 * @returns
 */
function isSuccessStatus(status) {
  return status >= 200 && status < 300
}

/**
 *
 * @param {string} url
 * @param {RequestData} [data]
 * @param {RequestOptions} [options]
 * @returns Promise
 */
async function getJSON(url, data, options = {}) {
  const response = await get(url, data, options.bypassRedirect)
  return parseJSON(response)
}

/**
 * @param {Response} response
 * @returns Promise<Response | *>
 */
async function throwGlobalError(response) {
  addGlobalErrorMessage(await parseError(response))
  throw response
}
