package InternalModel.LogicElements;

import InternalModel.LogicState;
import InternalModel.Vector2D;

import java.util.ArrayList;
import java.util.List;

public abstract class LogicElement {
    Vector2D position;

    List<LogicState> inputStates = new ArrayList<>(); // states at the input

    public void receiveNotification(int inputNumber, LogicState state){
        inputStates.set(inputNumber, state);
    }

    public abstract List<Vector2D> getInputPositions();

    public abstract LogicState computeValue(List<LogicState> states);

    public abstract Vector2D getOutput(); // returns (x,y)

    public LogicElement(int x, int y){
        position = new Vector2D(x,y);
    }
}
