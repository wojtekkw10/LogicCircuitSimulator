package LogicCircuitSimulator.LogicElements;

import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.LogicElements.BufferGate;
import LogicCircuitSimulator.Simulation.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Vector2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BufferGateTest {
    LogicElement gate = new BufferGate(10, 20, Rotation.RIGHT);

    @Test
    void inputPositionsTest(){
        List<Vector2D> positions = gate.getGeometry().getLocalInputPositions();

        assertEquals(new Vector2D(0,0), positions.get(0));
        assertEquals(1, positions.size());
    }

    @Test
    void outputPositionsTest(){
        List<Vector2D> positions = gate.getGeometry().getLocalOutputPositions();

        assertEquals(new Vector2D(1, 0), positions.get(0));
        assertEquals(1, positions.size());
    }

    @Test
    void outputValueTest(){
        List<LogicState> states = new ArrayList<>();

        states.add(LogicState.LOW);
        List<LogicState> outputStates = gate.computeLocalValues(states);
        assertEquals(LogicState.LOW, outputStates.get(0));

        states.set(0, LogicState.HIGH);
        outputStates = gate.computeLocalValues(states);
        assertEquals(LogicState.HIGH, outputStates.get(0));
    }

}