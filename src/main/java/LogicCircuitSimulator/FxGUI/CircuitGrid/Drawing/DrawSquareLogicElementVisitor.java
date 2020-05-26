package LogicCircuitSimulator.FxGUI.CircuitGrid.Drawing;

import LogicCircuitSimulator.FxGUI.CircuitGrid.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.*;
import LogicCircuitSimulator.Simulation.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

public class DrawSquareLogicElementVisitor implements LogicElementVisitor {
    private final Projection2D projection2D;
    private final GraphicsContext graphicsContext;

    public DrawSquareLogicElementVisitor(GraphicsContext graphicsContext, Projection2D projection2D){
        this.graphicsContext = graphicsContext;
        this.projection2D = projection2D;
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

    @Override
    public void visit(ToggleOn toggleOn) {
        drawGate(toggleOn, "ON");
    }

    @Override
    public void visit(ToggleOff toggleOff) {
        drawGate(toggleOff, "OFF");
    }

    @Override
    public void visit(ButtonLogicElement buttonLogicElement) {
        drawGate(buttonLogicElement, "BTN");
    }

    void drawGate(LogicElement le, String text){
        graphicsContext.setLineWidth(1);
        double scale = projection2D.getScale();
        Vector2D pos = le.getPosition();
        double height = le.getGeometry().getElementHeight();
        graphicsContext.setFont(new Font(Font.getFontNames().get(0), scale * 0.4));

        if(le.getRotation() == Rotation.RIGHT){
            Vector2D topLeft = projection2D.project(new Vector2D(pos.getX(), pos.getY()-0.5));
            Vector2D bottomRight = projection2D.project(new Vector2D(pos.getX()+1, pos.getY()+0.5 + height));
            Vector2D textPosition = projection2D.project(new Vector2D(pos.getX()+0.1, pos.getY()));

            drawGateInside(topLeft, bottomRight);
            drawGateLabel(textPosition.getX(), textPosition.getY(), text, 0, graphicsContext);
            drawGateLegs(le, new Vector2D(-0.2, 0), new Vector2D(0.2, 0));
        }
        else if(le.getRotation() == Rotation.LEFT){
            Vector2D topLeft = projection2D.project(new Vector2D(pos.getX(), pos.getY()-0.5));
            Vector2D bottomRight = projection2D.project(new Vector2D(pos.getX()+1, pos.getY()+0.5+height));
            Vector2D textPosition = projection2D.project(new Vector2D(pos.getX()+0.9, pos.getY()+height));

            drawGateInside(topLeft, bottomRight);
            drawGateLabel(textPosition.getX(), textPosition.getY(), text, 180, graphicsContext);
            drawGateLegs(le, new Vector2D(0.2, 0), new Vector2D(-0.2, 0));

        }
        else if(le.getRotation() == Rotation.UP){
            Vector2D topLeft = projection2D.project(new Vector2D(pos.getX()-0.5, pos.getY()));
            Vector2D bottomRight = projection2D.project(new Vector2D(pos.getX()+0.5+height, pos.getY()+1));
            Vector2D textPosition = projection2D.project(new Vector2D(pos.getX(), pos.getY()+0.9));

            drawGateInside(topLeft, bottomRight);
            drawGateLabel(textPosition.getX(), textPosition.getY(), text, -90, graphicsContext);
            drawGateLegs(le,  new Vector2D(0, 0.2), new Vector2D(0, -0.2));
        }
        else if(le.getRotation() == Rotation.DOWN){
            Vector2D topLeft = projection2D.project(new Vector2D(pos.getX()-0.5, pos.getY()));
            Vector2D bottomRight = projection2D.project(new Vector2D(pos.getX()+0.5 + height, pos.getY()+1));
            Vector2D textPosition = projection2D.project(new Vector2D(pos.getX() + height, pos.getY()+0.1));

            drawGateInside(topLeft, bottomRight);
            drawGateLabel(textPosition.getX(), textPosition.getY(), text, 90, graphicsContext);
            drawGateLegs(le,  new Vector2D(0, -0.2), new Vector2D(0, 0.2));
        }
    }

    private void drawGateLegs(LogicElement le, Vector2D inputLegDirection, Vector2D outputLegDirection) {
        List<Vector2D> inputPositions = le.getGeometry().getInputPositions();
        drawLinesFrom(inputPositions, inputLegDirection);

        List<Vector2D> outputPositions = le.getGeometry().getOutputPositions();
        drawLinesFrom(outputPositions, outputLegDirection);
    }

    private void drawGateInside(Vector2D topLeft, Vector2D bottomRight) {
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(topLeft.getX(), topLeft.getY(), bottomRight.getX() - topLeft.getX(), bottomRight.getY() - topLeft.getY());
        graphicsContext.setStroke(Color.GREY);
        graphicsContext.strokeRect(topLeft.getX(), topLeft.getY(), bottomRight.getX() - topLeft.getX(), bottomRight.getY() - topLeft.getY());
        graphicsContext.setFill(Color.AQUA);
    }

    private void drawGateLabel(double x, double y, String text, double degrees, GraphicsContext gc) {
        gc.save();
        gc.translate(x, y);
        gc.rotate(degrees);
        gc.translate(-x, -y);
        gc.fillText(text, x, y);
        gc.restore();
    }

    private void drawLinesFrom(List<Vector2D> points, Vector2D direction){
        graphicsContext.setStroke(Color.GREY);
        for (Vector2D point : points) {
            Vector2D projectedStart = projection2D.project(point);
            Vector2D projectedEnd = projection2D.project(new Vector2D(point.getX()+direction.getX(), point.getY()+direction.getY()));
            graphicsContext.strokeLine(projectedStart.getX(), projectedStart.getY(), projectedEnd.getX(), projectedEnd.getY());
        }
    }
}


//https://stackoverflow.com/questions/39524792/javafx-rotate-text