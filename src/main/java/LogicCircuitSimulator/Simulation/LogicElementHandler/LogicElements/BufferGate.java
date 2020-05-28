package LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements;

import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.Geometry.LogicElementGeometry;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.Geometry.OneInOneOut;
import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Rotation;

import java.util.List;

public class BufferGate extends LogicElement{
    public BufferGate(int x, int y, Rotation rot) {
        super(x, y, rot);
    }

    @Override
    public List<LogicState> computeLocalValues(List<LogicState> states) {
        return states;
    }

    @Override
    public void accept(LogicElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getName() {
        return "BFR";
    }

    @Override
    public LogicElementGeometry getNewGeometry() {
        return new OneInOneOut();
    }
}
