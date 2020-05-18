package LogicCircuitSimulator.NodeGrid;

import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.NodeHandler.Crossing;
import LogicCircuitSimulator.Simulation.NodeHandler.Node;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.ArrayNodeGrid;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.NodeGrid;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.UnboundGrid.UnboundHashMapGrid;
import LogicCircuitSimulator.Simulation.NodeHandler.WireState;
import LogicCircuitSimulator.Simulation.Orientation;
import LogicCircuitSimulator.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrayNodeGridTest {
    NodeGrid nodeGrid;

    @BeforeEach
    void init(){
        nodeGrid = new ArrayNodeGrid(new UnboundHashMapGrid<>());
        nodeGrid.setNode(new Node(new Vector2D(5,6), WireState.HIGH, WireState.LOW, Crossing.TOUCHING));
        nodeGrid.setNode(new Node(new Vector2D(4,6), WireState.HIGH, WireState.NONE, Crossing.NOT_TOUCHING));
        nodeGrid.setNode(new Node(new Vector2D(5,5), WireState.NONE, WireState.LOW, Crossing.NOT_TOUCHING));
    }

    private void setAndGetNodeTest(List<Node> nodes){
        NodeGrid nodeGrid = new ArrayNodeGrid(new UnboundHashMapGrid<>());
        for (Node node : nodes) {
            nodeGrid.setNode(node);
        }
        for (Node node : nodes) {
            assertEquals(node, nodeGrid.getNode(node.getPosition()));
        }
    }

    @Test
    void setAndGetNodeInFirstQuadrantTest(){
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(new Vector2D(5, 0), WireState.LOW, WireState.HIGH, Crossing.TOUCHING));
        nodes.add(new Node(new Vector2D(0, 6), WireState.HIGH, WireState.LOW, Crossing.TOUCHING));
        nodes.add(new Node(new Vector2D(8, 9), WireState.NONE, WireState.HIGH, Crossing.TOUCHING));
        nodes.add(new Node(new Vector2D(123_456, 654_321), WireState.LOW, WireState.NONE, Crossing.TOUCHING));
        setAndGetNodeTest(nodes);
    }

    @Test
    void setAndGetNodeInSecondQuadrantTest(){
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(new Vector2D(-5, 0), WireState.LOW, WireState.HIGH, Crossing.TOUCHING));
        nodes.add(new Node(new Vector2D(0, 6), WireState.HIGH, WireState.LOW, Crossing.TOUCHING));
        nodes.add(new Node(new Vector2D(-8, 9), WireState.NONE, WireState.HIGH, Crossing.TOUCHING));
        nodes.add(new Node(new Vector2D(-123_456, 654_321), WireState.LOW, WireState.NONE, Crossing.TOUCHING));
        setAndGetNodeTest(nodes);
    }

    @Test
    void setAndGetNodeInThirdQuadrantTest(){
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(new Vector2D(-5, 0), WireState.LOW, WireState.HIGH, Crossing.TOUCHING));
        nodes.add(new Node(new Vector2D(0, -6), WireState.HIGH, WireState.LOW, Crossing.TOUCHING));
        nodes.add(new Node(new Vector2D(-8, -9), WireState.NONE, WireState.HIGH, Crossing.TOUCHING));
        nodes.add(new Node(new Vector2D(-123_456, -654_321), WireState.LOW, WireState.NONE, Crossing.TOUCHING));
        setAndGetNodeTest(nodes);
    }

    @Test
    void setAndGetNodeInFourthQuadrantTest(){
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(new Vector2D(5, 0), WireState.LOW, WireState.HIGH, Crossing.TOUCHING));
        nodes.add(new Node(new Vector2D(0, -6), WireState.HIGH, WireState.LOW, Crossing.TOUCHING));
        nodes.add(new Node(new Vector2D(8, -9), WireState.NONE, WireState.HIGH, Crossing.TOUCHING));
        nodes.add(new Node(new Vector2D(123_456, -654_321), WireState.LOW, WireState.NONE, Crossing.TOUCHING));
        setAndGetNodeTest(nodes);
    }

    @Test
    void getLeftWireTest(){
        assertEquals(WireState.HIGH, nodeGrid.getLeftWire(new Vector2D(5,6)));
    }

    @Test
    void getRightWireTest(){
        assertEquals(WireState.HIGH, nodeGrid.getRightWire(new Vector2D(5,6)));
    }

    @Test
    void getUpWireTest(){
        assertEquals(WireState.LOW, nodeGrid.getUpWire(new Vector2D(5,6)));
    }

    @Test
    void getDownWireTest(){
        assertEquals(WireState.LOW, nodeGrid.getDownWire(new Vector2D(5,6)));
    }

    @Test
    void getCrossingTest(){
        assertEquals(Crossing.TOUCHING, nodeGrid.getCrossing(new Vector2D(5,6)));
        assertEquals(Crossing.NOT_TOUCHING, nodeGrid.getCrossing(new Vector2D(4,6)));
        assertEquals(Crossing.NOT_TOUCHING, nodeGrid.getCrossing(new Vector2D(5,5)));
    }

    @Test
    void setWireTest(){
        nodeGrid.setRightWire(new Vector2D(11, 11), WireState.HIGH);
        nodeGrid.setDownWire(new Vector2D(11, 11), WireState.HIGH);
        nodeGrid.setLeftWire(new Vector2D(11, 11), WireState.LOW);
        nodeGrid.setUpWire(new Vector2D(11, 11), WireState.NONE);

        assertEquals(WireState.HIGH, nodeGrid.getRightWire(new Vector2D(11,11)));
        assertEquals(WireState.HIGH, nodeGrid.getDownWire(new Vector2D(11,11)));
        assertEquals(WireState.LOW, nodeGrid.getLeftWire(new Vector2D(11,11)));
        assertEquals(WireState.NONE, nodeGrid.getUpWire(new Vector2D(11,11)));
    }

    @Test
    void setCrossingTest(){
        nodeGrid.setCrossing(new Vector2D(11, 11), Crossing.TOUCHING);
        assertEquals(Crossing.TOUCHING, nodeGrid.getCrossing(new Vector2D(11,11)));
        nodeGrid.setCrossing(new Vector2D(11, 11), Crossing.NOT_TOUCHING);
        assertEquals(Crossing.NOT_TOUCHING, nodeGrid.getCrossing(new Vector2D(11,11)));
    }

    @Test
    void getStateForVerticalWireNotTouchingCrossingTest(){
        nodeGrid.setDownWire(new Vector2D(20,20), WireState.HIGH);
        nodeGrid.setCrossing(new Vector2D(20, 20), Crossing.NOT_TOUCHING);
        nodeGrid.setCrossing(new Vector2D(20, 21), Crossing.NOT_TOUCHING);

        assertEquals(LogicState.HIGH, nodeGrid.getState(new Vector2D(20,20), Orientation.VERTICALLY));
        assertEquals(LogicState.HIGH, nodeGrid.getState(new Vector2D(20,21), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, nodeGrid.getState(new Vector2D(20,20), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, nodeGrid.getState(new Vector2D(20,21), Orientation.HORIZONTALLY));
    }

    @Test
    void getStateForVerticalWireTouchingCrossingTest(){
        nodeGrid.setDownWire(new Vector2D(20,20), WireState.HIGH);
        nodeGrid.setCrossing(new Vector2D(20, 20), Crossing.TOUCHING);
        nodeGrid.setCrossing(new Vector2D(20, 21), Crossing.TOUCHING);

        assertEquals(LogicState.HIGH, nodeGrid.getState(new Vector2D(20,20), Orientation.VERTICALLY));
        assertEquals(LogicState.HIGH, nodeGrid.getState(new Vector2D(20,21), Orientation.VERTICALLY));
        assertEquals(LogicState.HIGH, nodeGrid.getState(new Vector2D(20,20), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, nodeGrid.getState(new Vector2D(20,21), Orientation.HORIZONTALLY));
    }

    @Test
    void getStateForHorizontalWireNotTouchingCrossingTest(){
        nodeGrid.setRightWire(new Vector2D(20,20), WireState.HIGH);
        nodeGrid.setCrossing(new Vector2D(20, 20), Crossing.NOT_TOUCHING);
        nodeGrid.setCrossing(new Vector2D(21, 20), Crossing.NOT_TOUCHING);

        assertEquals(LogicState.LOW, nodeGrid.getState(new Vector2D(20,20), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, nodeGrid.getState(new Vector2D(21,20), Orientation.VERTICALLY));
        assertEquals(LogicState.HIGH, nodeGrid.getState(new Vector2D(20,20), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, nodeGrid.getState(new Vector2D(21,20), Orientation.HORIZONTALLY));
    }

    @Test
    void getStateForHorizontalWireTouchingCrossingTest(){
        nodeGrid.setRightWire(new Vector2D(20,20), WireState.HIGH);
        nodeGrid.setCrossing(new Vector2D(20, 20), Crossing.TOUCHING);
        nodeGrid.setCrossing(new Vector2D(21, 20), Crossing.TOUCHING);

        assertEquals(LogicState.HIGH, nodeGrid.getState(new Vector2D(20,20), Orientation.VERTICALLY));
        assertEquals(LogicState.HIGH, nodeGrid.getState(new Vector2D(21,20), Orientation.VERTICALLY));
        assertEquals(LogicState.HIGH, nodeGrid.getState(new Vector2D(20,20), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, nodeGrid.getState(new Vector2D(21,20), Orientation.HORIZONTALLY));
    }


}