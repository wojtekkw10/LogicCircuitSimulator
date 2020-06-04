package LogicCircuitSimulator.BooleanExpressionParser.CircuitGenerator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleCircuitGeneratorTest {
    @Test
    void simpleTest(){
        CircuitGenerator circuitGenerator = new SimpleCircuitGenerator();
        circuitGenerator.generate("((a OR b) OR c) OR d");
    }

}