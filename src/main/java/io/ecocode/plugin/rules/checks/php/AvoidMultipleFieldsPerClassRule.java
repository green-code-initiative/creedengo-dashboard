package io.ecocode.plugin.rules.checks.php;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.declaration.ClassDeclarationTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(key = "GS_11")
public class AvoidMultipleFieldsPerClassRule extends PHPVisitorCheck {
    private static final String MESSAGE = "Classes should not have too many fields";
    private static final int DEFAULT_THRESHOLD = 13; // Threshold set just for testing purposes

    @RuleProperty(description = "Maximum number of allowed Fields per class before raising an issue",
                  defaultValue = DEFAULT_THRESHOLD + "")
    public int threshold = DEFAULT_THRESHOLD;

    @Override
    public void visitClassDeclaration(ClassDeclarationTree tree) {
        if (tree.members()
            .stream()
            .filter(member -> member.is(Tree.Kind.CLASS_PROPERTY_DECLARATION))
            .count() > threshold) {
            context().newIssue(this, tree, MESSAGE);
        }
        super.visitClassDeclaration(tree);
    }
}
