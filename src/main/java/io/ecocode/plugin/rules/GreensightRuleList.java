package io.ecocode.plugin.rules;

import org.reflections.Reflections;
import org.sonar.check.Rule;

import java.util.ArrayList;
import java.util.List;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

/**
 * Store Checks (aka rules) available for all the programming languages supported by Greensight
 * Checks list are lazily loaded: content will be computed only when requested for the first time
 * then they will be cached for further request.
 */
public class GreensightRuleList {
    private static List<Class<?>> JavaRuleSet;
    private static List<Class<?>> PhpRuleSet;
    private static List<Class<?>> PythonRuleSet;

    /**
     * Return class that are annotated with @Rule inside specified package
     * @param packageName full path to package
     * @return list of class
     */
    private static List<Class<?>> getRules(String packageName)
    {
        Reflections reflects = new Reflections(packageName);
        return new ArrayList<>(reflects.get(SubTypes.of(TypesAnnotated.with(Rule.class)).asClass()));
    }

    /**
     * @return Greensight Java rules
     */
    public static List<Class<?>> getJavaRules() {
        if (JavaRuleSet == null) {
            JavaRuleSet = getRules("fr.capgemini.greensight.plugin.rules.checks.java");
        }

        return JavaRuleSet;
    }

    /**
     * @return Greensight Php rules
     */
    public static List<Class<?>> getPhpRules() {
        if (PhpRuleSet == null) {
            PhpRuleSet = getRules("fr.capgemini.greensight.plugin.rules.checks.php");
        }

        return PhpRuleSet;
    }

    /**
     * @return Greensight Python rules
     */
    public static List<Class<?>> getPythonRules() {
        if (PythonRuleSet == null) {
            PythonRuleSet = getRules("fr.capgemini.greensight.plugin.rules.checks.python");
        }

        return PythonRuleSet;
    }
}
