package LogicCircuitSimulator.BooleanExpressionParser.BooleanExpressionTree;

import LogicCircuitSimulator.BooleanExpressionParser.GrammarParser.BooleanExprLexer;
import LogicCircuitSimulator.BooleanExpressionParser.GrammarParser.BooleanExprParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class SimpleExpressionTreeGenerator implements ExpressionTreeGenerator{
    @Override
    public ExpressionNode generate(String expression) {
        BooleanExprLexer lexer = new BooleanExprLexer(CharStreams.fromString(expression));
        BooleanExprParser parser = new BooleanExprParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.booleanExpression();
        ExpressionTreeGeneratorVisitor treeGeneratorVisitor = new ExpressionTreeGeneratorVisitor();
        return treeGeneratorVisitor.visit(tree);
    }
}
