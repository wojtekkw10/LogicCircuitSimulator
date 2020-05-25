package LogicCircuitSimulator.Simulation;

import LogicCircuitSimulator.Simulation.LogicElements.*;
import LogicCircuitSimulator.Simulation.NodeHandler.*;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.UnboundGrid.GridIterator;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.UnboundGrid.UnboundGrid;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.UnboundGrid.UnboundHashMapGrid;
import LogicCircuitSimulator.Vector2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Simulation {

    private NodeHandler nodeHandler = new ArrayNodeHandler();
    private UnboundGrid<LogicElement> logicElements = new UnboundHashMapGrid<>();

    public void runOnce() {
        List<Generator> generators = new ArrayList<>();

        GridIterator<LogicElement> logicElementIterator = logicElements.iterator();
        while(logicElementIterator.hasNext()) {
            LogicElement element = logicElementIterator.next();
            List<Vector2D> inputPositions = element.getGeometry().getInputPositions();
            ArrayList<LogicState> inputStates = new ArrayList<>();

            for (Vector2D position : inputPositions) {
                LogicState inputState = nodeHandler.getState(position, Orientation.HORIZONTALLY);
                inputStates.add(inputState);
            }

            List<ComputedValue> results = element.computeValues(inputStates);
            for (ComputedValue result : results) {
                if (result.getState() == LogicState.HIGH)
                    if (element.getRotation() == Rotation.LEFT || element.getRotation() == Rotation.RIGHT) {
                        generators.add(new Generator(result.getPos(), Orientation.HORIZONTALLY));
                    } else {
                        generators.add(new Generator(result.getPos(), Orientation.VERTICALLY));
                    }
            }
        }
        //Propagate the high state throughout the wires
        nodeHandler.propagateGenerators(generators);
    }

    public Iterator<Node> nodeIterator() {
        return nodeHandler.iterator();
    }

    public Iterator<LogicElement> logicElementIterator() {
        return new LogicElementIterator();
    }

    public void updateWire(Vector2D pos, Orientation orientation, WireState state) {
        if(orientation == Orientation.HORIZONTALLY)
            nodeHandler.setRightWire(pos, state);
        else {
            nodeHandler.setDownWire(pos, state);
        }
    }

    public void updateCrossing(Vector2D pos, Crossing crossing) {
        nodeHandler.setCrossing(pos, crossing);
    }

    public Crossing getCrossing(Vector2D pos){
        return nodeHandler.getCrossing(pos);
    }

    public WireState getRightWire(Vector2D pos){
        return nodeHandler.getRightWire(pos);
    }
    public WireState getDownWire(Vector2D pos){
        return nodeHandler.getDownWire(pos);
    }

    public void addLogicGate(LogicElement logicElement){
        logicElements.set(logicElement.getPosition(), logicElement);
    }

    private void addNotLoop(Vector2D pos){
        int x = (int)pos.getX();
        int y = (int)pos.getY();
        nodeHandler.setRightWire(new Vector2D(x, y), WireState.LOW);
        nodeHandler.setDownWire(new Vector2D(x, y), WireState.LOW);
        nodeHandler.setCrossing(new Vector2D(x, y), Crossing.TOUCHING);
        nodeHandler.setRightWire(new Vector2D(x, y+1), WireState.LOW);
        nodeHandler.setCrossing(new Vector2D(x, y+1), Crossing.TOUCHING);
        nodeHandler.setRightWire(new Vector2D(x+1, y+1), WireState.LOW);
        nodeHandler.setRightWire(new Vector2D(x+2, y+1), WireState.LOW);
        nodeHandler.setRightWire(new Vector2D(x+2, y), WireState.LOW);
        nodeHandler.setCrossing(new Vector2D(x+3, y), Crossing.TOUCHING);
        nodeHandler.setDownWire(new Vector2D(x+3, y), WireState.LOW);
        nodeHandler.setCrossing(new Vector2D(x+3, y+1), Crossing.TOUCHING);
    }

    public void removeLogicElement(Vector2D pos){
        Iterator<LogicElement> logicElementsIterator = logicElementIterator();
        while(logicElementsIterator.hasNext()){
            LogicElement logicElement = logicElementsIterator.next();
            if(logicElement.getPosition().equals(pos)){
                logicElementsIterator.remove();
            }
        }

    }

    public void initTestSimulation(){
        addNotLoop(new Vector2D(10, 3));
        addNotLoop(new Vector2D(10, 5));

        addLogicGate(new NotGate(11,3, Rotation.DOWN));
        addLogicGate(new NotGate(11, 5, Rotation.RIGHT));

        int pos = 4;
        for (int i = 0; i < 10; i++) {
            addLogicGate(new BufferGate(pos, 1, Rotation.RIGHT));
            pos++;
            nodeHandler.setRightWire(new Vector2D(pos, 1), WireState.LOW);
            pos++;
        }
    }

    public NodeHandler getNodeHandler() {
        return nodeHandler;
    }

    public void setNodeHandler(NodeHandler nodeHandler) {
        this.nodeHandler = nodeHandler;
    }

    public void setLogicElements(UnboundGrid<LogicElement> logicElements) {
        this.logicElements = logicElements;
    }

    private class LogicElementIterator implements Iterator<LogicElement>{
        GridIterator<LogicElement> iterator = logicElements.iterator();

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public LogicElement next() {
            return iterator.next();
        }

        @Override
        public void remove() {
            logicElements.remove(iterator.currentPosition());
        }
    }
}