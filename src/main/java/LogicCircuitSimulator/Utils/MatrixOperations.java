package LogicCircuitSimulator.Utils;

import LogicCircuitSimulator.Simulation;
import LogicCircuitSimulator.Vector2D;
import org.ejml.simple.SimpleMatrix;

public class MatrixOperations {
    public static SimpleMatrix getTranslationMatrix(double x, double y){
        return new SimpleMatrix(
                new double[][] {
                        new double[] {1, 0, x},
                        new double[] {0, 1, y},
                        new double[] {0 ,0, 1}
                }
        );
    }

    public static SimpleMatrix getScalingMatrix(double scalingFactor){
        return new SimpleMatrix(
                new double[][] {
                        new double[] {scalingFactor, 0,             0},
                        new double[] {0,             scalingFactor, 0},
                        new double[] {0,             0,             1}
                }
        );
    }

    public static SimpleMatrix getScalingMatrix(double scalingFactor, double x, double y){
        SimpleMatrix translationMatrix = MatrixOperations.getTranslationMatrix(-x, -y);
        SimpleMatrix translationMatrix1 = MatrixOperations.getTranslationMatrix(x, y);
        SimpleMatrix scalingMatrix = MatrixOperations.getScalingMatrix(scalingFactor);

        return translationMatrix1.mult(scalingMatrix).mult(translationMatrix);
    }

    public static double getScaleFromMatrix(SimpleMatrix matrix){
        return matrix.get(0,0);
    }
    public static SimpleMatrix getVectorMatrix(double x, double y){
        return new SimpleMatrix(
                new double[][] {
                        new double[] {x},
                        new double[] {y},
                        new double[] {1}
                }
        );
    }

    public static Vector2D getVectorFromVectorMatrix(SimpleMatrix matrix){
        double x = matrix.get(0,0);
        double y = matrix.get(1,0);
        return new Vector2D(x,y);
    }

    public static Vector2D getVectorFromFullMatrix(SimpleMatrix matrix){
        double x = matrix.get(0,2);
        double y = matrix.get(1,2);
        return new Vector2D(x,y);
    }

    public static Vector2D projectPoint(SimpleMatrix projectionMatrix, Vector2D point){
        SimpleMatrix pos = MatrixOperations.getVectorMatrix(point.getX(), point.getY());
        SimpleMatrix projectedPos = projectionMatrix.mult(pos);
        return MatrixOperations.getVectorFromVectorMatrix(projectedPos);
    }

    public static SimpleMatrix getProjectionMatrix(double x, double y, double scale){
        return new SimpleMatrix(
                new double[][] {
                        new double[] {scale, 0, x},
                        new double[] {0, scale, y},
                        new double[] {0 ,0, 1}
                }
        );
    }
}
