package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.CompilationUnitTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

@Rule(key = "GS_19")
@SuppressWarnings("java-greensight:GS_19")
public class OneClassInterfaceRule extends IssuableSubscriptionVisitor {

    @Override
    public List<Kind> nodesToVisit() {
        return Collections.singletonList(Kind.COMPILATION_UNIT);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitNode(Tree tree) {
        int types = ((CompilationUnitTree) tree).types().size();
        if (types > 1) {
            reportIssue(tree, "There must be one class or interface or enum per file !");
        }
    }

}
