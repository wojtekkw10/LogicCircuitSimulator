package LogicCircuitSimulator.FxGUI.FXMLControllers;

import LogicCircuitSimulator.*;
import LogicCircuitSimulator.FxGUI.DrawNodeVisitor;
import LogicCircuitSimulator.FxGUI.DrawSquareLogicElementVisitor;
import LogicCircuitSimulator.FxGUI.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.FxGUI.GridMouseHandler.LogicElementMouseHandler;
import LogicCircuitSimulator.FxGUI.SimulationCanvasBackground;
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
import java.util.concurrent.atomic.AtomicLong;

public class BoardDrawer {
    BoardDTO boardDTO;

    public BoardDrawer(BoardDTO boardDTO) {
        this.boardDTO = boardDTO;
    }

    public void draw(long now){
        GraphicsContext graphicsContext = boardDTO.getGraphicsContext();
        Canvas canvas = boardDTO.getCanvas();
        Projection2D projection2D = boardDTO.getProjection2D();
        Simulation simulation = boardDTO.getSimulation();
        AtomicBoolean isLogicGateDragged = boardDTO.getIsLogicGateDragged();
        LogicElement logicGateDragged = boardDTO.getLogicGateDragged();
        Vector2D lastMousePosition = boardDTO.getLastMousePosition();
        BoardDTO.SyncMode syncMode = boardDTO.getSyncMode();
        AtomicInteger updatesSinceLastFrame = boardDTO.getUpdatesSinceLastFrame();
        AtomicInteger framesSinceLastFrame = boardDTO.getFramesSinceLastFrame();

        graphicsContext.setLineWidth(1);

        graphicsContext.setFont(new Font(Font.getFontNames().get(0), 15));

        SimulationCanvasBackground background = new SimulationCanvasBackground(canvas, boardDTO);

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
            }.performNoTransformation(lastMousePosition, projection2D);

            LogicElementVisitor drawLogicElement = new DrawSquareLogicElementVisitor(graphicsContext, projection2D);
            logicGateDragged.accept(drawLogicElement);
        }

        if(syncMode == BoardDTO.SyncMode.SYNCHRONIZED){
            updatesSinceLastFrame.getAndIncrement();
            simulation.runOnce();
        }
        framesSinceLastFrame.getAndIncrement();

        waitUntilNextFrame(now);
    }

    //Private functions
    private void clearCanvas(Color color){
        GraphicsContext graphicsContext = boardDTO.getGraphicsContext();
        Canvas canvas = boardDTO.getCanvas();

        graphicsContext.setFill(color);
        graphicsContext.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
    }
    private void resizeCanvasToAnchorPane(){
        Canvas canvas = boardDTO.getCanvas();
        AnchorPane anchorPane = boardDTO.getAnchorPane();

        canvas.setHeight(anchorPane.getHeight());
        canvas.setWidth(anchorPane.getWidth());
    }

    private void drawLogicGates(Iterator<LogicElement> logicElements){
        GraphicsContext graphicsContext = boardDTO.getGraphicsContext();
        Projection2D projection2D = boardDTO.getProjection2D();

        LogicElementVisitor drawLogicElement = new DrawSquareLogicElementVisitor(graphicsContext, projection2D);
        while(logicElements.hasNext()){
            logicElements.next().accept(drawLogicElement);
        }
    }

    private void drawNodes(Iterator<Node> nodes){
        GraphicsContext graphicsContext = boardDTO.getGraphicsContext();
        Projection2D projection2D = boardDTO.getProjection2D();

        NodeVisitor drawNode = new DrawNodeVisitor(graphicsContext, projection2D);
        while(nodes.hasNext()){
            nodes.next().accept(drawNode);
        }
    }

    private void updateTitleBar(long now){
        AtomicLong lastNow = boardDTO.getLastNow();
        AtomicInteger framesSinceLastFrame = boardDTO.getFramesSinceLastFrame();
        AtomicInteger updatesSinceLastFrame = boardDTO.getUpdatesSinceLastFrame();

        if(now > lastNow.get() + 1e9){
            App.decorateWindowTitle(framesSinceLastFrame.get(), updatesSinceLastFrame.get());
            framesSinceLastFrame.set(0);
            updatesSinceLastFrame.set(0);
            lastNow.set(now);
        }
    }

    private void waitUntilNextFrame(long now){
        int TARGET_FPS = boardDTO.getTARGET_FPS();
        try {
            Thread.sleep((long) Math.max(0, ((1e9 / TARGET_FPS) - (System.nanoTime() - now))/1000000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
