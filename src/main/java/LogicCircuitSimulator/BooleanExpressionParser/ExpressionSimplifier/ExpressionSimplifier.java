package LogicCircuitSimulator.BooleanExpressionParser.ExpressionSimplifier;

public interface ExpressionSimplifier {
    String simplify(String expression) throws InvalidExpressionException;
}
