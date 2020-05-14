package LogicCircuitSimulator.FxGUI.GridMouseSpecifiers;

import LogicCircuitSimulator.FxGUI.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.LogicElements.LogicElement;
import LogicCircuitSimulator.LogicElements.Rotation;
import LogicCircuitSimulator.Simulation;
import LogicCircuitSimulator.Vector2D;

import java.util.Iterator;

public abstract class MouseLogicElementSpecifier {

    private final Simulation simulation;
    private Vector2D currentLogicElementPos;
    private LogicElement currentLogicElement;

    public MouseLogicElementSpecifier(Simulation simulation){
        this.simulation = simulation;
    }

    abstract public void transformLogicElement();

    public void getElementFromMousePosition(Vector2D mousePos, Projection2D projection){
        Vector2D nodePos = getNodePosition(mousePos, projection);
        Vector2D cursorBoardPosition = getExactBoardPosition(mousePos, projection);
        LogicElement logicElement = calculateLogicElement(nodePos, cursorBoardPosition, simulation.logicElementIterator());
        if (logicElement != null) {
            currentLogicElementPos = logicElement.getPosition();
            currentLogicElement = logicElement;

            transformLogicElement();
        }

    }

    public void getElementPosFromElementAndMousePosition(Vector2D mousePos, Projection2D projection, LogicElement logicElement){
        Vector2D pos = getNodePosition(mousePos, projection);
        if(logicElement.getRotation() == Rotation.DOWN){
            pos = new Vector2D(pos.getX(), pos.getY() - 0.5);
        }
        else if(logicElement.getRotation() == Rotation.UP){
            pos = new Vector2D(pos.getX(), pos.getY() - 0.5);
        }
        currentLogicElementPos = pos;

        transformLogicElement();
    }
    //TODO: performWhenFound
    //TODO: alwaysPerform

    private Vector2D getExactBoardPosition(Vector2D mousePos, Projection2D projection){
        return projection.projectBack(new Vector2D(mousePos.getX(), mousePos.getY()));
    }

    private Vector2D getNodePosition(Vector2D mousePos, Projection2D projection){
        Vector2D pos = projection.projectBack(new Vector2D(mousePos.getX(), mousePos.getY()));
        int x = (int)pos.getX();
        int y = (int)pos.getY();
        Vector2D nodePos;


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

        if(yFraction>0.50){
            nodePos = new Vector2D(x, y+1);
        }
        else{
            nodePos = new Vector2D(x, y);
        }
        return nodePos;
    }

    private Vector2D calculateLogicElementPos(Vector2D cursorBoardPos, LogicElement logicElement, Projection2D projection2D, Vector2D nodePos){

        Vector2D pos = nodePos;
        if(logicElement.getRotation() == Rotation.DOWN){
            pos = new Vector2D(pos.getX(), pos.getY() - 0.5);
        }
        else if(logicElement.getRotation() == Rotation.UP){
            pos = new Vector2D(pos.getX(), pos.getY() - 0.5);
        }

        return pos;
    }

    private LogicElement calculateLogicElement(Vector2D nodePos, Vector2D cursorBoardPos, Iterator<LogicElement> logicElements){
        while(logicElements.hasNext()){
            LogicElement logicElement = logicElements.next();
            int widthOnBoard = getWidthOnBoard(logicElement);
            int heightOnBoard = getHeightOnBoard(logicElement);

            if(logicElement.getRotation() == Rotation.DOWN
                    //horizontal axis
                    && cursorBoardPos.getX() + 0.5 > logicElement.getPosition().getX()
                    && cursorBoardPos.getX() + 0.5 < logicElement.getPosition().getX() + widthOnBoard
                    //vertical axis
                    && cursorBoardPos.getY() > logicElement.getPosition().getY()
                    && cursorBoardPos.getY() < logicElement.getPosition().getY() + heightOnBoard){
                System.out.println("INSIDE SPECIFIER");
                System.out.println(logicElement.getPosition().getY());
                return logicElement;
            }
            else if(logicElement.getRotation() == Rotation.RIGHT
                    //horizontal axis
                    && cursorBoardPos.getX() > logicElement.getPosition().getX()
                    && cursorBoardPos.getX() < logicElement.getPosition().getX() + widthOnBoard
                    //vertical axis
                    && cursorBoardPos.getY() + 0.5 > logicElement.getPosition().getY()
                    && cursorBoardPos.getY() + 0.5 < logicElement.getPosition().getY() + heightOnBoard){
                return logicElement;
            }
            else if(logicElement.getRotation() == Rotation.LEFT
                    //horizontal axis
                    && cursorBoardPos.getX() > logicElement.getPosition().getX()
                    && cursorBoardPos.getX() < logicElement.getPosition().getX() + widthOnBoard
                    //vertical axis
                    && cursorBoardPos.getY() + 0.5 > logicElement.getPosition().getY()
                    && cursorBoardPos.getY() + 0.5 < logicElement.getPosition().getY() + heightOnBoard){
                return logicElement;
            }
            else if(logicElement.getRotation() == Rotation.UP
                    //horizontal axis
                    && cursorBoardPos.getX() + 0.5 > logicElement.getPosition().getX()
                    && cursorBoardPos.getX() + 0.5 < logicElement.getPosition().getX() + widthOnBoard
                    //vertical axis
                    && cursorBoardPos.getY() > logicElement.getPosition().getY()
                    && cursorBoardPos.getY() < logicElement.getPosition().getY() + heightOnBoard){
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

    public int getHeightOnBoard(LogicElement logicElement){
        if(logicElement.getRotation() == Rotation.RIGHT || logicElement.getRotation() == Rotation.LEFT){
            //on the board it looks like a gate has height
            //but in the system it has none
            //it's a line from a to b, no height
            return logicElement.getElementHeight() + 1;
        }
        else return logicElement.getElementWidth();
    }

    public int getWidthOnBoard(LogicElement logicElement){
        if(logicElement.getRotation() == Rotation.RIGHT || logicElement.getRotation() == Rotation.LEFT){
            return logicElement.getElementWidth();
        }
        //on the board it looks like a gate has height
        //but in the system it has none
        //it's a line from a to b, no height
        else return logicElement.getElementHeight() + 1;
    }

}
