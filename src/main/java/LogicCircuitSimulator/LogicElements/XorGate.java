package LogicCircuitSimulator.LogicElements;

import LogicCircuitSimulator.LogicElementVisitor;
import LogicCircuitSimulator.LogicState;
import LogicCircuitSimulator.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class XorGate extends LogicElement{
    public XorGate(int x, int y, Rotation rot) {
        super(x, y, rot);
    }

    @Override
    List<Vector2D> getLocalInputPositions() {
        ArrayList<Vector2D> inputPositions = new ArrayList<>();
        inputPositions.add(new Vector2D(0,0));
        inputPositions.add(new Vector2D(0,1));
        return inputPositions;
    }

    @Override
    List<Vector2D> getLocalOutputPositions() {
        ArrayList<Vector2D> outputPositions = new ArrayList<>();
        outputPositions.add(new Vector2D(1,0));
        return outputPositions;
    }

    @Override
    List<LogicState> computeLocalValues(List<LogicState> states) {
        List<LogicState> outputState = new ArrayList<>();
        if(states.get(0) == LogicState.HIGH && states.get(1) == LogicState.LOW){
            outputState.add(LogicState.HIGH);
        }
        else if(states.get(0) == LogicState.LOW && states.get(1) == LogicState.HIGH){
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
