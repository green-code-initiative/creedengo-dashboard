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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.webresource.api.assembler.PageBuilderService;

public class Dashboard implements Macro {

    private final PageBuilderService pageBuilderService;
    private static final Logger log = LoggerFactory.getLogger(Dashboard.class);

    @Autowired
    public Dashboard(@ComponentImport PageBuilderService pageBuilderService) {
        this.pageBuilderService = pageBuilderService;
    }

    @Override
    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        MacroHelper.requireWebResources(pageBuilderService);
        log.info("creedengo : Executing Dashboard macro with parameters: {}", map.toString());

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
                    output.append("<div class=\"creedengo-dashboard\">");
                    
                    // Retrieve project name from SonarQube
                    String projectName = projectKey; // Default to key
                    try {
                        org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarService sonarService = 
                            new org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarService(sonarqubeUrl, sonarqubeToken);
                        projectName = sonarService.projectInfo().getProjectName(projectKey);
                    } catch (Exception e) {
                        log.warn("creedengo : Failed to retrieve project name from SonarQube, using project key instead: {}", projectKey, e);
                    }
                    
                    output.append("<h1>Creedengo Dashboard of project ").append(MacroHelper.escapeHtml(projectName)).append("!</h1>");
                    output.append("<div class=\"creedengo-dashboard-columns\">");
                    
                    output.append("<div class=\"creedengo-dashboard-column\">");
                    EcoScore.appendScoreSection(output, sonarqubeUrl, sonarqubeToken, projectKey, branch, false);
                    output.append("</div>");
                    
                    output.append("<div class=\"creedengo-dashboard-column\">");
                    PriorityRulesMacro.appendPriorityRulesSection(output, sonarqubeUrl, sonarqubeToken, projectKey, branch, false);
                    output.append("</div>");
                    
                    output.append("</div>");
                    output.append("</div>");
                }
            }
        } else {
            output.append("<h1>Dashboard Creedengo! empty sonarqube_url</h1>");
        }
        return output.toString();
    }

    @Override
    public Macro.BodyType getBodyType() { return Macro.BodyType.NONE; }

    @Override
    public Macro.OutputType getOutputType() { return Macro.OutputType.BLOCK; }
}
