package LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController;

import LogicCircuitSimulator.App;
import LogicCircuitSimulator.FxGUI.CircuitGrid.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.FxGUI.CircuitGrid.BoardMouseSpecifiers.MouseCrossingSpecifier;
import LogicCircuitSimulator.FxGUI.CircuitGrid.BoardMouseSpecifiers.MouseLogicElementSpecifier;
import LogicCircuitSimulator.FxGUI.CircuitGrid.BoardMouseSpecifiers.MouseWireSpecifier;
import LogicCircuitSimulator.Simulation.NodeHandler.Crossing;
import LogicCircuitSimulator.Simulation.NodeHandler.WireState;
import LogicCircuitSimulator.Simulation.LogicElements.*;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Vector2D;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.*;

import java.util.concurrent.atomic.AtomicBoolean;

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
        AtomicBoolean isLogicGateLefted = boardDTO.getIsLogicGateLifted();
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
                new MouseLogicElementSpecifier(boardDTO.getSimulation()){
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
                boardDTO.getExecutor().shutdownNow();
                App.loadAndSetNewScene("/FXML/StartMenu.fxml");
            }
            createLogicElementAtMouseOnKeyEvent(event.getCode());
            event.consume();
        };
        canvas.addEventFilter(KeyEvent.KEY_RELEASED, onKeyReleasedEventHandler);

        //ON MOUSE PRESSED
        EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
            boardDTO.setLastMousePosition(new Vector2D(event.getX(), event.getY()));
            lastMousePressPosition = new Vector2D(event.getX(), event.getY());

            if(event.getButton() == MouseButton.PRIMARY){
                new MouseLogicElementSpecifier(boardDTO.getSimulation()){
                    @Override
                    public void doAction() {
                        boardDTO.setLogicGateDragged(getLogicElement());
                        boardDTO.getLogicGateDragged().setPosition(getPosition());
                        isLogicGateLefted.set(true);
                        boardDTO.setRelativeMouseToLogicGatePos(getRelativeMousePos());
                        removeLogicElement();
                    }
                }.getElementFromMousePosition(new Vector2D(event.getX(), event.getY()), projection2D);
            }

            new MouseWireSpecifier(boardDTO.getSimulation()){
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
            if(isLogicGateLefted.get() && event.getButton() == MouseButton.PRIMARY){

                new MouseLogicElementSpecifier(boardDTO.getSimulation()){
                    @Override
                    public void doAction() {
                        boardDTO.getLogicGateDragged().setPosition(getPosition());
                        boardDTO.getSimulation().addLogicGate(boardDTO.getLogicGateDragged());
                        isLogicGateLefted.set(false);
                    }
                }.getElementPosFromElementAndMousePosition(mousePos, projection2D, boardDTO.getLogicGateDragged(), boardDTO.getRelativeMouseToLogicGatePos());

                new MouseLogicElementSpecifier(boardDTO.getSimulation()){
                    @Override
                    public void doAction() {
                        LogicElement selectedElement = getLogicElement();

                        if(selectedElement.getName().equals("TGL_ON")){
                            Vector2D pos = getPosition();
                            removeLogicElement();
                            LogicElement toggleOff = new ToggleOff((int)pos.getX(), (int)pos.getY(), selectedElement.getRotation());
                            boardDTO.getSimulation().addLogicGate(toggleOff);
                        }
                        else if(selectedElement.getName().equals("TGL_OFF")){
                            Vector2D pos = getPosition();
                            removeLogicElement();
                            LogicElement toggleOn = new ToggleOn((int)pos.getX(), (int)pos.getY(), selectedElement.getRotation());
                            boardDTO.getSimulation().addLogicGate(toggleOn);
                        }
                        else if(selectedElement.getName().equals("BTN")){
                            Vector2D pos = getPosition();
                            removeLogicElement();
                            LogicElement toggleOn = new ButtonLogicElement((int)pos.getX(), (int)pos.getY(), selectedElement.getRotation());
                            boardDTO.getSimulation().addLogicGate(toggleOn);
                        }
                    }
                }.getElementFromMousePosition(mousePos, projection2D);
            }
            else{
                if (event.getButton() == MouseButton.PRIMARY && event.isStillSincePress()) {
                    new MouseCrossingSpecifier(boardDTO.getSimulation()){
                        @Override
                        public void doAction() {
                            if (getCrossing() == Crossing.TOUCHING) updateCrossing(Crossing.NOT_TOUCHING);
                            else updateCrossing(Crossing.TOUCHING);
                        }
                    }.performTransformation(mousePos, projection2D);

                }
                else if(event.getButton() == MouseButton.SECONDARY && event.isStillSincePress()){
                    new MouseLogicElementSpecifier(boardDTO.getSimulation()){
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

            if(event.getButton() == MouseButton.PRIMARY && !isLogicGateLefted.get()){
                new MouseWireSpecifier(boardDTO.getSimulation()){
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
        AtomicBoolean isLogicGateDragged = boardDTO.getIsLogicGateLifted();
        boardDTO.setRelativeMouseToLogicGatePos(new Vector2D(0.5,0));

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
        else if(keycode == KeyCode.DIGIT8){
            isLogicGateDragged.set(true);
            boardDTO.setLogicGateDragged(new ToggleOff(x,y, Rotation.RIGHT));
        }
        else if(keycode == KeyCode.DIGIT9){
            isLogicGateDragged.set(true);
            boardDTO.setLogicGateDragged(new ButtonLogicElement(x,y, Rotation.RIGHT));
        }
    }
}
