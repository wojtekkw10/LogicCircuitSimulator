package LogicCircuitSimulator.FxGUI.CircuitGrid.BoardMouseSpecifiers;

import LogicCircuitSimulator.FxGUI.CircuitGrid.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.Simulation.LCSSimulation;
import LogicCircuitSimulator.Simulation.NodeHandler.WireState;
import LogicCircuitSimulator.Simulation.Orientation;
import LogicCircuitSimulator.Vector2D;

public abstract class MouseWireSpecifier {
    int x;
    int y;

    LCSSimulation simulation;
    WireState currentWireState;
    Vector2D currentNodePos;
    Orientation currentOrientation;

    public MouseWireSpecifier(LCSSimulation simulation){
        this.simulation = simulation;
    }

    abstract public void doAction();

    public void performTransformation(Vector2D mousePos, Projection2D projection){
        Vector2D pos = projection.projectBack(new Vector2D(mousePos.getX(), mousePos.getY()));
        x = (int)pos.getX();
        y = (int)pos.getY();
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

        double unresponsiveSpace = 0.2;

        if(isInUpperTriangle(xFraction, yFraction) && xFraction > unresponsiveSpace && xFraction < 1 - unresponsiveSpace){
            currentWireState = simulation.getNodeHandler().getRightWire(new Vector2D(getX(),getY()));
            currentNodePos = new Vector2D(getX(),getY());
            currentOrientation = Orientation.HORIZONTALLY;
            doAction();
        }
        if(isInRightTriangle(xFraction, yFraction) && yFraction > unresponsiveSpace && yFraction < 1 - unresponsiveSpace){
            currentWireState = simulation.getNodeHandler().getDownWire(new Vector2D(getX()+1,getY()));
            currentNodePos = new Vector2D(getX()+1,getY());
            currentOrientation = Orientation.VERTICALLY;
            doAction();
        }
        if(isInLowerTriangle(xFraction, yFraction) && xFraction > unresponsiveSpace && xFraction < 1 - unresponsiveSpace){
            currentWireState = simulation.getNodeHandler().getRightWire(new Vector2D(getX(),getY()+1));
            currentNodePos = new Vector2D(getX(),getY()+1);
            currentOrientation = Orientation.HORIZONTALLY;
            doAction();
        }
        if(isInLeftTriangle(xFraction, yFraction) && yFraction > unresponsiveSpace && yFraction < 1 - unresponsiveSpace){
            currentWireState = simulation.getNodeHandler().getDownWire(new Vector2D(getX(),getY()));
            currentNodePos = new Vector2D(getX(),getY());
            currentOrientation = Orientation.VERTICALLY;
            doAction();
        }

    }

    boolean isInUpperTriangle(double xFraction, double yFraction){
        return xFraction > yFraction && xFraction < 1 - yFraction;
    }

    boolean isInLowerTriangle(double xFraction, double yFraction){
        return xFraction < yFraction && xFraction > 1 - yFraction;
    }

    boolean isInLeftTriangle(double xFraction, double yFraction){
        return xFraction < yFraction && xFraction < 1 - yFraction;
    }

    boolean isInRightTriangle(double xFraction, double yFraction){
        return xFraction > yFraction && xFraction > 1 - yFraction;
    }

    private int getX() {
        return x;
    }

    private int getY() {
        return y;
    }

    public WireState getWireState(){
        return currentWireState;
    }
    public void updateWireState(WireState state){
        if(currentOrientation == Orientation.HORIZONTALLY)
            simulation.getNodeHandler().setRightWire(currentNodePos, state);
        else simulation.getNodeHandler().setDownWire(currentNodePos, state);
    }
}
