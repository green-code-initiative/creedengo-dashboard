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

import org.greencodeinitiative.creedengo.confluence.plugin.service.ScoreService;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarApiException;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.webresource.api.assembler.PageBuilderService;

public class EcoScore implements Macro {

    private final PageBuilderService pageBuilderService;
    private static final Logger log = LoggerFactory.getLogger(EcoScore.class);
    private static final String[] SCORE_LETTERS = {"A", "B", "C", "D", "E"};

    @Autowired
    public EcoScore(@ComponentImport PageBuilderService pageBuilderService) {
        this.pageBuilderService = pageBuilderService;
    }

    @Override
    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        MacroHelper.requireWebResources(pageBuilderService);
        log.info("creedengo : Executing EcoScore macro with parameters: {}", map.toString());

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

                    appendScoreSection(output, sonarqubeUrl, sonarqubeToken, projectKey, branch, true);
                }
            }
        } else {
            output.append("<h1>")
                  .append(MacroHelper.getText("ecoscore.header.emptyurl"))
                  .append("</h1>");
        }
        return output.toString();
    }

    public static void appendScoreSection(StringBuilder output, String sonarqubeUrl,
                                     String sonarqubeToken, String projectKey, String branch, boolean displayHeader) {
        try {
            SonarService sonarService = new SonarService(sonarqubeUrl, sonarqubeToken);
            ScoreService scoreService = new ScoreService(sonarService);
            String score = scoreService.calculateProjectScore(projectKey, branch);
            ScoreService.ScoreTexts texts = ScoreService.getScoreTexts(score);
            log.info("creedengo : Calculated score {} for project {} on branch {}", score, projectKey, branch);

            output.append("<div class=\"creedengo-score-widget\">");
            if (displayHeader) {
                output.append("<h2>").append(MacroHelper.getText("ecoscore.header", MacroHelper.escapeHtml(projectKey))).append("</h2>");
            }
                                
            output.append("<h2 class=\"creedengo-score-title\">").append(MacroHelper.getText("ecoscore.subheader")).append("</h2>")
                .append("<div class=\"creedengo-rate-list\">");
            for (String letter : SCORE_LETTERS) {
                boolean isActive = letter.equals(score);
                if (isActive) {
                    output.append("<span class=\"creedengo-rate creedengo-rate-")
                          .append(letter.toLowerCase())
                          .append("\"><strong>")
                          .append(letter)
                          .append("</strong></span>");
                }
                output.append("<span class=\"creedengo-rate-item creedengo-rate-bg-")
                      .append(letter.toLowerCase())
                      .append("\">")
                      .append(letter)
                      .append("</span>");
            }
            output.append("</div>");
            if (texts != null) {
                output.append("<div class=\"creedengo-score-texts\">")
                      .append("<p class=\"creedengo-score-label\">").append(MacroHelper.escapeHtml(texts.getLabel())).append("</p>")
                      .append("<p class=\"creedengo-score-desc\">").append(MacroHelper.escapeHtml(texts.getDescription())).append("</p>")
                      .append("<p class=\"creedengo-score-tips\"><em>").append(MacroHelper.escapeHtml(texts.getTips())).append("</em></p>")
                      .append("</div>");
            }
            output.append("</div>");
        } catch (SonarApiException e) {
            log.error("creedengo : Failed to calculate score for project {}", projectKey, e);
            output.append("<div class=\"aui-message aui-message-warning\">")
                  .append("<p><strong>Attention :</strong> Impossible de calculer le score Creedengo.</p>")
                  .append("<p>").append(MacroHelper.escapeHtml(e.getMessage())).append("</p>")
                  .append("</div>");
        }
    }

    @Override
    public Macro.BodyType getBodyType() { return Macro.BodyType.NONE; }

    @Override
    public Macro.OutputType getOutputType() { return Macro.OutputType.BLOCK; }
}
