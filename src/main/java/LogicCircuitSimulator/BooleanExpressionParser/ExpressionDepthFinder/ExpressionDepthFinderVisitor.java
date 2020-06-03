package LogicCircuitSimulator.BooleanExpressionParser.ExpressionDepthFinder;

import LogicCircuitSimulator.BooleanExpressionParser.GrammarParser.BooleanExprBaseVisitor;
import LogicCircuitSimulator.BooleanExpressionParser.GrammarParser.BooleanExprParser;


public class ExpressionDepthFinderVisitor extends BooleanExprBaseVisitor<Integer> {
    private int depth = 0;

    @Override
    public Integer visitValueExpression(BooleanExprParser.ValueExpressionContext ctx) {
        depth = Math.max(depth, ctx.depth());
        return super.visitValueExpression(ctx);
    }

    public int getDepth() {
        return depth;
    }
}
