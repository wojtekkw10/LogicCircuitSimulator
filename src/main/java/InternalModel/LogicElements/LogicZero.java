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
        ArrayList<Vector2D> positions = new ArrayList<>();
        positions.add(new Vector2D(this.position.getX(),this.position.getY()));
        return positions;
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
        outputs.add(new Vector2D(this.position.getX(),this.position.getY()));
        return outputs;
    }
}
