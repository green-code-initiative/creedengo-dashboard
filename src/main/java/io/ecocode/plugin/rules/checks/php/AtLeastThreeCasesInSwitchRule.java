package io.ecocode.plugin.rules.checks.php;

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.statement.SwitchStatementTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(key = "GS_02")
public class AtLeastThreeCasesInSwitchRule extends PHPVisitorCheck {

    @Override
    public void visitSwitchStatement(SwitchStatementTree switchTree) {
        if (switchTree.cases().size() < 3) {
            context().newIssue(this, switchTree.switchToken(), "Replace this \"switch\" statement with \"if\" statements to increase readability.");
        }

        super.visitSwitchStatement(switchTree);
    }
}
