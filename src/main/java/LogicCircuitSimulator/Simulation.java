package LogicCircuitSimulator;

import LogicCircuitSimulator.LogicElements.*;
import LogicCircuitSimulator.WireGrid.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Simulation {
    public NodeHandler arrayNodeHandler = new ArrayNodeHandler();
    List<LogicElement> logicElements = new ArrayList<>();

    public Simulation() {
        addNotLoop(new Vector2D(10, 3));
        addNotLoop(new Vector2D(10, 5));

        logicElements.add(new NotGate(11, 3, Rotation.DOWN));
        logicElements.add(new NotGate(11, 5, Rotation.RIGHT));

        logicElements.add(new XorGate(-5, -5, Rotation.RIGHT));
        logicElements.add(new XorGate(-5, 10, Rotation.DOWN));

        int leftShift = -2;
        logicElements.add(new LogicClock(1+leftShift, 1, Rotation.RIGHT));
        logicElements.add(new LogicOne(1+leftShift, 2, Rotation.RIGHT));
        logicElements.add(new XorGate(3+leftShift, 1, Rotation.RIGHT));
        //logicElements.add(new NotGate(-10, -10, Rotation.RIGHT));

        arrayNodeHandler.setNode(new Node(new Vector2D(2+leftShift, 1), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayNodeHandler.setNode(new Node(new Vector2D(2+leftShift, 2), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayNodeHandler.setNode(new Node(new Vector2D(4+leftShift, 1), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayNodeHandler.setNode(new Node(new Vector2D(5+leftShift, 1), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));

        int pos = 4;
        for (int i = 0; i < 10; i++) {
            logicElements.add(new BufferGate(pos, 1, Rotation.RIGHT));
            pos++;
            arrayNodeHandler.setNode(new Node(new Vector2D(pos, 1), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
            pos++;
        }
    }

    public void runOnce() {
        List<Generator> generators = new ArrayList<>();

        for (int i = 0; i < logicElements.size(); i++) {
            LogicElement element = logicElements.get(i);
            List<Vector2D> inputPositions = element.getInputPositions();
            ArrayList<LogicState> inputStates = new ArrayList<>();

            for (int j = 0; j < inputPositions.size(); j++) {
                Vector2D position = inputPositions.get(j);
                LogicState inputState = arrayNodeHandler.getState(position, Orientation.HORIZONTALLY);
                inputStates.add(inputState);
            }

            List<ComputedValue> results = element.computeValues(inputStates);
            for (int j = 0; j < results.size(); j++) {
                if (results.get(j).getState() == LogicState.HIGH)
                    generators.add(new Generator(results.get(j).getPos(), Orientation.HORIZONTALLY));
            }

        }
        //Propagate the high state throughout the wires
        arrayNodeHandler.propagateGenerators(generators);
    }

    public Iterator<Node> nodeIterator() {
        return arrayNodeHandler.iterator();
    }

    public Iterator<LogicElement> logicElementIterator() {
        return logicElements.iterator();
    }

    public void updateWire(Vector2D pos, Orientation orientation, WireState state) {
        arrayNodeHandler.updateWire(pos, orientation, state);
    }

    public void updateCrossing(Vector2D pos, Node.WireCrossing crossing) {
        arrayNodeHandler.updateCrossing(pos, crossing);
    }

    public Node getNode(Vector2D pos){
        return arrayNodeHandler.getNode(pos);
    }

    public void addLogicGate(LogicElement logicElement){
        logicElements.add(logicElement);
    }

    private void addNotLoop(Vector2D pos){
        int x = (int)pos.getX();
        int y = (int)pos.getY();
        arrayNodeHandler.setNode(new Node(new Vector2D(x, y), WireState.LOW, WireState.LOW, Node.WireCrossing.TOUCHING));
        arrayNodeHandler.setNode(new Node(new Vector2D(x, y+1), WireState.LOW, WireState.NONE, Node.WireCrossing.TOUCHING));
        arrayNodeHandler.setNode(new Node(new Vector2D(x+1, y+1), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayNodeHandler.setNode(new Node(new Vector2D(x+2, y+1), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayNodeHandler.setNode(new Node(new Vector2D(x+1, y), WireState.NONE, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayNodeHandler.setNode(new Node(new Vector2D(x+2, y), WireState.LOW, WireState.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayNodeHandler.setNode(new Node(new Vector2D(x+3, y), WireState.NONE, WireState.LOW, Node.WireCrossing.TOUCHING));
        arrayNodeHandler.setNode(new Node(new Vector2D(x+3, y+1), WireState.NONE, WireState.NONE, Node.WireCrossing.TOUCHING));
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
}