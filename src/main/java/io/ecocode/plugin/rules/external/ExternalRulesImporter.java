package io.ecocode.plugin.rules.external;

import org.sonar.api.batch.rule.Severity;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rules.RuleType;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import io.ecocode.plugin.rules.external.ReportParser.Report;
import io.ecocode.plugin.rules.external.ReportParser.Rule;

public class ExternalRulesImporter {
    private static final Logger LOG = Loggers.get(ExternalRulesImporter.class);

    private final SensorContext context;
    private final Report report;

    public ExternalRulesImporter(SensorContext context, Report report) {
        this.context = context;
        this.report = report;
    }

    public void execute() {
        int ruleCount = 0;

        for (Rule rule : report.rules) {
            importRule(rule);
            ruleCount++;
        }

        LOG.info("Imported {} {}", ruleCount, pluralize("rule", ruleCount));
    }

    private void importRule(Rule rule) {
        context.newAdHocRule()
                .engineId(rule.engineId)
                .ruleId(rule.ruleId)
                .name(rule.name)
                .description(rule.description)
                .type(RuleType.valueOf(rule.type))
                .severity(Severity.valueOf(rule.severity))
                .save();
    }

    private static String pluralize(String msg, int count) {
        if (count == 1) {
            return msg;
        }
        return msg + "s";
    }

}
