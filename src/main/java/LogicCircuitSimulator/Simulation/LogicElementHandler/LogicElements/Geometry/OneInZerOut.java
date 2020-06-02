package LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.Geometry;

import LogicCircuitSimulator.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class OneInZerOut extends LogicElementGeometry{
    @Override
    public List<Vector2D> getLocalInputPositions() {
        ArrayList<Vector2D> inputPositions = new ArrayList<>();
        inputPositions.add(new Vector2D(0,0));
        return inputPositions;
    }

    @Override
    public List<Vector2D> getLocalOutputPositions() {
        return new ArrayList<>();
    }
}
