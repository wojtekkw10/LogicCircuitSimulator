package LogicCircuitSimulator.FxGUI.FXMLControllers;

import LogicCircuitSimulator.*;
import LogicCircuitSimulator.FxGUI.DrawSquareLogicElementVisitor;
import LogicCircuitSimulator.FxGUI.SimulationCanvasBackground;
import LogicCircuitSimulator.FxGUI.DrawNodeVisitor;
import LogicCircuitSimulator.FxGUI.GridMouseHandler.WireMouseHandler;
import LogicCircuitSimulator.LogicElements.LogicElement;
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
        GraphicsContext graphics = mainSimulationCanvas.getGraphicsContext2D();
        graphics.setLineWidth(1);

        graphics.setFont(new Font(Font.getFontNames().get(0), 15));
        //Image AND_GATE = new Image("/500px-AND_ANSI.png");

        SimulationCanvasBackground background = new SimulationCanvasBackground();

        if(syncMode == SyncMode.NOT_SYNCHRONIZED){
            executor.scheduleAtFixedRate(periodicTask, 0, (long) (1.0/TARGET_UPS*1e6), TimeUnit.MICROSECONDS);
            executor.submit(periodicTask);
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

                //DRAWING NODES
                Iterator<Node> nodes = simulation.nodeIterator();
                NodeVisitor drawNode = new DrawNodeVisitor(graphics, projectionMatrix);
                while(nodes.hasNext()){
                    nodes.next().accept(drawNode);
                }

                //DRAWING LOGIC GATES
                Iterator<LogicElement> logicElements = simulation.logicElementIterator();
                LogicElementVisitor drawLogicElement = new DrawSquareLogicElementVisitor(graphics, projectionMatrix);
                while(logicElements.hasNext()){
                    logicElements.next().accept(drawLogicElement);
                }

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
    }

    @FXML
    public void onMousePressed(MouseEvent mouseEvent) {
        lastMouseX = mouseEvent.getX();
        lastMouseY = mouseEvent.getY();
        Vector2D pos = MatrixOperations.getVectorFromVectorMatrix(projectionMatrix.invert().mult(MatrixOperations.getVectorMatrix(mouseEvent.getX(), mouseEvent.getY())));
        new WireMouseHandler(){
            public void upperTriangle(){
                if(simulation.getNode(new Vector2D(getX(),getY())).getRightWire() != Node.State.NONE) wireMode = WireMode.REMOVING;
                else wireMode = WireMode.ADDING;
            }
            @Override
            public void lowerTriangle() {
                if(simulation.getNode(new Vector2D(getX(),getY()+1)).getRightWire() != Node.State.NONE) wireMode = WireMode.REMOVING;
                else wireMode = WireMode.ADDING;
            }
            @Override
            public void leftTriangle() {
                if(simulation.getNode(new Vector2D(getX(),getY())).getDownWire() != Node.State.NONE) wireMode = WireMode.REMOVING;
                else wireMode = WireMode.ADDING;
            }
            @Override
            public void rightTriangle() {
                if(simulation.getNode(new Vector2D(getX()+1,getY())).getDownWire() != Node.State.NONE) wireMode = WireMode.REMOVING;
                else wireMode = WireMode.ADDING;
            }
        }.performFunction(pos);

    }

    @FXML
    public void onDragDetected(MouseEvent mouseEvent) {

    }

    @FXML
    public void onMouseDragged(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseButton.MIDDLE){

            double deltaX = lastMouseX - mouseEvent.getX();
            double deltaY = lastMouseY - mouseEvent.getY();

            lastMouseX = mouseEvent.getX();
            lastMouseY = mouseEvent.getY();

            projectionMatrix = MatrixOperations.getTranslationMatrix(-deltaX, -deltaY).mult(projectionMatrix);
        }

        if(mouseEvent.getButton() == MouseButton.PRIMARY){
            System.out.println(wireMode);
            Vector2D pos = MatrixOperations.getVectorFromVectorMatrix(projectionMatrix.invert().mult(MatrixOperations.getVectorMatrix(mouseEvent.getX(), mouseEvent.getY())));

            new WireMouseHandler(){
                public void upperTriangle(){
                    if(wireMode == WireMode.ADDING) simulation.updateWire(new Vector2D(getX(),getY()), Orientation.HORIZONTALLY, Node.State.HIGH);
                    else simulation.updateWire(new Vector2D(getX(),getY()), Orientation.HORIZONTALLY, Node.State.NONE);
                }
                @Override
                public void lowerTriangle() {
                    if(wireMode == WireMode.ADDING) simulation.updateWire(new Vector2D(getX(),getY()+1), Orientation.HORIZONTALLY, Node.State.HIGH);
                    else simulation.updateWire(new Vector2D(getX(),getY()+1), Orientation.HORIZONTALLY, Node.State.NONE);
                }
                @Override
                public void leftTriangle() {
                    if(wireMode == WireMode.ADDING) simulation.updateWire(new Vector2D(getX(),getY()), Orientation.VERTICALLY, Node.State.HIGH);
                    else simulation.updateWire(new Vector2D(getX(),getY()), Orientation.VERTICALLY, Node.State.NONE);
                }
                @Override
                public void rightTriangle() {
                    if(wireMode == WireMode.ADDING) simulation.updateWire(new Vector2D(getX()+1,getY()), Orientation.VERTICALLY, Node.State.HIGH);
                    else simulation.updateWire(new Vector2D(getX()+1,getY()), Orientation.VERTICALLY, Node.State.NONE);
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

    boolean isInUpperTriangle(double xFraction, double yFraction){
        return xFraction > yFraction && xFraction < 1 - yFraction;
    }

    boolean isInLowerTriangle(double xFraction, double yFraction){
        return xFraction < yFraction && xFraction > 1 - yFraction;
    }

    boolean isInLeftTriangle(double xFraction, double yFraction){
        return xFraction < yFraction && xFraction < 1 - yFraction;
    }

    boolean isInRightTriangle(double xFraction, double yFraction){
        return xFraction > yFraction && xFraction > 1 - yFraction;
    }

    void toggleWire(Vector2D pos, Orientation orientation){
        if(orientation == Orientation.HORIZONTALLY){
            if(wireMode == WireMode.ADDING){
                simulation.updateWire(pos, Orientation.HORIZONTALLY, Node.State.HIGH);
            }
            else{
                simulation.updateWire(pos, Orientation.HORIZONTALLY, Node.State.NONE);
            }
        }
        else{
            if(wireMode == WireMode.ADDING){
                simulation.updateWire(pos, Orientation.VERTICALLY, Node.State.HIGH);
            }
            else{
                simulation.updateWire(pos, Orientation.VERTICALLY, Node.State.NONE);
            }
        }

    }



}
