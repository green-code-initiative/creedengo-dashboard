package io.ecocode.plugin.rules.checks.python;

import org.sonar.check.Rule;
import org.sonar.plugins.python.api.PythonSubscriptionCheck;
import org.sonar.plugins.python.api.tree.StringLiteral;
import org.sonar.plugins.python.api.tree.Tree;

import java.util.regex.Pattern;

@Rule(key = "GS_01")
@SuppressWarnings("py-greensight:GS_01")
public class QueryNotSelectStarRule extends PythonSubscriptionCheck {
    private static final String MESSAGE = "Ban SELECT * from your SQL queries";

    private static final Pattern REGEX = Pattern.compile(".*select.*from", Pattern.CASE_INSENSITIVE);

    @Override
    public void initialize(Context context) {
        context.registerSyntaxNodeConsumer(Tree.Kind.STRING_LITERAL, ctx -> {
            StringLiteral str = (StringLiteral) ctx.syntaxNode();
            if (REGEX.matcher(str.firstToken().value()).find() && str.firstToken().value().contains("*")) {
                ctx.addIssue(str, MESSAGE);
            }
        });
    }
}
