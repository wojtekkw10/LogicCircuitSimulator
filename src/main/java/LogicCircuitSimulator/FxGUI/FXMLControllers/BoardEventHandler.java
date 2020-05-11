package LogicCircuitSimulator.FxGUI.FXMLControllers;

import LogicCircuitSimulator.FxGUI.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.FxGUI.GridMouseHandler.CrossingMouseHandler;
import LogicCircuitSimulator.FxGUI.GridMouseHandler.LogicElementMouseHandler;
import LogicCircuitSimulator.FxGUI.GridMouseHandler.WireMouseHandler;
import LogicCircuitSimulator.LogicElements.*;
import LogicCircuitSimulator.NodeHandler.Node;
import LogicCircuitSimulator.NodeHandler.WireState;
import LogicCircuitSimulator.Simulation;
import LogicCircuitSimulator.Vector2D;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.*;

import java.util.concurrent.atomic.AtomicBoolean;

public class BoardEventHandler {
    private final BoardDTO boardDTO;
    private Vector2D lastMousePosition;

    public BoardEventHandler(BoardDTO boardDTO) {
        this.boardDTO = boardDTO;
        this.lastMousePosition = boardDTO.getLastMousePosition();

        initialize();
    }

    private void initialize(){
        Canvas canvas = boardDTO.getCanvas();
        Projection2D projection2D = boardDTO.getProjection2D();
        Simulation simulation = boardDTO.getSimulation();
        AtomicBoolean isLogicGateDragged = boardDTO.getIsLogicGateDragged();
        double MAX_ZOOM = boardDTO.getMAX_ZOOM();
        double MIN_ZOOM = boardDTO.getMIN_ZOOM();
        double SCALING_FACTOR = boardDTO.getSCALING_FACTOR();
        final BoardDTO.WireMode[] wireMode = {boardDTO.getWireMode()};

        // ON SCROLL
        EventHandler<ScrollEvent> onScrollEventHandler = event -> {
            double currentScale = projection2D.getScale();
            if(event.getDeltaY()>0 && currentScale < MAX_ZOOM) {
                projection2D.scale(1+SCALING_FACTOR, lastMousePosition);
            }
            else if(event.getDeltaY()<0 && currentScale > MIN_ZOOM) {
                projection2D.scale(1-SCALING_FACTOR, lastMousePosition);
            }
        };
        canvas.addEventFilter(ScrollEvent.SCROLL, onScrollEventHandler);

        //ON KEY RELEASED
        EventHandler<KeyEvent> onKeyReleasedEventHandler = event -> {
            if(event.getCode() == KeyCode.R){
                new LogicElementMouseHandler(simulation){
                    @Override
                    public void transformLogicElement() {
                        rotateLogicElementClockwise();
                    }
                }.performTransformation(lastMousePosition, projection2D);
            }
            createLogicElementAtMouseOnKeyEvent(event.getCode());
        };
        canvas.addEventFilter(KeyEvent.KEY_RELEASED, onKeyReleasedEventHandler);

        //ON MOUSE PRESSED
        EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
            boardDTO.setLastMousePosition(new Vector2D(event.getX(), event.getY()));

            if(event.getButton() == MouseButton.PRIMARY){
                new LogicElementMouseHandler(simulation){
                    @Override
                    public void transformLogicElement() {
                        boardDTO.setLogicGateDragged(getLogicElement());
                        boardDTO.getLogicGateDragged().setPosition(getPosition());
                        isLogicGateDragged.set(true);
                        removeLogicElement();
                    }
                }.performTransformation(new Vector2D(event.getX(), event.getY()), projection2D);
            }

            new WireMouseHandler(simulation){
                @Override
                public void transformState() {
                    if(getWireState() != WireState.NONE) wireMode[0] = BoardDTO.WireMode.REMOVING;
                    else wireMode[0] = BoardDTO.WireMode.ADDING;
                }
            }.performTransformation(new Vector2D(event.getX(), event.getY()), projection2D);
        };
        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);

        //ON MOUSE RELEASED
        EventHandler<MouseEvent> onMouseReleasedEventHandler = event -> {
            Vector2D mousePos = new Vector2D(event.getX(), event.getY());
            if(isLogicGateDragged.get()){
                new LogicElementMouseHandler(simulation){
                    @Override
                    public void transformLogicElement() {
                        boardDTO.getLogicGateDragged().setPosition(getPosition());
                        simulation.addLogicGate(boardDTO.getLogicGateDragged());
                        isLogicGateDragged.set(false);
                    }
                }.performNoTransformation(mousePos, projection2D);
            }
            else{
                if (event.getButton() == MouseButton.PRIMARY && event.isStillSincePress()) {
                    new CrossingMouseHandler(simulation){
                        @Override
                        public void transformCrossing() {
                            if (getCrossing() == Node.WireCrossing.TOUCHING) updateCrossing(Node.WireCrossing.NOT_TOUCHING);
                            else updateCrossing(Node.WireCrossing.TOUCHING);
                        }
                    }.performTransformation(mousePos, projection2D);
                }
                else if(event.getButton() == MouseButton.SECONDARY && event.isStillSincePress()){
                    Vector2D pos = projection2D.projectBack(new Vector2D(event.getX(), event.getY()));
                    int nodeX = (int) pos.getX();
                    int nodeY = (int) pos.getY();
                    Vector2D nodePos = new Vector2D(nodeX, nodeY);
                    simulation.removeLogicElement(nodePos);
                }
            }
        };
        canvas.addEventFilter(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

        //ON MOUSE DRAGGED
        EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
            boardDTO.setLastMousePosition(new Vector2D(event.getX(), event.getY()));

            if(event.getButton() == MouseButton.MIDDLE){
                double deltaX = boardDTO.getLastMousePressPosition().getX() - event.getX();
                double deltaY = boardDTO.getLastMousePressPosition().getY() - event.getY();

                boardDTO.setLastMousePressPosition(new Vector2D(event.getX(), event.getY()));

                projection2D.translate(new Vector2D(-deltaX, -deltaY));
            }

            if(event.getButton() == MouseButton.PRIMARY && !isLogicGateDragged.get()){
                new WireMouseHandler(simulation){
                    @Override
                    public void transformState() {
                        if(wireMode[0] == BoardDTO.WireMode.ADDING) this.updateWireState(WireState.HIGH);
                        else updateWireState(WireState.NONE);
                    }
                }.performTransformation(new Vector2D(event.getX(), event.getY()), projection2D);
            }
        };
        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);

        //OM MOUSE MOVED
        EventHandler<MouseEvent> onMouseMovedEventHandler = event -> {
            lastMousePosition = new Vector2D(event.getX(), event.getY());
        };
        canvas.addEventFilter(MouseEvent.MOUSE_MOVED, onMouseMovedEventHandler);
    }

    private void createLogicElementAtMouseOnKeyEvent(KeyCode keycode){
        AtomicBoolean isLogicGateDragged = boardDTO.getIsLogicGateDragged();
        final LogicElement[] logicGateDragged = {boardDTO.getLogicGateDragged()};

        int x = 0;
        int y = 0;
        if(keycode == KeyCode.DIGIT1){
            isLogicGateDragged.set(true);
            logicGateDragged[0] = new AndGate(x,y, Rotation.RIGHT);
        }
        else if(keycode == KeyCode.DIGIT2){
            isLogicGateDragged.set(true);
            logicGateDragged[0] = new BufferGate(x, y, Rotation.RIGHT);
        }
        else if(keycode == KeyCode.DIGIT3){
            isLogicGateDragged.set(true);
            logicGateDragged[0] = new LogicClock(x, y, Rotation.RIGHT);
        }
        else if(keycode == KeyCode.DIGIT4){
            isLogicGateDragged.set(true);
            logicGateDragged[0] = new LogicOne(x, y, Rotation.RIGHT);
        }
        else if(keycode == KeyCode.DIGIT5){
            isLogicGateDragged.set(true);
            logicGateDragged[0] = new NotGate(x, y, Rotation.RIGHT);
        }
        else if(keycode == KeyCode.DIGIT6){
            isLogicGateDragged.set(true);
            logicGateDragged[0] = new OrGate(x, y, Rotation.RIGHT);
        }
        else if(keycode == KeyCode.DIGIT7){
            isLogicGateDragged.set(true);
            logicGateDragged[0] = new XorGate(x, y, Rotation.RIGHT);
        }
    }
}
