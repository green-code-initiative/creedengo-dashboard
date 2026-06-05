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
 * Java equivalent of {@code sonar.project-pull-requests.list.api.js}.
 *
 * <p>Calls {@code api/project_pull_requests/list} to retrieve pull requests of a project.</p>
 *
 * @see <a href="https://docs.sonarsource.com/sonarqube/latest/extension-guide/web-api/#api-project_pull_requests">SonarQube Project Pull Requests API</a>
 */
public class SonarProjectPullRequestsApi {

    private static final String ROUTE = "api/project_pull_requests/list";

    private final SonarClient client;

    public SonarProjectPullRequestsApi(SonarClient client) {
        this.client = client;
    }

    /**
     * Get the list of pull requests for a given project.
     *
     * @param project the SonarQube project key
     * @return a list of pull requests as {@link JSONObject}
     * @throws SonarApiException if the API call fails
     */
    public List<JSONObject> getProjectPullRequests(String project) throws SonarApiException {
        Map<String, String> params = SonarClient.params();
        params.put("project", project);

        JSONObject response = client.getJSON(ROUTE, params);
        JSONArray pullRequests = response.optJSONArray("pullRequests");

        List<JSONObject> result = new ArrayList<>();
        if (pullRequests != null) {
            for (int i = 0; i < pullRequests.length(); i++) {
                result.add(pullRequests.getJSONObject(i));
            }
        }
        return result;
    }
}
