package LogicCircuitSimulator.Simulation.NodeHandler;

import LogicCircuitSimulator.Vector2D;

import javax.annotation.concurrent.Immutable;
import java.util.Objects;

@Immutable
public class Node {

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
    private final WireState right;

    /**
     * State of the wire downwards from this node
     */
    private final WireState down;

    /**
     * Whether or not the wires are touching at this node
     */
    private final Crossing isTouching;

    /**
     * Creates a node
     * @param right signal state of the wire to the right of the node
     * @param down signal state of the wire down of the node
     * @param crossing the way wires cross at this node
     */
    public Node(Vector2D position, WireState right, WireState down, Crossing crossing){
        this.position = position;
        this.right = right;
        this.down = down;
        this.isTouching = crossing;
    }

    public Node(Node node){
        this.isTouching = node.isTouching;
        this.down = node.getDownWire();
        this.right = node.getRightWire();
        this.position = node.getPosition();
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
    public Crossing isTouching() {
        return isTouching;
    }

    /**
     * Accepts node visitor
     * @param visitor visiting visitor
     */
    public void accept(NodeVisitor visitor){
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(position, node.position) &&
                right == node.right &&
                down == node.down &&
                isTouching == node.isTouching;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, right, down, isTouching);
    }

    @Override
    public String toString() {
        return "Node{" +
                "position=" + position +
                ", right=" + right +
                ", down=" + down +
                ", isTouching=" + isTouching +
                '}';
    }
}
