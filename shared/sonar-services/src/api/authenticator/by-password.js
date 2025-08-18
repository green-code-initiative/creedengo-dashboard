import core from '@creedengo/core-services'
import SonarRequest from '../../polyfills/sonar-request'

const { Authenticator } = core.models
const { initSettings } = SonarRequest

const apiPath = 'api/authentication'

/**
   * Generates the URL for a given authentication endpoint
   * 
   * @param {String} server The Sonar server URL
   * @param {String} action One of 'login', 'logout', 'validate'
   * @returns 
   */
function getURl(server, action) {
    return new globalThis.URL(
        `${apiPath}/${action}`, server
    )
}


/**
 * Validate the connection status
 * 
 * @param {String} server 
 * @returns String
 * @throws Error If Server URL is wrong or if timedout
 */
async function validateConnection(server) {
    const validateUrl = getURl(server, 'validate')
    const response = await globalThis.fetch(validateUrl)
    const { valid } = await response.json();
    return valid
}

/**
 * Try to authenticate to sonar using the login API
 */
export default new Authenticator({
  name: 'By Password',
  connectionDetails: {},
  pending: false,
  connected: false,

  
  /**
   * Removes connection settings from the request API
   */
  async disconnect(serverURL) {
    let cause;
    let isConnected;
    const server = this.connectionDetails || serverURL
    if (!server) {
        throw new Error('No server defined. Probably no connected')
    }
    this.pending = true
    try {
        const logoutUrl = getURl(server, 'logout')
        await globalThis.fetch(logoutUrl, { method: 'POST' })
        isConnected = await validateConnection(server)
    } catch (error) {
        cause = error
    }
    this.connectionDetails = {}
    initSettings({ token: '', server: '' })
    this.pending = false
    this.connected = false
    if (cause || isConnected) {
        throw new Error('Still Connected. Please Retry.', { cause })
    }
  },
  
  /**
   * Connect with login and password
   * 
   * @param {String} server 
   * @param {String} login 
   * @param {String} password 
   * @throws {Error}
   */
  async connect(server, login, password) {
    let successStatus = false;
    let cause;
    this.connected = false;
    this.pending = true;
    const loginUrl = this.getURl(server, 'login')
    try {
        await globalThis.fetch(loginUrl, {
            method: 'POST',
            body: new globalThis.URLSearchParams({ login, password })
        })
        successStatus = validateConnection(server)
    } catch (error) {
        cause = error
        successStatus = false
    }
    this.pending = false
    this.connected = successStatus
    if (!successStatus) {
      throw new Error('Connection Failed', { cause })
    }
    this.connectionDetails = { server, login, password }
    initSettings({ server, token: '' })
  }
})
