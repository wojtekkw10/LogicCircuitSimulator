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

    @Test
    void twoLevelTree1(){
        ExpressionTreeGenerator generator = new SimpleExpressionTreeGenerator();
        ExpressionNode node = generator.generate("(a OR b) AND c");

        assertEquals(Operand.AND, node.getOperand());
        assertEquals("c", node.getSecondNode().getTerminalText());
        assertEquals(Operand.OR, node.getFirstNode().getOperand());
        assertEquals("a", node.getFirstNode().getFirstNode().getTerminalText());
        assertEquals("b", node.getFirstNode().getSecondNode().getTerminalText());
    }

    @Test
    void twoLevelTree2(){
        ExpressionTreeGenerator generator = new SimpleExpressionTreeGenerator();
        ExpressionNode node = generator.generate("a AND (b OR c)");

        assertEquals(Operand.AND, node.getOperand());
        assertEquals("a", node.getFirstNode().getTerminalText());
        assertEquals(Operand.OR, node.getSecondNode().getOperand());
        assertEquals("b", node.getSecondNode().getFirstNode().getTerminalText());
        assertEquals("c", node.getSecondNode().getSecondNode().getTerminalText());
    }


}