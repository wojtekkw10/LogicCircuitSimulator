package LogicCircuitSimulator.BooleanExpressionParser.CircuitGenerator;

import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElementFactory;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class CircuitColumn {
    private final List<LogicElement> logicElements = new ArrayList<>();
    private double yPosCounter = 0;
    private double x;

    public void addLogicElement(LogicElement logicElement){
        logicElement.setPosition(new Vector2D(logicElement.getX(), yPosCounter));
        int height = getHeightOnBoard(logicElement);
        yPosCounter += height;
        logicElements.add(logicElement);

    }

    public List<LogicElement> getLogicElements() {
        List<LogicElement> logicElements = new ArrayList<>();
        for (int i = 0; i < this.logicElements.size(); i++) {
            LogicElement newLogicElement = LogicElementFactory.instance(this.logicElements.get(i));
            newLogicElement.setPosition(new Vector2D(x, newLogicElement.getY()));
            logicElements.add(newLogicElement);
        }
        return logicElements;
    }

    public List<Double> getOutputPositionYs(){
        List<Double> allOutputPositions = new ArrayList<>();
        for (int i = 0; i < logicElements.size(); i++) {
            List<Vector2D> outputPositions = logicElements.get(i).getGeometry().getOutputPositions();
            for (int j = 0; j < outputPositions.size(); j++) {
                allOutputPositions.add(outputPositions.get(j).getY());
            }
        }
        return allOutputPositions;
    }

    public List<Double> getInputPositionYs(){
        List<Double> allInputPositions = new ArrayList<>();
        for (int i = 0; i < logicElements.size(); i++) {
            List<Vector2D> inputPositions = logicElements.get(i).getGeometry().getInputPositions();
            for (int j = 0; j < inputPositions.size(); j++) {
                allInputPositions.add(inputPositions.get(j).getY());
            }
        }
        return allInputPositions;
    }

    private int getHeightOnBoard(LogicElement logicElement) {
        if (logicElement.getRotation() == Rotation.RIGHT || logicElement.getRotation() == Rotation.LEFT) {
            //on the board it looks like a gate has height
            //but in the system it has none
            //it's a line from a to b, no height
            return logicElement.getGeometry().getElementHeight() + 1;
        } else return logicElement.getGeometry().getElementWidth();
    }

    private int getWidthOnBoard(LogicElement logicElement) {
        if (logicElement.getRotation() == Rotation.RIGHT || logicElement.getRotation() == Rotation.LEFT) {
            return logicElement.getGeometry().getElementWidth();
        }
        //on the board it looks like a gate has height
        //but in the system it has none
        //it's a line from a to b, no height
        else return logicElement.getGeometry().getElementHeight() + 1;
    }

    @Override
    public String toString() {
        return "CircuitColumn{" +
                "logicElements=" + logicElements +
                ", yPosCounter=" + yPosCounter +
                '}';
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }
}
