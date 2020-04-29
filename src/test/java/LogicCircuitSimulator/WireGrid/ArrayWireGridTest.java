package LogicCircuitSimulator.WireGrid;

import LogicCircuitSimulator.LogicState;
import LogicCircuitSimulator.Orientation;
import LogicCircuitSimulator.Vector2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ArrayWireGridTest {

    @Test
    void setAndGetElementWithinInitialDimensionsTest() {
        //Arrange
        WireGrid wireGrid = new ArrayWireGrid();

        //Act
        Node node = new Node(Node.State.HIGH, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING);
        wireGrid.setNode(new Vector2D(2,5), node);

        //Assert
        assertEquals(Node.State.HIGH, wireGrid.getNode(new Vector2D(2,5)).getRightWire());
        assertEquals(Node.State.LOW, wireGrid.getNode(new Vector2D(2,5)).getDownWire());
        assertEquals(Node.WireCrossing.NOT_TOUCHING, wireGrid.getNode(new Vector2D(2,5)).isTouching());

        assertEquals(Node.State.NONE, wireGrid.getNode(new Vector2D(1,5)).getRightWire());
        assertEquals(Node.State.NONE, wireGrid.getNode(new Vector2D(1,5)).getDownWire());
        assertEquals(Node.WireCrossing.NOT_TOUCHING, wireGrid.getNode(new Vector2D(1,5)).isTouching());

        assertEquals(Node.State.NONE, wireGrid.getNode(new Vector2D(4,9)).getRightWire());
        assertEquals(Node.State.NONE, wireGrid.getNode(new Vector2D(4,9)).getDownWire());
        assertEquals(Node.WireCrossing.NOT_TOUCHING, wireGrid.getNode(new Vector2D(4,9)).isTouching());
    }

    @Test
    void setAndGetElementOutsideOfInitialDimensionsTest() {
        //Arrange
        WireGrid wireGrid = new ArrayWireGrid();

        //Act

        Node node = new Node(Node.State.HIGH, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING);
        wireGrid.setNode(new Vector2D(5,10), node);

        //Assert
        assertEquals(Node.State.HIGH, wireGrid.getNode(new Vector2D(5,10)).getRightWire());
        assertEquals(Node.State.LOW, wireGrid.getNode(new Vector2D(5,10)).getDownWire());
        assertEquals(Node.WireCrossing.NOT_TOUCHING, wireGrid.getNode(new Vector2D(5,10)).isTouching());
    }

    @Test
    void getElementOutsideOfInitialDimensionsTest(){
        WireGrid wireGrid = new ArrayWireGrid();

        assertEquals(Node.State.NONE, wireGrid.getNode(new Vector2D(1,10)).getRightWire());
        assertEquals(Node.State.NONE, wireGrid.getNode(new Vector2D(1,10)).getDownWire());
        assertEquals(Node.WireCrossing.NOT_TOUCHING, wireGrid.getNode(new Vector2D(1,10)).isTouching());

        assertEquals(Node.State.NONE, wireGrid.getNode(new Vector2D(10,1)).getRightWire());
        assertEquals(Node.State.NONE, wireGrid.getNode(new Vector2D(10,1)).getDownWire());
        assertEquals(Node.WireCrossing.NOT_TOUCHING, wireGrid.getNode(new Vector2D(10,1)).isTouching());
    }

    @Test
    void getStateOutsideOfDimensionsTest(){
        WireGrid wireGrid = new ArrayWireGrid();
        LogicState logicState1H = wireGrid.getState(new Vector2D(100, 1), Orientation.HORIZONTALLY);
        LogicState logicState1V = wireGrid.getState(new Vector2D(100, 1), Orientation.VERTICALLY);

        assertEquals(LogicState.LOW, logicState1H);
        assertEquals(LogicState.LOW, logicState1V);

        LogicState logicState2H = wireGrid.getState(new Vector2D(1, 100), Orientation.HORIZONTALLY);
        LogicState logicState2V = wireGrid.getState(new Vector2D(1, 100), Orientation.VERTICALLY);

        assertEquals(LogicState.LOW, logicState2H);
        assertEquals(LogicState.LOW, logicState2V);
    }

    @Test
    void getStateException(){
        WireGrid wireGrid = new ArrayWireGrid();

        assertThrows(IllegalArgumentException.class, () -> {
            wireGrid.getState(new Vector2D(-1, 0), Orientation.HORIZONTALLY);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            wireGrid.getState(new Vector2D(0, -1), Orientation.HORIZONTALLY);
        });
    }

    @Test
    void getStateWithinDimensionsHorizontallyNotTouchingTest(){
        WireGrid wireGrid = new ArrayWireGrid();

        wireGrid.setNode(new Vector2D(5,5), new Node(Node.State.HIGH, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));

        LogicState stateHLeft = wireGrid.getState(new Vector2D(5,5), Orientation.HORIZONTALLY);
        LogicState stateVLeft = wireGrid.getState(new Vector2D(5,5), Orientation.VERTICALLY);

        LogicState stateHRight = wireGrid.getState(new Vector2D(6,5), Orientation.HORIZONTALLY);
        LogicState stateVRight = wireGrid.getState(new Vector2D(6,5), Orientation.VERTICALLY);

        assertEquals(LogicState.HIGH, stateHLeft);
        assertEquals(LogicState.LOW, stateVLeft);

        assertEquals(LogicState.HIGH, stateHRight);
        assertEquals(LogicState.LOW, stateVRight);
    }

    @Test
    void getStateWithinDimensionsVerticallyNotTouchingTest(){
        WireGrid wireGrid = new ArrayWireGrid();

        wireGrid.setNode(new Vector2D(5,4), new Node(Node.State.LOW, Node.State.HIGH, Node.WireCrossing.NOT_TOUCHING));

        LogicState stateHUp = wireGrid.getState(new Vector2D(5,4), Orientation.HORIZONTALLY);
        LogicState stateVUp = wireGrid.getState(new Vector2D(5,4), Orientation.VERTICALLY);

        LogicState stateHDown = wireGrid.getState(new Vector2D(5,5), Orientation.HORIZONTALLY);
        LogicState stateVDown = wireGrid.getState(new Vector2D(5,5), Orientation.VERTICALLY);

        assertEquals(LogicState.HIGH, stateVUp);
        assertEquals(LogicState.LOW, stateHUp);

        assertEquals(LogicState.HIGH, stateVDown);
        assertEquals(LogicState.LOW, stateHDown);
    }

    @Test
    void getStateWithinDimensionsHorizontallyTouchingTest(){
        WireGrid wireGrid = new ArrayWireGrid();

        wireGrid.setNode(new Vector2D(5,5), new Node(Node.State.HIGH, Node.State.LOW, Node.WireCrossing.TOUCHING));

        LogicState stateHLeft = wireGrid.getState(new Vector2D(5,5), Orientation.HORIZONTALLY);
        LogicState stateVLeft = wireGrid.getState(new Vector2D(5,5), Orientation.VERTICALLY);

        LogicState stateHRight = wireGrid.getState(new Vector2D(6,5), Orientation.HORIZONTALLY);
        LogicState stateVRight = wireGrid.getState(new Vector2D(6,5), Orientation.VERTICALLY);

        assertEquals(LogicState.HIGH, stateHLeft);
        assertEquals(LogicState.HIGH, stateVLeft);

        assertEquals(LogicState.HIGH, stateHRight);
        assertEquals(LogicState.LOW, stateVRight);

    }

    @Test
    void getStateWithinDimensionsVerticallyTouchingTest(){
        WireGrid wireGrid = new ArrayWireGrid();

        wireGrid.setNode(new Vector2D(5,4), new Node(Node.State.LOW, Node.State.HIGH, Node.WireCrossing.TOUCHING));

        LogicState stateHUp = wireGrid.getState(new Vector2D(5,4), Orientation.HORIZONTALLY);
        LogicState stateVUp = wireGrid.getState(new Vector2D(5,4), Orientation.VERTICALLY);

        LogicState stateHDown = wireGrid.getState(new Vector2D(5,5), Orientation.HORIZONTALLY);
        LogicState stateVDown = wireGrid.getState(new Vector2D(5,5), Orientation.VERTICALLY);

        assertEquals(LogicState.HIGH, stateVUp);
        assertEquals(LogicState.HIGH, stateHUp);

        assertEquals(LogicState.HIGH, stateVDown);
        assertEquals(LogicState.LOW, stateHDown);

    }

    @Test
    void getStateAtEdgesNotTouchingTest(){
        WireGrid wireGrid = new ArrayWireGrid();

        //Vertically
        wireGrid.setNode(new Vector2D(0,0), new Node(Node.State.LOW, Node.State.HIGH, Node.WireCrossing.NOT_TOUCHING));

        LogicState stateH = wireGrid.getState(new Vector2D(0,0), Orientation.HORIZONTALLY);
        LogicState stateV = wireGrid.getState(new Vector2D(0,0), Orientation.VERTICALLY);

        assertEquals(LogicState.HIGH, stateV);
        assertEquals(LogicState.LOW, stateH);

        //Horizontally
        wireGrid.setNode(new Vector2D(0,0), new Node(Node.State.HIGH, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));

        stateH = wireGrid.getState(new Vector2D(0,0), Orientation.HORIZONTALLY);
        stateV = wireGrid.getState(new Vector2D(0,0), Orientation.VERTICALLY);

        assertEquals(LogicState.LOW, stateV);
        assertEquals(LogicState.HIGH, stateH);
    }

    @Test
    void getStateAtEdgesTouchingTest(){
        WireGrid wireGrid = new ArrayWireGrid();

        //Vertically
        wireGrid.setNode(new Vector2D(0,0), new Node(Node.State.LOW, Node.State.HIGH, Node.WireCrossing.TOUCHING));

        LogicState stateH = wireGrid.getState(new Vector2D(0,0), Orientation.HORIZONTALLY);
        LogicState stateV = wireGrid.getState(new Vector2D(0,0), Orientation.VERTICALLY);

        assertEquals(LogicState.HIGH, stateV);
        assertEquals(LogicState.HIGH, stateH);

        //Horizontally
        wireGrid.setNode(new Vector2D(0,0), new Node(Node.State.HIGH, Node.State.LOW, Node.WireCrossing.TOUCHING));

        stateH = wireGrid.getState(new Vector2D(0,0), Orientation.HORIZONTALLY);
        stateV = wireGrid.getState(new Vector2D(0,0), Orientation.VERTICALLY);

        assertEquals(LogicState.HIGH, stateV);
        assertEquals(LogicState.HIGH, stateH);
    }

    @Test
    void getStateEdgeCasesTouchingTest(){
        WireGrid wireGrid = new ArrayWireGrid();

        //Horizontally
        wireGrid.setNode(new Vector2D(5,5), new Node(Node.State.HIGH, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(6,5), new Node(Node.State.NONE, Node.State.NONE, Node.WireCrossing.TOUCHING));

        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(6,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(6,5), Orientation.VERTICALLY));

        //Vertically
        wireGrid = new ArrayWireGrid();

        //Horizontally
        wireGrid.setNode(new Vector2D(5,4), new Node(Node.State.NONE, Node.State.HIGH, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(5,5), new Node(Node.State.NONE, Node.State.NONE, Node.WireCrossing.TOUCHING));

        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(5,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(5,5), Orientation.VERTICALLY));
    }

    @Test
    void resetWiresToLowTest(){
        WireGrid wireGrid = new ArrayWireGrid();

        wireGrid.setNode(new Vector2D(5,5), new Node(Node.State.HIGH, Node.State.HIGH, Node.WireCrossing.TOUCHING));
        wireGrid.setNode(new Vector2D(1,1), new Node(Node.State.HIGH, Node.State.HIGH, Node.WireCrossing.NOT_TOUCHING));

        Iterator2D<Node> it = wireGrid.getIterator();

        boolean containsHigh = false;
        while(it.hasNext()) {
            Node node = it.next();
            if (node.getRightWire() == Node.State.HIGH || node.getDownWire() == Node.State.HIGH)
                containsHigh = true;
            double x = it.currentPosition().getX();
            double y = it.currentPosition().getY();
            if(wireGrid.getState(new Vector2D(x, y), Orientation.VERTICALLY) == LogicState.HIGH)
                containsHigh = true;
            if(wireGrid.getState(new Vector2D(x, y), Orientation.HORIZONTALLY) == LogicState.HIGH)
                containsHigh = true;
        }

        assertTrue(containsHigh);

        wireGrid.resetWiresToLow();

        containsHigh = false;
        while(it.hasNext()) {
            Node node = it.next();
            if (node.getRightWire() == Node.State.HIGH || node.getDownWire() == Node.State.HIGH)
                containsHigh = true;
            double x = it.currentPosition().getX();
            double y = it.currentPosition().getY();
            if(wireGrid.getState(new Vector2D(x, y), Orientation.VERTICALLY) == LogicState.HIGH)
                containsHigh = true;
            if(wireGrid.getState(new Vector2D(x, y), Orientation.HORIZONTALLY) == LogicState.HIGH)
                containsHigh = true;
        }

        assertFalse(containsHigh);

    }

    @Test
    void propagationNotTouchingTest(){
        WireGrid wireGrid = new ArrayWireGrid();

        wireGrid.setNode(new Vector2D(5,5), new Node(Node.State.LOW, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));

        wireGrid.setNode(new Vector2D(6,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(7,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(7,4), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(8,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(9,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));

        wireGrid.setNode(new Vector2D(5,6), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(5,7), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(4,7), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(5,8), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(5,9), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));

        wireGrid.setNode(new Vector2D(5,4), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(5,3), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));

        wireGrid.setNode(new Vector2D(4,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(3,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));

        //HORIZONTALLY
        List<Generator> generators = new ArrayList<>();
        generators.add(new Generator(new Vector2D(5,5), Orientation.HORIZONTALLY));
        wireGrid.resetWiresToLow();
        //System.out.println(wireGrid);
        wireGrid.propagateGenerators(generators);
        //System.out.println(wireGrid);
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(9,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(9,5), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(5,9), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(5,9), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(7,4), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(7,4), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(4,7), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(4,7), Orientation.VERTICALLY));

        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(5,3), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(5,3), Orientation.VERTICALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(3,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(3,5), Orientation.VERTICALLY));

        //VERTICALLY
        generators = new ArrayList<>();
        generators.add(new Generator(new Vector2D(5,5), Orientation.VERTICALLY));
        wireGrid.resetWiresToLow();
        //System.out.println(wireGrid);
        wireGrid.propagateGenerators(generators);
        //System.out.println(wireGrid);
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(9,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(9,5), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(5,9), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(5,9), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(7,4), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(7,4), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(4,7), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(4,7), Orientation.VERTICALLY));

        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(5,3), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(5,3), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(3,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(3,5), Orientation.VERTICALLY));
    }

    @Test
    void propagationTouchingTest(){
        WireGrid wireGrid = new ArrayWireGrid();

        wireGrid.setNode(new Vector2D(5,5), new Node(Node.State.LOW, Node.State.LOW, Node.WireCrossing.TOUCHING));

        wireGrid.setNode(new Vector2D(6,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(7,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        wireGrid.setNode(new Vector2D(7,4), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(8,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(9,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));

        wireGrid.setNode(new Vector2D(5,6), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(5,7), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.TOUCHING));
        wireGrid.setNode(new Vector2D(4,7), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        wireGrid.setNode(new Vector2D(5,8), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(5,9), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));

        wireGrid.setNode(new Vector2D(5,4), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(5,3), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));

        wireGrid.setNode(new Vector2D(4,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(3,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));

        //HORIZONTALLY
        List<Generator> generators = new ArrayList<>();
        generators.add(new Generator(new Vector2D(5,5), Orientation.HORIZONTALLY));
        wireGrid.resetWiresToLow();
        //System.out.println(wireGrid);
        wireGrid.propagateGenerators(generators);
        //System.out.println(wireGrid);
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(9,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(9,5), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(5,9), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(5,9), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(7,4), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(7,4), Orientation.VERTICALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(4,7), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(4,7), Orientation.VERTICALLY));

        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(5,3), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(5,3), Orientation.VERTICALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(3,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(3,5), Orientation.VERTICALLY));
    }

    @Test
    void propagateExceptions(){
        WireGrid wireGrid = new ArrayWireGrid();
        List<Generator> generators = new ArrayList<>();
        generators.add(new Generator(new Vector2D(-1,0), Orientation.HORIZONTALLY));
        assertThrows(IllegalArgumentException.class, () -> {
            wireGrid.propagateGenerators(generators);
        });

        generators.clear();
        generators.add(new Generator(new Vector2D(0,-1), Orientation.HORIZONTALLY));
        assertThrows(IllegalArgumentException.class, () -> {
            wireGrid.propagateGenerators(generators);
        });

        generators.clear();
        generators.add(new Generator(new Vector2D(0,0), Orientation.HORIZONTALLY));
        assertDoesNotThrow( () -> {
            wireGrid.propagateGenerators(generators);
        });

        generators.clear();
        generators.add(new Generator(new Vector2D(11,0), Orientation.HORIZONTALLY));
        assertDoesNotThrow( () -> {
            wireGrid.propagateGenerators(generators);
        });

        generators.clear();
        generators.add(new Generator(new Vector2D(0,11), Orientation.HORIZONTALLY));
        assertDoesNotThrow( () -> {
            wireGrid.propagateGenerators(generators);
        });

        generators.clear();
        generators.add(new Generator(new Vector2D(10,5), Orientation.HORIZONTALLY));
        assertDoesNotThrow( () -> {
            wireGrid.propagateGenerators(generators);
        });

        generators.clear();
        generators.add(new Generator(new Vector2D(5,10), Orientation.HORIZONTALLY));
        assertDoesNotThrow( () -> {
            wireGrid.propagateGenerators(generators);
        });
    }

    @Test
    void generatorOutsideOfDimensionsVertically(){
        WireGrid wireGrid = new ArrayWireGrid();

        wireGrid.setNode(new Vector2D(5,5), new Node(Node.State.LOW, Node.State.LOW, Node.WireCrossing.TOUCHING));

        wireGrid.setNode(new Vector2D(6,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(7,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        wireGrid.setNode(new Vector2D(7,4), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(8,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(9,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));

        wireGrid.setNode(new Vector2D(5,6), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(5,7), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.TOUCHING));
        wireGrid.setNode(new Vector2D(4,7), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        wireGrid.setNode(new Vector2D(5,8), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(5,9), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));

        wireGrid.setNode(new Vector2D(5,4), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(5,3), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));

        wireGrid.setNode(new Vector2D(4,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(3,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));

        List<Generator> generators = new ArrayList<>();
        generators.add(new Generator(new Vector2D(5,10), Orientation.VERTICALLY));
        wireGrid.resetWiresToLow();
        //System.out.println(wireGrid);
        wireGrid.propagateGenerators(generators);
        //System.out.println(wireGrid);
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(9,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(9,5), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(5,9), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(5,9), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(7,4), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(7,4), Orientation.VERTICALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(4,7), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(4,7), Orientation.VERTICALLY));

        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(5,3), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(5,3), Orientation.VERTICALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(3,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(3,5), Orientation.VERTICALLY));
    }

    @Test
    void generatorOutsideOfDimensionsHorizontally(){
        WireGrid wireGrid = new ArrayWireGrid();

        wireGrid.setNode(new Vector2D(5,5), new Node(Node.State.LOW, Node.State.LOW, Node.WireCrossing.TOUCHING));

        wireGrid.setNode(new Vector2D(6,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(7,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        wireGrid.setNode(new Vector2D(7,4), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(8,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(9,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));

        wireGrid.setNode(new Vector2D(5,6), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(5,7), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.TOUCHING));
        wireGrid.setNode(new Vector2D(4,7), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        wireGrid.setNode(new Vector2D(5,8), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(5,9), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));

        wireGrid.setNode(new Vector2D(5,4), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(5,3), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));

        wireGrid.setNode(new Vector2D(4,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(3,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));

        List<Generator> generators = new ArrayList<>();
        generators.add(new Generator(new Vector2D(10,5), Orientation.HORIZONTALLY));
        wireGrid.resetWiresToLow();
        //System.out.println(wireGrid);
        wireGrid.propagateGenerators(generators);
        //System.out.println(wireGrid);
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(9,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(9,5), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(5,9), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(5,9), Orientation.VERTICALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(7,4), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(7,4), Orientation.VERTICALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(4,7), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(4,7), Orientation.VERTICALLY));

        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(5,3), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(5,3), Orientation.VERTICALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(3,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.LOW, wireGrid.getState(new Vector2D(3,5), Orientation.VERTICALLY));
    }

    @Test
    void toStringTest(){
        WireGrid wireGrid = new ArrayWireGrid();

        wireGrid.setNode(new Vector2D(5,5), new Node(Node.State.LOW, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));

        wireGrid.setNode(new Vector2D(6,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(7,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        wireGrid.setNode(new Vector2D(7,4), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(8,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(9,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));

        wireGrid.setNode(new Vector2D(5,6), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(5,7), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.TOUCHING));
        wireGrid.setNode(new Vector2D(4,7), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        wireGrid.setNode(new Vector2D(5,8), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(5,9), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));

        wireGrid.setNode(new Vector2D(5,4), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(5,3), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));

        wireGrid.setNode(new Vector2D(4,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        wireGrid.setNode(new Vector2D(3,5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));

        List<Generator> generators = new ArrayList<>();
        generators.add(new Generator(new Vector2D(5,5), Orientation.VERTICALLY));
        wireGrid.resetWiresToLow();
        //System.out.println(wireGrid);
        wireGrid.propagateGenerators(generators);
        //System.out.println(wireGrid);

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

        assertEquals(expected, wireGrid.toString());
    }

    @Test
    void iteratorTest(){
        //Arrange
        WireGrid wireGrid = new ArrayWireGrid();
        wireGrid.setNode(new Vector2D(0,0), new Node(Node.State.HIGH, Node.State.LOW, Node.WireCrossing.TOUCHING));
        wireGrid.setNode(new Vector2D(1,1), new Node(Node.State.LOW, Node.State.HIGH, Node.WireCrossing.NOT_TOUCHING));
        Iterator2D<Node> iterator = wireGrid.getIterator();

        //Act & Assert
        assertTrue(iterator.hasNext());
        Node w = iterator.next();
        Vector2D pos = iterator.currentPosition();
        assertEquals(Node.State.HIGH, w.getRightWire());
        assertEquals(Node.State.LOW, w.getDownWire());
        assertEquals(Node.WireCrossing.TOUCHING, w.isTouching());
        assertEquals(0, pos.getX());
        assertEquals(0, pos.getY());

        assertTrue(iterator.hasNext());
        w = iterator.next();
        pos = iterator.currentPosition();
        assertEquals(Node.State.LOW, w.getRightWire());
        assertEquals(Node.State.HIGH, w.getDownWire());
        assertEquals(Node.WireCrossing.NOT_TOUCHING, w.isTouching());
        assertEquals(1, pos.getX());
        assertEquals(1, pos.getY());

        assertFalse(iterator.hasNext());

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void updateTest(){
        WireGrid wireGrid = new ArrayWireGrid();

        wireGrid.updateWire(new Vector2D(0,0), Orientation.HORIZONTALLY, Node.State.LOW);
        wireGrid.updateWire(new Vector2D(1,1), Orientation.VERTICALLY, Node.State.HIGH);
        wireGrid.updateWire(new Vector2D(100,100), Orientation.VERTICALLY, Node.State.HIGH);
        wireGrid.updateCrossing(new Vector2D(0,1), Node.WireCrossing.TOUCHING);
        wireGrid.updateCrossing(new Vector2D(100,100), Node.WireCrossing.TOUCHING);

        assertEquals(Node.State.LOW, wireGrid.getNode(new Vector2D(0,0)).getRightWire());
        assertEquals(Node.State.HIGH, wireGrid.getNode(new Vector2D(1,1)).getDownWire());
        assertEquals(Node.State.HIGH, wireGrid.getNode(new Vector2D(100,100)).getDownWire());
        assertEquals(Node.WireCrossing.TOUCHING, wireGrid.getNode(new Vector2D(0,1)).isTouching());
        assertEquals(Node.WireCrossing.TOUCHING, wireGrid.getNode(new Vector2D(100,100)).isTouching());

    }
}