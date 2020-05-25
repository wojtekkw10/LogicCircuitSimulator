package LogicCircuitSimulator.Simulation.LogicElements;

import LogicCircuitSimulator.Simulation.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class ToggleOn extends LogicElement{
    public ToggleOn(int x, int y, Rotation rot) {
        super(x, y, rot);
    }

    @Override
    public List<LogicState> computeLocalValues(List<LogicState> states) {
        ArrayList<LogicState> outputStates = new ArrayList<>();
        outputStates.add(LogicState.HIGH);
        return outputStates;
    }

    @Override
    public void accept(LogicElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getName() {
        return "TGL_ON";
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
