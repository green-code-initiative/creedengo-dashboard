/**
 * @module score.services
 * @description This module provides functions to calculate the ABCDE score based on SonarQube issues.
 */

import api from './adapter.js';

const scoreTexts = {
    A: { 
        label: "Your app is fully optimized, congratulations!",
        description: "Don't forget to check it again if you update your app.",
        tips: "100 % optimized, congrats!"
    },
    B: { 
        label: "Your app is nearly optimized.",
        description: "Well done! You can continue by fixing the recommended rule on the right side. This is the one that currently has the highest impact on your app.",
        tips: "You have between 1 and 9 minor severities."
    },
    C: { 
        label: "Your app is not fully optimized.",
        description: "Keep going! You can continue by fixing the recommended rule on the right side. This is the one that currently has the highest impact on your app.", 
        tips: "You have between 10 and 19 minor severities or you have 1 or many major severity."
    },
    D: { 
        label: "Many elements of your application can be optimized.",
        description: "Don't worry! You can start by fixing the recommended rule on the right side. This is the one that currently has the highest impact on your app.", 
        tips: "You have more than 20 minor severities or more than 10 major severities or 1 or many critical severities."
    },
    E: {
        label: "Several elements of your application can be optimized.",
        description: "Don't worry! You can start by fixing the recommended rule on the right side. This is the one that currently has the highest impact on your app.", 
        tips: "You have 1 or more than 1 blocker severities."
    }
};

/**
 * Calculate an ABCDE score from the sustainability issues related to the project size.
 * @param {Object} config
 * @param {string} config.project Project Key
 * @param {string} config.branch Git Branch Name.
 * @returns {Promise<string>} Score between A and E.
 */
export async function calculateProjectScore(config) {

    const severityFacets = await api.services.getIssuesFacet('severities', config);
    const { info = 0, minor = 0, major = 0, critical = 0, blocker = 0 } = severityFacets;
    const consolidatedMinors = info + minor;

    const numberOfLines = await api.services.getNumberOfLineOfCode(config);
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

/**
 * Returns the texts corresponding to the score.
 * @param {string} score the value of the project's score.
 * @returns {Object} texts corresponding to the project score.
 */
export function getScoreTexts(score) {
  return scoreTexts[score]
}
