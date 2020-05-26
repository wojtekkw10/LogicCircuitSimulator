package LogicCircuitSimulator.Simulation.Serialization;

import LogicCircuitSimulator.Simulation.LogicElements.LogicClock;
import LogicCircuitSimulator.Simulation.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation.LogicElements.NotGate;
import LogicCircuitSimulator.Simulation.LogicElements.XorGate;
import LogicCircuitSimulator.Simulation.NodeHandler.Crossing;
import LogicCircuitSimulator.Simulation.NodeHandler.WireState;
import LogicCircuitSimulator.Simulation.Orientation;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Simulation.Simulation;
import LogicCircuitSimulator.Vector2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimpleSimulationSerializerTest {
    @Test
    void serializeWireTest(){
        Simulation simulation = new Simulation();
        simulation.updateWire(new Vector2D(20,30), Orientation.VERTICALLY, WireState.LOW);
        simulation.updateWire(new Vector2D(40,45), Orientation.HORIZONTALLY, WireState.HIGH);
        simulation.updateCrossing(new Vector2D(40,45), Crossing.NOT_TOUCHING);

        SimulationSerializer simulationSerializer = new SimpleSimulationSerializer();

        String serialized = simulationSerializer.serialize(simulation);

        assertTrue(serialized.contains("WI 20 30 NONE LOW TOUCHING"));
        assertTrue(serialized.contains("WI 40 45 HIGH NONE NOT_TOUCHING"));
    }

    @Test
    void deserializeWireTest(){
        String serialized = "WI 40 45 HIGH NONE NOT_TOUCHING \n" +
                "WI 20 30 NONE LOW TOUCHING ";
        Simulation simulation = new SimpleSimulationSerializer().deserialize(serialized);

        assertEquals(Crossing.NOT_TOUCHING, simulation.getCrossing(new Vector2D(40, 45)));
        assertEquals(Crossing.TOUCHING, simulation.getCrossing(new Vector2D(20, 30)));

        assertEquals(WireState.NONE, simulation.getDownWire(new Vector2D(40,45)));
        assertEquals(WireState.HIGH, simulation.getRightWire(new Vector2D(40,45)));
        assertEquals(WireState.NONE, simulation.getRightWire(new Vector2D(20,30)));
        assertEquals(WireState.LOW, simulation.getDownWire(new Vector2D(20,30)));
    }

    @Test
    void serializeLogicElementTest(){
        Simulation simulation = new Simulation();
        simulation.addLogicGate(new XorGate(30, 50, Rotation.UP));
        simulation.addLogicGate(new LogicClock(324, 1, Rotation.LEFT));
        simulation.addLogicGate(new NotGate(-45, -1, Rotation.DOWN));
        String serialized = new SimpleSimulationSerializer().serialize(simulation);

        assertTrue(serialized.contains("LE XOR 30 50 UP"));
        assertTrue(serialized.contains("LE CLK 324 1 LEFT"));
        assertTrue(serialized.contains("LE NOT -45 -1 DOWN"));
    }

    @Test
    void deserializeLogicElementTest(){
        String serialized = "LE XOR 30 50 UP\n" +
                "LE CLK 324 1 LEFT\n" +
                "LE NOT -45 -1 DOWN";

        Simulation simulation = new SimpleSimulationSerializer().deserialize(serialized);

        Iterator<LogicElement> iterator = simulation.logicElementIterator();
        List<LogicElement> logicElements = new ArrayList<>();
        while(iterator.hasNext()){
            logicElements.add(iterator.next());
        }

        assertEquals("XOR", logicElements.get(1).getName());
        assertEquals(new Vector2D(30, 50), logicElements.get(1).getPosition());

        assertEquals("CLK", logicElements.get(2).getName());
        assertEquals(new Vector2D(324, 1), logicElements.get(2).getPosition());

        assertEquals("NOT", logicElements.get(0).getName());
        assertEquals(new Vector2D(-45, -1), logicElements.get(0).getPosition());
    }

}