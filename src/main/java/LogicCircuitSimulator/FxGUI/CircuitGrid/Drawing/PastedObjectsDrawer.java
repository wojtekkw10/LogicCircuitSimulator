package LogicCircuitSimulator.FxGUI.CircuitGrid.Drawing;

import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.BoardDTO;
import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SelectionDTO;
import LogicCircuitSimulator.FxGUI.CircuitGrid.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation.NodeHandler.Node;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeVisitor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class PastedObjectsDrawer {
    private final BoardDTO boardDTO;

    public PastedObjectsDrawer(BoardDTO boardDTO) {
        this.boardDTO = boardDTO;
    }

    public void draw(SelectionDTO pastedObjects){
        GraphicsContext graphicsContext = boardDTO.getCanvas().getGraphicsContext2D();
        Projection2D projection2D = boardDTO.getProjection2D();

        List<LogicElement> logicElements = pastedObjects.getLogicElementsAsList();
        List<Node> nodes = pastedObjects.getNodesAsList();

        LogicElementVisitor drawingVisitor = new DrawSquareLogicElementVisitor(boardDTO, Color.AQUA, Color.GREY);
        for (int i = 0; i < logicElements.size(); i++) {
            logicElements.get(i).accept(drawingVisitor);
        }
        NodeVisitor drawingNodeVisitor = new DrawNodeVisitor(graphicsContext, projection2D, Color.AQUA, Color.GREY);
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).accept(drawingNodeVisitor);
        }


    }
}
