package LogicCircuitSimulator.FxGUI.FXMLControllers;

import LogicCircuitSimulator.FxGUI.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.FxGUI.GraphicalProjection.SimpleMatrixProjection2D;
import LogicCircuitSimulator.LogicElements.LogicElement;
import LogicCircuitSimulator.LogicElements.LogicOne;
import LogicCircuitSimulator.LogicElements.Rotation;
import LogicCircuitSimulator.Simulation;
import LogicCircuitSimulator.Vector2D;
import javafx.scene.canvas.Canvas;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BoardDTO {
    private final double MAX_ZOOM = 50;
    private final double MIN_ZOOM = 5;

    private final int TARGET_FPS = 100;
    private final int TARGET_UPS = 100;
    private final AtomicInteger updatesSinceLastFrame = new AtomicInteger();
    private final AtomicInteger framesSinceLastFrame = new AtomicInteger();

    private final Canvas canvas;
    private final Projection2D projection2D = new SimpleMatrixProjection2D(new Vector2D(0,0), 20);

    private Vector2D lastMousePosition = new Vector2D(0,0);
    private final AtomicBoolean isLogicGateDragged = new AtomicBoolean(false);
    private LogicElement logicGateDragged = new LogicOne(0,0, Rotation.RIGHT);

    private final SyncMode syncMode = SyncMode.NOT_SYNCHRONIZED;
    private final Simulation simulation = new Simulation();

    enum SyncMode{
        SYNCHRONIZED,
        NOT_SYNCHRONIZED
    }

    public BoardDTO(Canvas canvas) {
        this.canvas = canvas;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public double getMAX_ZOOM() {
        return MAX_ZOOM;
    }

    public double getMIN_ZOOM() {
        return MIN_ZOOM;
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

    public SyncMode getSyncMode() {
        return syncMode;
    }

    public Simulation getSimulation() {
        return simulation;
    }
}
