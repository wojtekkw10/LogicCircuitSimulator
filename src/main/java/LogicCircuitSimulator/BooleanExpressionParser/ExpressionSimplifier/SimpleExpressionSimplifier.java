package LogicCircuitSimulator.BooleanExpressionParser.ExpressionSimplifier;

import LogicCircuitSimulator.BooleanExpressionParser.GrammarParser.BooleanExprLexer;
import LogicCircuitSimulator.BooleanExpressionParser.GrammarParser.BooleanExprParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class SimpleExpressionSimplifier implements ExpressionSimplifier{
    @Override
    public String simplify(String expression) {
        BooleanExprLexer lexer = new BooleanExprLexer(CharStreams.fromString(expression));
        BooleanExprParser parser = new BooleanExprParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.booleanExpression();

        ExpressionSimplifierVisitor simplifier = new ExpressionSimplifierVisitor();
        String simplified = simplifier.visit(tree);

        lexer = new BooleanExprLexer(CharStreams.fromString(simplified));
        parser = new BooleanExprParser(new CommonTokenStream(lexer));
        tree = parser.booleanExpression();
        simplified = simplifier.visit(tree);
        return simplified;
    }
}
