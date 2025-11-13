//import sonarRequestAPI from '@sonar/sonar-request'
import sonarRequestAPI from '../polyfills/sonar-request.js'

const API = 'api/project_pull_requests'
const routeUrl = `${API}/list`

/**
 * Get an issues facet for a given project, branch, and facet name.
 * 
 * @param {string} project key of the project
 */
export async function getProjectPullRequests(project) {
  const searchParams = { project };
  const { pullRequests } = await sonarRequestAPI.getJSON(routeUrl, searchParams);
  return pullRequests;
}