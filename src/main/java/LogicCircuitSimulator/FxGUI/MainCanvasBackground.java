package LogicCircuitSimulator.FxGUI;

import LogicCircuitSimulator.FxGUI.FXMLControllers.SimulationCanvas;
import LogicCircuitSimulator.Simulation;
import LogicCircuitSimulator.Utils.MatrixOperations;
import LogicCircuitSimulator.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.ejml.simple.SimpleMatrix;

public class MainCanvasBackground {
    private static final Color dotColor = Color.AQUA;
    private static final Color backgroundColor = Color.BLACK;

    public void draw(GraphicsContext ctx, double canvasWidth, double canvasHeight, SimpleMatrix projection){
        ctx.setFill(backgroundColor);

        double scale = MatrixOperations.getScaleFromMatrix(projection);
        double MAX_SCALE = SimulationCanvas.MAX_SCALE;

        Color relativeDotColor = Color.color(dotColor.getRed(), dotColor.getGreen(), dotColor.getBlue(), Math.min(1, 1*scale/MAX_SCALE));
        ctx.setStroke(relativeDotColor);

        int amountHorizontally = (int)Math.ceil(canvasWidth / scale);
        int amountVertically = (int)Math.ceil(canvasHeight / scale);

        Vector2D gridShift = MatrixOperations.getVectorFromFullMatrix(projection);

        int gridShiftX = (int)(gridShift.getX()/scale);
        int gridShiftY = (int)(gridShift.getY()/scale);

        ctx.setLineWidth(2);

        ctx.fillRect(0, 0, canvasWidth, canvasHeight);
        for (int i = -gridShiftX; i < amountHorizontally - gridShiftX; i++) {
            for (int j = -gridShiftY; j < amountVertically - gridShiftY; j++) {
                SimpleMatrix pos = MatrixOperations.getVectorMatrix(i, j);
                SimpleMatrix projectedPos = projection.mult(pos);
                Vector2D pointPos = MatrixOperations.getVectorFromVectorMatrix(projectedPos);
                int x = (int)pointPos.getX();
                int y = (int)pointPos.getY();

                ctx.strokeLine(x,y,x,y);
            }
        }
    }
}
