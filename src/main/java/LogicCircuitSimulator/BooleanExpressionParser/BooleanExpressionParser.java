package LogicCircuitSimulator.BooleanExpressionParser;

import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SelectionDTO;

public interface BooleanExpressionParser{
    SelectionDTO parse(String expression);
}
