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

import java.util.Map;

import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarApiException;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarService;

/**
 * Java equivalent of {@code score.service.js}.
 *
 * <p>Calculates an ABCDE sustainability score for a SonarQube project based on
 * the number and severity of sustainability-tagged issues relative to the
 * project size (lines of code).</p>
 *
 * <h3>Score rules</h3>
 * <ul>
 *   <li><strong>E</strong> – 1 or more blocker severities</li>
 *   <li><strong>D</strong> – minor ratio &ge; 8%, or &ge; 10 major, or &ge; 1 critical</li>
 *   <li><strong>C</strong> – &ge; 10 consolidated minors, or &ge; 1 major</li>
 *   <li><strong>B</strong> – &ge; 1 consolidated minor</li>
 *   <li><strong>A</strong> – fully optimized (no issues)</li>
 * </ul>
 */
public class ScoreService {

    private final SonarService sonarService;

    public ScoreService(SonarService sonarService) {
        this.sonarService = sonarService;
    }

    /**
     * Calculate the ABCDE score for the given project and branch.
     *
     * <p>This is a direct port of the JavaScript {@code calculateProjectScore}
     * function from {@code score.service.js}.</p>
     *
     * @param project the SonarQube project key
     * @param branch  the Git branch name
     * @return a score character: A, B, C, D, or E
     * @throws SonarApiException if a SonarQube API call fails
     */
    public String calculateProjectScore(String project, String branch) throws SonarApiException {
        // 1. Get severity facets (equivalent of getIssuesFacet('severities', config))
        Map<String, Integer> severityFacets = sonarService.issues()
                .getIssuesFacet("severities", project, branch, null);

        int info = severityFacets.getOrDefault("info", 0);
        int minor = severityFacets.getOrDefault("minor", 0);
        int major = severityFacets.getOrDefault("major", 0);
        int critical = severityFacets.getOrDefault("critical", 0);
        int blocker = severityFacets.getOrDefault("blocker", 0);

        int consolidatedMinors = info + minor;

        // 2. Get number of lines of code
        long numberOfLines = sonarService.measures().getNumberOfLineOfCode(project, branch);

        // 3. Calculate minor ratio (guard against division by zero)
        double minorRatio = numberOfLines > 0
                ? (double) consolidatedMinors / numberOfLines
                : 0.0;

        // 4. Apply score rules (same order as the JavaScript implementation)
        if (blocker >= 1) {
            return "E";
        }
        if (minorRatio >= 0.08 || major >= 10 || critical >= 1) {
            return "D";
        }
        if (consolidatedMinors >= 10 || major >= 1) {
            return "C";
        }
        if (consolidatedMinors >= 1) {
            return "B";
        }
        return "A";
    }

    /**
     * Description texts associated with each score level.
     *
     * <p>Direct port of the {@code scoreTexts} constant from
     * {@code score.service.js}.</p>
     *
     * @param score the score character (A–E)
     * @return a {@link ScoreTexts} record, or {@code null} if the score is unknown
     */
    public static ScoreTexts getScoreTexts(String score) {
        if (score == null) {
            return null;
        }
        switch (score) {
            case "A":
                return new ScoreTexts(
                        "Your app is fully optimized, congratulations!",
                        "Don't forget to check it again if you update your app.",
                        "100 % optimized, congrats!"
                );
            case "B":
                return new ScoreTexts(
                        "Your app is nearly optimized.",
                        "Well done! You can continue by fixing the recommended rule on the right side. "
                                + "This is the one that currently has the highest impact on your app.",
                        "You have between 1 and 9 minor severities."
                );
            case "C":
                return new ScoreTexts(
                        "Your app is not fully optimized.",
                        "Keep going! You can continue by fixing the recommended rule on the right side. "
                                + "This is the one that currently has the highest impact on your app.",
                        "You have between 10 and 19 minor severities or you have 1 or many major severity."
                );
            case "D":
                return new ScoreTexts(
                        "Many elements of your application can be optimized.",
                        "Don't worry! You can start by fixing the recommended rule on the right side. "
                                + "This is the one that currently has the highest impact on your app.",
                        "You have more than 20 minor severities or more than 10 major severities "
                                + "or 1 or many critical severities."
                );
            case "E":
                return new ScoreTexts(
                        "Several elements of your application can be optimized.",
                        "Don't worry! You can start by fixing the recommended rule on the right side. "
                                + "This is the one that currently has the highest impact on your app.",
                        "You have 1 or more than 1 blocker severities."
                );
            default:
                return null;
        }
    }

    /**
     * Holds the label, description and tips for a given score.
     */
    public static class ScoreTexts {
        private final String label;
        private final String description;
        private final String tips;

        public ScoreTexts(String label, String description, String tips) {
            this.label = label;
            this.description = description;
            this.tips = tips;
        }

        public String getLabel() {
            return label;
        }

        public String getDescription() {
            return description;
        }

        public String getTips() {
            return tips;
        }
    }
}
