import core from '@creedengo/core-services'
import SonarRequest from '../../polyfills/sonar-request'

const { Authenticator } = core.models
const { initSettings, getJSON } = SonarRequest

// A sonar API that require authentication
// without project parameter
const testApi = '/api/favorites/search'

/**
 * Checks if the provided token works with the given token
 * And apply the authentication settings to the request API
 * if success
 */
export default new Authenticator({
  name: 'By Token',
  connectionDetails: {},
  pending: false,
  connected: false,
  
  /**
   * Removes connection settings from the request API
   */
  async disconnect() {
    this.connectionDetails = {}
    initSettings({ token: '', server: '' })
    this.connected = false
  },
  
  /**
   * Test Connection and apply to the request API
   * on success
   * 
   * @param {String} server Sonar Server URL
   * @param {String} token Sonar Authentication Token
   * @throws {Error}
   */
  async connect(server, token) {
    let successStatus = false
    let fetchError = null
    this.pending = true
    try {
        initSettings({ token, server })
        const response = await getJSON(testApi)
        successStatus = response.ok
        this.connectionDetails = { server, token }
    } catch (error) {
        fetchError = error
        successStatus = false
    }
    this.pending = false
    this.connected = successStatus
    if (!successStatus) {
      throw fetchError
    } 
  }
})