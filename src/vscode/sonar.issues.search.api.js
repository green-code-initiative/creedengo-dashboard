//import sonarRequestAPI from '@sonar/sonar-request'
import sonarRequestAPI from './sonar-request.js'

const API = 'api/issues'
const routeUrl = `${API}/search`
const sustainabilitySearchParams = {
  issueStatuses: 'OPEN,CONFIRMED',
  statuses: 'OPEN,CONFIRMED,REOPENED', // legacy support 
  tags: 'sustainability,greensight,ecocode,creedengo',
}

/**
 * Get an issues facet for a given project, branch, and facet name.
 * 
 * @param {string} componentKeys key of the project
 * @param {string} branch 
 * @param {string} facetName 
 * @returns Promise<{[key: string]: number}>
 */
export async function getIssuesFacet(componentKeys, branch, facetName) {
  const searchParams = {
    ...sustainabilitySearchParams,
    componentKeys,
    branch,
    facets: facetName,
    ps: 1, // no issues parsing, we only want the facets
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

function facetFormater(result, severity) {
  const { val, count } = severity;
  result[val.toLowerCase()] = count;
  return result;
}