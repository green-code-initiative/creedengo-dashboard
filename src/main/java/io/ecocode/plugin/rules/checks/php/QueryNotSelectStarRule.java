package io.ecocode.plugin.rules.checks.php;

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.expression.LiteralTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import java.util.regex.Pattern;

@Rule(key = "GS_01")
@SuppressWarnings("php-greensight:GS_01")
public class QueryNotSelectStarRule extends PHPVisitorCheck {
    private static final String MESSAGE = "Ban SELECT * from your SQL queries";

    private static final Pattern REGEX = Pattern.compile(".*select.*from", Pattern.CASE_INSENSITIVE);

    @Override
    public void visitLiteral(LiteralTree tree) {
        if (tree.is(Tree.Kind.REGULAR_STRING_LITERAL) && REGEX.matcher(tree.value()).find() && tree.value()
            .contains("*")) {
            context().newIssue(this, tree, MESSAGE);
        }
        super.visitLiteral(tree);
    }
}
