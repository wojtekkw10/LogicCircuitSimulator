package InternalModel;

import InternalModel.LogicElements.LogicElement;
import InternalModel.LogicElements.LogicOne;
import InternalModel.LogicElements.LogicZero;
import InternalModel.LogicElements.NotGate;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    public WireGrid wireGrid = new WireGrid(20, 5);
    List<LogicElement> logicElements = new ArrayList<>();

    Simulation(){
        wireGrid.setElement(10,1, new Wire(Wire.WireState.HIGH, Wire.WireState.HIGH, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(11,1, new Wire(Wire.WireState.LOW, Wire.WireState.LOW, Wire.WireCrossing.TOUCHING));
        wireGrid.setElement(11,0, new Wire(Wire.WireState.LOW, Wire.WireState.LOW, Wire.WireCrossing.TOUCHING));
        wireGrid.setElement(12,0, new Wire(Wire.WireState.LOW, Wire.WireState.LOW, Wire.WireCrossing.NOT_TOUCHING));

        wireGrid.setElement(10,0, new Wire(Wire.WireState.LOW, Wire.WireState.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(9,0, new Wire(Wire.WireState.LOW, Wire.WireState.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(8,0, new Wire(Wire.WireState.LOW, Wire.WireState.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(7,0, new Wire(Wire.WireState.LOW, Wire.WireState.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(6,0, new Wire(Wire.WireState.LOW, Wire.WireState.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(5,0, new Wire(Wire.WireState.LOW, Wire.WireState.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(4,0, new Wire(Wire.WireState.LOW, Wire.WireState.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(3,0, new Wire(Wire.WireState.LOW, Wire.WireState.LOW, Wire.WireCrossing.TOUCHING));
        wireGrid.setElement(2,0, new Wire(Wire.WireState.LOW, Wire.WireState.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(1,0, new Wire(Wire.WireState.LOW, Wire.WireState.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(0,0, new Wire(Wire.WireState.LOW, Wire.WireState.LOW, Wire.WireCrossing.NOT_TOUCHING));

        wireGrid.setElement(0,1, new Wire(Wire.WireState.NONE, Wire.WireState.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(0,2, new Wire(Wire.WireState.NONE, Wire.WireState.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(0,3, new Wire(Wire.WireState.NONE, Wire.WireState.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(0,4, new Wire(Wire.WireState.NONE, Wire.WireState.LOW, Wire.WireCrossing.NOT_TOUCHING));

        wireGrid.setElement(5,1, new Wire(Wire.WireState.NONE, Wire.WireState.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(3,1, new Wire(Wire.WireState.NONE, Wire.WireState.LOW, Wire.WireCrossing.NOT_TOUCHING));


        wireGrid.setElement(10,3, new Wire(Wire.WireState.NONE, Wire.WireState.LOW, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(10,4, new Wire(Wire.WireState.LOW, Wire.WireState.NONE, Wire.WireCrossing.NOT_TOUCHING));
        wireGrid.setElement(11,4, new Wire(Wire.WireState.LOW, Wire.WireState.NONE, Wire.WireCrossing.NOT_TOUCHING));


        logicElements.add(new LogicOne(0,0));
    }

    void simulate(int numberOfTicks){
        //simulate gates and get generators of signal 1
        //then propagate the 1s throughout thr wire

        //Required input and output wires without crossings because then you dont know which wire should get the result
        //These can't be changed in the program, they're set by the logic gate
        //Visually they are at the edge of the logic gate - you can connect to it only from one side so crossing are nonexistent
        //We can assume it's always TOUCHING

        List<Vector2D> generators = new ArrayList<>();

        for (int i = 0; i < logicElements.size(); i++) {
            LogicElement element = logicElements.get(i);
            List<Vector2D> inputPositions = element.getInputPositions();
            ArrayList<LogicState> inputStates = new ArrayList<>();

            for (int j = 0; j < inputPositions.size(); j++) {
                Vector2D position = inputPositions.get(j);
                LogicState inputState = wireGrid.getState(position);
                inputStates.add(inputState);
            }

            LogicState result = element.computeValue(inputStates);
            Vector2D outputPos = element.getOutput();

            if(result == LogicState.HIGH) generators.add(outputPos);
        }

        //Propagate the high state throughout the wires
        wireGrid.resetWiresToLow();
        wireGrid.propagateGenerators(generators);


    }
}
