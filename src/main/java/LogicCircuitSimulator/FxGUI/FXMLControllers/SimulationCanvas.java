package LogicCircuitSimulator.FxGUI.FXMLControllers;

import LogicCircuitSimulator.App;
import LogicCircuitSimulator.FxGUI.DrawSquareLogicElementVisitor;
import LogicCircuitSimulator.LogicElementVisitor;
import LogicCircuitSimulator.FxGUI.MainCanvasBackground;
import LogicCircuitSimulator.FxGUI.DrawNodeVisitor;
import LogicCircuitSimulator.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation;
import LogicCircuitSimulator.Utils.MatrixOperations;
import LogicCircuitSimulator.WireGrid.Node;
import LogicCircuitSimulator.NodeVisitor;
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

public class SimulationCanvas {
    private final double SCALING_FACTOR = 0.05;
    public static final double MAX_SCALE = 100;
    public static final double MIN_SCALE = 5;

    private double lastMouseX = 0;
    private double lastMouseY = 0;

    private double mouseScalingCenterX = 0;
    private double mouseScalingCenterY = 0;

    private int TARGET_FPS = 60;

    private SyncMode syncMode = SyncMode.SYNCHRONIZED;

    enum SyncMode{
        SYNCHRONIZED,
        NOT_SYNCHRONIZED
    }

    private double pivotX = 0;
    private double pivotY = 0;

    private long lastNow = 0;

    Simulation simulation = new Simulation();

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

        MainCanvasBackground background = new MainCanvasBackground();

        new AnimationTimer() {
            private short frames = 0;
            private int ups = 0;
            long lastNow = 0;

            @Override
            public void handle(long now) {
                //SETUP
                graphics.setStroke(Color.GREY);
                mainSimulationCanvas.setHeight(mainSimulationAnchorPane.getHeight());
                mainSimulationCanvas.setWidth(mainSimulationAnchorPane.getWidth());
                graphics.clearRect(0,0, mainSimulationCanvas.getWidth(), mainSimulationCanvas.getHeight());

                //BACKGROUND
                background.draw(graphics, mainSimulationCanvas.getWidth(), mainSimulationCanvas.getHeight(), projectionMatrix);

                if(now > lastNow + 1e9){
                    App.decorateWindowTitle(frames, ups);
                    frames = 0;
                    ups = 0;
                    lastNow = now;
                }

                for(int i = 0; i<5000; i++){
                    //graphics.strokeLine(100, 100, 120, 200);
                }
                for(int i = 0; i<3000; i++){
                    //graphics.drawImage(AND_GATE, 100, 100, 40, 20);
                }

                //DRAWING NODES
                Iterator<Node> nodes = simulation.nodeIterator();
                NodeVisitor drawNode = new DrawNodeVisitor(graphics, projectionMatrix);
                while(nodes.hasNext()){
                    Node node = nodes.next();
                    node.accept(drawNode);
                }

                //DRAWING LOGIC GATES
                Iterator<LogicElement> logicElements = simulation.logicElementIterator();
                LogicElementVisitor drawLogicElement = new DrawSquareLogicElementVisitor(graphics, projectionMatrix);
                while(logicElements.hasNext()){
                    LogicElement logicElement = logicElements.next();
                    logicElement.accept(drawLogicElement);
                }

                if(syncMode == SyncMode.SYNCHRONIZED){
                    simulation.runOnce();
                    ups++;
                    try {
                        Thread.sleep((long) Math.max(0, ((1e9 / TARGET_FPS) - (System.nanoTime() - now))/1000000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else if(syncMode == SyncMode.NOT_SYNCHRONIZED){
                    while(System.nanoTime() < now+(1e9/TARGET_FPS)){
                        simulation.runOnce();
                        ups++;
                    }
                }

                frames++;
            }
        }.start();
    }

    public void OnScroll(ScrollEvent scrollEvent) {
        pivotX = mouseScalingCenterX;
        pivotY = mouseScalingCenterY;
        double currentScale = MatrixOperations.getScaleFromMatrix(projectionMatrix);
        if(scrollEvent.getDeltaY()>0 && currentScale < MAX_SCALE) {
            projectionMatrix = MatrixOperations.getScalingAroundMatrix(1+ SCALING_FACTOR, mouseScalingCenterX, mouseScalingCenterY).mult(projectionMatrix);
        }
        else if(scrollEvent.getDeltaY()<0 && currentScale > MIN_SCALE) {
            projectionMatrix = MatrixOperations.getScalingAroundMatrix(1- SCALING_FACTOR, mouseScalingCenterX, mouseScalingCenterY).mult(projectionMatrix);
        }
    }


    @FXML
    public void onMousePressed(MouseEvent mouseEvent) {
        lastMouseX = mouseEvent.getX();
        lastMouseY = mouseEvent.getY();
    }

    public void onMouseDragged(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseButton.MIDDLE){

            double deltaX = lastMouseX - mouseEvent.getX();
            double deltaY = lastMouseY - mouseEvent.getY();

            lastMouseX = mouseEvent.getX();
            lastMouseY = mouseEvent.getY();

            projectionMatrix = translate(projectionMatrix, -deltaX, -deltaY);
        }

    }

    public void onMouseMoved(MouseEvent mouseEvent) {
        mouseScalingCenterX = mouseEvent.getX();
        mouseScalingCenterY = mouseEvent.getY();
    }

    SimpleMatrix translate(SimpleMatrix matrix, double x, double y){
        SimpleMatrix translationMatrix = new SimpleMatrix(
                new double[][] {
                        new double[] {1, 0, x},
                        new double[] {0, 1, y},
                        new double[] {0 ,0, 1}
                }
        );
        return translationMatrix.mult(matrix);
    }

}
