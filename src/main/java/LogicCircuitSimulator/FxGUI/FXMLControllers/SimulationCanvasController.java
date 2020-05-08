package LogicCircuitSimulator.FxGUI.FXMLControllers;

import LogicCircuitSimulator.*;
import LogicCircuitSimulator.FxGUI.DrawingManager;
import LogicCircuitSimulator.FxGUI.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.FxGUI.GraphicalProjection.SimpleMatrixProjection2D;
import LogicCircuitSimulator.FxGUI.GridMouseHandler.CrossingMouseHandler;
import LogicCircuitSimulator.FxGUI.GridMouseHandler.LogicElementHandler;
import LogicCircuitSimulator.FxGUI.GridMouseHandler.WireMouseHandler;
import LogicCircuitSimulator.LogicElements.*;
import LogicCircuitSimulator.WireGrid.Node;
import LogicCircuitSimulator.WireGrid.WireState;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationCanvasController {
    private final double SCALING_FACTOR = 0.05;
    public static final double MAX_ZOOM = 50;
    public static final double MIN_ZOOM = 5;

    public static Vector2D lastMousePressPosition = new Vector2D(0,0);
    public static Vector2D lastMousePosition = new Vector2D(0,0);

    private final int TARGET_UPS = 1_000_000;

    private final AtomicBoolean isLogicGateDragged = new AtomicBoolean(false);
    public static LogicElement logicGateDragged = new LogicOne(0,0, Rotation.RIGHT);

    Projection2D projection2D = new SimpleMatrixProjection2D(new Vector2D(0,0), 20);
    AtomicInteger updatesSinceLastFrame = new AtomicInteger();

    private WireMode wireMode = WireMode.ADDING;
    private final SyncMode syncMode = SyncMode.NOT_SYNCHRONIZED;
    Simulation simulation = new Simulation();

    enum SyncMode{
        SYNCHRONIZED,
        NOT_SYNCHRONIZED
    }
    enum WireMode{
        ADDING,
        REMOVING
    }

    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    Runnable simulationTask = () -> {
        simulation.runOnce();
        updatesSinceLastFrame.getAndIncrement();
    };

    @FXML
    public Canvas mainSimulationCanvas;
    public AnchorPane mainSimulationAnchorPane;

    @FXML
    void initialize(){
        if(syncMode == SyncMode.NOT_SYNCHRONIZED){
            executor.scheduleAtFixedRate(simulationTask, 0, (long) (1.0/TARGET_UPS*1e6), TimeUnit.MICROSECONDS);
        }

        DrawingManager drawingManager = new DrawingManager(mainSimulationCanvas, mainSimulationAnchorPane);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(syncMode == SyncMode.SYNCHRONIZED){
                    simulationTask.run();
                }
                drawingManager.update(lastMousePosition, isLogicGateDragged, logicGateDragged, projection2D, simulation, updatesSinceLastFrame);
                drawingManager.draw(now);
            }
        }.start();
    }

    @FXML
    public void OnScroll(ScrollEvent scrollEvent) {
        double currentScale = projection2D.getScale();
        if(scrollEvent.getDeltaY()>0 && currentScale < MAX_ZOOM) {
            projection2D.scale(1+SCALING_FACTOR, lastMousePosition);
        }
        else if(scrollEvent.getDeltaY()<0 && currentScale > MIN_ZOOM) {
            projection2D.scale(1-SCALING_FACTOR, lastMousePosition);
        }
    }

    @FXML
    public void onKeyReleased(KeyEvent keyEvent) {

        if(keyEvent.getCode() == KeyCode.R){
            new LogicElementHandler(simulation){
                @Override
                public void transformLogicElement() {
                    rotateLogicElementClockwise();
                }
            }.performTransformation(lastMousePosition, projection2D);
        }

        createLogicElementAtMouseOnKeyEvent(keyEvent.getCode());
    }

    @FXML
    public void onMousePressed(MouseEvent mouseEvent) {
        lastMousePressPosition = new Vector2D(mouseEvent.getX(), mouseEvent.getY());

        if(mouseEvent.getButton() == MouseButton.PRIMARY){
            new LogicElementHandler(simulation){
                @Override
                public void transformLogicElement() {
                    logicGateDragged = getLogicElement();
                    isLogicGateDragged.set(true);
                    removeLogicElement();
                }
            }.performTransformation(new Vector2D(mouseEvent.getX(), mouseEvent.getY()), projection2D);
        }

        new WireMouseHandler(simulation){
            @Override
            public void transformState() {
                if(getWireState() != WireState.NONE) wireMode = WireMode.REMOVING;
                else wireMode = WireMode.ADDING;
            }
        }.performTransformation(new Vector2D(mouseEvent.getX(), mouseEvent.getY()), projection2D);
    }

    @FXML
    public void onMouseReleased(MouseEvent mouseEvent) {
        Vector2D mousePos = new Vector2D(mouseEvent.getX(), mouseEvent.getY());
        if(isLogicGateDragged.get()){
            new LogicElementHandler(simulation){
                @Override
                public void transformLogicElement() {
                    logicGateDragged.setPosition(getPosition());
                    simulation.addLogicGate(logicGateDragged);
                    isLogicGateDragged.set(false);
                }
            }.performNoTransformation(mousePos, projection2D);
        }
        else{
            if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.isStillSincePress()) {
                new CrossingMouseHandler(simulation){
                    @Override
                    public void transformCrossing() {
                        if (getCrossing() == Node.WireCrossing.TOUCHING) updateCrossing(Node.WireCrossing.NOT_TOUCHING);
                        else updateCrossing(Node.WireCrossing.TOUCHING);
                    }
                }.performTransformation(mousePos, projection2D);
            }
            else if(mouseEvent.getButton() == MouseButton.SECONDARY && mouseEvent.isStillSincePress()){
                Vector2D nodePos = getNodePositionFromMousePosition(mouseEvent.getX(), mouseEvent.getY());
                simulation.removeLogicElement(nodePos);
            }
        }
    }

    @FXML
    public void onMouseDragged(MouseEvent mouseEvent) {
        lastMousePosition = new Vector2D(mouseEvent.getX(), mouseEvent.getY());

        if(mouseEvent.getButton() == MouseButton.MIDDLE){
            double deltaX = lastMousePressPosition.getX() - mouseEvent.getX();
            double deltaY = lastMousePressPosition.getY() - mouseEvent.getY();

            lastMousePressPosition = new Vector2D(mouseEvent.getX(), mouseEvent.getY());

            projection2D.translate(new Vector2D(-deltaX, -deltaY));
        }

        if(mouseEvent.getButton() == MouseButton.PRIMARY && !isLogicGateDragged.get()){
            new WireMouseHandler(simulation){
                @Override
                public void transformState() {
                    if(wireMode == WireMode.ADDING) this.updateWireState(WireState.HIGH);
                    else updateWireState(WireState.NONE);
                }
            }.performTransformation(new Vector2D(mouseEvent.getX(), mouseEvent.getY()), projection2D);
        }
    }

    @FXML
    public void onMouseMoved(MouseEvent mouseEvent) {
        lastMousePosition = new Vector2D(mouseEvent.getX(), mouseEvent.getY());
    }

    public void shutdown(){
        executor.shutdownNow();
    }

    //Private functions
    private Vector2D getNodePositionFromMousePosition(double x, double y){
        Vector2D pos = projection2D.projectBack(new Vector2D(x,y));
        int nodeX = (int) pos.getX();
        int nodeY = (int) pos.getY();
        return new Vector2D(nodeX, nodeY);
    }

    private void createLogicElementAtMouseOnKeyEvent(KeyCode keycode){
        int x = 0;
        int y = 0;
        if(keycode == KeyCode.DIGIT1){
            isLogicGateDragged.set(true);
            logicGateDragged = new AndGate(x, y, Rotation.RIGHT);
        }
        else if(keycode == KeyCode.DIGIT2){
            isLogicGateDragged.set(true);
            logicGateDragged = new BufferGate(x, y, Rotation.RIGHT);
        }
        else if(keycode == KeyCode.DIGIT3){
            isLogicGateDragged.set(true);
            logicGateDragged = new LogicClock(x, y, Rotation.RIGHT);
        }
        else if(keycode == KeyCode.DIGIT4){
            isLogicGateDragged.set(true);
            logicGateDragged = new LogicOne(x, y, Rotation.RIGHT);
        }
        else if(keycode == KeyCode.DIGIT5){
            isLogicGateDragged.set(true);
            logicGateDragged = new NotGate(x, y, Rotation.RIGHT);
        }
        else if(keycode == KeyCode.DIGIT6){
            isLogicGateDragged.set(true);
            logicGateDragged = new OrGate(x, y, Rotation.RIGHT);
        }
        else if(keycode == KeyCode.DIGIT7){
            isLogicGateDragged.set(true);
            logicGateDragged = new XorGate(x, y, Rotation.RIGHT);
        }
    }

}


