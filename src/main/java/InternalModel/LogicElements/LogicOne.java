package InternalModel.LogicElements;

import InternalModel.LogicState;
import InternalModel.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class LogicOne extends LogicElement {

    public LogicOne(int x, int y) {
        super(x, y);
    }

    @Override
    public List<Vector2D> getInputPositions() {
        List<Vector2D> positions = new ArrayList<>();
        positions.add(new Vector2D(this.position.getX(),this.position.getY()));
        return positions;
    }

    @Override
    public LogicState computeValue(List<LogicState> states) {
        return LogicState.HIGH;
    }

    @Override
    public Vector2D getOutput() {
        return new Vector2D(this.position.getX(),this.position.getY());
    }
}
