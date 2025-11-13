//import sonarRequestAPI from '@sonar/sonar-request'
import sonarRequestAPI from '../polyfills/sonar-request.js'
import { SUSTAINABILITY_TAGS } from '../constants.js'

const API = 'api/rules'
const routeUrl = `${API}/search`
const sustainabilitySearchParams = { tags: SUSTAINABILITY_TAGS }

/**
 * Return Sonarqube rules
 * !! only gather sustainability rules !!
 *
 * @param {Object} params
 * @param {string} params.rule_key
 * @param {string} params.languages
 * @param {string} params.qprofile
 * @param {'true'|'false'|'yes'|'no'} params.prioritizedRule
 * @param {'INFO'|'MINOR'|'MAJOR'|'CRITICAL'|'BLOCKER'} params.severities
 * @param {'INFO'|'LOW'|'MEDIUM'|'HIGH'|'BLOCKER'} params.impactSeverities
 * @param {'MAINTAINABILITY'|'RELIABILITY'|'SECURITY'} params.impactSoftwareQualities
 * @returns Promise<Array<{[key: string]: any}>>
 */
export async function findRules({ project, branch }) {
  const searchParams = {
    ...sustainabilitySearchParams,
    componentKeys: project,
    branch,
  }
  const page = await sonarRequestAPI.getJSON(routeUrl, searchParams)
  const { rules = [] } = page
  return rules
}
