package LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements;

import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.Geometry.LogicElementGeometry;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.Geometry.OneInOneOut;
import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Rotation;

import java.util.ArrayList;
import java.util.List;

public class NotGate extends LogicElement{
    public NotGate(int x, int y, Rotation rotation) {
        super(x, y, rotation);
    }
    public NotGate(){}

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
    public String getName() {
        return "NOT";
    }

    @Override
    public LogicElementGeometry getNewGeometry() {
        return new OneInOneOut();

    }

    @Override
    public void accept(LogicElementVisitor visitor) {
        visitor.visit(this);
    }
}
