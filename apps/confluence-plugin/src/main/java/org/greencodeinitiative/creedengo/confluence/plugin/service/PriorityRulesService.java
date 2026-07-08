/*
 * Creedengo Dashboard plugin - Service de récupération des règles par priorité
 * Copyright © 2025 Green Code Initiative
 */
package org.greencodeinitiative.creedengo.confluence.plugin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarApiException;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarService;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.api.SonarIssuesApi;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.api.SonarRulesApi;
import org.json.JSONObject;

/**
 * Service pour récupérer les règles SonarQube groupées par priorité (severity),
 * enrichies du nombre d'issues ouvertes associées.
 *
 * <p>Les règles sont filtrées via les tags de durabilité définis dans
 * {@code SonarConstants.SUSTAINABILITY_TAGS} (sustainability, greensight, ecocode, creedengo).
 * À l'intérieur de chaque groupe de sévérité, les règles sont triées par nombre
 * d'issues décroissant.</p>
 */
public class PriorityRulesService {

    private final SonarRulesApi sonarRulesApi;
    private final SonarIssuesApi sonarIssuesApi;

    public PriorityRulesService(SonarService sonarService) {
        this.sonarRulesApi = sonarService.rules();
        this.sonarIssuesApi = sonarService.issues();
    }

    /**
     * Retourne une map des règles groupées par priorité (severity), chaque règle
     * étant enrichie d'un champ {@code issueCount} indiquant le nombre d'issues
     * ouvertes associées dans le projet.
     *
     * <p>Les règles sont triées par {@code issueCount} décroissant à l'intérieur
     * de chaque groupe de sévérité.</p>
     *
     * @param project le projectKey SonarQube
     * @param branch  la branche
     * @return {@code Map<severity, List<JSONObject>>} — chaque JSONObject
     *         contient les champs de la règle plus {@code issueCount}
     * @throws SonarApiException en cas d'erreur API
     */
    public Map<String, List<JSONObject>> getRulesByPriority(String project, String branch) throws SonarApiException {
        List<JSONObject> rules = sonarRulesApi.findRules(project, branch);
        Map<String, Integer> issueCountByRule = sonarIssuesApi.getIssueCountByRuleKey(project, branch);

        Map<String, List<JSONObject>> byPriority = new HashMap<>();
        for (JSONObject rule : rules) {
            String severity = rule.optString("severity", "info").toLowerCase();
            String ruleKey = rule.optString("key", "");
            int issueCount = issueCountByRule.getOrDefault(ruleKey, 0);
            rule.put("issueCount", issueCount);
            byPriority.computeIfAbsent(severity, k -> new ArrayList<>()).add(rule);
        }

        // Sort rules within each severity by issueCount descending
        for (List<JSONObject> ruleList : byPriority.values()) {
            ruleList.sort((a, b) -> Integer.compare(b.optInt("issueCount", 0), a.optInt("issueCount", 0)));
        }

        return byPriority;
    }
}