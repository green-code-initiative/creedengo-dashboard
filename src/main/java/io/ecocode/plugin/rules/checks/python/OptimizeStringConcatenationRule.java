package io.ecocode.plugin.rules.checks.python;

import org.sonar.check.Rule;
import org.sonar.plugins.python.api.PythonSubscriptionCheck;
import org.sonar.plugins.python.api.SubscriptionContext;
import org.sonar.plugins.python.api.tree.*;

import javax.annotation.ParametersAreNonnullByDefault;

@Rule(key = "GS_03")
@SuppressWarnings("py-greensight:GS_03")
public class OptimizeStringConcatenationRule extends PythonSubscriptionCheck {

    private static final String MESSAGE = "Prefer str.join() to concatenate string";

    @Override
    @ParametersAreNonnullByDefault
    public void initialize(Context context) {
        // match everything with a =
        context.registerSyntaxNodeConsumer(Tree.Kind.ASSIGNMENT_STMT, ctx -> {
            AssignmentStatement as = (AssignmentStatement) ctx.syntaxNode();
            CheckAndRaise(ctx, as.assignedValue());
        });
        // match + in an assignment
        context.registerSyntaxNodeConsumer(Tree.Kind.COMPOUND_ASSIGNMENT, ctx -> {
            CompoundAssignmentStatement cas = (CompoundAssignmentStatement) ctx.syntaxNode();
            CheckAndRaise(ctx, cas.rhsExpression());

            // si un += est rencontré et que ce qui est à droite est un string
            if (cas.compoundAssignmentToken().value().equals("+=") && cas.rhsExpression()
                .is(Tree.Kind.STRING_LITERAL)) {
                ctx.addIssue(cas.compoundAssignmentToken(), MESSAGE);
            }
        });
        // match elements inside array
        context.registerSyntaxNodeConsumer(Tree.Kind.EXPRESSION_LIST, ctx -> {
            ExpressionList el = (ExpressionList) ctx.syntaxNode();
            for (Tree t : el.expressions()) {
                CheckAndRaise(ctx, t);
            }
        });
    }

    /**
     * Check and Raise if the provided Tree is a binary expression and contains at the left or right a string
     *
     * @param ctx  context, only used for report issue
     * @param tree tree object to test, object should be BinaryExpression
     */
    private void CheckAndRaise(SubscriptionContext ctx, Tree tree) {
        if (tree instanceof BinaryExpression) {
            BinaryExpression be = (BinaryExpression) tree;
            if (be.leftOperand() instanceof StringLiteral || be.rightOperand() instanceof StringLiteral) {
                // skip if expression contain a str.join
                if (IsStrJoin(be.leftOperand(), be.rightOperand())) return;
                // skip if operator is something else than +
                if (!be.operator().value().equals("+")) return;

                // if left or right is a binary expression, we need to do a recursive call
                // to check what's inside.
                if (be.leftOperand() instanceof BinaryExpression) {
                    CheckAndRaise(ctx, be.leftOperand());
                } else {
                    if (be.rightOperand() instanceof BinaryExpression) {
                        CheckAndRaise(ctx, be.rightOperand());
                    } else {
                        ctx.addIssue(be, MESSAGE);
                    }
                }
            }
        }
    }

    /**
     * Check if at least one of the provided expression is a str.join
     *
     * @param expres list of expression
     * @return true if at least one contain str.join call. false otherwise
     */
    private boolean IsStrJoin(Expression... expres) {
        for (Expression expre : expres) {
            if (expre instanceof CallExpression) {
                CallExpression call = (CallExpression) expre;
                if (call.callee() instanceof QualifiedExpression) {
                    QualifiedExpression qe = (QualifiedExpression) call.callee();
                    if (qe.name().name().equals("join")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
