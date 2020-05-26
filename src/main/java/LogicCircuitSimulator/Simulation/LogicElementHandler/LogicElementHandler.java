package LogicCircuitSimulator.Simulation.LogicElementHandler;

import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElement;
import LogicCircuitSimulator.Vector2D;

import java.util.Iterator;
import java.util.Optional;

public interface LogicElementHandler {
    void set(LogicElement logicElement);
    Optional<LogicElement> get(Vector2D pos);
    void remove(Vector2D pos);
    Iterator<LogicElement> iterator();
}
