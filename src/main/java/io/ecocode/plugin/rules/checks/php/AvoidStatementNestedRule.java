package io.ecocode.plugin.rules.checks.php;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.statement.*;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(key = "GS_10")
public class AvoidStatementNestedRule extends PHPVisitorCheck {

    private static final int DEFAULT_MAX = 3;
    private static final String MESSAGE = "Avoid nested statements";
    private static final Tree.Kind[] STATEMENTS = {Tree.Kind.IF_STATEMENT, Tree.Kind.FOR_STATEMENT,
        Tree.Kind.FOREACH_STATEMENT, Tree.Kind.WHILE_STATEMENT, Tree.Kind.DO_WHILE_STATEMENT, Tree.Kind.TRY_STATEMENT
        , Tree.Kind.SWITCH_STATEMENT, Tree.Kind.CASE_CLAUSE, Tree.Kind.DEFAULT_CLAUSE, Tree.Kind.CATCH_BLOCK};

    @RuleProperty(description = "Maximum allowed control flow statement nesting depth.",
                  defaultValue = "" + DEFAULT_MAX)
    public int max = DEFAULT_MAX;

    @Override
    public void visitBlock(BlockTree tree) {
        Tree parent = tree.getParent();

        if (parent.is(STATEMENTS)) {
            main(parent);
        }

        super.visitBlock(tree);
    }

    private void main(Tree tree) {
        int count = countStatementDepth(tree);
        if (count > max) context().newIssue(this, tree, MESSAGE);
    }

    private int countStatementDepth(Tree tree) {
        int count = 1;
        Tree parent = tree.getParent();
        if (parent.is(Tree.Kind.BLOCK) || parent.is(Tree.Kind.CASE_CLAUSE) || parent.is(Tree.Kind.DEFAULT_CLAUSE)) {
            parent = parent.getParent();
        }
        if (parent.is(STATEMENTS)) {
            count = count + countStatementDepth(parent);
        }
        return count;
    }

}
