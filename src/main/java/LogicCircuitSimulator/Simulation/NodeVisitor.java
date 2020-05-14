package LogicCircuitSimulator.Simulation;

import LogicCircuitSimulator.Simulation.NodeHandler.Node;

public interface NodeVisitor {
    void visit(Node node);
}
