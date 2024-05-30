package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.Tree;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

@Rule(key = "GS_13")
@SuppressWarnings("java-greensight:GS_13")
public class AvoidMultipleFieldsPerClassRule extends IssuableSubscriptionVisitor {

    private static final String MESSAGE = "Classes should not have too many fields";
    private static final int DEFAULT_THRESHOLD = 13; // Threshold set just for testing purposes

    @RuleProperty(description = "Maximum number of allowed Fields per class before raising an issue",
                  defaultValue = DEFAULT_THRESHOLD + "")
    public int threshold = DEFAULT_THRESHOLD;

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.CLASS);
    }

    /*
    ClassTree has a List of all of its members (variables, functions, etc.)
    For this rule, we're only taking into account variables (Tree.Kind.VARIABLE)
     */
    @Override
    @ParametersAreNonnullByDefault
    public void visitNode(Tree tree) {
        ClassTree classTree = (ClassTree) tree;
        if (classTree.members().stream().filter(member -> member.is(Tree.Kind.VARIABLE)).count() > threshold) {
            reportIssue(classTree.simpleName(), MESSAGE);
        }
    }
}
