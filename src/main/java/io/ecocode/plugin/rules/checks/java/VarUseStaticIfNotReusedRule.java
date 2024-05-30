package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.*;
import org.sonar.plugins.java.api.tree.Tree.Kind;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Rule(key = "GS_10")
@SuppressWarnings("java-greensight:GS_10")
public class VarUseStaticIfNotReusedRule extends IssuableSubscriptionVisitor {
    private static final String MESSAGE = "Make shared final class attributes static";

    @Override
    public List<Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.VARIABLE);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitNode(Tree tree) {
        VariableTree forStatement = (VariableTree) tree;
        visitVariable(forStatement);
    }

    public void visitVariable(VariableTree variableTree) {
        ModifiersTree modifiersList = variableTree.modifiers();

        List<String> arrayModifiersString = new ArrayList<>();

        for (ModifierTree mod : modifiersList) {
            if (!mod.is(Tree.Kind.ANNOTATION)) {
                ModifierKeywordTree modKeyword = (ModifierKeywordTree) mod;
                arrayModifiersString.add(modKeyword.modifier().name());
            }
        }

        String parentKind = variableTree.parent().kind().name();

        if (arrayModifiersString.contains("FINAL") && !arrayModifiersString.contains("STATIC") && parentKind.equals(
            "CLASS")) {
            reportIssue(variableTree.firstToken(), MESSAGE);
        }
    }

}
