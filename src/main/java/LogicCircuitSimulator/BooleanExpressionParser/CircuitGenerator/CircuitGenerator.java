package LogicCircuitSimulator.BooleanExpressionParser.CircuitGenerator;

import LogicCircuitSimulator.BooleanExpressionParser.ExpressionSimplifier.InvalidExpressionException;
import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SelectionDTO;

public interface CircuitGenerator {
    SelectionDTO generate(String expression) throws InvalidExpressionException;
}
