package LogicCircuitSimulator.BooleanExpressionParser;

import LogicCircuitSimulator.BooleanExpressionParser.GrammarParser.BooleanExprBaseVisitor;
import LogicCircuitSimulator.BooleanExpressionParser.GrammarParser.BooleanExprLexer;
import LogicCircuitSimulator.BooleanExpressionParser.GrammarParser.BooleanExprParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class CircuitDepthCounter extends BooleanExprBaseVisitor<Integer> {
    private int depth = 1;

    @Override
    public Integer visitValueExpression(BooleanExprParser.ValueExpressionContext ctx) {
        if(ctx.LEFT_PAREN() != null) depth++;
        return super.visitValueExpression(ctx);
    }

    public int getDepth(String expression){
        BooleanExprLexer lexer = new BooleanExprLexer(CharStreams.fromString(expression));
        BooleanExprParser parser = new BooleanExprParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.booleanExpression();
        visit(tree);
        return depth;
    }
}
