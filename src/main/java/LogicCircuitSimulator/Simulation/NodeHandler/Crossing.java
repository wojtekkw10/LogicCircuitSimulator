package LogicCircuitSimulator.Simulation.NodeHandler;

/**
 * The way two wires cross
 */
public enum Crossing{
    /**
     * They're touching, signal from horizontal wire propagates to the vertical wire and vice versa
     */
    TOUCHING,
    /**
     * They're not touching, signal from horizontal wire does NOT propagate to the vertical wire and vice versa
     */
    NOT_TOUCHING,
}
