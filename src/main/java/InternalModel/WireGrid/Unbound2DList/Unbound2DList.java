package InternalModel.WireGrid.Unbound2DList;

import InternalModel.Vector2D;
import InternalModel.WireGrid.Iterator2D;

import java.util.Optional;

public interface Unbound2DList<T>{

    /**
     * Returns element from array
     * @param pos position of the element
     * @return specified element or empty if element doesn't exist
     */
    Optional<T> get(Vector2D pos);

    /**
     * Sets an element in array
     * @param pos position of the element
     * @param element element to be set
     */
    void set(Vector2D pos, T element);

    /**
     * Removes an element from array
     * @param pos position of the element
     */
    void remove(Vector2D pos);

    /**
     * Iterator for all non-empty elements.
     * @return iterator
     */
    Iterator2D<T> iterator();

}