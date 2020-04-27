package InternalModel.WireGrid;

import InternalModel.LogicState;
import InternalModel.Orientation;
import InternalModel.Vector2D;

import java.util.*;

/**
 * Stores and processes wire data in the grid
 */
public final class ArrayWireGrid implements WireGrid{
    /**
     * Stores node data
     */
    UpUnbound2DList<Node> nodes;

    /**
     * Creates a wireGrid of given width and height
     * @param initialWidth initial width of the grid
     * @param initialHeight initial height of the grid
     * @throws IllegalArgumentException if specified width or height is less than 1
     */
    public ArrayWireGrid(int initialWidth, int initialHeight){
        if(initialWidth < 1) throw new IllegalArgumentException("Illegal initialWidth: "+initialWidth+" for initialWidth > 0");
        if(initialHeight < 1) throw new IllegalArgumentException("Illegal initialHeight: "+initialHeight+" for initialHeight > 0");

        nodes = new UpUnbound2DArrayList<>(Node::new, initialWidth, initialHeight);
    }

    @Override
    public void setNode(Vector2D pos, Node node){
        nodes.set(pos, node);
    }

    @Override
    public void resetWiresToLow(){
        for (int i = 0; i < nodes.getWidth(); i++) {
            for (int j = 0; j < nodes.getHeight(); j++) {
                if(nodes.get(new Vector2D(i, j)).getRightWire() != Node.State.NONE) {
                    Node node = nodes.get(new Vector2D(i, j));
                    Node newNode = new Node(Node.State.LOW, node.getDownWire(), node.isTouching());
                    nodes.set(new Vector2D(i, j), newNode);
                }
                if(nodes.get(new Vector2D(i, j)).getDownWire() != Node.State.NONE) {
                    Node node = nodes.get(new Vector2D(i, j));
                    Node newNode = new Node(node.getRightWire(), Node.State.LOW, node.isTouching());
                    nodes.set(new Vector2D(i, j), newNode);
                }
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

            //Generators out of wires' sight won't do anything
            if(generator.getPos().getX() > nodes.getWidth()) continue;
            if(generator.getPos().getY() > nodes.getHeight()) continue;

            stack.add(generator.getPos());
        }

        //Add to stack positions next to generators depending on their orientation
        for (int i = 0; i < generators.size(); i++) {
            Generator generator = generators.get(i);
            Vector2D pos = generator.getPos();

            //Generators out of wires' sight won't do anything
            if(generator.getPos().getX() > nodes.getWidth()) continue;
            if(generator.getPos().getY() > nodes.getHeight()) continue;

            if(generator.getOrientation() == Orientation.HORIZONTALLY){
                if(nodes.get(pos).getRightWire() == Node.State.LOW){
                    Node node = nodes.get(pos);
                    Node newNode = new Node(Node.State.HIGH, node.getDownWire(), node.isTouching());
                    nodes.set(pos, newNode);

                    stack.add(new Vector2D(pos.getX()+1, pos.getY()));
                }
                if(pos.getX() - 1 >= 0
                        && nodes.get(new Vector2D(pos.getX()-1, pos.getY())).getRightWire() == Node.State.LOW){
                    Node node = nodes.get(new Vector2D(pos.getX()-1, pos.getY()));
                    Node newNode = new Node(Node.State.HIGH, node.getDownWire(), node.isTouching());
                    nodes.set(new Vector2D(pos.getX()-1, pos.getY()), newNode);

                    stack.add(new Vector2D(pos.getX()-1, pos.getY()));
                }
            }
            else{
                if(nodes.get(pos).getDownWire() == Node.State.LOW){
                    Node node = nodes.get(pos);
                    Node newNode = new Node(node.getRightWire(), Node.State.HIGH, node.isTouching());
                    nodes.set(pos, newNode);

                    stack.add(new Vector2D(pos.getX(), pos.getY()+1));
                }
                if(pos.getY() - 1 >= 0 &&
                        nodes.get(new Vector2D(pos.getX(), pos.getY()-1)).getDownWire() == Node.State.LOW){
                    Node node = nodes.get(new Vector2D(pos.getX(), pos.getY()-1));
                    Node newNode = new Node(node.getRightWire(), Node.State.HIGH, node.isTouching());
                    nodes.set(new Vector2D(pos.getX(), pos.getY()-1), newNode);

                    stack.add(new Vector2D(pos.getX(), pos.getY()-1));
                }
            }
        }

        //propagate signal from the positions on the stack
        while(!stack.isEmpty()){
            Vector2D pos = stack.pop();
            int x = pos.getX();
            int y = pos.getY();

            if(x+1 <= nodes.getWidth()
                    && getState(new Vector2D(x,y), Orientation.HORIZONTALLY) == LogicState.HIGH
                    && nodes.get(new Vector2D(x, y)).getRightWire() == Node.State.LOW){
                Node node = nodes.get(new Vector2D(pos.getX(), pos.getY()));
                Node newNode = new Node(Node.State.HIGH, node.getDownWire(), node.isTouching());
                nodes.set(new Vector2D(pos.getX(), pos.getY()), newNode);

                stack.add(new Vector2D(x+1, y));
            }
            if(y+1 <= nodes.getHeight()
                    && getState(new Vector2D(x,y), Orientation.VERTICALLY) == LogicState.HIGH
                    && nodes.get(new Vector2D(x, y)).getDownWire() == Node.State.LOW){
                Node node = nodes.get(new Vector2D(pos.getX(), pos.getY()));
                Node newNode = new Node(node.getRightWire(), Node.State.HIGH, node.isTouching());
                nodes.set(new Vector2D(pos.getX(), pos.getY()), newNode);

                stack.add(new Vector2D(x, y+1));
            }
            if(x-1 >= 0
                    && getState(new Vector2D(x,y), Orientation.HORIZONTALLY) == LogicState.HIGH
                    && nodes.get(new Vector2D(x-1, y)).getRightWire() == Node.State.LOW){
                Node node = nodes.get(new Vector2D(pos.getX()-1, pos.getY()));
                Node newNode = new Node(Node.State.HIGH, node.getDownWire() , node.isTouching());
                nodes.set(new Vector2D(pos.getX()-1, pos.getY()), newNode);

                stack.add(new Vector2D(x-1, y));
            }
            if(y-1 >= 0
                    && getState(new Vector2D(x,y), Orientation.VERTICALLY) == LogicState.HIGH
                    && nodes.get(new Vector2D(x, y-1)).getDownWire() == Node.State.LOW){
                Node node = nodes.get(new Vector2D(pos.getX(), pos.getY()-1));
                Node newNode = new Node(node.getRightWire(), Node.State.HIGH, node.isTouching());
                nodes.set(new Vector2D(pos.getX(), pos.getY()-1), newNode);

                stack.add(new Vector2D(x, y-1));
            }
        }
    }

    @Override
    public LogicState getState(Vector2D pos, Orientation orientation){
        int x = pos.getX();
        int y = pos.getY();

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
        return nodes.get(pos);
    }

    @Override
    public Iterator2D<Node> getIterator() {
        return new MainIterator();
    }

    @Override
    public void updateWire(Vector2D pos, Orientation orientation, Node.State state) {
        Node node = nodes.get(pos);
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
        Node node = nodes.get(pos);
        Node newNode = new Node(node.getRightWire(), node.getDownWire(), crossing);
        nodes.set(pos, newNode);
    }

    public int getWidth() {
        return nodes.getWidth();
    }

    public int getHeight() {
        return nodes.getHeight();
    }

    private class MainIterator implements Iterator2D<Node>{
        int currentX = -1;
        int currentY = 0;

        @Override
        public boolean hasNext() {
            if(currentX < nodes.getWidth() - 1) return true;
            else{
                if(currentY < nodes.getHeight() - 1) return true;
                else return false;
            }
        }

        @Override
        public Node next() {
            if(!hasNext()) throw new NoSuchElementException("WireGrid has no more elements");

            if(currentX < nodes.getWidth() - 1) currentX++;
            else{
                currentY++;
                currentX = 0;
            }
            return nodes.get(new Vector2D(currentX, currentY));
        }

        @Override
        public Vector2D currentPosition() {
            return new Vector2D(currentX, currentY);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        String lowStateColor = "\u001b[38;5;245m";
        String highStateColor = "\u001b[33;1m";
        String reset = "\u001b[0m";

        for (int i = 0; i < nodes.getHeight(); i++) {
            for (int j = 0; j < nodes.getWidth(); j++) {
                if(nodes.get(new Vector2D(j, i)).isTouching() == Node.WireCrossing.TOUCHING) stringBuilder.append("*");
                else stringBuilder.append("+");

                if(nodes.get(new Vector2D(j, i)).getRightWire() == Node.State.HIGH) stringBuilder.append(highStateColor).append("-").append(reset);
                else if(nodes.get(new Vector2D(j, i)).getRightWire() == Node.State.LOW) stringBuilder.append(lowStateColor).append("-").append(reset);
                else stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
            for (int j = 0; j < nodes.getWidth(); j++) {
                if(nodes.get(new Vector2D(j, i)).getDownWire() == Node.State.HIGH) stringBuilder.append(highStateColor).append("| ").append(reset);
                else if(nodes.get(new Vector2D(j, i)).getDownWire() == Node.State.LOW) stringBuilder.append(lowStateColor).append("| ").append(reset);
                else stringBuilder.append("  ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
