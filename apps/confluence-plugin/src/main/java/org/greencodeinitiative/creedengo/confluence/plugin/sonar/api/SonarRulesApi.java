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

import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarApiException;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarClient;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarConstants;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Java equivalent of {@code sonar.rules.search.api.js} and {@code sonar.rules.show.api.js}.
 *
 * <p>Calls {@code api/rules/search} and {@code api/rules/show}.</p>
 *
 * @see <a href="https://docs.sonarsource.com/sonarqube/latest/extension-guide/web-api/#api-rules">SonarQube Rules API</a>
 */
public class SonarRulesApi {

    private static final String SEARCH_ROUTE = "api/rules/search";
    private static final String SHOW_ROUTE = "api/rules/show";

    private final SonarClient client;

    public SonarRulesApi(SonarClient client) {
        this.client = client;
    }

    /**
     * Return SonarQube sustainability rules.
     * Only gathers rules tagged with sustainability tags.
     *
     * @param project the SonarQube project key
     * @param branch  the branch name
     * @return a list of rules as {@link JSONObject}
     * @throws SonarApiException if the API call fails
     */
    public List<JSONObject> findRules(String project, String branch) throws SonarApiException {
        Map<String, String> params = SonarClient.params();
        params.put("tags", SonarConstants.SUSTAINABILITY_TAGS);
        params.put("componentKeys", project);
        params.put("branch", branch);

        JSONObject response = client.getJSON(SEARCH_ROUTE, params);
        JSONArray rules = response.optJSONArray("rules");

        List<JSONObject> result = new ArrayList<>();
        if (rules != null) {
            for (int i = 0; i < rules.length(); i++) {
                result.add(rules.getJSONObject(i));
            }
        }
        return result;
    }

    /**
     * Return details about a specific rule.
     *
     * @param ruleKey the rule key (e.g. {@code "java:S1234"})
     * @return the rule details as a {@link JSONObject}
     * @throws SonarApiException if the API call fails
     */
    public JSONObject getRuleDetails(String ruleKey) throws SonarApiException {
        Map<String, String> params = SonarClient.params();
        params.put("rule_key", ruleKey);

        JSONObject response = client.getJSON(SHOW_ROUTE, params);
        return response.getJSONObject("rule");
    }

    /**
     * Return SonarQube rules for a specific tag (e.g. {@code "cpu"} or {@code "ram"}).
     *
     * @param tag the exact tag to filter on
     * @return a list of rules as {@link JSONObject}
     * @throws SonarApiException if the API call fails
     */
    public List<JSONObject> findRulesByTag(String tag) throws SonarApiException {
        Map<String, String> params = SonarClient.params();
        params.put("tags", tag);

        JSONObject response = client.getJSON(SEARCH_ROUTE, params);
        JSONArray rules = response.optJSONArray("rules");

        List<JSONObject> result = new ArrayList<>();
        if (rules != null) {
            for (int i = 0; i < rules.length(); i++) {
                result.add(rules.getJSONObject(i));
            }
        }
        return result;
    }
}
