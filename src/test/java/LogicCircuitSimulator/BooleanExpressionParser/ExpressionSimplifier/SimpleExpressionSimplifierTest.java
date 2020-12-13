package LogicCircuitSimulator.BooleanExpressionParser.ExpressionSimplifier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleExpressionSimplifierTest {
    @Test
    void simplifyTerminal() throws InvalidExpressionException {
        ExpressionSimplifier simplifier = new SimpleExpressionSimplifier();
        String result = simplifier.simplify("(TRUE)");

        assertEquals("TRUE", result);
    }

    @Test
    void simplifyNestedParensAroundTerminal() throws InvalidExpressionException {
        ExpressionSimplifier simplifier = new SimpleExpressionSimplifier();
        String result = simplifier.simplify("((((((((TRUE))))))))");

        assertEquals("TRUE", result);
    }

    @Test
    void simplifyNestedParensAroundComplexExpression() throws InvalidExpressionException {
        ExpressionSimplifier simplifier = new SimpleExpressionSimplifier();
        String result = simplifier.simplify("((TRUE OR ((FALSE))) AND (((FALSE))))");

        assertEquals("(TRUE OR FALSE) AND FALSE", result);
    }

    @Test
    void dontSimplifyParallelParens() throws InvalidExpressionException {
        ExpressionSimplifier simplifier = new SimpleExpressionSimplifier();
        String result = simplifier.simplify("(TRUE OR FALSE) AND (FALSE AND TRUE)");

        assertEquals("(TRUE OR FALSE) AND (FALSE AND TRUE)", result);
    }


}