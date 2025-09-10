import api from './adapter.js'
import settings from './settings.service.js'

import * as priorityRule from './priority-rule.service.js'
import * as score from './score.service.js'

export default {
    api,
    settings,
    ...priorityRule,
    ...score
}