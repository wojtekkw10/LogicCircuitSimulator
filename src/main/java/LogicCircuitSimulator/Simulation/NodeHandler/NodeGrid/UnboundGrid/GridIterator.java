package LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.UnboundGrid;

import LogicCircuitSimulator.Vector2D;

/**
 * Interface for iterator that gives 2D position of the element
 * @param <T> type of the element
 */
public interface GridIterator<T> {
    /**
     * Checks for the next element
     * @return true if there is a next element in the array
     */
    boolean hasNext();

    /**
     * Returns the element in the array
     * @return element from array
     */
    T next();

    /**
     * 2D position of current element
     * @return position of element
     */
    Vector2D currentPosition();
}
