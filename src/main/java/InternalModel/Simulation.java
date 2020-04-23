package InternalModel;

import InternalModel.LogicElements.LogicElement;
import InternalModel.LogicElements.LogicOne;
import InternalModel.WireGrid.ArrayWireGrid;
import InternalModel.WireGrid.Generator;
import InternalModel.WireGrid.Wire;
import InternalModel.WireGrid.WireGrid;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    public WireGrid arrayWireGrid = new ArrayWireGrid(20, 5);
    List<LogicElement> logicElements = new ArrayList<>();

    Simulation(){
        arrayWireGrid.setElement(new Vector2D(10, 1), new Wire(Wire.State.HIGH, Wire.State.HIGH, Wire.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(11, 1), new Wire(Wire.State.LOW, Wire.State.LOW, Wire.WireCrossing.TOUCHING));
        arrayWireGrid.setElement(new Vector2D(11, 0), new Wire(Wire.State.LOW, Wire.State.LOW, Wire.WireCrossing.TOUCHING));
        arrayWireGrid.setElement(new Vector2D(12, 0), new Wire(Wire.State.LOW, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));

        arrayWireGrid.setElement(new Vector2D(10, 0), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(9, 0), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(8, 0), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(7, 0), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(6, 0), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(5, 0), new Wire(Wire.State.LOW, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(4, 0), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(3, 0), new Wire(Wire.State.LOW, Wire.State.LOW, Wire.WireCrossing.TOUCHING));
        arrayWireGrid.setElement(new Vector2D(2, 0), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(1, 0), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(0, 0), new Wire(Wire.State.LOW, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));

        arrayWireGrid.setElement(new Vector2D(0, 1), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(0, 2), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(0, 3), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(0, 4), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));

        arrayWireGrid.setElement(new Vector2D(5, 1), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(3, 1), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));


        arrayWireGrid.setElement(new Vector2D(10, 3), new Wire(Wire.State.NONE, Wire.State.LOW, Wire.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(10, 4), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));
        arrayWireGrid.setElement(new Vector2D(11, 4), new Wire(Wire.State.LOW, Wire.State.NONE, Wire.WireCrossing.NOT_TOUCHING));

        //TODO: check arguments
        logicElements.add(new LogicOne(5,2));
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

            LogicState result = element.computeValue(inputStates);
            Vector2D outputPos = element.getOutput();

            if(result == LogicState.HIGH) generators.add(new Generator(outputPos, Orientation.HORIZONTALLY));
        }

        //Propagate the high state throughout the wires
        arrayWireGrid.propagateGenerators(generators);


    }
}
