package LogicCircuitSimulator.BooleanExpressionParser.BooleanExpressionTree;

public class ExpressionNode {
    private ExpressionNode firstNode;
    private ExpressionNode secondNode;
    private String terminalText;
    private Operand operand;
    private int depth;

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public ExpressionNode getFirstNode() {
        return firstNode;
    }

    public void setFirstNode(ExpressionNode firstNode) {
        this.firstNode = firstNode;
    }

    public ExpressionNode getSecondNode() {
        return secondNode;
    }

    public void setSecondNode(ExpressionNode secondNode) {
        this.secondNode = secondNode;
    }

    public String getTerminalText() {
        return terminalText;
    }

    public void setTerminalText(String terminalText) {
        this.terminalText = terminalText;
    }

    public Operand getOperand() {
        return operand;
    }

    public void setOperand(Operand operand) {
        this.operand = operand;
    }

    @Override
    public String toString() {
        return "ExpressionNode{" +
                "firstNode=" + firstNode +
                ", secondNode=" + secondNode +
                ", terminalText='" + terminalText + '\'' +
                ", operand=" + operand +
                ", depth=" + depth +
                '}';
    }
}
