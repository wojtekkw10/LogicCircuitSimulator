package LogicCircuitSimulator.Simulation.LogicElements;

import LogicCircuitSimulator.Simulation.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicElements.Geometry.LogicElementGeometry;
import LogicCircuitSimulator.Simulation.LogicElements.Geometry.ZeroInOneOut;
import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class LogicZero extends LogicElement {
    public LogicZero(int x, int y, Rotation rotation) {
        super(x, y, rotation);
    }

    @Override
    public List<LogicState> computeLocalValues(List<LogicState> states) {
        ArrayList<LogicState> outputStates = new ArrayList<>();
        outputStates.add(LogicState.LOW);
        return outputStates;
    }

    @Override
    public String getName() {
        return "ZERO";
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
