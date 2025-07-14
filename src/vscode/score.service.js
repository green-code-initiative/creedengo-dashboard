/**
 * @module score.services
 * @description This module provides functions to calculate the ABCDE score based on SonarQube issues.
 */
import { setConnectionConfiguration } from './sonar-request.js'
import { getIssuesFacet } from './sonar.issues.search.api.js';
import { getNumberOfLineOfCode } from './sonar.measures.component.api.js';

/**
 * Calculate an ABCDE score from the sustainability issues related to the project size.
 * @param {string} projectKey Project Key
 * @param {string} branch Git Branch Name.
 * @returns {Promise<string>} Score between A and E.
 */
export async function calculateProjectScore({projectKey, branch, server, token }) {
    if (token) {
        setConnectionConfiguration({ server, token });
    }
    const severityFacets = await getIssuesFacet(projectKey, branch, 'severities');
    const { info = 0, minor = 0, major = 0, critical = 0, blocker = 0 } = severityFacets;
    const consolidatedMinors = info + minor;

    const numberOfLines = await getNumberOfLineOfCode(projectKey, branch);
    const minorRatio = consolidatedMinors / numberOfLines;

    if (blocker >= 1) {
        return 'E';
    } 
    if (minorRatio >= 0.08 || major >= 10 || critical >= 1) {
        return 'D';
    }
    if (consolidatedMinors >= 10 || major >= 1) {
        return 'C';
    } 
    if (consolidatedMinors >= 1) {
        return 'B';
    }
    return 'A';
}
