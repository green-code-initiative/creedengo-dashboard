package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.CaseGroupTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

@Rule(key = "GS_04")
@SuppressWarnings("java-greensight:GS_04")
public class SwitchAvoidOperationsInCaseRule extends IssuableSubscriptionVisitor {

    private static final int DEFAULT_MAX = 3;
    private static final String MESSAGE = "Limit instructions inside a switch case";

    @RuleProperty(description = "Maximum allowed operations in case", defaultValue = "" + DEFAULT_MAX)
    public int max = DEFAULT_MAX;

    @Override
    public List<Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.CASE_GROUP);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitNode(Tree tree) {
        CaseGroupTree caseGroupTree = (CaseGroupTree) tree;
        visitCaseGroup(caseGroupTree);
    }

    public void visitCaseGroup(CaseGroupTree caseGroupTree) {
        int nbOfExpression = caseGroupTree.body().size();
        if (nbOfExpression > max) {
            reportIssue(caseGroupTree, MESSAGE);
        }
    }
}
