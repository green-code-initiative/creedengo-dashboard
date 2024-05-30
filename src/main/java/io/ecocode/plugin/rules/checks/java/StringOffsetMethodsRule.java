package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

@Rule(key = "GS_26")
@SuppressWarnings("java-greensight:GS_26")
public class StringOffsetMethodsRule extends IssuableSubscriptionVisitor {

    private static int found;

    @Override
    public List<Kind> nodesToVisit() {
        found = 0;
        return Collections.singletonList(Kind.METHOD_INVOCATION);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitNode(Tree tree) {
        MethodInvocationTree call = (MethodInvocationTree) tree;
        String methodName = call.symbol().name();
        if (call.methodSelect().is(Kind.MEMBER_SELECT)) {
            MemberSelectExpressionTree mset = (MemberSelectExpressionTree) call.methodSelect();
            if (found == 0) {
                if ("indexOf".equals(methodName) || "lastIndexOf".equals(methodName)
                        || "startsWith".equals(methodName)) {
                    found = mset.identifier().identifierToken().line();
                }
            } else if ("substring".equals(methodName) && found == mset.identifier().identifierToken().line()) {
                reportIssue(call,
                        "String offset-based methods should be preferred for finding substrings from offsets");
                found = 0;
            } else {
                found = 0;
            }
        }

    }
}
