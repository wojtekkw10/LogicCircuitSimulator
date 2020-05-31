package LogicCircuitSimulator.FxGUI.CircuitGrid.Drawing;

import LogicCircuitSimulator.FxGUI.CircuitGrid.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.Simulation.NodeHandler.Crossing;
import LogicCircuitSimulator.Simulation.NodeHandler.Node;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeVisitor;
import LogicCircuitSimulator.Simulation.NodeHandler.WireState;
import LogicCircuitSimulator.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawNodeVisitor implements NodeVisitor {
    private final Projection2D projection2D;
    private final GraphicsContext graphicsContext;
    private final Color activeColor;
    private final Color inactiveColor;

    public DrawNodeVisitor(GraphicsContext graphicsContext, Projection2D projection2D, Color activeColor, Color inactiveColor){
        this.graphicsContext = graphicsContext;
        this.projection2D = projection2D;
        this.activeColor = activeColor;
        this.inactiveColor = inactiveColor;
    }

    @Override
    public void visit(Node node) {
        graphicsContext.setLineWidth(1);
        if(node.getRightWire() != WireState.NONE){
            Vector2D pos = node.getPosition();
            Vector2D projectedStart = projection2D.project(pos);
            Vector2D projectedEnd = projection2D.project(new Vector2D(pos.getX()+1, pos.getY()));
            if(node.getRightWire() == WireState.LOW) graphicsContext.setStroke(inactiveColor);
            else graphicsContext.setStroke(activeColor);
            graphicsContext.strokeLine(projectedStart.getX(), projectedStart.getY(), projectedEnd.getX(), projectedEnd.getY());
        }
        if(node.getDownWire() != WireState.NONE){
            Vector2D pos = node.getPosition();
            Vector2D projectedStart = projection2D.project(pos);
            Vector2D projectedEnd = projection2D.project(new Vector2D(pos.getX(), pos.getY()+1));
            if(node.getDownWire() == WireState.LOW) graphicsContext.setStroke(inactiveColor);
            else graphicsContext.setStroke(activeColor);
            graphicsContext.strokeLine(projectedStart.getX(), projectedStart.getY(), projectedEnd.getX(), projectedEnd.getY());
        }
        if(node.isTouching() == Crossing.TOUCHING){
            graphicsContext.setFill(activeColor);
            Vector2D pos = node.getPosition();
            Vector2D projectedStart = projection2D.project(new Vector2D(pos.getX()-0.1, pos.getY()-0.1));
            Vector2D projectedEnd = projection2D.project(new Vector2D(pos.getX()+0.1, pos.getY()+0.1));
            double width = projectedEnd.getX()-projectedStart.getX();
            double height = projectedEnd.getY()-projectedStart.getY();

            graphicsContext.fillRect(projectedStart.getX(), projectedStart.getY(), width, height);

        }
    }
}
