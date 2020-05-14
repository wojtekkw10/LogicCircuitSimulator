package LogicCircuitSimulator.WireGrid;

import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Orientation;
import LogicCircuitSimulator.Simulation.NodeHandler.*;
import LogicCircuitSimulator.Vector2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ArrayNodeHandlerTest {

    @Test
    void setAndGetElementWithinInitialDimensionsTest() {
        //Arrange
        NodeHandler nodeHandler = new ArrayNodeHandler();

        //Act
        nodeHandler.setNode(new Node(new Vector2D(2,5), WireState.HIGH, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));

        //Assert
        assertEquals(WireState.HIGH, nodeHandler.getNode(new Vector2D(2,5)).getRightWire());
        assertEquals(WireState.LOW, nodeHandler.getNode(new Vector2D(2,5)).getDownWire());
        assertEquals(Node.WireCrossing.NOT_TOUCHING, nodeHandler.getNode(new Vector2D(2,5)).isTouching());

        assertEquals(WireState.NONE, nodeHandler.getNode(new Vector2D(1,5)).getRightWire());
        assertEquals(WireState.NONE, nodeHandler.getNode(new Vector2D(1,5)).getDownWire());
        assertEquals(Node.WireCrossing.NOT_TOUCHING, nodeHandler.getNode(new Vector2D(1,5)).isTouching());

        assertEquals(WireState.NONE, nodeHandler.getNode(new Vector2D(4,9)).getRightWire());
        assertEquals(WireState.NONE, nodeHandler.getNode(new Vector2D(4,9)).getDownWire());
        assertEquals(Node.WireCrossing.NOT_TOUCHING, nodeHandler.getNode(new Vector2D(4,9)).isTouching());
    }

    @Test
    void setAndGetElementOutsideOfInitialDimensionsTest() {
        //Arrange
        NodeHandler nodeHandler = new ArrayNodeHandler();

        //Act

        nodeHandler.setNode(new Node(new Vector2D(5,10), WireState.HIGH, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));

        //Assert
        assertEquals(WireState.HIGH, nodeHandler.getNode(new Vector2D(5,10)).getRightWire());
        assertEquals(WireState.LOW, nodeHandler.getNode(new Vector2D(5,10)).getDownWire());
        assertEquals(Node.WireCrossing.NOT_TOUCHING, nodeHandler.getNode(new Vector2D(5,10)).isTouching());
    }

    @Test
    void getElementOutsideOfInitialDimensionsTest(){
        NodeHandler nodeHandler = new ArrayNodeHandler();

        assertEquals(WireState.NONE, nodeHandler.getNode(new Vector2D(1,10)).getRightWire());
        assertEquals(WireState.NONE, nodeHandler.getNode(new Vector2D(1,10)).getDownWire());
        assertEquals(Node.WireCrossing.NOT_TOUCHING, nodeHandler.getNode(new Vector2D(1,10)).isTouching());

        assertEquals(WireState.NONE, nodeHandler.getNode(new Vector2D(10,1)).getRightWire());
        assertEquals(WireState.NONE, nodeHandler.getNode(new Vector2D(10,1)).getDownWire());
        assertEquals(Node.WireCrossing.NOT_TOUCHING, nodeHandler.getNode(new Vector2D(10,1)).isTouching());
    }

    @Test
    void getStateOutsideOfDimensionsTest(){
        NodeHandler nodeHandler = new ArrayNodeHandler();
        LogicState logicState1H = nodeHandler.getState(new Vector2D(100, 1), Orientation.HORIZONTALLY);
        LogicState logicState1V = nodeHandler.getState(new Vector2D(100, 1), Orientation.VERTICALLY);

        assertEquals(LogicState.LOW, logicState1H);
        assertEquals(LogicState.LOW, logicState1V);

        LogicState logicState2H = nodeHandler.getState(new Vector2D(1, 100), Orientation.HORIZONTALLY);
        LogicState logicState2V = nodeHandler.getState(new Vector2D(1, 100), Orientation.VERTICALLY);

        assertEquals(LogicState.LOW, logicState2H);
        assertEquals(LogicState.LOW, logicState2V);
    }

    @Test
    void getStateWithinDimensionsHorizontallyNotTouchingTest(){
        NodeHandler nodeHandler = new ArrayNodeHandler();

        nodeHandler.setNode(new Node(new Vector2D(5,5), WireState.HIGH, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));

        LogicState stateHLeft = nodeHandler.getState(new Vector2D(5,5), Orientation.HORIZONTALLY);
        LogicState stateVLeft = nodeHandler.getState(new Vector2D(5,5), Orientation.VERTICALLY);

        LogicState stateHRight = nodeHandler.getState(new Vector2D(6,5), Orientation.HORIZONTALLY);
        LogicState stateVRight = nodeHandler.getState(new Vector2D(6,5), Orientation.VERTICALLY);

        assertEquals(LogicState.HIGH, stateHLeft);
        assertEquals(LogicState.LOW, stateVLeft);

        assertEquals(LogicState.HIGH, stateHRight);
        assertEquals(LogicState.LOW, stateVRight);
    }

    @Test
    void getStateWithinDimensionsVerticallyNotTouchingTest(){
        NodeHandler nodeHandler = new ArrayNodeHandler();

        nodeHandler.setNode(new Node(new Vector2D(5,4), WireState.LOW, WireState.HIGH, Node.WireCrossing.NOT_TOUCHING));

        LogicState stateHUp = nodeHandler.getState(new Vector2D(5,4), Orientation.HORIZONTALLY);
        LogicState stateVUp = nodeHandler.getState(new Vector2D(5,4), Orientation.VERTICALLY);

        LogicState stateHDown = nodeHandler.getState(new Vector2D(5,5), Orientation.HORIZONTALLY);
        LogicState stateVDown = nodeHandler.getState(new Vector2D(5,5), Orientation.VERTICALLY);

        assertEquals(LogicState.HIGH, stateVUp);
        assertEquals(LogicState.LOW, stateHUp);

        assertEquals(LogicState.HIGH, stateVDown);
        assertEquals(LogicState.LOW, stateHDown);
    }

    @Test
    void getStateWithinDimensionsHorizontallyTouchingTest(){
        NodeHandler nodeHandler = new ArrayNodeHandler();

        nodeHandler.setNode(new Node(new Vector2D(5,5), WireState.HIGH, WireState.LOW, Node.WireCrossing.TOUCHING));

        LogicState stateHLeft = nodeHandler.getState(new Vector2D(5,5), Orientation.HORIZONTALLY);
        LogicState stateVLeft = nodeHandler.getState(new Vector2D(5,5), Orientation.VERTICALLY);

        LogicState stateHRight = nodeHandler.getState(new Vector2D(6,5), Orientation.HORIZONTALLY);
        LogicState stateVRight = nodeHandler.getState(new Vector2D(6,5), Orientation.VERTICALLY);

        assertEquals(LogicState.HIGH, stateHLeft);
        assertEquals(LogicState.HIGH, stateVLeft);

        assertEquals(LogicState.HIGH, stateHRight);
        assertEquals(LogicState.LOW, stateVRight);

    }

    @Test
    void getStateWithinDimensionsVerticallyTouchingTest(){
        NodeHandler nodeHandler = new ArrayNodeHandler();

        nodeHandler.setNode(new Node(new Vector2D(5,4), WireState.LOW, WireState.HIGH, Node.WireCrossing.TOUCHING));

        LogicState stateHUp = nodeHandler.getState(new Vector2D(5,4), Orientation.HORIZONTALLY);
        LogicState stateVUp = nodeHandler.getState(new Vector2D(5,4), Orientation.VERTICALLY);

        LogicState stateHDown = nodeHandler.getState(new Vector2D(5,5), Orientation.HORIZONTALLY);
        LogicState stateVDown = nodeHandler.getState(new Vector2D(5,5), Orientation.VERTICALLY);

        assertEquals(LogicState.HIGH, stateVUp);
        assertEquals(LogicState.HIGH, stateHUp);

        assertEquals(LogicState.HIGH, stateVDown);
        assertEquals(LogicState.LOW, stateHDown);

    }

    @Test
    void getStateAtEdgesNotTouchingTest(){
        NodeHandler nodeHandler = new ArrayNodeHandler();

        //Vertically
        nodeHandler.setNode(new Node(new Vector2D(0,0), WireState.LOW, WireState.HIGH, Node.WireCrossing.NOT_TOUCHING));

        LogicState stateH = nodeHandler.getState(new Vector2D(0,0), Orientation.HORIZONTALLY);
        LogicState stateV = nodeHandler.getState(new Vector2D(0,0), Orientation.VERTICALLY);

        assertEquals(LogicState.HIGH, stateV);
        assertEquals(LogicState.LOW, stateH);

        //Horizontally
        nodeHandler.setNode(new Node(new Vector2D(0,0), WireState.HIGH, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));

        stateH = nodeHandler.getState(new Vector2D(0,0), Orientation.HORIZONTALLY);
        stateV = nodeHandler.getState(new Vector2D(0,0), Orientation.VERTICALLY);

        assertEquals(LogicState.LOW, stateV);
        assertEquals(LogicState.HIGH, stateH);
    }

    @Test
    void getStateAtEdgesTouchingTest(){
        NodeHandler nodeHandler = new ArrayNodeHandler();

        //Vertically
        nodeHandler.setNode(new Node(new Vector2D(0,0), WireState.LOW, WireState.HIGH, Node.WireCrossing.TOUCHING));

        LogicState stateH = nodeHandler.getState(new Vector2D(0,0), Orientation.HORIZONTALLY);
        LogicState stateV = nodeHandler.getState(new Vector2D(0,0), Orientation.VERTICALLY);

        assertEquals(LogicState.HIGH, stateV);
        assertEquals(LogicState.HIGH, stateH);

        //Horizontally
        nodeHandler.setNode(new Node(new Vector2D(0,0), WireState.HIGH, WireState.LOW, Node.WireCrossing.TOUCHING));

        stateH = nodeHandler.getState(new Vector2D(0,0), Orientation.HORIZONTALLY);
        stateV = nodeHandler.getState(new Vector2D(0,0), Orientation.VERTICALLY);

        assertEquals(LogicState.HIGH, stateV);
        assertEquals(LogicState.HIGH, stateH);
    }

    @Test
    void getStateEdgeCasesTouchingTest(){
        NodeHandler nodeHandler = new ArrayNodeHandler();

        //Horizontally
        nodeHandler.setNode(new Node(new Vector2D(5,5), WireState.HIGH, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(6,5), WireState.NONE, WireState.NONE, Node.WireCrossing.TOUCHING));

        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(6,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(6,5), Orientation.VERTICALLY));

        //Vertically
        nodeHandler = new ArrayNodeHandler();

        //Horizontally
        nodeHandler.setNode(new Node(new Vector2D(5,4), WireState.NONE, WireState.HIGH, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,5), WireState.NONE, WireState.NONE, Node.WireCrossing.TOUCHING));

        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(5,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(5,5), Orientation.VERTICALLY));
    }

    @Test
    void resetWiresToLowTest(){
        NodeHandler nodeHandler = new ArrayNodeHandler();

        nodeHandler.setNode(new Node(new Vector2D(5,5), WireState.HIGH, WireState.HIGH, Node.WireCrossing.TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(1,1), WireState.HIGH, WireState.HIGH, Node.WireCrossing.NOT_TOUCHING));

        Iterator<Node> it = nodeHandler.iterator();

        boolean containsHigh = false;
        while(it.hasNext()) {
            Node node = it.next();
            if (node.getRightWire() == WireState.HIGH || node.getDownWire() == WireState.HIGH)
                containsHigh = true;
            double x = node.getPosition().getX();
            double y = node.getPosition().getY();
            if(nodeHandler.getState(new Vector2D(x, y), Orientation.VERTICALLY) == LogicState.HIGH)
                containsHigh = true;
            if(nodeHandler.getState(new Vector2D(x, y), Orientation.HORIZONTALLY) == LogicState.HIGH)
                containsHigh = true;
        }

        assertTrue(containsHigh);

        List<Generator> generators = new ArrayList<>();
        nodeHandler.propagateGenerators(generators);

        containsHigh = false;
        while(it.hasNext()) {
            Node node = it.next();
            if (node.getRightWire() == WireState.HIGH || node.getDownWire() == WireState.HIGH)
                containsHigh = true;
            double x = node.getPosition().getX();
            double y = node.getPosition().getY();
            if(nodeHandler.getState(new Vector2D(x, y), Orientation.VERTICALLY) == LogicState.HIGH)
                containsHigh = true;
            if(nodeHandler.getState(new Vector2D(x, y), Orientation.HORIZONTALLY) == LogicState.HIGH)
                containsHigh = true;
        }

        assertFalse(containsHigh);

    }

    @Test
    void propagationNotTouchingTest(){
        NodeHandler nodeHandler = new ArrayNodeHandler();

        nodeHandler.setNode(new Node(new Vector2D(5,5), WireState.LOW, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(6,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(7,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(7,4), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(8,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(9,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,6), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,7), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(4,7), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,8), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,9), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,4), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,3), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(4,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(3,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));

        //HORIZONTALLY
        List<Generator> generators = new ArrayList<>();
        generators.add(new Generator(new Vector2D(5,5), Orientation.HORIZONTALLY));
        nodeHandler.propagateGenerators(generators);
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(9,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(9,5), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(5,9), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(5,9), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(7,4), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(7,4), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(4,7), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(4,7), Orientation.VERTICALLY));

        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(5,3), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(5,3), Orientation.VERTICALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(3,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(3,5), Orientation.VERTICALLY));

        //VERTICALLY
        generators = new ArrayList<>();
        generators.add(new Generator(new Vector2D(5,5), Orientation.VERTICALLY));
        nodeHandler.propagateGenerators(generators);
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(9,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(9,5), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(5,9), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(5,9), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(7,4), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(7,4), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(4,7), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(4,7), Orientation.VERTICALLY));

        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(5,3), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(5,3), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(3,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(3,5), Orientation.VERTICALLY));
    }

    @Test
    void propagationTouchingTest(){
        NodeHandler nodeHandler = new ArrayNodeHandler();

        nodeHandler.setNode(new Node(new Vector2D(5,5), WireState.LOW, WireState.LOW, Node.WireCrossing.TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(6,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(7,5), WireState.LOW, WireState.NONE, Node.WireCrossing.TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(7,4), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(8,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(9,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,6), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,7), WireState.NONE, WireState.LOW, Node.WireCrossing.TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(4,7), WireState.LOW, WireState.NONE, Node.WireCrossing.TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,8), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,9), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,4), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,3), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(4,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(3,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));

        //HORIZONTALLY
        List<Generator> generators = new ArrayList<>();
        generators.add(new Generator(new Vector2D(5,5), Orientation.HORIZONTALLY));
        nodeHandler.propagateGenerators(generators);
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(9,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(9,5), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(5,9), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(5,9), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(7,4), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(7,4), Orientation.VERTICALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(4,7), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(4,7), Orientation.VERTICALLY));

        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(5,3), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(5,3), Orientation.VERTICALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(3,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(3,5), Orientation.VERTICALLY));
    }

    @Test
    void propagateExceptions(){
        NodeHandler nodeHandler = new ArrayNodeHandler();
        List<Generator> generators = new ArrayList<>();

        generators.add(new Generator(new Vector2D(0,0), Orientation.HORIZONTALLY));
        assertDoesNotThrow( () -> {
            nodeHandler.propagateGenerators(generators);
        });

        generators.clear();
        generators.add(new Generator(new Vector2D(11,0), Orientation.HORIZONTALLY));
        assertDoesNotThrow( () -> {
            nodeHandler.propagateGenerators(generators);
        });

        generators.clear();
        generators.add(new Generator(new Vector2D(0,11), Orientation.HORIZONTALLY));
        assertDoesNotThrow( () -> {
            nodeHandler.propagateGenerators(generators);
        });

        generators.clear();
        generators.add(new Generator(new Vector2D(10,5), Orientation.HORIZONTALLY));
        assertDoesNotThrow( () -> {
            nodeHandler.propagateGenerators(generators);
        });

        generators.clear();
        generators.add(new Generator(new Vector2D(5,10), Orientation.HORIZONTALLY));
        assertDoesNotThrow( () -> {
            nodeHandler.propagateGenerators(generators);
        });
    }

    @Test
    void generatorOutsideOfDimensionsVertically(){
        NodeHandler nodeHandler = new ArrayNodeHandler();

        nodeHandler.setNode(new Node(new Vector2D(5,5), WireState.LOW, WireState.LOW, Node.WireCrossing.TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(6,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(7,5), WireState.LOW, WireState.NONE, Node.WireCrossing.TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(7,4), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(8,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(9,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,6), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,7), WireState.NONE, WireState.LOW, Node.WireCrossing.TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(4,7), WireState.LOW, WireState.NONE, Node.WireCrossing.TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,8), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,9), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,4), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,3), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(4,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(3,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));

        List<Generator> generators = new ArrayList<>();
        generators.add(new Generator(new Vector2D(5,10), Orientation.VERTICALLY));
        nodeHandler.propagateGenerators(generators);
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(9,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(9,5), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(5,9), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(5,9), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(7,4), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(7,4), Orientation.VERTICALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(4,7), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(4,7), Orientation.VERTICALLY));

        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(5,3), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(5,3), Orientation.VERTICALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(3,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(3,5), Orientation.VERTICALLY));
    }

    @Test
    void generatorOutsideOfDimensionsHorizontally(){
        NodeHandler nodeHandler = new ArrayNodeHandler();

        nodeHandler.setNode(new Node(new Vector2D(5,5), WireState.LOW, WireState.LOW, Node.WireCrossing.TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(6,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(7,5), WireState.LOW, WireState.NONE, Node.WireCrossing.TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(7,4), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(8,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(9,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,6), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,7), WireState.NONE, WireState.LOW, Node.WireCrossing.TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(4,7), WireState.LOW, WireState.NONE, Node.WireCrossing.TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,8), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,9), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,4), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,3), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(4,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(3,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));

        List<Generator> generators = new ArrayList<>();
        generators.add(new Generator(new Vector2D(10,5), Orientation.HORIZONTALLY));
        nodeHandler.propagateGenerators(generators);
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(9,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(9,5), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(5,9), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(5,9), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(7,4), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(7,4), Orientation.VERTICALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(4,7), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(4,7), Orientation.VERTICALLY));

        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(5,3), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(5,3), Orientation.VERTICALLY));
        assertEquals(LogicState.HIGH, nodeHandler.getState(new Vector2D(3,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, nodeHandler.getState(new Vector2D(3,5), Orientation.VERTICALLY));
    }

    @Test
    void toStringTest(){
        NodeHandler nodeHandler = new ArrayNodeHandler();

        nodeHandler.setNode(new Node(new Vector2D(5,5), WireState.LOW, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(6,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(7,5), WireState.LOW, WireState.NONE, Node.WireCrossing.TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(7,4), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(8,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(9,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,6), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,7), WireState.NONE, WireState.LOW, Node.WireCrossing.TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(4,7), WireState.LOW, WireState.NONE, Node.WireCrossing.TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,8), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,9), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,4), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(5,3), WireState.NONE, WireState.LOW, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(4,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(3,5), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));

        List<Generator> generators = new ArrayList<>();
        generators.add(new Generator(new Vector2D(5,5), Orientation.VERTICALLY));
        nodeHandler.propagateGenerators(generators);

        String expected = "+ + + + + + + \n" +
                "    \u001B[33;1m| \u001B[0m        \n" +
                "+ + + + + + + \n" +
                "    \u001B[33;1m| \u001B[0m  \u001B[38;5;245m| \u001B[0m    \n" +
                "+\u001B[38;5;245m-\u001B[0m+\u001B[38;5;245m-\u001B[0m+\u001B[38;5;245m-\u001B[0m+\u001B[38;5;245m-\u001B[0m*\u001B[38;5;245m-\u001B[0m+\u001B[38;5;245m-\u001B[0m+\u001B[38;5;245m-\u001B[0m\n" +
                "    \u001B[33;1m| \u001B[0m        \n" +
                "+ + + + + + + \n" +
                "    \u001B[33;1m| \u001B[0m        \n" +
                "+ *\u001B[33;1m-\u001B[0m* + + + + \n" +
                "    \u001B[33;1m| \u001B[0m        \n" +
                "+ + + + + + + \n" +
                "    \u001B[33;1m| \u001B[0m        \n" +
                "+ + + + + + + \n" +
                "    \u001B[33;1m| \u001B[0m        \n";

        assertEquals(expected, nodeHandler.toString());
    }

    @Test
    void iteratorTest(){
        //Arrange
        NodeHandler nodeHandler = new ArrayNodeHandler();
        nodeHandler.setNode(new Node(new Vector2D(0,0), WireState.HIGH, WireState.LOW, Node.WireCrossing.TOUCHING));
        nodeHandler.setNode(new Node(new Vector2D(1,1), WireState.LOW, WireState.HIGH, Node.WireCrossing.NOT_TOUCHING));
        Iterator<Node> iterator = nodeHandler.iterator();

        //Act & Assert
        assertTrue(iterator.hasNext());
        Node w = iterator.next();
        Vector2D pos = w.getPosition();
        assertEquals(WireState.HIGH, w.getRightWire());
        assertEquals(WireState.LOW, w.getDownWire());
        assertEquals(Node.WireCrossing.TOUCHING, w.isTouching());
        assertEquals(0, pos.getX());
        assertEquals(0, pos.getY());

        assertTrue(iterator.hasNext());
        w = iterator.next();
        pos = w.getPosition();
        assertEquals(WireState.LOW, w.getRightWire());
        assertEquals(WireState.HIGH, w.getDownWire());
        assertEquals(Node.WireCrossing.NOT_TOUCHING, w.isTouching());
        assertEquals(1, pos.getX());
        assertEquals(1, pos.getY());

        assertFalse(iterator.hasNext());

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void updateTest(){
        NodeHandler nodeHandler = new ArrayNodeHandler();

        nodeHandler.updateWire(new Vector2D(0,0), Orientation.HORIZONTALLY, WireState.LOW);
        nodeHandler.updateWire(new Vector2D(1,1), Orientation.VERTICALLY, WireState.HIGH);
        nodeHandler.updateWire(new Vector2D(100,100), Orientation.VERTICALLY, WireState.HIGH);
        nodeHandler.updateCrossing(new Vector2D(0,1), Node.WireCrossing.TOUCHING);
        nodeHandler.updateCrossing(new Vector2D(100,100), Node.WireCrossing.TOUCHING);

        assertEquals(WireState.LOW, nodeHandler.getNode(new Vector2D(0,0)).getRightWire());
        assertEquals(WireState.HIGH, nodeHandler.getNode(new Vector2D(1,1)).getDownWire());
        assertEquals(WireState.HIGH, nodeHandler.getNode(new Vector2D(100,100)).getDownWire());
        assertEquals(Node.WireCrossing.TOUCHING, nodeHandler.getNode(new Vector2D(0,1)).isTouching());
        assertEquals(Node.WireCrossing.TOUCHING, nodeHandler.getNode(new Vector2D(100,100)).isTouching());

    }
}