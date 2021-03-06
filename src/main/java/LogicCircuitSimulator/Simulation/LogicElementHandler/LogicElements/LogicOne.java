package LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements;

import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.Geometry.LogicElementGeometry;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.Geometry.ZeroInOneOut;
import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Rotation;

import java.util.ArrayList;
import java.util.List;

public class LogicOne extends LogicElement {

    public LogicOne(int x, int y, Rotation rotation) {
        super(x, y, rotation);
    }
    public LogicOne(){}

    @Override
    public List<LogicState> computeLocalValues(List<LogicState> states) {
        ArrayList<LogicState> outputStates = new ArrayList<>();
        outputStates.add(LogicState.HIGH);
        return outputStates;
    }

    @Override
    public String getName() {
        return "ONE";
    }

    @Override
    public LogicElementGeometry getNewGeometry() {
        return new ZeroInOneOut();
    }

    @Override
    public void accept(LogicElementVisitor visitor) {
        visitor.visit(this);
    }
}
