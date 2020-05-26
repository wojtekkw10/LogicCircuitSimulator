package LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements;

import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Vector2D;

public class ComputedValue {
    private final Vector2D pos;
    private final LogicState state;

    public ComputedValue(Vector2D pos, LogicState state) {
        this.pos = pos;
        this.state = state;
    }

    public Vector2D getPos() {
        return pos;
    }

    public LogicState getState() {
        return state;
    }

    @Override
    public String toString() {
        return "ComputedValue{" +
                "" + pos +
                ", " + state +
                '}';
    }
}
