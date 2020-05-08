package LogicCircuitSimulator;

import LogicCircuitSimulator.NodeHandler.Node;

public interface NodeVisitor {
    void visit(Node node);
}
