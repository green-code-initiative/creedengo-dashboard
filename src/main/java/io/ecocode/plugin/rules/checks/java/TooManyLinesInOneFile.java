package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.CompilationUnitTree;
import org.sonar.plugins.java.api.tree.Tree;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

@Rule(key = "GS_15")
@SuppressWarnings("java-greensight:GS_15")
public class TooManyLinesInOneFile extends IssuableSubscriptionVisitor {

    private static final int DEFAULT_LINES_THRESHOLD = 750;

    @RuleProperty(description = "Maximum number of lines in one file before raising an issue",
                  defaultValue = DEFAULT_LINES_THRESHOLD + "")
    public int threshold = DEFAULT_LINES_THRESHOLD;

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.COMPILATION_UNIT);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitNode(Tree tree) {
        CompilationUnitTree compilationUnitTree = (CompilationUnitTree) tree;

        // TODO: inspect this line below, eofToken is sometimes null which it shouldn't be according to Intellij IDEA
        //noinspection ConstantConditions
        if (compilationUnitTree.eofToken() == null) return;

        int linesPerFile = compilationUnitTree.eofToken().line();
        if (linesPerFile > threshold) {
            addIssue(threshold + 1, "THRESHOLD SURPASSED");
        }
    }
}
