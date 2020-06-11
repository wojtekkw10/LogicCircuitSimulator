package LogicCircuitSimulator.BooleanExpressionParser;

import LogicCircuitSimulator.BooleanExpressionParser.CircuitGenerator.CircuitGenerator;
import LogicCircuitSimulator.BooleanExpressionParser.CircuitGenerator.SimpleCircuitGenerator;
import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SelectionDTO;

public class SimpleBooleanExpressionParser implements BooleanExpressionParser{
    @Override
    public SelectionDTO parse(String expression) {
        CircuitGenerator circuitGenerator = new SimpleCircuitGenerator();
        return circuitGenerator.generate(expression);
    }
}
