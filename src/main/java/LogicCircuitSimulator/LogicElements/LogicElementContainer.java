package LogicCircuitSimulator.LogicElements;

import LogicCircuitSimulator.Vector2D;
import LogicCircuitSimulator.WireGrid.Iterator2D;

public interface LogicElementContainer {
    void add(Vector2D pos, LogicElement logicElement);
    void remove(Vector2D pos);
    Iterator2D<LogicElement> iterator();
}
