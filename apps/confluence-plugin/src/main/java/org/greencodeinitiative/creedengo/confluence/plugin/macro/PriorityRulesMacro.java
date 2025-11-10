/*
* Creedengo Dashboard plugin - Provides dashboard to monitor and help reduce the environmental footprint of your programs
* Copyright © 2025 Green Code Initiative (https://green-code-initiative.org)
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package org.greencodeinitiative.creedengo.confluence.plugin.macro;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.greencodeinitiative.creedengo.confluence.plugin.service.PriorityRulesService;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarApiException;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.webresource.api.assembler.PageBuilderService;

public class PriorityRulesMacro implements Macro {
    
    private final PageBuilderService pageBuilderService;
    private static final Logger log = LoggerFactory.getLogger(PriorityRulesMacro.class);

    
    @Autowired
    public PriorityRulesMacro(@ComponentImport PageBuilderService pageBuilderService) {
        this.pageBuilderService = pageBuilderService;
    }
    
    @Override
    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        MacroHelper.requireWebResources(pageBuilderService);
        log.info("creedengo : Executing PriorityRulesMacro with parameters: {}", map.toString());
        
        String sonarqubeUrl = map.get("sonarqube_url");
        String sonarqubeToken = map.get("sonarqube_token");
        String projectKey = map.get("project_key");
        String branch = map.getOrDefault("branch", "main");
        
        StringBuilder output = new StringBuilder();
        if (sonarqubeUrl != null) {
            if (!MacroHelper.isValidUrl(sonarqubeUrl)) {
                log.warn("creedengo : Invalid SonarQube URL format: {}", sonarqubeUrl);
                output.append("<div class=\"aui-message aui-message-error\">")
                .append("<p><strong>Erreur :</strong> L'URL SonarQube fournie est invalide : ")
                .append(MacroHelper.escapeHtml(sonarqubeUrl))
                .append("</p>")
                .append("<p>Format attendu : <code>https://sonar.example.com</code></p>")
                .append("</div>");
            } else {
                String serverStatus = MacroHelper.verifySonarServer(sonarqubeUrl, sonarqubeToken);
                if (serverStatus != null) {
                    log.warn("creedengo : SonarQube server verification failed: {}", serverStatus);
                    output.append("<div class=\"aui-message aui-message-warning\">")
                    .append("<p><strong>Attention :</strong> Impossible de joindre le serveur SonarQube.</p>")
                    .append("<p>").append(MacroHelper.escapeHtml(serverStatus)).append("</p>")
                    .append("</div>");
                } else {

                    appendPriorityRulesSection(output, sonarqubeUrl, sonarqubeToken, projectKey, branch, true);
                }
            }
        } else {
            output.append("<h1>")
            .append(MacroHelper.getText("priorityrules.header.emptyurl"))
            .append("</h1>");
        }
        return output.toString();
    }
    
    public static void appendPriorityRulesSection(StringBuilder output, String sonarqubeUrl,
        String sonarqubeToken, String projectKey, String branch, boolean displayHeader) {
            try {
                SonarService sonarService = new SonarService(sonarqubeUrl, sonarqubeToken);
                PriorityRulesService priorityRulesService = new PriorityRulesService(sonarService);
                log.info("creedengo : Retrieve rules for project {} on branch {}", projectKey, branch);
                Map<String, List<JSONObject>>rulesByPriority = priorityRulesService.getRulesByPriority(projectKey, branch);
                
                // Collect all rules with at least 1 issue, sorted by priority then by issueCount
                List<JSONObject> allRulesWithIssues = new ArrayList<>();
                String[] priorityOrder = {"blocker", "critical", "major", "minor", "info"};
                
                for (String priority : priorityOrder) {
                    List<JSONObject> rules = rulesByPriority.get(priority);
                    if (rules != null) {
                        for (JSONObject rule : rules) {
                            if (rule.optInt("issueCount", 0) > 0) {
                                rule.put("_priorityRank", getPriorityRank(priority));
                                allRulesWithIssues.add(rule);
                            }
                        }
                    }
                }
                
                // Sort by priority rank (lower = more critical), then by issueCount descending
                allRulesWithIssues.sort(Comparator
                    .comparingInt((JSONObject r) -> r.optInt("_priorityRank", 999))
                    .thenComparingInt((JSONObject r) -> -r.optInt("issueCount", 0)));
                
                // Keep only top 3
                List<JSONObject> topRules = allRulesWithIssues.subList(0, Math.min(3, allRulesWithIssues.size()));
                
                output.append("<div class='creedengo-priority-macro'>");
                
                if (topRules.isEmpty()) {
                    output.append("<div class='priority-info'>");
                    output.append("<p>Aucune règle de durabilité détectée sur ce projet.</p>");
                    output.append("</div>");
                } else {
                                        // i18n externalisation du label principal
                    output.append("<div class='priority-header'>");
                    if (displayHeader) {
                        output.append("<h1>").append(MacroHelper.getText("priorityrules.header", MacroHelper.escapeHtml(projectKey))).append("</h1>");
                    }
                    output.append("<h3>Top ").append(topRules.size()).append(" règles critiques</h3>");
                    output.append("</div>");
                    output.append("<div class='priority-items'>");
                    
                    int rank = 1;
                    for (JSONObject rule : topRules) {
                        String name = rule.optString("name", "");
                        String key = rule.optString("key", "");
                        String severity = rule.optString("severity", "info").toLowerCase();
                        int issueCount = rule.optInt("issueCount", 0);
                        String description = rule.optString("htmlDesc", "");
                        
                        output.append("<div class='priority-item rank-").append(rank).append(" severity-").append(severity).append("'>");
                        output.append("<div class='priority-rank'>").append(rank).append("</div>");
                        output.append("<div class='priority-content'>");
                        output.append("<div class='priority-title'>").append(MacroHelper.escapeHtml(name)).append("</div>");
                        output.append("<div class='priority-meta'>");
                        output.append("<span class='rule-key'>[").append(key).append("]</span>");
                        output.append("<span class='rule-severity severity-").append(severity).append("'>").append(severity.toUpperCase()).append("</span>");
                        output.append("<span class='rule-count'>").append(issueCount).append(" issue(s)</span>");
                        output.append("</div>");
                        if (!description.isEmpty()) {
                            output.append("<div class='priority-desc'>").append(description).append("</div>");
                        }
                        output.append("</div>");
                        output.append("</div>");
                        rank++;
                    }
                    
                    output.append("</div>");
                }
                
                output.append("</div>");
            } catch (SonarApiException e) {
                log.error("creedengo : Failed to get priority rules for project {}", projectKey, e);
                output.append("<div class=\"aui-message aui-message-warning\">")
                .append("<p><strong>Attention :</strong> Impossible de calculer les règles de priorité Creedengo.</p>")
                .append("<p>").append(MacroHelper.escapeHtml(e.getMessage())).append("</p>")
                .append("</div>");
            }
        }
        
        /**
         * Return a numeric rank for a priority level (lower = more critical).
         */
        private static int getPriorityRank(String priority) {
            switch (priority.toLowerCase()) {
                case "blocker":
                    return 0;
                case "critical":
                    return 1;
                case "major":
                    return 2;
                case "minor":
                    return 3;
                case "info":
                    return 4;
                default:
                    return 999;
            }
        }

        
        @Override
        public Macro.BodyType getBodyType() { return Macro.BodyType.NONE; }
        
        @Override
        public Macro.OutputType getOutputType() { return Macro.OutputType.BLOCK; }
    }
