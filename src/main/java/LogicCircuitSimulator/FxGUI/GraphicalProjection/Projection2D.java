package LogicCircuitSimulator.FxGUI.GraphicalProjection;

import LogicCircuitSimulator.Vector2D;

public interface Projection2D {
    Vector2D project(Vector2D point);
    Vector2D projectBack(Vector2D point);
    void scale(double amount, Vector2D point);
    void translate(Vector2D amount);
}
