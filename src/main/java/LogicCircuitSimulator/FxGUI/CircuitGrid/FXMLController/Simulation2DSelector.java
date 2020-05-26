package LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController;

import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElementFactory;
import LogicCircuitSimulator.Simulation.NodeHandler.Node;
import LogicCircuitSimulator.Vector2D;

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
            if(x > boardDTO.getSelectLeftUpper().getX() && x < boardDTO.getSelectRightBottom().getX() &&
                    y > boardDTO.getSelectLeftUpper().getY() && y < boardDTO.getSelectRightBottom().getY()){
                selectedLogicElements.add(
                        LogicElementFactory.getLogicElement(
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
            if(x > boardDTO.getSelectLeftUpper().getX() && x < boardDTO.getSelectRightBottom().getX() &&
                    y > boardDTO.getSelectLeftUpper().getY() && y < boardDTO.getSelectRightBottom().getY()){
                selectedNodes.add(node);
            }
        }
        return selectedNodes;
    }
}
