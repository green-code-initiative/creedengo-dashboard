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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarApiException;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarClient;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarConstants;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Java equivalent of {@code sonar.issues.search.api.js}.
 *
 * <p>Calls {@code api/issues/search} to retrieve sustainability-tagged issues
 * that are not closed.</p>
 *
 * @see <a href="https://docs.sonarsource.com/sonarqube/latest/extension-guide/web-api/#api-issues">SonarQube Issues API</a>
 */
public class SonarIssuesApi {

    private static final String ROUTE = "api/issues/search";

    private final SonarClient client;

    public SonarIssuesApi(SonarClient client) {
        this.client = client;
    }

    /**
     * Return issues detected by SonarQube.
     * Only gathers sustainability-tagged issues that are NOT closed.
     *
     * @param project the SonarQube project key
     * @param branch  the branch name
     * @return a list of issues as {@link JSONObject}
     * @throws SonarApiException if the API call fails
     */
    public List<JSONObject> findIssues(String project, String branch) throws SonarApiException {
        Map<String, String> params = buildSustainabilityParams();
        params.put("componentKeys", project);
        params.put("branch", branch);
        
        JSONObject response = client.getJSON(ROUTE, params);
        JSONArray issues = response.optJSONArray("issues");

        List<JSONObject> result = new ArrayList<>();
        if (issues != null) {
            for (int i = 0; i < issues.length(); i++) {
                result.add(issues.getJSONObject(i));
            }
        }
        return result;
    }

    /**
     * Get an issues facet for a given project, branch, and facet name.
     *
     * <p>Equivalent of the JavaScript {@code getIssuesFacet} function.
     * Returns a map where keys are facet values (lowercased) and values are counts.</p>
     *
     * @param facetName the facet to retrieve (e.g. {@code "severities"}, {@code "types"})
     * @param project   the SonarQube project key
     * @param branch    the branch name
     * @param severity  optional severity filter (may be {@code null})
     * @return a map of facet value → count
     * @throws SonarApiException if the API call fails or the facet is not found
     */
    public Map<String, Integer> getIssuesFacet(String facetName, String project, String branch, String severity)
            throws SonarApiException {

        Map<String, String> params = buildSustainabilityParams();
        params.put("componentKeys", project);
        params.put("branch", branch);
        params.put("facets", facetName);
        params.put("ps", "100"); // no issues parsing, we only want the facets
        if (severity != null && !severity.isEmpty()) {
            params.put("severity", severity);
        }

        JSONObject response = client.getJSON(ROUTE, params);
        JSONArray facets = response.optJSONArray("facets");

        if (facets == null) {
            throw new SonarApiException("No facets returned by SonarQube for route " + ROUTE);
        }

        // Find the facet matching the requested name
        JSONObject matchingFacet = null;
        for (int i = 0; i < facets.length(); i++) {
            JSONObject facetObj = facets.getJSONObject(i);
            String property = facetObj.optString("property", "");
            if (facetName.equals(property)) {
                matchingFacet = facetObj;
                break;
            }
        }

        if (matchingFacet == null) {
            throw new SonarApiException("Facet " + facetName + " not found in SonarQube response");
        }

        // Reduce facet values to a map {value_lowercase -> count}
        Map<String, Integer> result = new LinkedHashMap<>();
        JSONArray values = matchingFacet.optJSONArray("values");
        if (values != null) {
            for (int i = 0; i < values.length(); i++) {
                JSONObject valObj = values.getJSONObject(i);
                String val = valObj.getString("val").toLowerCase();
                int count = valObj.getInt("count");
                result.put(val, count);
            }
        }

        return result;
    }

    /**
     * Return the number of open sustainability issues per rule key for a given project and branch.
     *
     * <p>Uses the {@code rules} facet of {@code api/issues/search} to avoid paginating
     * through all issues. Rule keys are returned with their original casing.</p>
     *
     * @param project the SonarQube project key
     * @param branch  the branch name
     * @return a map of rule key → open issue count (0 if a rule has no issues)
     * @throws SonarApiException if the API call fails
     */
    public Map<String, Integer> getIssueCountByRuleKey(String project, String branch) throws SonarApiException {
        Map<String, String> params = buildSustainabilityParams();
        params.put("componentKeys", project);
        params.put("branch", branch);
        params.put("facets", "rules");
        params.put("ps", "1"); // we only need facets, not the issue list

        JSONObject response = client.getJSON(ROUTE, params);
        JSONArray facets = response.optJSONArray("facets");

        if (facets == null) {
            return new LinkedHashMap<>();
        }

        for (int i = 0; i < facets.length(); i++) {
            JSONObject facetObj = facets.getJSONObject(i);
            if ("rules".equals(facetObj.optString("property", ""))) {
                Map<String, Integer> result = new LinkedHashMap<>();
                JSONArray values = facetObj.optJSONArray("values");
                if (values != null) {
                    for (int j = 0; j < values.length(); j++) {
                        JSONObject valObj = values.getJSONObject(j);
                        String val = valObj.getString("val"); // preserve original casing
                        int count = valObj.getInt("count");
                        result.put(val, count);
                    }
                }
                return result;
            }
        }

        return new LinkedHashMap<>();
    }

    /**
     * Get an issues facet filtered by a specific tag (e.g. "cpu" or "ram").
     *
     * <p>Unlike {@link #getIssuesFacet}, this method filters by a single explicit tag
     * instead of the global sustainability tags list.</p>
     *
     * @param facetName the facet to retrieve (e.g. {@code "severities"})
     * @param tag       the specific tag to filter on (e.g. {@code "cpu"})
     * @param project   the SonarQube project key
     * @param branch    the branch name
     * @return a map of facet value → count
     * @throws SonarApiException if the API call fails or the facet is not found
     */
    public Map<String, Integer> getIssuesFacetByTag(String facetName, String tag, String project, String branch)
            throws SonarApiException {

        Map<String, String> params = buildTagParams(tag);
        params.put("componentKeys", project);
        params.put("branch", branch);
        params.put("facets", facetName);
        params.put("ps", "1");

        JSONObject response = client.getJSON(ROUTE, params);
        JSONArray facets = response.optJSONArray("facets");

        if (facets == null) {
            throw new SonarApiException("No facets returned by SonarQube for route " + ROUTE);
        }

        JSONObject matchingFacet = null;
        for (int i = 0; i < facets.length(); i++) {
            JSONObject facetObj = facets.getJSONObject(i);
            if (facetName.equals(facetObj.optString("property", ""))) {
                matchingFacet = facetObj;
                break;
            }
        }

        if (matchingFacet == null) {
            throw new SonarApiException("Facet " + facetName + " not found in SonarQube response");
        }

        Map<String, Integer> result = new LinkedHashMap<>();
        JSONArray values = matchingFacet.optJSONArray("values");
        if (values != null) {
            for (int i = 0; i < values.length(); i++) {
                JSONObject valObj = values.getJSONObject(i);
                result.put(valObj.getString("val").toLowerCase(), valObj.getInt("count"));
            }
        }
        return result;
    }

    /**
     * Return the number of open issues per rule key filtered by a specific tag.
     *
     * @param tag     the specific tag to filter on (e.g. {@code "cpu"})
     * @param project the SonarQube project key
     * @param branch  the branch name
     * @return a map of rule key → open issue count
     * @throws SonarApiException if the API call fails
     */
    public Map<String, Integer> getIssueCountByRuleKeyByTag(String tag, String project, String branch)
            throws SonarApiException {

        Map<String, String> params = buildTagParams(tag);
        params.put("componentKeys", project);
        params.put("branch", branch);
        params.put("facets", "rules");
        params.put("ps", "1");

        JSONObject response = client.getJSON(ROUTE, params);
        JSONArray facets = response.optJSONArray("facets");

        if (facets == null) {
            return new LinkedHashMap<>();
        }

        for (int i = 0; i < facets.length(); i++) {
            JSONObject facetObj = facets.getJSONObject(i);
            if ("rules".equals(facetObj.optString("property", ""))) {
                Map<String, Integer> result = new LinkedHashMap<>();
                JSONArray values = facetObj.optJSONArray("values");
                if (values != null) {
                    for (int j = 0; j < values.length(); j++) {
                        JSONObject valObj = values.getJSONObject(j);
                        result.put(valObj.getString("val"), valObj.getInt("count"));
                    }
                }
                return result;
            }
        }
        return new LinkedHashMap<>();
    }

    // ---- private helpers ----

    private static Map<String, String> buildSustainabilityParams() {
        Map<String, String> params = SonarClient.params();
        params.put("issueStatuses", SonarConstants.ISSUE_STATUSES);
        params.put("statuses", SonarConstants.LEGACY_STATUSES);
        params.put("tags", SonarConstants.SUSTAINABILITY_TAGS);
        return params;
    }

    private static Map<String, String> buildTagParams(String tag) {
        Map<String, String> params = SonarClient.params();
        params.put("issueStatuses", SonarConstants.ISSUE_STATUSES);
        params.put("statuses", SonarConstants.LEGACY_STATUSES);
        params.put("tags", tag);
        return params;
    }
}
