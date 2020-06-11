package LogicCircuitSimulator.BooleanExpressionParser.CircuitGenerator;

import LogicCircuitSimulator.BooleanExpressionParser.BooleanExpressionTree.ExpressionNode;
import LogicCircuitSimulator.BooleanExpressionParser.BooleanExpressionTree.ExpressionTreeGenerator;
import LogicCircuitSimulator.BooleanExpressionParser.BooleanExpressionTree.Operand;
import LogicCircuitSimulator.BooleanExpressionParser.BooleanExpressionTree.SimpleExpressionTreeGenerator;
import LogicCircuitSimulator.BooleanExpressionParser.ExpressionSimplifier.ExpressionSimplifier;
import LogicCircuitSimulator.BooleanExpressionParser.ExpressionSimplifier.SimpleExpressionSimplifier;
import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SelectionDTO;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElementHandler;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.*;
import LogicCircuitSimulator.Simulation.LogicElementHandler.SimpleLogicElementHandler;
import LogicCircuitSimulator.Simulation.NodeHandler.*;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SimpleCircuitGenerator implements CircuitGenerator {
    @Override
    public SelectionDTO generate(String expression) {
        NodeHandler nodeHandler = new ArrayNodeHandler();
        LogicElementHandler logicElementHandler = new SimpleLogicElementHandler();

        ExpressionSimplifier simplifier = new SimpleExpressionSimplifier();
        String simplifiedExpression = simplifier.simplify(expression);
        ExpressionTreeGenerator treeGenerator = new SimpleExpressionTreeGenerator();
        ExpressionNode tree = treeGenerator.generate(simplifiedExpression);
        int maxDepth = findMaxDepth(tree);

        List<String> identifiers = getIdentifiers(tree);
        List<String> identifiersWithNoRepetitions = removeRepetitions(identifiers);
        int neededSpace = getNeededSpaceForIdentifiers(identifiers, identifiersWithNoRepetitions);

        assignDepths(tree, maxDepth);
        moveInputsToBottom(tree, maxDepth);
        assignDepths(tree, maxDepth);

        List<CircuitColumn> circuitColumns = generateCircuitColumns(tree, maxDepth);
        generateAndAddWires(nodeHandler, circuitColumns);
        addLogicElements(logicElementHandler, circuitColumns);

        //INPUTS
        int shift = neededSpace;
        boolean alreadyShifted = false;
        for (int i = 0; i < identifiersWithNoRepetitions.size(); i++) {
            Vector2D output = new Vector2D(-neededSpace, i);
            List<Double> ys = getYsForIdentifier(identifiersWithNoRepetitions.get(i), identifiers);
            List<Vector2D> inputs = new ArrayList<>();
            for (int j = 0; j < ys.size(); j++) {
                inputs.add(new Vector2D(0, ys.get(j)));
                if(output.getY() != ys.get(j) && !alreadyShifted) {
                    shift--;
                    alreadyShifted = true;
                }
            }
            alreadyShifted = false;
            connectIdentifiers(nodeHandler, output, inputs, shift);
            logicElementHandler.add(new InputGate(-neededSpace-1, i, Rotation.RIGHT));
        }

        SelectionDTO selectionDTO = new SelectionDTO();
        selectionDTO.setLogicElementHandler(logicElementHandler);
        selectionDTO.setNodeHandler(nodeHandler);

        return selectionDTO;
    }

    private List<Double> getYsForIdentifier(String identifier, List<String> allIdentifiers){
        List<Double> ys = new ArrayList<>();
        for (int i = 0; i < allIdentifiers.size(); i++) {
            if(identifier.equals(allIdentifiers.get(i))){
                ys.add((double)i);
            }
        }
        return ys;
    }

    void connectIdentifiers(NodeHandler nodeHandler, Vector2D output, List<Vector2D> inputs, int shift){
        double minY = getMinY(inputs);
        double maxY = getMaxY(inputs);
        for (int i = 0; i < shift; i++) {
            nodeHandler.setRightWire(new Vector2D(output.getX() + i, output.getY()), WireState.LOW);
        }

        for (int i = 0; i < output.getY() - minY; i++) {
            nodeHandler.setUpWire(new Vector2D(output.getX() + shift, output.getY() - i), WireState.LOW);
        }
        for (int i = 0; i < maxY - output.getY(); i++) {
            nodeHandler.setDownWire(new Vector2D(output.getX() + shift, output.getY() + i), WireState.LOW);
            if(i < maxY - output.getY() - 1) nodeHandler.setCrossing(new Vector2D(output.getX() + shift, output.getY() + i + 1), Crossing.NOT_TOUCHING);
        }

        for (int i = 0; i < inputs.size(); i++) {
            for (int j = 0; j < Math.abs(inputs.get(i).getX() - (output.getX() + shift)); j++) {
                nodeHandler.setLeftWire(new Vector2D(inputs.get(i).getX() - j, inputs.get(i).getY()), WireState.LOW);
            }
            if(nodeHandler.getLeftWire(new Vector2D(output.getX() + shift, inputs.get(i).getY())) == WireState.NONE)
                nodeHandler.setCrossing(new Vector2D(output.getX() + shift, inputs.get(i).getY()), Crossing.TOUCHING);
        }

    }

    private double getMaxY(List<Vector2D> points){
        double maxY = -Double.MAX_VALUE;
        for (int i = 0; i < points.size(); i++) {
            if(points.get(i).getY() > maxY){
                maxY = points.get(i).getY();
            }
        }
        return maxY;
    }
    private double getMinY(List<Vector2D> points){
        double minY = Double.MAX_VALUE;
        for (int i = 0; i < points.size(); i++) {
            if(points.get(i).getY() < minY){
                minY = points.get(i).getY();
            }
        }
        return minY;
    }

    private void generateAndAddWires(NodeHandler nodeHandler, List<CircuitColumn> circuitColumns){
        double sumX = 0;
        circuitColumns.get(0).setX(0);
        for (int i = 0; i < circuitColumns.size()-1; i++) {

            List<Double> outputPosYs = circuitColumns.get(i).getOutputPositionYs();
            double outputX = circuitColumns.get(i).getX();
            List<Double> inputPosYs = circuitColumns.get(i+1).getInputPositionYs();


            int space = calculateNeededSpace(outputPosYs, inputPosYs);
            sumX += space;
            circuitColumns.get(i+1).setX(sumX);
            double inputX = circuitColumns.get(i+1).getX();

            List<Boolean> crooked = getCrookedLines(outputPosYs, inputPosYs);
            double shift = 1;
            for (int j = 0; j < outputPosYs.size(); j++) {
                connect(nodeHandler, new Vector2D(outputX+1, outputPosYs.get(j)), new Vector2D(inputX, inputPosYs.get(j)), shift);
                if(crooked.get(j)) shift++;
            }

        }
    }

    private int getNeededSpaceForIdentifiers(List<String> allIdentifiers, List<String> identifierWithNoRepetitions){
        int shift = -1;
        boolean alreadyShifted = false;
        for (int i = 0; i < identifierWithNoRepetitions.size(); i++) {
            List<Double> ys = getYsForIdentifier(identifierWithNoRepetitions.get(i), allIdentifiers);
            for (int j = 0; j < ys.size(); j++) {
                if(i != ys.get(j) && !alreadyShifted) {
                    shift--;
                    alreadyShifted = true;
                }
            }
            alreadyShifted = false;
        }

        return -shift;

    }

    private void addLogicElements(LogicElementHandler logicElementHandler, List<CircuitColumn> circuitColumns){
        for (CircuitColumn circuitColumn : circuitColumns) {
            List<LogicElement> logicElements = circuitColumn.getLogicElements();
            for (LogicElement logicElement : logicElements) {
                logicElementHandler.add(logicElement);
            }
        }
    }

    private List<String> removeRepetitions(List<String> identifiers){
        List<String> used = new ArrayList<>();
        for (int i = 0; i < identifiers.size(); i++) {
            if(!used.contains(identifiers.get(i))){
                used.add(identifiers.get(i));
            }
        }
        return used;

    }

    private List<String> getIdentifiers(ExpressionNode tree){
        List<String> identifiers = new ArrayList<>();
        Stack<ExpressionNode> nodeStack = new Stack<>();
        nodeStack.add(tree);
        while (!nodeStack.isEmpty()) {
            ExpressionNode currentNode = nodeStack.pop();
            if(currentNode.getTerminalText() != null){
                identifiers.add(currentNode.getTerminalText());
            }

            if (currentNode.getSecondNode() != null) {
                nodeStack.add(currentNode.getSecondNode());
            }
            if (currentNode.getFirstNode() != null) {
                nodeStack.add(currentNode.getFirstNode());
            }
        }
        return identifiers;
    }

    private List<CircuitColumn> generateCircuitColumns(ExpressionNode tree, int maxDepth){
        List<CircuitColumn> circuitColumns = new ArrayList<>();
        for (int i = 0; i < maxDepth + 1; i++) {
            circuitColumns.add(new CircuitColumn());
        }
        Stack<ExpressionNode> nodeStack = new Stack<>();
        nodeStack.add(tree);
        while (!nodeStack.isEmpty()) {
            ExpressionNode currentNode = nodeStack.pop();
            LogicElement newLogicElement = new OrGate();
            if (currentNode.isGhost()) {
                circuitColumns.get(currentNode.getDepth()).addLogicElement(new BufferGate());
            } else if (currentNode.getOperand() != null) {
                if (currentNode.getOperand() == Operand.OR) {
                    newLogicElement = new OrGate();
                } else if (currentNode.getOperand() == Operand.AND) {
                    newLogicElement = new AndGate();
                } else if (currentNode.getOperand() == Operand.NOT) {
                    newLogicElement = new NotGate();
                }
                circuitColumns.get(currentNode.getDepth()).addLogicElement(newLogicElement);
            } else if (currentNode.getTerminalText() != null) {
                circuitColumns.get(currentNode.getDepth()).addLogicElement(new BufferGate());
            }

            if (currentNode.getSecondNode() != null) {
                nodeStack.add(currentNode.getSecondNode());
            }
            if (currentNode.getFirstNode() != null) {
                nodeStack.add(currentNode.getFirstNode());
            }
        }
        return circuitColumns;

    }

    private int calculateNeededSpace(List<Double> outputPosYs, List<Double> inputPosYs) {
        int neededSpace = 2;
        for (int i = 0; i < outputPosYs.size(); i++) {
            if(!outputPosYs.get(i).equals(inputPosYs.get(i))) neededSpace++;
        }
        return neededSpace;
    }

    private List<Boolean> getCrookedLines(List<Double> outputPosYs, List<Double> inputPosYs) {
        List<Boolean> crooked = new ArrayList<>();

        for (int i = 0; i < outputPosYs.size(); i++) {
            if(!outputPosYs.get(i).equals(inputPosYs.get(i))) crooked.add(true);
            else crooked.add(false);
        }
        return crooked;
    }

    void connect(NodeHandler nodeHandler, Vector2D start, Vector2D end, double shift) {
        if (start.getY() == end.getY()) {
            for (int i = 0; i < end.getX() - start.getX(); i++) {
                nodeHandler.setRightWire(new Vector2D(start.getX() + i, start.getY()), WireState.LOW);
            }
        } else {
            for (int i = 0; i < shift; i++) {
                nodeHandler.setRightWire(new Vector2D(start.getX() + i, start.getY()), WireState.LOW);
            }
            for (int i = 0; i < Math.abs(end.getY() - start.getY()); i++) {
                if (end.getY() > start.getY()) {
                    nodeHandler.setUpWire(new Vector2D(start.getX() + shift , start.getY() + i + 1), WireState.LOW);
                } else {
                    nodeHandler.setDownWire(new Vector2D(start.getX() + shift , start.getY() - i - 1), WireState.LOW);
                }
            }
            for (int i = (int) shift; i < end.getX() - start.getX(); i++) {
                nodeHandler.setRightWire(new Vector2D(start.getX() + i, end.getY()), WireState.LOW);
            }
        }
    }

    private void assignDepths(ExpressionNode tree, int maxDepth) {
        Stack<ExpressionNode> nodeStack = new Stack<>();
        tree.setDepth(maxDepth); //so that it counts from zero
        nodeStack.add(tree);

        //Setting depth of every node
        while (!nodeStack.isEmpty()) {
            ExpressionNode currentNode = nodeStack.pop();
            if (currentNode.getSecondNode() != null) {
                ExpressionNode node = currentNode.getSecondNode();
                node.setDepth(currentNode.getDepth() - 1);
                nodeStack.add(node);
            }
            if (currentNode.getFirstNode() != null) {
                ExpressionNode node = currentNode.getFirstNode();
                node.setDepth(currentNode.getDepth() - 1);
                nodeStack.add(node);
            }
        }
    }

    private void moveInputsToBottom(ExpressionNode tree, int maxDepth) {
        assignDepths(tree, maxDepth);

        Stack<ExpressionNode> nodeStack = new Stack<>();
        nodeStack.add(tree);
        while (!nodeStack.isEmpty()) {
            ExpressionNode currentNode = nodeStack.pop();

            if (currentNode.getFirstNode() != null) {
                if (currentNode.getFirstNode().getTerminalText() != null) {
                    if (currentNode.getFirstNode().getDepth() > 0) {
                        currentNode.getFirstNode().setFirstNode(new ExpressionNode());
                        currentNode.getFirstNode().getFirstNode().setTerminalText(currentNode.getFirstNode().getTerminalText());
                        currentNode.getFirstNode().setGhost(true);
                        currentNode.getFirstNode().getFirstNode().setDepth(currentNode.getFirstNode().getDepth() - 1);
                    }
                }
            }
            if (currentNode.getSecondNode() != null) {
                if (currentNode.getSecondNode().getTerminalText() != null) {
                    if (currentNode.getSecondNode().getDepth() > 0) {
                        currentNode.getSecondNode().setFirstNode(new ExpressionNode());
                        currentNode.getSecondNode().getFirstNode().setTerminalText(currentNode.getSecondNode().getTerminalText());
                        currentNode.getSecondNode().setGhost(true);
                        currentNode.getSecondNode().getFirstNode().setDepth(currentNode.getSecondNode().getDepth() - 1);
                    }
                }
            }

            if (currentNode.getSecondNode() != null) {
                nodeStack.add(currentNode.getSecondNode());
            }
            if (currentNode.getFirstNode() != null) {
                nodeStack.add(currentNode.getFirstNode());
            }
        }

    }

    private int findMaxDepth(ExpressionNode tree) {
        Stack<ExpressionNode> nodeStack = new Stack<>();
        int maxDepth = 0;
        tree.setDepth(0); //so that it counts from zero
        nodeStack.add(tree);

        //Setting depth of every node
        while (!nodeStack.isEmpty()) {
            ExpressionNode currentNode = nodeStack.pop();
            if (currentNode.getSecondNode() != null) {
                ExpressionNode node = currentNode.getSecondNode();
                node.setDepth(currentNode.getDepth() - 1);
                maxDepth = Math.min(maxDepth, currentNode.getDepth() - 1);
                nodeStack.add(node);
            }
            if (currentNode.getFirstNode() != null) {
                ExpressionNode node = currentNode.getFirstNode();
                node.setDepth(currentNode.getDepth() - 1);
                maxDepth = Math.min(maxDepth, currentNode.getDepth() - 1);
                nodeStack.add(node);
            }
        }
        return -maxDepth;
    }
}
