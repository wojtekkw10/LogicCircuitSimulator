package LogicCircuitSimulator.Simulation;

import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElementHandler;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.ComputedValue;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElementFactory;
import LogicCircuitSimulator.Simulation.LogicElementHandler.SimpleLogicElementHandler;
import LogicCircuitSimulator.Simulation.NodeHandler.ArrayNodeHandler;
import LogicCircuitSimulator.Simulation.NodeHandler.Generator;
import LogicCircuitSimulator.Simulation.NodeHandler.Node;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeHandler;
import LogicCircuitSimulator.Vector2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleLCSSimulation implements LCSSimulation{
    private NodeHandler nodeHandler = new ArrayNodeHandler();
    private LogicElementHandler logicElements = new SimpleLogicElementHandler();

    public synchronized SimpleLCSSimulation instanceOf(LCSSimulation simulation){
        synchronized (simulation){
            SimpleLCSSimulation simpleLCSSimulation = new SimpleLCSSimulation();
            LogicElementHandler oldLogicElementHandler = simulation.getLogicElementHandler();
            NodeHandler oldNodeHandler = simulation.getNodeHandler();

            LogicElementHandler newLogicElementHandler = simpleLCSSimulation.getLogicElementHandler();
            NodeHandler newNodeHandler = simpleLCSSimulation.getNodeHandler();

            Iterator<LogicElement> logicElementIterator = oldLogicElementHandler.iterator();
            Iterator<Node> nodeIterator = oldNodeHandler.iterator();

            while(logicElementIterator.hasNext())
                newLogicElementHandler.add(LogicElementFactory.instance(logicElementIterator.next()));
            while(nodeIterator.hasNext())
                newNodeHandler.setNode(new Node(nodeIterator.next()));


            return simpleLCSSimulation;
        }



    }

    public synchronized void runOnce() {
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
}