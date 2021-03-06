package LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController;

import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElementHandler;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElementFactory;
import LogicCircuitSimulator.Simulation.LogicElementHandler.SimpleLogicElementHandler;
import LogicCircuitSimulator.Simulation.NodeHandler.ArrayNodeHandler;
import LogicCircuitSimulator.Simulation.NodeHandler.Node;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Simulation2DSelector {
    private final BoardDTO boardDTO;

    public Simulation2DSelector(BoardDTO boardDTO){
        this.boardDTO = boardDTO;
    }

    public List<LogicElement> getSelectedLogicElements(){
        List<LogicElement> selectedLogicElements = new ArrayList<>();
        Iterator<LogicElement> logicElements = boardDTO.getSimulation().getLogicElementHandler().iterator();
        while(logicElements.hasNext()){
            LogicElement element = logicElements.next();
            double x = element.getGeometry().getPosition().getX();
            double y = element.getGeometry().getPosition().getY();
            if(x > boardDTO.getSelectFirstPoint().getX() && x < boardDTO.getSelectSecondPoint().getX() &&
                    y > boardDTO.getSelectFirstPoint().getY() && y < boardDTO.getSelectSecondPoint().getY()){
                if(element.getName().equals("BTN")) selectedLogicElements.add(LogicElementFactory.buttonInstance((int)element.getX(), (int)element.getY(), element.getRotation(), false));
                else selectedLogicElements.add(
                        LogicElementFactory.instance(
                                element.getName(), (int)element.getX(), (int)element.getY(), element.getRotation()));
            }
        }
        return selectedLogicElements;
    }
    public List<Node> getSelectedNodes(){
        List<Node> selectedNodes = new ArrayList<>();
        Iterator<Node> nodes = boardDTO.getSimulation().getNodeHandler().iterator();
        while(nodes.hasNext()){
            Node node = nodes.next();
            double x = node.getPosition().getX();
            double y = node.getPosition().getY();
            if(x > boardDTO.getSelectFirstPoint().getX() && x < boardDTO.getSelectSecondPoint().getX() &&
                    y > boardDTO.getSelectFirstPoint().getY() && y < boardDTO.getSelectSecondPoint().getY()){
                selectedNodes.add(node);
            }
        }
        return selectedNodes;
    }

    public SelectionDTO getSelectedObjects(){
        LogicElementHandler logicElementHandler = new SimpleLogicElementHandler();
        NodeHandler nodeHandler = new ArrayNodeHandler();

        Iterator<Node> nodes = boardDTO.getSimulation().getNodeHandler().iterator();
        while(nodes.hasNext()){
            Node node = nodes.next();
            double x = node.getPosition().getX();
            double y = node.getPosition().getY();
            if(x > boardDTO.getSelectUpperLeft().getX() && x < boardDTO.getSelectBottomRight().getX() &&
                    y > boardDTO.getSelectUpperLeft().getY() && y < boardDTO.getSelectBottomRight().getY()){
               nodeHandler.setNode(node);
            }
        }

        Iterator<LogicElement> logicElements = boardDTO.getSimulation().getLogicElementHandler().iterator();
        while(logicElements.hasNext()){
            LogicElement element = logicElements.next();
            double x = element.getGeometry().getPosition().getX();
            double y = element.getGeometry().getPosition().getY();
            if(x > boardDTO.getSelectUpperLeft().getX() && x < boardDTO.getSelectBottomRight().getX() &&
                    y > boardDTO.getSelectUpperLeft().getY() && y < boardDTO.getSelectBottomRight().getY()){
                logicElementHandler.add(LogicElementFactory.instance(element));
            }
        }

        SelectionDTO selected = new SelectionDTO();
        selected.setLogicElementHandler(logicElementHandler);
        selected.setNodeHandler(nodeHandler);
        return  selected;

    }
}
