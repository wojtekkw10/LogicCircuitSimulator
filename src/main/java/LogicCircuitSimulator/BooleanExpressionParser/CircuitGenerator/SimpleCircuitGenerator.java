package LogicCircuitSimulator.BooleanExpressionParser.CircuitGenerator;

import LogicCircuitSimulator.BooleanExpressionParser.BooleanExpressionTree.ExpressionNode;
import LogicCircuitSimulator.BooleanExpressionParser.BooleanExpressionTree.ExpressionTreeGenerator;
import LogicCircuitSimulator.BooleanExpressionParser.BooleanExpressionTree.Operand;
import LogicCircuitSimulator.BooleanExpressionParser.BooleanExpressionTree.SimpleExpressionTreeGenerator;
import LogicCircuitSimulator.BooleanExpressionParser.CircuitDepthCounter;
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

import java.util.*;

public class SimpleCircuitGenerator implements CircuitGenerator{
    @Override
    public SelectionDTO generate(String expression) {
        NodeHandler nodeHandler = new ArrayNodeHandler();
        LogicElementHandler logicElementHandler = new SimpleLogicElementHandler();

        ExpressionSimplifier simplifier = new SimpleExpressionSimplifier();
        String simplifiedExpression = simplifier.simplify(expression);

        ExpressionTreeGenerator treeGenerator = new SimpleExpressionTreeGenerator();
        ExpressionNode tree = treeGenerator.generate(simplifiedExpression);
        CircuitDepthCounter depthCounter = new CircuitDepthCounter();
        int depth = depthCounter.getDepth(simplifiedExpression) - 1;

        assignDepths(tree, depth);

        List<List<LogicElement>> logicElementColumns = putLogicElementsIntoColumns(tree);
        System.out.println(logicElementColumns);


        List<List<Double>> inputPositions = new ArrayList<>();
        List<List<Double>> outputPositions = new ArrayList<>();

        List<Integer> yLogicElementPositionCounter = new ArrayList<>();
        for (int i = 0; i < depth + 1; i++) {
            yLogicElementPositionCounter.add(0);
            inputPositions.add(new ArrayList<>());
            outputPositions.add(new ArrayList<>());
        }

        Stack<ExpressionNode> nodeStack = new Stack<>();
        nodeStack.add(tree);
        while(!nodeStack.isEmpty()){
            ExpressionNode currentNode = nodeStack.pop();
            int x = getLogicElementColumnX(currentNode.getDepth(), logicElementColumns);
            System.out.println(x);
            int y = yLogicElementPositionCounter.get(currentNode.getDepth());
            LogicElement newLogicElement = new OrGate(x, y, Rotation.RIGHT);
            if(currentNode.getOperand() != null){
                if(currentNode.getOperand() == Operand.OR){
                    newLogicElement = new OrGate(x, y, Rotation.RIGHT);
                }
                else if(currentNode.getOperand() == Operand.AND){
                    newLogicElement = new AndGate(x, y, Rotation.RIGHT);
                }
                else if(currentNode.getOperand() == Operand.NOT){
                    newLogicElement = new NotGate(x, y, Rotation.RIGHT);
                }
                yLogicElementPositionCounter.set(currentNode.getDepth(), yLogicElementPositionCounter.get(currentNode.getDepth())
                        + getHeightOnBoard(newLogicElement));
                logicElementHandler.add(newLogicElement);

            }

            if(currentNode.getTerminalText() != null){
                logicElementHandler.add(new InputGate(x, y, Rotation.RIGHT));
                yLogicElementPositionCounter.set(currentNode.getDepth(), yLogicElementPositionCounter.get(currentNode.getDepth()) + 1);
            }

            List<Vector2D> logicElementInputPositions = logicElementHandler.get(new Vector2D(x, y)).get().getGeometry().getInputPositions();
            for (int i = 0; i < logicElementInputPositions.size(); i++) {
                inputPositions.get(currentNode.getDepth()).add(logicElementInputPositions.get(i).getY());
            }

            List<Vector2D> logicElementOutputPositions = logicElementHandler.get(new Vector2D(x, y)).get().getGeometry().getOutputPositions();
            for (int i = 0; i < logicElementOutputPositions.size(); i++) {
                outputPositions.get(currentNode.getDepth()).add(logicElementOutputPositions.get(i).getY());
            }



            if(currentNode.getSecondNode() != null) {
                nodeStack.add(currentNode.getSecondNode());
            }
            if(currentNode.getFirstNode() != null) {
                nodeStack.add(currentNode.getFirstNode());
            }
        }

        System.out.println("INPUT: "+inputPositions);
        System.out.println("OUTPUT: "+outputPositions);
        System.out.println("__");

        List<List<List<Vector2D>>> pointsToConnect = new ArrayList<>();
        //Adding wires
        for (int i = 1; i < inputPositions.size(); i++) {
            List<Double> outPositions = outputPositions.get(i-1);
            List<Double> inPositions = inputPositions.get(i);

            List<List<Vector2D>> columnsPoints = new ArrayList<>();
            for (int j = 0; j < outPositions.size(); j++) {
                double outY = outPositions.get(j);
                double inY = inPositions.get(j);

                int outX = getLogicElementColumnX(i-1, logicElementColumns) + 1;
                int inX = getLogicElementColumnX(i, logicElementColumns);

                nodeHandler.setRightWire(new Vector2D(outX, outY), WireState.LOW);
                nodeHandler.setLeftWire(new Vector2D(inX, inY), WireState.LOW);

                List<Vector2D> twoPoints = new ArrayList<>();
                twoPoints.add(new Vector2D(outX, outY));
                twoPoints.add(new Vector2D(inX-1, inY));
                columnsPoints.add(twoPoints);
            }
            pointsToConnect.add(columnsPoints);
        }

        for (int i = 0; i < pointsToConnect.size(); i++) {
            boolean conflict = false;
            NodeHandler tempNodeHandler = new ArrayNodeHandler();
            for (int j = 0; j < pointsToConnect.get(i).size(); j++) {
                boolean conf = connect(tempNodeHandler, pointsToConnect.get(i).get(j).get(0), pointsToConnect.get(i).get(j).get(1), j+1);
                if(conf) conflict = true;
            }

            if(!conflict){
                for (int j = 0; j < pointsToConnect.get(i).size(); j++) {
                    connect(nodeHandler, pointsToConnect.get(i).get(j).get(0), pointsToConnect.get(i).get(j).get(1), j+1);
                }
            }
            else{
                System.out.println("CONFLICT: "+i);
                for (int j = 0, k = pointsToConnect.get(i).size() - 1; j < pointsToConnect.get(i).size(); j++, k--) {
                    connect(nodeHandler, pointsToConnect.get(i).get(j).get(0), pointsToConnect.get(i).get(j).get(1), k+1);
                }
            }
        }



        SelectionDTO selectionDTO = new SelectionDTO();
        selectionDTO.setLogicElementHandler(logicElementHandler);
        selectionDTO.setNodeHandler(nodeHandler);

        return selectionDTO;
    }

    boolean connect(NodeHandler nodeHandler, Vector2D start, Vector2D end, double shift){
        boolean conflict = false;
        if(start.getY() == end.getY()){
            for (int i = 0; i < end.getX() - start.getX(); i++) {
                if(nodeHandler.getRightWire(new Vector2D(start.getX()+i, start.getY())) != WireState.NONE) conflict = true;
                nodeHandler.setRightWire(new Vector2D(start.getX()+i, start.getY()), WireState.LOW);
            }
        }
        else{
            for (int i = 0; i < shift; i++) {
                if(nodeHandler.getRightWire(new Vector2D(start.getX()+i, start.getY())) != WireState.NONE) conflict = true;
                nodeHandler.setRightWire(new Vector2D(start.getX()+i, start.getY()), WireState.LOW);
            }
            for (int i = 0; i < Math.abs(end.getY() - start.getY()); i++) {
                if(end.getY() > start.getY()) {
                    if(nodeHandler.getUpWire(new Vector2D(start.getX()+shift, start.getY()+i+1)) != WireState.NONE) conflict = true;
                    nodeHandler.setUpWire(new Vector2D(start.getX()+shift, start.getY()+i+1), WireState.LOW);
                }
                else {
                    if(nodeHandler.getDownWire(new Vector2D(start.getX()+shift, start.getY()-i-1)) != WireState.NONE) conflict = true;
                    nodeHandler.setDownWire(new Vector2D(start.getX()+shift, start.getY()-i-1), WireState.LOW);
                }
            }
            for (int i = (int)shift; i < end.getX() - start.getX(); i++) {
                if(nodeHandler.getRightWire(new Vector2D(start.getX()+i, start.getY())) != WireState.NONE) conflict = true;
                nodeHandler.setRightWire(new Vector2D(start.getX()+i, end.getY()), WireState.LOW);
            }
        }
        return conflict;

    }

    private void assignDepths(ExpressionNode tree, int maxDepth){
        Stack<ExpressionNode> nodeStack = new Stack<>();
        tree.setDepth(maxDepth); //so that it counts from zero
        nodeStack.add(tree);

        //Setting depth of every node
        while(!nodeStack.isEmpty()){
            ExpressionNode currentNode = nodeStack.pop();
            if(currentNode.getSecondNode() != null) {
                ExpressionNode node = currentNode.getSecondNode();
                node.setDepth(currentNode.getDepth()-1);
                nodeStack.add(node);
            }
            if(currentNode.getFirstNode() != null) {
                ExpressionNode node = currentNode.getFirstNode();
                node.setDepth(currentNode.getDepth()-1);
                nodeStack.add(node);
            }
        }
    }

    public int getHeightOnBoard(LogicElement logicElement){
        if(logicElement.getRotation() == Rotation.RIGHT || logicElement.getRotation() == Rotation.LEFT){
            //on the board it looks like a gate has height
            //but in the system it has none
            //it's a line from a to b, no height
            return logicElement.getGeometry().getElementHeight() + 1;
        }
        else return logicElement.getGeometry().getElementWidth();
    }

    public int getWidthOnBoard(LogicElement logicElement){
        if(logicElement.getRotation() == Rotation.RIGHT || logicElement.getRotation() == Rotation.LEFT){
            return logicElement.getGeometry().getElementWidth();
        }
        //on the board it looks like a gate has height
        //but in the system it has none
        //it's a line from a to b, no height
        else return logicElement.getGeometry().getElementHeight() + 1;
    }

    private List<List<LogicElement>> putLogicElementsIntoColumns(ExpressionNode tree){
        List<List<LogicElement>> columns = new ArrayList<>();

        Stack<ExpressionNode> nodeStack = new Stack<>();
        nodeStack.add(tree);
        while(!nodeStack.isEmpty()){
            ExpressionNode currentNode = nodeStack.pop();

            int x = currentNode.getDepth();
            while(columns.size() <= x) columns.add(new ArrayList<>());
            LogicElement newLogicElement = new OrGate(0, 0, Rotation.RIGHT);
            if(currentNode.getOperand() != null){
                if(currentNode.getOperand() == Operand.OR){
                    newLogicElement = new OrGate(0, 0, Rotation.RIGHT);
                }
                else if(currentNode.getOperand() == Operand.AND){
                    newLogicElement = new AndGate(0, 0, Rotation.RIGHT);
                }
                else if(currentNode.getOperand() == Operand.NOT){
                    newLogicElement = new NotGate(0, 0, Rotation.RIGHT);
                }
                columns.get(x).add(newLogicElement);
            }

            if(currentNode.getTerminalText() != null){
                columns.get(x).add(new InputGate(0,0, Rotation.RIGHT));
            }

            if(currentNode.getSecondNode() != null) {
                nodeStack.add(currentNode.getSecondNode());
            }
            if(currentNode.getFirstNode() != null) {
                nodeStack.add(currentNode.getFirstNode());
            }
        }
        return columns;
    }

    private int getLogicElementColumnX(int depth, List<List<LogicElement>> columns){
        int x = 0;
        for (int i = 0; i < depth; i++) {
            x += columns.get(i).size() + 2;
        }
        return x;
    }


}
