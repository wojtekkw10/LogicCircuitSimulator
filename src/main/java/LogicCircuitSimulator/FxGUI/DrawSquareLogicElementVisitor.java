package LogicCircuitSimulator.FxGUI;

import LogicCircuitSimulator.FxGUI.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.LogicElementVisitor;
import LogicCircuitSimulator.LogicElements.*;
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

    void drawGate(LogicElement le, String text){
        graphicsContext.setLineWidth(1);
        double scale = projection2D.getScale();
        Vector2D pos = le.getPosition();
        double height = le.getElementHeight();
        graphicsContext.setFont(new javafx.scene.text.Font(Font.getFontNames().get(0), scale * 0.4));

        if(le.getRotation() == Rotation.RIGHT){
            Vector2D topLeft = projection2D.project(new Vector2D(pos.getX(), pos.getY()-0.5));
            Vector2D bottomRight = projection2D.project(new Vector2D(pos.getX()+1, pos.getY()+0.5 + height));
            Vector2D textPosition = projection2D.project(new Vector2D(pos.getX()+0.1, pos.getY()));

            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect(topLeft.getX(), topLeft.getY(), bottomRight.getX() - topLeft.getX(), bottomRight.getY() - topLeft.getY());
            graphicsContext.setStroke(Color.GREY);
            graphicsContext.strokeRect(topLeft.getX(), topLeft.getY(), bottomRight.getX() - topLeft.getX(), bottomRight.getY() - topLeft.getY());
            graphicsContext.setFill(Color.AQUA);
            drawGateLabel(textPosition.getX(), textPosition.getY(), text, 0, graphicsContext);

            //rysowanie odnog
            graphicsContext.setStroke(Color.GREY);
            List<Vector2D> inputPositions = le.getInputPositions();
            for (Vector2D inputPos : inputPositions) {
                Vector2D projectedStart = projection2D.project(inputPos);
                Vector2D projectedEnd = projection2D.project(new Vector2D(inputPos.getX()-0.2, inputPos.getY()));
                graphicsContext.strokeLine(projectedStart.getX(), projectedStart.getY(), projectedEnd.getX(), projectedEnd.getY());
            }

            graphicsContext.setStroke(Color.GREY);
            List<Vector2D> outputPositions = le.getOutputPositions();
            for (Vector2D outputPos : outputPositions) {
                Vector2D projectedStart = projection2D.project(outputPos);
                Vector2D projectedEnd = projection2D.project(new Vector2D(outputPos.getX()+0.2, outputPos.getY()));
                graphicsContext.strokeLine(projectedStart.getX(), projectedStart.getY(), projectedEnd.getX(), projectedEnd.getY());
            }
        }
        else if(le.getRotation() == Rotation.LEFT){
            Vector2D topLeft = projection2D.project(new Vector2D(pos.getX(), pos.getY()-0.5));
            Vector2D bottomRight = projection2D.project(new Vector2D(pos.getX()+1, pos.getY()+0.5+height));
            Vector2D textPosition = projection2D.project(new Vector2D(pos.getX()+0.9, pos.getY()+height));

            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect(topLeft.getX(), topLeft.getY(), bottomRight.getX() - topLeft.getX(), bottomRight.getY() - topLeft.getY());
            graphicsContext.setStroke(Color.GREY);
            graphicsContext.strokeRect(topLeft.getX(), topLeft.getY(), bottomRight.getX() - topLeft.getX(), bottomRight.getY() - topLeft.getY());
            graphicsContext.setFill(Color.AQUA);
            drawGateLabel(textPosition.getX(), textPosition.getY(), text, 180, graphicsContext);

            //rysowanie odnog
            graphicsContext.setStroke(Color.GREY);
            List<Vector2D> inputPositions = le.getInputPositions();
            for (Vector2D inputPos : inputPositions) {
                Vector2D projectedStart = projection2D.project(inputPos);
                Vector2D projectedEnd = projection2D.project(new Vector2D(inputPos.getX()+0.2, inputPos.getY()));
                graphicsContext.strokeLine(projectedStart.getX(), projectedStart.getY(), projectedEnd.getX(), projectedEnd.getY());
            }

            graphicsContext.setStroke(Color.GREY);
            List<Vector2D> outputPositions = le.getOutputPositions();
            for (Vector2D outputPos : outputPositions) {
                Vector2D projectedStart = projection2D.project(outputPos);
                Vector2D projectedEnd = projection2D.project(new Vector2D(outputPos.getX()-0.2, outputPos.getY()));
                graphicsContext.strokeLine(projectedStart.getX(), projectedStart.getY(), projectedEnd.getX(), projectedEnd.getY());
            }
        }
        else if(le.getRotation() == Rotation.UP){
            Vector2D topLeft = projection2D.project(new Vector2D(pos.getX()-0.5, pos.getY()));
            Vector2D bottomRight = projection2D.project(new Vector2D(pos.getX()+0.5+height, pos.getY()+1));
            Vector2D textPosition = projection2D.project(new Vector2D(pos.getX(), pos.getY()+0.9));

            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect(topLeft.getX(), topLeft.getY(), bottomRight.getX() - topLeft.getX(), bottomRight.getY() - topLeft.getY());
            graphicsContext.setStroke(Color.GREY);
            graphicsContext.strokeRect(topLeft.getX(), topLeft.getY(), bottomRight.getX() - topLeft.getX(), bottomRight.getY() - topLeft.getY());
            graphicsContext.setFill(Color.AQUA);
            drawGateLabel(textPosition.getX(), textPosition.getY(), text, -90, graphicsContext);

            //rysowanie odnog
            graphicsContext.setStroke(Color.GREY);
            List<Vector2D> inputPositions = le.getInputPositions();
            for (Vector2D inputPos : inputPositions) {
                Vector2D projectedStart = projection2D.project(inputPos);
                Vector2D projectedEnd = projection2D.project(new Vector2D(inputPos.getX(), inputPos.getY()+0.2));
                graphicsContext.strokeLine(projectedStart.getX(), projectedStart.getY(), projectedEnd.getX(), projectedEnd.getY());
            }

            graphicsContext.setStroke(Color.GREY);
            List<Vector2D> outputPositions = le.getOutputPositions();
            for (Vector2D outputPos : outputPositions) {
                Vector2D projectedStart = projection2D.project(outputPos);
                Vector2D projectedEnd = projection2D.project(new Vector2D(outputPos.getX(), outputPos.getY()-0.2));
                graphicsContext.strokeLine(projectedStart.getX(), projectedStart.getY(), projectedEnd.getX(), projectedEnd.getY());
            }
        }
        else if(le.getRotation() == Rotation.DOWN){
            Vector2D topLeft = projection2D.project(new Vector2D(pos.getX()-0.5, pos.getY()));
            Vector2D bottomRight = projection2D.project(new Vector2D(pos.getX()+0.5 + height, pos.getY()+1));
            Vector2D textPosition = projection2D.project(new Vector2D(pos.getX() + height, pos.getY()+0.1));

            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect(topLeft.getX(), topLeft.getY(), bottomRight.getX() - topLeft.getX(), bottomRight.getY() - topLeft.getY());
            graphicsContext.setStroke(Color.GREY);
            graphicsContext.strokeRect(topLeft.getX(), topLeft.getY(), bottomRight.getX() - topLeft.getX(), bottomRight.getY() - topLeft.getY());
            graphicsContext.setFill(Color.AQUA);
            drawGateLabel(textPosition.getX(), textPosition.getY(), text, 90, graphicsContext);

            //rysowanie odnog
            graphicsContext.setStroke(Color.GREY);
            List<Vector2D> inputPositions = le.getInputPositions();
            for (Vector2D inputPos : inputPositions) {
                Vector2D projectedStart = projection2D.project(inputPos);
                Vector2D projectedEnd = projection2D.project(new Vector2D(inputPos.getX(), inputPos.getY()-0.2));
                graphicsContext.strokeLine(projectedStart.getX(), projectedStart.getY(), projectedEnd.getX(), projectedEnd.getY());
            }

            graphicsContext.setStroke(Color.GREY);
            List<Vector2D> outputPositions = le.getOutputPositions();
            for (Vector2D outputPos : outputPositions) {
                Vector2D projectedStart = projection2D.project(outputPos);
                Vector2D projectedEnd = projection2D.project(new Vector2D(outputPos.getX(), outputPos.getY()+0.2));
                graphicsContext.strokeLine(projectedStart.getX(), projectedStart.getY(), projectedEnd.getX(), projectedEnd.getY());
            }
        }
    }

    public static void drawGateLabel(double x, double y, String text, double degrees, GraphicsContext gc) {
        gc.save();
        gc.translate(x, y);
        gc.rotate(degrees);
        gc.translate(-x, -y);
        gc.fillText(text, x, y);
        gc.restore();
    }

    private void drawLinesFrom(List<Vector2D> points, Vector2D direction){

    }
}


//https://stackoverflow.com/questions/39524792/javafx-rotate-text