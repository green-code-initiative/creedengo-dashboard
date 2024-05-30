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
import java.util.regex.Pattern;

@Rule(key = "GS_07")
@SuppressWarnings("java-greensight:GS_07")
public class VarNotIncrementOrDecrementUnusedRule extends IssuableSubscriptionVisitor {

    private static final Pattern KIND_NAMES = Pattern.compile(
        "^(POSTFIX_INCREMENT|PREFIX_INCREMENT|POSTFIX_DECREMENT|PREFIX_DECREMENT).*$",
        Pattern.CASE_INSENSITIVE);
    private static final String MESSAGE = "Do not increment nor decrement unused variables";

    @Override
    public List<Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.VARIABLE);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitNode(Tree tree) {
        VariableTree variableTree = (VariableTree) tree;
        List<IdentifierTree> listUsages = variableTree.symbol().usages();
        if (listUsages.size() > 0) {
            if (listUsages.size() == 1 && KIND_NAMES.matcher(listUsages.get(0).parent().kind().name()).find()) {
                reportIssue(listUsages.get(0).parent().parent(), MESSAGE);
            } else if (listUsages.size() == 2 && KIND_NAMES.matcher(listUsages.get(1).parent().kind().name())
                .find() && listUsages.get(0).symbol().metadata() == listUsages.get(1).symbol().metadata()) {
                reportIssue(listUsages.get(0).parent().parent(), MESSAGE);
            }
        }
    }
}
