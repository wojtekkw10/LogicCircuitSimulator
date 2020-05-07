package LogicCircuitSimulator.FxGUI.FXMLControllers;

import LogicCircuitSimulator.*;
import LogicCircuitSimulator.FxGUI.DrawSquareLogicElementVisitor;
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
    public static final double MAX_SCALE = 100;
    public static final double MIN_SCALE = 5;

    private double lastMouseX = 0;
    private double lastMouseY = 0;

    private double pivotX = 0;
    private double pivotY = 0;

    private final int TARGET_UPS = 60;
    private final int TARGET_FPS = 70;

    private boolean isLogicGateDragged = false;
    private LogicElement logicGateDragged;

    GraphicsContext graphics;




    enum SyncMode{
        SYNCHRONIZED,
        NOT_SYNCHRONIZED
    }

    enum WireMode{
        ADDING,
        REMOVING
    }
    private WireMode wireMode = WireMode.ADDING;
    private final SyncMode syncMode = SyncMode.NOT_SYNCHRONIZED;
    Simulation simulation = new Simulation();


    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    AtomicInteger ups = new AtomicInteger();
    Runnable periodicTask = () -> {
        simulation.runOnce();
        ups.getAndIncrement();
    };

    SimpleMatrix projectionMatrix = new SimpleMatrix(
            new double[][] {
                    new double[] {20, 0, 0},
                    new double[] {0, 20, 0},
                    new double[] {0 ,0, 1}
            }
    );

    @FXML
    public Canvas mainSimulationCanvas;

    @FXML
    public AnchorPane mainSimulationAnchorPane;

    @FXML
    void initialize(){
        graphics = mainSimulationCanvas.getGraphicsContext2D();
        graphics.setLineWidth(1);

        graphics.setFont(new Font(Font.getFontNames().get(0), 15));
        //Image AND_GATE = new Image("/500px-AND_ANSI.png");

        SimulationCanvasBackground background = new SimulationCanvasBackground();

        if(syncMode == SyncMode.NOT_SYNCHRONIZED){
            executor.scheduleAtFixedRate(periodicTask, 0, (long) (1.0/TARGET_UPS*1e6), TimeUnit.MICROSECONDS);
        }


        new AnimationTimer() {
            private short frames = 0;
            long lastNow = 0;

            @Override
            public void handle(long now) {
                //WINDOW TiTLE UPDATE
                if(now > lastNow + 1e9){
                    App.decorateWindowTitle(frames, ups.get());
                    frames = 0;
                    ups.set(0);
                    lastNow = now;
                }

                //SETUP
                graphics.setStroke(Color.GREY);
                mainSimulationCanvas.setHeight(mainSimulationAnchorPane.getHeight());
                mainSimulationCanvas.setWidth(mainSimulationAnchorPane.getWidth());
                graphics.clearRect(0,0, mainSimulationCanvas.getWidth(), mainSimulationCanvas.getHeight());

                //BACKGROUND
                background.draw(graphics, mainSimulationCanvas.getWidth(), mainSimulationCanvas.getHeight(), projectionMatrix);

                //DRAWING LOGIC GATES
                Iterator<LogicElement> logicElements = simulation.logicElementIterator();
                LogicElementVisitor drawLogicElement = new DrawSquareLogicElementVisitor(graphics, projectionMatrix);
                while(logicElements.hasNext()){
                    logicElements.next().accept(drawLogicElement);
                }

                //DRAWING NODES
                Iterator<Node> nodes = simulation.nodeIterator();
                NodeVisitor drawNode = new DrawNodeVisitor(graphics, projectionMatrix);

                while(nodes.hasNext()){
                    nodes.next().accept(drawNode);
                }

                if(isLogicGateDragged) graphics.strokeRect(pivotX, pivotY, 30, 30);



                if(syncMode == SyncMode.SYNCHRONIZED){
                    ups.getAndIncrement();
                    simulation.runOnce();
                }
                frames++;

                try {
                    Thread.sleep((long) Math.max(0, ((1e9 / TARGET_FPS) - (System.nanoTime() - now))/1000000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @FXML
    public void OnScroll(ScrollEvent scrollEvent) {
        double currentScale = MatrixOperations.getScaleFromMatrix(projectionMatrix);
        if(scrollEvent.getDeltaY()>0 && currentScale < MAX_SCALE) {
            projectionMatrix = MatrixOperations.getScalingMatrix(1 + SCALING_FACTOR, pivotX, pivotY).mult(projectionMatrix);
        }
        else if(scrollEvent.getDeltaY()<0 && currentScale > MIN_SCALE) {
            projectionMatrix = MatrixOperations.getScalingMatrix(1 - SCALING_FACTOR, pivotX, pivotY).mult(projectionMatrix);
        }
    }

    @FXML
    public void onMouseClicked(MouseEvent mouseEvent) {

    }

    public void onKeyReleased(KeyEvent keyEvent) {
        System.out.println("KEY EVENT");
        Vector2D pos = MatrixOperations.getVectorFromVectorMatrix(projectionMatrix.invert().mult(MatrixOperations.getVectorMatrix(pivotX, pivotY)));
        int x = (int) pos.getX();
        int y = (int) pos.getY();
        if(x<0) x--;
        if(y<0) y--;
        pos = new Vector2D(x, y);



        System.out.println(pos);
        if(keyEvent.getCode() == KeyCode.R){
            Iterator<LogicElement> logicElements = simulation.logicElementIterator();
            while(logicElements.hasNext()){
                LogicElement logicElement = logicElements.next();
                if(logicElement.getPosition().equals(pos)){
                    if(logicElement.getRotation() == Rotation.RIGHT) logicElement.setRotation(Rotation.DOWN);
                    else if(logicElement.getRotation() == Rotation.DOWN) logicElement.setRotation(Rotation.LEFT);
                    else if(logicElement.getRotation() == Rotation.LEFT) logicElement.setRotation(Rotation.UP);
                    else if(logicElement.getRotation() == Rotation.UP) logicElement.setRotation(Rotation.RIGHT);
                }

            }
        }
        else if(keyEvent.getCode() == KeyCode.DIGIT1){
            isLogicGateDragged = true;
            logicGateDragged = new AndGate(x, y, Rotation.RIGHT);
        }
        else if(keyEvent.getCode() == KeyCode.DIGIT2){
            isLogicGateDragged = true;
            logicGateDragged = new BufferGate(x, y, Rotation.RIGHT);
        }
        else if(keyEvent.getCode() == KeyCode.DIGIT3){
            isLogicGateDragged = true;
            logicGateDragged = new LogicClock(x, y, Rotation.RIGHT);
        }
        else if(keyEvent.getCode() == KeyCode.DIGIT4){
            isLogicGateDragged = true;
            logicGateDragged = new LogicOne(x, y, Rotation.RIGHT);
        }
        else if(keyEvent.getCode() == KeyCode.DIGIT5){
            isLogicGateDragged = true;
            logicGateDragged = new NotGate(x, y, Rotation.RIGHT);
        }
        else if(keyEvent.getCode() == KeyCode.DIGIT6){
            isLogicGateDragged = true;
            logicGateDragged = new OrGate(x, y, Rotation.RIGHT);
        }
        else if(keyEvent.getCode() == KeyCode.DIGIT7){
            isLogicGateDragged = true;
            logicGateDragged = new XorGate(x, y, Rotation.RIGHT);
        }
    }

    @FXML
    public void onMousePressed(MouseEvent mouseEvent) {
        lastMouseX = mouseEvent.getX();
        lastMouseY = mouseEvent.getY();

        Vector2D pos = MatrixOperations.getVectorFromVectorMatrix(projectionMatrix.invert().mult(MatrixOperations.getVectorMatrix(mouseEvent.getX(), mouseEvent.getY())));
        new WireMouseHandler(simulation){
            @Override
            public void doAction() {
                if(getWireState() != Node.State.NONE) wireMode = WireMode.REMOVING;
                else wireMode = WireMode.ADDING;
            }
        }.performFunction(pos);

    }

    @FXML
    public void onDragDetected(MouseEvent mouseEvent) {
        Vector2D pos = MatrixOperations.getVectorFromVectorMatrix(projectionMatrix.invert().mult(MatrixOperations.getVectorMatrix(pivotX, pivotY)));
        int x = (int) pos.getX();
        int y = (int) pos.getY();
        //if(x<=0) x--;
        //if(y<=0) y--;
        pos = new Vector2D(x, y);

        if(mouseEvent.getButton() == MouseButton.PRIMARY){
            System.out.println(pos);
            Iterator<LogicElement> logicElements = simulation.logicElementIterator();
            while(logicElements.hasNext()){
                LogicElement logicElement = logicElements.next();
                if(logicElement.getPosition().equals(pos)){
                    logicGateDragged = logicElement;
                    isLogicGateDragged = true;
                    logicElements.remove();
                }

            }
        }
    }

    public void onMouseReleased(MouseEvent mouseEvent) {
        if(isLogicGateDragged){
            Vector2D pos = MatrixOperations.getVectorFromVectorMatrix(projectionMatrix.invert().mult(MatrixOperations.getVectorMatrix(pivotX, pivotY)));
            int x = (int) pos.getX();
            int y = (int) pos.getY();
            System.out.println("RELEASED AT "+x+" "+y);
            logicGateDragged.setPosition(new Vector2D(x,y));
            simulation.addLogicGate(logicGateDragged);
            isLogicGateDragged = false;
        }
        else{
            if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.isStillSincePress()) {
                Vector2D pos = MatrixOperations.getVectorFromVectorMatrix(projectionMatrix.invert().mult(MatrixOperations.getVectorMatrix(mouseEvent.getX(), mouseEvent.getY())));
                int x = (int) pos.getX();
                int y = (int) pos.getY();
                Vector2D nodePos = new Vector2D(x, y);

                Node node = simulation.getNode(nodePos);
                if (node.isTouching() == Node.WireCrossing.TOUCHING) {
                    simulation.updateCrossing(nodePos, Node.WireCrossing.NOT_TOUCHING);
                } else simulation.updateCrossing(nodePos, Node.WireCrossing.TOUCHING);

            }
            else if(mouseEvent.getButton() == MouseButton.SECONDARY && mouseEvent.isStillSincePress()){
                Vector2D pos = MatrixOperations.getVectorFromVectorMatrix(projectionMatrix.invert().mult(MatrixOperations.getVectorMatrix(mouseEvent.getX(), mouseEvent.getY())));
                int x = (int) pos.getX();
                int y = (int) pos.getY();
                Vector2D nodePos = new Vector2D(x, y);

                Iterator<LogicElement> logicElements = simulation.logicElementIterator();
                while(logicElements.hasNext()){
                    LogicElement logicElement = logicElements.next();
                    if(logicElement.getPosition().equals(nodePos)){
                        logicElements.remove();
                    }

                }
            }
        }


    }

    public void onDragOver(DragEvent dragEvent) {

    }

    public void onDragDropped(DragEvent dragEvent) {

    }

    public void onMouseDragOver(MouseDragEvent mouseDragEvent) {
        System.out.println("ON MOUSE DRAGGED");

    }

    @FXML
    public void onMouseDragged(MouseEvent mouseEvent) {
        pivotX = mouseEvent.getX();
        pivotY = mouseEvent.getY();

        if(mouseEvent.getButton() == MouseButton.MIDDLE){

            double deltaX = lastMouseX - mouseEvent.getX();
            double deltaY = lastMouseY - mouseEvent.getY();

            lastMouseX = mouseEvent.getX();
            lastMouseY = mouseEvent.getY();

            projectionMatrix = MatrixOperations.getTranslationMatrix(-deltaX, -deltaY).mult(projectionMatrix);
        }

        if(mouseEvent.getButton() == MouseButton.PRIMARY && !isLogicGateDragged){
            Vector2D pos = MatrixOperations.getVectorFromVectorMatrix(projectionMatrix.invert().mult(MatrixOperations.getVectorMatrix(mouseEvent.getX(), mouseEvent.getY())));

            new WireMouseHandler(simulation){
                @Override
                public void doAction() {
                    if(wireMode == WireMode.ADDING) updateWireState(Node.State.HIGH);
                    else updateWireState(Node.State.NONE);
                }
            }.performFunction(pos);
        }
    }

    @FXML
    public void onMouseMoved(MouseEvent mouseEvent) {
        pivotX = mouseEvent.getX();
        pivotY = mouseEvent.getY();
    }

    public void shutdown(){
        executor.shutdownNow();
        try {
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


