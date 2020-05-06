package LogicCircuitSimulator.FxGUI;

import LogicCircuitSimulator.FxGUI.FXMLControllers.SimulationCanvasController;
import LogicCircuitSimulator.Utils.MatrixOperations;
import LogicCircuitSimulator.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.ejml.simple.SimpleMatrix;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

public class SimulationCanvasBackground {
    private static final Color dotColor = Color.AQUA;
    private static final Color backgroundColor = Color.BLACK;

    public void draw(GraphicsContext ctx, double canvasWidth, double canvasHeight, SimpleMatrix projection){
        ctx.setFill(backgroundColor);

        double scale = MatrixOperations.getScaleFromMatrix(projection);

        setStroke(ctx, scale);

        int amountHorizontally = (int)Math.ceil(canvasWidth / scale);
        int amountVertically = (int)Math.ceil(canvasHeight / scale);

        Vector2D gridShift = MatrixOperations.getVectorFromFullMatrix(projection);

        int gridShiftX = (int)(gridShift.getX()/scale);
        int gridShiftY = (int)(gridShift.getY()/scale);

        double backgroundDisappearingFactor = 1.5;

        ctx.fillRect(0, 0, canvasWidth, canvasHeight);
        if(scale > SimulationCanvasController.MIN_SCALE*backgroundDisappearingFactor){
            for (int i = -gridShiftX; i < amountHorizontally - gridShiftX; i++) {
                for (int j = -gridShiftY; j < amountVertically - gridShiftY; j++) {
                    Vector2D pointPos = MatrixOperations.projectPoint(projection, new Vector2D(i, j));
                    int x = (int)pointPos.getX();
                    int y = (int)pointPos.getY();

                    ctx.strokeLine(x,y,x,y);
                }
            }
        }



    }

    private void setStroke(GraphicsContext ctx, double scale){
        double MAX_SCALE = SimulationCanvasController.MAX_SCALE;
        Color relativeDotColor = Color.color(dotColor.getRed(), dotColor.getGreen(), dotColor.getBlue(), Math.min(1, 1*scale/MAX_SCALE));
        ctx.setStroke(relativeDotColor);
        ctx.setLineWidth(2);
    }
}
