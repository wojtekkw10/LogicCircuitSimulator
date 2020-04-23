package InternalModel.WireGrid;

import InternalModel.Vector2D;

public interface Iterator2D<T> {
    boolean hasNext();
    T next();
    Vector2D currentPosition();
}
