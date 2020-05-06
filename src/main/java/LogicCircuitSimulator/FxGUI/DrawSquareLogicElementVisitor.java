package LogicCircuitSimulator.FxGUI;

import LogicCircuitSimulator.LogicElementVisitor;
import LogicCircuitSimulator.LogicElements.*;
import LogicCircuitSimulator.Utils.MatrixOperations;
import LogicCircuitSimulator.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.ejml.simple.SimpleMatrix;

import java.awt.*;

public class DrawSquareLogicElementVisitor implements LogicElementVisitor {
    private final SimpleMatrix projectionMatrix;
    private final GraphicsContext graphicsContext;

    public DrawSquareLogicElementVisitor(GraphicsContext graphicsContext, SimpleMatrix projectionMatrix){
        this.graphicsContext = graphicsContext;
        this.projectionMatrix = projectionMatrix;
    }

    @Override
    public void visit(AndGate andGate) {
        drawGate(andGate, "AND");
    }

    @Override
    public void visit(OrGate orGate) {
        drawGate(orGate, "OR");
    }

    @Override
    public void visit(BufferGate bufferGate) {
        drawGate(bufferGate, "BFR");
    }

    @Override
    public void visit(LogicOne logicOne) {
        drawGate(logicOne, "ONE");
    }

    @Override
    public void visit(LogicZero logicZero) {
        drawGate(logicZero, "ZERO");
    }

    @Override
    public void visit(NotGate notGate) {
        drawGate(notGate, "NOT");
    }

    @Override
    public void visit(XorGate xorGate) {
        drawGate(xorGate, "XOR");
    }

    @Override
    public void visit(LogicClock clock) {
        drawGate(clock, "CLK");
    }

    void drawGate(LogicElement le, String text){
        double height = le.getElementHeight();
        Vector2D pos = le.getPosition();
        Vector2D topLeft = MatrixOperations.getVectorFromVectorMatrix(projectionMatrix.mult(MatrixOperations.getVectorMatrix(pos.getX(), pos.getY()-0.5)));
        Vector2D bottomRight = MatrixOperations.getVectorFromVectorMatrix(projectionMatrix.mult(MatrixOperations.getVectorMatrix(pos.getX()+1, pos.getY()+0.5 + height)));
        Vector2D textPosition = MatrixOperations.getVectorFromVectorMatrix(projectionMatrix.mult(MatrixOperations.getVectorMatrix(pos.getX()+0.1, pos.getY())));

        double scale = MatrixOperations.getScaleFromMatrix(projectionMatrix);
        graphicsContext.setFont(new javafx.scene.text.Font(Font.getFontNames().get(0), scale * 0.4));

        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(topLeft.getX(), topLeft.getY(), bottomRight.getX() - topLeft.getX(), bottomRight.getY() - topLeft.getY());
        graphicsContext.setStroke(Color.GREY);
        graphicsContext.strokeRect(topLeft.getX(), topLeft.getY(), bottomRight.getX() - topLeft.getX(), bottomRight.getY() - topLeft.getY());
        graphicsContext.setFill(Color.AQUA);
        graphicsContext.fillText(text, textPosition.getX(), textPosition.getY());
        //TODO: obracanie i rysowanie (zaznaczanie) wejścia wyjścia
    }
}
