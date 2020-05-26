package LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements;

import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.Geometry.LogicElementGeometry;
import LogicCircuitSimulator.Simulation.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Vector2D;

import java.util.ArrayList;
import java.util.List;

public abstract class LogicElement {
    private final LogicElementGeometry geometry;

    public abstract String getName();
    public abstract LogicElementGeometry getNewGeometry();
    public abstract List<LogicState> computeLocalValues(List<LogicState> states);
    public abstract void accept(LogicElementVisitor visitor);

    public Vector2D getPosition() {
        return geometry.getPosition();
    }
    public Rotation getRotation() {
        return geometry.getRotation();
    }

    public List<ComputedValue> computeValues(List<LogicState> states){
        if(states.size() != geometry.getInputPositions().size()) throw new IllegalArgumentException("Incorrect number of input states");

        List<ComputedValue> outputStates = new ArrayList<>();
        List<Vector2D> positions = geometry.getOutputPositions();
        List<LogicState> localOutputStates = computeLocalValues(states);
        for (int i = 0; i < positions.size(); i++) {
            outputStates.add(new ComputedValue(positions.get(i), localOutputStates.get(i)));
        }
        return outputStates;
    }

    public void setPosition(Vector2D position) {
        this.geometry.setPosition(position);
    }

    public LogicElement(int x, int y, Rotation rot){
        this.geometry = getNewGeometry();
        this.geometry.setPosition(new Vector2D(x,y));
        this.geometry.setRotation(rot);
    }

    public void setRotation(Rotation rotation) {
        this.geometry.setRotation(rotation);
    }
    public LogicElementGeometry getGeometry() {
        return geometry;
    }

    public double getX(){
        return getGeometry().getPosition().getX();
    }
    public double getY(){
        return getGeometry().getPosition().getY();
    }
}
