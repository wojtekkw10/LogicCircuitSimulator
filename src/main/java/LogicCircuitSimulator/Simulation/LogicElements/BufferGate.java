package LogicCircuitSimulator.Simulation.LogicElements;

import LogicCircuitSimulator.Simulation.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicElements.Geometry.LogicElementGeometry;
import LogicCircuitSimulator.Simulation.LogicElements.Geometry.OneInOneOut;
import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Vector2D;

import java.util.ArrayList;
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
