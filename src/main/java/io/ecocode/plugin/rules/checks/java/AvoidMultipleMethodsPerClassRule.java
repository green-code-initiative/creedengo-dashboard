package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.Collections;
import java.util.List;

@Rule(key = "GS_14")
@SuppressWarnings("java-greensight:GS_14")
public class AvoidMultipleMethodsPerClassRule extends IssuableSubscriptionVisitor {

    private static final String MESSAGE = "Class's methods have surpassed the threshold";
    private static final int DEFAULT_THRESHOLD = 30; // Threshold set just for testing purposes

    @RuleProperty(description = "Maximum number of method in one class before raising an issue",
                  defaultValue = DEFAULT_THRESHOLD + "")
    public int threshold;

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.CLASS);
    }

    @Override
    public void visitNode(Tree tree) {
        ClassTree classTree = (ClassTree) tree;
        if (classTree.members().stream().filter(this::memberIsMethod).count() > threshold) {
            reportIssue(classTree.simpleName(), MESSAGE);
        }
    }

    /*
    Per SonarQube's definitions, constructors are considered methods as well
     */
    public boolean memberIsMethod(Tree member) {
        return member.is(Tree.Kind.METHOD) || member.is(Tree.Kind.CONSTRUCTOR);
    }
}
