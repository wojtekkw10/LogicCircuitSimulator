package LogicCircuitSimulator.Simulation.LogicElements;

import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Vector2D;

import java.util.ArrayList;
import java.util.List;

public abstract class LogicElementGeometry {
    private Rotation rotation;
    private Vector2D position;

    public abstract List<Vector2D> getLocalInputPositions();
    public abstract List<Vector2D> getLocalOutputPositions();

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

    public int getElementWidth(){
        int lowestX = 0;
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
        double lowestY = 0;
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

    public Rotation getRotation() {
        return rotation;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }
}
