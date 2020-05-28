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
        Projection2D projection2D = boardDTO.getProjection2D();

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
                    boardDTO.setTARGET_UPS(100);
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
            createLogicElementAtMouseOnKeyEvent(event.getCode());
            event.consume();
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

    private void createLogicElementAtMouseOnKeyEvent(KeyCode keycode){
        AtomicBoolean isLogicGateDragged = boardDTO.getIsLogicGateLifted();
        boardDTO.setRelativeMouseToLogicGatePos(new Vector2D(0.5,0));

        int x = 0;
        int y = 0;

        KeyCode[] keyArray = {KeyCode.DIGIT1, KeyCode.DIGIT2, KeyCode.DIGIT3,
                KeyCode.DIGIT4, KeyCode.DIGIT5, KeyCode.DIGIT6, KeyCode.DIGIT7, KeyCode.DIGIT8, KeyCode.DIGIT9};
        LogicElement[] logicElementArray = {
                new LogicClock(x,y, Rotation.RIGHT),
                new BufferGate(x,y, Rotation.RIGHT),
                new LogicOne(x,y, Rotation.RIGHT),
                new NotGate(x,y, Rotation.RIGHT),
                new OrGate(x,y, Rotation.RIGHT),
                new AndGate(x,y, Rotation.RIGHT),
                new XorGate(x,y, Rotation.RIGHT),
                new ToggleOff(x,y, Rotation.RIGHT),
                new ButtonLogicElement(x,y, Rotation.RIGHT)
        };

        for (int i = 0; i < keyArray.length; i++) {
            if(keycode == keyArray[i]) {
                boardDTO.setLogicGateDragged(logicElementArray[i]);
                isLogicGateDragged.set(true);
            }

        }
    }
}
