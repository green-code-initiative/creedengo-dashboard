package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.ConditionalExpressionTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.Collections;
import java.util.List;

@Rule(key = "GS_11")
@SuppressWarnings("java-greensight:GS_11")
public class AvoidTernaryOperatorRule extends IssuableSubscriptionVisitor {

    private static final String MESSAGE = "Avoid using ternary operators";
    private int currentLine = -1;

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.CONDITIONAL_EXPRESSION);
    }

    /*
    "ConditionalExpressionTree: condition - questionToken - trueExpression - colonToken - falseExpression"
    We report an issue, if we encounter a questionToken, with the MESSAGE specified above
     */
    @Override
    public void visitNode(Tree tree){
        ConditionalExpressionTree conditionalExpression = (ConditionalExpressionTree) tree;
        //Sonar do not allow multiple report issue on the same line
        if(conditionalExpression.questionToken().line() != currentLine) {
            currentLine = conditionalExpression.questionToken().line();
            reportIssue(conditionalExpression.questionToken(), MESSAGE);
        }
    }
}
