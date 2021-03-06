package LogicCircuitSimulator.LogicElements;

import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicOne;
import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Vector2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogicOneTest {
    LogicElement gate = new LogicOne(10, 20, Rotation.RIGHT);

    @Test
    void inputPositionsTest(){
        List<Vector2D> positions = gate.getGeometry().getLocalInputPositions();

        assertEquals(0, positions.size());
    }

    @Test
    void outputPositionsTest(){
        List<Vector2D> positions = gate.getGeometry().getLocalOutputPositions();

        assertEquals(new Vector2D(1,0), positions.get(0));
        assertEquals(1, positions.size());
    }

    @Test
    void outputValueTest(){
        List<LogicState> states = new ArrayList<>();

        List<LogicState> outputStates = gate.computeLocalValues(states);
        assertEquals(LogicState.HIGH, outputStates.get(0));
    }

}