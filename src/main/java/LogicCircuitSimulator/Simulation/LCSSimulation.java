package LogicCircuitSimulator.Simulation;

import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElementHandler;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeHandler;

public interface LCSSimulation {
    void runOnce();

    void setNodeHandler(NodeHandler nodeHandler);
    NodeHandler getNodeHandler();

    void setLogicElementHandler(LogicElementHandler logicElementHandler);
    LogicElementHandler getLogicElementHandler();
}
