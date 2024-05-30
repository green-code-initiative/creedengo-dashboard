package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.*;
import org.sonar.plugins.java.api.tree.Tree.Kind;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

@Rule(key = "GS_21")
@SuppressWarnings("java-greensight:GS_21")
public class ObjectCreatedOnlyToCallGetClassRule extends IssuableSubscriptionVisitor {

    @Override
    public List<Kind> nodesToVisit() {
        return Collections.singletonList(Kind.METHOD_INVOCATION);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitNode(Tree tree) {
        MethodInvocationTree call = (MethodInvocationTree) tree;
        String methodName = call.symbol().name();
        if("getClass".equals(methodName)){
            if(call.methodSelect().is(Tree.Kind.MEMBER_SELECT)){
                ExpressionTree exprTree = ((MemberSelectExpressionTree) call.methodSelect()).expression();
                if(exprTree.is(Tree.Kind.NEW_CLASS, Tree.Kind.NEW_ARRAY)){
                    reportIssue(exprTree, "That method should not be used to create a new Class");
                }else if(exprTree.is(Tree.Kind.IDENTIFIER) && variableUsedOnlyToGetClass((IdentifierTree) exprTree)){
                    reportIssue(getInitializer((IdentifierTree)exprTree));
                }
            }
        }
    }

    @CheckForNull
    private static ExpressionTree getInitializer(IdentifierTree tree) {
        Symbol symbol = tree.symbol();
        if (symbol.isVariableSymbol()) {
            VariableTree declaration = ((Symbol.VariableSymbol) symbol).declaration();
            if (declaration != null) {
                return declaration.initializer();
            }
        }
        return null;
    }

    private static boolean variableUsedOnlyToGetClass(IdentifierTree tree) {
        if ("this".equals(tree.name()) || "super".equals(tree.name())) {
            return false;
        }
        Symbol symbol = tree.symbol();
        return symbol.usages().size() == 1 && hasBeenInitialized(tree);
    }

    private static boolean hasBeenInitialized(IdentifierTree tree) {
        ExpressionTree initializer = getInitializer(tree);
        return initializer != null && initializer.is(Tree.Kind.NEW_CLASS);
    }

    private void reportIssue(@Nullable ExpressionTree expressionTree) {
        if (expressionTree != null) {
            reportIssue(expressionTree,
                    "Remove this object instantiation and use \"" + getTypeName(expressionTree) + ".class\" instead");
        }
    }

    private static String getTypeName(ExpressionTree tree) {
        Type type = tree.symbolType();
        String name = getTypeName(type);
        if (name.isEmpty()) {
            name = getAnonymousClassTypeName(type.symbol());
        }
        return name;
    }

    private static String getAnonymousClassTypeName(Symbol.TypeSymbol symbol) {
        if (symbol.interfaces().isEmpty()) {
            return getTypeName(symbol.superClass());
        }
        return getTypeName(symbol.interfaces().get(0));
    }

    private static String getTypeName(Type type) {
        return type.symbol().name();
    }

}
