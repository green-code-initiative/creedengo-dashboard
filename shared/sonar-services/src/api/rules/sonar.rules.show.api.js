//import sonarRequestAPI from '@sonar/sonar-request'
import sonarRequestAPI from '../polyfills/sonar-request.js'

const API = 'api/rules'
const routeUrl = `${API}/show`

/**
 * Return details about a rule
 *
 * @param {string} rule_key
 * @returns Promise<Object>>
 */
export async function getRuleDetails(ruleKey) {
  const searchParams = { 'rule_key': ruleKey }
  const { rule } = await sonarRequestAPI.getJSON(routeUrl, searchParams)
  return rule
}
