package LogicCircuitSimulator.BooleanExpressionParser;

import LogicCircuitSimulator.BooleanExpressionParser.ExpressionSimplifier.InvalidExpressionException;
import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SelectionDTO;

public interface BooleanExpressionParser{
    SelectionDTO parse(String expression) throws InvalidExpressionException;
}
