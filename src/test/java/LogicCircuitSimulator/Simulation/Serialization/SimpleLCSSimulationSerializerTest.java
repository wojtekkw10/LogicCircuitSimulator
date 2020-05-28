package LogicCircuitSimulator.Simulation.Serialization;

import LogicCircuitSimulator.Simulation.LCSSimulation;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElementHandler;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicClock;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.NotGate;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.XorGate;
import LogicCircuitSimulator.Simulation.NodeHandler.Crossing;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeHandler;
import LogicCircuitSimulator.Simulation.NodeHandler.WireState;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Simulation.SimpleLCSSimulation;
import LogicCircuitSimulator.Vector2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleLCSSimulationSerializerTest {
    @Test
    void serializeWireTest(){
        LCSSimulation simulation = new SimpleLCSSimulation();
        NodeHandler nodeHandler = simulation.getNodeHandler();
        nodeHandler.setDownWire(new Vector2D(20,30), WireState.LOW);
        nodeHandler.setRightWire(new Vector2D(40,45), WireState.HIGH);
        nodeHandler.setCrossing(new Vector2D(40,45), Crossing.NOT_TOUCHING);

        LCSSimulationSerializer simulationSerializer = new SimpleLCSSimulationSerializer();

        String serialized = simulationSerializer.serialize(simulation);

        assertTrue(serialized.contains("WI 20 30 NONE LOW TOUCHING"));
        assertTrue(serialized.contains("WI 40 45 HIGH NONE NOT_TOUCHING"));
    }

    @Test
    void deserializeWireTest(){
        String serialized = "WI 40 45 HIGH NONE NOT_TOUCHING \n" +
                "WI 20 30 NONE LOW TOUCHING ";
        LCSSimulation simulation = new SimpleLCSSimulationSerializer().deserialize(serialized);
        NodeHandler nodeHandler = simulation.getNodeHandler();

        assertEquals(Crossing.NOT_TOUCHING, nodeHandler.getCrossing(new Vector2D(40, 45)));
        assertEquals(Crossing.TOUCHING, nodeHandler.getCrossing(new Vector2D(20, 30)));

        assertEquals(WireState.NONE, nodeHandler.getDownWire(new Vector2D(40,45)));
        assertEquals(WireState.HIGH, nodeHandler.getRightWire(new Vector2D(40,45)));
        assertEquals(WireState.NONE, nodeHandler.getRightWire(new Vector2D(20,30)));
        assertEquals(WireState.LOW, nodeHandler.getDownWire(new Vector2D(20,30)));
    }

    @Test
    void serializeLogicElementTest(){
        LCSSimulation simulation = new SimpleLCSSimulation();
        LogicElementHandler logicElementHandler = simulation.getLogicElementHandler();
        logicElementHandler.add(new XorGate(30, 50, Rotation.UP));
        logicElementHandler.add(new LogicClock(324, 1, Rotation.LEFT));
        logicElementHandler.add(new NotGate(-45, -1, Rotation.DOWN));
        String serialized = new SimpleLCSSimulationSerializer().serialize(simulation);

        assertTrue(serialized.contains("LE XOR 30 50 UP"));
        assertTrue(serialized.contains("LE CLK 324 1 LEFT"));
        assertTrue(serialized.contains("LE NOT -45 -1 DOWN"));
    }

    @Test
    void deserializeLogicElementTest(){
        String serialized = "LE XOR 30 50 UP\n" +
                "LE CLK 324 1 LEFT\n" +
                "LE NOT -45 -1 DOWN";

        LCSSimulation simulation = new SimpleLCSSimulationSerializer().deserialize(serialized);

        Iterator<LogicElement> iterator = simulation.getLogicElementHandler().iterator();
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