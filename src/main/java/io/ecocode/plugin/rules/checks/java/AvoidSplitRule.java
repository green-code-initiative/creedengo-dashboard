package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;

import java.util.Collections;
import java.util.List;

@Rule(key = "GS_25")
@SuppressWarnings("java-greensight:GS_25")
public class AvoidSplitRule extends IssuableSubscriptionVisitor {

    @Override
    public List<Kind> nodesToVisit() {
        return Collections.singletonList(Kind.METHOD_INVOCATION);
    }

    @Override
    public void visitNode(Tree tree) {
        MethodInvocationTree call = (MethodInvocationTree) tree;
        String methodName = call.symbol().name();
        if("split".equals(methodName)){
            reportIssue(call, "Avoid using the method str.split");
        }
    }
}