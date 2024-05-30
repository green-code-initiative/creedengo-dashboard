package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.*;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Deque;
import java.util.LinkedList;

@Rule(key = "GS_22")
@SuppressWarnings("java-greensight:GS_22")
public class NotConcatenatePlusStringJavaRule extends BaseTreeVisitor implements JavaFileScanner {

    private static final String MESSAGE = "Strings should not be concatenated using '+' in a loop";
    private JavaFileScannerContext context;
    private final Deque<Tree> loopLevel = new LinkedList<>();

    @Override
    public void scanFile(JavaFileScannerContext context) {
        this.context = context;
        loopLevel.clear();
        scan(context.getTree());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitAssignmentExpression(AssignmentExpressionTree tree) {
        if (!loopLevel.isEmpty() && isStringConcatenation(tree) && isNotLoopLocalVar(tree) && isNotArrayAccess(tree)) {
            context.reportIssue(this, tree, MESSAGE);
        }
        super.visitAssignmentExpression(tree);
    }

    private boolean isNotLoopLocalVar(AssignmentExpressionTree tree) {
        IdentifierTree idTree = getIdentifierTree(tree.variable());
        if (idTree == null) {
            return false;
        }
        Tree declaration = idTree.symbol().declaration();
        if (declaration == null) {
            return true;
        }
        Tree parent = declaration.parent();
        Tree parentLoop = loopLevel.peek();
        while (parent != null && !parent.equals(parentLoop)) {
            if (parent.is(Tree.Kind.CLASS,
                          Tree.Kind.ENUM,
                          Tree.Kind.INTERFACE,
                          Tree.Kind.METHOD,
                          Tree.Kind.LAMBDA_EXPRESSION)) {
                // declaration is necessarily not part of a loop
                return true;
            }
            parent = parent.parent();
        }
        return parent == null;
    }

    private static boolean isNotArrayAccess(AssignmentExpressionTree tree) {
        return !tree.variable().is(Tree.Kind.ARRAY_ACCESS_EXPRESSION);
    }

    @Nullable
    private static IdentifierTree getIdentifierTree(ExpressionTree tree) {
        tree = skipParentheses(tree);
        switch (tree.kind()) {
            case IDENTIFIER:
                return (IdentifierTree) tree;
            case MEMBER_SELECT:
                return getIdentifierTree(((MemberSelectExpressionTree) tree).expression());
            case ARRAY_ACCESS_EXPRESSION:
                return getIdentifierTree(((ArrayAccessExpressionTree) tree).expression());
            case METHOD_INVOCATION:
                return getIdentifierTree(((MethodInvocationTree) tree).methodSelect());
            default:
                // any other unsupported case
                return null;
        }
    }

    private static boolean isStringConcatenation(AssignmentExpressionTree tree) {
        return tree.symbolType().is("java.lang.String") && isConcatenation(tree);
    }

    private static boolean isConcatenation(AssignmentExpressionTree tree) {
        if (tree.is(Tree.Kind.ASSIGNMENT)) {
            ExpressionTree expressionTree = skipParentheses(tree.expression());
            return expressionTree.is(Tree.Kind.PLUS) && concatenateVariable(tree.variable(),
                                                                            (BinaryExpressionTree) expressionTree);
        }
        return tree.is(Tree.Kind.PLUS_ASSIGNMENT);
    }

    private static boolean concatenateVariable(ExpressionTree variable, BinaryExpressionTree plus) {
        return concatenateVariable(variable, plus.leftOperand()) || concatenateVariable(variable, plus.rightOperand());
    }

    private static boolean concatenateVariable(ExpressionTree variable, ExpressionTree operand) {
        if (operand.is(Tree.Kind.PLUS)) {
            return concatenateVariable(variable, (BinaryExpressionTree) operand);
        }
        return areEquivalent(variable, operand);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitForEachStatement(ForEachStatement tree) {
        loopLevel.push(tree);
        super.visitForEachStatement(tree);
        loopLevel.pop();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitForStatement(ForStatementTree tree) {
        loopLevel.push(tree);
        super.visitForStatement(tree);
        loopLevel.pop();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitWhileStatement(WhileStatementTree tree) {
        loopLevel.push(tree);
        super.visitWhileStatement(tree);
        loopLevel.pop();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitDoWhileStatement(DoWhileStatementTree tree) {
        loopLevel.push(tree);
        super.visitDoWhileStatement(tree);
        loopLevel.pop();
    }

    public static ExpressionTree skipParentheses(ExpressionTree tree) {
        ExpressionTree result = tree;
        while (result.is(Tree.Kind.PARENTHESIZED_EXPRESSION)) {
            result = ((ParenthesizedTree) result).expression();
        }
        return result;
    }

    /**
     * return true, if nodes are syntactically equivalent
     */
    private static boolean areEquivalent(ExpressionTree leftNode, ExpressionTree rightNode) {
        if (leftNode == rightNode) {
            return true;
        }
        if (leftNode == null || rightNode == null) {
            return false;
        }
        if (leftNode.kind() != rightNode.kind() || leftNode.is(Tree.Kind.OTHER)) {
            return false;
        }
        return true;
    }

}
