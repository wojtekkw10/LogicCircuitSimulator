package LogicCircuitSimulator.BooleanExpressionParser;

import LogicCircuitSimulator.BooleanExpressionParser.ExpressionSimplifier.ExpressionSimplifier;
import LogicCircuitSimulator.BooleanExpressionParser.ExpressionSimplifier.SimpleExpressionSimplifier;

import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SelectionDTO;
import LogicCircuitSimulator.Simulation.NodeHandler.ArrayNodeHandler;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeHandler;
import LogicCircuitSimulator.Simulation.NodeHandler.WireState;
import LogicCircuitSimulator.Vector2D;

public class SimpleBooleanExpressionParser implements BooleanExpressionParser{
    @Override
    public SelectionDTO parse(String expression) {
        CircuitDepthCounter depthCounter = new CircuitDepthCounter();
        ExpressionSimplifier simplifier = new SimpleExpressionSimplifier();
        String expr = simplifier.simplify(expression);

        int depth = depthCounter.getDepth(expression);
        System.out.println(depth);
        System.out.println(expr);

        //generator.visit(tree);

        NodeHandler nodeHandler = new ArrayNodeHandler();
        nodeHandler.setRightWire(new Vector2D(0,0), WireState.LOW);
        SelectionDTO selectionDTO = new SelectionDTO();
        selectionDTO.setNodeHandler(nodeHandler);
        return selectionDTO;
    }
}
