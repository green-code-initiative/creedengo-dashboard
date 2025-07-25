/**
 * @module score.services
 * @description This module provides functions to calculate the ABCDE score based on SonarQube issues.
 */

import api from './adapter';

/**
 * Calculate an ABCDE score from the sustainability issues related to the project size.
 * @param {Object} config
 * @param {string} config.project Project Key
 * @param {string} config.branch Git Branch Name.
 * @returns {Promise<string>} Score between A and E.
 */
export async function calculateProjectScore(config) {

    const severityFacets = await api.getIssuesFacet('severities', config);
    const { info = 0, minor = 0, major = 0, critical = 0, blocker = 0 } = severityFacets;
    const consolidatedMinors = info + minor;

    const numberOfLines = await api.getNumberOfLineOfCode(config);
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
