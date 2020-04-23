package InternalModel.WireGrid;

import InternalModel.LogicState;
import InternalModel.Orientation;
import InternalModel.Vector2D;

import java.util.List;

/**
 * Stores and processes wires in logic circuits.
 * @implSpec The grid is unbounded up, that means that setElement() and getElement()
 * functions should throw IllegalArgumentException when x or y is < 0.
 * Otherwise they should perform operations correctly.
 */
public interface WireGrid {

    /**
     * Sets a Wire element in the grid
     * @param pos position of the element
     * @param node specified wire
     * @throws IllegalArgumentException if wire pos is negative
     */
    void setElement(Vector2D pos, Node node);

    /**
     * Resets all wires in the grid to LOW. Should be executed before propagateGenerators()
     */
    void resetWiresToLow();

    /**
     * Propagates a HIGH signal from generators throughout the wires.
     * Signal is propagated to surrounding wires in the same direction when Crossing is NOT_TOUCHING.
     * Otherwise the signal can turn to wires on the sides.
     * @param generators list of generators
     * @throws IllegalArgumentException if generators' position is negative
     */
    void propagateGenerators(List<Generator> generators);

    /**
     * Returns a LogicState at a given point in the grid.
     * Returns HIGH when a HIGH signal is going through the point in the specified direction.
     * Otherwise returns LOW.
     * @param pos position of the point in the grid
     * @param orientation direction of the wire
     * @return LogicState at a specified point
     * @throws IllegalArgumentException if pos is negative
     */
    LogicState getState(Vector2D pos, Orientation orientation);

    /**
     * Returns a Wire element at a specified position
     * @param pos position of the wire
     * @return wire at a specified position
     * @throws IllegalArgumentException if pos is negative
     */
    Node getElement(Vector2D pos);

    /**
     * Iterator for all wire grid elements.
     * Returns elements from rectangular array where width and height is determined by furthest placed objects
     * @return wire iterator
     */
    Iterator2D<Node> getIterator();

    /**
     * Updates WireState of the wire at a given position. Recommended for removing a wire from grid.
     * @param pos position of the wire. When wire is vertical, it's the upper point. When it's horizontal it's the leftmost point.
     * @param orientation orientation of the wire. Either Horizontally or Vertically
     * @param state new state applied to the specified wire
     * @throws IllegalArgumentException if pos is negative
     */
    void updateWire(Vector2D pos, Orientation orientation, Node.State state);

    /**
     * Updates type of the crossing at a specified position in the grid
     * @param pos position of the crossing
     * @param crossing new crossingType applied to the crossing
     * @throws IllegalArgumentException if pos is negative
     */
    void updateCrossing(Vector2D pos, Node.WireCrossing crossing);

    /**
     * Upper bound on the array. Is equal to the furthest set element in the horizontal direction
     * @return 'width' of the grid
     */
    int getWidth();

    /**
     * Upper bound on the array. Is equal to the furthest set element in the vertical direction
     * @return 'height' of the grid
     */
    int getHeight();

}