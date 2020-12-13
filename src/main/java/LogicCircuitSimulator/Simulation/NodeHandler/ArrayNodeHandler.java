package LogicCircuitSimulator.Simulation.NodeHandler;

import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.ArrayNode;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.ArrayNodeGrid;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.NodeGrid;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.UnboundGrid.UnboundHashMapGrid;
import LogicCircuitSimulator.Simulation.Orientation;
import LogicCircuitSimulator.Vector2D;

import java.util.*;

/**
 * Stores and processes wire data in the grid
 */
public final class ArrayNodeHandler implements NodeHandler {

    public ArrayNodeHandler(){

    }

    public ArrayNodeHandler(NodeHandler nodeHandler){
        Iterator<Node> iterator = nodeHandler.iterator();
        while(iterator.hasNext()){
            this.nodeGrid.setNode(iterator.next());
        }
    }

    /**
     * Stores node data
     */
    private final NodeGrid nodeGrid = new ArrayNodeGrid(new UnboundHashMapGrid<>());

    @Override
    public LogicState getState(Vector2D pos, Orientation orientation){
        return nodeGrid.getState(pos, orientation);
    }

    @Override
    public Iterator<Node> iterator() {
        return nodeGrid.iterator();
    }

    @Override
    public void setNode(Node node) {
        nodeGrid.setNode(node);
    }

    @Override
    public void removeNode(Vector2D pos) {
        nodeGrid.removeNode(pos);
    }

    @Override
    public void setRightWire(Vector2D pos, WireState state) {
        nodeGrid.setRightWire(pos, state);

        if(isDefaultNode(nodeGrid.getNode(pos))){
            nodeGrid.removeNode(pos);
        }
    }

    @Override
    public void setDownWire(Vector2D pos, WireState state) {
        nodeGrid.setDownWire(pos, state);

        if(isDefaultNode(nodeGrid.getNode(pos))){
            nodeGrid.removeNode(pos);
        }
    }

    @Override
    public void setUpWire(Vector2D pos, WireState state) {
        nodeGrid.setUpWire(pos, state);

        if(isDefaultNode(nodeGrid.getNode(new Vector2D(pos.getX(), pos.getY()-1)))){
            nodeGrid.removeNode(new Vector2D(pos.getX(), pos.getY()-1));
        }
    }

    @Override
    public void setLeftWire(Vector2D pos, WireState state) {
        nodeGrid.setLeftWire(pos, state);

        if(isDefaultNode(nodeGrid.getNode(new Vector2D(pos.getX() - 1, pos.getY())))){
            nodeGrid.removeNode(new Vector2D(pos.getX() - 1, pos.getY()));
        }
    }

    @Override
    public WireState getRightWire(Vector2D pos) {
        return nodeGrid.getRightWire(pos);
    }

    @Override
    public WireState getDownWire(Vector2D pos) {
        return nodeGrid.getDownWire(pos);
    }

    @Override
    public WireState getUpWire(Vector2D pos) {
        return nodeGrid.getUpWire(pos);
    }

    @Override
    public WireState getLeftWire(Vector2D pos) {
        return nodeGrid.getLeftWire(pos);
    }

    @Override
    public Crossing getCrossing(Vector2D pos) {
        return nodeGrid.getCrossing(pos);
    }

    @Override
    public void setCrossing(Vector2D pos, Crossing crossing) {
        nodeGrid.setCrossing(pos, crossing);

        if(isDefaultNode(nodeGrid.getNode(pos))){
            nodeGrid.removeNode(pos);
        }
    }

    public Node getNode(Vector2D pos) {
        return nodeGrid.getNode(pos);
    }

    @Override
    public void propagateGenerators(List<Generator> generators){
        resetWiresToLow();

        Deque<Vector2D> candidatesForSearch = new ArrayDeque<>();

        for(Generator generator : generators){
            candidatesForSearch.add(generator.getPos());
            candidatesForSearch.addAll(getSurroundingCandidates(generator.getPos(), generator.getOrientation()));
            setSurroundingWiresIfExistToHigh(generator.getPos(), generator.getOrientation());
        }

        //propagate signal from the positions on the stack
        while(!candidatesForSearch.isEmpty()){
            Vector2D pos = candidatesForSearch.pop();
            double x = pos.getX();
            double y = pos.getY();

            if(isStateHigh(pos, Orientation.HORIZONTALLY) && rightWireExists(pos)){
                nodeGrid.setRightWire(pos, WireState.HIGH);
                candidatesForSearch.add(new Vector2D(x+1, y));
            }
            if(isStateHigh(pos, Orientation.VERTICALLY) && downWireExists(pos)){
                nodeGrid.setDownWire(pos, WireState.HIGH);
                candidatesForSearch.add(new Vector2D(x, y+1));
            }
            if(isStateHigh(pos, Orientation.HORIZONTALLY) && leftWireExists(pos)){
                nodeGrid.setLeftWire(pos, WireState.HIGH);
                candidatesForSearch.add(new Vector2D(x-1, y));
            }
            if(isStateHigh(pos, Orientation.VERTICALLY) && upWireExists(pos)){
                nodeGrid.setUpWire(pos, WireState.HIGH);
                candidatesForSearch.add(new Vector2D(x, y-1));
            }
        }
    }

    private boolean isDefaultNode(Node node){
        if(node.getRightWire() != WireState.NONE) return false;
        if(node.getDownWire() != WireState.NONE) return false;
        if(node.isTouching() != Crossing.TOUCHING) return false;
        return true;
    }

    private void resetWiresToLow(){
        Iterator<Node> iterator = nodeGrid.iterator();
        while(iterator.hasNext()){
            Node node = iterator.next();
            resetNodeWiresToLow(node.getPosition());
        }
    }

    private void resetNodeWiresToLow(Vector2D pos){
        if(getArrayNode(pos).getRightWire() == WireState.HIGH){
            setRightWire(pos, WireState.LOW);
        }
        if(getArrayNode(pos).getDownWire() == WireState.HIGH){
            setDownWire(pos, WireState.LOW);
        }
    }

    private List<Vector2D> getSurroundingCandidates(Vector2D pos, Orientation orientation){
        List<Vector2D> candidates = new ArrayList<>();
        if (orientation == Orientation.HORIZONTALLY) {
            if (rightWireExists(pos)) candidates.add(new Vector2D(pos.getX() + 1, pos.getY()));
            if (leftWireExists(pos)) candidates.add(new Vector2D(pos.getX() - 1, pos.getY()));
        }
        else {
            if (downWireExists(pos)) candidates.add(new Vector2D(pos.getX(), pos.getY() + 1));
            if (upWireExists(pos)) candidates.add(new Vector2D(pos.getX(), pos.getY() - 1));

        }
        return candidates;
    }

    private void setSurroundingWiresIfExistToHigh(Vector2D pos, Orientation orientation){
        if (orientation == Orientation.HORIZONTALLY) {
            if (rightWireExists(pos)) nodeGrid.setRightWire(pos, WireState.HIGH);
            if (leftWireExists(pos)) nodeGrid.setLeftWire(pos, WireState.HIGH);
        }
        else {
            if (downWireExists(pos)) nodeGrid.setDownWire(pos, WireState.HIGH);
            if (upWireExists(pos)) nodeGrid.setUpWire(pos, WireState.HIGH);
        }
    }

    private boolean rightWireExists(Vector2D pos){
        return getNode(pos).getRightWire() == WireState.LOW;
    }

    private boolean downWireExists(Vector2D pos){
        return getNode(pos).getDownWire() == WireState.LOW;
    }

    private boolean leftWireExists(Vector2D pos){
        return getNode(new Vector2D(pos.getX()-1, pos.getY())).getRightWire() == WireState.LOW;
    }

    private boolean upWireExists(Vector2D pos){
        return getNode(new Vector2D(pos.getX(), pos.getY()-1)).getDownWire() == WireState.LOW;
    }

    private boolean isStateHigh(Vector2D pos, Orientation orientation){
        return getState(pos, orientation) == LogicState.HIGH;
    }

    private ArrayNode getArrayNode(Vector2D pos){
        return ArrayNode.fromNode(nodeGrid.getNode(pos));
    }

    @Override
    public String toString() {
        return nodeGrid.toString();
    }

}
