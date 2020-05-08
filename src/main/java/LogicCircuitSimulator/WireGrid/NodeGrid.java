package LogicCircuitSimulator.WireGrid;

import LogicCircuitSimulator.LogicState;
import LogicCircuitSimulator.Orientation;
import LogicCircuitSimulator.Vector2D;

import java.util.Iterator;

public interface NodeGrid {
    Node getNode(Vector2D pos);
    void setNode(Vector2D pos, Node node);

    WireState getLeftWire(Vector2D pos);
    WireState getUpWire(Vector2D pos);
    WireState getRightWire(Vector2D pos);
    WireState getDownWire(Vector2D pos);

    void setLeftWire(Vector2D pos, WireState state);
    void setUpWire(Vector2D pos, WireState state);
    void setRightWire(Vector2D pos, WireState state);
    void setDownWire(Vector2D pos, WireState state);

    void setCrossing(Vector2D pos, Node.WireCrossing crossing);
    Node.WireCrossing getCrossing(Vector2D pos);

    LogicState getState(Vector2D pos, Orientation orientation);
    Iterator<Node> iterator();
}
