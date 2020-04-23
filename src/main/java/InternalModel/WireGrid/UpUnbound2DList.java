package InternalModel.WireGrid;

import InternalModel.Vector2D;

public interface UpUnbound2DList<T> {
    void set(Vector2D pos, T element);
    T get(Vector2D pos);
    int getWidth();
    int getHeight();
}
