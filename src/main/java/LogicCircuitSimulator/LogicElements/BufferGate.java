package LogicCircuitSimulator.LogicElements;

import LogicCircuitSimulator.LogicElementVisitor;
import LogicCircuitSimulator.LogicState;
import LogicCircuitSimulator.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class BufferGate extends LogicElement{
    public BufferGate(int x, int y, Rotation rot) {
        super(x, y, rot);
    }

    @Override
    public List<Vector2D> getLocalInputPositions() {
        ArrayList<Vector2D> inputPositions = new ArrayList<>();
        inputPositions.add(new Vector2D(0,0));
        return inputPositions;
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
    public List<Vector2D> getLocalOutputPositions() {
        ArrayList<Vector2D> outputPositions = new ArrayList<>();
        outputPositions.add(new Vector2D(1,0));
        return outputPositions;
    }
}
