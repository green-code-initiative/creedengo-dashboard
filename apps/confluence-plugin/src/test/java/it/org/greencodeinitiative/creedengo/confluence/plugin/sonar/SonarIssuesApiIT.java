package it.org.greencodeinitiative.creedengo.confluence.plugin.sonar;

import java.util.List;
import java.util.Map;

import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarApiException;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarClient;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.api.SonarIssuesApi;
import org.json.JSONObject;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Integration tests for {@link SonarIssuesApi} using a mock SonarQube server.
 */
public class SonarIssuesApiIT {

    private static MockSonarServer mockServer;
    private static SonarIssuesApi issuesApi;

    @BeforeClass
    public static void startServer() throws Exception {
        mockServer = new MockSonarServer();
        mockServer.start();
        SonarClient client = new SonarClient(mockServer.getBaseUrl(), "test-token");
        issuesApi = new SonarIssuesApi(client);
    }

    @AfterClass
    public static void stopServer() {
        mockServer.stop();
    }

    // ---- findIssues ----

    @Test
    public void findIssues_shouldReturnListOfIssues() throws SonarApiException {
        List<JSONObject> issues = issuesApi.findIssues("my-project", "main");

        assertNotNull("Issues list should not be null", issues);
        assertEquals("Should return 2 issues", 2, issues.size());
    }

    @Test
    public void findIssues_firstIssueShouldHaveExpectedFields() throws SonarApiException {
        List<JSONObject> issues = issuesApi.findIssues("my-project", "main");

        JSONObject first = issues.get(0);
        assertEquals("AYz1234", first.getString("key"));
        assertEquals("java:S1234", first.getString("rule"));
        assertEquals("MAJOR", first.getString("severity"));
        assertEquals("OPEN", first.getString("status"));
        assertEquals("my-project", first.getString("project"));
        assertEquals(42, first.getInt("line"));
    }

    @Test
    public void findIssues_secondIssueShouldHaveExpectedFields() throws SonarApiException {
        List<JSONObject> issues = issuesApi.findIssues("my-project", "main");

        JSONObject second = issues.get(1);
        assertEquals("AYz5678", second.getString("key"));
        assertEquals("java:S5678", second.getString("rule"));
        assertEquals("MINOR", second.getString("severity"));
        assertEquals("CONFIRMED", second.getString("status"));
    }

    @Test
    public void findIssues_issuesShouldContainTags() throws SonarApiException {
        List<JSONObject> issues = issuesApi.findIssues("my-project", "main");

        JSONObject first = issues.get(0);
        assertTrue("First issue should have tags", first.has("tags"));
        assertEquals(2, first.getJSONArray("tags").length());
    }

    // ---- getIssuesFacet ----

    @Test
    public void getIssuesFacet_severities_shouldReturnMap() throws SonarApiException {
        Map<String, Integer> facet = issuesApi.getIssuesFacet("severities", "my-project", "main", null);

        assertNotNull("Facet map should not be null", facet);
        assertFalse("Facet map should not be empty", facet.isEmpty());
    }

    @Test
    public void getIssuesFacet_severities_shouldContainExpectedValues() throws SonarApiException {
        Map<String, Integer> facet = issuesApi.getIssuesFacet("severities", "my-project", "main", null);

        assertEquals("MAJOR count", Integer.valueOf(5), facet.get("major"));
        assertEquals("MINOR count", Integer.valueOf(3), facet.get("minor"));
        assertEquals("CRITICAL count", Integer.valueOf(1), facet.get("critical"));
    }

    @Test
    public void getIssuesFacet_severities_keysShouldBeLowercase() throws SonarApiException {
        Map<String, Integer> facet = issuesApi.getIssuesFacet("severities", "my-project", "main", null);

        for (String key : facet.keySet()) {
            assertEquals("Key should be lowercase: " + key, key.toLowerCase(), key);
        }
    }

    @Test
    public void getIssuesFacet_types_shouldReturnExpectedValues() throws SonarApiException {
        Map<String, Integer> facet = issuesApi.getIssuesFacet("types", "my-project", "main", null);

        assertEquals("CODE_SMELL count", Integer.valueOf(7), facet.get("code_smell"));
        assertEquals("BUG count", Integer.valueOf(2), facet.get("bug"));
    }

    @Test(expected = SonarApiException.class)
    public void getIssuesFacet_unknownFacet_shouldThrow() throws SonarApiException {
        issuesApi.getIssuesFacet("unknown_facet", "my-project", "main", null);
    }

    @Test
    public void getIssuesFacet_withSeverityFilter_shouldWork() throws SonarApiException {
        // The mock always returns the same response, but we verify the call doesn't fail
        Map<String, Integer> facet = issuesApi.getIssuesFacet("severities", "my-project", "main", "MAJOR");

        assertNotNull(facet);
        assertFalse(facet.isEmpty());
    }
}
