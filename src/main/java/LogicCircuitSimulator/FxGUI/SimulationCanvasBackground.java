package LogicCircuitSimulator.FxGUI;

import LogicCircuitSimulator.FxGUI.FXMLControllers.SimulationCanvasController;
import LogicCircuitSimulator.FxGUI.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.Vector2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SimulationCanvasBackground {
    private static final Color dotColor = Color.AQUA;
    private static final Color backgroundColor = Color.BLACK;
    private final GraphicsContext graphicsContext;
    private final Canvas simulationCanvas;

    public SimulationCanvasBackground(Canvas simulationCanvas) {
        this.simulationCanvas = simulationCanvas;
        this.graphicsContext = simulationCanvas.getGraphicsContext2D();
    }

    public void draw(Projection2D projection){
        graphicsContext.setFill(backgroundColor);

        double canvasWidth = simulationCanvas.getWidth();
        double canvasHeight = simulationCanvas.getHeight();

        double scale = projection.getScale();

        setStroke(graphicsContext, scale);

        int amountHorizontally = (int)Math.ceil(canvasWidth / scale);
        int amountVertically = (int)Math.ceil(canvasHeight / scale);

        Vector2D gridShift = projection.getTranslation();

        int gridShiftX = (int)(gridShift.getX()/scale);
        int gridShiftY = (int)(gridShift.getY()/scale);

        double backgroundDisappearingFactor = 1.5;

        if(scale > SimulationCanvasController.MIN_ZOOM *backgroundDisappearingFactor){
            for (int i = -gridShiftX; i < amountHorizontally - gridShiftX; i++) {
                for (int j = -gridShiftY; j < amountVertically - gridShiftY; j++) {
                    Vector2D pointPos = projection.project(new Vector2D(i,j));
                    int x = (int)pointPos.getX();
                    int y = (int)pointPos.getY();

                    graphicsContext.strokeLine(x,y,x,y);
                }
            }
        }
    }

    private void setStroke(GraphicsContext ctx, double scale){
        double MAX_SCALE = SimulationCanvasController.MAX_ZOOM;
        Color relativeDotColor = Color.color(dotColor.getRed(), dotColor.getGreen(), dotColor.getBlue(), Math.min(1, 1*scale/MAX_SCALE));
        ctx.setStroke(relativeDotColor);
        ctx.setLineWidth(2);
    }
}
