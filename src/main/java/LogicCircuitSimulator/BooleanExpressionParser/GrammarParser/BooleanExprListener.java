// Generated from BooleanExpr.g4 by ANTLR 4.7.2

    package LogicCircuitSimulator.BooleanExpressionParser.GrammarParser;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link BooleanExprParser}.
 */
public interface BooleanExprListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link BooleanExprParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterBooleanExpression(BooleanExprParser.BooleanExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BooleanExprParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitBooleanExpression(BooleanExprParser.BooleanExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link BooleanExprParser#orExpression}.
	 * @param ctx the parse tree
	 */
	void enterOrExpression(BooleanExprParser.OrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BooleanExprParser#orExpression}.
	 * @param ctx the parse tree
	 */
	void exitOrExpression(BooleanExprParser.OrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link BooleanExprParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(BooleanExprParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BooleanExprParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(BooleanExprParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link BooleanExprParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void enterValueExpression(BooleanExprParser.ValueExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BooleanExprParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void exitValueExpression(BooleanExprParser.ValueExpressionContext ctx);
}