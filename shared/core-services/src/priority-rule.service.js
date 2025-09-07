/**
 * @module priority-rule.services
 * @description This module provides functions to recommend a priority rule to fix.
 */

import api from './adapter.js';

/**
 * @typedef {Object} DescriptionSection
 * @property {string} key
 * @property {string} content
 * @property {string} [context]
 */

/**
 * @typedef {Object} RuleDetails
 * @property {string} key
 * @property {string} name
 * @property {string} htmlDesc
 * @property {'INFO'|'MINOR'|'MAJOR'|'CRITICAL'|'BLOCKER'} severity
 * @property {string[]} tags
 * @property {string} langName
 * @property {DescriptionSection[]} descriptionSections
 */

/**
 * @param {Object} ruleFacet
 * @returns {string}
 */
function getMostIssuesRule(ruleFacet) {
    let ruleKey = null
    let max = 0
    for (let [key, value] of Object.entries(ruleFacet)) {
        if (value > max) {
            max = value
            ruleKey = key 
        }
    }
    return ruleKey
}

const SEVERITY_LEVELS = [
    'BLOCKER',
    'CRITICAL',
    'MAJOR',
    'MINOR,INFO'
]

/**
 * Select a PriorityRule based the score calculation algorithm
 * 
 * @param {Object} config
 * @param {string} config.project key of the project
 * @param {string} config.branch 
 * @returns {Promise<RuleDetails|null>}
 */
export async function getPriorityRule(config) {
    let ruleKey;

    for (let severity of SEVERITY_LEVELS) {
        const ruleFacet = await api.services.getIssuesFacet('rules', { ...config, severity });
        ruleKey = getMostIssuesRule(ruleFacet)
        if (ruleKey) {
            break
        }
    }

    const ruleDetails = ruleKey ? 
        await api.services.getRuleDetails(ruleKey) :
        null
    return ruleDetails;
}
