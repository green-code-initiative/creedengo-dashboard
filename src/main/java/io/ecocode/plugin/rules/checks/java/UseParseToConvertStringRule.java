package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.NewClassTree;
import org.sonar.plugins.java.api.tree.Tree;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Rule(key = "GS_24")
@SuppressWarnings("java-greensight:GS_24")
public class UseParseToConvertStringRule extends IssuableSubscriptionVisitor {

    private static final String MESSAGE = "Parsing should be used to convert String to primitive";
    private final List<String> listPrimitive = new ArrayList<>(Arrays.asList("Byte",
                                                                             "Short",
                                                                             "Long",
                                                                             "Float",
                                                                             "Double",
                                                                             "Integer"));

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.NEW_CLASS);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitNode(Tree tree) {
        NewClassTree classStatement = (NewClassTree) tree;
        if (listPrimitive.contains(classStatement.symbolType().name())) {
            if (classStatement.arguments().get(0).symbolType().is("java.lang.String")) {
                reportIssue(classStatement.identifier(), MESSAGE);
            }
        }
    }
}

