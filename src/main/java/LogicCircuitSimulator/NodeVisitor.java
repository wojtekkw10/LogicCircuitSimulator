package LogicCircuitSimulator;

import LogicCircuitSimulator.WireGrid.Node;

public interface NodeVisitor {
    void visit(Node node);
}
