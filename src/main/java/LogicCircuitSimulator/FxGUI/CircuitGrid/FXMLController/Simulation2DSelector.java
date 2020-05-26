package LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController;

import LogicCircuitSimulator.Simulation.LogicElements.LogicElement;
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
        Iterator<LogicElement> logicElements = boardDTO.getSimulation().logicElementIterator();
        while(logicElements.hasNext()){
            LogicElement element = logicElements.next();
            double x = element.getGeometry().getPosition().getX();
            double y = element.getGeometry().getPosition().getY();
            Vector2D screenPos = boardDTO.getProjection2D().project(new Vector2D(x,y));
            x = screenPos.getX();
            y = screenPos.getY();
            if(x > boardDTO.getSelectLeftUpper().getX() && x < boardDTO.getSelectRightBottom().getX() &&
                    y > boardDTO.getSelectLeftUpper().getY() && y < boardDTO.getSelectRightBottom().getY()){
                selectedLogicElements.add(element);
            }
        }
        return selectedLogicElements;
    }
    public List<Node> getSelectedNodes(){
        List<Node> selectedNodes = new ArrayList<>();
        Iterator<Node> nodes = boardDTO.getSimulation().nodeIterator();
        while(nodes.hasNext()){
            Node node = nodes.next();
            double x = node.getPosition().getX();
            double y = node.getPosition().getY();
            Vector2D screenPos = boardDTO.getProjection2D().project(new Vector2D(x,y));
            x = screenPos.getX();
            y = screenPos.getY();
            if(x > boardDTO.getSelectLeftUpper().getX() && x < boardDTO.getSelectRightBottom().getX() &&
                    y > boardDTO.getSelectLeftUpper().getY() && y < boardDTO.getSelectRightBottom().getY()){
                selectedNodes.add(node);
            }
        }
        return selectedNodes;
    }
}
