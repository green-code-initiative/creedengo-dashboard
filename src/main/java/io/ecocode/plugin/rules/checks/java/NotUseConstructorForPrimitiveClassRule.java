package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.LiteralTree;
import org.sonar.plugins.java.api.tree.NewClassTree;
import org.sonar.plugins.java.api.tree.Tree;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Rule(key = "GS_20")
@SuppressWarnings("java-greensight:GS_20")
public class NotUseConstructorForPrimitiveClassRule extends IssuableSubscriptionVisitor {

    private static final String MESSAGE = "do not use constructor for primitive class";
    private final List<String> listPrimitive = new ArrayList<>(Arrays.asList("String",
                                                                             "Byte",
                                                                             "Character",
                                                                             "Short",
                                                                             "Long",
                                                                             "Float",
                                                                             "Double",
                                                                             "Integer",
                                                                             "Boolean",
                                                                             "BigDecimal")); //List of primitive type
    private static final long MIN_BIG_INTEGER_VALUE = Long.MIN_VALUE;
    private static final long MAX_BIG_INTEGER_VALUE = Long.MAX_VALUE;

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.NEW_CLASS);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitNode(Tree tree) {
        NewClassTree classStatement = (NewClassTree) tree;
        if (listPrimitive.contains(classStatement.symbolType().name())) {
            reportIssue(classStatement.identifier(), MESSAGE);
        } else {
            if (!isBigIntegerPotentiallyBiggerThanLong(classStatement)) {
                reportIssue(classStatement.identifier(), MESSAGE);
            }
        }

    }

    /*
    Check if BigInteger is useful
    return true when it's useful, otherwise return false
     */
    // TODO: Condition on line 71 is weird because it is always false, someone need to look it up
    private static boolean isBigIntegerPotentiallyBiggerThanLong(NewClassTree newClassTree) {
        //if this not BigInteger, we pass
        if (!newClassTree.symbolType().is("java.math.BigInteger")) {
            return true;
        }
        //if argument it's not a string, we pass
        ExpressionTree argument = newClassTree.arguments().get(0);
        if (!argument.is(Tree.Kind.STRING_LITERAL)) {
            return true;
        }
        try {
            LiteralTree stringValue = (LiteralTree) argument;
            Long value = Long.parseLong(stringValue.value().replace("\"", ""));
            if (value < MIN_BIG_INTEGER_VALUE || value > MAX_BIG_INTEGER_VALUE) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return true;
        }
    }
}

