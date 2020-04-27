package InternalModel;

import InternalModel.LogicElements.LogicElement;
import InternalModel.LogicElements.LogicOne;
import InternalModel.LogicElements.NotGate;
import InternalModel.WireGrid.ArrayWireGrid;
import InternalModel.WireGrid.Generator;
import InternalModel.WireGrid.Node;
import InternalModel.WireGrid.WireGrid;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    public WireGrid arrayWireGrid = new ArrayWireGrid(20, 5);
    List<LogicElement> logicElements = new ArrayList<>();

    Simulation(){
        arrayWireGrid.setElement(new Vector2D(10, 1), new Node(Node.State.HIGH, Node.State.HIGH, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(11, 1), new Node(Node.State.LOW, Node.State.LOW, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setElement(new Vector2D(11, 0), new Node(Node.State.LOW, Node.State.LOW, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setElement(new Vector2D(12, 0), new Node(Node.State.LOW, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));

        arrayWireGrid.setElement(new Vector2D(10, 0), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(9, 0), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(8, 0), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(7, 0), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(6, 0), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(5, 0), new Node(Node.State.LOW, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(4, 0), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(3, 0), new Node(Node.State.LOW, Node.State.LOW, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setElement(new Vector2D(2, 0), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(1, 0), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(0, 0), new Node(Node.State.LOW, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));

        arrayWireGrid.setElement(new Vector2D(0, 1), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(0, 2), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(0, 3), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(0, 4), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));

        arrayWireGrid.setElement(new Vector2D(5, 1), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(3, 1), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.NOT_TOUCHING));


        arrayWireGrid.setElement(new Vector2D(10, 3), new Node(Node.State.LOW, Node.State.LOW, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setElement(new Vector2D(10, 4), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setElement(new Vector2D(11, 4), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(12, 4), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(11, 3), new Node(Node.State.NONE, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(12, 3), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(13, 3), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setElement(new Vector2D(13, 4), new Node(Node.State.NONE, Node.State.NONE, Node.WireCrossing.TOUCHING));


        arrayWireGrid.setElement(new Vector2D(10, 5), new Node(Node.State.LOW, Node.State.LOW, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setElement(new Vector2D(10, 6), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setElement(new Vector2D(11, 6), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(12, 6), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(11, 5), new Node(Node.State.NONE, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(12, 5), new Node(Node.State.LOW, Node.State.NONE, Node.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(13, 5), new Node(Node.State.NONE, Node.State.LOW, Node.WireCrossing.TOUCHING));
        arrayWireGrid.setElement(new Vector2D(13, 6), new Node(Node.State.NONE, Node.State.NONE, Node.WireCrossing.TOUCHING));

        //TODO: check arguments
        //logicElements.add(new LogicOne(0,0));
        logicElements.add(new NotGate(11,3));
        logicElements.add(new NotGate(11,5));
    }

    void simulate(int numberOfTicks){
        //simulate gates and get generators of signal 1
        //then propagate the 1s throughout thr wire

        //Required input and output wires without crossings because then you dont know which wire should get the result
        //These can't be changed in the program, they're set by the logic gate
        //Visually they are at the edge of the logic gate - you can connect to it only from one side so crossing are nonexistent
        //We can assume it's always TOUCHING

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

            List<LogicState> results = element.computeValues(inputStates);
            ArrayList<Vector2D> outputPos = element.getOutputPositions();
            for (int j = 0; j < results.size(); j++) {
                if(results.get(j) == LogicState.HIGH) generators.add(new Generator(outputPos.get(j), Orientation.HORIZONTALLY));
            }
        }
        //Propagate the high state throughout the wires
        arrayWireGrid.resetWiresToLow();
        arrayWireGrid.propagateGenerators(generators);


    }
}
