package InternalModel.WireGrid;

import InternalModel.LogicState;
import InternalModel.Orientation;
import InternalModel.Vector2D;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        Wire wire = new Wire(Wire.State.HIGH, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING);
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
        wireGrid.setElement(new Vector2D(6,5), new Wire(Wire.State.HIGH, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));

        LogicState stateH = wireGrid.getState(new Vector2D(6,5), Orientation.HORIZONTALLY);
        LogicState stateV = wireGrid.getState(new Vector2D(6,5), Orientation.VERTICALLY);

        assertEquals(LogicState.HIGH, stateH);
        assertEquals(LogicState.LOW, stateV);

    }

    @Test
    void getStateWithinDimensionsVerticallyNotTouchingTest(){
        WireGrid wireGrid = new ArrayWireGrid(10, 10);

        wireGrid.setElement(new Vector2D(5,4), new Wire(Wire.State.LOW, Wire.State.HIGH, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(5,5), new Wire(Wire.State.LOW, Wire.State.HIGH, Wire.WireCrossing.NOT_TOUCHING));

        LogicState stateH = wireGrid.getState(new Vector2D(5,5), Orientation.HORIZONTALLY);
        LogicState stateV = wireGrid.getState(new Vector2D(5,5), Orientation.VERTICALLY);

        assertEquals(LogicState.HIGH, stateV);
        assertEquals(LogicState.LOW, stateH);

    }

    @Test
    void getStateWithinDimensionsHorizontallyTouchingTest(){
        WireGrid wireGrid = new ArrayWireGrid(10, 10);

        wireGrid.setElement(new Vector2D(5,5), new Wire(Wire.State.HIGH, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(6,5), new Wire(Wire.State.HIGH, Wire.State.LOW, Wire.WireCrossing.TOUCHING));

        LogicState stateH = wireGrid.getState(new Vector2D(6,5), Orientation.HORIZONTALLY);
        LogicState stateV = wireGrid.getState(new Vector2D(6,5), Orientation.VERTICALLY);

        assertEquals(LogicState.HIGH, stateH);
        assertEquals(LogicState.HIGH, stateV);

    }

    @Test
    void getStateWithinDimensionsVerticallyTouchingTest(){
        WireGrid wireGrid = new ArrayWireGrid(10, 10);

        wireGrid.setElement(new Vector2D(5,4), new Wire(Wire.State.LOW, Wire.State.HIGH, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(new Vector2D(5,5), new Wire(Wire.State.LOW, Wire.State.HIGH, Wire.WireCrossing.TOUCHING));

        LogicState stateH = wireGrid.getState(new Vector2D(5,5), Orientation.HORIZONTALLY);
        LogicState stateV = wireGrid.getState(new Vector2D(5,5), Orientation.VERTICALLY);

        assertEquals(LogicState.HIGH, stateV);
        assertEquals(LogicState.HIGH, stateH);

    }


}