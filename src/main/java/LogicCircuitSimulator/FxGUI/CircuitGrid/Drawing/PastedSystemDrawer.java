package LogicCircuitSimulator.FxGUI.CircuitGrid.Drawing;

import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.BoardDTO;
import LogicCircuitSimulator.FxGUI.CircuitGrid.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.Simulation.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation.NodeHandler.Crossing;
import LogicCircuitSimulator.Simulation.NodeHandler.Node;
import LogicCircuitSimulator.Simulation.NodeHandler.WireState;
import LogicCircuitSimulator.Simulation.NodeVisitor;
import LogicCircuitSimulator.Vector2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;

public class PastedSystemDrawer {
    private final BoardDTO boardDTO;

    public PastedSystemDrawer(BoardDTO boardDTO) {
        this.boardDTO = boardDTO;
    }

    public void draw(){
        GraphicsContext graphicsContext = boardDTO.getCanvas().getGraphicsContext2D();
        Projection2D projection2D = boardDTO.getProjection2D();

        //DRAWING PASTED ELEMENTS AT THE MOUSE
        for (int i = 0; i < boardDTO.getPastedNodes().size(); i++) {
            Vector2D pos = boardDTO.getPastedNodes().get(i).getPosition();
            boardDTO.getSimulation().getNodeHandler().setNode(new Node(pos, WireState.NONE, WireState.NONE, Crossing.TOUCHING));
        }
        //FINDING POINT (0,0)
        List<LogicElement> pastedLogicElements = boardDTO.getPastedLogicElements();
        List<Node> pastedNodes = boardDTO.getPastedNodes();
        double lowestX = Double.MAX_VALUE;
        double lowestY = Double.MAX_VALUE;
        for (int i = 0; i < pastedLogicElements.size(); i++) {
            if(pastedLogicElements.get(i).getX() < lowestX) lowestX = pastedLogicElements.get(i).getX();
            if(pastedLogicElements.get(i).getY() < lowestY) lowestY = pastedLogicElements.get(i).getY();
        }

        //LOGIC ELEMENTS
        Vector2D startPoint = new Vector2D(lowestX, lowestY);
        for (int i = 0; i < pastedLogicElements.size(); i++) {
            Vector2D pos = pastedLogicElements.get(i).getPosition();
            pastedLogicElements.get(i).setPosition(new Vector2D(pos.getX() - startPoint.getX(), pos.getY() - startPoint.getY()));
        }

        for (int i = 0; i < pastedLogicElements.size(); i++) {
            Vector2D pos = projection2D.projectBack(boardDTO.getLastMousePosition());
            pos = new Vector2D((int)pos.getX(), (int)pos.getY());
            Vector2D oldPos = pastedLogicElements.get(i).getPosition();
            pastedLogicElements.get(i).setPosition(new Vector2D(oldPos.getX()+pos.getX(), oldPos.getY()+pos.getY()));
        }
        LogicElementVisitor drawingVisitor = new DrawSquareLogicElementVisitor(graphicsContext, projection2D);

        for (int i = 0; i < pastedLogicElements.size(); i++) {
            System.out.println(pastedLogicElements.get(i).getPosition());
            pastedLogicElements.get(i).accept(drawingVisitor);
        }
        //NODES
        for (int i = 0; i < pastedNodes.size(); i++) {
            Vector2D oldPos = pastedNodes.get(i).getPosition();
            Node oldNode = pastedNodes.get(i);
            Node newNode = new Node(new Vector2D(oldPos.getX()-startPoint.getX(), oldPos.getY()-startPoint.getY()),
                    oldNode.getRightWire(), oldNode.getDownWire(), oldNode.isTouching());
            pastedNodes.set(i, newNode);
        }
        for (int i = 0; i < pastedNodes.size(); i++) {
            Vector2D pos = projection2D.projectBack(boardDTO.getLastMousePosition());
            pos = new Vector2D((int)pos.getX(), (int)pos.getY());
            Vector2D oldPos = pastedNodes.get(i).getPosition();
            Node oldNode = pastedNodes.get(i);
            Node newNode = new Node(new Vector2D(oldPos.getX()+pos.getX(), oldPos.getY()+pos.getY()),
                    oldNode.getRightWire(), oldNode.getDownWire(), oldNode.isTouching());
            pastedNodes.set(i, newNode);
        }
        NodeVisitor drawingNodeVisitor = new DrawNodeVisitor(graphicsContext, projection2D);
        for (int i = 0; i < pastedNodes.size(); i++) {
            pastedNodes.get(i).accept(drawingNodeVisitor);
        }
    }
}
