package LogicCircuitSimulator.Simulation.NodeHandler;

import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Orientation;
import LogicCircuitSimulator.Vector2D;

import java.util.Iterator;
import java.util.List;

/**
 * Intelligently assumes touching crossings where needed to create cohesive circuits
 */
public class IntelligentArrayNodeHandler implements NodeHandler{
    NodeHandler arrayNodeHandler = new ArrayNodeHandler();

    @Override
    public void setRightWire(Vector2D pos, WireState state) {

    }

    @Override
    public void setDownWire(Vector2D pos, WireState state) {

    }

    @Override
    public WireState getRightWire(Vector2D pos) {
        return null;
    }

    @Override
    public WireState getDownWire(Vector2D pos) {
        return null;
    }

    @Override
    public WireState getUpWire(Vector2D pos) {
        return null;
    }

    @Override
    public WireState getLeftWire(Vector2D pos) {
        return null;
    }

    @Override
    public Crossing getCrossing(Vector2D pos) {
        return null;
    }

    @Override
    public void setCrossing(Vector2D pos, Crossing crossing) {

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
    //TODO: add code here
}
