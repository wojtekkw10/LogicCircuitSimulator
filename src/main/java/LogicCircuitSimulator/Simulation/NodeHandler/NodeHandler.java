package LogicCircuitSimulator.Simulation.NodeHandler;

import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.NodeGrid;
import LogicCircuitSimulator.Simulation.Orientation;
import LogicCircuitSimulator.Vector2D;

import java.util.Iterator;
import java.util.List;

/**
 * Stores and processes wire data in a logic circuit. The coordinates are unbounded.
 */
public interface NodeHandler extends NodeGrid {

    /**
     * Propagates a HIGH signal from generators throughout the wires.
     * Signal is propagated to surrounding wires in the same direction when Crossing is NOT_TOUCHING.
     * Otherwise the signal can turn to wires on the sides.
     * @param generators list of generators
     */
    void propagateGenerators(List<Generator> generators);
}