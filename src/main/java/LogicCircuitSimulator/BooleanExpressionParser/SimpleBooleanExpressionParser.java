package LogicCircuitSimulator.BooleanExpressionParser;

import LogicCircuitSimulator.BooleanExpressionParser.CircuitGenerator.CircuitGenerator;
import LogicCircuitSimulator.BooleanExpressionParser.CircuitGenerator.SimpleCircuitGenerator;
import LogicCircuitSimulator.BooleanExpressionParser.ExpressionSimplifier.InvalidExpressionException;
import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SelectionDTO;

public class SimpleBooleanExpressionParser implements BooleanExpressionParser{
    @Override
    public SelectionDTO parse(String expression) throws InvalidExpressionException {
        CircuitGenerator circuitGenerator = new SimpleCircuitGenerator();
        return circuitGenerator.generate(expression);
    }
}
