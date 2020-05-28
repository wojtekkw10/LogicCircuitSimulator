package LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController;

import LogicCircuitSimulator.App;
import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.EventHandlers.*;
import LogicCircuitSimulator.FxGUI.CircuitGrid.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.*;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Vector2D;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.concurrent.atomic.AtomicBoolean;

public class BoardEventHandler {
    private final BoardDTO boardDTO;

    public BoardEventHandler(BoardDTO boardDTO) {
        this.boardDTO = boardDTO;

        new BoardDraggingEventHandler(boardDTO);
        new BoardZoomingEventHandler(boardDTO);
        new LogicElementEventHandler(boardDTO);
        new NodeEventHandler(boardDTO);
        new SelectionEventHandling(boardDTO);

        initialize();
    }

    private void initialize(){
        Canvas canvas = boardDTO.getCanvas();

        //ON KEY RELEASED
        EventHandler<KeyEvent> onKeyReleasedEventHandler = event -> {
            if(event.getCode() == KeyCode.P){
                if(boardDTO.shouldDrawSpeedStats()) boardDTO.setShouldDrawSpeedStats(false);
                else boardDTO.setShouldDrawSpeedStats(true);
            }
            else if(event.getCode() == KeyCode.K) {
                if (boardDTO.isUpsLimited()) {
                    boardDTO.setTARGET_UPS(1_000_000);
                    boardDTO.setUpsLimited(false);
                    boardDTO.setUpsChanged(true);
                }
                else {
                    boardDTO.setTARGET_UPS(5);
                    boardDTO.setUpsLimited(true);
                    boardDTO.setUpsChanged(true);
                }
            }
            else if(event.getCode() == KeyCode.ESCAPE){
                if(!boardDTO.isShouldDrawSelectionRect()){
                    boardDTO.getExecutor().shutdownNow();
                    App.loadAndSetNewScene("/FXML/StartMenu.fxml");
                }

            }
        };
        canvas.addEventFilter(KeyEvent.KEY_RELEASED, onKeyReleasedEventHandler);

        //ON MOUSE PRESSED
        EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
            boardDTO.setLastMousePosition(new Vector2D(event.getX(), event.getY()));
        };
        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);

        //ON MOUSE DRAGGED
        EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
            boardDTO.setLastMousePosition(new Vector2D(event.getX(), event.getY()));
        };
        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);

        //OM MOUSE MOVED
        EventHandler<MouseEvent> onMouseMovedEventHandler = event -> {
            boardDTO.setLastMousePosition(new Vector2D(event.getX(), event.getY()));
        };
        canvas.addEventFilter(MouseEvent.MOUSE_MOVED, onMouseMovedEventHandler);
    }
}
