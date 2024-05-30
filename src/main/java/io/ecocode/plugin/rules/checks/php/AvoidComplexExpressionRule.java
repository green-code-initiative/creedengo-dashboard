package io.ecocode.plugin.rules.checks.php;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.expression.BinaryExpressionTree;
import org.sonar.plugins.php.api.tree.expression.ParenthesisedExpressionTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(key = "GS_09")
public class AvoidComplexExpressionRule extends PHPVisitorCheck {

    private static final int DEFAULT_MAX = 3; //Default maximum complexity
    private static final String MESSAGE = "Limit number of operator in expression";
    private int expressionCount;

    @RuleProperty(description = "Maximum complexity before raising an issue", defaultValue = DEFAULT_MAX + "")
    public int max = DEFAULT_MAX;

    @Override
    public void visitBinaryExpression(BinaryExpressionTree tree) {
        //on traite uniquement les expressions binaires qui n'ont pas pour parent d'autres expressions
        //ainsi si on les découpe sous forme d'abre on prend celui au somment pour parcourir ses enfants
        if (!tree.getParent()
            .is(Tree.Kind.CONDITIONAL_AND, Tree.Kind.CONDITIONAL_OR) && tree.is(Tree.Kind.CONDITIONAL_AND,
                                                                                Tree.Kind.CONDITIONAL_OR)) {
            expressionCount = 0;
            countExpressionsRecursive(tree);
            if (expressionCount > max) {
                context().newIssue(this, tree, MESSAGE);
            }
        }
        super.visitBinaryExpression(tree);
    }

    private void countExpressionsRecursive(BinaryExpressionTree tree) {
        /*
        Lorsque que l'ont parcours une expression, on regarde membre gauche et droite.
        Si l'expression est entre parenthese on analyse à l'interieur.
        Si le membre contient une autre expression binaire on refait appel à la fonction en lui passant le membre
        Ainsi on incrémentera la variable globale du nombre d'expressions binaires imbriquées
         */
        expressionCount++;
        Tree leftOperand = tree.leftOperand();
        Tree rightOperand = tree.rightOperand();
        if (leftOperand.is(Tree.Kind.PARENTHESISED_EXPRESSION)) {
            leftOperand = ((ParenthesisedExpressionTree) leftOperand).expression();
        }
        if (rightOperand.is(Tree.Kind.PARENTHESISED_EXPRESSION)) {
            rightOperand = ((ParenthesisedExpressionTree) rightOperand).expression();
        }
        if (leftOperand.is(Tree.Kind.CONDITIONAL_AND, Tree.Kind.CONDITIONAL_OR)) {
            countExpressionsRecursive((BinaryExpressionTree) leftOperand);
        }
        if (rightOperand.is(Tree.Kind.CONDITIONAL_AND, Tree.Kind.CONDITIONAL_OR)) {
            countExpressionsRecursive((BinaryExpressionTree) rightOperand);
        }
    }

}
