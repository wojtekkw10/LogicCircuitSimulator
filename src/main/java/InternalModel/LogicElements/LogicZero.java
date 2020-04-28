package InternalModel.LogicElements;

import InternalModel.LogicState;
import InternalModel.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class LogicZero extends LogicElement {
    public LogicZero(int x, int y, Rotation rotation) {
        super(x, y, rotation);
    }

    @Override
    public ArrayList<Vector2D> getLocalInputPositions() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<LogicState> computeLocalValues(List<LogicState> states) {
        ArrayList<LogicState> outputStates = new ArrayList<>();
        outputStates.add(LogicState.LOW);
        return outputStates;
    }

    @Override
    public ArrayList<Vector2D> getLocalOutputPositions() {
        ArrayList<Vector2D> outputs = new ArrayList<>();
        outputs.add(new Vector2D(0,0));
        return outputs;
    }
}
