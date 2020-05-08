package LogicCircuitSimulator.WireGrid;

import LogicCircuitSimulator.LogicState;
import LogicCircuitSimulator.Orientation;
import LogicCircuitSimulator.Vector2D;
import LogicCircuitSimulator.WireGrid.Unbound2DList.UnboundGrid;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayNodeGrid implements NodeGrid{
    UnboundGrid<ArrayNode> nodes;

    public ArrayNodeGrid(UnboundGrid<ArrayNode> unboundGrid){
        this.nodes = unboundGrid;
    }

    @Override
    public Node getNode(Vector2D pos) {
        ArrayNode arrayNode = nodes.get(pos).orElse(new ArrayNode());
        return new Node(pos, arrayNode.getRightWire(), arrayNode.getDownWire(), arrayNode.isTouching());
    }

    @Override
    public void setNode(Vector2D pos, Node node) {
        nodes.set(pos, ArrayNode.fromNode(node));
    }

    @Override
    public WireState getLeftWire(Vector2D pos) {
        return getNode(new Vector2D(pos.getX()-1, pos.getY())).getRightWire();
    }

    @Override
    public WireState getUpWire(Vector2D pos) {
        return getNode(new Vector2D(pos.getX(), pos.getY()-1)).getDownWire();
    }

    @Override
    public WireState getRightWire(Vector2D pos) {
        return getNode(new Vector2D(pos.getX(), pos.getY())).getRightWire();
    }

    @Override
    public WireState getDownWire(Vector2D pos) {
        return getNode(new Vector2D(pos.getX(), pos.getY())).getDownWire();
    }

    @Override
    public void setLeftWire(Vector2D pos, WireState state) {
        Vector2D realPos = new Vector2D(pos.getX()-1, pos.getY());
        Node node = getNode(realPos);
        ArrayNode arrayNode = new ArrayNode(state, node.getDownWire(), node.isTouching());
        nodes.set(realPos, arrayNode);
    }

    @Override
    public void setUpWire(Vector2D pos, WireState state) {
        Vector2D realPos = new Vector2D(pos.getX(), pos.getY()-1);
        Node node = getNode(realPos);
        ArrayNode arrayNode = new ArrayNode(node.getRightWire(), state, node.isTouching());
        nodes.set(realPos, arrayNode);
    }

    @Override
    public void setRightWire(Vector2D pos, WireState state) {
        Node node = getNode(pos);
        ArrayNode arrayNode = new ArrayNode(state, node.getDownWire(), node.isTouching());
        nodes.set(pos, arrayNode);
    }

    @Override
    public void setDownWire(Vector2D pos, WireState state) {
        Node node = getNode(pos);
        ArrayNode arrayNode = new ArrayNode(node.getRightWire(), state, node.isTouching());
        nodes.set(pos, arrayNode);
    }

    @Override
    public void setCrossing(Vector2D pos, Node.WireCrossing crossing) {
        Node node = getNode(pos);
        ArrayNode arrayNode = new ArrayNode(node.getRightWire(), node.getDownWire(), crossing);
        nodes.set(pos, arrayNode);
    }

    @Override
    public Node.WireCrossing getCrossing(Vector2D pos) {
        return getNode(pos).isTouching();
    }

    @Override
    public LogicState getState(Vector2D pos, Orientation orientation) {
        double x = pos.getX();
        double y = pos.getY();

        if(orientation == null) throw new NullPointerException("Argument 'orientation' is null");

        ArrayNode arrayNode = getArrayNode(pos);

        if(arrayNode.isTouching() == Node.WireCrossing.TOUCHING){
            if(arrayNode.getDownWire() == WireState.HIGH) return LogicState.HIGH;
            if(arrayNode.getRightWire() == WireState.HIGH) return LogicState.HIGH;

            ArrayNode upperArrayNode = getArrayNode(new Vector2D(x, y-1));
            if(upperArrayNode.getDownWire() == WireState.HIGH) return LogicState.HIGH;

            ArrayNode arrayNodeToLeft = getArrayNode(new Vector2D(x-1, y));
            if(arrayNodeToLeft.getRightWire() == WireState.HIGH)
                return LogicState.HIGH;
        }
        else{
            if(orientation == Orientation.VERTICALLY){
                if(arrayNode.getDownWire() == WireState.HIGH) return LogicState.HIGH;

                ArrayNode upperArrayNode = getArrayNode(new Vector2D(x, y-1));
                if(upperArrayNode.getDownWire() == WireState.HIGH) return LogicState.HIGH;
            }
            else{
                if(arrayNode.getRightWire() == WireState.HIGH) return LogicState.HIGH;

                ArrayNode arrayNodeToLeft = getArrayNode(new Vector2D(x-1, y));
                if(arrayNodeToLeft.getRightWire() == WireState.HIGH) return LogicState.HIGH;
            }
        }
        return LogicState.LOW;
    }

    private ArrayNode getArrayNode(Vector2D pos){
        return nodes.get(pos).orElse(new ArrayNode());
    }

    @Override
    public Iterator<Node> iterator() {
        return new MainIterator();
    }

    private class MainIterator implements Iterator<Node>{
        Iterator2D<ArrayNode> iterator2D = nodes.iterator();

        @Override
        public boolean hasNext() {
            return iterator2D.hasNext();
        }

        @Override
        public Node next() {
            if(!hasNext()) throw new NoSuchElementException("ArrayNodeGrid has no more elements");

            ArrayNode arrayNode = iterator2D.next();
            Vector2D currentPosition = iterator2D.currentPosition();
            return new Node(currentPosition, arrayNode.getRightWire(), arrayNode.getDownWire(), arrayNode.isTouching());
        }
    }
}
