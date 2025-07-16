
import SonarRequestPolyfill from './polyfills/sonar-request'

const SonarRequest = self.SonarRequest ?? SonarRequestPolyfill

export const getJSON = SonarRequest.getJSON
