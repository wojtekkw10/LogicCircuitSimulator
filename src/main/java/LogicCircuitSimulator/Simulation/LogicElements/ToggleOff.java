package LogicCircuitSimulator.Simulation.LogicElements;

import LogicCircuitSimulator.Simulation.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicElements.Geometry.LogicElementGeometry;
import LogicCircuitSimulator.Simulation.LogicElements.Geometry.ZeroInOneOut;
import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class ToggleOff extends LogicElement{
    public ToggleOff(int x, int y, Rotation rot) {
        super(x, y, rot);
    }

    @Override
    public List<LogicState> computeLocalValues(List<LogicState> states) {
        ArrayList<LogicState> outputStates = new ArrayList<>();
        outputStates.add(LogicState.LOW);
        return outputStates;
    }

    @Override
    public void accept(LogicElementVisitor visitor) {
        visitor.visit(this);
    }


    @Override
    public String getName() {
        return "TGL_OFF";
    }

    @Override
    public LogicElementGeometry getNewGeometry() {
        return new ZeroInOneOut();

    }
}
