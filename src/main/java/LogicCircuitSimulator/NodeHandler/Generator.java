package LogicCircuitSimulator.NodeHandler;

import LogicCircuitSimulator.Orientation;
import LogicCircuitSimulator.Vector2D;

/**
 * Stores generator data
 */
public final class Generator {
    /**
     * Position of the generator
     */
    private final Vector2D pos;

    /**
     * Orientation - either vertically or horizontally
     */
    private final Orientation orientation;

    /**
     * Creates a generator
     * @param pos position
     * @param orientation orientation
     */
    public Generator(Vector2D pos, Orientation orientation) {
        this.pos = pos;
        this.orientation = orientation;
    }

    /**
     * @return position of the generator
     */
    public Vector2D getPos() {
        return pos;
    }

    /**
     * @return orientation of the generator
     */
    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public String toString() {
        return "Generator{" +
                pos +
                ", " + orientation +
                '}';
    }
}
