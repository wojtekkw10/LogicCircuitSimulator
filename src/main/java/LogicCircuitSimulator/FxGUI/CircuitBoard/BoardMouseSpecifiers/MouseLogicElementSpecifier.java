package LogicCircuitSimulator.FxGUI.CircuitBoard.BoardMouseSpecifiers;

import LogicCircuitSimulator.FxGUI.CircuitBoard.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.Simulation.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Simulation.Simulation;
import LogicCircuitSimulator.Vector2D;

import java.util.Iterator;

public abstract class MouseLogicElementSpecifier {

    private final Simulation simulation;
    private Vector2D currentLogicElementPos;
    private LogicElement currentLogicElement;



    private Vector2D relativeMousePos;

    public MouseLogicElementSpecifier(Simulation simulation){
        this.simulation = simulation;
    }

    abstract public void doAction();

    public void getElementFromMousePosition(Vector2D mousePos, Projection2D projection){
        Vector2D cursorBoardPosition = getExactBoardPosition(mousePos, projection);
        LogicElement logicElement = findLogicElement(cursorBoardPosition, simulation.logicElementIterator());

        if (logicElement != null) {
            currentLogicElementPos = logicElement.getPosition();
            currentLogicElement = logicElement;
            relativeMousePos = new Vector2D(cursorBoardPosition.getX() - logicElement.getPosition().getX(),
                    cursorBoardPosition.getY() - logicElement.getPosition().getY());

            doAction();
        }

    }

    public void getElementPosFromElementAndMousePosition(Vector2D mousePos, Projection2D projection,
                                                         LogicElement logicElement, Vector2D relativeMousePos){
        Vector2D pos = projection.projectBack(mousePos);

        Vector2D adjustedPos = new Vector2D(pos.getX() - relativeMousePos.getX()+0.5, pos.getY() - relativeMousePos.getY());
        currentLogicElementPos = getNodePosition(adjustedPos, projection);


        doAction();
    }

    private Vector2D getExactBoardPosition(Vector2D mousePos, Projection2D projection){
        return projection.projectBack(new Vector2D(mousePos.getX(), mousePos.getY()));
    }

    private Vector2D getNodePosition(Vector2D mouseBoardPos, Projection2D projection){

        int x = (int)mouseBoardPos.getX();
        int y = (int)mouseBoardPos.getY();
        Vector2D nodePos;


        double xFraction = mouseBoardPos.getX() - x;
        double yFraction = mouseBoardPos.getY() - y;

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

    private LogicElement findLogicElement(Vector2D cursorBoardPos, Iterator<LogicElement> logicElements){
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
        simulation.removeLogicElement(currentLogicElementPos);
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

    public Vector2D getRelativeMousePos() {
        return relativeMousePos;
    }

}
