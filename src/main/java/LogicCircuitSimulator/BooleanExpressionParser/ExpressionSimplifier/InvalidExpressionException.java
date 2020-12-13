package LogicCircuitSimulator.BooleanExpressionParser.ExpressionSimplifier;

public class InvalidExpressionException extends Exception{
    private String message;
    public InvalidExpressionException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
