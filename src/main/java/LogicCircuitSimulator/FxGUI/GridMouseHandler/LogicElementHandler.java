package LogicCircuitSimulator.FxGUI.GridMouseHandler;

import LogicCircuitSimulator.LogicElements.LogicElement;
import LogicCircuitSimulator.LogicElements.Rotation;
import LogicCircuitSimulator.Simulation;
import LogicCircuitSimulator.Utils.MatrixOperations;
import LogicCircuitSimulator.Vector2D;
import LogicCircuitSimulator.WireGrid.Node;
import org.ejml.simple.SimpleMatrix;

import java.util.Iterator;

public abstract class LogicElementHandler {

    private final Simulation simulation;
    private Vector2D currentLogicElementPos;
    private LogicElement currentLogicElement;

    public LogicElementHandler(Simulation simulation){
        this.simulation = simulation;
    }

    abstract public void transformLogicElement();

    public void performTransformation(Vector2D mousePos, SimpleMatrix projectionMatrix){
        Vector2D nodePos = getNodePosition(mousePos, projectionMatrix);
        System.out.println("NODE POS: "+nodePos);
        LogicElement logicElement = getLogicElement(nodePos, simulation.logicElementIterator());
        if (logicElement != null) {
            int width = logicElement.getElementWidth();
            int height = logicElement.getElementWidth();

            currentLogicElementPos = nodePos;
            currentLogicElement = logicElement;

            transformLogicElement();
        }

    }

    public void performNoTransformation(Vector2D mousePos, SimpleMatrix projectionMatrix){
        Vector2D nodePos = getNodePosition(mousePos, projectionMatrix);

        System.out.println("NODE POS: "+nodePos);
        currentLogicElementPos = nodePos;

        transformLogicElement();
    }

    private Vector2D getNodePosition(Vector2D mousePos, SimpleMatrix projectionMatrix){
        Vector2D pos = MatrixOperations.getVectorFromVectorMatrix(projectionMatrix.invert().mult(MatrixOperations.getVectorMatrix(mousePos.getX(), mousePos.getY())));
        int x = (int)pos.getX();
        int y = (int)pos.getY();
        Vector2D nodePos = new Vector2D(x,y);


        double xFraction = pos.getX() - x;
        double yFraction = pos.getY() - y;

        if(xFraction < 0) {
            xFraction = 1-(-xFraction);
            x--;
        }
        if(yFraction < 0) {
            yFraction = 1-(-yFraction);
            y--;
        }
        //if(y<0) y++;
        System.out.println(x);

        if(yFraction>0.50){
            System.out.println("HAH"+yFraction);
            nodePos = new Vector2D(x, y+1);
        }
        else{
            nodePos = new Vector2D(x, y);
        }
        return nodePos;
    }

    private LogicElement getLogicElement(Vector2D pos, Iterator<LogicElement> logicElements){
        while(logicElements.hasNext()){
            LogicElement logicElement = logicElements.next();
            if(logicElement.getPosition().equals(pos)){
                return logicElement;
            }
        }
        return null;
    }

    public void rotateLogicElementClockwise(){
        Iterator<LogicElement> logicElements = simulation.logicElementIterator();
        while(logicElements.hasNext()){
            LogicElement logicElement = logicElements.next();
            if(logicElement.getPosition().equals(currentLogicElementPos)){
                if(logicElement.getRotation() == Rotation.RIGHT) logicElement.setRotation(Rotation.DOWN);
                else if(logicElement.getRotation() == Rotation.DOWN) logicElement.setRotation(Rotation.LEFT);
                else if(logicElement.getRotation() == Rotation.LEFT) logicElement.setRotation(Rotation.UP);
                else if(logicElement.getRotation() == Rotation.UP) logicElement.setRotation(Rotation.RIGHT);
            }
        }
    }

    public LogicElement getLogicElement(){
        return currentLogicElement;
    }

    public void removeLogicElement(){
        Iterator<LogicElement> logicElements = simulation.logicElementIterator();
        while(logicElements.hasNext()){
            LogicElement logicElement = logicElements.next();
            if(logicElement.getPosition().equals(currentLogicElementPos)){
                logicElements.remove();
            }
        }
    }

    public Vector2D getPosition(){
        return currentLogicElementPos;
    }

}
