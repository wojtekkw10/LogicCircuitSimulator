package LogicCircuitSimulator;

import LogicCircuitSimulator.LogicElements.*;
import LogicCircuitSimulator.WireGrid.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Simulation {
    public WireGrid arrayWireGrid = new ArrayWireGrid();
    List<LogicElement> logicElements = new ArrayList<>();

    public Simulation() {
        arrayWireGrid.setNode(new Node(new Vector2D(0, 0), Node.State.NONE, Node.State.NONE, Node.WireCrossing.TOUCHING));



        arrayWireGrid.setNode(new Node(new Vector2D(10, 3), Node.State.LOW, Node.State.LOW, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Node(new Vector2D(10, 4), Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Node(new Vector2D(11, 4), Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setNode(new Node(new Vector2D(12, 4), Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setNode(new Node(new Vector2D(11, 3), Node.State.NONE, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setNode(new Node(new Vector2D(12, 3), Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setNode(new Node(new Vector2D(13, 3), Node.State.NONE, Node.State.LOW, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Node(new Vector2D(13, 4), Node.State.NONE, Node.State.NONE, Node.WireCrossing.TOUCHING));


        arrayWireGrid.setNode(new Node(new Vector2D(10, 5), Node.State.LOW, Node.State.LOW, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Node(new Vector2D(10, 6), Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Node(new Vector2D(11, 6), Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setNode(new Node(new Vector2D(12, 6), Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setNode(new Node(new Vector2D(11, 5), Node.State.NONE, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setNode(new Node(new Vector2D(12, 5), Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setNode(new Node(new Vector2D(13, 5), Node.State.NONE, Node.State.LOW, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Node(new Vector2D(13, 6), Node.State.NONE, Node.State.NONE, Node.WireCrossing.TOUCHING));

        logicElements.add(new NotGate(11, 3, Rotation.LEFT));
        logicElements.add(new NotGate(11, 5, Rotation.RIGHT));

        int leftShift = -2;

        logicElements.add(new LogicClock(1+leftShift, 1, Rotation.RIGHT));
        logicElements.add(new LogicOne(1+leftShift, 2, Rotation.RIGHT));
        logicElements.add(new XorGate(3+leftShift, 1, Rotation.RIGHT));
        //logicElements.add(new NotGate(-10, -10, Rotation.RIGHT));

        arrayWireGrid.setNode(new Node(new Vector2D(1+leftShift, 1), Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Node(new Vector2D(2+leftShift, 1), Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Node(new Vector2D(1+leftShift, 2), Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Node(new Vector2D(2+leftShift, 2), Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Node(new Vector2D(4+leftShift, 1), Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Node(new Vector2D(5+leftShift, 1), Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));

        int pos = 4;
        for (int i = 0; i < 100; i++) {
            logicElements.add(new BufferGate(pos, 1, Rotation.RIGHT));
            pos++;
            arrayWireGrid.setNode(new Node(new Vector2D(pos, 1), Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
            pos++;
        }
    }

    public synchronized void runOnce() {
        List<Generator> generators = new ArrayList<>();

        for (int i = 0; i < logicElements.size(); i++) {
            LogicElement element = logicElements.get(i);
            List<Vector2D> inputPositions = element.getInputPositions();
            ArrayList<LogicState> inputStates = new ArrayList<>();

            for (int j = 0; j < inputPositions.size(); j++) {
                Vector2D position = inputPositions.get(j);
                LogicState inputState = arrayWireGrid.getState(position, Orientation.HORIZONTALLY);
                inputStates.add(inputState);
            }

            List<ComputedValue> results = element.computeValues(inputStates);
            for (int j = 0; j < results.size(); j++) {
                if (results.get(j).getState() == LogicState.HIGH)
                    generators.add(new Generator(results.get(j).getPos(), Orientation.HORIZONTALLY));
            }

        }
        //Propagate the high state throughout the wires
        arrayWireGrid.propagateGenerators(generators);
    }

    public Iterator<Node> nodeIterator() {
        return arrayWireGrid.iterator();
    }

    public Iterator<LogicElement> logicElementIterator() {
        return logicElements.iterator();
    }

    public synchronized void updateWire(Vector2D pos, Orientation orientation, Node.State state) {
        arrayWireGrid.updateWire(pos, orientation, state);
    }

    public void updateCrossing(Vector2D pos, Node.WireCrossing crossing) {
        arrayWireGrid.updateCrossing(pos, crossing);
    }

    public Node getNode(Vector2D pos){
        return arrayWireGrid.getNode(pos);
    }
}