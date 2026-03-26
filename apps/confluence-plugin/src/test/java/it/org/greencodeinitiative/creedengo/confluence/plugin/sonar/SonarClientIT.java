package it.org.greencodeinitiative.creedengo.confluence.plugin.sonar;

import java.util.Map;

import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarApiException;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarClient;
import org.json.JSONObject;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Integration tests for {@link SonarClient} using a mock HTTP server.
 */
public class SonarClientIT {

    private static MockSonarServer mockServer;
    private static SonarClient client;

    @BeforeClass
    public static void startServer() throws Exception {
        mockServer = new MockSonarServer();
        mockServer.start();
        client = new SonarClient(mockServer.getBaseUrl(), "test-token");
    }

    @AfterClass
    public static void stopServer() {
        mockServer.stop();
    }

    @Test
    public void getJSON_shouldReturnParsedJsonObject() throws SonarApiException {
        Map<String, String> params = SonarClient.params();
        params.put("componentKeys", "my-project");
        params.put("branch", "main");

        JSONObject response = client.getJSON("api/issues/search", params);

        assertNotNull("Response should not be null", response);
        assertTrue("Response should contain 'issues'", response.has("issues"));
        assertEquals(3, response.getJSONArray("issues").length());
    }

    @Test
    public void getJSON_shouldSendQueryParams() throws SonarApiException {
        Map<String, String> params = SonarClient.params();
        params.put("component", "my-project");
        params.put("branch", "main");
        params.put("metricKeys", "ncloc");

        JSONObject response = client.getJSON("api/measures/component", params);

        assertNotNull(response);
        assertTrue(response.has("component"));
    }

    @Test
    public void getJSON_withTrailingSlashBaseUrl_shouldWork() throws SonarApiException {
        SonarClient clientWithSlash = new SonarClient(mockServer.getBaseUrl() + "/", "test-token");

        JSONObject response = clientWithSlash.getJSON("api/rules/search", SonarClient.params());

        assertNotNull(response);
        assertTrue(response.has("rules"));
    }

    @Test
    public void getJSON_withNullToken_shouldNotSetAuthHeader() throws SonarApiException {
        SonarClient clientNoAuth = new SonarClient(mockServer.getBaseUrl(), null);

        JSONObject response = clientNoAuth.getJSON("api/rules/search", SonarClient.params());

        assertNotNull(response);
    }

    @Test
    public void getJSON_withEmptyToken_shouldNotSetAuthHeader() throws SonarApiException {
        SonarClient clientNoAuth = new SonarClient(mockServer.getBaseUrl(), "");

        JSONObject response = clientNoAuth.getJSON("api/rules/search", SonarClient.params());

        assertNotNull(response);
    }

    @Test(expected = SonarApiException.class)
    public void getJSON_onHttpError_shouldThrowSonarApiException() throws SonarApiException {
        client.getJSON("api/auth/fail", SonarClient.params());
    }

    @Test
    public void getJSON_onHttpError_exceptionContainsStatusCode() {
        try {
            client.getJSON("api/auth/fail", SonarClient.params());
            fail("Should have thrown SonarApiException");
        } catch (SonarApiException e) {
            assertTrue("Exception message should contain HTTP 401",
                    e.getMessage().contains("401"));
        }
    }

    @Test(expected = SonarApiException.class)
    public void getJSON_withUnreachableServer_shouldThrowSonarApiException() throws SonarApiException {
        SonarClient badClient = new SonarClient("http://localhost:1", "token");
        badClient.getJSON("api/issues/search", SonarClient.params());
    }

    @Test
    public void getJSON_withNullParams_shouldWork() throws SonarApiException {
        JSONObject response = client.getJSON("api/rules/search", null);

        assertNotNull(response);
        assertTrue(response.has("rules"));
    }

    @Test
    public void getJSON_withEmptyParams_shouldWork() throws SonarApiException {
        JSONObject response = client.getJSON("api/rules/search", SonarClient.params());

        assertNotNull(response);
    }

    @Test
    public void params_shouldReturnMutableMap() {
        Map<String, String> params = SonarClient.params();
        params.put("key", "value");
        assertEquals("value", params.get("key"));
    }
}
