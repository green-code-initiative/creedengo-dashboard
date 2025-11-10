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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarApiException;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarClient;

/**
 * Java equivalent of {@code sonar.project-branches.list.api.js}.
 *
 * <p>Calls {@code api/project_branches/list} to retrieve branches of a project.</p>
 *
 * @see <a href="https://docs.sonarsource.com/sonarqube/latest/extension-guide/web-api/#api-project_branches">SonarQube Project Branches API</a>
 */
public class SonarProjectBranchesApi {

    private static final String ROUTE = "api/project_branches/list";

    private final SonarClient client;

    public SonarProjectBranchesApi(SonarClient client) {
        this.client = client;
    }

    /**
     * Get the list of branches for a given project.
     *
     * @param project the SonarQube project key
     * @return a list of branches as {@link JSONObject}
     * @throws SonarApiException if the API call fails
     */
    public List<JSONObject> getProjectBranches(String project) throws SonarApiException {
        Map<String, String> params = SonarClient.params();
        params.put("project", project);

        JSONObject response = client.getJSON(ROUTE, params);
        JSONArray branches = response.optJSONArray("branches");

        List<JSONObject> result = new ArrayList<>();
        if (branches != null) {
            for (int i = 0; i < branches.length(); i++) {
                result.add(branches.getJSONObject(i));
            }
        }
        return result;
    }
}
