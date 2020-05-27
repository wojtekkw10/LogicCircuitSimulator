package LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.EventHandlers;

import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.BoardDTO;
import LogicCircuitSimulator.Vector2D;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class BoardDraggingEventHandler {
    private final BoardDTO boardDTO;
    private Vector2D lastMousePressPosition = new Vector2D(0,0);

    public BoardDraggingEventHandler(BoardDTO boardDTO){
        this.boardDTO = boardDTO;
        initialize();
    }

    private void initialize(){
        EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
            boardDTO.setLastMousePosition(new Vector2D(event.getX(), event.getY()));

            if (event.getButton() == MouseButton.MIDDLE) {
                double deltaX = lastMousePressPosition.getX() - event.getX();
                double deltaY = lastMousePressPosition.getY() - event.getY();

                lastMousePressPosition = new Vector2D(event.getX(), event.getY());

                boardDTO.getProjection2D().translate(new Vector2D(-deltaX, -deltaY));
            }
        };
        boardDTO.getCanvas().addEventFilter(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);

        EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
            lastMousePressPosition = new Vector2D(event.getX(), event.getY());
        };
        boardDTO.getCanvas().addEventFilter(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
    }

}
