//import sonarRequestAPI from '@sonar/sonar-request'
import sonarRequestAPI from '../polyfills/sonar-request.js'
import { SUSTAINABILITY_TAGS } from '../constants.js'

const API = 'api/issues'
const routeUrl = `${API}/search`
const sustainabilitySearchParams = {
  issueStatuses: 'OPEN,CONFIRMED',
  statuses: 'OPEN,CONFIRMED,REOPENED', // legacy support 
  tags: SUSTAINABILITY_TAGS,
}

/**
 * Return issues detected by Sonarqube
 * !! only gather sustainability tagged issues that are NOT closed !!
 *
 * @param {Object} params
 * @param {string} params.componentKeys  key of the project
 * @param {string} params.branch
 * @returns Promise<Array<{[key: string]: any}>>
 */
export async function findIssues({ project, branch }) {
  const searchParams = {
    ...sustainabilitySearchParams,
    componentKeys: project,
    branch,
  }
  const page = await sonarRequestAPI.getJSON(routeUrl, searchParams)
  const { issues = [] } = page
  return issues
}

function facetFormater(result, severity) {
  const { val, count } = severity;
  result[val.toLowerCase()] = count;
  return result;
}

/**
 * Get an issues facet for a given project, branch, and facet name.
 * 
 * @param {string} facetName 
 * @param {Object} config
 * @param {string} config.project key of the project
 * @param {string} config.branch 
 * @returns Promise<{[key: string]: number}>
 */
export async function getIssuesFacet(facetName, { project, branch, severity }) {
  const searchParams = {
    ...sustainabilitySearchParams,
    componentKeys: project,
    branch,
    severity,
    facets: facetName,
    ps: 0, // no issues parsing, we only want the facets
  }
  const { facets } = await sonarRequestAPI.getJSON(routeUrl, searchParams);

  function filterFacets({ property = '' }) {
    return property === facetName;
  }

  const facet = facets.find(filterFacets);

  if (!facet) {
    throw new Error(`Facet ${facetName} not found in SonarQube response`);
  }

  return facet.values.reduce(facetFormater, {});
}
