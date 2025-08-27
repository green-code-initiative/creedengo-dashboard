import * as request from './polyfills/sonar-request.js'
import * as i18n from './polyfills/sonar-i18n.js'
import * as toast from './polyfills/sonar-toast.js'

import { handlers } from './api/mock/msw-handler.js'

import * as issues from './api/issues/sonar.issues.search.api.js'
import * as measures from './api/measures/sonar.measures.component.api.js'
import * as branches from './api/project-branches/sonar.project-branches.list.api.js'

export default {
    initSettings: request.initSettings,
    ...request,
    ...i18n,
    ...toast,
    ...issues,
    ...measures,
    ...branches,
    handlers
}