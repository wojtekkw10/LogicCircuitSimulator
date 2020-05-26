package LogicCircuitSimulator.Simulation.LogicElements;

import LogicCircuitSimulator.Simulation.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicElements.Geometry.LogicElementGeometry;
import LogicCircuitSimulator.Simulation.LogicElements.Geometry.TwoInOneOut;
import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class AndGate extends LogicElement{

    public AndGate(int x, int y, Rotation rot) {
        super(x, y, rot);
    }

    @Override
    public String getName() {
        return "AND";
    }

    @Override
    public LogicElementGeometry getNewGeometry() {
        return new TwoInOneOut();
    }

    @Override
    public List<LogicState> computeLocalValues(List<LogicState> states) {
        List<LogicState> outputState = new ArrayList<>();
        if(states.get(0) == LogicState.HIGH && states.get(1) == LogicState.HIGH){
            outputState.add(LogicState.HIGH);
        }
        else{
            outputState.add(LogicState.LOW);
        }
        return outputState;
    }

    @Override
    public void accept(LogicElementVisitor visitor) {
        visitor.visit(this);
    }
}
