package io.ecocode.plugin.quality_profiles;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.check.Rule;

import io.ecocode.plugin.rules.GreensightRuleList;
import io.ecocode.plugin.rules.definitions.PhpRulesDefinition;

/**
 * Declare Greensight PHP Quality Profile
 */
public final class PhpQualityProfile implements BuiltInQualityProfilesDefinition {
    public static final String QUALITY_PROFILE_NAME = "Greensight";

    @Override
    public void define(Context context) {

        NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(QUALITY_PROFILE_NAME,
                                                                               PhpRulesDefinition.LANGUAGE);

        profile.setDefault(true);

        GreensightRuleList.getPhpRules()
                .forEach(rule -> profile.activateRule(PhpRulesDefinition.REPOSITORY,
                                                      rule.getAnnotation(Rule.class).key()));

        profile.done();
    }
}
