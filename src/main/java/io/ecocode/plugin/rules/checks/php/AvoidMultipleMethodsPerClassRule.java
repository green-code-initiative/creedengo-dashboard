package io.ecocode.plugin.rules.checks.php;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.php.api.PHPKeyword;
import org.sonar.plugins.php.api.tree.Tree.Kind;
import org.sonar.plugins.php.api.tree.declaration.ClassDeclarationTree;
import org.sonar.plugins.php.api.tree.declaration.ClassMemberTree;
import org.sonar.plugins.php.api.tree.declaration.ClassTree;
import org.sonar.plugins.php.api.tree.declaration.MethodDeclarationTree;
import org.sonar.plugins.php.api.tree.expression.AnonymousClassTree;
import org.sonar.plugins.php.api.tree.lexical.SyntaxToken;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import javax.annotation.ParametersAreNonnullByDefault;

@Rule(key = "GS_05")
public class AvoidMultipleMethodsPerClassRule extends PHPVisitorCheck {

    private static final String MESSAGE =
        "Class \"%s\" has %s methods, which is greater than %s authorized. Split " + "it" + " into smaller classes.";
    private static final String MESSAGE_ANONYMOUS_CLASS = "This anonymous class has %s methods, which is greater " +
        "than" + " %s authorized. Split it into smaller classes.";

    private static final int DEFAULT_THRESHOLD = 20;
    private static final boolean DEFAULT_NON_PUBLIC = true;

    @RuleProperty(key = "maximumMethodThreshold",
                  description = "How many function are allowed before raising an issue",
                  defaultValue = "" + DEFAULT_THRESHOLD)
    public int maximumMethodThreshold = DEFAULT_THRESHOLD;

    @RuleProperty(key = "countNonpublicMethods",
                  description = "Set to false if you want to count all function no matter if they are pubilc, " +
                      "protected or private",
                  defaultValue = "" + DEFAULT_NON_PUBLIC)
    public boolean countNonpublicMethods = DEFAULT_NON_PUBLIC;


    @Override
    @ParametersAreNonnullByDefault
    public void visitClassDeclaration(ClassDeclarationTree tree) {
        super.visitClassDeclaration(tree);
        if (tree.is(Kind.CLASS_DECLARATION, Kind.INTERFACE_DECLARATION)) {
            checkClass(tree);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitAnonymousClass(AnonymousClassTree tree) {
        super.visitAnonymousClass(tree);
        checkClass(tree);
    }

    private void checkClass(ClassTree tree) {
        int nbMethod = getNumberOfMethods(tree);

        if (nbMethod > maximumMethodThreshold) {
            String message;
            if (tree.is(Kind.ANONYMOUS_CLASS)) {
                message = String.format(MESSAGE_ANONYMOUS_CLASS, nbMethod, maximumMethodThreshold);
            } else {
                message = String.format(MESSAGE,
                                        ((ClassDeclarationTree) tree).name().text(),
                                        nbMethod,
                                        maximumMethodThreshold);
            }
            context().newIssue(this, tree.classToken(), message);
        }
    }

    private int getNumberOfMethods(ClassTree tree) {
        int nbMethod = 0;

        for (ClassMemberTree classMember : tree.members()) {
            if (classMember.is(Kind.METHOD_DECLARATION) && !isExcluded((MethodDeclarationTree) classMember)) {
                nbMethod++;
            }
        }

        return nbMethod;
    }

    /**
     * Return true if method is private or protected.
     */
    private boolean isExcluded(MethodDeclarationTree tree) {
        if (!countNonpublicMethods) {

            for (SyntaxToken modifierToken : tree.modifiers()) {
                String modifier = modifierToken.text();
                if (PHPKeyword.PROTECTED.getValue().equals(modifier) || PHPKeyword.PRIVATE.getValue()
                    .equals(modifier)) {
                    return true;
                }
            }
        }
        return false;
    }

}
