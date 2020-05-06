package LogicCircuitSimulator.WireGrid;

import LogicCircuitSimulator.LogicState;
import LogicCircuitSimulator.Orientation;
import LogicCircuitSimulator.Vector2D;

import java.util.Iterator;
import java.util.List;

/**
 * Stores and processes wire data in a logic circuit. The coordinates are unbounded.
 */
public interface WireGrid {

    /**
     * Sets a Wire element in the grid
     * @param node specified wire
     */
    void setNode(Node node);

    /**
     * Returns a Node element at a specified position
     * @param pos position of the wire
     * @return node at a specified position
     */
    Node getNode(Vector2D pos);

    /**
     * Updates WireState of the wire at a given position. Recommended for removing a wire from grid.
     * @param pos position of the wire. When wire is vertical, it's the upper point. When it's horizontal it's the leftmost point.
     * @param orientation orientation of the wire. Either Horizontally or Vertically
     * @param state new state applied to the specified wire
     */
    void updateWire(Vector2D pos, Orientation orientation, Node.State state);

    /**
     * Updates type of the crossing at a specified position in the grid
     * @param pos position of the crossing
     * @param crossing new crossingType applied to the crossing
     */
    void updateCrossing(Vector2D pos, Node.WireCrossing crossing);

    /**
     * Iterator for all non-empty wire grid nodes.
     * @return node iterator
     */
    Iterator<Node> iterator();

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
     * Propagates a HIGH signal from generators throughout the wires.
     * Signal is propagated to surrounding wires in the same direction when Crossing is NOT_TOUCHING.
     * Otherwise the signal can turn to wires on the sides.
     * @param generators list of generators
     */
    void propagateGenerators(List<Generator> generators);


}