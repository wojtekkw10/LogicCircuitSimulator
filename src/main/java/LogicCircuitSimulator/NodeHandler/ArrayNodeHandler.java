package LogicCircuitSimulator.NodeHandler;

import LogicCircuitSimulator.LogicState;
import LogicCircuitSimulator.NodeHandler.NodeGrid.ArrayNode;
import LogicCircuitSimulator.NodeHandler.NodeGrid.ArrayNodeGrid;
import LogicCircuitSimulator.Orientation;
import LogicCircuitSimulator.Vector2D;
import LogicCircuitSimulator.NodeHandler.NodeGrid.NodeGrid;
import LogicCircuitSimulator.NodeHandler.NodeGrid.Unbound2DList.UnboundHashMapGrid;

import java.util.*;

/**
 * Stores and processes wire data in the grid
 */
public final class ArrayNodeHandler implements NodeHandler {
    /**
     * Stores node data
     */
    NodeGrid nodeGrid = new ArrayNodeGrid(new UnboundHashMapGrid<>());

    @Override
    public void setNode(Node node){
        nodeGrid.setNode(node);
    }

    @Override
    public LogicState getState(Vector2D pos, Orientation orientation){
        return nodeGrid.getState(pos, orientation);
    }

    @Override
    public Node getNode(Vector2D pos) {
        return nodeGrid.getNode(pos);
    }

    @Override
    public Iterator<Node> iterator() {
        return nodeGrid.iterator();
    }

    @Override
    public void updateWire(Vector2D pos, Orientation orientation, WireState state) {
        if(orientation == Orientation.HORIZONTALLY){
            nodeGrid.setRightWire(pos, state);
        }
        else {
            nodeGrid.setDownWire(pos, state);
        }
    }

    @Override
    public void updateCrossing(Vector2D pos, Node.WireCrossing crossing) {
        nodeGrid.setCrossing(pos, crossing);
    }

    @Override
    public void propagateGenerators(List<Generator> generators){
        resetWiresToLow();

        Deque<Vector2D> candidatesForSearch = new ArrayDeque<>();

        for(Generator generator : generators){
            candidatesForSearch.add(generator.getPos());
            candidatesForSearch.addAll(getSurroundingCandidates(generator.getPos(), generator.getOrientation()));
            setSurroundingWiresToHigh(generator.getPos(), generator.getOrientation());
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

    private void resetWiresToLow(){
        Iterator<Node> iterator = nodeGrid.iterator();
        while(iterator.hasNext()){
            Node node = iterator.next();
            resetNodeWiresToLow(node.getPosition());
        }
    }

    private void resetNodeWiresToLow(Vector2D pos){
        if(getArrayNode(pos).getRightWire() == WireState.HIGH){
            updateWire(pos, Orientation.HORIZONTALLY, WireState.LOW);
        }
        if(getArrayNode(pos).getDownWire() == WireState.HIGH){
            updateWire(pos, Orientation.VERTICALLY, WireState.LOW);
        }
    }

    List<Vector2D> getSurroundingCandidates(Vector2D pos, Orientation orientation){
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

    void setSurroundingWiresToHigh(Vector2D pos, Orientation orientation){
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
