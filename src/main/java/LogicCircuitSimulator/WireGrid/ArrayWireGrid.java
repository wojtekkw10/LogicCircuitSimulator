package LogicCircuitSimulator.WireGrid;

import LogicCircuitSimulator.LogicState;
import LogicCircuitSimulator.Orientation;
import LogicCircuitSimulator.Vector2D;
import LogicCircuitSimulator.WireGrid.Unbound2DList.Unbound2DList;
import LogicCircuitSimulator.WireGrid.Unbound2DList.Unbound2DListHashMap;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Stores and processes wire data in the grid
 */
public final class ArrayWireGrid implements WireGrid{
    /**
     * Stores node data
     */
    Unbound2DList<ArrayNode> nodes = new Unbound2DListHashMap<>();

    @Override
    public void setNode(Node node){
        nodes.set(node.getPosition(), ArrayNode.fromNode(node));
    }

    @Override
    public void resetWiresToLow(){
        Iterator2D<ArrayNode> iterator = nodes.iterator();
        while(iterator.hasNext()){
            ArrayNode current = iterator.next();
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
                    ArrayNode arrayNode = getArrayNode(pos);
                    ArrayNode newArrayNode = new ArrayNode(Node.State.HIGH, arrayNode.getDownWire(), arrayNode.isTouching());
                    nodes.set(pos, newArrayNode);

                    stack.add(new Vector2D(pos.getX()+1, pos.getY()));
                }
                if(pos.getX() - 1 >= 0
                        && getNode(new Vector2D(pos.getX()-1, pos.getY())).getRightWire() == Node.State.LOW){
                    ArrayNode arrayNode = getArrayNode(new Vector2D(pos.getX()-1, pos.getY()));
                    ArrayNode newArrayNode = new ArrayNode(Node.State.HIGH, arrayNode.getDownWire(), arrayNode.isTouching());
                    nodes.set(new Vector2D(pos.getX()-1, pos.getY()), newArrayNode);

                    stack.add(new Vector2D(pos.getX()-1, pos.getY()));
                }
            }
            else{
                if(getNode(pos).getDownWire() == Node.State.LOW){
                    ArrayNode arrayNode = getArrayNode(pos);
                    ArrayNode newArrayNode = new ArrayNode(arrayNode.getRightWire(), Node.State.HIGH, arrayNode.isTouching());
                    nodes.set(pos, newArrayNode);

                    stack.add(new Vector2D(pos.getX(), pos.getY()+1));
                }
                if(pos.getY() - 1 >= 0 &&
                        getNode(new Vector2D(pos.getX(), pos.getY()-1)).getDownWire() == Node.State.LOW){
                    ArrayNode arrayNode = getArrayNode(new Vector2D(pos.getX(), pos.getY()-1));
                    ArrayNode newArrayNode = new ArrayNode(arrayNode.getRightWire(), Node.State.HIGH, arrayNode.isTouching());
                    nodes.set(new Vector2D(pos.getX(), pos.getY()-1), newArrayNode);

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
                ArrayNode arrayNode = getArrayNode(new Vector2D(pos.getX(), pos.getY()));
                ArrayNode newArrayNode = new ArrayNode(Node.State.HIGH, arrayNode.getDownWire(), arrayNode.isTouching());
                nodes.set(new Vector2D(pos.getX(), pos.getY()), newArrayNode);

                stack.add(new Vector2D(x+1, y));
            }
            if(getState(new Vector2D(x,y), Orientation.VERTICALLY) == LogicState.HIGH
                    && getNode(new Vector2D(x, y)).getDownWire() == Node.State.LOW){
                ArrayNode arrayNode = getArrayNode(new Vector2D(pos.getX(), pos.getY()));
                ArrayNode newArrayNode = new ArrayNode(arrayNode.getRightWire(), Node.State.HIGH, arrayNode.isTouching());
                nodes.set(new Vector2D(pos.getX(), pos.getY()), newArrayNode);

                stack.add(new Vector2D(x, y+1));
            }
            if(getState(new Vector2D(x,y), Orientation.HORIZONTALLY) == LogicState.HIGH
                    && getNode(new Vector2D(x-1, y)).getRightWire() == Node.State.LOW){
                ArrayNode arrayNode = getArrayNode(new Vector2D(pos.getX()-1, pos.getY()));
                ArrayNode newArrayNode = new ArrayNode(Node.State.HIGH, arrayNode.getDownWire() , arrayNode.isTouching());
                nodes.set(new Vector2D(pos.getX()-1, pos.getY()), newArrayNode);

                stack.add(new Vector2D(x-1, y));
            }
            if(getState(new Vector2D(x,y), Orientation.VERTICALLY) == LogicState.HIGH
                    && getNode(new Vector2D(x, y-1)).getDownWire() == Node.State.LOW){
                ArrayNode arrayNode = getArrayNode(new Vector2D(pos.getX(), pos.getY()-1));
                ArrayNode newArrayNode = new ArrayNode(arrayNode.getRightWire(), Node.State.HIGH, arrayNode.isTouching());
                nodes.set(new Vector2D(pos.getX(), pos.getY()-1), newArrayNode);

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

        ArrayNode arrayNode = getArrayNode(pos);

        if(arrayNode.isTouching() == Node.WireCrossing.TOUCHING){
            if(arrayNode.getDownWire() == Node.State.HIGH) return LogicState.HIGH;
            if(arrayNode.getRightWire() == Node.State.HIGH) return LogicState.HIGH;
            if(y-1>=0){
                ArrayNode upperArrayNode = getArrayNode(new Vector2D(x, y-1));
                if(upperArrayNode.getDownWire() == Node.State.HIGH) return LogicState.HIGH;
            }
            if(x-1>=0){
                ArrayNode arrayNodeToLeft = getArrayNode(new Vector2D(x-1, y));
                if(arrayNodeToLeft.getRightWire() == Node.State.HIGH)
                    return LogicState.HIGH; }
        }
        else{
            if(orientation == Orientation.VERTICALLY){
                if(arrayNode.getDownWire() == Node.State.HIGH) return LogicState.HIGH;
                if(y-1>=0){
                    ArrayNode upperArrayNode = getArrayNode(new Vector2D(x, y-1));
                    if(upperArrayNode.getDownWire() == Node.State.HIGH) return LogicState.HIGH;
                }
            }
            else{
                if(arrayNode.getRightWire() == Node.State.HIGH) return LogicState.HIGH;
                if(x-1>=0){
                    ArrayNode arrayNodeToLeft = getArrayNode(new Vector2D(x-1, y));
                    if(arrayNodeToLeft.getRightWire() == Node.State.HIGH) return LogicState.HIGH;
                }
            }
        }
        return LogicState.LOW;
    }

    @Override
    public Node getNode(Vector2D pos) {
        ArrayNode arrayNode = getArrayNode(pos);
        return new Node(pos, arrayNode.getRightWire(), arrayNode.getDownWire(), arrayNode.isTouching());
    }

    @Override
    public Iterator<Node> iterator() {
        return new MainIterator();
    }

    @Override
    public void updateWire(Vector2D pos, Orientation orientation, Node.State state) {
        ArrayNode arrayNode = getArrayNode(pos);
        if(orientation == Orientation.HORIZONTALLY){
            ArrayNode newArrayNode = new ArrayNode(state, arrayNode.getDownWire(), arrayNode.isTouching());
            nodes.set(pos, newArrayNode);
        }
        else {
            ArrayNode newArrayNode = new ArrayNode(arrayNode.getRightWire(), state, arrayNode.isTouching());
            nodes.set(pos, newArrayNode);
        }
    }

    @Override
    public void updateCrossing(Vector2D pos, Node.WireCrossing crossing) {
        ArrayNode arrayNode = getArrayNode(pos);
        ArrayNode newArrayNode = new ArrayNode(arrayNode.getRightWire(), arrayNode.getDownWire(), crossing);
        nodes.set(pos, newArrayNode);
    }

    private ArrayNode getArrayNode(Vector2D pos){
        return nodes.get(pos).orElse(new ArrayNode(Node.State.NONE, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
    }

    @Override
    public String toString() {
        Iterator2D<ArrayNode> it = nodes.iterator();
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

        ArrayList<ArrayList<ArrayNode>> grid = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            ArrayList<ArrayNode> column = new ArrayList<>();
            for (int j = 0; j < height; j++) {
                column.add(new ArrayNode());
            }
            grid.add(column);
        }

        it = nodes.iterator();
        while(it.hasNext()){
            ArrayNode curr = it.next();
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

    private class MainIterator implements Iterator<Node>{
        Iterator2D<ArrayNode> iterator2D = nodes.iterator();

        @Override
        public boolean hasNext() {
            return iterator2D.hasNext();
        }

        @Override
        public Node next() {
            if(!hasNext()) throw new NoSuchElementException("Unbound list has no more elements");

            ArrayNode arrayNode = iterator2D.next();
            Vector2D currentPosition = iterator2D.currentPosition();
            return new Node(currentPosition, arrayNode.getRightWire(), arrayNode.getDownWire(), arrayNode.isTouching());
        }
    }
}
