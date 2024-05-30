package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.*;
import org.sonar.plugins.java.api.tree.Tree.Kind;

import java.util.List;
import java.util.regex.Pattern;

@Rule(key = "GS_06")
@SuppressWarnings("java-greensight:GS_06")
public class AvoidStatementsNestedRule extends BaseTreeVisitor implements JavaFileScanner {

    private JavaFileScannerContext context;

    private static final int DEFAULT_MAX = 3;
    private static final String MESSAGE = "Avoid nested statements";

    @RuleProperty(description = "Maximum allowed control flow statement nesting depth.",
                  defaultValue = "" + DEFAULT_MAX)
    public int max = DEFAULT_MAX;

    private static final Pattern NESTING_NAMES =
        Pattern.compile("^(?:IF|FOR|FOR_EACH|WHILE|DO|TRY|SWITCH)_STATEMENT" + ".*$",
                                                                 Pattern.CASE_INSENSITIVE);

    @Override
    public void scanFile(JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
    }

    @Override
    public void visitBlock(BlockTree tree) {
        checkBlock(tree.body(), 0);
        super.visitBlock(tree);
    }


    public void checkBlock(List<StatementTree> listStatements, int depth) {
        for (StatementTree statement : listStatements) {
            if (NESTING_NAMES.matcher(statement.kind().name()).find()) {
                getNewStatementWithKind(statement, depth);
            }
        }
    }

    public void getNewStatementWithKind(StatementTree newSt, int depth) {
        switch (newSt.kind().name()) {
            case "IF_STATEMENT":
                IfStatementTree ifSt = (IfStatementTree) newSt;
                checkIfRuleAccepted(ifSt.thenStatement(), depth);
                break;
            case "FOR_STATEMENT":
                ForStatementTree forSt = (ForStatementTree) newSt;
                checkIfRuleAccepted(forSt.statement(), depth);
                break;
            case "FOR_EACH_STATEMENT":
                ForEachStatement forEachSt = (ForEachStatement) newSt;
                checkIfRuleAccepted(forEachSt.statement(), depth);
                break;
            case "WHILE_STATEMENT":
                WhileStatementTree whileSt = (WhileStatementTree) newSt;
                checkIfRuleAccepted(whileSt.statement(), depth);
                break;
            case "DO_STATEMENT":
                DoWhileStatementTree doSt = (DoWhileStatementTree) newSt;
                checkIfRuleAccepted(doSt.statement(), depth);
                break;
            case "TRY_STATEMENT":
                handleTryStatement(newSt, depth);
                break;
            case "SWITCH_STATEMENT":
                SwitchStatementTree switchSt = (SwitchStatementTree) newSt;
                checkIfRuleAcceptedSwitchCase(switchSt, depth);
                break;
            default:
                break;
        }
    }

    private void handleTryStatement(StatementTree newSt, int depth) {
        TryStatementTree trySt = (TryStatementTree) newSt;
        checkIfRuleAccepted(trySt.block(), depth);
        if (trySt.catches().size() > 0) {
            checkIfRuleAccepted(trySt.catches().get(0).block(), depth);
        }
    }

    public void checkIfRuleAccepted(StatementTree tree, int depth) {
        depth++;
        if (depth > max) {
            if (tree.parent() == null) return;
            //noinspection ConstantConditions
            SyntaxToken first = tree.parent().firstToken();
            if (first == null) return;

            context.reportIssue(this, first, MESSAGE);
        } else {
            if (tree.is(Kind.BLOCK) && tree instanceof BlockTree) {
                BlockTree blockTree = (BlockTree) tree;
                checkBlock(blockTree.body(), depth);
            }
        }
    }

    public void checkIfRuleAcceptedSwitchCase(SwitchStatementTree switchSt, int depth) {
        depth++;
        if (depth > max) {
            if (switchSt.firstToken() == null) return;

            //noinspection ConstantConditions
            if (switchSt.firstToken().firstToken() == null) return;

            //noinspection ConstantConditions
            context.reportIssue(this, switchSt.firstToken().firstToken(), MESSAGE);
        } else {
            List<CaseGroupTree> caseGroup = switchSt.cases();
            for (CaseGroupTree cas : caseGroup) {
                checkBlock(cas.body(), depth);
            }
        }
    }
}
