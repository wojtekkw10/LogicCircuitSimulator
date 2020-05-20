package LogicCircuitSimulator.NodeHandler;

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
    void setAndGetWireTest() {
        //Arrange
        NodeHandler nodeHandler = new ArrayNodeHandler();

        //Act
        nodeHandler.setRightWire(new Vector2D(2,5), WireState.HIGH);
        nodeHandler.setDownWire(new Vector2D(2,5), WireState.LOW);

        //Assert
        assertEquals(WireState.HIGH, nodeHandler.getRightWire(new Vector2D(2,5)));
        assertEquals(WireState.LOW, nodeHandler.getDownWire(new Vector2D(2,5)));
        assertEquals(Crossing.NOT_TOUCHING, nodeHandler.getCrossing(new Vector2D(2,5)));

        assertEquals(WireState.NONE, nodeHandler.getRightWire(new Vector2D(1,5)));
        assertEquals(WireState.NONE, nodeHandler.getDownWire(new Vector2D(1,5)));
        assertEquals(Crossing.NOT_TOUCHING, nodeHandler.getCrossing(new Vector2D(1,5)));

        assertEquals(WireState.NONE, nodeHandler.getRightWire(new Vector2D(4,9)));
        assertEquals(WireState.NONE, nodeHandler.getDownWire(new Vector2D(4,9)));
        assertEquals(Crossing.NOT_TOUCHING, nodeHandler.getCrossing(new Vector2D(4,9)));
    }

    @Test
    void propagationNotTouchingTest(){
        NodeHandler nodeHandler = new ArrayNodeHandler();

        addInitialWires(nodeHandler);

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

        nodeHandler.setCrossing(new Vector2D(5,5), Crossing.TOUCHING);
        nodeHandler.setCrossing(new Vector2D(7,5), Crossing.TOUCHING);
        nodeHandler.setCrossing(new Vector2D(5,7), Crossing.TOUCHING);
        nodeHandler.setCrossing(new Vector2D(4,7), Crossing.TOUCHING);

        addInitialWires(nodeHandler);

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

        nodeHandler.setCrossing(new Vector2D(5,5), Crossing.TOUCHING);
        nodeHandler.setCrossing(new Vector2D(7,5), Crossing.TOUCHING);
        nodeHandler.setCrossing(new Vector2D(5,7), Crossing.TOUCHING);
        nodeHandler.setCrossing(new Vector2D(4,7), Crossing.TOUCHING);

        addInitialWires(nodeHandler);

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

        nodeHandler.setCrossing(new Vector2D(5,5), Crossing.TOUCHING);
        nodeHandler.setCrossing(new Vector2D(7,5), Crossing.TOUCHING);
        nodeHandler.setCrossing(new Vector2D(5,7), Crossing.TOUCHING);
        nodeHandler.setCrossing(new Vector2D(4,7), Crossing.TOUCHING);

        addInitialWires(nodeHandler);

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

        nodeHandler.setCrossing(new Vector2D(7,5), Crossing.TOUCHING);
        nodeHandler.setCrossing(new Vector2D(5,7), Crossing.TOUCHING);
        nodeHandler.setCrossing(new Vector2D(4,7), Crossing.TOUCHING);

        addInitialWires(nodeHandler);

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
        nodeHandler.setRightWire(new Vector2D(0,0), WireState.HIGH);
        nodeHandler.setDownWire(new Vector2D(0,0), WireState.LOW);
        nodeHandler.setCrossing(new Vector2D(0,0), Crossing.TOUCHING);

        nodeHandler.setRightWire(new Vector2D(1,1), WireState.LOW);
        nodeHandler.setDownWire(new Vector2D(1, 1), WireState.HIGH);
        nodeHandler.setCrossing(new Vector2D(1,1), Crossing.NOT_TOUCHING);

        Iterator<Node> iterator = nodeHandler.iterator();

        //Act & Assert
        assertTrue(iterator.hasNext());
        Node w = iterator.next();
        Vector2D pos = w.getPosition();
        assertEquals(WireState.HIGH, w.getRightWire());
        assertEquals(WireState.LOW, w.getDownWire());
        assertEquals(Crossing.TOUCHING, w.isTouching());
        assertEquals(0, pos.getX());
        assertEquals(0, pos.getY());

        assertTrue(iterator.hasNext());
        w = iterator.next();
        pos = w.getPosition();
        assertEquals(WireState.LOW, w.getRightWire());
        assertEquals(WireState.HIGH, w.getDownWire());
        assertEquals(Crossing.NOT_TOUCHING, w.isTouching());
        assertEquals(1, pos.getX());
        assertEquals(1, pos.getY());

        assertFalse(iterator.hasNext());

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    private void addInitialWires(NodeHandler nodeHandler){
        nodeHandler.setRightWire(new Vector2D(5,5), WireState.LOW);
        nodeHandler.setDownWire(new Vector2D(5,5), WireState.LOW);
        nodeHandler.setRightWire(new Vector2D(6,5), WireState.LOW);
        nodeHandler.setRightWire(new Vector2D(7,5), WireState.LOW);
        nodeHandler.setDownWire(new Vector2D(7,4), WireState.LOW);
        nodeHandler.setRightWire(new Vector2D(8,5), WireState.LOW);
        nodeHandler.setRightWire(new Vector2D(9,5), WireState.LOW);
        nodeHandler.setDownWire(new Vector2D(5,6), WireState.LOW);
        nodeHandler.setDownWire(new Vector2D(5,7), WireState.LOW);
        nodeHandler.setRightWire(new Vector2D(4,7), WireState.LOW);
        nodeHandler.setDownWire(new Vector2D(5,8), WireState.LOW);
        nodeHandler.setDownWire(new Vector2D(5,9), WireState.LOW);
        nodeHandler.setDownWire(new Vector2D(5,4), WireState.LOW);
        nodeHandler.setDownWire(new Vector2D(5,3), WireState.LOW);
        nodeHandler.setRightWire(new Vector2D(4,5), WireState.LOW);
        nodeHandler.setRightWire(new Vector2D(3,5), WireState.LOW);
    }
}