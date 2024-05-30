package io.ecocode.plugin.rules.definitions;

import org.reflections.Reflections;
import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;
import org.sonar.plugins.python.api.PythonCustomRuleRepository;

import io.ecocode.plugin.rules.GreensightRuleList;
import io.ecocode.plugin.rules.GreensightRulesDefinition;

import java.util.ArrayList;
import java.util.List;

public class PythonRulesDefinition implements GreensightRulesDefinition, PythonCustomRuleRepository {

    private static final String RESOURCE_BASE_PATH = "/org/sonar/l10n/python/rules/squid";
    public static final String REPOSITORY = "py-greensight";
    public static final String LANGUAGE = "py";

    @Override
    public String repositoryKey(){
        return REPOSITORY;
    }

    @Override
    public List<Class> checkClasses(){
        return new ArrayList<>(GreensightRuleList.getPythonRules());
    }

    @Override
    public void define(Context context) {
        NewRepository repository = context
                .createRepository(REPOSITORY, LANGUAGE)
                .setName("Greensight by Capgemini");

        // Load rule meta data from annotations
        RulesDefinitionAnnotationLoader annotationLoader = new RulesDefinitionAnnotationLoader();
        GreensightRuleList.getPythonRules().forEach(ruleClass -> annotationLoader.load(repository, ruleClass));

        // Optionally override html description from annotation with content from html files
        repository.rules().forEach(newRule -> ruleMetadata(newRule, RESOURCE_BASE_PATH));

        repository.done();
    }

}
