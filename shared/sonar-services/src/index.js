import * as request from './polyfills/sonar-request'
import * as i18n from './polyfills/sonar-i18n'
import * as toast from './polyfills/sonar-toast'

import { handlers } from './api/mock/msw-handler'

import * as issues from './api/issues/sonar.issues.search.api'
import * as measures from './api/measures/sonar.measures.component.api'

export default {
    ...request,
    ...i18n,
    ...toast,
    ...issues,
    ...measures,
    handlers
}