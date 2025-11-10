package it.org.greencodeinitiative.creedengo.confluence.plugin.sonar;

import java.util.List;

import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarApiException;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarClient;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.api.SonarRulesApi;
import org.json.JSONObject;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Integration tests for {@link SonarRulesApi} using a mock SonarQube server.
 */
public class SonarRulesApiIT {

    private static MockSonarServer mockServer;
    private static SonarRulesApi rulesApi;

    @BeforeClass
    public static void startServer() throws Exception {
        mockServer = new MockSonarServer();
        mockServer.start();
        SonarClient client = new SonarClient(mockServer.getBaseUrl(), "test-token");
        rulesApi = new SonarRulesApi(client);
    }

    @AfterClass
    public static void stopServer() {
        mockServer.stop();
    }

    // ---- findRules ----

    @Test
    public void findRules_shouldReturnListOfRules() throws SonarApiException {
        List<JSONObject> rules = rulesApi.findRules("my-project", "main");

        assertNotNull("Rules list should not be null", rules);
        assertEquals("Should return 2 rules", 2, rules.size());
    }

    @Test
    public void findRules_firstRuleShouldHaveExpectedFields() throws SonarApiException {
        List<JSONObject> rules = rulesApi.findRules("my-project", "main");

        JSONObject first = rules.get(0);
        assertEquals("java:S1234", first.getString("key"));
        assertEquals("Avoid energy-consuming pattern", first.getString("name"));
        assertEquals("MAJOR", first.getString("severity"));
        assertEquals("java", first.getString("lang"));
        assertEquals("CODE_SMELL", first.getString("type"));
    }

    @Test
    public void findRules_rulesShouldContainSustainabilityTags() throws SonarApiException {
        List<JSONObject> rules = rulesApi.findRules("my-project", "main");

        JSONObject first = rules.get(0);
        assertTrue("Rule should have tags", first.has("tags"));
        assertTrue("Tags should contain 'creedengo'",
                first.getJSONArray("tags").toString().contains("creedengo"));
    }

    // ---- getRuleDetails ----

    @Test
    public void getRuleDetails_shouldReturnRuleObject() throws SonarApiException {
        JSONObject rule = rulesApi.getRuleDetails("java:S1234");

        assertNotNull("Rule should not be null", rule);
    }

    @Test
    public void getRuleDetails_shouldContainExpectedFields() throws SonarApiException {
        JSONObject rule = rulesApi.getRuleDetails("java:S1234");

        assertEquals("java:S1234", rule.getString("key"));
        assertEquals("Avoid energy-consuming pattern", rule.getString("name"));
        assertEquals("MAJOR", rule.getString("severity"));
        assertEquals("Java", rule.getString("langName"));
    }

    @Test
    public void getRuleDetails_shouldContainDescription() throws SonarApiException {
        JSONObject rule = rulesApi.getRuleDetails("java:S1234");

        assertTrue("Rule should have HTML description", rule.has("htmlDesc"));
        assertTrue("Rule should have Markdown description", rule.has("mdDesc"));
        assertFalse("HTML description should not be empty", rule.getString("htmlDesc").isEmpty());
    }
}
