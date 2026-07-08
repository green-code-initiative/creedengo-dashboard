
package org.greencodeinitiative.creedengo.confluence.plugin.macro;

import java.util.regex.Pattern;

import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarApiException;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.SonarClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.webresource.api.assembler.PageBuilderService;

public class MacroHelper {
    private static final Logger log = LoggerFactory.getLogger(MacroHelper.class);
    private static final Pattern URL_PATTERN = Pattern.compile(
            "^https?://[a-zA-Z0-9]([a-zA-Z0-9\\-]*[a-zA-Z0-9])?(\\.[a-zA-Z0-9]([a-zA-Z0-9\\-]*[a-zA-Z0-9])?)*"
            + "(:\\d{1,5})?(/[^\\s]*)?$"
    );

    public static boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        return URL_PATTERN.matcher(url.trim()).matches();
    }

    public static String verifySonarServer(String sonarqubeUrl, String sonarqubeToken) {
        try {
            SonarClient client = new SonarClient(sonarqubeUrl, sonarqubeToken);
            String status = client.checkServerStatus();
            if (!"UP".equals(status)) {
                return "Le serveur SonarQube a répondu mais son statut est : " + status;
            }
            return null; // success
        } catch (SonarApiException e) {
            return "Erreur de connexion au serveur SonarQube : " + e.getMessage();
        }
    }

    public static void requireWebResources(PageBuilderService pageBuilderService) {
        try {
            pageBuilderService.assembler().resources().requireWebResource("org.green-code-initiative.creedengo-confluence-plugin:creedengo-confluence-plugin-resources");
            log.debug("creedengo : Web resources loaded successfully");
        } catch (Exception e) {
            log.error("creedengo : Failed to load web resources", e);
        }
    }

    public static String escapeHtml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
        /**
     * Récupère une chaîne i18n depuis les ressources Atlassian (avec paramètres optionnels).
     * @param key la clé i18n
     * @param params paramètres à injecter dans la chaîne (optionnel)
     * @return la chaîne traduite, ou la clé si non trouvée
     */
    public static String getText(String key, Object... params) {
        try {
            com.atlassian.sal.api.message.I18nResolver resolver =
                com.atlassian.sal.api.component.ComponentLocator.getComponent(com.atlassian.sal.api.message.I18nResolver.class);
            if (resolver != null) {
                // Cast params to Serializable[] to match the method signature
                return resolver.getText(key, params != null ? java.util.Arrays.copyOf(params, params.length, java.io.Serializable[].class) : null);
            }
        } catch (Throwable t) {
            log.warn("Could not resolve i18n key: {}", key, t);
        }
        // fallback: return key
        return key;
    }
}
