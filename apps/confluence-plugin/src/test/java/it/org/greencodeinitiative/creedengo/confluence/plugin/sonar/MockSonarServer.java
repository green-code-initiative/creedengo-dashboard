package it.org.greencodeinitiative.creedengo.confluence.plugin.sonar;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Lightweight embedded HTTP server that mocks SonarQube Web API responses.
 *
 * <p>Uses {@code com.sun.net.httpserver.HttpServer} (JDK built-in) so no
 * external dependency is required. Each API route is pre-configured with
 * realistic JSON responses matching the real SonarQube API contract.</p>
 *
 * <p>Usage:</p>
 * <pre>{@code
 * MockSonarServer server = new MockSonarServer();
 * server.start();
 * String baseUrl = server.getBaseUrl(); // http://localhost:<port>
 * // ... run tests ...
 * server.stop();
 * }</pre>
 */
public class MockSonarServer {

    private HttpServer server;
    private int port;

    // ---- Realistic mock JSON responses ----

    public static final String ISSUES_SEARCH_RESPONSE = "{"
            + "\"total\": 2,"
            + "\"p\": 1,"
            + "\"ps\": 100,"
            + "\"paging\": {\"pageIndex\": 1, \"pageSize\": 100, \"total\": 2},"
            + "\"issues\": ["
            + "  {"
            + "    \"key\": \"AYz1234\","
            + "    \"rule\": \"java:S1234\","
            + "    \"severity\": \"MAJOR\","
            + "    \"component\": \"my-project:src/main/java/App.java\","
            + "    \"project\": \"my-project\","
            + "    \"line\": 42,"
            + "    \"status\": \"OPEN\","
            + "    \"message\": \"Avoid this pattern for sustainability\","
            + "    \"tags\": [\"creedengo\", \"sustainability\"],"
            + "    \"type\": \"CODE_SMELL\""
            + "  },"
            + "  {"
            + "    \"key\": \"AYz5678\","
            + "    \"rule\": \"java:S5678\","
            + "    \"severity\": \"MINOR\","
            + "    \"component\": \"my-project:src/main/java/Util.java\","
            + "    \"project\": \"my-project\","
            + "    \"line\": 10,"
            + "    \"status\": \"CONFIRMED\","
            + "    \"message\": \"Use energy-efficient alternative\","
            + "    \"tags\": [\"ecocode\"],"
            + "    \"type\": \"CODE_SMELL\""
            + "  }"
            + "],"
            + "\"components\": [],"
            + "\"facets\": ["
            + "  {"
            + "    \"property\": \"severities\","
            + "    \"values\": ["
            + "      {\"val\": \"MAJOR\", \"count\": 5},"
            + "      {\"val\": \"MINOR\", \"count\": 3},"
            + "      {\"val\": \"CRITICAL\", \"count\": 1}"
            + "    ]"
            + "  },"
            + "  {"
            + "    \"property\": \"types\","
            + "    \"values\": ["
            + "      {\"val\": \"CODE_SMELL\", \"count\": 7},"
            + "      {\"val\": \"BUG\", \"count\": 2}"
            + "    ]"
            + "  }"
            + "]"
            + "}";

    public static final String MEASURES_COMPONENT_RESPONSE = "{"
            + "\"component\": {"
            + "  \"key\": \"my-project\","
            + "  \"name\": \"My Project\","
            + "  \"qualifier\": \"TRK\","
            + "  \"measures\": ["
            + "    {"
            + "      \"metric\": \"ncloc\","
            + "      \"value\": \"12345\""
            + "    }"
            + "  ]"
            + "}"
            + "}";

    public static final String RULES_SEARCH_RESPONSE = "{"
            + "\"total\": 2,"
            + "\"p\": 1,"
            + "\"ps\": 100,"
            + "\"rules\": ["
            + "  {"
            + "    \"key\": \"java:S1234\","
            + "    \"name\": \"Avoid energy-consuming pattern\","
            + "    \"severity\": \"MAJOR\","
            + "    \"lang\": \"java\","
            + "    \"tags\": [\"creedengo\", \"sustainability\"],"
            + "    \"type\": \"CODE_SMELL\""
            + "  },"
            + "  {"
            + "    \"key\": \"java:S5678\","
            + "    \"name\": \"Prefer efficient data structure\","
            + "    \"severity\": \"MINOR\","
            + "    \"lang\": \"java\","
            + "    \"tags\": [\"ecocode\"],"
            + "    \"type\": \"CODE_SMELL\""
            + "  }"
            + "]"
            + "}";

    public static final String RULES_SHOW_RESPONSE = "{"
            + "\"rule\": {"
            + "  \"key\": \"java:S1234\","
            + "  \"name\": \"Avoid energy-consuming pattern\","
            + "  \"severity\": \"MAJOR\","
            + "  \"lang\": \"java\","
            + "  \"langName\": \"Java\","
            + "  \"tags\": [\"creedengo\", \"sustainability\"],"
            + "  \"type\": \"CODE_SMELL\","
            + "  \"htmlDesc\": \"<p>This rule detects patterns that consume excessive energy.</p>\","
            + "  \"mdDesc\": \"This rule detects patterns that consume excessive energy.\""
            + "}"
            + "}";

    public static final String PROJECT_BRANCHES_RESPONSE = "{"
            + "\"branches\": ["
            + "  {"
            + "    \"name\": \"main\","
            + "    \"isMain\": true,"
            + "    \"type\": \"LONG\","
            + "    \"status\": {\"qualityGateStatus\": \"OK\"}"
            + "  },"
            + "  {"
            + "    \"name\": \"develop\","
            + "    \"isMain\": false,"
            + "    \"type\": \"LONG\","
            + "    \"status\": {\"qualityGateStatus\": \"OK\"}"
            + "  }"
            + "]"
            + "}";

    public static final String PROJECT_PULL_REQUESTS_RESPONSE = "{"
            + "\"pullRequests\": ["
            + "  {"
            + "    \"key\": \"42\","
            + "    \"title\": \"Fix sustainability issues\","
            + "    \"branch\": \"feature/green\","
            + "    \"base\": \"main\","
            + "    \"status\": {\"qualityGateStatus\": \"OK\"}"
            + "  },"
            + "  {"
            + "    \"key\": \"43\","
            + "    \"title\": \"Add ecocode rules\","
            + "    \"branch\": \"feature/eco\","
            + "    \"base\": \"main\","
            + "    \"status\": {\"qualityGateStatus\": \"ERROR\"}"
            + "  }"
            + "]"
            + "}";

    public static final String ERROR_401_RESPONSE = "{\"errors\":[{\"msg\":\"Not authorized. Analyzing this project requires authentication.\"}]}";

    /**
     * Start the mock server on a random available port.
     */
    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        port = server.getAddress().getPort();

        // api/issues/search
        server.createContext("/api/issues/search", jsonHandler(ISSUES_SEARCH_RESPONSE));

        // api/measures/component
        server.createContext("/api/measures/component", jsonHandler(MEASURES_COMPONENT_RESPONSE));

        // api/rules/search
        server.createContext("/api/rules/search", jsonHandler(RULES_SEARCH_RESPONSE));

        // api/rules/show
        server.createContext("/api/rules/show", jsonHandler(RULES_SHOW_RESPONSE));

        // api/project_branches/list
        server.createContext("/api/project_branches/list", jsonHandler(PROJECT_BRANCHES_RESPONSE));

        // api/project_pull_requests/list
        server.createContext("/api/project_pull_requests/list", jsonHandler(PROJECT_PULL_REQUESTS_RESPONSE));

        // Endpoint to simulate auth failure
        server.createContext("/api/auth/fail", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                byte[] body = ERROR_401_RESPONSE.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(401, body.length);
                OutputStream os = exchange.getResponseBody();
                os.write(body);
                os.close();
            }
        });

        server.setExecutor(null);
        server.start();
    }

    /**
     * Stop the mock server.
     */
    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    /**
     * @return the base URL (e.g. {@code http://localhost:54321})
     */
    public String getBaseUrl() {
        return "http://localhost:" + port;
    }

    /**
     * Parse query string from a URI.
     */
    public static Map<String, String> parseQuery(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return result;
        }
        for (String param : query.split("&")) {
            String[] pair = param.split("=", 2);
            String key = pair[0];
            String value = pair.length > 1 ? pair[1] : "";
            result.put(key, value);
        }
        return result;
    }

    private static HttpHandler jsonHandler(final String responseBody) {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                byte[] body = responseBody.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, body.length);
                OutputStream os = exchange.getResponseBody();
                os.write(body);
                os.close();
            }
        };
    }
}
