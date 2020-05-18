package LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid;

import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.NodeHandler.Crossing;
import LogicCircuitSimulator.Simulation.Orientation;
import LogicCircuitSimulator.Vector2D;
import LogicCircuitSimulator.Simulation.NodeHandler.Node;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.UnboundGrid.GridIterator;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.UnboundGrid.UnboundGrid;
import LogicCircuitSimulator.Simulation.NodeHandler.WireState;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayNodeGrid implements NodeGrid {
    private final UnboundGrid<ArrayNode> nodes;

    public ArrayNodeGrid(UnboundGrid<ArrayNode> unboundGrid){
        this.nodes = unboundGrid;
    }

    @Override
    public Node getNode(Vector2D pos) {
        ArrayNode arrayNode = nodes.get(pos).orElse(new ArrayNode());
        return new Node(pos, arrayNode.getRightWire(), arrayNode.getDownWire(), arrayNode.isTouching());
    }

    @Override
    public void setNode(Node node) {
        nodes.set(node.getPosition(), ArrayNode.fromNode(node));
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
    public void setCrossing(Vector2D pos, Crossing crossing) {
        Node node = getNode(pos);
        ArrayNode arrayNode = new ArrayNode(node.getRightWire(), node.getDownWire(), crossing);
        nodes.set(pos, arrayNode);
    }

    @Override
    public Crossing getCrossing(Vector2D pos) {
        return getNode(pos).isTouching();
    }

    @Override
    public LogicState getState(Vector2D pos, Orientation orientation) {
        double x = pos.getX();
        double y = pos.getY();

        if(orientation == null) throw new NullPointerException("Argument 'orientation' is null");

        ArrayNode arrayNode = getArrayNode(pos);

        if(arrayNode.isTouching() == Crossing.TOUCHING){
            if(arrayNode.getDownWire() == WireState.HIGH) return LogicState.HIGH;
            if(arrayNode.getRightWire() == WireState.HIGH) return LogicState.HIGH;

            ArrayNode upperArrayNode = getArrayNode(new Vector2D(x, y-1));
            if(upperArrayNode.getDownWire() == WireState.HIGH) return LogicState.HIGH;

            ArrayNode arrayNodeToLeft = getArrayNode(new Vector2D(x-1, y));
            if(arrayNodeToLeft.getRightWire() == WireState.HIGH)
                return LogicState.HIGH; }
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
        GridIterator<ArrayNode> gridIterator = nodes.iterator();

        @Override
        public boolean hasNext() {
            return gridIterator.hasNext();
        }

        @Override
        public Node next() {
            if(!hasNext()) throw new NoSuchElementException("ArrayNodeGrid has no more elements");

            ArrayNode arrayNode = gridIterator.next();
            Vector2D currentPosition = gridIterator.currentPosition();
            return new Node(currentPosition, arrayNode.getRightWire(), arrayNode.getDownWire(), arrayNode.isTouching());
        }
    }

    @Override
    public String toString() {
        //once 'added' element can't be removed so it's possible to calculate it when adding elements
        Iterator<Node> it = this.iterator();
        int lowestX = Integer.MAX_VALUE;
        int highestX = Integer.MIN_VALUE;
        int lowestY = Integer.MAX_VALUE;
        int highestY = Integer.MIN_VALUE;

        while(it.hasNext()){
            Node node = it.next();
            Vector2D pos = node.getPosition();
            if(pos.getX() < lowestX) lowestX = (int)pos.getX();
            if(pos.getY() < lowestY) lowestY = (int)pos.getY();
            if(pos.getX() > highestX) highestX = (int)pos.getX();
            if(pos.getY() > highestY) highestY = (int)pos.getY();
        }
        int width = highestX - lowestX + 1;
        int height = highestY - lowestY + 1;

        ArrayList<ArrayList<ArrayNode>> grid = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            ArrayList<ArrayNode> column = new ArrayList<>();
            for (int j = 0; j < height; j++) {
                column.add(new ArrayNode());
            }
            grid.add(column);
        }

        it = this.iterator();
        while(it.hasNext()){
            Node curr = it.next();
            Vector2D pos = curr.getPosition();
            grid.get((int)pos.getX()-lowestX).set((int)pos.getY()-lowestY, ArrayNode.fromNode(curr));
        }

        StringBuilder stringBuilder = new StringBuilder();
        String lowStateColor = "\u001b[38;5;245m";
        String highStateColor = "\u001b[33;1m";
        String reset = "\u001b[0m";

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(grid.get(j).get(i).isTouching() == Crossing.TOUCHING) stringBuilder.append("*");
                else stringBuilder.append("+");

                if(grid.get(j).get(i).getRightWire() == WireState.HIGH) stringBuilder.append(highStateColor).append("-").append(reset);
                else if(grid.get(j).get(i).getRightWire() == WireState.LOW) stringBuilder.append(lowStateColor).append("-").append(reset);
                else stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
            for (int j = 0; j < width; j++) {
                if(grid.get(j).get(i).getDownWire() == WireState.HIGH) stringBuilder.append(highStateColor).append("| ").append(reset);
                else if(grid.get(j).get(i).getDownWire() == WireState.LOW) stringBuilder.append(lowStateColor).append("| ").append(reset);
                else stringBuilder.append("  ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
