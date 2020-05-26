package LogicCircuitSimulator.FxGUI.CircuitGrid.Drawing;

import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.BoardDTO;
import LogicCircuitSimulator.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SelectAreaDrawer {
    public void draw(BoardDTO boardDTO){
        GraphicsContext graphicsContext = boardDTO.getCanvas().getGraphicsContext2D();
        double width = boardDTO.getSelectRightBottom().getX() - boardDTO.getSelectLeftUpper().getX();
        double height = boardDTO.getSelectRightBottom().getY() - boardDTO.getSelectLeftUpper().getY();
        Vector2D leftUpper = boardDTO.getSelectLeftUpper();
        Vector2D rightBottom = boardDTO.getSelectRightBottom();

        graphicsContext.setStroke(Color.GREY);
        if(width < 0 && height < 0){
            leftUpper = new Vector2D(rightBottom.getX(), rightBottom.getY());
            graphicsContext.strokeRect(leftUpper.getX(), leftUpper.getY(), -width, -height);
        }
        else if(width < 0){
            leftUpper = new Vector2D(rightBottom.getX(), leftUpper.getY());
            graphicsContext.strokeRect(leftUpper.getX(), leftUpper.getY(), -width, height);
        }
        else if(height < 0) {
            leftUpper = new Vector2D(leftUpper.getX(), rightBottom.getY());
            graphicsContext.strokeRect(leftUpper.getX(), leftUpper.getY(), width, -height);
        }
        else {
            graphicsContext.strokeRect(leftUpper.getX(), leftUpper.getY(), width, height);
        }

        graphicsContext.setFill(new Color(0.2, 0.2, 0.2, 0.3));
        graphicsContext.fillRect(boardDTO.getSelectLeftUpper().getX(), boardDTO.getSelectLeftUpper().getY(),
                width, height);
    }
}
