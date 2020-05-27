package LogicCircuitSimulator.FxGUI.CircuitGrid.Drawing;

import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.BoardDTO;
import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SelectionDTO;
import LogicCircuitSimulator.FxGUI.CircuitGrid.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElementHandler;
import LogicCircuitSimulator.Simulation.LogicElementHandler.SimpleLogicElementHandler;
import LogicCircuitSimulator.Simulation.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation.NodeHandler.*;
import LogicCircuitSimulator.Simulation.NodeVisitor;
import LogicCircuitSimulator.Vector2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SystemDrawer {
    private final BoardDTO boardDTO;

    public SystemDrawer(BoardDTO boardDTO) {
        this.boardDTO = boardDTO;
    }

    public void draw(SelectionDTO pastedObjects){
        GraphicsContext graphicsContext = boardDTO.getCanvas().getGraphicsContext2D();
        Projection2D projection2D = boardDTO.getProjection2D();
        //EXTRACTING OBJECTS
        List<LogicElement> logicElements = pastedObjects.getLogicElementsAsList();
        List<Node> nodes = pastedObjects.getNodesAsList();

        //DRAWING PASTED ELEMENTS AT THE MOUSE
        //FINDING POINT (0,0)
        double lowestX = Double.MAX_VALUE;
        double lowestY = Double.MAX_VALUE;
        for (int i = 0; i < logicElements.size(); i++) {
            if(logicElements.get(i).getX() < lowestX) lowestX = logicElements.get(i).getX();
            if(logicElements.get(i).getY() < lowestY) lowestY = logicElements.get(i).getY();
        }

        //LOGIC ELEMENTS

        Vector2D startPoint = new Vector2D(lowestX, lowestY);
        System.out.println("START POINT: "+startPoint);
        for (int i = 0; i < logicElements.size(); i++) {
            Vector2D pos = logicElements.get(i).getPosition();
            logicElements.get(i).setPosition(new Vector2D(pos.getX() - startPoint.getX(), pos.getY() - startPoint.getY()));
        }
        System.out.println("_+_+");
        System.out.println(logicElements.get(0).getGeometry().getPosition());

        for (int i = 0; i < logicElements.size(); i++) {
            Vector2D pos = projection2D.projectBack(boardDTO.getLastMousePosition());
            pos = new Vector2D((int)pos.getX(), (int)pos.getY());
            Vector2D oldPos = logicElements.get(i).getPosition();
            logicElements.get(i).setPosition(new Vector2D(oldPos.getX()+pos.getX(), oldPos.getY()+pos.getY()));
        }
        LogicElementVisitor drawingVisitor = new DrawSquareLogicElementVisitor(graphicsContext, projection2D);
        System.out.println(logicElements.get(0).getGeometry().getPosition());
        for (int i = 0; i < logicElements.size(); i++) {
            logicElements.get(i).accept(drawingVisitor);
        }
        //NODES
        System.out.println("_+_+");
        System.out.println(nodes);
        for (int i = 0; i < nodes.size(); i++) {
            Vector2D oldPos = nodes.get(i).getPosition();
            Node oldNode = nodes.get(i);
            Node newNode = new Node(new Vector2D(oldPos.getX()-startPoint.getX(), oldPos.getY()-startPoint.getY()),
                    oldNode.getRightWire(), oldNode.getDownWire(), oldNode.isTouching());
            nodes.set(i, newNode);
        }
        System.out.println(nodes);
        for (int i = 0; i < nodes.size(); i++) {
            Vector2D pos = projection2D.projectBack(boardDTO.getLastMousePosition());
            pos = new Vector2D((int)pos.getX(), (int)pos.getY());
            Vector2D oldPos = nodes.get(i).getPosition();
            Node oldNode = nodes.get(i);
            Node newNode = new Node(new Vector2D(oldPos.getX()+pos.getX(), oldPos.getY()+pos.getY()),
                    oldNode.getRightWire(), oldNode.getDownWire(), oldNode.isTouching());
            nodes.set(i, newNode);
        }
        System.out.println(nodes);
        NodeVisitor drawingNodeVisitor = new DrawNodeVisitor(graphicsContext, projection2D);
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).accept(drawingNodeVisitor);
        }

        //UPDATE PASTED OBJECTS IN BoardDto.Pasted

        SelectionDTO selectionDTO = new SelectionDTO();
        NodeHandler nodeHandler = new ArrayNodeHandler();
        LogicElementHandler logicElementHandler = new SimpleLogicElementHandler();
        for (int i = 0; i < nodes.size(); i++) {
            nodeHandler.setNode(nodes.get(i));
        }
        for (int i = 0; i < logicElements.size(); i++) {
            logicElementHandler.add(logicElements.get(i));
        }
        selectionDTO.setNodeHandler(nodeHandler);
        selectionDTO.setLogicElementHandler(logicElementHandler);
        boardDTO.setPasted(selectionDTO);
    }
}
