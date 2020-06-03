package LogicCircuitSimulator.BooleanExpressionParser.ExpressionSimplifier;

import LogicCircuitSimulator.BooleanExpressionParser.GrammarParser.BooleanExprBaseVisitor;
import LogicCircuitSimulator.BooleanExpressionParser.GrammarParser.BooleanExprParser;
import org.antlr.v4.runtime.tree.ParseTree;

public class ExpressionSimplifierVisitor extends BooleanExprBaseVisitor<String> {

    @Override
    public String visitBooleanExpression(BooleanExprParser.BooleanExpressionContext ctx) {
        if(areParensNested(ctx.orExpression())){
            return visitOrExpression(ctx.orExpression().andExpression().valueExpression().orExpression());
        }
        return visitOrExpression(ctx.orExpression());
    }

    @Override
    public String visitAndExpression(BooleanExprParser.AndExpressionContext ctx) {
        String expr = "";
        if(ctx.AND() != null){
            expr = visitAndExpression(ctx.andExpression()) + " AND " + visitValueExpression(ctx.valueExpression());
        }
        else{
            expr = visitValueExpression(ctx.valueExpression());
        }
        return expr;
    }

    @Override
    public String visitOrExpression(BooleanExprParser.OrExpressionContext ctx) {
        String expr = "";
        if(ctx.OR() != null){
            expr = visitOrExpression(ctx.orExpression()) + " OR " + visitAndExpression(ctx.andExpression());
        }
        else{
            expr = visitAndExpression(ctx.andExpression());
        }
        return expr;
    }

    @Override
    public String visitValueExpression(BooleanExprParser.ValueExpressionContext ctx) {
        String expr = "";
        if(ctx.LEFT_PAREN() != null){
            if(!areParensNested(ctx.orExpression())){
                if(isTerminal(ctx.orExpression())){
                    expr  = visitOrExpression(ctx.orExpression());
                    System.out.println(isTerminal(ctx.orExpression()));
                    System.out.println(ctx.orExpression().getText());
                }
                else{
                    expr = "(" + visitOrExpression(ctx.orExpression()) + ")";
                }
            }
            else{
                expr = visitOrExpression(ctx.orExpression());
            }

        }
        else{
            expr = ctx.getText();
        }
        return expr;
    }

    private boolean areParensNested(BooleanExprParser.OrExpressionContext ctx){
        if(ctx.OR() == null){
            if(ctx.andExpression() != null && ctx.andExpression().AND() == null){
                if(ctx.andExpression().valueExpression() != null && ctx.andExpression().valueExpression().LEFT_PAREN() != null){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isTerminal(BooleanExprParser.OrExpressionContext ctx){
        if(ctx.OR() == null){
            if(ctx.andExpression() != null && ctx.andExpression().AND() == null){
                if(ctx.andExpression().valueExpression() != null && ctx.andExpression().valueExpression().orExpression() == null){
                    return true;
                }
            }
        }
        return false;
    }
}
