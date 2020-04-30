package LogicCircuitSimulator.FxGUI;

import LogicCircuitSimulator.NodeVisitor;
import LogicCircuitSimulator.Utils.MatrixOperations;
import LogicCircuitSimulator.Vector2D;
import LogicCircuitSimulator.WireGrid.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.ejml.simple.SimpleMatrix;

public class DrawNodeVisitor implements NodeVisitor {
    private final SimpleMatrix projectionMatrix;
    private final GraphicsContext graphicsContext;

    public DrawNodeVisitor(GraphicsContext graphicsContext, SimpleMatrix projectionMatrix){
        this.graphicsContext = graphicsContext;
        this.projectionMatrix = projectionMatrix;
    }

    @Override
    public void visit(Node node) {
        graphicsContext.setLineWidth(1);
        if(node.getRightWire() != Node.State.NONE){
            Vector2D pos = node.getPosition();
            Vector2D projectedStart = MatrixOperations.getVectorFromVectorMatrix(projectionMatrix.mult(MatrixOperations.getVectorMatrix(pos.getX(), pos.getY())));
            Vector2D projectedEnd = MatrixOperations.getVectorFromVectorMatrix(projectionMatrix.mult(MatrixOperations.getVectorMatrix(pos.getX()+1, pos.getY())));
            if(node.getRightWire() == Node.State.LOW) graphicsContext.setStroke(Color.GREY);
            else graphicsContext.setStroke(Color.AQUA);
            graphicsContext.strokeLine(projectedStart.getX(), projectedStart.getY(), projectedEnd.getX(), projectedEnd.getY());
        }
        if(node.getDownWire() != Node.State.NONE){
            Vector2D pos = node.getPosition();
            Vector2D projectedStart = MatrixOperations.getVectorFromVectorMatrix(projectionMatrix.mult(MatrixOperations.getVectorMatrix(pos.getX(), pos.getY())));
            Vector2D projectedEnd = MatrixOperations.getVectorFromVectorMatrix(projectionMatrix.mult(MatrixOperations.getVectorMatrix(pos.getX(), pos.getY()+1)));
            if(node.getDownWire() == Node.State.LOW) graphicsContext.setStroke(Color.GREY);
            else graphicsContext.setStroke(Color.AQUA);
            graphicsContext.strokeLine(projectedStart.getX(), projectedStart.getY(), projectedEnd.getX(), projectedEnd.getY());
        }
        if(node.isTouching() == Node.WireCrossing.TOUCHING){
            graphicsContext.setFill(Color.AQUA);
            Vector2D pos = node.getPosition();
            Vector2D projectedStart = MatrixOperations.getVectorFromVectorMatrix(projectionMatrix.mult(MatrixOperations.getVectorMatrix(pos.getX()-0.1, pos.getY()-0.1)));
            Vector2D projectedEnd = MatrixOperations.getVectorFromVectorMatrix(projectionMatrix.mult(MatrixOperations.getVectorMatrix(pos.getX()+0.1, pos.getY()+0.1)));
            graphicsContext.fillRect(projectedStart.getX(), projectedStart.getY(),
                    projectedEnd.getX()-projectedStart.getX(), projectedEnd.getY()-projectedStart.getY());

        }
        //TODO: rysowanie touching
    }
}
