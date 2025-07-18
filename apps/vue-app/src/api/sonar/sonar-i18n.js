
import { SonarI18nPolyfill } from '@creedengo/sonar-services'

export const t = self.t ?? SonarI18nPolyfill.t
export const tp = self.tp ?? SonarI18nPolyfill.tp