import SonarRequestPolyfill from '../polyfills/sonar-request.js'

const SonarRequest = globalThis.SonarRequest ?? SonarRequestPolyfill

export const getJSON = SonarRequest.getJSON
