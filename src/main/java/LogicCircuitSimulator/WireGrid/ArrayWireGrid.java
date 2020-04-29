package LogicCircuitSimulator.WireGrid;

import LogicCircuitSimulator.LogicState;
import LogicCircuitSimulator.Orientation;
import LogicCircuitSimulator.Vector2D;
import LogicCircuitSimulator.WireGrid.Unbound2DList.Unbound2DList;
import LogicCircuitSimulator.WireGrid.Unbound2DList.Unbound2DListHashMap;
import java.util.*;

/**
 * Stores and processes wire data in the grid
 */
public final class ArrayWireGrid implements WireGrid{
    /**
     * Stores node data
     */
    Unbound2DList<Node> nodes = new Unbound2DListHashMap<>();

    @Override
    public void setNode(Vector2D pos, Node node){
        nodes.set(pos, node);
    }

    @Override
    public void resetWiresToLow(){
        Iterator2D<Node> iterator = nodes.iterator();
        while(iterator.hasNext()){
            Node current = iterator.next();
            if(current.getRightWire() == Node.State.HIGH){
                updateWire(iterator.currentPosition(), Orientation.HORIZONTALLY, Node.State.LOW);
            }
            if(current.getDownWire() == Node.State.HIGH){
                updateWire(iterator.currentPosition(), Orientation.VERTICALLY, Node.State.LOW);
            }
        }
    }

    @Override
    public void propagateGenerators(List<Generator> generators){
        Deque<Vector2D> stack = new ArrayDeque<>();

        //Add to stack positions at which are generators
        for (int i = 0; i < generators.size(); i++) {
            Generator generator = generators.get(i);
            if(generator.getPos().getX() < 0) throw new IllegalArgumentException("Illegal argument 'generators'. Generator at index "+i+
                    " is at position: ("+generator.getPos().getX()+","+generator.getPos().getY()+")");
            if(generator.getPos().getY() < 0) throw new IllegalArgumentException("Illegal argument 'generators'. Generator at index "+i+
                    " is at position: ("+generator.getPos().getX()+","+generator.getPos().getY()+")");

            stack.add(generator.getPos());
        }

        //Add to stack positions next to generators depending on their orientation
        for (int i = 0; i < generators.size(); i++) {
            Generator generator = generators.get(i);
            Vector2D pos = generator.getPos();


            if(generator.getOrientation() == Orientation.HORIZONTALLY){
                if(getNode(pos).getRightWire() == Node.State.LOW){
                    Node node = getNode(pos);
                    Node newNode = new Node(Node.State.HIGH, node.getDownWire(), node.isTouching());
                    nodes.set(pos, newNode);

                    stack.add(new Vector2D(pos.getX()+1, pos.getY()));
                }
                if(pos.getX() - 1 >= 0
                        && getNode(new Vector2D(pos.getX()-1, pos.getY())).getRightWire() == Node.State.LOW){
                    Node node = getNode(new Vector2D(pos.getX()-1, pos.getY()));
                    Node newNode = new Node(Node.State.HIGH, node.getDownWire(), node.isTouching());
                    nodes.set(new Vector2D(pos.getX()-1, pos.getY()), newNode);

                    stack.add(new Vector2D(pos.getX()-1, pos.getY()));
                }
            }
            else{
                if(getNode(pos).getDownWire() == Node.State.LOW){
                    Node node = getNode(pos);
                    Node newNode = new Node(node.getRightWire(), Node.State.HIGH, node.isTouching());
                    nodes.set(pos, newNode);

                    stack.add(new Vector2D(pos.getX(), pos.getY()+1));
                }
                if(pos.getY() - 1 >= 0 &&
                        getNode(new Vector2D(pos.getX(), pos.getY()-1)).getDownWire() == Node.State.LOW){
                    Node node = getNode(new Vector2D(pos.getX(), pos.getY()-1));
                    Node newNode = new Node(node.getRightWire(), Node.State.HIGH, node.isTouching());
                    nodes.set(new Vector2D(pos.getX(), pos.getY()-1), newNode);

                    stack.add(new Vector2D(pos.getX(), pos.getY()-1));
                }
            }
        }

        //propagate signal from the positions on the stack
        while(!stack.isEmpty()){
            Vector2D pos = stack.pop();
            double x = pos.getX();
            double y = pos.getY();

            if(getState(new Vector2D(x,y), Orientation.HORIZONTALLY) == LogicState.HIGH
                    && getNode(new Vector2D(x, y)).getRightWire() == Node.State.LOW){
                Node node = getNode(new Vector2D(pos.getX(), pos.getY()));
                Node newNode = new Node(Node.State.HIGH, node.getDownWire(), node.isTouching());
                nodes.set(new Vector2D(pos.getX(), pos.getY()), newNode);

                stack.add(new Vector2D(x+1, y));
            }
            if(getState(new Vector2D(x,y), Orientation.VERTICALLY) == LogicState.HIGH
                    && getNode(new Vector2D(x, y)).getDownWire() == Node.State.LOW){
                Node node = getNode(new Vector2D(pos.getX(), pos.getY()));
                Node newNode = new Node(node.getRightWire(), Node.State.HIGH, node.isTouching());
                nodes.set(new Vector2D(pos.getX(), pos.getY()), newNode);

                stack.add(new Vector2D(x, y+1));
            }
            if(getState(new Vector2D(x,y), Orientation.HORIZONTALLY) == LogicState.HIGH
                    && getNode(new Vector2D(x-1, y)).getRightWire() == Node.State.LOW){
                Node node = getNode(new Vector2D(pos.getX()-1, pos.getY()));
                Node newNode = new Node(Node.State.HIGH, node.getDownWire() , node.isTouching());
                nodes.set(new Vector2D(pos.getX()-1, pos.getY()), newNode);

                stack.add(new Vector2D(x-1, y));
            }
            if(getState(new Vector2D(x,y), Orientation.VERTICALLY) == LogicState.HIGH
                    && getNode(new Vector2D(x, y-1)).getDownWire() == Node.State.LOW){
                Node node = getNode(new Vector2D(pos.getX(), pos.getY()-1));
                Node newNode = new Node(node.getRightWire(), Node.State.HIGH, node.isTouching());
                nodes.set(new Vector2D(pos.getX(), pos.getY()-1), newNode);

                stack.add(new Vector2D(x, y-1));
            }
        }
    }

    @Override
    public LogicState getState(Vector2D pos, Orientation orientation){
        double x = pos.getX();
        double y = pos.getY();

        if(x < 0) throw new IllegalArgumentException("Illegal x: "+x+" for x >= 0");
        if(y < 0) throw new IllegalArgumentException("Illegal y: "+x+" for y >= 0");
        if(orientation == null) throw new NullPointerException("Argument 'orientation' is null");

        Node node = getNode(pos);

        if(node.isTouching() == Node.WireCrossing.TOUCHING){
            if(node.getDownWire() == Node.State.HIGH) return LogicState.HIGH;
            if(node.getRightWire() == Node.State.HIGH) return LogicState.HIGH;
            if(y-1>=0){
                Node upperNode = getNode(new Vector2D(x, y-1));
                if(upperNode.getDownWire() == Node.State.HIGH) return LogicState.HIGH;
            }
            if(x-1>=0){
                Node nodeToLeft = getNode(new Vector2D(x-1, y));
                if(nodeToLeft.getRightWire() == Node.State.HIGH)
                    return LogicState.HIGH; }
        }
        else{
            if(orientation == Orientation.VERTICALLY){
                if(node.getDownWire() == Node.State.HIGH) return LogicState.HIGH;
                if(y-1>=0){
                    Node upperNode = getNode(new Vector2D(x, y-1));
                    if(upperNode.getDownWire() == Node.State.HIGH) return LogicState.HIGH;
                }
            }
            else{
                if(node.getRightWire() == Node.State.HIGH) return LogicState.HIGH;
                if(x-1>=0){
                    Node nodeToLeft = getNode(new Vector2D(x-1, y));
                    if(nodeToLeft.getRightWire() == Node.State.HIGH) return LogicState.HIGH;
                }
            }
        }
        return LogicState.LOW;
    }

    @Override
    public Node getNode(Vector2D pos) {
        return nodes.get(pos).orElse(new Node(Node.State.NONE, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
    }

    @Override
    public Iterator2D<Node> getIterator() {
        return nodes.iterator();
    }

    @Override
    public void updateWire(Vector2D pos, Orientation orientation, Node.State state) {
        Node node = getNode(pos);
        if(orientation == Orientation.HORIZONTALLY){
            Node newNode = new Node(state, node.getDownWire(), node.isTouching());
            nodes.set(pos, newNode);
        }
        else {
            Node newNode = new Node(node.getRightWire(), state, node.isTouching());
            nodes.set(pos, newNode);
        }
    }

    @Override
    public void updateCrossing(Vector2D pos, Node.WireCrossing crossing) {
        Node node = getNode(pos);
        Node newNode = new Node(node.getRightWire(), node.getDownWire(), crossing);
        nodes.set(pos, newNode);
    }

    @Override
    public String toString() {
        Iterator2D<Node> it = nodes.iterator();
        int lowestX = Integer.MAX_VALUE;
        int highestX = Integer.MIN_VALUE;
        int lowestY = Integer.MAX_VALUE;
        int highestY = Integer.MIN_VALUE;

        while(it.hasNext()){
            it.next();
            Vector2D pos = it.currentPosition();
            if(pos.getX() < lowestX) lowestX = (int)pos.getX();
            if(pos.getY() < lowestY) lowestY = (int)pos.getY();
            if(pos.getX() > highestX) highestX = (int)pos.getX();
            if(pos.getY() > highestY) highestY = (int)pos.getY();
        }
        int width = highestX - lowestX + 1;
        int height = highestY - lowestY + 1;

        ArrayList<ArrayList<Node>> grid = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            ArrayList<Node> column = new ArrayList<>();
            for (int j = 0; j < height; j++) {
                column.add(new Node());
            }
            grid.add(column);
        }

        it = nodes.iterator();
        while(it.hasNext()){
            Node curr = it.next();
            Vector2D pos = it.currentPosition();
            grid.get((int)pos.getX()-lowestX).set((int)pos.getY()-lowestY, curr);
        }

        StringBuilder stringBuilder = new StringBuilder();
        String lowStateColor = "\u001b[38;5;245m";
        String highStateColor = "\u001b[33;1m";
        String reset = "\u001b[0m";

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(grid.get(j).get(i).isTouching() == Node.WireCrossing.TOUCHING) stringBuilder.append("*");
                else stringBuilder.append("+");

                if(grid.get(j).get(i).getRightWire() == Node.State.HIGH) stringBuilder.append(highStateColor).append("-").append(reset);
                else if(grid.get(j).get(i).getRightWire() == Node.State.LOW) stringBuilder.append(lowStateColor).append("-").append(reset);
                else stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
            for (int j = 0; j < width; j++) {
                if(grid.get(j).get(i).getDownWire() == Node.State.HIGH) stringBuilder.append(highStateColor).append("| ").append(reset);
                else if(grid.get(j).get(i).getDownWire() == Node.State.LOW) stringBuilder.append(lowStateColor).append("| ").append(reset);
                else stringBuilder.append("  ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
