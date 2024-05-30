package io.ecocode.plugin.rules.checks.php;

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.expression.AssignmentExpressionTree;
import org.sonar.plugins.php.api.tree.expression.BinaryExpressionTree;
import org.sonar.plugins.php.api.tree.statement.BlockTree;
import org.sonar.plugins.php.api.tree.statement.ExpressionStatementTree;
import org.sonar.plugins.php.api.tree.statement.ForStatementTree;
import org.sonar.plugins.php.api.tree.statement.StatementTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@Rule(key = "GS_08")
public class ForLoopNotCallFuncInDeclarationRule extends PHPVisitorCheck {
    private static final String MESSAGE = "Do not call functions inside for loop condition or iteration";

    @Override
    @ParametersAreNonnullByDefault
    public void visitForStatement(ForStatementTree tree) {
        checkInit(tree);

        checkCondition(tree);

        checkUpdate(tree);

        checkStatements(tree);

        super.visitForStatement(tree);
    }

    private void checkInit(ForStatementTree tree){
        AssignmentExpressionTree assign = (AssignmentExpressionTree) tree.init().get(0);
        if(assign.value().is(Tree.Kind.FUNCTION_CALL) || assign.variable().is(Tree.Kind.FUNCTION_CALL)){
            context().newIssue(this, assign, MESSAGE);
        }
    }

    private void checkCondition(ForStatementTree tree){
        BinaryExpressionTree condition = (BinaryExpressionTree) tree.condition().get(0);
        if(condition.leftOperand().is(Tree.Kind.FUNCTION_CALL) || condition.rightOperand().is(Tree.Kind.FUNCTION_CALL)){
            context().newIssue(this, condition, MESSAGE);
        }
    }

    private void checkUpdate(ForStatementTree tree){
        if(tree.update().get(0).getKind().getAssociatedInterface().isAssignableFrom(AssignmentExpressionTree.class)) {
            AssignmentExpressionTree assign = (AssignmentExpressionTree) tree.update().get(0);
            if (assign.value().is(Tree.Kind.FUNCTION_CALL) || assign.variable().is(Tree.Kind.FUNCTION_CALL)) {
                context().newIssue(this, assign, MESSAGE);
            }
        }
    }

    private void checkStatements(ForStatementTree tree){
        List<StatementTree> statements = tree.statements();
        if(statements != null && statements.get(0).is(Tree.Kind.BLOCK)){
            BlockTree blockTree = (BlockTree) statements.get(0);
            blockTree.statements().forEach(statementTree -> {
                if(statementTree.is(Tree.Kind.EXPRESSION_STATEMENT)){
                    ExpressionStatementTree expressionStatementTree = (ExpressionStatementTree) statementTree;
                    if(expressionStatementTree.expression().is(Tree.Kind.FUNCTION_CALL)){
                        context().newIssue(this, expressionStatementTree, MESSAGE);
                    }
                }

            });
        }
    }
}
