package InternalModel.LogicElements;

import InternalModel.LogicState;
import InternalModel.Vector2D;

import java.util.ArrayList;
import java.util.List;

public abstract class LogicElement {
    public Vector2D getPosition() {
        return position;
    }

    protected Vector2D position;
    protected Rotation rotation;

    LogicElement(){
        position = new Vector2D(0,0);
        rotation = Rotation.RIGHT;
    }

    abstract ArrayList<Vector2D> getLocalInputPositions();
    abstract ArrayList<Vector2D> getLocalOutputPositions();
    abstract ArrayList<LogicState> computeLocalValues(List<LogicState> states);

    public ArrayList<Vector2D> getInputPositions(){
        if(rotation == Rotation.RIGHT) {
            return translateByVector(getLocalInputPositions(), getPosition());
        }
        else if(rotation == Rotation.LEFT){
            ArrayList<Vector2D> localInputPositions = getLocalInputPositions();
            int width = getElementWidth();
            ArrayList<Vector2D> inputPositions = flipHorizontally(localInputPositions, width);
            return translateByVector(inputPositions, getPosition());
        }
        return null;
    }

    public ArrayList<Vector2D> getOutputPositions(){
        if(rotation == Rotation.RIGHT) {
            return translateByVector(getLocalOutputPositions(), getPosition());
        }
        else if(rotation == Rotation.LEFT){
            ArrayList<Vector2D> localOutputPositions = getLocalOutputPositions();
            int width = getElementWidth();
            ArrayList<Vector2D> outputPositions = flipHorizontally(localOutputPositions, width);
            return translateByVector(outputPositions, position);
        }
        return null;
    }

    public ArrayList<ComputedValue> computeValues(ArrayList<LogicState> states){
        ArrayList<ComputedValue> outputStates = new ArrayList<>();
        ArrayList<Vector2D> positions = getOutputPositions();
        ArrayList<LogicState> localOutputStates = computeLocalValues(states);
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

    private int getElementWidth(){
        int lowestX = Integer.MAX_VALUE;
        int greatestX = Integer.MIN_VALUE;

        ArrayList<Vector2D> localOutputPositions = getLocalOutputPositions();
        for (int i = 0; i < localOutputPositions.size(); i++) {
            if(localOutputPositions.get(i).getX() > greatestX) greatestX = localOutputPositions.get(i).getX();
            if(localOutputPositions.get(i).getX() < lowestX) lowestX = localOutputPositions.get(i).getX();
        }
        ArrayList<Vector2D> localInputPositions = getLocalInputPositions();
        for (int i = 0; i < localInputPositions.size(); i++) {
            if(localInputPositions.get(i).getX() > greatestX) greatestX = localInputPositions.get(i).getX();
            if(localInputPositions.get(i).getX() < lowestX) lowestX = localInputPositions.get(i).getX();
        }

        return greatestX - lowestX;
    }

    private int getElementHeight(){
        int lowestY = Integer.MAX_VALUE;
        int greatestY = Integer.MIN_VALUE;

        ArrayList<Vector2D> localOutputPositions = getLocalOutputPositions();
        for (int i = 0; i < localOutputPositions.size(); i++) {
            if(localOutputPositions.get(i).getY() > greatestY) greatestY = localOutputPositions.get(i).getY();
            if(localOutputPositions.get(i).getY() < lowestY) lowestY = localOutputPositions.get(i).getY();
        }
        ArrayList<Vector2D> localInputPositions = getLocalInputPositions();
        for (int i = 0; i < localInputPositions.size(); i++) {
            if(localInputPositions.get(i).getY() > greatestY) greatestY = localInputPositions.get(i).getY();
            if(localInputPositions.get(i).getY() < lowestY) lowestY = localInputPositions.get(i).getY();
        }

        return greatestY - lowestY;
    }

    private ArrayList<Vector2D> translateByVector(ArrayList<Vector2D> points, Vector2D vector){
        ArrayList<Vector2D> newPoints = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            newPoints.add(new Vector2D(points.get(i).getX() + vector.getX(), points.get(i).getY() + vector.getY()));
        }
        return newPoints;
    }

    private ArrayList<Vector2D> flipHorizontally(ArrayList<Vector2D> points, int width){
        ArrayList<Vector2D> newPoints = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            newPoints.add(new Vector2D(width - points.get(i).getX() , points.get(i).getY()));
        }
        return newPoints;
    }

    private ArrayList<Vector2D> flipVertically(ArrayList<Vector2D> points, int height){
        ArrayList<Vector2D> newPoints = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            newPoints.add(new Vector2D(points.get(i).getX() , height - points.get(i).getY()));
        }
        return newPoints;
    }

    ArrayList<Vector2D> rotateAroundOrigin(ArrayList<Vector2D> points){
        ArrayList<Vector2D> newPoints = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            newPoints.add(new Vector2D(points.get(i).getY(), points.get(i).getX()));
        }
        return newPoints;
    }


}
