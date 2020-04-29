package LogicCircuitSimulator.LogicElements;

import LogicCircuitSimulator.LogicState;
import LogicCircuitSimulator.Vector2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotGateTest {
    LogicElement gate = new NotGate(10, 20, Rotation.RIGHT);

    @Test
    void inputPositionsTest(){
        List<Vector2D> positions = gate.getLocalInputPositions();

        assertEquals(new Vector2D(0,0), positions.get(0));
        assertEquals(1, positions.size());
    }

    @Test
    void outputPositionsTest(){
        List<Vector2D> positions = gate.getLocalOutputPositions();

        assertEquals(new Vector2D(1, 0), positions.get(0));
        assertEquals(1, positions.size());
    }

    @Test
    void outputValueTest(){
        List<LogicState> states = new ArrayList<>();

        states.add(LogicState.LOW);
        List<LogicState> outputStates = gate.computeLocalValues(states);
        assertEquals(LogicState.HIGH, outputStates.get(0));

        states.set(0, LogicState.HIGH);
        outputStates = gate.computeLocalValues(states);
        assertEquals(LogicState.LOW, outputStates.get(0));
    }

}