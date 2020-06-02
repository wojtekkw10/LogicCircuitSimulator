package LogicCircuitSimulator.FxGUI.CircuitGrid.Drawing;

import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.BoardDTO;
import LogicCircuitSimulator.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SelectionRectDrawer {
    public void draw(BoardDTO boardDTO){
        GraphicsContext graphicsContext = boardDTO.getCanvas().getGraphicsContext2D();
        Vector2D screenUpperLeft = boardDTO.getProjection2D().project(boardDTO.getSelectUpperLeft());
        Vector2D screenRightBottom = boardDTO.getProjection2D().project(boardDTO.getSelectBottomRight());
        double width = screenRightBottom.getX() - screenUpperLeft.getX();
        double height = screenRightBottom.getY() - screenUpperLeft.getY();
        Vector2D leftUpper = screenUpperLeft;
        Vector2D rightBottom = screenRightBottom;

        graphicsContext.setStroke(Color.GREY);

        graphicsContext.strokeRect(leftUpper.getX(), leftUpper.getY(), width, height);
        graphicsContext.setFill(new Color(0.2, 0.2, 0.2, 0.3));
        graphicsContext.fillRect(leftUpper.getX(), leftUpper.getY(), width, height);



    }
}
