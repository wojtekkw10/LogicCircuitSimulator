package LogicCircuitSimulator.BooleanExpressionParser;

import LogicCircuitSimulator.BooleanExpressionParser.CircuitGenerator.CircuitGenerator;
import LogicCircuitSimulator.BooleanExpressionParser.CircuitGenerator.SimpleCircuitGenerator;
import LogicCircuitSimulator.BooleanExpressionParser.ExpressionSimplifier.ExpressionSimplifier;
import LogicCircuitSimulator.BooleanExpressionParser.ExpressionSimplifier.SimpleExpressionSimplifier;
import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SelectionDTO;

public class SimpleBooleanExpressionParser implements BooleanExpressionParser{
    @Override
    public SelectionDTO parse(String expression) {
        ExpressionSimplifier simplifier = new SimpleExpressionSimplifier();
        String expr = simplifier.simplify(expression);

        CircuitGenerator circuitGenerator = new SimpleCircuitGenerator();
        return circuitGenerator.generate(expr);
    }
}
