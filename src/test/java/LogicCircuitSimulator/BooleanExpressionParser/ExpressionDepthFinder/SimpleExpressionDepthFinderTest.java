package LogicCircuitSimulator.BooleanExpressionParser.ExpressionDepthFinder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleExpressionDepthFinderTest {
    @Test
    void terminalDepth(){
        ExpressionDepthFinder depthFinder = new SimpleExpressionDepthFinder();
        int depth = depthFinder.findDepth("TRUE");

        assertEquals(1, depth);
    }

    @Test
    void simpleExpressionDepth(){
        ExpressionDepthFinder depthFinder = new SimpleExpressionDepthFinder();
        int depth = depthFinder.findDepth("(TRUE AND FALSE) AND TRUE");

        assertEquals(2, depth);
    }

    @Test
    void complexExpressionDepth(){
        ExpressionDepthFinder depthFinder = new SimpleExpressionDepthFinder();
        int depth = depthFinder.findDepth("FALSE OR ((TRUE AND FALSE) AND TRUE)");

        assertEquals(3, depth);
    }

    @Test
    void complexAndParallelExpressionDepth(){
        ExpressionDepthFinder depthFinder = new SimpleExpressionDepthFinder();
        int depth = depthFinder.findDepth("(FALSE OR ((TRUE AND FALSE) AND TRUE)) AND (FALSE OR ((TRUE AND FALSE) AND TRUE))");

        assertEquals(4, depth);
    }

    @Test
    void parallelExpressionDepth(){
        ExpressionDepthFinder depthFinder = new SimpleExpressionDepthFinder();
        int depth = depthFinder.findDepth("(FALSE OR TRUE) AND (TRUE AND FALSE)");

        assertEquals(2, depth);
    }

    @Test
    void parallelLongExpressionDepth(){
        ExpressionDepthFinder depthFinder = new SimpleExpressionDepthFinder();
        int depth = depthFinder.findDepth("(FALSE OR TRUE) AND (TRUE AND FALSE) AND (TRUE AND FALSE) AND (TRUE AND FALSE)");

        assertEquals(2, depth);
    }


}