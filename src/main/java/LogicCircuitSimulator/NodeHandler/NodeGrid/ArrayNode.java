package LogicCircuitSimulator.NodeHandler.NodeGrid;

import LogicCircuitSimulator.NodeHandler.Node;
import LogicCircuitSimulator.NodeHandler.WireState;

import javax.annotation.concurrent.Immutable;

/**
 * Wire class. The most basic unit in logic circuit grid.
 */
@Immutable
final public class ArrayNode {

    /**
     * State of the wire to right from this node
     */
    private final WireState right;

    /**
     * State of the wire downwards from this node
     */
    private final WireState down;

    /**
     * Whether or not the wires are touching at this node
     */
    private final Node.WireCrossing isTouching;

    /**
     * Creates a default node with no wires in it and NOT_TOUCHING crossing
     */
    public ArrayNode(){
        right = WireState.NONE;
        down = WireState.NONE;
        isTouching = Node.WireCrossing.NOT_TOUCHING;
    }

    /**
     * Creates a node
     * @param right signal state of the wire to the right of the node
     * @param down signal state of the wire down of the node
     * @param wireCrossing the way wires cross at this node
     */
    public ArrayNode(WireState right, WireState down, Node.WireCrossing wireCrossing){
        this.right = right;
        this.down = down;
        this.isTouching = wireCrossing;
    }

    /**
     * @return signal state of the wire to the right
     */
    public final WireState getRightWire() {
        return right;
    }

    /**
     * @return signal state of the wire down of the node
     */
    public final WireState getDownWire() {
        return down;
    }

    /**
     * @return the way the wires are crossing
     */
    public Node.WireCrossing isTouching() {
        return isTouching;
    }

    /**
     * Static factory method (Effective Java: Item 1)
     */
    public static ArrayNode fromNode(Node node){
        return new ArrayNode(node.getRightWire(), node.getDownWire(), node.isTouching());
    }
}
