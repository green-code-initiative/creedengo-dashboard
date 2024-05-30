package io.ecocode.plugin.quality_profiles;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.check.Rule;

import io.ecocode.plugin.rules.GreensightRuleList;
import io.ecocode.plugin.rules.definitions.JavaRulesDefinition;

/**
 * Declare Greensight Java Quality profile, contains all java rules
 */
public final class JavaQualityProfile implements BuiltInQualityProfilesDefinition {
    public static final String QUALITY_PROFILE_NAME = "Greensight";

    @Override
    public void define(Context context) {
        // Create a Java quality profile for Greensight
        NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(QUALITY_PROFILE_NAME,
                                                                               JavaRulesDefinition.LANGUAGE);
        profile.setDefault(true);

        // Define list of rules
        GreensightRuleList.getJavaRules()
                .forEach(rule -> profile.activateRule(JavaRulesDefinition.REPOSITORY,
                                                         rule.getAnnotation(Rule.class).key()));

        // Quality profile is done
        profile.done();
    }
}
