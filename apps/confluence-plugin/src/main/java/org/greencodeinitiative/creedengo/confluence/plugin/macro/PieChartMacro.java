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

import java.util.Locale;
import java.util.Map;

import org.greencodeinitiative.creedengo.confluence.plugin.service.PieChartService;
import org.greencodeinitiative.creedengo.confluence.plugin.service.PieChartService.PieChartData;
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

/**
 * Confluence macro that renders a donut (pie) chart showing open SonarQube issues
 * filtered by a specific metric tag (e.g. CPU or RAM).
 *
 * <p>Each arc segment is proportional to the number of issues at that severity:</p>
 * <ul>
 *   <li>Yellow — minor / info</li>
 *   <li>Orange — major</li>
 *   <li>Red    — blocker / critical</li>
 *   <li>Green  — remaining (no open issues)</li>
 * </ul>
 */
public class PieChartMacro implements Macro {

    private static final Logger log = LoggerFactory.getLogger(PieChartMacro.class);

    private final PageBuilderService pageBuilderService;

    @Autowired
    public PieChartMacro(@ComponentImport PageBuilderService pageBuilderService) {
        this.pageBuilderService = pageBuilderService;
    }

    @Override
    public String execute(Map<String, String> map, String s, ConversionContext conversionContext)
            throws MacroExecutionException {
        MacroHelper.requireWebResources(pageBuilderService);
        log.info("creedengo : Executing PieChartMacro with parameters: {}", map.toString());

        String sonarqubeUrl = map.get("sonarqube_url");
        String sonarqubeToken = map.get("sonarqube_token");
        String projectKey = map.get("project_key");
        String branch = map.getOrDefault("branch", "main");
        String metricTag = map.getOrDefault("metric_tag", "cpu").toUpperCase(Locale.ROOT);

        StringBuilder output = new StringBuilder();

        if (sonarqubeUrl == null) {
            output.append("<h1>")
                  .append(MacroHelper.getText("piechart.header.emptyurl"))
                  .append("</h1>");
            return output.toString();
        }

        if (!MacroHelper.isValidUrl(sonarqubeUrl)) {
            log.warn("creedengo : Invalid SonarQube URL format: {}", sonarqubeUrl);
            output.append("<div class=\"aui-message aui-message-error\">")
                  .append("<p><strong>Erreur :</strong> L'URL SonarQube fournie est invalide : ")
                  .append(MacroHelper.escapeHtml(sonarqubeUrl))
                  .append("</p>")
                  .append("<p>Format attendu : <code>https://sonar.example.com</code></p>")
                  .append("</div>");
            return output.toString();
        }

        String serverStatus = MacroHelper.verifySonarServer(sonarqubeUrl, sonarqubeToken);
        if (serverStatus != null) {
            log.warn("creedengo : SonarQube server verification failed: {}", serverStatus);
            output.append("<div class=\"aui-message aui-message-warning\">")
                  .append("<p><strong>Attention :</strong> Impossible de joindre le serveur SonarQube.</p>")
                  .append("<p>").append(MacroHelper.escapeHtml(serverStatus)).append("</p>")
                  .append("</div>");
            return output.toString();
        }

        appendPieChartSection(output, sonarqubeUrl, sonarqubeToken, projectKey, branch, metricTag, true);
        return output.toString();
    }

    /**
     * Build and append the full PieChart HTML section to {@code output}.
     *
     * @param output        the string builder to append to
     * @param sonarqubeUrl  the SonarQube server base URL
     * @param sonarqubeToken the authentication token
     * @param projectKey    the SonarQube project key
     * @param branch        the branch name
     * @param metricTag     the metric tag (e.g. {@code "CPU"} or {@code "RAM"})
     * @param displayHeader whether to render the project header
     */
    public static void appendPieChartSection(StringBuilder output, String sonarqubeUrl,
            String sonarqubeToken, String projectKey, String branch, String metricTag, boolean displayHeader) {
        try {
            SonarService sonarService = new SonarService(sonarqubeUrl, sonarqubeToken);
            PieChartService pieChartService = new PieChartService(sonarService);
            PieChartData data = pieChartService.getData(projectKey, branch, metricTag);

            log.info("creedengo : PieChart data for project {} / tag {} — critical={} major={} minor={} nbRules={} optimized={}",
                    projectKey, metricTag,
                    data.criticalIssues(), data.majorIssues(), data.minorIssues(),
                    data.nbRules(), data.optimizedRules());

            output.append("<div class=\"creedengo-piechart-widget\">");

            if (displayHeader) {
                output.append("<h2>")
                      .append(MacroHelper.getText("piechart.header",
                              MacroHelper.escapeHtml(projectKey),
                              MacroHelper.escapeHtml(metricTag)))
                      .append("</h2>");
            }

            output.append("<div class=\"creedengo-piechart-body\">");
            output.append(buildDonutHtml(data));
            output.append(buildLegendHtml(data, metricTag));
            output.append("</div>"); // .creedengo-piechart-body
            output.append("</div>"); // .creedengo-piechart-widget

        } catch (SonarApiException e) {
            log.error("creedengo : Failed to retrieve PieChart data for project {}", projectKey, e);
            output.append("<div class=\"aui-message aui-message-error\">")
                  .append("<p><strong>Erreur :</strong> Impossible de récupérer les données SonarQube.</p>")
                  .append("<p>").append(MacroHelper.escapeHtml(e.getMessage())).append("</p>")
                  .append("</div>");
        }
    }

    // ------------------------------------------------------------------ helpers

    private static String buildDonutHtml(PieChartData data) {
        int total = data.totalIssues();
        String gradient = buildConicGradient(data.minorIssues(), data.majorIssues(), data.criticalIssues(), total);

        return "<div class=\"creedengo-donut\" style=\"background: " + gradient + "\">"
             + "<div class=\"creedengo-donut-inner\">"
             + "<div class=\"creedengo-donut-value\">" + total + "</div>"
             + "<div class=\"creedengo-donut-label\">" + MacroHelper.getText("piechart.donut.label") + "</div>"
             + "</div>"
             + "</div>";
    }

    private static String buildConicGradient(int minor, int major, int critical, int total) {
        if (total == 0) {
            return "conic-gradient(#6fcf47 0deg 360deg)";
        }
        double t = (double) total;
        double dMinor    = (minor    / t) * 360.0;
        double dMajor    = (major    / t) * 360.0;
        double dCritical = (critical / t) * 360.0;
        double stopMinor           = dMinor;
        double stopMajor           = dMinor + dMajor;
        double stopCritical        = dMinor + dMajor + dCritical;

        return String.format(Locale.US,
            "conic-gradient("
            + "#f2c94c 0deg %.2fdeg, "
            + "#f2994a %.2fdeg %.2fdeg, "
            + "#eb5757 %.2fdeg %.2fdeg, "
            + "#6fcf47 %.2fdeg 360deg"
            + ")",
            stopMinor,
            stopMinor, stopMajor,
            stopMajor, stopCritical,
            stopCritical);
    }

    private static String buildLegendHtml(PieChartData data, String metricTag) {
        StringBuilder legend = new StringBuilder();
        legend.append("<div class=\"creedengo-piechart-legend\">")
              .append("<div class=\"piechart-tag-badge\">").append(MacroHelper.escapeHtml(metricTag)).append("</div>")
              .append("<ul class=\"piechart-legend-list\">")
              .append(legendItem("#eb5757", MacroHelper.getText("piechart.legend.critical"), data.criticalIssues()))
              .append(legendItem("#f2994a", MacroHelper.getText("piechart.legend.major"),    data.majorIssues()))
              .append(legendItem("#f2c94c", MacroHelper.getText("piechart.legend.minor"),    data.minorIssues()))
              .append(legendItem("#6fcf47", MacroHelper.getText("piechart.legend.optimized"), data.optimizedRules()))
              .append("</ul>")
              .append("<div class=\"piechart-rules-total\">")
              .append(MacroHelper.getText("piechart.rules.total", String.valueOf(data.nbRules())))
              .append("</div>")
              .append("</div>");
        return legend.toString();
    }

    private static String legendItem(String color, String label, int count) {
        return "<li class=\"piechart-legend-item\">"
             + "<span class=\"piechart-legend-dot\" style=\"background:" + color + "\"></span>"
             + "<span class=\"piechart-legend-text\">" + MacroHelper.escapeHtml(label) + "</span>"
             + "<span class=\"piechart-legend-count\">" + count + "</span>"
             + "</li>";
    }

    @Override
    public BodyType getBodyType() {
        return BodyType.NONE;
    }

    @Override
    public OutputType getOutputType() {
        return OutputType.BLOCK;
    }
}
