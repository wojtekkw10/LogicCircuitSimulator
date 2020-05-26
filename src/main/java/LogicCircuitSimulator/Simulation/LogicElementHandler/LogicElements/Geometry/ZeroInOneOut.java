package LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.Geometry;

import LogicCircuitSimulator.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class ZeroInOneOut extends LogicElementGeometry{
    @Override
    public List<Vector2D> getLocalInputPositions() {
        return new ArrayList<>();
    }

    @Override
    public List<Vector2D> getLocalOutputPositions() {
        ArrayList<Vector2D> outputs = new ArrayList<>();
        outputs.add(new Vector2D(1,0));
        return outputs;
    }
}
