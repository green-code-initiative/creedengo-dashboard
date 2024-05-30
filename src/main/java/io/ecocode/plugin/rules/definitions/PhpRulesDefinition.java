package io.ecocode.plugin.rules.definitions;

import java.util.List;

import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;
import org.sonar.plugins.php.api.visitors.PHPCustomRuleRepository;

import io.ecocode.plugin.rules.GreensightRuleList;
import io.ecocode.plugin.rules.GreensightRulesDefinition;


public class PhpRulesDefinition implements GreensightRulesDefinition, PHPCustomRuleRepository {
    private static final String RESOURCE_BASE_PATH = "/org/sonar/l10n/php/rules/squid";
    public static final String REPOSITORY = "php-greensight";
    public static final String LANGUAGE = "php";

    @Override
    public String repositoryKey(){
        return REPOSITORY;
    }

    @Override
    public List<Class<?>> checkClasses(){
        return GreensightRuleList.getPhpRules();
    }

    @Override
    public void define(Context context) {
        NewRepository repository = context
                .createRepository(REPOSITORY, LANGUAGE)
                .setName("Greensight by Capgemini");

        // Load rule meta data from annotations
        RulesDefinitionAnnotationLoader annotationLoader = new RulesDefinitionAnnotationLoader();
        GreensightRuleList.getPhpRules().forEach(ruleClass -> annotationLoader.load(repository, ruleClass));

        // Optionally override html description from annotation with content from html files
        repository.rules().forEach(newRule -> ruleMetadata(newRule, RESOURCE_BASE_PATH));

        repository.done();
    }

}
