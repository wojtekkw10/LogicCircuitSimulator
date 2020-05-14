package LogicCircuitSimulator.Simulation.LogicElements;

import LogicCircuitSimulator.Simulation.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Vector2D;

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
        outputs.add(new Vector2D(1,0));
        return outputs;
    }

    @Override
    public void accept(LogicElementVisitor visitor) {
        visitor.visit(this);
    }
}
