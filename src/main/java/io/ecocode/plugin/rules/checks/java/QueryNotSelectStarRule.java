package io.ecocode.plugin.rules.checks.java;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.LiteralTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.regex.Pattern;

@Rule(key = "GS_01")
@SuppressWarnings("java-greensight:GS_01")
public class QueryNotSelectStarRule extends BaseTreeVisitor implements JavaFileScanner
{
	private static final String MESSAGE = "Ban SELECT * from your SQL queries";
	private JavaFileScannerContext context;

	private static final Pattern REGEX = Pattern.compile(".*select.*from", Pattern.CASE_INSENSITIVE);

	@Override
	public void scanFile(JavaFileScannerContext context)
	{
		this.context = context;
		scan(context.getTree());
	}

	@Override
	public void visitLiteral(LiteralTree tree)
	{
		if
		(
			tree.is(Tree.Kind.STRING_LITERAL) && 
			REGEX.matcher(tree.value()).find() && 
			tree.value().contains("*")
		)
		{
			context.reportIssue(this, tree, MESSAGE);
		}
	}
}
