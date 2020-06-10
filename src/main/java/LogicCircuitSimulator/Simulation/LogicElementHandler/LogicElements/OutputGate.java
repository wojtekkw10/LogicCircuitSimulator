package LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements;

import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.Geometry.LogicElementGeometry;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.Geometry.OneInZerOut;
import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Rotation;

import java.util.ArrayList;
import java.util.List;

public class OutputGate extends LogicElement{
    public OutputGate(int x, int y, Rotation rotation) {
        super(x, y, rotation);
    }
    public OutputGate(){}

    @Override
    public List<LogicState> computeLocalValues(List<LogicState> states) {
        ArrayList<LogicState> outputStates = new ArrayList<>();
        outputStates.add(LogicState.LOW);
        return outputStates;
    }

    @Override
    public String getName() {
        return "OUT";
    }

    @Override
    public LogicElementGeometry getNewGeometry() {
        return new OneInZerOut();

    }

    @Override
    public void accept(LogicElementVisitor visitor) {
        visitor.visit(this);
    }

}
