package LogicCircuitSimulator.BooleanExpressionParser.CircuitGenerator;

import LogicCircuitSimulator.BooleanExpressionParser.BooleanExpressionTree.ExpressionNode;
import LogicCircuitSimulator.BooleanExpressionParser.BooleanExpressionTree.ExpressionTreeGenerator;
import LogicCircuitSimulator.BooleanExpressionParser.BooleanExpressionTree.Operand;
import LogicCircuitSimulator.BooleanExpressionParser.BooleanExpressionTree.SimpleExpressionTreeGenerator;
import LogicCircuitSimulator.BooleanExpressionParser.CircuitDepthCounter;
import LogicCircuitSimulator.BooleanExpressionParser.ExpressionDepthFinder.ExpressionDepthFinder;
import LogicCircuitSimulator.BooleanExpressionParser.ExpressionDepthFinder.SimpleExpressionDepthFinder;
import LogicCircuitSimulator.BooleanExpressionParser.ExpressionSimplifier.ExpressionSimplifier;
import LogicCircuitSimulator.BooleanExpressionParser.ExpressionSimplifier.SimpleExpressionSimplifier;
import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SelectionDTO;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElementHandler;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.*;
import LogicCircuitSimulator.Simulation.LogicElementHandler.SimpleLogicElementHandler;
import LogicCircuitSimulator.Simulation.NodeHandler.ArrayNodeHandler;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeHandler;
import LogicCircuitSimulator.Simulation.NodeHandler.WireState;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Vector2D;
import org.antlr.v4.runtime.atn.ActionTransition;

import java.util.*;

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

        assignDepths(tree, maxDepth);
        moveInputsToBottom(tree, maxDepth);
        assignDepths(tree, maxDepth);

        List<List<LogicElement>> logicElementColumns = putLogicElementsIntoColumns(tree);
        List<Integer> columnXs = new ArrayList<>();
        List<CircuitColumn> circuitColumns = new ArrayList<>();
        System.out.println(logicElementColumns);

        List<List<Double>> inputPositions = new ArrayList<>();
        List<List<Double>> outputPositions = new ArrayList<>();

        for (int i = 0; i < maxDepth + 1; i++) {
            columnXs.add(0);
            circuitColumns.add(new CircuitColumn());
        }

        Stack<ExpressionNode> nodeStack = new Stack<>();
        nodeStack.add(tree);
        while (!nodeStack.isEmpty()) {
            ExpressionNode currentNode = nodeStack.pop();
            int x = generateColumnXs(currentNode.getDepth(), logicElementColumns);
            circuitColumns.get(currentNode.getDepth()).setX(x);
            columnXs.set(currentNode.getDepth(), x);

            x = 0;
            int y = 0;
            LogicElement newLogicElement = new OrGate(x, y, Rotation.RIGHT);
            if (currentNode.isGhost()) {
                circuitColumns.get(currentNode.getDepth()).addLogicElement(new BufferGate(x, y, Rotation.RIGHT));
            } else if (currentNode.getOperand() != null) {
                if (currentNode.getOperand() == Operand.OR) {
                    newLogicElement = new OrGate(x, y, Rotation.RIGHT);
                } else if (currentNode.getOperand() == Operand.AND) {
                    newLogicElement = new AndGate(x, y, Rotation.RIGHT);
                } else if (currentNode.getOperand() == Operand.NOT) {
                    newLogicElement = new NotGate(x, y, Rotation.RIGHT);
                }
                circuitColumns.get(currentNode.getDepth()).addLogicElement(newLogicElement);
            } else if (currentNode.getTerminalText() != null) {
                circuitColumns.get(currentNode.getDepth()).addLogicElement(new InputGate(x, y, Rotation.RIGHT));
            }

            if (currentNode.getSecondNode() != null) {
                nodeStack.add(currentNode.getSecondNode());
            }
            if (currentNode.getFirstNode() != null) {
                nodeStack.add(currentNode.getFirstNode());
            }
        }

        for (int i = 0; i < circuitColumns.size(); i++) {
            outputPositions.add(circuitColumns.get(i).getOutputPositionYs());
            inputPositions.add(circuitColumns.get(i).getInputPositionYs());
        }


        System.out.println("INPUT: " + inputPositions);
        System.out.println("OUTPUT: " + outputPositions);
        System.out.println("__");

        System.out.println(circuitColumns.size());
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
                System.out.println("CONNECTING: "+new Vector2D(outputX+1, outputPosYs.get(j)) + " "+new Vector2D(inputX, inputPosYs.get(j)));
                if(crooked.get(j)) shift++;
            }




            System.out.println("NEEDED SPACE: "+space);
        }

        for (CircuitColumn circuitColumn : circuitColumns) {
            List<LogicElement> logicElements = circuitColumn.getLogicElements();
            for (LogicElement logicElement : logicElements) {
                logicElementHandler.add(logicElement);
            }
        }

        SelectionDTO selectionDTO = new SelectionDTO();
        selectionDTO.setLogicElementHandler(logicElementHandler);
        selectionDTO.setNodeHandler(nodeHandler);

        return selectionDTO;
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


    private int generateColumnXs(int depth, List<List<LogicElement>> columns){
        int x = 0;
        for (int i = 0; i < depth; i++) {
            x += columns.get(i).size() + 2;
        }
        return x;
    }

    boolean connect(NodeHandler nodeHandler, Vector2D start, Vector2D end, double shift) {
        //nodeHandler.setLeftWire(start, WireState.LOW);
        //nodeHandler.setRightWire(end, WireState.LOW);
        boolean conflict = false;
        boolean goesUp = false;
        if (start.getY() == end.getY()) {
            for (int i = 0; i < end.getX() - start.getX(); i++) {
                double x = start.getX() + i;
                if (nodeHandler.getRightWire(new Vector2D(start.getX() + i, start.getY())) != WireState.NONE)
                    conflict = true;
                nodeHandler.setRightWire(new Vector2D(start.getX() + i, start.getY()), WireState.LOW);
                if(x >= end.getX()) conflict = true;
            }
        } else {
            for (int i = 0; i < shift; i++) {
                if (nodeHandler.getRightWire(new Vector2D(start.getX() + i, start.getY())) != WireState.NONE)
                    conflict = true;
                double x = start.getX() + i;
                nodeHandler.setRightWire(new Vector2D(x, start.getY()), WireState.LOW);
                if(x >= end.getX()) conflict = true;
            }
            for (int i = 0; i < Math.abs(end.getY() - start.getY()); i++) {
                double x = start.getX() + shift;
                if (end.getY() > start.getY()) {
                    if (nodeHandler.getUpWire(new Vector2D(x , start.getY() + i + 1)) != WireState.NONE)
                        conflict = true;
                    nodeHandler.setUpWire(new Vector2D(x , start.getY() + i + 1), WireState.LOW);
                    goesUp = true;
                } else {
                    if (nodeHandler.getDownWire(new Vector2D(x , start.getY() - i - 1)) != WireState.NONE)
                        conflict = true;
                    goesUp = true;
                    nodeHandler.setDownWire(new Vector2D(x , start.getY() - i - 1), WireState.LOW);
                }
                if(x >= end.getX()) conflict = true;
            }
            for (int i = (int) shift; i < end.getX() - start.getX(); i++) {
                double x = start.getX() + i;
                if (nodeHandler.getRightWire(new Vector2D(x, start.getY())) != WireState.NONE)
                    conflict = true;
                nodeHandler.setRightWire(new Vector2D(x, end.getY()), WireState.LOW);
                if(x >= end.getX()) conflict = true;
            }
        }
        return goesUp;

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

    public int getHeightOnBoard(LogicElement logicElement) {
        if (logicElement.getRotation() == Rotation.RIGHT || logicElement.getRotation() == Rotation.LEFT) {
            //on the board it looks like a gate has height
            //but in the system it has none
            //it's a line from a to b, no height
            return logicElement.getGeometry().getElementHeight() + 1;
        } else return logicElement.getGeometry().getElementWidth();
    }

    public int getWidthOnBoard(LogicElement logicElement) {
        if (logicElement.getRotation() == Rotation.RIGHT || logicElement.getRotation() == Rotation.LEFT) {
            return logicElement.getGeometry().getElementWidth();
        }
        //on the board it looks like a gate has height
        //but in the system it has none
        //it's a line from a to b, no height
        else return logicElement.getGeometry().getElementHeight() + 1;
    }

    private List<List<LogicElement>> putLogicElementsIntoColumns(ExpressionNode tree) {
        List<List<LogicElement>> columns = new ArrayList<>();

        Stack<ExpressionNode> nodeStack = new Stack<>();
        nodeStack.add(tree);
        while (!nodeStack.isEmpty()) {
            ExpressionNode currentNode = nodeStack.pop();

            int x = currentNode.getDepth();
            while (columns.size() <= x) columns.add(new ArrayList<>());
            LogicElement newLogicElement = new OrGate(0, 0, Rotation.RIGHT);
            if (currentNode.getOperand() != null) {
                if (currentNode.getOperand() == Operand.OR) {
                    newLogicElement = new OrGate(0, 0, Rotation.RIGHT);
                } else if (currentNode.getOperand() == Operand.AND) {
                    newLogicElement = new AndGate(0, 0, Rotation.RIGHT);
                } else if (currentNode.getOperand() == Operand.NOT) {
                    newLogicElement = new NotGate(0, 0, Rotation.RIGHT);
                }
                columns.get(x).add(newLogicElement);
            }

            if (currentNode.getTerminalText() != null) {
                columns.get(x).add(new InputGate(0, 0, Rotation.RIGHT));
            }

            if (currentNode.getSecondNode() != null) {
                nodeStack.add(currentNode.getSecondNode());
            }
            if (currentNode.getFirstNode() != null) {
                nodeStack.add(currentNode.getFirstNode());
            }
        }
        return columns;
    }

    private int getLogicElementColumnX(int depth, List<Integer> columnXs) {
        int x = 0;
        for (int i = 0; i < depth; i++) {
            x += columnXs.get(i) + 2;
        }
        return columnXs.get(depth);
    }

    private void moveInputsToBottom(ExpressionNode tree, int maxDepth) {
        assignDepths(tree, maxDepth);

        System.out.println("MOVING INPUTS TO BOTTOM");
        Stack<ExpressionNode> nodeStack = new Stack<>();
        nodeStack.add(tree);
        while (!nodeStack.isEmpty()) {
            ExpressionNode currentNode = nodeStack.pop();
            System.out.println(currentNode);

            if (currentNode.getFirstNode() != null) {
                if (currentNode.getFirstNode().getTerminalText() != null) {
                    if (currentNode.getFirstNode().getDepth() > 0) {
                        currentNode.getFirstNode().setFirstNode(new ExpressionNode());
                        currentNode.getFirstNode().getFirstNode().setTerminalText(currentNode.getFirstNode().getTerminalText());
                        currentNode.getFirstNode().setGhost(true);
                        currentNode.getFirstNode().getFirstNode().setDepth(currentNode.getFirstNode().getDepth() - 1);
                        System.out.println("BUM: " + currentNode);
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
                        System.out.println("BUM: " + currentNode);
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
