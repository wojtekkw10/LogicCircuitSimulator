package InternalModel.WireGrid;

import InternalModel.Vector2D;

/**
 * Interface for unbounded 2D arrays
 * @param <T> type of array element
 */
interface UpUnbound2DList<T> {

    /**
     * Returns element from array. Because of possible optimizations, should NEVER be used to change object's state.
     * It's best to use Immutable objects.
     * @param pos position of the element
     * @return specified element
     */
    T get(Vector2D pos);

    /**
     * Sets an element in array
     * @param pos position of the element
     * @param element element to be set
     */
    void set(Vector2D pos, T element);

    /**
     * Position of the furthest element in the horizontal direction
     * @return width of the enclosing array
     */
    int getWidth();

    /**
     * Position of the furthest element in the vertical direction
     * @return height of the enclosing array
     */
    int getHeight();
}
