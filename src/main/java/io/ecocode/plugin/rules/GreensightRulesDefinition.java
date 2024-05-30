package io.ecocode.plugin.rules;

import com.google.gson.Gson;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.debt.DebtRemediationFunction;
import org.sonar.api.server.rule.RulesDefinition;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * This class is responsible for loading metadata for each GreensightRule
 * Currently, it loads an HTML and JSON file for each rule
 */
public interface GreensightRulesDefinition extends RulesDefinition {
    Gson gson = new Gson();

    default void ruleMetadata(RulesDefinition.NewRule rule, String RESOURCE_BASE_PATH) {
        String metadataKey = rule.key();
        addHtmlDescription(rule, metadataKey, RESOURCE_BASE_PATH);
        addMetadata(rule, metadataKey, RESOURCE_BASE_PATH);
    }

    private void addMetadata(RulesDefinition.NewRule rule, String metadataKey, String RESOURCE_BASE_PATH) {
        URL resource = getClass().getResource(RESOURCE_BASE_PATH + "/" + metadataKey + ".json");
        if (resource != null) {
            RuleMetatada metatada = gson.fromJson(readResource(resource), RuleMetatada.class);
            rule.setSeverity(metatada.defaultSeverity.toUpperCase(Locale.US));
            rule.setName(metatada.title);
            rule.setTags(metatada.tags);
            rule.setType(RuleType.valueOf(metatada.type));
            rule.setStatus(RuleStatus.valueOf(metatada.status.toUpperCase(Locale.US)));
            if (metatada.remediation != null) {
                rule.setDebtRemediationFunction(metatada.remediation.remediationFunction(rule.debtRemediationFunctions()));
                rule.setGapDescription(metatada.remediation.linearDesc);
            }
        }
    }

    private void addHtmlDescription(RulesDefinition.NewRule rule, String metadataKey, String RESOURCE_BASE_PATH) {
        URL resource = getClass().getResource(RESOURCE_BASE_PATH + "/" + metadataKey + ".html");
        if (resource != null) {
            rule.setHtmlDescription(readResource(resource));
        }
    }

    private String readResource(URL resource) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openStream()))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to read: " + resource, e);
        }
    }

    class RuleMetatada {
        String title;
        String status;
        @Nullable
        Remediation remediation;

        String type;
        String[] tags;
        String defaultSeverity;
    }

    class Remediation {
        String func;
        String constantCost;
        String linearDesc;
        String linearOffset;
        String linearFactor;

        public DebtRemediationFunction remediationFunction(RulesDefinition.DebtRemediationFunctions drf) {
            if (func.startsWith("Constant")) {
                return drf.constantPerIssue(constantCost.replace("mn", "min"));
            }
            if ("Linear".equals(func)) {
                return drf.linear(linearFactor.replace("mn", "min"));
            }
            return drf.linearWithOffset(linearFactor.replace("mn", "min"), linearOffset.replace("mn", "min"));
        }
    }
}
