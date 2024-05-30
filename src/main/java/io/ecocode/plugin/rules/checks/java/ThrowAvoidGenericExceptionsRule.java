package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Rule(key = "GS_05")
@SuppressWarnings("java-greensight:GS_05")
public class ThrowAvoidGenericExceptionsRule extends BaseTreeVisitor implements JavaFileScanner {

    private JavaFileScannerContext context;

    private static final Pattern GENERIC_EXCEPTIONS_NAMES = Pattern.compile(
        "^(java.lang.Error|java.lang.RuntimeException|java.lang.Throwable|java.lang.Exception).*$",
        Pattern.CASE_INSENSITIVE);
    private static final String MESSAGE = "Avoid generic exceptions";

    @Override
    public void scanFile(JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
    }

    @Override
    public void visitMethod(MethodTree tree) {
        ListTree<TypeTree> throwsList = tree.throwsClauses();
        String nameThrowToAvoid = "";
        for (TypeTree throwName : throwsList) {
            Matcher matcher = GENERIC_EXCEPTIONS_NAMES.matcher(throwName.symbolType().fullyQualifiedName());
            if (matcher.find()) {
                nameThrowToAvoid = throwName.symbolType().fullyQualifiedName();
            }
        }
        if (!nameThrowToAvoid.isEmpty()) {
            context.reportIssue(this, tree.throwsClauses(), MESSAGE);
        }
        super.visitMethod(tree);
    }

    @Override
    public void visitThrowStatement(ThrowStatementTree tree) {
        Matcher matcher = GENERIC_EXCEPTIONS_NAMES.matcher(tree.expression().symbolType().fullyQualifiedName());

        if (matcher.find()) {
            context.reportIssue(this, tree, MESSAGE);
        }
        super.visitThrowStatement(tree);
    }
}
