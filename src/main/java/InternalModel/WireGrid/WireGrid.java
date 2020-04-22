package InternalModel.WireGrid;

import InternalModel.LogicState;
import InternalModel.Orientation;
import InternalModel.Vector2D;

import java.util.List;

/**
 * Stores and processes wires in logic circuits.
 * @implSpec setElement() and getElement() function should throw IllegalArgumentException when x or y is < 0.
 * Otherwise they should perform operations correctly.
 * Functions getWidth() and getHeight() should give upper bounds for width and height of the grid for iteration purposes.
 */
public interface WireGrid {

    /**
     * Sets a Wire element at a specified position (x,y)
     * @param pos position of the wire
     * @param wire specified wire
     */
    void setElement(Vector2D pos, Wire wire);

    /**
     * Resets all wires in the grid to LOW
     */
    void resetWiresToLow();

    /**
     * Propagates a HIGH signal from generators throughout the wires.
     * Signal is propagated to surrounding wires in the same direction when Crossing is NOT_TOUCHING.
     * Otherwise the signal can turn to wires on the sides.
     * @param generators list of generators
     */
    void propagateGenerators(List<Vector2D> generators);

    /**
     * Returns a LogicState at a given point in the grid.
     * Returns HIGH when a HIGH signal is going through the point in the specified direction.
     * Otherwise returns LOW.
     * @param pos position of the point in the grid
     * @param orientation direction of the wire
     * @return LogicState at a specified point
     */
    LogicState getState(Vector2D pos, Orientation orientation);

    /**
     * Returns a Wire element at a specified position
     * @param pos position of the wire
     * @return wire at a specified position
     */
    Wire getElement(Vector2D pos);

    /**
     * Upper bound in the x direction. For iteration purposes.
     * @return upper bound in x direction
     */
    int getWidth();

    /**
     * Upper bound in the y direction. For iteration purposes.
     * @return upper bound in y direction
     */
    int getHeight();
}
