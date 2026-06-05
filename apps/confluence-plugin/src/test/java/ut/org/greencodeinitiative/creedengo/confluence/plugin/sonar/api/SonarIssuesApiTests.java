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
package ut.org.greencodeinitiative.creedengo.confluence.plugin.sonar.api;

import java.util.List;
import java.util.Map;

import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarApiException;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarClient;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.api.SonarIssuesApi;
import org.json.JSONArray;
import org.json.JSONObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@link SonarIssuesApi}.
 */
public class SonarIssuesApiTests {

    @Mock
    private SonarClient sonarClient;

    private SonarIssuesApi issuesApi;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        issuesApi = new SonarIssuesApi(sonarClient);
    }

    // ---- findIssues tests ----

    @Test
    public void testFindIssues_shouldReturnEmptyListWhenNoIssues() throws SonarApiException {
        // Given
        JSONObject response = new JSONObject();
        response.put("issues", new JSONArray());
        when(sonarClient.getJSON(anyString(), any(Map.class))).thenReturn(response);

        // When
        List<JSONObject> issues = issuesApi.findIssues("my-project", "main");

        // Then
        assertNotNull("Issues list should not be null", issues);
        assertEquals("Should return empty list", 0, issues.size());
    }

    @Test
    public void testFindIssues_shouldReturnListOfIssues() throws SonarApiException {
        // Given
        JSONArray issuesArray = new JSONArray();
        JSONObject issue1 = new JSONObject();
        issue1.put("key", "AYz1234");
        issue1.put("rule", "java:S1234");
        issue1.put("severity", "MAJOR");
        issue1.put("status", "OPEN");
        issuesArray.put(issue1);

        JSONObject response = new JSONObject();
        response.put("issues", issuesArray);
        when(sonarClient.getJSON(anyString(), any(Map.class))).thenReturn(response);

        // When
        List<JSONObject> issues = issuesApi.findIssues("my-project", "main");

        // Then
        assertNotNull("Issues list should not be null", issues);
        assertEquals("Should return 1 issue", 1, issues.size());
        assertEquals("First issue key should match", "AYz1234", issues.get(0).getString("key"));
    }

    // ---- getIssuesFacet tests ----

    @Test
    public void testGetIssuesFacet_shouldReturnFacetCounts() throws SonarApiException {
        // Given
        JSONArray facetsArray = new JSONArray();
        JSONObject facet = new JSONObject();
        facet.put("property", "severities");

        JSONArray valuesArray = new JSONArray();
        JSONObject value1 = new JSONObject();
        value1.put("val", "MAJOR");
        value1.put("count", 5);
        JSONObject value2 = new JSONObject();
        value2.put("val", "MINOR");
        value2.put("count", 3);
        valuesArray.put(value1);
        valuesArray.put(value2);

        facet.put("values", valuesArray);
        facetsArray.put(facet);

        JSONObject response = new JSONObject();
        response.put("facets", facetsArray);
        when(sonarClient.getJSON(anyString(), any(Map.class))).thenReturn(response);

        // When
        Map<String, Integer> result = issuesApi.getIssuesFacet("severities", "my-project", "main", null);

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Should have 2 facet values", 2, result.size());
        assertEquals("MAJOR count should be 5", Integer.valueOf(5), result.get("major"));
        assertEquals("MINOR count should be 3", Integer.valueOf(3), result.get("minor"));
    }

    @Test(expected = SonarApiException.class)
    public void testGetIssuesFacet_shouldThrowExceptionWhenFacetNotFound() throws SonarApiException {
        // Given
        JSONObject response = new JSONObject();
        response.put("facets", new JSONArray());
        when(sonarClient.getJSON(anyString(), any(Map.class))).thenReturn(response);

        // When
        issuesApi.getIssuesFacet("types", "my-project", "main", null);

        // Then - exception should be thrown
    }

    @Test
    public void testGetIssuesFacet_shouldFilterBySeverityWhenProvided() throws SonarApiException {
        // Given
        JSONArray facetsArray = new JSONArray();
        JSONObject facet = new JSONObject();
        facet.put("property", "severities");

        JSONArray valuesArray = new JSONArray();
        JSONObject value = new JSONObject();
        value.put("val", "BLOCKER");
        value.put("count", 1);
        valuesArray.put(value);

        facet.put("values", valuesArray);
        facetsArray.put(facet);

        JSONObject response = new JSONObject();
        response.put("facets", facetsArray);
        when(sonarClient.getJSON(anyString(), any(Map.class))).thenReturn(response);

        // When
        Map<String, Integer> result = issuesApi.getIssuesFacet("severities", "my-project", "main", "CRITICAL");

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Should have 1 facet value", 1, result.size());
        assertEquals("BLOCKER count should be 1", Integer.valueOf(1), result.get("blocker"));
    }

    // ---- getIssueCountByRuleKey tests ----

    @Test
    public void testGetIssueCountByRuleKey_shouldReturnRuleKeyCounts() throws SonarApiException {
        // Given
        JSONArray facetsArray = new JSONArray();
        JSONObject facet = new JSONObject();
        facet.put("property", "rules");

        JSONArray valuesArray = new JSONArray();
        JSONObject value1 = new JSONObject();
        value1.put("val", "java:S1234");
        value1.put("count", 10);
        JSONObject value2 = new JSONObject();
        value2.put("val", "java:S5678");
        value2.put("count", 7);
        valuesArray.put(value1);
        valuesArray.put(value2);

        facet.put("values", valuesArray);
        facetsArray.put(facet);

        JSONObject response = new JSONObject();
        response.put("facets", facetsArray);
        when(sonarClient.getJSON(anyString(), any(Map.class))).thenReturn(response);

        // When
        Map<String, Integer> result = issuesApi.getIssueCountByRuleKey("my-project", "main");

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Should have 2 rules", 2, result.size());
        assertEquals("Rule java:S1234 count should be 10", Integer.valueOf(10), result.get("java:S1234"));
        assertEquals("Rule java:S5678 count should be 7", Integer.valueOf(7), result.get("java:S5678"));
    }

    @Test
    public void testGetIssueCountByRuleKey_shouldReturnEmptyMapWhenNoRulesFacet() throws SonarApiException {
        // Given
        JSONObject response = new JSONObject();
        response.put("facets", new JSONArray());
        when(sonarClient.getJSON(anyString(), any(Map.class))).thenReturn(response);

        // When
        Map<String, Integer> result = issuesApi.getIssueCountByRuleKey("my-project", "main");

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Should return empty map", 0, result.size());
    }

    // ---- getIssuesFacetByTag tests ----

    @Test
    public void testGetIssuesFacetByTag_shouldReturnFacetCountsForTag() throws SonarApiException {
        // Given
        JSONArray facetsArray = new JSONArray();
        JSONObject facet = new JSONObject();
        facet.put("property", "severities");

        JSONArray valuesArray = new JSONArray();
        JSONObject value = new JSONObject();
        value.put("val", "HIGH");
        value.put("count", 4);
        valuesArray.put(value);

        facet.put("values", valuesArray);
        facetsArray.put(facet);

        JSONObject response = new JSONObject();
        response.put("facets", facetsArray);
        when(sonarClient.getJSON(anyString(), any(Map.class))).thenReturn(response);

        // When
        Map<String, Integer> result = issuesApi.getIssuesFacetByTag("severities", "cpu", "my-project", "main");

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Should have 1 facet value", 1, result.size());
        assertEquals("HIGH count should be 4", Integer.valueOf(4), result.get("high"));
    }

    @Test(expected = SonarApiException.class)
    public void testGetIssuesFacetByTag_shouldThrowExceptionWhenFacetNotFound() throws SonarApiException {
        // Given
        JSONObject response = new JSONObject();
        response.put("facets", new JSONArray());
        when(sonarClient.getJSON(anyString(), any(Map.class))).thenReturn(response);

        // When
        issuesApi.getIssuesFacetByTag("types", "ram", "my-project", "main");

        // Then - exception should be thrown
    }

    // ---- getIssueCountByRuleKeyByTag tests ----

    @Test
    public void testGetIssueCountByRuleKeyByTag_shouldReturnRuleKeyCountsForTag() throws SonarApiException {
        // Given
        JSONArray facetsArray = new JSONArray();
        JSONObject facet = new JSONObject();
        facet.put("property", "rules");

        JSONArray valuesArray = new JSONArray();
        JSONObject value1 = new JSONObject();
        value1.put("val", "java:S999");
        value1.put("count", 6);
        valuesArray.put(value1);

        facet.put("values", valuesArray);
        facetsArray.put(facet);

        JSONObject response = new JSONObject();
        response.put("facets", facetsArray);
        when(sonarClient.getJSON(anyString(), any(Map.class))).thenReturn(response);

        // When
        Map<String, Integer> result = issuesApi.getIssueCountByRuleKeyByTag("cpu", "my-project", "main");

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Should have 1 rule", 1, result.size());
        assertEquals("Rule java:S999 count should be 6", Integer.valueOf(6), result.get("java:S999"));
    }

    @Test
    public void testGetIssueCountByRuleKeyByTag_shouldReturnEmptyMapWhenNoRulesFacet() throws SonarApiException {
        // Given
        JSONObject response = new JSONObject();
        response.put("facets", new JSONArray());
        when(sonarClient.getJSON(anyString(), any(Map.class))).thenReturn(response);

        // When
        Map<String, Integer> result = issuesApi.getIssueCountByRuleKeyByTag("cpu", "my-project", "main");

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Should return empty map", 0, result.size());
    }
}
