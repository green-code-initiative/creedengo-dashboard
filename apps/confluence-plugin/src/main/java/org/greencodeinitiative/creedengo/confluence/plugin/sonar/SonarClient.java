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
package org.greencodeinitiative.creedengo.confluence.plugin.sonar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP client for SonarQube Web API.
 *
 * <p>Equivalent of the JavaScript {@code sonar-request.js} polyfill.
 * Authenticates via a Bearer token and returns parsed JSON responses.</p>
 *
 * <p>Usage:</p>
 * <pre>{@code
 * SonarClient client = new SonarClient("https://sonar.example.com", "squ_mytoken");
 * JSONObject json = client.getJSON("api/issues/search", params);
 * }</pre>
 *
 * @see <a href="https://docs.sonarsource.com/sonarqube/latest/extension-guide/web-api/">SonarQube Web API</a>
 */
public class SonarClient {

    private static final Logger log = LoggerFactory.getLogger(SonarClient.class);

    private static final int CONNECT_TIMEOUT_MS = 10_000;
    private static final int READ_TIMEOUT_MS = 30_000;

    private final String baseUrl;
    private final String token;

    /**
     * Create a new SonarQube API client.
     *
     * @param baseUrl the SonarQube server base URL (e.g. {@code https://sonar.example.com})
     * @param token   the authentication token (Bearer)
     */
    public SonarClient(String baseUrl, String token) {
        // strip trailing slash
        this.baseUrl = baseUrl != null && baseUrl.endsWith("/")
                ? baseUrl.substring(0, baseUrl.length() - 1)
                : baseUrl;
        this.token = token;
    }

    /**
     * Execute a GET request and return the parsed JSON response.
     *
     * @param route  the API route (e.g. {@code "api/issues/search"})
     * @param params query parameters (may be {@code null} or empty)
     * @return the parsed {@link JSONObject}
     * @throws SonarApiException if the request fails
     */
    public JSONObject getJSON(String route, Map<String, String> params) throws SonarApiException {
        String url = buildUrl(route, params);
        log.debug("SonarQube GET {}", url);

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            if (token != null && !token.isEmpty()) {
                connection.setRequestProperty("Authorization", "Bearer " + token);
            }
            connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
            connection.setReadTimeout(READ_TIMEOUT_MS);

            int status = connection.getResponseCode();
            if (status < 200 || status >= 300) {
                String errorBody = readStream(connection);
                throw new SonarApiException(
                        "SonarQube API returned HTTP " + status + " for " + route + ": " + errorBody);
            }

            String body = readBody(connection);
            return new JSONObject(body);

        } catch (SonarApiException e) {
            throw e;
        } catch (IOException e) {
            throw new SonarApiException("Failed to call SonarQube API: " + route, e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    // ---- URL building ----

    private String buildUrl(String route, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(baseUrl).append('/').append(route);
        if (params != null && !params.isEmpty()) {
            sb.append('?').append(encodeParams(params));
        }
        return sb.toString();
    }

    private static String encodeParams(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append('&');
            }
            sb.append(urlEncode(entry.getKey()))
              .append('=')
              .append(urlEncode(entry.getValue()));
        }
        return sb.toString();
    }

    private static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            // UTF-8 is always supported
            throw new IllegalStateException(e);
        }
    }

    // ---- Response reading ----

    private static String readBody(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            return readAll(reader);
        }
    }

    private static String readStream(HttpURLConnection connection) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
            return readAll(reader);
        } catch (Exception e) {
            return "(unable to read error body)";
        }
    }

    private static String readAll(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    // ---- Server health check ----

    /**
     * Check that the SonarQube server is reachable and responding.
     *
     * <p>Calls {@code api/system/status} and verifies the server returns a
     * valid JSON response with a {@code status} field.</p>
     *
     * @return the server status string (e.g. {@code "UP"}, {@code "STARTING"})
     * @throws SonarApiException if the server is unreachable or returns an error
     */
    public String checkServerStatus() throws SonarApiException {
        JSONObject json = getJSON("api/system/status", null);
        if (!json.has("status")) {
            throw new SonarApiException("Unexpected response from api/system/status: missing 'status' field");
        }
        return json.getString("status");
    }

    // ---- Helper for building parameter maps ----

    /**
     * Convenience method to create a mutable parameter map.
     *
     * @return a new empty {@link LinkedHashMap}
     */
    public static Map<String, String> params() {
        return new LinkedHashMap<>();
    }
}
