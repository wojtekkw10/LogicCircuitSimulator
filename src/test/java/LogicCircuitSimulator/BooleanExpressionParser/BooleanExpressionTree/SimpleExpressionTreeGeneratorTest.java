package LogicCircuitSimulator.BooleanExpressionParser.BooleanExpressionTree;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleExpressionTreeGeneratorTest {
    @Test
    void oneLevelTree(){
        ExpressionTreeGenerator generator = new SimpleExpressionTreeGenerator();
        ExpressionNode node = generator.generate("a OR b");

        assertEquals(Operand.OR, node.getOperand());
        assertEquals("a", node.getFirstNode().getTerminalText());
        assertEquals("b", node.getSecondNode().getTerminalText());
    }

}