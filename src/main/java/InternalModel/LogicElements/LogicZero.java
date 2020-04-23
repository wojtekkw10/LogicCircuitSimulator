package InternalModel.LogicElements;

import InternalModel.LogicElements.LogicElement;
import InternalModel.LogicState;
import InternalModel.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class LogicZero extends LogicElement {
    public LogicZero(int x, int y) {
        super(x, y);
    }

    @Override
    public List<Vector2D> getInputPositions() {
        List<Vector2D> positions = new ArrayList<>();
        positions.add(new Vector2D(this.position.getX(),this.position.getY()));
        return positions;
    }

    @Override
    public ArrayList<LogicState> computeValues(List<LogicState> states) {
        ArrayList<LogicState> outputStates = new ArrayList<>();
        outputStates.add(LogicState.LOW);
        return outputStates;
    }

    @Override
    public ArrayList<Vector2D> getOutputPositions() {
        ArrayList<Vector2D> outputs = new ArrayList<>();
        outputs.add(new Vector2D(this.position.getX(),this.position.getY()));
        return outputs;
    }
}
