package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.BinaryExpressionTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.ParenthesizedTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@Rule(key = "GS_18")
@SuppressWarnings("java-greensight:GS_18")
public class AvoidComplexExpressionRule extends IssuableSubscriptionVisitor {

    private static final int DEFAULT_MAX = 3; //Default maximum complexity
    private static final String MESSAGE = "Limit number of operator in expression";
    private final Deque<ExpressionTree> expressionPile = new ArrayDeque<>(); //Pile for navigate in ExpressionTree

    @RuleProperty(description = "Maximum complexity before raising an issue", defaultValue = DEFAULT_MAX + "")
    public int max = DEFAULT_MAX;

    @Override
    public List<Tree.Kind> nodesToVisit() {
        List<Kind> kindList = new ArrayList<>();
        kindList.add(Tree.Kind.CONDITIONAL_AND);
        kindList.add(Tree.Kind.CONDITIONAL_OR);
        return Collections.unmodifiableList(kindList);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitNode(Tree tree) {
        BinaryExpressionTree binaryStatement = (BinaryExpressionTree) tree;
        if (binaryStatement.parent() == null) return;
        //noinspection ConstantConditions
        Tree.Kind parent = binaryStatement.parent().kind();

        //if parent was && or ||, this expression was already process
        if (!parent.name().equals("CONDITIONAL_AND") && !parent.name().equals("CONDITIONAL_OR")) {
            int CmpExp = 1;
            expressionPile.clear();
            expressionPile.add(binaryStatement.leftOperand());
            expressionPile.add(binaryStatement.rightOperand());
            //Loop until there are no more child with operator && and || in ExpressionTree or when 4 operators has
            // been detected
            while (!expressionPile.isEmpty() && CmpExp <= max) {
                CmpExp = CmpExp + findComplexityExpression(expressionPile.poll());
            }
            if (CmpExp > max) {
                reportIssue(binaryStatement, MESSAGE);
            }
        }


    }

    /*
    Detect "&&" and "||" in ExpressionTree and add their child in pile
    return 1 when "&&" or "|| are detected, otherwise return 0
     */
    private int findComplexityExpression(ExpressionTree expTree) {
        switch (expTree.kind().name()) {
            case "CONDITIONAL_AND": //detect ... && ...
            case "CONDITIONAL_OR": //detect ... || ...
                handleConditionnalOr(expTree);
                return 1;
            case "PARENTHESIZED_EXPRESSION": //detect (...)
                ParenthesizedTree Parenthesized = (ParenthesizedTree) expTree;
                expressionPile.add(Parenthesized.expression());
                break;
        }

        return 0;
    }

    private void handleConditionnalOr(ExpressionTree expTree) {
        BinaryExpressionTree binaryExpression = (BinaryExpressionTree) expTree;
        expressionPile.add(binaryExpression.leftOperand());
        expressionPile.add(binaryExpression.rightOperand());
    }

}

