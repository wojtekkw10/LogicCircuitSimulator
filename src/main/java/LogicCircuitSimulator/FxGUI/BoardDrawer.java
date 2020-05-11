package LogicCircuitSimulator.FxGUI;

import LogicCircuitSimulator.*;
import LogicCircuitSimulator.FxGUI.FXMLControllers.SimulationCanvasController;
import LogicCircuitSimulator.FxGUI.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.FxGUI.GraphicalProjection.SimpleMatrixProjection2D;
import LogicCircuitSimulator.FxGUI.GridMouseHandler.LogicElementMouseHandler;
import LogicCircuitSimulator.LogicElements.LogicElement;
import LogicCircuitSimulator.NodeHandler.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BoardDrawer {
    private final int TARGET_FPS = 10000;
    private final SyncMode syncMode = SyncMode.NOT_SYNCHRONIZED;

    Projection2D projection2D = new SimpleMatrixProjection2D(new Vector2D(0,0), 20);
    Simulation simulation = new Simulation();
    Canvas canvas;
    AnchorPane anchorPane;
    GraphicsContext graphicsContext;

    private AtomicBoolean isLogicGateDragged = new AtomicBoolean(false);
    LogicElement logicGateDragged;

    Vector2D lastMousePosition;

    private long lastNow;

    enum SyncMode{
        SYNCHRONIZED,
        NOT_SYNCHRONIZED
    }

    private int framesSinceLastFrame;
    AtomicInteger updatesSinceLastFrame = new AtomicInteger();

    public BoardDrawer(Canvas canvas, AnchorPane anchorPane) {
        this.canvas = canvas;
        this.anchorPane = anchorPane;
    }

    public void draw(long now){
        graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setLineWidth(1);

        graphicsContext.setFont(new Font(Font.getFontNames().get(0), 15));

        SimulationCanvasBackground background = new SimulationCanvasBackground(canvas);

        updateTitleBar(now);
        resizeCanvasToAnchorPane();
        clearCanvas(Color.BLACK);
        background.draw(projection2D);
        drawLogicGates(simulation.logicElementIterator());
        drawNodes(simulation.nodeIterator());

        if(isLogicGateDragged.get()) {
            new LogicElementMouseHandler(simulation) {
                @Override
                public void transformLogicElement() {
                    logicGateDragged.setPosition(getPosition());
                }
            }.performNoTransformation(SimulationCanvasController.lastMousePosition, projection2D);

            LogicElementVisitor drawLogicElement = new DrawSquareLogicElementVisitor(graphicsContext, projection2D);
            logicGateDragged.accept(drawLogicElement);
        }

        if(syncMode == SyncMode.SYNCHRONIZED){
            updatesSinceLastFrame.getAndIncrement();
            simulation.runOnce();
        }
        framesSinceLastFrame++;

        waitUntilNextFrame(now);
    }

    public void update(Vector2D lastMousePosition, AtomicBoolean isLogicGateDragged,
                       LogicElement logicGateDragged, Projection2D projection2D,
                       Simulation simulation, AtomicInteger updatesSinceLastFrame){
        this.lastMousePosition = lastMousePosition;
        this.isLogicGateDragged = isLogicGateDragged;
        this.logicGateDragged = logicGateDragged;
        this.projection2D = projection2D;
        this.simulation = simulation;
        this.updatesSinceLastFrame = updatesSinceLastFrame;

    }

    //Private functions
    private void clearCanvas(Color color){
        graphicsContext.setFill(color);
        graphicsContext.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
    }
    private void resizeCanvasToAnchorPane(){
        canvas.setHeight(anchorPane.getHeight());
        canvas.setWidth(anchorPane.getWidth());
    }

    private void drawLogicGates(Iterator<LogicElement> logicElements){
        LogicElementVisitor drawLogicElement = new DrawSquareLogicElementVisitor(graphicsContext, projection2D);
        while(logicElements.hasNext()){
            logicElements.next().accept(drawLogicElement);
        }
    }

    private void drawNodes(Iterator<Node> nodes){
        NodeVisitor drawNode = new DrawNodeVisitor(graphicsContext, projection2D);
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
}
