package LogicCircuitSimulator.LogicElements;

import LogicCircuitSimulator.LogicElementVisitor;
import LogicCircuitSimulator.LogicState;
import LogicCircuitSimulator.Vector2D;

import java.util.ArrayList;
import java.util.List;

public abstract class LogicElement {
    public Vector2D getPosition() {
        return position;
    }

    private Vector2D position;

    public Rotation getRotation() {
        return rotation;
    }

    private Rotation rotation;

    abstract List<Vector2D> getLocalInputPositions();
    abstract List<Vector2D> getLocalOutputPositions();
    abstract List<LogicState> computeLocalValues(List<LogicState> states);

    public abstract void accept(LogicElementVisitor visitor);

    public List<Vector2D> getInputPositions(){
        List<Vector2D> inputPositions = getLocalInputPositions();
        inputPositions = rotate(inputPositions, rotation, getElementWidth(), getElementHeight());
        return translateByVector(inputPositions, position);
    }

    public List<Vector2D> getOutputPositions(){
        List<Vector2D> outputPositions = getLocalOutputPositions();
        outputPositions = rotate(outputPositions, rotation, getElementWidth(), getElementHeight());
        return translateByVector(outputPositions, position);
    }

    public List<ComputedValue> computeValues(List<LogicState> states){
        if(states.size() != getInputPositions().size()) throw new IllegalArgumentException("Incorrect number of input states");

        List<ComputedValue> outputStates = new ArrayList<>();
        List<Vector2D> positions = getOutputPositions();
        List<LogicState> localOutputStates = computeLocalValues(states);
        for (int i = 0; i < positions.size(); i++) {
            outputStates.add(new ComputedValue(positions.get(i), localOutputStates.get(i)));
        }
        return outputStates;
    }


    public LogicElement(int x, int y, Rotation rot){
        position = new Vector2D(x,y);
        this.rotation = rot;
    }

    public void move(Vector2D newPosition){
        position = newPosition;
    }
    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public int getElementWidth(){
        int lowestX = Integer.MAX_VALUE;
        int greatestX = Integer.MIN_VALUE;

        List<Vector2D> localOutputPositions = getLocalOutputPositions();
        for (int i = 0; i < localOutputPositions.size(); i++) {
            if(localOutputPositions.get(i).getX() > greatestX) greatestX = (int)localOutputPositions.get(i).getX();
            if(localOutputPositions.get(i).getX() < lowestX) lowestX = (int)localOutputPositions.get(i).getX();
        }
        List<Vector2D> localInputPositions = getLocalInputPositions();
        for (int i = 0; i < localInputPositions.size(); i++) {
            if(localInputPositions.get(i).getX() > greatestX) greatestX = (int)localInputPositions.get(i).getX();
            if(localInputPositions.get(i).getX() < lowestX) lowestX = (int)localInputPositions.get(i).getX();
        }

        return greatestX - lowestX;
    }

    public int getElementHeight(){
        double lowestY = Double.MAX_VALUE;
        double greatestY = Double.MIN_VALUE;

        List<Vector2D> localOutputPositions = getLocalOutputPositions();
        for (int i = 0; i < localOutputPositions.size(); i++) {
            if(localOutputPositions.get(i).getY() > greatestY) greatestY = localOutputPositions.get(i).getY();
            if(localOutputPositions.get(i).getY() < lowestY) lowestY = localOutputPositions.get(i).getY();
        }
        List<Vector2D> localInputPositions = getLocalInputPositions();
        for (int i = 0; i < localInputPositions.size(); i++) {
            if(localInputPositions.get(i).getY() > greatestY) greatestY = localInputPositions.get(i).getY();
            if(localInputPositions.get(i).getY() < lowestY) lowestY = localInputPositions.get(i).getY();
        }

        return (int)(greatestY - lowestY);
    }

    private ArrayList<Vector2D> translateByVector(List<Vector2D> points, Vector2D vector){
        ArrayList<Vector2D> newPoints = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            newPoints.add(new Vector2D(points.get(i).getX() + vector.getX(), points.get(i).getY() + vector.getY()));
        }
        return newPoints;
    }

    private ArrayList<Vector2D> flipHorizontally(List<Vector2D> points, int width){
        ArrayList<Vector2D> newPoints = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            newPoints.add(new Vector2D(width - points.get(i).getX() , points.get(i).getY()));
        }
        return newPoints;
    }

    private ArrayList<Vector2D> flipVertically(List<Vector2D> points, int height){
        ArrayList<Vector2D> newPoints = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            newPoints.add(new Vector2D(points.get(i).getX() , height - points.get(i).getY()));
        }
        return newPoints;
    }

    private ArrayList<Vector2D> rotateAroundOrigin(List<Vector2D> points){
        ArrayList<Vector2D> newPoints = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            newPoints.add(new Vector2D(points.get(i).getY(), points.get(i).getX()));
        }
        return newPoints;
    }

    List<Vector2D> rotate(List<Vector2D> points, Rotation desiredRotation, int width, int height){
        List<Vector2D> result = new ArrayList<>();
        if(desiredRotation == Rotation.RIGHT) {
            result = points;
        }
        else if(desiredRotation == Rotation.LEFT){
            result = flipVertically(flipHorizontally(points, width), height);
        }
        else if(desiredRotation == Rotation.DOWN){
            points = rotateAroundOrigin(points);
            result = flipHorizontally(points, getElementHeight());
        }
        else if(desiredRotation == Rotation.UP){
            points = rotateAroundOrigin(points);
            result = flipVertically(points, getElementWidth());
        }
        return result;
    }


}
