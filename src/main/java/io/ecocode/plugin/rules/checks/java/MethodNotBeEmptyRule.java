package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.BlockTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.StatementTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Rule(key = "GS_03")
@SuppressWarnings("java-greensight:GS_03")
public class MethodNotBeEmptyRule extends IssuableSubscriptionVisitor {

    private static final String MESSAGE = "Avoid empty methods";

    @Override
    public List<Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.METHOD);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitNode(Tree tree) {
        MethodTree methodTree = (MethodTree) tree;
        BlockTree blockTree = methodTree.block();
        if (blockTree != null) {
            boolean methodNotEmpty = !isEmpty(blockTree);
            boolean commentExist = blockTree.closeBraceToken().trivias().size() > 0;
            if (!methodNotEmpty && !commentExist) {
                reportIssue(methodTree, MESSAGE, Collections.emptyList(), 1);
            }
        }
    }

    private static boolean isEmpty(BlockTree block) {
        List<StatementTree> body = block.body();
        return body.isEmpty() || body.stream().allMatch(stmt -> stmt.is(Kind.EMPTY_STATEMENT));
    }
}
