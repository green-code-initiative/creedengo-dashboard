package it.org.greencodeinitiative.creedengo.confluence.plugin.sonar;

import java.util.List;

import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarApiException;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarClient;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.api.SonarProjectBranchesApi;
import org.json.JSONObject;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Integration tests for {@link SonarProjectBranchesApi} using a mock SonarQube server.
 */
public class SonarProjectBranchesApiIT {

    private static MockSonarServer mockServer;
    private static SonarProjectBranchesApi branchesApi;

    @BeforeClass
    public static void startServer() throws Exception {
        mockServer = new MockSonarServer();
        mockServer.start();
        SonarClient client = new SonarClient(mockServer.getBaseUrl(), "test-token");
        branchesApi = new SonarProjectBranchesApi(client);
    }

    @AfterClass
    public static void stopServer() {
        mockServer.stop();
    }

    @Test
    public void getProjectBranches_shouldReturnListOfBranches() throws SonarApiException {
        List<JSONObject> branches = branchesApi.getProjectBranches("my-project");

        assertNotNull("Branches list should not be null", branches);
        assertEquals("Should return 2 branches", 2, branches.size());
    }

    @Test
    public void getProjectBranches_mainBranchShouldBeFirst() throws SonarApiException {
        List<JSONObject> branches = branchesApi.getProjectBranches("my-project");

        JSONObject main = branches.get(0);
        assertEquals("main", main.getString("name"));
        assertTrue("Main branch isMain should be true", main.getBoolean("isMain"));
    }

    @Test
    public void getProjectBranches_developBranchShouldBeSecond() throws SonarApiException {
        List<JSONObject> branches = branchesApi.getProjectBranches("my-project");

        JSONObject develop = branches.get(1);
        assertEquals("develop", develop.getString("name"));
        assertFalse("Develop branch isMain should be false", develop.getBoolean("isMain"));
    }

    @Test
    public void getProjectBranches_branchesShouldHaveQualityGateStatus() throws SonarApiException {
        List<JSONObject> branches = branchesApi.getProjectBranches("my-project");

        for (JSONObject branch : branches) {
            assertTrue("Branch should have status", branch.has("status"));
            JSONObject status = branch.getJSONObject("status");
            assertTrue("Status should have qualityGateStatus", status.has("qualityGateStatus"));
        }
    }
}
