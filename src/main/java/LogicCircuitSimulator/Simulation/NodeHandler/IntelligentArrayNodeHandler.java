package LogicCircuitSimulator.Simulation.NodeHandler;

import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Orientation;
import LogicCircuitSimulator.Vector2D;

import java.util.Iterator;
import java.util.List;

/**
 * Intelligently updates crossings where needed to create cohesive circuits
 */
public class IntelligentArrayNodeHandler implements NodeHandler{
    NodeHandler arrayNodeHandler = new ArrayNodeHandler();
    //TODO: add code here

    @Override
    public void setNode(Node node) {

    }

    @Override
    public Node getNode(Vector2D pos) {
        return null;
    }

    @Override
    public void updateWire(Vector2D pos, Orientation orientation, WireState state) {

    }

    @Override
    public void updateCrossing(Vector2D pos, Crossing crossing) {

    }

    @Override
    public Iterator<Node> iterator() {
        return null;
    }

    @Override
    public LogicState getState(Vector2D pos, Orientation orientation) {
        return null;
    }

    @Override
    public void propagateGenerators(List<Generator> generators) {

    }
}
