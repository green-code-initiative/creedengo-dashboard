package it.org.greencodeinitiative.creedengo.confluence.plugin.sonar;

import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarApiException;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarClient;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.api.SonarMeasuresApi;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Integration tests for {@link SonarMeasuresApi} using a mock SonarQube server.
 */
public class SonarMeasuresApiIT {

    private static MockSonarServer mockServer;
    private static SonarMeasuresApi measuresApi;

    @BeforeClass
    public static void startServer() throws Exception {
        mockServer = new MockSonarServer();
        mockServer.start();
        SonarClient client = new SonarClient(mockServer.getBaseUrl(), "test-token");
        measuresApi = new SonarMeasuresApi(client);
    }

    @AfterClass
    public static void stopServer() {
        mockServer.stop();
    }

    @Test
    public void getNumberOfLineOfCode_shouldReturnExpectedValue() throws SonarApiException {
        long ncloc = measuresApi.getNumberOfLineOfCode("my-project", "main");

        assertEquals("Should return 1853 lines of code", 1853L, ncloc);
    }

    @Test
    public void getNumberOfLineOfCode_shouldReturnPositiveValue() throws SonarApiException {
        long ncloc = measuresApi.getNumberOfLineOfCode("my-project", "main");

        assertTrue("Lines of code should be positive", ncloc > 0);
    }
}
