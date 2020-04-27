package InternalModel.LogicElements;

import InternalModel.LogicState;
import InternalModel.Vector2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogicElementPositionsTest {

    static class AsymmetricGate extends LogicElement{

        @Override
        public ArrayList<Vector2D> getLocalInputPositions() {
            //*----
            //-*---
            //*----
            ArrayList<Vector2D> localInputPositions = new ArrayList<>();
            localInputPositions.add(new Vector2D(0,0));
            localInputPositions.add(new Vector2D(1,1));
            localInputPositions.add(new Vector2D(0,2));
            return localInputPositions;
        }

        @Override
        public ArrayList<Vector2D> getLocalOutputPositions() {
            //-----*
            //----*-
            //----*-
            ArrayList<Vector2D> localOutputPositions = new ArrayList<>();
            localOutputPositions.add(new Vector2D(5, 0));
            localOutputPositions.add(new Vector2D(4, 1));
            localOutputPositions.add(new Vector2D(4, 2));
            return localOutputPositions;
        }

        @Override
        protected ArrayList<LogicState> computeLocalValues(List<LogicState> states) {
            return null;
        }
    }

    LogicElement asymmetricElement = new AsymmetricGate();

    @Test
    void getInputPositionsTest(){
        System.out.println(asymmetricElement.getInputPositions());
        assertTrue(true);

    }

/*    @Test
    void getInputPositionsDownTest(){
        LogicElement element = new NotGate(10, 10, Rotation.DOWN);
    }

    @Test
    void getInputPositionsLeftTest(){
        LogicElement element = new NotGate(10, 10, Rotation.LEFT);

    }

    @Test
    void getInputPositionsUpTest(){
        LogicElement element = new NotGate(10, 10, Rotation.UP);

    }

    @Test
    void getOutputPositionsRightTest(){
        LogicElement element = new NotGate(10, 10, Rotation.DOWN);

    }

    @Test
    void getOutputPositionsDownTest(){

    }

    @Test
    void getOutputPositionsLeftTest(){

    }

    @Test
    void getOutputPositionsUpTest(){

    }*/
}