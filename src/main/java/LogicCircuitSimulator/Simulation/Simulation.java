package LogicCircuitSimulator.Simulation;

import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElementHandler;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.BufferGate;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.ComputedValue;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.NotGate;
import LogicCircuitSimulator.Simulation.LogicElementHandler.SimpleLogicElementHandler;
import LogicCircuitSimulator.Simulation.NodeHandler.*;
import LogicCircuitSimulator.Vector2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Simulation {

    private NodeHandler nodeHandler = new ArrayNodeHandler();
    private LogicElementHandler logicElements = new SimpleLogicElementHandler();

    public void runOnce() {
        List<Generator> generators = new ArrayList<>();

        Iterator<LogicElement> logicElementIterator = logicElements.iterator();
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

    public void initTestSimulation(){
        addNotLoop(new Vector2D(10, 3));
        addNotLoop(new Vector2D(10, 5));

        int pos = 4;
        for (int i = 0; i < 100; i++) {
            logicElements.add(new BufferGate(pos, 1, Rotation.RIGHT));
            pos++;
            nodeHandler.setRightWire(new Vector2D(pos, 1), WireState.LOW);
            pos++;
        }
    }

    public NodeHandler getNodeHandler() {
        return nodeHandler;
    }
    public LogicElementHandler getLogicElementHandler(){
        return logicElements;
    }

    public void setNodeHandler(NodeHandler nodeHandler) {
        this.nodeHandler = nodeHandler;
    }

    public void setLogicElementHandler(LogicElementHandler logicElements) {
        this.logicElements = logicElements;
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
        logicElements.add(new NotGate(x+1,y, Rotation.RIGHT));
    }
}