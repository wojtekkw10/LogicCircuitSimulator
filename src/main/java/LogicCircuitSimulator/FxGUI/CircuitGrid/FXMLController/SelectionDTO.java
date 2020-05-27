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

public class SelectionDTO {
    private NodeHandler nodeHandler = new ArrayNodeHandler();
    private LogicElementHandler logicElementHandler = new SimpleLogicElementHandler();

    public SelectionDTO(){
        nodeHandler = new ArrayNodeHandler();
        logicElementHandler = new SimpleLogicElementHandler();
    }

    public SelectionDTO(SelectionDTO selectionDTO){
        NodeHandler nodeHandler = selectionDTO.getNodeHandler();
        LogicElementHandler logicElementHandler = selectionDTO.getLogicElementHandler();

        Iterator<Node> nodeIterator = nodeHandler.iterator();
        Iterator<LogicElement> logicElementIterator = logicElementHandler.iterator();

        while(nodeIterator.hasNext()){
            this.nodeHandler.setNode(nodeIterator.next());
        }
        while(logicElementIterator.hasNext()){
            this.logicElementHandler.add(LogicElementFactory.instance(logicElementIterator.next()));
        }

    }

    public NodeHandler getNodeHandler() {
        return nodeHandler;
    }

    public void setNodeHandler(NodeHandler nodeHandler) {
        this.nodeHandler = nodeHandler;
    }

    public LogicElementHandler getLogicElementHandler() {
        return logicElementHandler;
    }

    public void setLogicElementHandler(LogicElementHandler logicElementHandler) {
        this.logicElementHandler = logicElementHandler;
    }

    public List<LogicElement> getLogicElementsAsList(){
        List<LogicElement> logicElements = new ArrayList<>();
        Iterator<LogicElement> logicElementIterator = logicElementHandler.iterator();
        while(logicElementIterator.hasNext()){
            logicElements.add(LogicElementFactory.instance(logicElementIterator.next()));
        }
        return logicElements;
    }

    public List<Node> getNodesAsList(){
        List<Node> nodes = new ArrayList<>();
        Iterator<Node> nodeIterator = nodeHandler.iterator();
        while(nodeIterator.hasNext()){
            nodes.add(new Node(nodeIterator.next()));
        }
        return nodes;
    }
}
