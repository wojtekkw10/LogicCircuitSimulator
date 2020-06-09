package LogicCircuitSimulator.BooleanExpressionParser.CircuitGenerator;

import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.Geometry.LogicElementGeometry;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.Geometry.OneInOneOut;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Rotation;

import java.util.List;

public class GhostGate extends LogicElement {
    public GhostGate(int x, int y, Rotation rot) {
        super(x, y, rot);
    }

    @Override
    public String getName() {
        return "GHOST";
    }

    @Override
    public LogicElementGeometry getNewGeometry() {
        return new OneInOneOut();
    }

    @Override
    public List<LogicState> computeLocalValues(List<LogicState> states) {
        return states;
    }

    @Override
    public void accept(LogicElementVisitor visitor) {
        visitor.visit(this);
    }
}
