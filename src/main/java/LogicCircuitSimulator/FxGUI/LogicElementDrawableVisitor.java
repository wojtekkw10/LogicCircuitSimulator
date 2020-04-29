package LogicCircuitSimulator.FxGUI;

import LogicCircuitSimulator.LogicElementVisitor;
import LogicCircuitSimulator.LogicElements.*;
import javafx.scene.canvas.GraphicsContext;

public class LogicElementDrawableVisitor implements LogicElementVisitor {
    GraphicsContext ctx;

    public LogicElementDrawableVisitor(GraphicsContext ctx){
        this.ctx = ctx;
    }

    @Override
    public void visit(AndGate andGate) {

    }

    @Override
    public void visit(OrGate andGate) {

    }

    @Override
    public void visit(BufferGate bufferGate) {

    }

    @Override
    public void visit(LogicOne logicOne) {

    }

    @Override
    public void visit(LogicZero logicZero) {

    }

    @Override
    public void visit(NotGate notGate) {

    }

    @Override
    public void visit(XorGate xorGate) {

    }
}
