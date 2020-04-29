package LogicCircuitSimulator.LogicElements;

import LogicCircuitSimulator.LogicState;
import LogicCircuitSimulator.Vector2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AndGateTest {
    LogicElement gate = new AndGate(10, 20, Rotation.RIGHT);

    @Test
    void inputPositionsTest(){
        List<Vector2D> positions = gate.getLocalInputPositions();

        assertEquals(new Vector2D(0,0), positions.get(0));
        assertEquals(new Vector2D(0,1), positions.get(1));
        assertEquals(2, positions.size());
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
        states.add(LogicState.HIGH);
        List<LogicState> outputStates = gate.computeLocalValues(states);
        assertEquals(LogicState.LOW, outputStates.get(0));

        states.clear();
        states.add(LogicState.HIGH);
        states.add(LogicState.LOW);
        outputStates = gate.computeLocalValues(states);
        assertEquals(LogicState.LOW, outputStates.get(0));

        states.clear();
        states.add(LogicState.LOW);
        states.add(LogicState.LOW);
        outputStates = gate.computeLocalValues(states);
        assertEquals(LogicState.LOW, outputStates.get(0));

        states.clear();
        states.add(LogicState.HIGH);
        states.add(LogicState.HIGH);
        outputStates = gate.computeLocalValues(states);
        assertEquals(LogicState.HIGH, outputStates.get(0));
    }

}