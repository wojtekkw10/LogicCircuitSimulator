package LogicCircuitSimulator;

import LogicCircuitSimulator.LogicElements.*;

public interface LogicElementVisitor {
    void visit(AndGate andGate);
    void visit(OrGate andGate);
    void visit(BufferGate bufferGate);
    void visit(LogicOne logicOne);
    void visit(LogicZero logicZero);
    void visit(NotGate notGate);
    void visit(XorGate xorGate);
}
