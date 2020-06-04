package LogicCircuitSimulator.BooleanExpressionParser.BooleanExpressionTree;

import LogicCircuitSimulator.BooleanExpressionParser.GrammarParser.BooleanExprBaseVisitor;
import LogicCircuitSimulator.BooleanExpressionParser.GrammarParser.BooleanExprParser;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ExpressionTreeGeneratorVisitor extends BooleanExprBaseVisitor<ExpressionNode> {

    @Override
    public ExpressionNode visitBooleanExpression(BooleanExprParser.BooleanExpressionContext ctx) {
        return visitOrExpression(ctx.orExpression());
    }

    @Override
    public ExpressionNode visitOrExpression(BooleanExprParser.OrExpressionContext ctx) {
        if(ctx.OR() != null){
            ExpressionNode node = new ExpressionNode();
            node.setOperand(Operand.OR);
            ExpressionNode firstNode = visitOrExpression(ctx.orExpression());
            ExpressionNode secondNode = visitAndExpression(ctx.andExpression());
            node.setFirstNode(firstNode);
            node.setSecondNode(secondNode);
            return node;
        }
        else{
            return visitAndExpression(ctx.andExpression());
        }
    }

    @Override
    public ExpressionNode visitAndExpression(BooleanExprParser.AndExpressionContext ctx) {
        if(ctx.AND() != null){
            ExpressionNode node = new ExpressionNode();
            node.setOperand(Operand.AND);
            ExpressionNode firstNode = visitAndExpression(ctx.andExpression());
            ExpressionNode secondNode = visitValueExpression(ctx.valueExpression());
            node.setFirstNode(firstNode);
            node.setSecondNode(secondNode);
            return node;
        }
        else{
            return visitValueExpression(ctx.valueExpression());
        }
    }

    @Override
    public ExpressionNode visitValueExpression(BooleanExprParser.ValueExpressionContext ctx) {
        if(ctx.NOT() != null){
            ExpressionNode node = new ExpressionNode();
            node.setOperand(Operand.NOT);
            ExpressionNode firstNode = visitOrExpression(ctx.orExpression());
            node.setFirstNode(firstNode);
            return node;
        }
        else if(ctx.LEFT_PAREN() != null){
            return visitOrExpression(ctx.orExpression());
        }
        else{
            ExpressionNode node  = new ExpressionNode();
            node.setTerminalText(ctx.getText());
            return node;
        }
    }

    @Override
    public ExpressionNode visitTerminal(TerminalNode node) {
        ExpressionNode expressionNode = new ExpressionNode();
        expressionNode.setTerminalText(node.getText());
        return expressionNode;
    }
}
