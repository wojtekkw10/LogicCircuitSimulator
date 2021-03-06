package LogicCircuitSimulator.FxGUI.CircuitGrid.BoardMouseSpecifiers;

import LogicCircuitSimulator.FxGUI.CircuitGrid.GraphicalProjection.Projection2D;
import LogicCircuitSimulator.Simulation.LCSSimulation;
import LogicCircuitSimulator.Simulation.NodeHandler.Crossing;
import LogicCircuitSimulator.Simulation.NodeHandler.WireState;
import LogicCircuitSimulator.Vector2D;

public abstract class MouseCrossingSpecifier {

    private final LCSSimulation simulation;
    private Vector2D currentPosition;
    private Crossing currentCrossing;

    public MouseCrossingSpecifier(LCSSimulation simulation){
        this.simulation = simulation;
    }

    abstract public void doAction();

    public void performTransformation(Vector2D mousePos, Projection2D projection){
        Vector2D pos = projection.projectBack(new Vector2D(mousePos.getX(), mousePos.getY()));
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

        if(xFraction < 0.5 && yFraction < 0.5){
            nodePos = new Vector2D(x, y);
        }
        else if(xFraction > 0.5 && yFraction < 0.5){
            nodePos = new Vector2D(x+1, y);
        }
        else if(xFraction < 0.5 && yFraction > 0.5){
            nodePos = new Vector2D(x, y+1);
        }
        else if(xFraction > 0.5 && yFraction > 0.5){
            nodePos = new Vector2D(x+1, y+1);
        }

        currentCrossing = simulation.getNodeHandler().getCrossing(nodePos);
        currentPosition = nodePos;
        doAction();
    }

    public void updateCrossing(Crossing crossing){
        simulation.getNodeHandler().setCrossing(currentPosition, crossing);
    }

    public Crossing getCrossing(){
        return currentCrossing;
    }

    public int getNumberOfWiresAround(){
        int numberOfWires = 0;
        if(simulation.getNodeHandler().getDownWire(currentPosition) != WireState.NONE) numberOfWires++;
        if(simulation.getNodeHandler().getUpWire(currentPosition) != WireState.NONE) numberOfWires++;
        if(simulation.getNodeHandler().getLeftWire(currentPosition) != WireState.NONE) numberOfWires++;
        if(simulation.getNodeHandler().getRightWire(currentPosition) != WireState.NONE) numberOfWires++;
        return numberOfWires;
    }

}
