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
package org.greencodeinitiative.creedengo.confluence.plugin.sonar.api;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarApiException;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarClient;

/**
 * Java equivalent of {@code sonar.measures.component.api.js}.
 *
 * <p>Calls {@code api/measures/component} to retrieve project metrics.</p>
 *
 * @see <a href="https://docs.sonarsource.com/sonarqube/latest/extension-guide/web-api/#api-measures">SonarQube Measures API</a>
 */
public class SonarMeasuresApi {

    private static final String ROUTE = "api/measures/component";

    private final SonarClient client;

    public SonarMeasuresApi(SonarClient client) {
        this.client = client;
    }

    /**
     * Return the number of lines of code (ncloc metric) for a project.
     *
     * @param project the SonarQube project key
     * @param branch  the branch name
     * @return the number of lines of code, or 0 if unavailable
     * @throws SonarApiException if the API call fails
     */
    public long getNumberOfLineOfCode(String project, String branch) throws SonarApiException {
        Map<String, String> params = SonarClient.params();
        params.put("component", project);
        params.put("branch", branch);
        params.put("metricKeys", "ncloc");

        JSONObject response = client.getJSON(ROUTE, params);

        // Navigate: response.component.measures[0].value
        JSONObject component = response.optJSONObject("component");
        if (component == null) {
            return 0;
        }
        JSONArray measures = component.optJSONArray("measures");
        if (measures == null || measures.length() == 0) {
            return 0;
        }
        JSONObject firstMeasure = measures.getJSONObject(0);
        String value = firstMeasure.optString("value", null);
        if (value == null) {
            return 0;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
