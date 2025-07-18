//import sonarRequestAPI from '@sonar/sonar-request'
import sonarRequestAPI from '../polyfills/sonar-request'

const API = 'api/measures'
const routeUrl = `${API}/component`

/**
 * Return the number of Line of code of a project
 *
 * @param {string} component  key of the project
 * @param {string} branch
 * @returns
 */
export async function getNumberOfLineOfCode(component, branch) {
  const searchParams = { component, branch, metricKeys: 'ncloc' };
  const page = await sonarRequestAPI.getJSON(routeUrl, searchParams);
  return Number(page?.component?.measures?.[0]?.value || 0);
}
