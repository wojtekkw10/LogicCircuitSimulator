package LogicCircuitSimulator.Simulation.LogicElements;

import LogicCircuitSimulator.Simulation.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class NotGate extends LogicElement{
    public NotGate(int x, int y, Rotation rotation) {
        super(x, y, rotation);
    }

    @Override
    public List<Vector2D> getLocalInputPositions() {
        ArrayList<Vector2D> inputPositions = new ArrayList<>();
        inputPositions.add(new Vector2D(0,0));
        return inputPositions;
    }

    @Override
    public List<LogicState> computeLocalValues(List<LogicState> states) {
        ArrayList<LogicState> outputStates = new ArrayList<>();

        LogicState outputState;

        LogicState inputState = states.get(0);
        if(inputState == LogicState.HIGH) outputState = LogicState.LOW;
        else outputState = LogicState.HIGH;

        outputStates.add(outputState);
        return outputStates;
    }

    @Override
    public List<Vector2D> getLocalOutputPositions() {
        ArrayList<Vector2D> outputPositions = new ArrayList<>();
        outputPositions.add(new Vector2D(1,0));
        return outputPositions;
    }

    @Override
    public String getName() {
        return "NOT";
    }

    @Override
    public void accept(LogicElementVisitor visitor) {
        visitor.visit(this);
    }
}
