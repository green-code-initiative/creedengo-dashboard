package it.org.greencodeinitiative.creedengo.confluence.plugin.score;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.greencodeinitiative.creedengo.confluence.plugin.service.ScoreService;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarApiException;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarService;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Integration tests for {@link ScoreService}.
 *
 * <p>Uses a lightweight embedded HTTP server to simulate different SonarQube
 * responses and validate that the ABCDE score is calculated correctly.</p>
 */
public class ScoreServiceIT {

    private static HttpServer server;
    private static int port;

    /** The severity facet response currently served by the mock — changed per test scenario. */
    private static volatile String issuesResponse;
    /** The measures response currently served by the mock — changed per test scenario. */
    private static volatile String measuresResponse;

    @BeforeClass
    public static void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        port = server.getAddress().getPort();

        server.createContext("/api/issues/search", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                byte[] body = issuesResponse.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, body.length);
                OutputStream os = exchange.getResponseBody();
                os.write(body);
                os.close();
            }
        });

        server.createContext("/api/measures/component", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                byte[] body = measuresResponse.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, body.length);
                OutputStream os = exchange.getResponseBody();
                os.write(body);
                os.close();
            }
        });

        server.setExecutor(null);
        server.start();
    }

    @AfterClass
    public static void stopServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    // ---- Helper to build a SonarService pointing at our mock ----

    private ScoreService createScoreService() {
        SonarService sonarService = new SonarService("http://localhost:" + port, "test-token");
        return new ScoreService(sonarService);
    }

    // ---- Helper to build mock JSON responses ----

    private static String severityFacetResponse(int info, int minor, int major, int critical, int blocker) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"total\":0,\"issues\":[],\"facets\":[{\"property\":\"severities\",\"values\":[");
        sb.append("{\"val\":\"INFO\",\"count\":").append(info).append("},");
        sb.append("{\"val\":\"MINOR\",\"count\":").append(minor).append("},");
        sb.append("{\"val\":\"MAJOR\",\"count\":").append(major).append("},");
        sb.append("{\"val\":\"CRITICAL\",\"count\":").append(critical).append("},");
        sb.append("{\"val\":\"BLOCKER\",\"count\":").append(blocker).append("}");
        sb.append("]}]}");
        return sb.toString();
    }

    private static String nclocResponse(long ncloc) {
        return "{\"component\":{\"key\":\"p\",\"measures\":[{\"metric\":\"ncloc\",\"value\":\"" + ncloc + "\"}]}}";
    }

    // ==================== Score A ====================

    @Test
    public void scoreA_noIssues() throws SonarApiException {
        issuesResponse = severityFacetResponse(0, 0, 0, 0, 0);
        measuresResponse = nclocResponse(1000);

        assertEquals("A", createScoreService().calculateProjectScore("p", "main"));
    }

    // ==================== Score B ====================

    @Test
    public void scoreB_fewMinorIssues() throws SonarApiException {
        issuesResponse = severityFacetResponse(2, 3, 0, 0, 0);
        measuresResponse = nclocResponse(10000);

        assertEquals("B", createScoreService().calculateProjectScore("p", "main"));
    }

    @Test
    public void scoreB_singleInfoIssue() throws SonarApiException {
        issuesResponse = severityFacetResponse(1, 0, 0, 0, 0);
        measuresResponse = nclocResponse(10000);

        assertEquals("B", createScoreService().calculateProjectScore("p", "main"));
    }

    // ==================== Score C ====================

    @Test
    public void scoreC_tenConsolidatedMinors() throws SonarApiException {
        issuesResponse = severityFacetResponse(5, 5, 0, 0, 0);
        measuresResponse = nclocResponse(10000);

        assertEquals("C", createScoreService().calculateProjectScore("p", "main"));
    }

    @Test
    public void scoreC_oneMajor() throws SonarApiException {
        issuesResponse = severityFacetResponse(0, 0, 1, 0, 0);
        measuresResponse = nclocResponse(10000);

        assertEquals("C", createScoreService().calculateProjectScore("p", "main"));
    }

    // ==================== Score D ====================

    @Test
    public void scoreD_highMinorRatio() throws SonarApiException {
        // 80 minors / 1000 lines = 0.08 ratio → D
        issuesResponse = severityFacetResponse(40, 40, 0, 0, 0);
        measuresResponse = nclocResponse(1000);

        assertEquals("D", createScoreService().calculateProjectScore("p", "main"));
    }

    @Test
    public void scoreD_tenMajors() throws SonarApiException {
        issuesResponse = severityFacetResponse(0, 0, 10, 0, 0);
        measuresResponse = nclocResponse(10000);

        assertEquals("D", createScoreService().calculateProjectScore("p", "main"));
    }

    @Test
    public void scoreD_oneCritical() throws SonarApiException {
        issuesResponse = severityFacetResponse(0, 0, 0, 1, 0);
        measuresResponse = nclocResponse(10000);

        assertEquals("D", createScoreService().calculateProjectScore("p", "main"));
    }

    // ==================== Score E ====================

    @Test
    public void scoreE_oneBlocker() throws SonarApiException {
        issuesResponse = severityFacetResponse(0, 0, 0, 0, 1);
        measuresResponse = nclocResponse(10000);

        assertEquals("E", createScoreService().calculateProjectScore("p", "main"));
    }

    @Test
    public void scoreE_multipleBlockers() throws SonarApiException {
        issuesResponse = severityFacetResponse(5, 5, 5, 5, 3);
        measuresResponse = nclocResponse(10000);

        assertEquals("E", createScoreService().calculateProjectScore("p", "main"));
    }

    // ==================== ScoreTexts ====================

    @Test
    public void getScoreTexts_returnsTextsForAllScores() {
        for (String score : new String[]{"A", "B", "C", "D", "E"}) {
            ScoreService.ScoreTexts texts = ScoreService.getScoreTexts(score);
            assertNotNull("Texts should exist for score " + score, texts);
            assertNotNull("Label should not be null for score " + score, texts.getLabel());
            assertNotNull("Description should not be null for score " + score, texts.getDescription());
            assertNotNull("Tips should not be null for score " + score, texts.getTips());
        }
    }

    @Test
    public void getScoreTexts_returnsNullForUnknownScore() {
        assertNull(ScoreService.getScoreTexts("Z"));
        assertNull(ScoreService.getScoreTexts(null));
    }

    @Test
    public void getScoreTexts_scoreAHasExpectedLabel() {
        ScoreService.ScoreTexts texts = ScoreService.getScoreTexts("A");
        assertEquals("Your app is fully optimized, congratulations!", texts.getLabel());
    }
}
