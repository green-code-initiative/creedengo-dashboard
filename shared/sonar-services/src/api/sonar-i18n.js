import { SonarI18nPolyfill } from '../polyfills/sonar-i18n.js'

export const t = globalThis.t ?? SonarI18nPolyfill.t
export const tp = globalThis.tp ?? SonarI18nPolyfill.tp
