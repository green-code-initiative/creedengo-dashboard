package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Rule(key = "GS_17")
@SuppressWarnings("java-greensight:GS_17")
public class LoopOneBreakOrContinueRule extends BaseTreeVisitor implements JavaFileScanner {

    private static final String MESSAGE = "Only one break and continue in this loop allowed";

    private static final Deque<List<Tree>> breakAndContinueCounter = new ArrayDeque<>();
    private static final Deque<Boolean> currentScopeIsSwitch = new ArrayDeque<>();
    private int loopCount;

    private JavaFileScannerContext context;

    @Override
    public void scanFile(JavaFileScannerContext context) {
        this.context = context;
        loopCount = 0;
        scan(context.getTree());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitForStatement(ForStatementTree tree) {
        enterLoop();
        super.visitForStatement(tree);
        leaveLoop(tree.forKeyword());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitForEachStatement(ForEachStatement tree) {
        enterLoop();
        super.visitForEachStatement(tree);
        leaveLoop(tree.forKeyword());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitWhileStatement(WhileStatementTree tree) {
        enterLoop();
        super.visitWhileStatement(tree);
        leaveLoop(tree.whileKeyword());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitDoWhileStatement(DoWhileStatementTree tree) {
        enterLoop();
        super.visitDoWhileStatement(tree);
        leaveLoop(tree.doKeyword());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitBreakStatement(BreakStatementTree tree) {
        if (isInLoop() && !isInSwitch()) {
            incrementBreakCounter(tree);
        }
        super.visitBreakStatement(tree);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitContinueStatement(ContinueStatementTree tree) {
        if (isInLoop()) {
            incrementBreakCounter(tree);
        }
        super.visitContinueStatement(tree);
    }

    private boolean isInLoop() {
        return loopCount > 0;
    }

    private boolean isInSwitch() {
        return currentScopeIsSwitch.peek();
    }

    private void incrementBreakCounter(Tree tree) {
        if (!breakAndContinueCounter.isEmpty()) {
            breakAndContinueCounter.peek().add(tree);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void visitSwitchStatement(SwitchStatementTree tree) {
        currentScopeIsSwitch.push(true);
        super.visitSwitchStatement(tree);
        currentScopeIsSwitch.pop();
    }

    private void enterLoop() {
        loopCount++;
        breakAndContinueCounter.push(new ArrayList<>());
        currentScopeIsSwitch.push(false);
    }

    private void leaveLoop(Tree primaryLocationTree) {
        List<Tree> breakAndContinues = new ArrayList<>();
        if (!breakAndContinueCounter.isEmpty()) {
            breakAndContinues = breakAndContinueCounter.pop();
        }
        if (breakAndContinues.size() > 1) {
            context.reportIssue(this, primaryLocationTree, MESSAGE);
        }
        loopCount--;
        currentScopeIsSwitch.pop();
    }
}
