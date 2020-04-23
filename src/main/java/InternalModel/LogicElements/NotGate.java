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
        List<Vector2D> inputPositions = new ArrayList<>();
        inputPositions.add(new Vector2D(this.position.getX(), this.position.getY()));
        return inputPositions;
    }

    @Override
    public ArrayList<LogicState> computeValues(List<LogicState> states) {
        ArrayList<LogicState> outputStates = new ArrayList<>();
        LogicState state1 = states.get(0);
        if(state1 == LogicState.HIGH) outputStates.add(LogicState.LOW);
        else outputStates.add(LogicState.HIGH);
        return outputStates;
    }

    @Override
    public ArrayList<Vector2D> getOutputPositions() {
        ArrayList<Vector2D> outputPositions = new ArrayList<>();
        outputPositions.add(new Vector2D(this.position.getX()+1, this.position.getY()));
        return outputPositions;
    }
}
