package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.CaseGroupTree;
import org.sonar.plugins.java.api.tree.SwitchStatementTree;
import org.sonar.plugins.java.api.tree.Tree;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;

@Rule(key = "GS_16")
@SuppressWarnings("java-greensight:GS_16")
public class TooManyCaseInSwitchRule extends IssuableSubscriptionVisitor {
    private static final String MESSAGE = "Switch statement should not have too many case clauses";
    private static final int DEFAULT_MAXIMUM_CASES = 30;

    @RuleProperty(description = "Maximum number of case", defaultValue = "" + DEFAULT_MAXIMUM_CASES)
    public int maximumCases = DEFAULT_MAXIMUM_CASES;

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return List.of(Tree.Kind.SWITCH_STATEMENT); // Visit switch
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitNode(Tree tree) {
        SwitchStatementTree switchTree = (SwitchStatementTree) tree;
        if (isSwitchOverEnum(switchTree)) { // If case clauses are enum
            return;
        }

        List<CaseGroupTree> cases = switchTree.cases();
        int size = cases.size();
        if (size > maximumCases) {
            reportIssue(switchTree.switchKeyword(), MESSAGE);
        }
    }

    private static boolean isSwitchOverEnum(SwitchStatementTree switchStatementTree) {
        Type type = switchStatementTree.expression().symbolType();
        return type.symbol().isEnum();
    }
}
