package InternalModel.LogicElements;

import InternalModel.LogicState;
import InternalModel.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class NotGate extends LogicElement{
    public NotGate(int x, int y) {
        super(x, y);
    }

    @Override
    public List<Vector2D> getInputPositions() {
        //TODO: przelozyc do konstruktora
        List<Vector2D> inputPositions = new ArrayList<>();
        inputPositions.add(new Vector2D(this.position.getX(), this.position.getY()));
        return inputPositions;
    }

    @Override
    public LogicState computeValue(List<LogicState> states) {
        LogicState state1 = states.get(0);
        if(state1 == LogicState.HIGH) return LogicState.LOW;
        else return LogicState.HIGH;
    }

    @Override
    public Vector2D getOutput() {
        //TODO: przelozyc do konstruktora
        return new Vector2D(this.position.getX()+1, this.position.getY());
    }
}
