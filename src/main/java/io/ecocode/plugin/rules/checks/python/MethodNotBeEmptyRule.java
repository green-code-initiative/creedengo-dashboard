package io.ecocode.plugin.rules.checks.python;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.sonar.check.Rule;
import org.sonar.plugins.python.api.PythonSubscriptionCheck;
import org.sonar.plugins.python.api.tree.FunctionDef;
import org.sonar.plugins.python.api.tree.Token;
import org.sonar.plugins.python.api.tree.Tree;
import org.sonar.python.tree.TreeUtils;

@Rule(key = "GS_02")
@SuppressWarnings("py-greensight:GS_02")
public class MethodNotBeEmptyRule extends PythonSubscriptionCheck {

    private static final String MESSAGE = "Add a nested comment explaining why this %s is empty, or complete the " +
        "implementation.";
    private static final List<String> ABC_DECORATORS = Arrays.asList("abstractmethod",
                                                                     "abstractstaticmethod",
                                                                     "abstractproperty",
                                                                     "abstractclassmethod");

    @Override
    public void initialize(Context context) {
        context.registerSyntaxNodeConsumer(Tree.Kind.FUNCDEF, ctx -> {
            FunctionDef functionDef = (FunctionDef) ctx.syntaxNode();
            if (functionDef.decorators()
                .stream()
                .map(d -> TreeUtils.decoratorNameFromExpression(d.expression()))
                .filter(Objects::nonNull)
                .flatMap(s -> Arrays.stream(s.split("\\.")))
                .anyMatch(ABC_DECORATORS::contains)) {
                return;
            }

            if (functionDef.body().statements().size() == 1 && functionDef.body()
                .statements()
                .get(0)
                .is(Tree.Kind.PASS_STMT)) {
                if (TreeUtils.tokens(functionDef).stream().anyMatch(t -> !t.trivia().isEmpty())) {
                    return;
                }
                if (hasCommentAbove(functionDef)) {
                    return;
                }
                String type = functionDef.isMethodDefinition() ? "method" : "function";
                ctx.addIssue(functionDef.name(), String.format(MESSAGE, type));
            }
        });
    }

    private static boolean hasCommentAbove(FunctionDef functionDef) {
        Tree parent = functionDef.parent();
        List<Token> tokens = TreeUtils.tokens(parent);
        Token defKeyword = functionDef.defKeyword();
        int index = tokens.indexOf(defKeyword);
        if (index == 0) {
            parent = parent.parent();
            tokens = TreeUtils.tokens(parent);
            index = tokens.indexOf(defKeyword);
        }
        return index > 0 && !tokens.get(index - 1).trivia().isEmpty();
    }
}
