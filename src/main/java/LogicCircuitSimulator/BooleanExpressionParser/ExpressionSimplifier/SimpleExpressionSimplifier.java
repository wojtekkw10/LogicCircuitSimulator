package LogicCircuitSimulator.BooleanExpressionParser.ExpressionSimplifier;

import LogicCircuitSimulator.BooleanExpressionParser.GrammarParser.BooleanExprLexer;
import LogicCircuitSimulator.BooleanExpressionParser.GrammarParser.BooleanExprParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;

public class SimpleExpressionSimplifier implements ExpressionSimplifier{
    @Override
    public String simplify(String expression) throws InvalidExpressionException {

        BooleanExprLexer lexer = new BooleanExprLexer(CharStreams.fromString(expression));

        BooleanExprParser parser = new BooleanExprParser(new CommonTokenStream(lexer));
        parser.setErrorHandler(new BailErrorStrategy());
        try{
            ParseTree tree = parser.booleanExpression();
            System.out.println(tree.getChild(0));
            ExpressionSimplifierVisitor simplifier = new ExpressionSimplifierVisitor();
            String simplified = simplifier.visit(tree);

            lexer = new BooleanExprLexer(CharStreams.fromString(simplified));
            parser = new BooleanExprParser(new CommonTokenStream(lexer));
            tree = parser.booleanExpression();
            simplified = simplifier.visit(tree);

            return simplified;
        }
        catch(ParseCancellationException | RecognitionException e){
            throw new InvalidExpressionException("Invalid expression.");
        }
    }
}
