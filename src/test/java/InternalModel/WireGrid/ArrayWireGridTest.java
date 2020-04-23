package InternalModel.WireGrid;

import InternalModel.LogicState;
import InternalModel.Orientation;
import InternalModel.Vector2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ArrayWireGridTest {

    @Test
    void constructorExceptionTest(){
        //Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () ->{
                    new ArrayWireGrid(0, 10);
        });

        //Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () ->{
            new ArrayWireGrid(10, -1);
        });
    }

    @Test
    void setAndGetElementWithinInitialDimensionsTest() {
        //Arrange
        WireGrid wireGrid = new ArrayWireGrid(5, 10);

        //Act
        Wire wire = new Wire(Wire.State.HIGH, Wire.State.LOW, Wire.WireCrossing.TOUCHING);
        wire.setIsTouching(Wire.WireCrossing.NOT_TOUCHING);
        wireGrid.setElement(new Vector2D(2,5), wire);

        //Assert
        assertEquals(Wire.State.HIGH, wireGrid.getElement(new Vector2D(2,5)).getRightWire());
        assertEquals(Wire.State.LOW, wireGrid.getElement(new Vector2D(2,5)).getDownWire());
        assertEquals(Wire.WireCrossing.NOT_TOUCHING, wireGrid.getElement(new Vector2D(2,5)).isTouching());

        assertEquals(Wire.State.NONE, wireGrid.getElement(new Vector2D(1,5)).getRightWire());
        assertEquals(Wire.State.NONE, wireGrid.getElement(new Vector2D(1,5)).getDownWire());
        assertEquals(Wire.WireCrossing.NOT_TOUCHING, wireGrid.getElement(new Vector2D(1,5)).isTouching());

        assertEquals(Wire.State.NONE, wireGrid.getElement(new Vector2D(4,9)).getRightWire());
        assertEquals(Wire.State.NONE, wireGrid.getElement(new Vector2D(4,9)).getDownWire());
        assertEquals(Wire.WireCrossing.NOT_TOUCHING, wireGrid.getElement(new Vector2D(4,9)).isTouching());
    }

    @Test
    void setAndGetElementOutsideOfInitialDimensionsTest() {
        //Arrange
        WireGrid wireGrid = new ArrayWireGrid(2, 2);

        //Act
        assertEquals(2, wireGrid.getWidth());
        assertEquals(2, wireGrid.getHeight());

        Wire wire = new Wire(Wire.State.HIGH, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING);
        wireGrid.setElement(new Vector2D(5,10), wire);

        //Assert
        assertEquals(Wire.State.HIGH, wireGrid.getElement(new Vector2D(5,10)).getRightWire());
        assertEquals(Wire.State.LOW, wireGrid.getElement(new Vector2D(5,10)).getDownWire());
        assertEquals(Wire.WireCrossing.NOT_TOUCHING, wireGrid.getElement(new Vector2D(5,10)).isTouching());
        assertEquals(6, wireGrid.getWidth());
        assertEquals(11, wireGrid.getHeight());
    }

    @Test
    void getElementOutsideOfInitialDimensionsTest(){
        WireGrid wireGrid = new ArrayWireGrid(2, 2);

        assertEquals(Wire.State.NONE, wireGrid.getElement(new Vector2D(1,10)).getRightWire());
        assertEquals(Wire.State.NONE, wireGrid.getElement(new Vector2D(1,10)).getDownWire());
        assertEquals(Wire.WireCrossing.NOT_TOUCHING, wireGrid.getElement(new Vector2D(1,10)).isTouching());

        assertEquals(Wire.State.NONE, wireGrid.getElement(new Vector2D(10,1)).getRightWire());
        assertEquals(Wire.State.NONE, wireGrid.getElement(new Vector2D(10,1)).getDownWire());
        assertEquals(Wire.WireCrossing.NOT_TOUCHING, wireGrid.getElement(new Vector2D(10,1)).isTouching());
    }

    @Test
    void setElementExceptionTest() {
        //Arrange
        WireGrid wireGrid = new ArrayWireGrid(5, 10);

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            wireGrid.setElement(new Vector2D(-1, 2), new Wire());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            wireGrid.setElement(new Vector2D(0, -1), new Wire());
        });
    }

    @Test
    void getElementExceptionTest() {
        //Arrange
        WireGrid wireGrid = new ArrayWireGrid(5, 10);

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            wireGrid.getElement(new Vector2D(-1, 2));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            wireGrid.getElement(new Vector2D(0, -1));
        });
    }

    @Test
    void getStateOutsideOfDimensionsTest(){
        WireGrid wireGrid = new ArrayWireGrid(5, 10);
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
        WireGrid wireGrid = new ArrayWireGrid(10, 10);

        assertThrows(IllegalArgumentException.class, () -> {
            wireGrid.getState(new Vector2D(-1, 0), Orientation.HORIZONTALLY);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            wireGrid.getState(new Vector2D(0, -1), Orientation.HORIZONTALLY);
        });
    }

    @Test
    void getStateWithinDimensionsHorizontallyNotTouchingTest(){
        WireGrid wireGrid = new ArrayWireGrid(10, 10);

        wireGrid.setElement(new Vector2D(5,5), new Wire(Wire.State.HIGH, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));

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
        WireGrid wireGrid = new ArrayWireGrid(10, 10);

        wireGrid.setElement(new Vector2D(5,4), new Wire(Wire.State.LOW, Wire.State.HIGH, Wire.WireCrossing.NOT_TOUCHING));

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
        WireGrid wireGrid = new ArrayWireGrid(10, 10);

        wireGrid.setElement(new Vector2D(5,5), new Wire(Wire.State.HIGH, Wire.State.LOW, Wire.WireCrossing.TOUCHING));

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
        WireGrid wireGrid = new ArrayWireGrid(10, 10);

        wireGrid.setElement(new Vector2D(5,4), new Wire(Wire.State.LOW, Wire.State.HIGH, Wire.WireCrossing.TOUCHING));

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
        WireGrid wireGrid = new ArrayWireGrid(10, 10);

        //Vertically
        wireGrid.setElement(new Vector2D(0,0), new Wire(Wire.State.LOW, Wire.State.HIGH, Wire.WireCrossing.NOT_TOUCHING));

        LogicState stateH = wireGrid.getState(new Vector2D(0,0), Orientation.HORIZONTALLY);
        LogicState stateV = wireGrid.getState(new Vector2D(0,0), Orientation.VERTICALLY);

        assertEquals(LogicState.HIGH, stateV);
        assertEquals(LogicState.LOW, stateH);

        //Horizontally
        wireGrid.setElement(new Vector2D(0,0), new Wire(Wire.State.HIGH, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));

        stateH = wireGrid.getState(new Vector2D(0,0), Orientation.HORIZONTALLY);
        stateV = wireGrid.getState(new Vector2D(0,0), Orientation.VERTICALLY);

        assertEquals(LogicState.LOW, stateV);
        assertEquals(LogicState.HIGH, stateH);
    }

    @Test
    void getStateAtEdgesTouchingTest(){
        WireGrid wireGrid = new ArrayWireGrid(10, 10);

        //Vertically
        wireGrid.setElement(new Vector2D(0,0), new Wire(Wire.State.LOW, Wire.State.HIGH, Wire.WireCrossing.TOUCHING));

        LogicState stateH = wireGrid.getState(new Vector2D(0,0), Orientation.HORIZONTALLY);
        LogicState stateV = wireGrid.getState(new Vector2D(0,0), Orientation.VERTICALLY);

        assertEquals(LogicState.HIGH, stateV);
        assertEquals(LogicState.HIGH, stateH);

        //Horizontally
        wireGrid.setElement(new Vector2D(0,0), new Wire(Wire.State.HIGH, Wire.State.LOW, Wire.WireCrossing.TOUCHING));

        stateH = wireGrid.getState(new Vector2D(0,0), Orientation.HORIZONTALLY);
        stateV = wireGrid.getState(new Vector2D(0,0), Orientation.VERTICALLY);

        assertEquals(LogicState.HIGH, stateV);
        assertEquals(LogicState.HIGH, stateH);
    }

    @Test
    void getStateEdgeCasesTouchingTest(){
        WireGrid wireGrid = new ArrayWireGrid(10, 10);

        //Horizontally
        wireGrid.setElement(new Vector2D(5,5), new Wire(Wire.State.HIGH, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(6,5), new Wire(Wire.State.NONE, Wire.State.NONE, Wire.WireCrossing.TOUCHING));

        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(6,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(6,5), Orientation.VERTICALLY));

        //Vertically
        wireGrid = new ArrayWireGrid(10, 10);

        //Horizontally
        wireGrid.setElement(new Vector2D(5,4), new Wire(Wire.State.NONE, Wire.State.HIGH, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(5,5), new Wire(Wire.State.NONE, Wire.State.NONE, Wire.WireCrossing.TOUCHING));

        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(5,5), Orientation.HORIZONTALLY));
        assertEquals(LogicState.HIGH, wireGrid.getState(new Vector2D(5,5), Orientation.VERTICALLY));
    }

    @Test
    void resetWiresToLowTest(){
        WireGrid wireGrid = new ArrayWireGrid(10, 10);

        wireGrid.setElement(new Vector2D(5,5), new Wire(Wire.State.HIGH, Wire.State.HIGH, Wire.WireCrossing.TOUCHING));
        wireGrid.setElement(new Vector2D(1,1), new Wire(Wire.State.HIGH, Wire.State.HIGH, Wire.WireCrossing.NOT_TOUCHING));

        boolean containsHigh = false;
        for (int i = 0; i < wireGrid.getWidth(); i++) {
            for (int j = 0; j < wireGrid.getHeight(); j++) {
                if(wireGrid.getState(new Vector2D(i, j), Orientation.VERTICALLY) == LogicState.HIGH)
                    containsHigh = true;
                if(wireGrid.getState(new Vector2D(i, j), Orientation.HORIZONTALLY) == LogicState.HIGH)
                    containsHigh = true;
            }
        }

        assertTrue(containsHigh);

        wireGrid.resetWiresToLow();

        containsHigh = false;
        for (int i = 0; i < wireGrid.getWidth(); i++) {
            for (int j = 0; j < wireGrid.getHeight(); j++) {
                if(wireGrid.getState(new Vector2D(i, j), Orientation.VERTICALLY) == LogicState.HIGH)
                    containsHigh = true;
                if(wireGrid.getState(new Vector2D(i, j), Orientation.HORIZONTALLY) == LogicState.HIGH)
                    containsHigh = true;
            }
        }

        assertFalse(containsHigh);

    }

    @Test
    void propagationNotTouchingTest(){
        WireGrid wireGrid = new ArrayWireGrid(10, 10);

        wireGrid.setElement(new Vector2D(5,5), new Wire(Wire.State.LOW, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));

        wireGrid.setElement(new Vector2D(6,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(7,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(7,4), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(8,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(9,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));

        wireGrid.setElement(new Vector2D(5,6), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(5,7), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(4,7), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(5,8), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(5,9), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));

        wireGrid.setElement(new Vector2D(5,4), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(5,3), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));

        wireGrid.setElement(new Vector2D(4,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(3,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));

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
        WireGrid wireGrid = new ArrayWireGrid(10, 10);

        wireGrid.setElement(new Vector2D(5,5), new Wire(Wire.State.LOW, Wire.State.LOW, Wire.WireCrossing.TOUCHING));

        wireGrid.setElement(new Vector2D(6,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(7,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.TOUCHING));
        wireGrid.setElement(new Vector2D(7,4), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(8,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(9,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));

        wireGrid.setElement(new Vector2D(5,6), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(5,7), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.TOUCHING));
        wireGrid.setElement(new Vector2D(4,7), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.TOUCHING));
        wireGrid.setElement(new Vector2D(5,8), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(5,9), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));

        wireGrid.setElement(new Vector2D(5,4), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(5,3), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));

        wireGrid.setElement(new Vector2D(4,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(3,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));

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
        WireGrid wireGrid = new ArrayWireGrid(10, 10);
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
        assertEquals(11, wireGrid.getWidth());

        generators.clear();
        generators.add(new Generator(new Vector2D(5,10), Orientation.HORIZONTALLY));
        assertDoesNotThrow( () -> {
            wireGrid.propagateGenerators(generators);
        });
        assertEquals(11, wireGrid.getHeight());
    }

    @Test
    void generatorOutsideOfDimensionsVertically(){
        WireGrid wireGrid = new ArrayWireGrid(10, 10);

        wireGrid.setElement(new Vector2D(5,5), new Wire(Wire.State.LOW, Wire.State.LOW, Wire.WireCrossing.TOUCHING));

        wireGrid.setElement(new Vector2D(6,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(7,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.TOUCHING));
        wireGrid.setElement(new Vector2D(7,4), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(8,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(9,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));

        wireGrid.setElement(new Vector2D(5,6), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(5,7), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.TOUCHING));
        wireGrid.setElement(new Vector2D(4,7), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.TOUCHING));
        wireGrid.setElement(new Vector2D(5,8), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(5,9), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));

        wireGrid.setElement(new Vector2D(5,4), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(5,3), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));

        wireGrid.setElement(new Vector2D(4,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(3,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));

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
        WireGrid wireGrid = new ArrayWireGrid(10, 10);

        wireGrid.setElement(new Vector2D(5,5), new Wire(Wire.State.LOW, Wire.State.LOW, Wire.WireCrossing.TOUCHING));

        wireGrid.setElement(new Vector2D(6,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(7,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.TOUCHING));
        wireGrid.setElement(new Vector2D(7,4), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(8,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(9,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));

        wireGrid.setElement(new Vector2D(5,6), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(5,7), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.TOUCHING));
        wireGrid.setElement(new Vector2D(4,7), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.TOUCHING));
        wireGrid.setElement(new Vector2D(5,8), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(5,9), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));

        wireGrid.setElement(new Vector2D(5,4), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(5,3), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));

        wireGrid.setElement(new Vector2D(4,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(3,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));

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
        WireGrid wireGrid = new ArrayWireGrid(10, 10);

        wireGrid.setElement(new Vector2D(5,5), new Wire(Wire.State.LOW, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));

        wireGrid.setElement(new Vector2D(6,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(7,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.TOUCHING));
        wireGrid.setElement(new Vector2D(7,4), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(8,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(9,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));

        wireGrid.setElement(new Vector2D(5,6), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(5,7), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.TOUCHING));
        wireGrid.setElement(new Vector2D(4,7), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.TOUCHING));
        wireGrid.setElement(new Vector2D(5,8), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(5,9), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));

        wireGrid.setElement(new Vector2D(5,4), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(5,3), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));

        wireGrid.setElement(new Vector2D(4,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(3,5), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));

        List<Generator> generators = new ArrayList<>();
        generators.add(new Generator(new Vector2D(5,5), Orientation.VERTICALLY));
        wireGrid.resetWiresToLow();
        //System.out.println(wireGrid);
        wireGrid.propagateGenerators(generators);
        //System.out.println(wireGrid);

        String expected = "+ + + + + + + + + + \n" +
                "                    \n" +
                "+ + + + + + + + + + \n" +
                "                    \n" +
                "+ + + + + + + + + + \n" +
                "                    \n" +
                "+ + + + + + + + + + \n" +
                "          \u001B[33;1m| \u001B[0m        \n" +
                "+ + + + + + + + + + \n" +
                "          \u001B[33;1m| \u001B[0m  \u001B[38;5;245m| \u001B[0m    \n" +
                "+ + + +\u001B[38;5;245m-\u001B[0m+\u001B[38;5;245m-\u001B[0m+\u001B[38;5;245m-\u001B[0m+\u001B[38;5;245m-\u001B[0m*\u001B[38;5;245m-\u001B[0m+\u001B[38;5;245m-\u001B[0m+\u001B[38;5;245m-\u001B[0m\n" +
                "          \u001B[33;1m| \u001B[0m        \n" +
                "+ + + + + + + + + + \n" +
                "          \u001B[33;1m| \u001B[0m        \n" +
                "+ + + + *\u001B[33;1m-\u001B[0m* + + + + \n" +
                "          \u001B[33;1m| \u001B[0m        \n" +
                "+ + + + + + + + + + \n" +
                "          \u001B[33;1m| \u001B[0m        \n" +
                "+ + + + + + + + + + \n" +
                "          \u001B[33;1m| \u001B[0m        \n";

        assertEquals(expected, wireGrid.toString());
    }

    @Test
    void iteratorTest(){
        //Arrange
        WireGrid wireGrid = new ArrayWireGrid(2, 2);
        wireGrid.setElement(new Vector2D(0,0), new Wire(Wire.State.HIGH, Wire.State.LOW, Wire.WireCrossing.TOUCHING));
        wireGrid.setElement(new Vector2D(1,1), new Wire(Wire.State.LOW, Wire.State.HIGH, Wire.WireCrossing.NOT_TOUCHING));
        Iterator2D<Wire> iterator = wireGrid.getIterator();

        //Act & Assert
        assertTrue(iterator.hasNext());
        Wire w = iterator.next();
        Vector2D pos = iterator.currentPosition();
        assertEquals(Wire.State.HIGH, w.getRightWire());
        assertEquals(Wire.State.LOW, w.getDownWire());
        assertEquals(Wire.WireCrossing.TOUCHING, w.isTouching());
        assertEquals(0, pos.getX());
        assertEquals(0, pos.getY());

        assertTrue(iterator.hasNext());
        w = iterator.next();
        pos = iterator.currentPosition();
        assertEquals(Wire.State.NONE, w.getRightWire());
        assertEquals(Wire.State.NONE, w.getDownWire());
        assertEquals(Wire.WireCrossing.NOT_TOUCHING, w.isTouching());
        assertEquals(1, pos.getX());
        assertEquals(0, pos.getY());

        assertTrue(iterator.hasNext());
        w = iterator.next();
        pos = iterator.currentPosition();
        assertEquals(Wire.State.NONE, w.getRightWire());
        assertEquals(Wire.State.NONE, w.getDownWire());
        assertEquals(Wire.WireCrossing.NOT_TOUCHING, w.isTouching());
        assertEquals(0, pos.getX());
        assertEquals(1, pos.getY());

        assertTrue(iterator.hasNext());
        w = iterator.next();
        pos = iterator.currentPosition();
        assertEquals(Wire.State.LOW, w.getRightWire());
        assertEquals(Wire.State.HIGH, w.getDownWire());
        assertEquals(Wire.WireCrossing.NOT_TOUCHING, w.isTouching());
        assertEquals(1, pos.getX());
        assertEquals(1, pos.getY());

        assertFalse(iterator.hasNext());

        assertThrows(NoSuchElementException.class, iterator::next);
    }


}