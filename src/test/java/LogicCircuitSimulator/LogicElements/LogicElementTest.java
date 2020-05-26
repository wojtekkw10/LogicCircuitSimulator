package LogicCircuitSimulator.LogicElements;

import LogicCircuitSimulator.Simulation.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.Geometry.LogicElementGeometry;
import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.ComputedValue;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Vector2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class LogicElementTest {

    static class AsymmetricGate extends LogicElement {

        public AsymmetricGate(int x, int y, Rotation rot) {
            super(x, y, rot);
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public LogicElementGeometry getNewGeometry() {
            return new LogicElementGeometry() {
                @Override
                public List<Vector2D> getLocalInputPositions() {
                    //*----
                    //-*---
                    //--*--
                    ArrayList<Vector2D> localInputPositions = new ArrayList<>();
                    localInputPositions.add(new Vector2D(0,0));
                    localInputPositions.add(new Vector2D(1,1));
                    localInputPositions.add(new Vector2D(2,2));
                    return localInputPositions;
                }

                @Override
                public List<Vector2D> getLocalOutputPositions() {
                    //-----*
                    //----*-
                    //----*-
                    ArrayList<Vector2D> localOutputPositions = new ArrayList<>();
                    localOutputPositions.add(new Vector2D(5, 0));
                    localOutputPositions.add(new Vector2D(4, 1));
                    localOutputPositions.add(new Vector2D(4, 2));
                    return localOutputPositions;
                }
            };
        }

        @Override
        public List<LogicState> computeLocalValues(List<LogicState> states) {
            return states;
        }

        @Override
        public void accept(LogicElementVisitor visitor) {
        }
    }

    @Test
    void getPositionsRightTest(){
        LogicElement asymmetric = new AsymmetricGate(5, 10, Rotation.RIGHT);
        List<Vector2D> inputPositions = asymmetric.getGeometry().getInputPositions();
        List<Vector2D> outputPositions = asymmetric.getGeometry().getOutputPositions();

        assertTrue(inputPositions.contains(new Vector2D(5,10)));
        assertTrue(inputPositions.contains(new Vector2D(6,11)));
        assertTrue(inputPositions.contains(new Vector2D(7,12)));

        assertTrue(outputPositions.contains(new Vector2D(10,10)));
        assertTrue(outputPositions.contains(new Vector2D(9,11)));
        assertTrue(outputPositions.contains(new Vector2D(9,12)));
    }

    @Test
    void getPositionsLeftTest(){
        LogicElement asymmetric = new AsymmetricGate(5, 10, Rotation.LEFT);
        List<Vector2D> inputPositions = asymmetric.getGeometry().getInputPositions();
        List<Vector2D> outputPositions = asymmetric.getGeometry().getOutputPositions();

        assertTrue(inputPositions.contains(new Vector2D(8,10)));
        assertTrue(inputPositions.contains(new Vector2D(9,11)));
        assertTrue(inputPositions.contains(new Vector2D(10,12)));

        assertTrue(outputPositions.contains(new Vector2D(6,10)));
        assertTrue(outputPositions.contains(new Vector2D(6,11)));
        assertTrue(outputPositions.contains(new Vector2D(5,12)));
    }

    @Test
    void getPositionsDownTest(){
        LogicElement asymmetric = new AsymmetricGate(5, 10, Rotation.DOWN);
        List<Vector2D> inputPositions = asymmetric.getGeometry().getInputPositions();
        List<Vector2D> outputPositions = asymmetric.getGeometry().getOutputPositions();

        assertTrue(inputPositions.contains(new Vector2D(5,12)));
        assertTrue(inputPositions.contains(new Vector2D(6,11)));
        assertTrue(inputPositions.contains(new Vector2D(7,10)));

        assertTrue(outputPositions.contains(new Vector2D(5,14)));
        assertTrue(outputPositions.contains(new Vector2D(6,14)));
        assertTrue(outputPositions.contains(new Vector2D(7,15)));
    }

    @Test
    void getPositionsUpTest(){
        LogicElement asymmetric = new AsymmetricGate(5, 10, Rotation.UP);
        List<Vector2D> inputPositions = asymmetric.getGeometry().getInputPositions();
        List<Vector2D> outputPositions = asymmetric.getGeometry().getOutputPositions();

        assertTrue(inputPositions.contains(new Vector2D(5,15)));
        assertTrue(inputPositions.contains(new Vector2D(6,14)));
        assertTrue(inputPositions.contains(new Vector2D(7,13)));

        assertTrue(outputPositions.contains(new Vector2D(5,10)));
        assertTrue(outputPositions.contains(new Vector2D(6,11)));
        assertTrue(outputPositions.contains(new Vector2D(7,11)));
    }

    @Test
    void computeValuesTest(){
        LogicElement asymmetric = new AsymmetricGate(5, 10, Rotation.UP);
        List<LogicState> inputValues = new ArrayList<>();
        inputValues.add(LogicState.HIGH);
        inputValues.add(LogicState.HIGH);
        inputValues.add(LogicState.LOW);

        List<ComputedValue> values = asymmetric.computeValues(inputValues);

        assertEquals(LogicState.HIGH, values.get(0).getState());
        assertEquals(LogicState.HIGH, values.get(1).getState());
        assertEquals(LogicState.LOW, values.get(2).getState());

        assertEquals(new Vector2D(5,10), values.get(0).getPos());
        assertEquals(new Vector2D(6,11), values.get(1).getPos());
        assertEquals(new Vector2D(7,11), values.get(2).getPos());

        inputValues.add(LogicState.LOW);
        assertThrows(IllegalArgumentException.class, () -> {
            asymmetric.computeValues(inputValues);
        });

        assertEquals("ComputedValue{Vector2D{5.0, 10.0}, HIGH}", values.get(0).toString());
    }

    @Test
    void moveAndRotateTest(){
        LogicElement asymmetric = new AsymmetricGate(100, 100, Rotation.RIGHT);
        asymmetric.setPosition(new Vector2D(5, 10));
        asymmetric.setRotation(Rotation.DOWN);

        assertEquals(new Vector2D(5, 10), asymmetric.getPosition());

        List<Vector2D> inputPositions = asymmetric.getGeometry().getInputPositions();
        List<Vector2D> outputPositions = asymmetric.getGeometry().getOutputPositions();

        assertTrue(inputPositions.contains(new Vector2D(5,12)));
        assertTrue(inputPositions.contains(new Vector2D(6,11)));
        assertTrue(inputPositions.contains(new Vector2D(7,10)));

        assertTrue(outputPositions.contains(new Vector2D(5,14)));
        assertTrue(outputPositions.contains(new Vector2D(6,14)));
        assertTrue(outputPositions.contains(new Vector2D(7,15)));
    }
}