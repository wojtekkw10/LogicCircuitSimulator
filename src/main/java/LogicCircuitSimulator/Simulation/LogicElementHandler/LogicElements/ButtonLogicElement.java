package LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements;

import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.Geometry.LogicElementGeometry;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.Geometry.ZeroInOneOut;
import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Rotation;

import java.util.ArrayList;
import java.util.List;

public class ButtonLogicElement extends LogicElement{
    private int counter = 20;

    public ButtonLogicElement(int x, int y, Rotation rot) {
        super(x, y, rot);
    }
    public ButtonLogicElement(){}

    @Override
    public List<LogicState> computeLocalValues(List<LogicState> states) {
        ArrayList<LogicState> outputStates = new ArrayList<>();
        if(counter > 0) outputStates.add(LogicState.HIGH);
        else outputStates.add(LogicState.LOW);
        counter--;
        return outputStates;
    }

    @Override
    public void accept(LogicElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getName() {
        return "BTN";
    }

    @Override
    public LogicElementGeometry getNewGeometry() {
        return new ZeroInOneOut();
    }
}
