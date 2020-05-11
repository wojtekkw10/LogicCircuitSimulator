package LogicCircuitSimulator.FxGUI.FXMLControllers;

import LogicCircuitSimulator.FxGUI.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.FxGUI.GraphicalProjection.SimpleMatrixProjection2D;
import LogicCircuitSimulator.LogicElements.LogicElement;
import LogicCircuitSimulator.LogicElements.LogicOne;
import LogicCircuitSimulator.LogicElements.Rotation;
import LogicCircuitSimulator.Simulation;
import LogicCircuitSimulator.Vector2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import org.checkerframework.checker.units.qual.A;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BoardDTO {
    private final double SCALING_FACTOR = 0.05;

    private final AnchorPane anchorPane;
    private final Canvas canvas;
    private final GraphicsContext graphicsContext;

    private final double MAX_ZOOM = 50;
    private final double MIN_ZOOM = 5;

    private Vector2D lastMousePressPosition = new Vector2D(0,0);
    private Vector2D lastMousePosition = new Vector2D(0,0);

    private final int TARGET_FPS = 100;
    private final int TARGET_UPS = 100;

    private final AtomicBoolean isLogicGateDragged = new AtomicBoolean(false);
    private LogicElement logicGateDragged = new LogicOne(0,0, Rotation.RIGHT);

    private final Projection2D projection2D = new SimpleMatrixProjection2D(new Vector2D(0,0), 20);

    private final AtomicInteger updatesSinceLastFrame = new AtomicInteger();
    private final AtomicInteger framesSinceLastFrame = new AtomicInteger();

    private  final WireMode wireMode = WireMode.ADDING;
    private final SyncMode syncMode = SyncMode.NOT_SYNCHRONIZED;
    private final Simulation simulation = new Simulation();

    enum SyncMode{
        SYNCHRONIZED,
        NOT_SYNCHRONIZED
    }
    public enum WireMode{
        ADDING,
        REMOVING
    }

    public BoardDTO(AnchorPane anchorPane, Canvas canvas) {
        this.anchorPane = anchorPane;
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
    }

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final Runnable simulationTask = () -> {
        simulation.runOnce();
        updatesSinceLastFrame.getAndIncrement();
    };

    private final AtomicLong lastNow = new AtomicLong(0);

    public double getSCALING_FACTOR() {
        return SCALING_FACTOR;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    public double getMAX_ZOOM() {
        return MAX_ZOOM;
    }

    public double getMIN_ZOOM() {
        return MIN_ZOOM;
    }

    public Vector2D getLastMousePressPosition() {
        return lastMousePressPosition;
    }

    public void setLastMousePressPosition(Vector2D lastMousePressPosition) {
        this.lastMousePressPosition = lastMousePressPosition;
    }

    public Vector2D getLastMousePosition() {
        return lastMousePosition;
    }

    public void setLastMousePosition(Vector2D lastMousePosition) {
        this.lastMousePosition = lastMousePosition;
    }

    public int getTARGET_FPS() {
        return TARGET_FPS;
    }

    public int getTARGET_UPS() {
        return TARGET_UPS;
    }

    public AtomicBoolean getIsLogicGateDragged() {
        return isLogicGateDragged;
    }

    public LogicElement getLogicGateDragged() {
        return logicGateDragged;
    }

    public void setLogicGateDragged(LogicElement logicGateDragged) {
        this.logicGateDragged = logicGateDragged;
    }

    public Projection2D getProjection2D() {
        return projection2D;
    }

    public AtomicInteger getUpdatesSinceLastFrame() {
        return updatesSinceLastFrame;
    }

    public AtomicInteger getFramesSinceLastFrame() {
        return framesSinceLastFrame;
    }

    public WireMode getWireMode() {
        return wireMode;
    }


    public SyncMode getSyncMode() {
        return syncMode;
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public ScheduledExecutorService getExecutor() {
        return executor;
    }

    public Runnable getSimulationTask() {
        return simulationTask;
    }

    public AtomicLong getLastNow() {
        return lastNow;
    }
}
