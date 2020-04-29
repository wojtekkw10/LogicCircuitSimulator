package LogicCircuitSimulator.FxGUI.FXMLControllers;

import LogicCircuitSimulator.App;
import LogicCircuitSimulator.FxGUI.MainCanvasBackground;
import LogicCircuitSimulator.Simulation;
import LogicCircuitSimulator.Utils.MatrixOperations;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.List;

public class SimulationCanvas {
    private final double SCALING_FACTOR = 0.05;
    private final double MAX_SCALE = 30;
    private final double MIN_SCALE = 5;

    private double lastMouseX = 0;
    private double lastMouseY = 0;

    private double mouseScalingCenterX = 0;
    private double mouseScalingCenterY = 0;

    private double pivotX = 0;
    private double pivotY = 0;

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
        graphics.setFont(new Font(Font.getFontNames().get(0), 15));
        //Image AND_GATE = new Image("/500px-AND_ANSI.png");
        List<List<Integer>> a = new ArrayList<>();
        for(int i=0; i<500; i++){
            List<Integer> temp = new ArrayList<>();
            for (int j = 0; j < 1500; j++) {
                temp.add(0);
            }
            a.add(temp);
        }

        MainCanvasBackground background = new MainCanvasBackground();

        new AnimationTimer() {
            private short frames = 0;
            long lastNow = 0;
            @Override
            public void handle(long now) {
                mainSimulationCanvas.setHeight(mainSimulationAnchorPane.getHeight());
                mainSimulationCanvas.setWidth(mainSimulationAnchorPane.getWidth());

                graphics.clearRect(0,0, mainSimulationCanvas.getWidth(), mainSimulationCanvas.getHeight());

                background.draw(graphics, mainSimulationCanvas.getWidth(), mainSimulationCanvas.getHeight(), projectionMatrix);

                graphics.setFill(Color.BLACK);
                if(now > lastNow + 1e9){
                    App.setWindowTitleFPS(frames);
                    frames = 0;
                    lastNow = now;
                }

                for(int i = 0; i<5000; i++){
                    graphics.strokeLine(100, 100, 120, 200);
                }
                for(int i = 0; i<3000; i++){
                    //graphics.strokeRect(100, 100, 100, 100);
                    //graphics.drawImage(AND_GATE, 100, 100, 40, 20);
                }



                for(int i = 0; i<1; i++){
                    graphics.fillText("AND", 50, 100); //niepotrzebne bo mamy kształt bramek z png
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
            projectionMatrix = MatrixOperations.getScalingAroundMatrix(1+ SCALING_FACTOR, pivotX, pivotY).mult(projectionMatrix);
        }
        else if(scrollEvent.getDeltaY()<0 && currentScale > MIN_SCALE) {
            projectionMatrix = MatrixOperations.getScalingAroundMatrix(1- SCALING_FACTOR, pivotX, pivotY).mult(projectionMatrix);
        }
        System.out.println(currentScale);
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
//TODO: create transformation matrix and update it (multiply by the next value ) here
