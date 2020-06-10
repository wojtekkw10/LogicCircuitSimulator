package LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements;

import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.Geometry.LogicElementGeometry;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.Geometry.ZeroInOneOut;
import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Rotation;
import javafx.scene.control.Toggle;

import java.util.ArrayList;
import java.util.List;

public class ToggleOff extends LogicElement{
    public ToggleOff(int x, int y, Rotation rot) {
        super(x, y, rot);
    }
    public ToggleOff(){}

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
