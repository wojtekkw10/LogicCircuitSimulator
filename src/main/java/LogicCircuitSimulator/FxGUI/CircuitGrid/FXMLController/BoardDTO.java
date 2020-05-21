package LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController;

import LogicCircuitSimulator.FxGUI.CircuitGrid.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.FxGUI.CircuitGrid.GraphicalProjection.SimpleMatrixProjection2D;
import LogicCircuitSimulator.Simulation.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation.LogicElements.LogicOne;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Simulation.Simulation;
import LogicCircuitSimulator.Vector2D;
import javafx.scene.canvas.Canvas;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BoardDTO {
    private final double MAX_ZOOM = 50;
    private final double MIN_ZOOM = 5;

    private final int TARGET_FPS = 100;
    private int TARGET_UPS = 100;
    private final AtomicInteger updatesSinceLastFrame = new AtomicInteger();
    private final AtomicInteger framesSinceLastFrame = new AtomicInteger();

    private final Canvas canvas;
    private final Projection2D projection2D = new SimpleMatrixProjection2D(new Vector2D(0,0), 20);

    private Vector2D lastMousePosition = new Vector2D(0,0);
    private final AtomicBoolean isLogicGateDragged = new AtomicBoolean(false);
    private LogicElement logicGateDragged = new LogicOne(0,0, Rotation.RIGHT);



    private Vector2D relativeMouseToLogicGatePos = new Vector2D(0,0);

    private final SyncMode syncMode = SyncMode.NOT_SYNCHRONIZED;
    private final Simulation simulation = new Simulation();

    private boolean shouldDrawSpeedStats = true;
    private boolean isUpsLimited = true;
    private boolean upsChanged = false;

    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    enum SyncMode{
        SYNCHRONIZED,
        NOT_SYNCHRONIZED
    }
    public Vector2D getRelativeMouseToLogicGatePos() {
        return relativeMouseToLogicGatePos;
    }

    public void setRelativeMouseToLogicGatePos(Vector2D relativeMouseToLogicGatePos) {
        this.relativeMouseToLogicGatePos = relativeMouseToLogicGatePos;
    }

    public ScheduledExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    public BoardDTO(Canvas canvas) {
        this.canvas = canvas;
        this.simulation.initTestSimulation();
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

    public void setTARGET_UPS(int TARGET_UPS) {
        this.TARGET_UPS = TARGET_UPS;
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

    public boolean shouldDrawSpeedStats() {
        return shouldDrawSpeedStats;
    }

    public void setShouldDrawSpeedStats(boolean shouldDrawSpeedStats) {
        this.shouldDrawSpeedStats = shouldDrawSpeedStats;
    }

    public boolean isUpsLimited() {
        return isUpsLimited;
    }

    public void setUpsLimited(boolean upsLimited) {
        isUpsLimited = upsLimited;
    }

    public boolean isUpsChanged() {
        return upsChanged;
    }

    public void setUpsChanged(boolean upsChanged) {
        this.upsChanged = upsChanged;
    }
}