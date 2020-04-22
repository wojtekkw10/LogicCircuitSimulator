package InternalModel.WireGrid;

import InternalModel.Orientation;
import InternalModel.Vector2D;

public final class Generator {
    private final Vector2D pos;
    private final Orientation orientation;

    public Generator(Vector2D pos, Orientation orientation) {
        this.pos = pos;
        this.orientation = orientation;
    }

    public Vector2D getPos() {
        return pos;
    }

    public Orientation getOrientation() {
        return orientation;
    }
}
