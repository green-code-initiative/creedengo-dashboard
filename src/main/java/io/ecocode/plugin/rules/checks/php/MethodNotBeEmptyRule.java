package io.ecocode.plugin.rules.checks.php;

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.declaration.FunctionDeclarationTree;
import org.sonar.plugins.php.api.tree.declaration.MethodDeclarationTree;
import org.sonar.plugins.php.api.tree.statement.BlockTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;


@Rule(key = "GS_03")
@SuppressWarnings("php-greensight:GS_03")
public class MethodNotBeEmptyRule extends PHPVisitorCheck {

    private static final String MESSAGE = "Avoid empty methods";

    @Override
    public void visitMethodDeclaration(MethodDeclarationTree methodTree) {//est appelé à chaque methode détectée
        Tree tree = methodTree.body();
        if (tree != null && tree.is(Tree.Kind.BLOCK)) { //on rentre dans le contenu entre {} de la methode
            BlockTree blockTree = (BlockTree) tree;
            checkEmpty(blockTree, methodTree);
        }
        super.visitMethodDeclaration(methodTree); //on appelle super pour permettre aux autres règles utilisant
        // visitMethodDeclaration de fonctionner
    }

    @Override
    public void visitFunctionDeclaration(FunctionDeclarationTree functionTree) {
        BlockTree blockTree = functionTree.body();
        if (blockTree != null) {
            checkEmpty(blockTree, functionTree);
        }
        super.visitFunctionDeclaration(functionTree);
    }

    private void checkEmpty(BlockTree blockTree, Tree tree) {
        if (blockTree.statements().isEmpty()) { //si le contenu est vide on déclenche une issue
            context().newIssue(this, tree, MESSAGE);
        }
    }

}
