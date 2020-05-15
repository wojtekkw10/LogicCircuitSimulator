package LogicCircuitSimulator.FxGUI.CircuitBoard.FXMLController;

import LogicCircuitSimulator.App;
import LogicCircuitSimulator.FxGUI.CircuitBoard.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.FxGUI.CircuitBoard.BoardMouseSpecifiers.MouseCrossingSpecifier;
import LogicCircuitSimulator.FxGUI.CircuitBoard.BoardMouseSpecifiers.MouseLogicElementSpecifier;
import LogicCircuitSimulator.FxGUI.CircuitBoard.BoardMouseSpecifiers.MouseWireSpecifier;
import LogicCircuitSimulator.Simulation.NodeHandler.Node;
import LogicCircuitSimulator.Simulation.NodeHandler.WireState;
import LogicCircuitSimulator.Simulation.LogicElements.*;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Simulation.Simulation;
import LogicCircuitSimulator.Vector2D;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.*;

import java.util.concurrent.atomic.AtomicBoolean;

import static LogicCircuitSimulator.App.primaryStage;

public class BoardEventHandler {
    private final BoardDTO boardDTO;
    private Vector2D lastMousePressPosition = new Vector2D(0,0);
    private WireMode wireMode = WireMode.ADDING;

    private final double SCALING_FACTOR = 0.05;

    public enum WireMode{
        ADDING,
        REMOVING
    }

    public BoardEventHandler(BoardDTO boardDTO) {
        this.boardDTO = boardDTO;

        initialize();
    }

    private void initialize(){
        Canvas canvas = boardDTO.getCanvas();
        Projection2D projection2D = boardDTO.getProjection2D();
        Simulation simulation = boardDTO.getSimulation();
        AtomicBoolean isLogicGateDragged = boardDTO.getIsLogicGateDragged();
        double MAX_ZOOM = boardDTO.getMAX_ZOOM();
        double MIN_ZOOM = boardDTO.getMIN_ZOOM();

        // ON SCROLL
        EventHandler<ScrollEvent> onScrollEventHandler = event -> {
            double currentScale = projection2D.getScale();
            if(event.getDeltaY()>0 && currentScale < MAX_ZOOM) {
                projection2D.scale(1+SCALING_FACTOR, boardDTO.getLastMousePosition());
            }
            else if(event.getDeltaY()<0 && currentScale > MIN_ZOOM) {
                projection2D.scale(1-SCALING_FACTOR, boardDTO.getLastMousePosition());
            }
        };
        canvas.addEventFilter(ScrollEvent.SCROLL, onScrollEventHandler);

        //ON KEY RELEASED
        EventHandler<KeyEvent> onKeyReleasedEventHandler = event -> {
            if(event.getCode() == KeyCode.R){
                new MouseLogicElementSpecifier(simulation){
                    @Override
                    public void doAction() {
                        rotateLogicElementClockwise();
                    }
                }.getElementFromMousePosition(boardDTO.getLastMousePosition(), projection2D);
            }
            else if(event.getCode() == KeyCode.P){
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
                App.loadAndSetNewScene("/FXML/StartMenu2.fxml");
            }
            createLogicElementAtMouseOnKeyEvent(event.getCode());
        };
        canvas.addEventFilter(KeyEvent.KEY_RELEASED, onKeyReleasedEventHandler);

        //ON MOUSE PRESSED
        EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
            boardDTO.setLastMousePosition(new Vector2D(event.getX(), event.getY()));
            lastMousePressPosition = new Vector2D(event.getX(), event.getY());

            if(event.getButton() == MouseButton.PRIMARY){
                new MouseLogicElementSpecifier(simulation){
                    @Override
                    public void doAction() {
                        boardDTO.setLogicGateDragged(getLogicElement());
                        boardDTO.getLogicGateDragged().setPosition(getPosition());
                        isLogicGateDragged.set(true);
                        removeLogicElement();
                    }
                }.getElementFromMousePosition(new Vector2D(event.getX(), event.getY()), projection2D);
            }

            new MouseWireSpecifier(simulation){
                @Override
                public void doAction() {
                    if(getWireState() != WireState.NONE) wireMode = WireMode.REMOVING;
                    else wireMode = WireMode.ADDING;
                }
            }.performTransformation(new Vector2D(event.getX(), event.getY()), projection2D);
        };
        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);

        //ON MOUSE RELEASED
        EventHandler<MouseEvent> onMouseReleasedEventHandler = event -> {
            Vector2D mousePos = new Vector2D(event.getX(), event.getY());
            if(isLogicGateDragged.get()){
                new MouseLogicElementSpecifier(simulation){
                    @Override
                    public void doAction() {
                        boardDTO.getLogicGateDragged().setPosition(getPosition());
                        simulation.addLogicGate(boardDTO.getLogicGateDragged());
                        isLogicGateDragged.set(false);
                    }
                }.getElementPosFromElementAndMousePosition(mousePos, projection2D, boardDTO.getLogicGateDragged());
            }
            else{
                if (event.getButton() == MouseButton.PRIMARY && event.isStillSincePress()) {
                    new MouseCrossingSpecifier(simulation){
                        @Override
                        public void doAction() {
                            if (getCrossing() == Node.WireCrossing.TOUCHING) updateCrossing(Node.WireCrossing.NOT_TOUCHING);
                            else updateCrossing(Node.WireCrossing.TOUCHING);
                        }
                    }.performTransformation(mousePos, projection2D);
                }
                else if(event.getButton() == MouseButton.SECONDARY && event.isStillSincePress()){
                    new MouseLogicElementSpecifier(simulation){
                        @Override
                        public void doAction() {
                            removeLogicElement();
                        }
                    }.getElementFromMousePosition(new Vector2D(event.getX(), event.getY()), projection2D);
                }
            }
        };
        canvas.addEventFilter(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

        //ON MOUSE DRAGGED
        EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
            boardDTO.setLastMousePosition(new Vector2D(event.getX(), event.getY()));

            if(event.getButton() == MouseButton.MIDDLE){
                double deltaX = lastMousePressPosition.getX() - event.getX();
                double deltaY = lastMousePressPosition.getY() - event.getY();

                lastMousePressPosition = new Vector2D(event.getX(), event.getY());

                projection2D.translate(new Vector2D(-deltaX, -deltaY));
            }

            if(event.getButton() == MouseButton.PRIMARY && !isLogicGateDragged.get()){
                new MouseWireSpecifier(simulation){
                    @Override
                    public void doAction() {
                        if(wireMode == WireMode.ADDING) this.updateWireState(WireState.HIGH);
                        else updateWireState(WireState.NONE);
                    }
                }.performTransformation(new Vector2D(event.getX(), event.getY()), projection2D);
            }
        };
        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);

        //OM MOUSE MOVED
        EventHandler<MouseEvent> onMouseMovedEventHandler = event -> {
            boardDTO.setLastMousePosition(new Vector2D(event.getX(), event.getY()));
        };
        canvas.addEventFilter(MouseEvent.MOUSE_MOVED, onMouseMovedEventHandler);
    }

    private void createLogicElementAtMouseOnKeyEvent(KeyCode keycode){
        AtomicBoolean isLogicGateDragged = boardDTO.getIsLogicGateDragged();

        int x = 0;
        int y = 0;
        if(keycode == KeyCode.DIGIT1){
            isLogicGateDragged.set(true);
            boardDTO.setLogicGateDragged(new LogicClock(x,y, Rotation.RIGHT));
        }
        else if(keycode == KeyCode.DIGIT2){
            isLogicGateDragged.set(true);
            boardDTO.setLogicGateDragged(new BufferGate(x,y, Rotation.RIGHT));
        }
        else if(keycode == KeyCode.DIGIT3){
            isLogicGateDragged.set(true);
            boardDTO.setLogicGateDragged(new LogicOne(x,y, Rotation.RIGHT));
        }
        else if(keycode == KeyCode.DIGIT4){
            isLogicGateDragged.set(true);
            boardDTO.setLogicGateDragged(new NotGate(x,y, Rotation.RIGHT));
        }
        else if(keycode == KeyCode.DIGIT5){
            isLogicGateDragged.set(true);
            boardDTO.setLogicGateDragged(new OrGate(x,y, Rotation.RIGHT));
        }
        else if(keycode == KeyCode.DIGIT6){
            isLogicGateDragged.set(true);
            boardDTO.setLogicGateDragged(new AndGate(x,y, Rotation.RIGHT));
        }
        else if(keycode == KeyCode.DIGIT7){
            isLogicGateDragged.set(true);
            boardDTO.setLogicGateDragged(new XorGate(x,y, Rotation.RIGHT));
        }
    }
}
