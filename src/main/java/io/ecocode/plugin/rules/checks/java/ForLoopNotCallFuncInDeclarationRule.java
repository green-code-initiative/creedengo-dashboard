package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.*;
import org.sonar.plugins.java.api.tree.Tree.Kind;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

@Rule(key = "GS_09")
@SuppressWarnings("java-greensight:GS_09")
public class ForLoopNotCallFuncInDeclarationRule extends IssuableSubscriptionVisitor {

    private static final String MESSAGE = "Do not call functions inside for loop condition or iteration";

    @Override
    public List<Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.FOR_STATEMENT);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitNode(Tree tree) {
        ForStatementTree forStatement = (ForStatementTree) tree;
        visitForStatement(forStatement);
    }

    public void visitForStatement(ForStatementTree forStatement) {
        if (forStatement.initializer().isEmpty()) return;

        StatementTree st = forStatement.initializer().get(0);
        if (st instanceof VariableTree) {
            VariableTree initializer = (VariableTree) st;
            if (forStatement.condition() instanceof BinaryExpressionTree) {
                BinaryExpressionTree condition = (BinaryExpressionTree) forStatement.condition();
                if (condition == null) return;
                if (forStatement.update().isEmpty()) return;

                ExpressionStatementTree updates = (ExpressionStatementTree) forStatement.update().get(0);
                ExpressionTree parentInitializer = initializer.initializer();
                if (parentInitializer == null) return;


                if (parentInitializer.is(Kind.METHOD_INVOCATION) || condition.leftOperand()
                    .is(Kind.METHOD_INVOCATION) || condition.rightOperand()
                    .is(Kind.METHOD_INVOCATION) || updates.expression().is(Kind.METHOD_INVOCATION)) {
                    reportIssue(forStatement.initializer(), forStatement.update(), MESSAGE);
                }
            }
        }
    }

}
