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

    private boolean nodesWereChanged = false;
    private boolean logicElementsWereChanged = false;
    private List<Node> nodes = new ArrayList<>();
    private List<LogicElement> logicElements = new ArrayList<>();

    public SelectionDTO(){
        nodesWereChanged = true;
        nodeHandler = new ArrayNodeHandler();
        logicElementHandler = new SimpleLogicElementHandler();
    }

    public SelectionDTO(SelectionDTO selectionDTO){
        nodesWereChanged = true;
        logicElementsWereChanged = true;
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
        nodesWereChanged = true;
        return nodeHandler;
    }

    public void setNodeHandler(NodeHandler nodeHandler) {
        nodesWereChanged = true;
        this.nodeHandler = nodeHandler;
    }

    public LogicElementHandler getLogicElementHandler() {
        logicElementsWereChanged = true;
        return logicElementHandler;
    }

    public void setLogicElementHandler(LogicElementHandler logicElementHandler) {
        logicElementsWereChanged = true;
        this.logicElementHandler = logicElementHandler;
    }

    public List<LogicElement> getLogicElementsAsList(){
        if(logicElementsWereChanged){
            logicElements = new ArrayList<>();
            Iterator<LogicElement> logicElementIterator = logicElementHandler.iterator();
            while(logicElementIterator.hasNext()){
                logicElements.add(LogicElementFactory.instance(logicElementIterator.next()));
            }
            logicElementsWereChanged = false;
        }



        return logicElements;
    }

    public List<Node> getNodesAsList(){
        if(nodesWereChanged){
            nodes = new ArrayList<>();
            Iterator<Node> nodeIterator = nodeHandler.iterator();
            while(nodeIterator.hasNext()){
                nodes.add(new Node(nodeIterator.next()));
            }
            nodesWereChanged = false;
        }
        return nodes;
    }
}
