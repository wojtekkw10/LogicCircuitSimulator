// Generated from BooleanExpr.g4 by ANTLR 4.7.2

    package LogicCircuitSimulator.BooleanExpressionParser.GrammarParser;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link BooleanExprParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface BooleanExprVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link BooleanExprParser#booleanExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanExpression(BooleanExprParser.BooleanExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link BooleanExprParser#orExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpression(BooleanExprParser.OrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link BooleanExprParser#andExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(BooleanExprParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link BooleanExprParser#valueExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueExpression(BooleanExprParser.ValueExpressionContext ctx);
}