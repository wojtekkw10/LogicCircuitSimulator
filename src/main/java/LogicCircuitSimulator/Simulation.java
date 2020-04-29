package LogicCircuitSimulator;

import LogicCircuitSimulator.LogicElements.*;
import LogicCircuitSimulator.WireGrid.*;

import java.util.ArrayList;
import java.util.List;

public class Simulation{
    WireGrid arrayWireGrid = new ArrayWireGrid();
    List<LogicElement> logicElements = new ArrayList<>();

    public Simulation(){
        arrayWireGrid.setNode(new Vector2D(0, 0), new Node(Node.State.NONE, Node.State.NONE, Node.WireCrossing.TOUCHING));

        arrayWireGrid.setNode(new Vector2D(1, 1), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Vector2D(2, 1), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Vector2D(1, 2), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Vector2D(2, 2), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Vector2D(4, 1), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Vector2D(5, 1), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));

        arrayWireGrid.setNode(new Vector2D(10, 3), new Node(Node.State.LOW, Node.State.LOW, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Vector2D(10, 4), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Vector2D(11, 4), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setNode(new Vector2D(12, 4), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setNode(new Vector2D(11, 3), new Node(Node.State.NONE, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setNode(new Vector2D(12, 3), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setNode(new Vector2D(13, 3), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Vector2D(13, 4), new Node(Node.State.NONE, Node.State.NONE, Node.WireCrossing.TOUCHING));


        arrayWireGrid.setNode(new Vector2D(10, 5), new Node(Node.State.LOW, Node.State.LOW, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Vector2D(10, 6), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Vector2D(11, 6), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setNode(new Vector2D(12, 6), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setNode(new Vector2D(11, 5), new Node(Node.State.NONE, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setNode(new Vector2D(12, 5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setNode(new Vector2D(13, 5), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setNode(new Vector2D(13, 6), new Node(Node.State.NONE, Node.State.NONE, Node.WireCrossing.TOUCHING));

        logicElements.add(new NotGate(11,3, Rotation.LEFT));
        logicElements.add(new NotGate(11,5, Rotation.RIGHT));

        logicElements.add(new LogicOne(1,1, Rotation.RIGHT));
        logicElements.add(new LogicOne(1,2, Rotation.RIGHT));
        logicElements.add(new XorGate(3,1, Rotation.RIGHT));

        System.out.println(arrayWireGrid);
        runOnce();
        System.out.println(arrayWireGrid);
        runOnce();
        System.out.println(arrayWireGrid);
    }

    public void runOnce(){
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
                if(results.get(j).getState() == LogicState.HIGH) generators.add(new Generator(results.get(j).getPos(), Orientation.HORIZONTALLY));
            }
        }
        //Propagate the high state throughout the wires
        arrayWireGrid.resetWiresToLow();
        arrayWireGrid.propagateGenerators(generators);
    }

    public Iterator2D<Node> iterator(){
        return arrayWireGrid.getIterator();
    }
}
