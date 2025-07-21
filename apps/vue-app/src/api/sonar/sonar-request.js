import SonarRequestPolyfill from '@creedengo/sonar-services'

const SonarRequest = self.SonarRequest ?? SonarRequestPolyfill

export const getJSON = SonarRequest.getJSON
