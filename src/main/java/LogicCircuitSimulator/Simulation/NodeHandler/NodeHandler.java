package LogicCircuitSimulator.Simulation.NodeHandler;

import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Orientation;
import LogicCircuitSimulator.Vector2D;

import java.util.Iterator;
import java.util.List;

/**
 * Stores and processes wire data in a logic circuit. The coordinates are unbounded.
 */
public interface NodeHandler {

    void setRightWire(Vector2D pos, WireState state);
    void setDownWire(Vector2D pos, WireState state);

    WireState getRightWire(Vector2D pos);
    WireState getDownWire(Vector2D pos);
    WireState getUpWire(Vector2D pos);
    WireState getLeftWire(Vector2D pos);
    Crossing getCrossing(Vector2D pos);

    /**
     * Updates type of the crossing at a specified position in the grid
     * @param pos position of the crossing
     * @param crossing new crossingType applied to the crossing
     */
    void setCrossing(Vector2D pos, Crossing crossing);

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