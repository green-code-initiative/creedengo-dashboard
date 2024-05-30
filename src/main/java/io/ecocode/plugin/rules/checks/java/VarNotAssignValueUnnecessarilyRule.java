package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;
import org.sonar.plugins.java.api.tree.VariableTree;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

@Rule(key = "GS_08")
@SuppressWarnings("java-greensight:GS_08")
public class VarNotAssignValueUnnecessarilyRule extends IssuableSubscriptionVisitor {
    private static final String MESSAGE = "Do not declare variables that will not be used";

    @Override
    public List<Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.VARIABLE);
    }


    @Override
    @ParametersAreNonnullByDefault
    public void visitNode(Tree tree) {
        VariableTree variableTree = (VariableTree) tree;

        if (variableTree.symbol().usages().size() == 1) {
            List<IdentifierTree> listUsages = variableTree.symbol().usages();

            if (listUsages.get(0).parent().is(Kind.RETURN_STATEMENT)) {
                reportIssue(listUsages.get(0), MESSAGE);
            }
        }
    }
}
