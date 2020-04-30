package LogicCircuitSimulator.WireGrid;

import LogicCircuitSimulator.NodeVisitor;
import LogicCircuitSimulator.Vector2D;

import javax.annotation.concurrent.Immutable;

@Immutable
public class Node {
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

    public final Vector2D getPosition() {
        return position;
    }

    /**
     * Position of the node in the grid
     */
    private final Vector2D position;

    /**
     * State of the wire to right from this node
     */
    private final Node.State right;

    /**
     * State of the wire downwards from this node
     */
    private final Node.State down;

    /**
     * Whether or not the wires are touching at this node
     */
    private final Node.WireCrossing isTouching;

    /**
     * Creates a node
     * @param right signal state of the wire to the right of the node
     * @param down signal state of the wire down of the node
     * @param wireCrossing the way wires cross at this node
     */
    public Node(Vector2D position, Node.State right, Node.State down, Node.WireCrossing wireCrossing){
        this.position = position;
        this.right = right;
        this.down = down;
        this.isTouching = wireCrossing;
    }

    /**
     * @return signal state of the wire to the right
     */
    public final Node.State getRightWire() {
        return right;
    }

    /**
     * @return signal state of the wire down of the node
     */
    public final Node.State getDownWire() {
        return down;
    }

    /**
     * @return the way the wires are crossing
     */
    public Node.WireCrossing isTouching() {
        return isTouching;
    }

    /**
     * Accepts node visitor
     * @param visitor visiting visitor
     */
    public void accept(NodeVisitor visitor){
        visitor.visit(this);
    }

}
