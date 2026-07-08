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

import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarApiException;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarClient;
import org.json.JSONObject;

/**
 * Java equivalent of {@code sonar.components.show.api.js}.
 *
 * <p>Calls {@code api/components/show} to retrieve project metadata (name, description, etc).</p>
 *
 * @see <a href="https://docs.sonarsource.com/sonarqube/latest/extension-guide/web-api/#api-components">SonarQube Components API</a>
 */
public class SonarProjectInfoApi {

    private static final String ROUTE = "api/components/show";

    private final SonarClient client;

    public SonarProjectInfoApi(SonarClient client) {
        this.client = client;
    }

    /**
     * Return project information (name, description, etc.) for a given project key.
     *
     * @param projectKey the SonarQube project key
     * @return the project component as a {@link JSONObject}
     * @throws SonarApiException if the API call fails
     */
    public JSONObject getProjectInfo(String projectKey) throws SonarApiException {
        Map<String, String> params = SonarClient.params();
        params.put("component", projectKey);

        JSONObject response = client.getJSON(ROUTE, params);
        return response.getJSONObject("component");
    }

    /**
     * Return the display name of a project for a given project key.
     *
     * @param projectKey the SonarQube project key
     * @return the project name, or the project key if name is not set
     * @throws SonarApiException if the API call fails
     */
    public String getProjectName(String projectKey) throws SonarApiException {
        JSONObject component = getProjectInfo(projectKey);
        return component.optString("name", projectKey);
    }
}
