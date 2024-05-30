package io.ecocode.plugin.rules.checks.php;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.php.api.tree.statement.SwitchStatementTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;


@Rule(key = "GS_04")
@SuppressWarnings("java-greensight:GS_04")
public class SwitchAvoidOperationsInCaseRule extends PHPVisitorCheck {

    private static final int DEFAULT_MAX = 3;
    private static final String MESSAGE = "Limit instructions inside a switch case";

    @RuleProperty(description = "Maximum allowed operations in case", defaultValue = "" + DEFAULT_MAX)
    public int max = DEFAULT_MAX;

    @Override
    public void visitSwitchStatement(SwitchStatementTree switchTree) {
        if (!switchTree.cases().isEmpty()) {
            switchTree.cases().forEach(switchCaseClauseTree -> {
                if (!switchCaseClauseTree.statements().isEmpty() && switchCaseClauseTree.statements().size() > max) {
                    context().newIssue(this, switchCaseClauseTree, MESSAGE);
                }
            });
        }
        super.visitSwitchStatement(switchTree);
    }

}
