//import sonarRequestAPI from '@sonar/sonar-request'
import sonarRequestAPI from '../polyfills/sonar-request.js'

const API = 'api/measures'
const routeUrl = `${API}/component`

/**
 * Return the number of Line of code of a project
 *
 * @param {Object} options
 * @param {string} options.project  key of the project
 * @param {string} options.branch
 * @returns
 */
export async function getNumberOfLineOfCode({ project, branch }) {
  const searchParams = { component: project, branch, metricKeys: 'ncloc' };
  const page = await sonarRequestAPI.getJSON(routeUrl, searchParams);
  return Number(page?.component?.measures?.[0]?.value || 0);
}
