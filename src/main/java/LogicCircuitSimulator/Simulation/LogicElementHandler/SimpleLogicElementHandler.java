package LogicCircuitSimulator.Simulation.LogicElementHandler;

import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.UnboundGrid.GridIterator;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.UnboundGrid.UnboundGrid;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.UnboundGrid.UnboundHashMapGrid;
import LogicCircuitSimulator.Vector2D;

import java.util.Iterator;
import java.util.Optional;

public class SimpleLogicElementHandler implements LogicElementHandler{
    private final UnboundGrid<LogicElement> logicElements = new UnboundHashMapGrid<>();

    @Override
    public void add(LogicElement logicElement) {
        logicElements.set(logicElement.getPosition(), logicElement);
    }

    @Override
    public Optional<LogicElement> get(Vector2D pos) {
        return logicElements.get(pos);
    }

    @Override
    public void remove(Vector2D pos) {
        logicElements.remove(pos);
    }

    @Override
    public Iterator<LogicElement> iterator() {
        return new LogicElementIterator();
    }

    private class LogicElementIterator implements Iterator<LogicElement>{
        GridIterator<LogicElement> iterator = logicElements.iterator();

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public LogicElement next() {
            return iterator.next();
        }

        @Override
        public void remove() {
            logicElements.remove(iterator.currentPosition());
        }
    }
}
