package it.org.greencodeinitiative.creedengo.confluence.plugin.sonar;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Lightweight embedded HTTP server that mocks SonarQube Web API responses.
 *
 * <p>Uses {@code com.sun.net.httpserver.HttpServer} (JDK built-in) so no
 * external dependency is required. Responses are loaded from JSON fixture files
 * under {@code src/test/resources/fixtures/sonar/}, which were captured from a
 * real SonarQube instance running the
 * {@code org.green-code-initiative:creedengo-java-plugin-test-project} project.</p>
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

    private static final String FIXTURES_BASE = "/fixtures/sonar/";

    private HttpServer server;
    private int port;

    // ---- Fixture loading ----

    /**
     * Load a JSON fixture file from the classpath.
     *
     * @param name file name relative to {@code /fixtures/sonar/} (e.g. {@code "issues-search.json"})
     * @return the file contents as a UTF-8 string
     * @throws IllegalStateException if the fixture is not found
     */
    public static String loadFixture(String name) {
        String path = FIXTURES_BASE + name;
        try (InputStream is = MockSonarServer.class.getResourceAsStream(path)) {
            Objects.requireNonNull(is, "Fixture not found on classpath: " + path);
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load fixture: " + path, e);
        }
    }

    // ---- Cached fixture responses ----

    public static final String ISSUES_SEARCH_RESPONSE       = loadFixture("issues-search.json");
    public static final String MEASURES_COMPONENT_RESPONSE  = loadFixture("measures-component.json");
    public static final String PROJECT_BRANCHES_RESPONSE    = loadFixture("project-branches.json");
    public static final String PROJECT_PULL_REQUESTS_RESPONSE = loadFixture("project-pull-requests.json");
    public static final String RULES_SEARCH_RESPONSE        = loadFixture("rules-search.json");
    public static final String RULES_SHOW_RESPONSE          = loadFixture("rules-show.json");

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
