package it.org.greencodeinitiative.creedengo.confluence.plugin.sonar;

import java.util.List;

import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarApiException;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarClient;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.api.SonarProjectPullRequestsApi;
import org.json.JSONObject;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Integration tests for {@link SonarProjectPullRequestsApi} using a mock SonarQube server.
 */
public class SonarProjectPullRequestsApiIT {

    private static MockSonarServer mockServer;
    private static SonarProjectPullRequestsApi pullRequestsApi;

    @BeforeClass
    public static void startServer() throws Exception {
        mockServer = new MockSonarServer();
        mockServer.start();
        SonarClient client = new SonarClient(mockServer.getBaseUrl(), "test-token");
        pullRequestsApi = new SonarProjectPullRequestsApi(client);
    }

    @AfterClass
    public static void stopServer() {
        mockServer.stop();
    }

    @Test
    public void getProjectPullRequests_shouldReturnListOfPRs() throws SonarApiException {
        List<JSONObject> prs = pullRequestsApi.getProjectPullRequests("my-project");

        assertNotNull("PR list should not be null", prs);
        assertEquals("Should return 2 pull requests", 2, prs.size());
    }

    @Test
    public void getProjectPullRequests_firstPRShouldHaveCorrectKey() throws SonarApiException {
        List<JSONObject> prs = pullRequestsApi.getProjectPullRequests("my-project");

        JSONObject pr1 = prs.get(0);
        assertEquals("42", pr1.getString("key"));
        assertEquals("Fix sustainability issues", pr1.getString("title"));
    }

    @Test
    public void getProjectPullRequests_prsShouldHaveBranchAndBase() throws SonarApiException {
        List<JSONObject> prs = pullRequestsApi.getProjectPullRequests("my-project");

        for (JSONObject pr : prs) {
            assertTrue("PR should have branch", pr.has("branch"));
            assertTrue("PR should have base", pr.has("base"));
        }
    }

    @Test
    public void getProjectPullRequests_prsShouldHaveStatus() throws SonarApiException {
        List<JSONObject> prs = pullRequestsApi.getProjectPullRequests("my-project");

        for (JSONObject pr : prs) {
            assertTrue("PR should have status", pr.has("status"));
            JSONObject status = pr.getJSONObject("status");
            assertTrue("Status should have qualityGateStatus", status.has("qualityGateStatus"));
        }
    }

    @Test
    public void getProjectPullRequests_secondPRShouldHaveDifferentKey() throws SonarApiException {
        List<JSONObject> prs = pullRequestsApi.getProjectPullRequests("my-project");

        JSONObject pr2 = prs.get(1);
        assertEquals("43", pr2.getString("key"));
        assertEquals("Add ecocode rules", pr2.getString("title"));
    }
}
