import api from './adapter'
import settings from './settings.service'
import * as score from './score.service'

export default {
    api,
    settings,
    ...score
}