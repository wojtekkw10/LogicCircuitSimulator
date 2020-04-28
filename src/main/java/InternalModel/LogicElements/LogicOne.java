package InternalModel.LogicElements;

import InternalModel.LogicState;
import InternalModel.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class LogicOne extends LogicElement {

    public LogicOne(int x, int y, Rotation rotation) {
        super(x, y, rotation);
    }

    @Override
    public List<Vector2D> getLocalInputPositions() {
        return new ArrayList<>();
    }

    @Override
    public List<LogicState> computeLocalValues(List<LogicState> states) {
        ArrayList<LogicState> outputStates = new ArrayList<>();
        outputStates.add(LogicState.HIGH);
        return outputStates;
    }

    @Override
    public List<Vector2D> getLocalOutputPositions() {
        ArrayList<Vector2D> outputs = new ArrayList<>();
        outputs.add(new Vector2D(0,0));
        return outputs;
    }
}
