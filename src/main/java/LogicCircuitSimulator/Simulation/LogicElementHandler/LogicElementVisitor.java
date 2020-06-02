package LogicCircuitSimulator.Simulation.LogicElementHandler;

import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.*;

public interface LogicElementVisitor {
    void visit(AndGate andGate);
    void visit(OrGate orGate);
    void visit(BufferGate bufferGate);
    void visit(LogicOne logicOne);
    void visit(LogicZero logicZero);
    void visit(NotGate notGate);
    void visit(XorGate xorGate);
    void visit(LogicClock clock);
    void visit(ToggleOn toggleOn);
    void visit(ToggleOff toggleOff);
    void visit(ButtonLogicElement buttonLogicElement);
    void visit(OutputGate outputGate);
    void visit(InputGate inputGate);
}
