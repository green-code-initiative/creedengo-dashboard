package io.ecocode.plugin.rules.external;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.sonar.api.CoreProperties;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Configuration;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import io.ecocode.plugin.rules.external.ReportParser.Report;

import javax.annotation.ParametersAreNonnullByDefault;

public class ExternalRulesImportSensor implements Sensor {
    private static final Logger LOG = Loggers.get(ExternalRulesImportSensor.class);
    static final String REPORT_PATHS_PROPERTY_KEY = "sonar.greensightRulesPath";

    private final Configuration config;

    public ExternalRulesImportSensor(Configuration config) {
        this.config = config;
    }

    public static List<PropertyDefinition> properties() {
        return Collections.singletonList(PropertyDefinition.builder(REPORT_PATHS_PROPERTY_KEY)
                                             .name("Issues report paths")
                                             .description(
                                                 "List of comma-separated paths (absolute or relative) containing " +
                                                     "report with issues created by external rule engines.")
                                             .category(CoreProperties.CATEGORY_EXTERNAL_ISSUES)
                                             .onQualifiers(Qualifiers.PROJECT)
                                             .build());
    }

    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor.name("Import external issues report")
            .onlyWhenConfiguration(c -> c.hasKey(REPORT_PATHS_PROPERTY_KEY));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void execute(SensorContext context) {
        Set<String> reportPaths = loadReportPaths();
        for (String reportPath : reportPaths) {
            LOG.debug("Importing issues from '{}'", reportPath);
            Path reportFilePath = context.fileSystem().resolvePath(reportPath).toPath();
            ReportParser parser = new ReportParser(reportFilePath);
            Report report = parser.parse();
            ExternalRulesImporter issueImporter = new ExternalRulesImporter(context, report);
            issueImporter.execute();
        }
    }

    private Set<String> loadReportPaths() {
        return Arrays.stream(config.getStringArray(REPORT_PATHS_PROPERTY_KEY)).collect(Collectors.toSet());
    }
}
