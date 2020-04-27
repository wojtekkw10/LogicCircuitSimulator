package InternalModel.LogicElements;

import InternalModel.LogicState;
import InternalModel.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class NotGate extends LogicElement{
    public NotGate(int x, int y, Rotation rotation) {
        super(x, y, rotation);
    }

    @Override
    public ArrayList<Vector2D> getLocalInputPositions() {
        ArrayList<Vector2D> inputPositions = new ArrayList<>();
        inputPositions.add(new Vector2D(0,0));
        return inputPositions;
    }

    @Override
    public ArrayList<LogicState> computeLocalValues(List<LogicState> states) {
        ArrayList<LogicState> outputStates = new ArrayList<>();

        LogicState outputState;

        LogicState inputState = states.get(0);
        if(inputState == LogicState.HIGH) outputState = LogicState.LOW;
        else outputState = LogicState.HIGH;

        outputStates.add(outputState);
        return outputStates;
    }

    @Override
    public ArrayList<Vector2D> getLocalOutputPositions() {
        ArrayList<Vector2D> outputPositions = new ArrayList<>();
        outputPositions.add(new Vector2D(1,0));
        return outputPositions;
    }
}
