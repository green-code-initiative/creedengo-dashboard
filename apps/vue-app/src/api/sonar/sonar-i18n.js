
import SonarI18nPolyfill from './polyfills/sonar-i18n'

export const t = self.t ?? SonarI18nPolyfill.t
export const tp = self.tp ?? SonarI18nPolyfill.tp