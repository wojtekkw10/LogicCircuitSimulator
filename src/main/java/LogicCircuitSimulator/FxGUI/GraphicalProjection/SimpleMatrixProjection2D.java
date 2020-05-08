package LogicCircuitSimulator.FxGUI.GraphicalProjection;

import LogicCircuitSimulator.Utils.MatrixOperations;
import LogicCircuitSimulator.Vector2D;
import org.ejml.simple.SimpleMatrix;

public class SimpleMatrixProjection2D implements Projection2D{
    private SimpleMatrix projectionMatrix;

    public SimpleMatrixProjection2D(Vector2D startingPosition, double startingScale){
        new SimpleMatrix(
                new double[][] {
                        new double[] {startingScale, 0, startingPosition.getX()},
                        new double[] {0, startingScale, startingPosition.getY()},
                        new double[] {0 ,0, 1}
                }
        );
    }

    @Override
    public Vector2D project(Vector2D point) {
        SimpleMatrix pointMatrix = MatrixOperations.getVectorMatrix(point.getX(), point.getY());
        SimpleMatrix projectedPos = projectionMatrix.mult(pointMatrix);
        return getVectorFromVectorMatrix(projectedPos);
    }

    @Override
    public Vector2D projectBack(Vector2D point) {
        SimpleMatrix pointMatrix = MatrixOperations.getVectorMatrix(point.getX(), point.getY());
        SimpleMatrix projectedPos = projectionMatrix.invert().mult(pointMatrix);
        return getVectorFromVectorMatrix(projectedPos);
    }

    @Override
    public void scale(double amount, Vector2D point) {
        SimpleMatrix scalingMatrix = getScalingMatrix(amount);
        projectionMatrix = scalingMatrix.mult(projectionMatrix);
    }

    @Override
    public void translate(Vector2D amount) {
        SimpleMatrix translationMatrix = getTranslationMatrix(amount);
        projectionMatrix = translationMatrix.mult(projectionMatrix);

    }

    private Vector2D getVectorFromVectorMatrix(SimpleMatrix matrix){
        double x = matrix.get(0,0);
        double y = matrix.get(1,0);
        return new Vector2D(x,y);
    }

    private SimpleMatrix getTranslationMatrix(Vector2D amount){
        return new SimpleMatrix(
                new double[][] {
                        new double[] {1, 0, amount.getX()},
                        new double[] {0, 1, amount.getY()},
                        new double[] {0 ,0, 1}
                }
        );
    }

    private SimpleMatrix getScalingMatrix(double amount){
        return new SimpleMatrix(
                new double[][] {
                        new double[] {amount, 0,             0},
                        new double[] {0,             amount, 0},
                        new double[] {0,             0,             1}
                }
        );
    }
}
