package io.ecocode.plugin.rules.external;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class ReportParser {
    private final Gson gson = new Gson();
    private final Path filePath;

    public ReportParser(Path filePath) {
        this.filePath = filePath;
    }

    public Report parse() {
        try (Reader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            return validate(gson.fromJson(reader, Report.class));
        } catch (JsonIOException | IOException e) {
            throw new IllegalStateException("Failed to read external rules report '" + filePath + "'", e);
        } catch (JsonSyntaxException e) {
            throw new IllegalStateException("Failed to read external rules report '" + filePath + "': invalid JSON " + "syntax",
                                            e);
        }
    }

    private Report validate(Report report) {
        if (report.rules != null) {
            for (Rule rule : report.rules) {
                mandatoryField(rule.engineId, "engineId");
                mandatoryField(rule.ruleId, "ruleId");
                mandatoryField(rule.name, "name");
                mandatoryField(rule.severity, "severity");
                mandatoryField(rule.type, "type");
                mandatoryField(rule.description, "description");
            }
        }
        return report;
    }

    private void mandatoryField(@Nullable String value, String fieldName) {
        if (value == null || value.isEmpty() || value.isBlank()) {
            throw new IllegalStateException(String.format("Failed to parse report '%s': missing mandatory field '%s'.",
                                                          filePath,
                                                          fieldName));
        }
    }

    static class Report {
        Rule[] rules;

        public Report() {
            // http://stackoverflow.com/a/18645370/229031
        }
    }

    static class Rule {
        String engineId;
        String ruleId;
        String name;
        String severity;
        String type;
        String description;

        public Rule() {
            // http://stackoverflow.com/a/18645370/229031
        }
    }
}
