import * as search from './sonar.rules.search.api.js'
import * as show from './sonar.rules.show.api.js'

export default {
    ...search,
    ...show
}