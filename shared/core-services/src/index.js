import api from './adapter'
import * as models from './models'
import settings from './settings.service'
import * as score from './score.service'

export default {
    api,
    models,
    settings,
    ...score
}