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
    Unbound2DList<ArrayNode> nodes = new Unbound2DListHashMap<>();

    @Override
    public void setNode(Node node){
        nodes.set(node.getPosition(), ArrayNode.fromNode(node));
    }

    private void resetWiresToLow(){
        Iterator2D<ArrayNode> iterator = nodes.iterator();
        while(iterator.hasNext()){
            iterator.next();
            resetNodeWiresToLow(iterator.currentPosition());
        }
    }

    private void resetNodeWiresToLow(Vector2D pos){
        if(getArrayNode(pos).getRightWire() == Node.State.HIGH){
            updateWire(pos, Orientation.HORIZONTALLY, Node.State.LOW);
        }
        if(getArrayNode(pos).getDownWire() == Node.State.HIGH){
            updateWire(pos, Orientation.VERTICALLY, Node.State.LOW);
        }
    }

    @Override
    public void propagateGenerators(List<Generator> generators){
        resetWiresToLow();

        Deque<Vector2D> candidatesForSearch = new ArrayDeque<>();

        //Add to stack positions at which are generators
        for (Generator generator : generators) {
            candidatesForSearch.add(generator.getPos());
        }

        //Add to stack positions next to generators depending on their orientation
        for (Generator generator : generators) {
            Vector2D generatorPos = generator.getPos();

            if (generator.getOrientation() == Orientation.HORIZONTALLY) {
                if (rightWireExists(generatorPos)) {
                    updateRightWireToHigh(generatorPos);
                    candidatesForSearch.add(new Vector2D(generatorPos.getX() + 1, generatorPos.getY()));
                }
                if (leftWireExists(generatorPos)) {
                    updateLeftWireToHigh(generatorPos);
                    candidatesForSearch.add(new Vector2D(generatorPos.getX() - 1, generatorPos.getY()));
                }
            } else {
                if (downWireExists(generatorPos)) {
                    updateDownWireToHigh(generatorPos);
                    candidatesForSearch.add(new Vector2D(generatorPos.getX(), generatorPos.getY() + 1));
                }
                if (upWireExists(generatorPos)) {
                    updateUpWireToHigh(generatorPos);
                    candidatesForSearch.add(new Vector2D(generatorPos.getX(), generatorPos.getY() - 1));
                }
            }
        }

        //propagate signal from the positions on the stack
        while(!candidatesForSearch.isEmpty()){
            Vector2D pos = candidatesForSearch.pop();
            double x = pos.getX();
            double y = pos.getY();

            if(isStateHigh(pos, Orientation.HORIZONTALLY) && rightWireExists(pos)){
                updateRightWireToHigh(pos);
                candidatesForSearch.add(new Vector2D(x+1, y));
            }
            if(isStateHigh(pos, Orientation.VERTICALLY) && downWireExists(pos)){
                updateDownWireToHigh(pos);
                candidatesForSearch.add(new Vector2D(x, y+1));
            }
            if(isStateHigh(pos, Orientation.HORIZONTALLY) && leftWireExists(pos)){
                updateLeftWireToHigh(pos);
                candidatesForSearch.add(new Vector2D(x-1, y));
            }
            if(isStateHigh(pos, Orientation.VERTICALLY) && upWireExists(pos)){
                updateUpWireToHigh(pos);
                candidatesForSearch.add(new Vector2D(x, y-1));
            }
        }
    }

    boolean rightWireExists(Vector2D pos){
        return getNode(pos).getRightWire() == Node.State.LOW;
    }

    boolean downWireExists(Vector2D pos){
        return getNode(pos).getDownWire() == Node.State.LOW;
    }

    boolean leftWireExists(Vector2D pos){
        return getNode(new Vector2D(pos.getX()-1, pos.getY())).getRightWire() == Node.State.LOW;
    }

    boolean upWireExists(Vector2D pos){
        return getNode(new Vector2D(pos.getX(), pos.getY()-1)).getDownWire() == Node.State.LOW;
    }

    boolean isStateHigh(Vector2D pos, Orientation orientation){
        return getState(pos, orientation) == LogicState.HIGH;
    }

    private void updateRightWireToHigh(Vector2D pos){
        ArrayNode arrayNode = getArrayNode(pos);
        ArrayNode newArrayNode = new ArrayNode(Node.State.HIGH, arrayNode.getDownWire(), arrayNode.isTouching());
        nodes.set(pos, newArrayNode);
    }

    private void updateDownWireToHigh(Vector2D pos){
        ArrayNode arrayNode = getArrayNode(pos);
        ArrayNode newArrayNode = new ArrayNode(arrayNode.getRightWire(), Node.State.HIGH, arrayNode.isTouching());
        nodes.set(pos, newArrayNode);
    }

    private void updateUpWireToHigh(Vector2D pos){
        ArrayNode arrayNode = getArrayNode(new Vector2D(pos.getX(), pos.getY()-1));
        ArrayNode newArrayNode = new ArrayNode(arrayNode.getRightWire(), Node.State.HIGH, arrayNode.isTouching());
        nodes.set(new Vector2D(pos.getX(), pos.getY()-1), newArrayNode);
    }

    private void updateLeftWireToHigh(Vector2D pos){
        ArrayNode arrayNode = getArrayNode(new Vector2D(pos.getX()-1, pos.getY()));
        ArrayNode newArrayNode = new ArrayNode(Node.State.HIGH, arrayNode.getDownWire(), arrayNode.isTouching());
        nodes.set(new Vector2D(pos.getX()-1, pos.getY()), newArrayNode);
    }

    @Override
    public LogicState getState(Vector2D pos, Orientation orientation){
        double x = pos.getX();
        double y = pos.getY();

        if(orientation == null) throw new NullPointerException("Argument 'orientation' is null");

        ArrayNode arrayNode = getArrayNode(pos);

        if(arrayNode.isTouching() == Node.WireCrossing.TOUCHING){
            if(arrayNode.getDownWire() == Node.State.HIGH) return LogicState.HIGH;
            if(arrayNode.getRightWire() == Node.State.HIGH) return LogicState.HIGH;

            ArrayNode upperArrayNode = getArrayNode(new Vector2D(x, y-1));
            if(upperArrayNode.getDownWire() == Node.State.HIGH) return LogicState.HIGH;

            ArrayNode arrayNodeToLeft = getArrayNode(new Vector2D(x-1, y));
            if(arrayNodeToLeft.getRightWire() == Node.State.HIGH)
                    return LogicState.HIGH;
        }
        else{
            if(orientation == Orientation.VERTICALLY){
                if(arrayNode.getDownWire() == Node.State.HIGH) return LogicState.HIGH;

                ArrayNode upperArrayNode = getArrayNode(new Vector2D(x, y-1));
                if(upperArrayNode.getDownWire() == Node.State.HIGH) return LogicState.HIGH;
            }
            else{
                if(arrayNode.getRightWire() == Node.State.HIGH) return LogicState.HIGH;

                ArrayNode arrayNodeToLeft = getArrayNode(new Vector2D(x-1, y));
                if(arrayNodeToLeft.getRightWire() == Node.State.HIGH) return LogicState.HIGH;
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
        ArrayNode newArrayNode;
        if(orientation == Orientation.HORIZONTALLY){
            newArrayNode = new ArrayNode(state, arrayNode.getDownWire(), arrayNode.isTouching());
        }
        else {
            newArrayNode = new ArrayNode(arrayNode.getRightWire(), state, arrayNode.isTouching());
        }
        nodes.set(pos, newArrayNode);
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
