package LogicCircuitSimulator.BooleanExpressionParser.CircuitGenerator;

import LogicCircuitSimulator.BooleanExpressionParser.BooleanExpressionTree.ExpressionNode;
import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SelectionDTO;

public interface CircuitGenerator {
    SelectionDTO generate(String expression);
}
