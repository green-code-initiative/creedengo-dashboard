package io.ecocode.plugin.rules.definitions;

import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;
import org.sonar.plugins.java.api.CheckRegistrar;
import org.sonar.plugins.java.api.JavaCheck;

import io.ecocode.plugin.rules.GreensightRuleList;
import io.ecocode.plugin.rules.GreensightRulesDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provide the "checks" (implementations of rules) classes that are going be executed during
 * source code analysis.
 * This class is a batch extension by implementing the {@link org.sonar.plugins.java.api.CheckRegistrar} interface.
 */
public class JavaRulesDefinition implements GreensightRulesDefinition, CheckRegistrar {

    private static final String RESOURCE_BASE_PATH = "/org/sonar/l10n/java/rules/squid";
    public static final String REPOSITORY = "java-greensight";
    public static final String LANGUAGE = "java";

    /**
     * Register the classes that will be used to instantiate checks during analysis.
     */
    @Override
    public void register(RegistrarContext registrarContext) {
        // Call to registerClassesForRepository to associate the classes with the correct repository key
        registrarContext.registerClassesForRepository(JavaRulesDefinition.REPOSITORY,
                                                      checkClasses(),
                                                      testCheckClasses());
    }

    /**
     * Lists all the main checks provided by the plugin
     */
    private static List<Class<? extends JavaCheck>> checkClasses() {
        ArrayList<Class<? extends JavaCheck>> ret = new ArrayList<>();
        GreensightRuleList.getJavaRules().forEach((rule) -> {
            // check if rule implement JavaCheck interface
            if (JavaCheck.class.isAssignableFrom(rule)) {
                //noinspection unchecked
                ret.add((Class<? extends JavaCheck>) rule);
            }
        }); return ret;
    }

    /**
     * Lists all the test checks provided by the plugin
     */
    public static List<Class<? extends JavaCheck>> testCheckClasses() {
        return Collections.emptyList();
    }

    @Override
    public void define(Context context) {
        NewRepository repository = context.createRepository(REPOSITORY, LANGUAGE).setName("Greensight by Capgemini");

        // Load rule meta data from annotations
        RulesDefinitionAnnotationLoader annotationLoader = new RulesDefinitionAnnotationLoader();
        GreensightRuleList.getJavaRules().forEach(ruleClass -> annotationLoader.load(repository, ruleClass));

        // Optionally override html description from annotation with content from html files
        repository.rules().forEach(newRule -> ruleMetadata(newRule, RESOURCE_BASE_PATH));

        repository.done();
    }
}
