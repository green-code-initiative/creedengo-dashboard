package io.ecocode.plugin.rules.checks.php;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.php.api.tree.ScriptTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import javax.annotation.ParametersAreNonnullByDefault;

@Rule(key = "GS_06")
public class TooManyLinesInOneFile extends PHPVisitorCheck {

    private static final String MESSAGE =
        "File \"%s\" has %s lines, which is greater than %s authorized. Split it " + "into smaller files.";

    private static final int DEFAULT_THRESHOLD = 1000;

    @RuleProperty(description = "Maximum number of line in one file before raising an issue",
                  defaultValue = DEFAULT_THRESHOLD + "")
    public int maxNumberOfLineThreshold = DEFAULT_THRESHOLD;

    @Override
    @ParametersAreNonnullByDefault
    public void visitScript(ScriptTree tree) {
        long numberOfLines = context().getPhpFile().contents().lines().count();
        if (numberOfLines > maxNumberOfLineThreshold) {
            String fileName = context().getPhpFile().filename();
            context().newLineIssue(this,
                                   maxNumberOfLineThreshold + 1,
                                   String.format(MESSAGE, fileName, numberOfLines, maxNumberOfLineThreshold));
        }
        super.visitScript(tree);
    }

}
