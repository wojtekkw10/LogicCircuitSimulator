package LogicCircuitSimulator.BooleanExpressionParser.CircuitGenerator;

import LogicCircuitSimulator.BooleanExpressionParser.ExpressionSimplifier.InvalidExpressionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleCircuitGeneratorTest {
    @Test
    void simpleTest() throws InvalidExpressionException {
        CircuitGenerator circuitGenerator = new SimpleCircuitGenerator();
        circuitGenerator.generate("((a OR b) OR c) OR d");
    }

}