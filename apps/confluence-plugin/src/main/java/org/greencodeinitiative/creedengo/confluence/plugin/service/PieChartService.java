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
package org.greencodeinitiative.creedengo.confluence.plugin.service;

import java.util.List;
import java.util.Map;

import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarApiException;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarService;
import org.json.JSONObject;

/**
 * Retrieves all data needed to render a PieChart (donut) widget for a given metric tag.
 *
 * <p>Supported metric tags are typically {@code cpu} and {@code ram}, corresponding to
 * SonarQube rule tags that categorize sustainability issues by resource consumption.</p>
 */
public class PieChartService {

    private final SonarService sonarService;

    public PieChartService(SonarService sonarService) {
        this.sonarService = sonarService;
    }

    /**
     * All data required to render a PieChart donut for a given tag.
     */
    public static final class PieChartData {
        private final int criticalIssues;
        private final int majorIssues;
        private final int minorIssues;
        private final int nbRules;
        private final int optimizedRules;

        public PieChartData(int criticalIssues, int majorIssues, int minorIssues, int nbRules, int optimizedRules) {
            this.criticalIssues = criticalIssues;
            this.majorIssues = majorIssues;
            this.minorIssues = minorIssues;
            this.nbRules = nbRules;
            this.optimizedRules = optimizedRules;
        }

        public int criticalIssues()  { return criticalIssues; }
        public int majorIssues()     { return majorIssues; }
        public int minorIssues()     { return minorIssues; }
        public int nbRules()         { return nbRules; }
        public int optimizedRules()  { return optimizedRules; }

        public int totalIssues() {
            return criticalIssues + majorIssues + minorIssues;
        }
    }

    /**
     * Fetch PieChart data for the given project, branch, and metric tag.
     *
     * @param project   the SonarQube project key
     * @param branch    the branch name
     * @param metricTag the tag to filter on (e.g. {@code "CPU"} or {@code "RAM"})
     * @return a {@link PieChartData} instance with all aggregated counts
     * @throws SonarApiException if any SonarQube API call fails
     */
    public PieChartData getData(String project, String branch, String metricTag) throws SonarApiException {
        String tag = metricTag.toLowerCase();

        // 1. Severity facets filtered by the specific metric tag
        Map<String, Integer> severities = sonarService.issues().getIssuesFacetByTag("severities", tag, project, branch);
        int criticalIssues = severities.getOrDefault("critical", 0) + severities.getOrDefault("blocker", 0);
        int majorIssues = severities.getOrDefault("major", 0);
        int minorIssues = severities.getOrDefault("minor", 0) + severities.getOrDefault("info", 0);

        // 2. Total rules for this tag
        List<JSONObject> rules = sonarService.rules().findRulesByTag(tag);
        int nbRules = rules.size();

        // 3. Rules with at least 1 open issue -> infer optimized rules count
        Map<String, Integer> issuesByRule = sonarService.issues().getIssueCountByRuleKeyByTag(tag, project, branch);
        long rulesWithIssues = issuesByRule.values().stream().filter(count -> count > 0).count();
        int optimizedRules = Math.max(0, nbRules - (int) rulesWithIssues);

        return new PieChartData(criticalIssues, majorIssues, minorIssues, nbRules, optimizedRules);
    }
}
