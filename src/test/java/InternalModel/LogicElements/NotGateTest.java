package InternalModel.LogicElements;

import InternalModel.LogicState;
import InternalModel.Vector2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotGateTest {
    LogicElement gate = new NotGate(10, 20, Rotation.RIGHT);

    @Test
    void inputPositionsTest(){
        List<Vector2D> positions = gate.getLocalInputPositions();

        assertEquals(0, positions.get(0).getX());
        assertEquals(0, positions.get(0).getY());
        assertEquals(1, positions.size());
    }

    @Test
    void outputPositionsTest(){
        List<Vector2D> positions = gate.getLocalOutputPositions();

        assertEquals(1, positions.get(0).getX());
        assertEquals(0, positions.get(0).getY());
        assertEquals(1, positions.size());
    }

    @Test
    void outputValueTest(){
        List<LogicState> states = new ArrayList<>();

        states.add(LogicState.LOW);
        ArrayList<LogicState> outputStates = gate.computeLocalValues(states);
        assertEquals(LogicState.HIGH, outputStates.get(0));

        states.set(0, LogicState.HIGH);
        outputStates = gate.computeLocalValues(states);
        assertEquals(LogicState.LOW, outputStates.get(0));
    }

}