package LogicCircuitSimulator.BooleanExpressionParser.ExpressionDepthFinder;

import LogicCircuitSimulator.BooleanExpressionParser.GrammarParser.BooleanExprLexer;
import LogicCircuitSimulator.BooleanExpressionParser.GrammarParser.BooleanExprParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class SimpleExpressionDepthFinder implements ExpressionDepthFinder{
    @Override
    public int findDepth(String expression) {
        BooleanExprLexer lexer = new BooleanExprLexer(CharStreams.fromString(expression));
        BooleanExprParser parser = new BooleanExprParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.booleanExpression();
        ExpressionDepthFinderVisitor depthFinder = new ExpressionDepthFinderVisitor();
        depthFinder.visit(tree);
        return depthFinder.getDepth() / 4;
    }
}
