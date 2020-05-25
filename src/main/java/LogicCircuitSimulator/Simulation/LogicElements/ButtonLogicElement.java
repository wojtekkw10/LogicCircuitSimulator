package LogicCircuitSimulator.Simulation.LogicElements;

import LogicCircuitSimulator.Simulation.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class ButtonLogicElement extends LogicElement{
    private int counter = 20;

    public ButtonLogicElement(int x, int y, Rotation rot) {
        super(x, y, rot);
    }

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
        return new LogicElementGeometry() {
            @Override
            public List<Vector2D> getLocalInputPositions() {
                return new ArrayList<>();
            }

            @Override
            public List<Vector2D> getLocalOutputPositions() {
                ArrayList<Vector2D> outputPositions = new ArrayList<>();
                outputPositions.add(new Vector2D(1,0));
                return outputPositions;
            }
        };
    }
}
