package LogicCircuitSimulator.FxGUI.FXMLControllers;

import LogicCircuitSimulator.*;
import LogicCircuitSimulator.FxGUI.DrawSquareLogicElementVisitor;
import LogicCircuitSimulator.FxGUI.GridMouseHandler.CrossingMouseHandler;
import LogicCircuitSimulator.FxGUI.GridMouseHandler.LogicElementHandler;
import LogicCircuitSimulator.FxGUI.SimulationCanvasBackground;
import LogicCircuitSimulator.FxGUI.DrawNodeVisitor;
import LogicCircuitSimulator.FxGUI.GridMouseHandler.WireMouseHandler;
import LogicCircuitSimulator.LogicElements.*;
import LogicCircuitSimulator.Utils.MatrixOperations;
import LogicCircuitSimulator.WireGrid.Node;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.ejml.simple.SimpleMatrix;

import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationCanvasController {
    private final double SCALING_FACTOR = 0.05;
    public static final double MAX_ZOOM = 50;
    public static final double MIN_ZOOM = 5;

    private Vector2D lastMousePressPosition = new Vector2D(0,0);
    private Vector2D lastMousePosition = new Vector2D(0,0);

    private final int TARGET_UPS = 1_000_000;
    private final int TARGET_FPS = 10000;

    private boolean isLogicGateDragged = false;
    private LogicElement logicGateDragged;

    private GraphicsContext graphicsContext;

    private long lastNow;

    private int framesSinceLastFrame;
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

    SimpleMatrix projectionMatrix = MatrixOperations.getProjectionMatrix(0,0, 20);

    @FXML
    public Canvas mainSimulationCanvas;
    public AnchorPane mainSimulationAnchorPane;

    @FXML
    void initialize(){
        graphicsContext = mainSimulationCanvas.getGraphicsContext2D();
        graphicsContext.setLineWidth(1);

        graphicsContext.setFont(new Font(Font.getFontNames().get(0), 15));

        SimulationCanvasBackground background = new SimulationCanvasBackground(mainSimulationCanvas);

        if(syncMode == SyncMode.NOT_SYNCHRONIZED){
            executor.scheduleAtFixedRate(simulationTask, 0, (long) (1.0/TARGET_UPS*1e6), TimeUnit.MICROSECONDS);
        }

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateTitleBar(now);

                resizeCanvasToAnchorPane();
                clearCanvas(Color.BLACK);
                background.draw(projectionMatrix);
                drawLogicGates(simulation.logicElementIterator());
                drawNodes(simulation.nodeIterator());

                if(isLogicGateDragged) {
                    new LogicElementHandler(simulation) {
                        @Override
                        public void transformLogicElement() {
                            logicGateDragged.setPosition(getPosition());
                        }
                    }.performNoTransformation(lastMousePosition, projectionMatrix);

                    LogicElementVisitor drawLogicElement = new DrawSquareLogicElementVisitor(graphicsContext, projectionMatrix);
                    logicGateDragged.accept(drawLogicElement);
                }

                if(syncMode == SyncMode.SYNCHRONIZED){
                    updatesSinceLastFrame.getAndIncrement();
                    simulation.runOnce();
                }
                framesSinceLastFrame++;

                waitUntilNextFrame(now);
            }
        }.start();
    }

    @FXML
    public void OnScroll(ScrollEvent scrollEvent) {
        double currentScale = MatrixOperations.getScaleFromMatrix(projectionMatrix);
        if(scrollEvent.getDeltaY()>0 && currentScale < MAX_ZOOM) {
            projectionMatrix = MatrixOperations.getScalingMatrix(1 + SCALING_FACTOR, lastMousePosition.getX(), lastMousePosition.getY()).mult(projectionMatrix);
        }
        else if(scrollEvent.getDeltaY()<0 && currentScale > MIN_ZOOM) {
            projectionMatrix = MatrixOperations.getScalingMatrix(1 - SCALING_FACTOR, lastMousePosition.getX(), lastMousePosition.getY()).mult(projectionMatrix);
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
            }.performTransformation(lastMousePosition, projectionMatrix);
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
                    isLogicGateDragged = true;
                    removeLogicElement();
                }
            }.performTransformation(new Vector2D(mouseEvent.getX(), mouseEvent.getY()), projectionMatrix);
        }

        new WireMouseHandler(simulation){
            @Override
            public void transformState() {
                if(getWireState() != Node.State.NONE) wireMode = WireMode.REMOVING;
                else wireMode = WireMode.ADDING;
            }
        }.performTransformation(new Vector2D(mouseEvent.getX(), mouseEvent.getY()), projectionMatrix);

    }

    @FXML
    public void onMouseReleased(MouseEvent mouseEvent) {
        Vector2D mousePos = new Vector2D(mouseEvent.getX(), mouseEvent.getY());
        if(isLogicGateDragged){
            new LogicElementHandler(simulation){
                @Override
                public void transformLogicElement() {
                    logicGateDragged.setPosition(getPosition());
                    simulation.addLogicGate(logicGateDragged);
                    isLogicGateDragged = false;
                }
            }.performNoTransformation(mousePos, projectionMatrix);



        }
        else{
            if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.isStillSincePress()) {
                new CrossingMouseHandler(simulation){
                    @Override
                    public void transformCrossing() {
                        if (getCrossing() == Node.WireCrossing.TOUCHING) updateCrossing(Node.WireCrossing.NOT_TOUCHING);
                        else updateCrossing(Node.WireCrossing.TOUCHING);
                    }
                }.performTransformation(mousePos, projectionMatrix);
            }
            else if(mouseEvent.getButton() == MouseButton.SECONDARY && mouseEvent.isStillSincePress()){
                Vector2D nodePos = getNodePositionFromMousePosition(mouseEvent.getX(), mouseEvent.getY(), projectionMatrix);
                removeLogicElement(nodePos, simulation.logicElementIterator());
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

            projectionMatrix = MatrixOperations.getTranslationMatrix(-deltaX, -deltaY).mult(projectionMatrix);
        }

        if(mouseEvent.getButton() == MouseButton.PRIMARY && !isLogicGateDragged){
            new WireMouseHandler(simulation){
                @Override
                public void transformState() {
                    if(wireMode == WireMode.ADDING) this.updateWireState(Node.State.HIGH);
                    else updateWireState(Node.State.NONE);
                }
            }.performTransformation(new Vector2D(mouseEvent.getX(), mouseEvent.getY()), projectionMatrix);
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
    private void clearCanvas(Color color){
        graphicsContext.setFill(color);
        graphicsContext.clearRect(0,0, mainSimulationCanvas.getWidth(), mainSimulationCanvas.getHeight());
        graphicsContext.fillRect(0,0, mainSimulationCanvas.getWidth(), mainSimulationCanvas.getHeight());
    }
    private void resizeCanvasToAnchorPane(){
        mainSimulationCanvas.setHeight(mainSimulationAnchorPane.getHeight());
        mainSimulationCanvas.setWidth(mainSimulationAnchorPane.getWidth());
    }

    private void drawLogicGates(Iterator<LogicElement>  logicElements){
        LogicElementVisitor drawLogicElement = new DrawSquareLogicElementVisitor(graphicsContext, projectionMatrix);
        while(logicElements.hasNext()){
            logicElements.next().accept(drawLogicElement);
        }
    }

    private void drawNodes(Iterator<Node> nodes){
        NodeVisitor drawNode = new DrawNodeVisitor(graphicsContext, projectionMatrix);
        while(nodes.hasNext()){
            nodes.next().accept(drawNode);
        }
    }

    private void updateTitleBar(long now){
        if(now > lastNow + 1e9){
            App.decorateWindowTitle(framesSinceLastFrame, updatesSinceLastFrame.get());
            framesSinceLastFrame = 0;
            updatesSinceLastFrame.set(0);
            lastNow = now;
        }
    }

    private void waitUntilNextFrame(long now){
        try {
            Thread.sleep((long) Math.max(0, ((1e9 / TARGET_FPS) - (System.nanoTime() - now))/1000000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void removeLogicElement(Vector2D pos, Iterator<LogicElement> logicElements){
        while(logicElements.hasNext()){
            LogicElement logicElement = logicElements.next();
            if(logicElement.getPosition().equals(pos)){
                logicElements.remove();
            }
        }
    }

    private LogicElement getLogicElement(Vector2D pos, Iterator<LogicElement> logicElements){
        while(logicElements.hasNext()){
            LogicElement logicElement = logicElements.next();
            if(logicElement.getPosition().equals(pos)){
                return logicElement;
            }
        }
        return null;
    }

    private Vector2D getNodePositionFromMousePosition(double x, double y, SimpleMatrix projectionMatrix){
        Vector2D pos = MatrixOperations.getVectorFromVectorMatrix(projectionMatrix.invert().mult(MatrixOperations.getVectorMatrix(x, y)));
        int nodeX = (int) pos.getX();
        int nodeY = (int) pos.getY();
        return new Vector2D(nodeX, nodeY);
    }

    private void toggleNodeCrossing(Vector2D nodePos){
        Node node = simulation.getNode(nodePos);
        if (node.isTouching() == Node.WireCrossing.TOUCHING) {
            simulation.updateCrossing(nodePos, Node.WireCrossing.NOT_TOUCHING);
        } else simulation.updateCrossing(nodePos, Node.WireCrossing.TOUCHING);
    }

    private void rotateLogicElementClockwise(Vector2D nodePos, Iterator<LogicElement> logicElements){
        while(logicElements.hasNext()){
            LogicElement logicElement = logicElements.next();
            if(logicElement.getPosition().equals(nodePos)){
                if(logicElement.getRotation() == Rotation.RIGHT) logicElement.setRotation(Rotation.DOWN);
                else if(logicElement.getRotation() == Rotation.DOWN) logicElement.setRotation(Rotation.LEFT);
                else if(logicElement.getRotation() == Rotation.LEFT) logicElement.setRotation(Rotation.UP);
                else if(logicElement.getRotation() == Rotation.UP) logicElement.setRotation(Rotation.RIGHT);
            }
        }
    }

    private void createLogicElementAtMouseOnKeyEvent(KeyCode keycode){
        int x = 0;
        int y = 0;
        if(keycode == KeyCode.DIGIT1){
            isLogicGateDragged = true;
            logicGateDragged = new AndGate(x, y, Rotation.RIGHT);
        }
        else if(keycode == KeyCode.DIGIT2){
            isLogicGateDragged = true;
            logicGateDragged = new BufferGate(x, y, Rotation.RIGHT);
        }
        else if(keycode == KeyCode.DIGIT3){
            isLogicGateDragged = true;
            logicGateDragged = new LogicClock(x, y, Rotation.RIGHT);
        }
        else if(keycode == KeyCode.DIGIT4){
            isLogicGateDragged = true;
            logicGateDragged = new LogicOne(x, y, Rotation.RIGHT);
        }
        else if(keycode == KeyCode.DIGIT5){
            isLogicGateDragged = true;
            logicGateDragged = new NotGate(x, y, Rotation.RIGHT);
        }
        else if(keycode == KeyCode.DIGIT6){
            isLogicGateDragged = true;
            logicGateDragged = new OrGate(x, y, Rotation.RIGHT);
        }
        else if(keycode == KeyCode.DIGIT7){
            isLogicGateDragged = true;
            logicGateDragged = new XorGate(x, y, Rotation.RIGHT);
        }
    }

}


