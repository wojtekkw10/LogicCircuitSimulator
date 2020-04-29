package LogicCircuitSimulator.WireGrid;

import javax.annotation.concurrent.Immutable;

/**
 * Wire class. The most basic unit in logic circuit grid.
 */
@Immutable
final public class Node {
    /**
     * State of the wire
     */
    public enum State{
        /**
         * Signal is in the HIGH state, represents logical 1
         */
        HIGH,
        /**
         * Signal is in the LOW state, represents logical 0
         */
        LOW,
        /**
         * There is no wire in this place
         */
        NONE
    }

    /**
     * The way two wires cross
     */
    public enum WireCrossing{
        /**
         * They're touching, signal from horizontal wire propagates to the vertical wire and vice versa
         */
        TOUCHING,
        /**
         * They're not touching, signal from horizontal wire does NOT propagate to the vertical wire and vice versa
         */
        NOT_TOUCHING,
    }

    /**
     * State of the wire to right from this node
     */
    private final State right;

    /**
     * State of the wire downwards from this node
     */
    private final State down;

    /**
     * Whether or not the wires are touching at this node
     */
    private final WireCrossing isTouching;

    /**
     * Creates a default node with no wires in it and NOT_TOUCHING crossing
     */
    public Node(){
        right = State.NONE;
        down = State.NONE;
        isTouching = WireCrossing.NOT_TOUCHING;
    }

    /**
     * Creates a node
     * @param right signal state of the wire to the right of the node
     * @param down signal state of the wire down of the node
     * @param wireCrossing the way wires cross at this node
     */
    public Node(State right, State down, WireCrossing wireCrossing){
        this.right = right;
        this.down = down;
        this.isTouching = wireCrossing;
    }

    /**
     * @return signal state of the wire to the right
     */
    public final State getRightWire() {
        return right;
    }

    /**
     * @return signal state of the wire down of the node
     */
    public final State getDownWire() {
        return down;
    }

    /**
     * @return the way the wires are crossing
     */
    public WireCrossing isTouching() {
        return isTouching;
    }
}
