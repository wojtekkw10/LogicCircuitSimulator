package LogicCircuitSimulator.FxGUI.WireMouseHandler;

import LogicCircuitSimulator.Orientation;
import LogicCircuitSimulator.Vector2D;

public abstract class WireMouseHandler {
    int x;
    int y;

    abstract void upperTriangle();
    abstract void lowerTriangle();
    abstract void leftTriangle();
    abstract void rightTriangle();

    public void performFunction(Vector2D pos){
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
            upperTriangle();
        }
        if(isInRightTriangle(xFraction, yFraction) && yFraction > unresponsiveSpace && yFraction < 1 - unresponsiveSpace){
            rightTriangle();
        }
        if(isInLowerTriangle(xFraction, yFraction) && xFraction > unresponsiveSpace && xFraction < 1 - unresponsiveSpace){
            lowerTriangle();
        }
        if(isInLeftTriangle(xFraction, yFraction) && yFraction > unresponsiveSpace && yFraction < 1 - unresponsiveSpace){
            leftTriangle();
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
}
