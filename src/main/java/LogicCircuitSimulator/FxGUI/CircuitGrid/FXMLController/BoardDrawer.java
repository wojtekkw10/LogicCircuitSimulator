package LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController;

import LogicCircuitSimulator.*;
import LogicCircuitSimulator.FxGUI.CircuitGrid.BoardMouseSpecifiers.MouseLogicElementSpecifier;
import LogicCircuitSimulator.FxGUI.CircuitGrid.Drawing.*;
import LogicCircuitSimulator.FxGUI.CircuitGrid.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.Simulation.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation.NodeHandler.Crossing;
import LogicCircuitSimulator.Simulation.NodeHandler.Node;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeHandler;
import LogicCircuitSimulator.Simulation.NodeHandler.WireState;
import LogicCircuitSimulator.Simulation.NodeVisitor;
import LogicCircuitSimulator.Simulation.Simulation;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BoardDrawer {
    private final BoardDTO boardDTO;
    private final AnchorPane anchorPane;
    private final GraphicsContext graphicsContext;
    private final AtomicLong lastNow = new AtomicLong(0);

    private int currentFPS = 0;
    private int currentUPS = 0;


    public BoardDrawer(BoardDTO boardDTO, AnchorPane anchorPane) {
        this.boardDTO = boardDTO;
        this.anchorPane = anchorPane;
        this.graphicsContext = boardDTO.getCanvas().getGraphicsContext2D();
    }

    public void draw(long now){
        Canvas canvas = boardDTO.getCanvas();
        Projection2D projection2D = boardDTO.getProjection2D();
        Simulation simulation = boardDTO.getSimulation();
        AtomicBoolean isLogicGateDragged = boardDTO.getIsLogicGateLifted();
        LogicElement logicGateDragged = boardDTO.getLogicGateDragged();
        Vector2D lastMousePosition = boardDTO.getLastMousePosition();
        AtomicInteger framesSinceLastFrame = boardDTO.getFramesSinceLastFrame();

        graphicsContext.setLineWidth(1);

        graphicsContext.setFont(new Font(Font.getFontNames().get(0), 15));

        resizeCanvasToAnchorPane();
        clearCanvas(Color.BLACK);
        new SimulationCanvasBackground(canvas, boardDTO).draw(projection2D);
        drawLogicGates(simulation.getLogicElementHandler().iterator());
        drawNodes(simulation.getNodeHandler());
        drawSpeedStats(now);

        System.out.println("--");
        System.out.println(boardDTO.getSelected().getNodesAsList());
        System.out.println(boardDTO.getCopied().getNodesAsList());
        System.out.println(boardDTO.getPasted().getNodesAsList());

        //SELECTING RECT
        if(boardDTO.shouldDrawSelectionRect()){
            new SelectionRectDrawer().draw(boardDTO);
            Simulation2DSelector selector = new Simulation2DSelector(boardDTO);

            SelectionDTO selected = selector.getSelectedObjects();
            boardDTO.setSelected(selected);
        }

        if(boardDTO.shouldDrawPastedSystem()){
            new SystemDrawer(boardDTO).draw(boardDTO.getPasted());
        }



        if(isLogicGateDragged.get()) {
            new MouseLogicElementSpecifier(simulation) {
                @Override
                public void doAction() {
                    logicGateDragged.setPosition(getPosition());
                }
            }.getElementPosFromElementAndMousePosition(lastMousePosition, projection2D, logicGateDragged, boardDTO.getRelativeMouseToLogicGatePos());

            LogicElementVisitor drawLogicElement = new DrawSquareLogicElementVisitor(graphicsContext, projection2D);
            logicGateDragged.accept(drawLogicElement);
        }

        framesSinceLastFrame.getAndIncrement();

        waitUntilNextFrame(now);
    }

    //Private functions
    private void clearCanvas(Color color){
        Canvas canvas = boardDTO.getCanvas();

        graphicsContext.setFill(color);
        graphicsContext.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
    }
    private void resizeCanvasToAnchorPane(){
        Canvas canvas = boardDTO.getCanvas();

        canvas.setHeight(anchorPane.getHeight());
        canvas.setWidth(anchorPane.getWidth());
    }

    private void drawLogicGates(Iterator<LogicElement> logicElements){
        Projection2D projection2D = boardDTO.getProjection2D();

        LogicElementVisitor drawLogicElement = new DrawSquareLogicElementVisitor(graphicsContext, projection2D);
        while(logicElements.hasNext()){
            logicElements.next().accept(drawLogicElement);
        }
    }

    private void drawNodes(NodeHandler nodeHandler){
        Projection2D projection2D = boardDTO.getProjection2D();
        Iterator<Node> nodes = nodeHandler.iterator();

        NodeVisitor drawNodeVisitor = new DrawNodeVisitor(graphicsContext, projection2D);
        while(nodes.hasNext()){
            Node node = nodes.next();
            int numberOfSurroundingWires = getNumberOfSurroundingWires(node, nodeHandler);
            if(numberOfSurroundingWires < 3)
                node = new Node(node.getPosition(), node.getRightWire(), node.getDownWire(), Crossing.NOT_TOUCHING);

            node.accept(drawNodeVisitor);
        }
    }

    private int getNumberOfSurroundingWires(Node node, NodeHandler nodeHandler){
        int number = 0;
        if(nodeHandler.getUpWire(node.getPosition()) != WireState.NONE) number++;
        if(nodeHandler.getRightWire(node.getPosition()) != WireState.NONE) number++;
        if(nodeHandler.getDownWire(node.getPosition()) != WireState.NONE) number++;
        if(nodeHandler.getLeftWire(node.getPosition()) != WireState.NONE) number++;
        return number;

    }

    private void drawSpeedStats(long now){
        AtomicInteger framesSinceLastFrame = boardDTO.getFramesSinceLastFrame();
        AtomicInteger updatesSinceLastFrame = boardDTO.getUpdatesSinceLastFrame();

        if(now > lastNow.get() + 1e9){
            currentUPS = updatesSinceLastFrame.get();
            currentFPS = framesSinceLastFrame.get();
            framesSinceLastFrame.set(0);
            updatesSinceLastFrame.set(0);
            lastNow.set(now);
        }

        if(boardDTO.shouldDrawSpeedStats()){
            double PIXELS_PER_CHAR = 6.5;

            graphicsContext.setFont(new Font(Font.getFontNames().get(0), 14));
            graphicsContext.setFill(Color.gray(0.05, 0.5));
            String stats = "FPS: "+currentFPS + ", UPS: "+currentUPS;
            int statsWidth = stats.length();
            graphicsContext.fillRect(anchorPane.getWidth() - 20 - statsWidth * PIXELS_PER_CHAR, 0, 20 + statsWidth * PIXELS_PER_CHAR, 30);
            graphicsContext.setFill(Color.WHITE);
            graphicsContext.fillText(stats, anchorPane.getWidth() - 20 - statsWidth * PIXELS_PER_CHAR,  20);
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
