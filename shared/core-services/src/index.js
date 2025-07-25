import api from './adapter'
import * as score from './score.service'

export default {
    api,
    ...score
}