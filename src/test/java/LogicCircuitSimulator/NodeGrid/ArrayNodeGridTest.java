package LogicCircuitSimulator.NodeGrid;

import LogicCircuitSimulator.Simulation.NodeHandler.Node;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.ArrayNodeGrid;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.NodeGrid;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.UnboundGrid.UnboundHashMapGrid;
import LogicCircuitSimulator.Simulation.NodeHandler.WireState;
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
        nodeGrid.setNode(new Node(new Vector2D(5,6), WireState.HIGH, WireState.LOW, Node.WireCrossing.TOUCHING));
        nodeGrid.setNode(new Node(new Vector2D(4,6), WireState.HIGH, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeGrid.setNode(new Node(new Vector2D(5,5), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
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
        nodes.add(new Node(new Vector2D(5, 0), WireState.LOW, WireState.HIGH, Node.WireCrossing.TOUCHING));
        nodes.add(new Node(new Vector2D(0, 6), WireState.HIGH, WireState.LOW, Node.WireCrossing.TOUCHING));
        nodes.add(new Node(new Vector2D(8, 9), WireState.NONE, WireState.HIGH, Node.WireCrossing.TOUCHING));
        nodes.add(new Node(new Vector2D(123_456, 654_321), WireState.LOW, WireState.NONE, Node.WireCrossing.TOUCHING));
        setAndGetNodeTest(nodes);
    }

    @Test
    void setAndGetNodeInSecondQuadrantTest(){
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(new Vector2D(-5, 0), WireState.LOW, WireState.HIGH, Node.WireCrossing.TOUCHING));
        nodes.add(new Node(new Vector2D(0, 6), WireState.HIGH, WireState.LOW, Node.WireCrossing.TOUCHING));
        nodes.add(new Node(new Vector2D(-8, 9), WireState.NONE, WireState.HIGH, Node.WireCrossing.TOUCHING));
        nodes.add(new Node(new Vector2D(-123_456, 654_321), WireState.LOW, WireState.NONE, Node.WireCrossing.TOUCHING));
        setAndGetNodeTest(nodes);
    }

    @Test
    void setAndGetNodeInThirdQuadrantTest(){
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(new Vector2D(-5, 0), WireState.LOW, WireState.HIGH, Node.WireCrossing.TOUCHING));
        nodes.add(new Node(new Vector2D(0, -6), WireState.HIGH, WireState.LOW, Node.WireCrossing.TOUCHING));
        nodes.add(new Node(new Vector2D(-8, -9), WireState.NONE, WireState.HIGH, Node.WireCrossing.TOUCHING));
        nodes.add(new Node(new Vector2D(-123_456, -654_321), WireState.LOW, WireState.NONE, Node.WireCrossing.TOUCHING));
        setAndGetNodeTest(nodes);
    }

    @Test
    void setAndGetNodeInFourthQuadrantTest(){
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(new Vector2D(5, 0), WireState.LOW, WireState.HIGH, Node.WireCrossing.TOUCHING));
        nodes.add(new Node(new Vector2D(0, -6), WireState.HIGH, WireState.LOW, Node.WireCrossing.TOUCHING));
        nodes.add(new Node(new Vector2D(8, -9), WireState.NONE, WireState.HIGH, Node.WireCrossing.TOUCHING));
        nodes.add(new Node(new Vector2D(123_456, -654_321), WireState.LOW, WireState.NONE, Node.WireCrossing.TOUCHING));
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
        assertEquals(Node.WireCrossing.TOUCHING, nodeGrid.getCrossing(new Vector2D(5,6)));
        assertEquals(Node.WireCrossing.NOT_TOUCHING, nodeGrid.getCrossing(new Vector2D(4,6)));
        assertEquals(Node.WireCrossing.NOT_TOUCHING, nodeGrid.getCrossing(new Vector2D(5,5)));
    }

    //TODO: setWire tests
    //TODO: setCrossingtest
    //TODO: getStatetest


}