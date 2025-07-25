import SonarRequestPolyfill from '../polyfills/sonar-request'

const SonarRequest = globalThis.SonarRequest ?? SonarRequestPolyfill

export const getJSON = SonarRequest.getJSON
