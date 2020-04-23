package InternalModel.WireGrid;

import InternalModel.LogicState;
import InternalModel.Orientation;
import InternalModel.Vector2D;

import java.util.*;

public final class ArrayWireGrid implements WireGrid{
    UpUnbound2DArrayList<Wire> wires;

    /**
     * Creates a wireGrid of given width and height
     * @param initialWidth initial width of the grid
     * @param initialHeight initial height of the grid
     * @throws IllegalArgumentException if specified width or height is less than 1
     */
    public ArrayWireGrid(int initialWidth, int initialHeight){
        if(initialWidth < 1) throw new IllegalArgumentException("Illegal initialWidth: "+initialWidth+" for initialWidth > 0");
        if(initialHeight < 1) throw new IllegalArgumentException("Illegal initialHeight: "+initialHeight+" for initialHeight > 0");

        wires = new UpUnbound2DArrayList<>(Wire::new, initialWidth, initialHeight);
    }

    @Override
    public void setElement(Vector2D pos, Wire wire){
        wires.set(pos, wire);
    }

    @Override
    public void resetWiresToLow(){
        for (int i = 0; i < wires.getWidth(); i++) {
            for (int j = 0; j < wires.getHeight(); j++) {
                if(wires.get(new Vector2D(i, j)).getRightWire() != Wire.State.NONE) {
                    Wire wire = wires.get(new Vector2D(i, j));
                    Wire newWire = new Wire(Wire.State.LOW, wire.getDownWire(), wire.isTouching());
                    wires.set(new Vector2D(i, j), newWire);
                }
                if(wires.get(new Vector2D(i, j)).getDownWire() != Wire.State.NONE) {
                    Wire wire = wires.get(new Vector2D(i, j));
                    Wire newWire = new Wire(wire.getRightWire(), Wire.State.LOW, wire.isTouching());
                    wires.set(new Vector2D(i, j), newWire);
                }
            }
        }
    }

    @Override
    public void propagateGenerators(List<Generator> generators){
        resetWiresToLow();
        Deque<Vector2D> stack = new ArrayDeque<>();
        for (int i = 0; i < generators.size(); i++) {
            Generator generator = generators.get(i);
            if(generator.getPos().getX() < 0) throw new IllegalArgumentException("Illegal argument 'generators'. Generator at index "+i+
                    " is at position: ("+generator.getPos().getX()+","+generator.getPos().getY()+")");
            if(generator.getPos().getY() < 0) throw new IllegalArgumentException("Illegal argument 'generators'. Generator at index "+i+
                    " is at position: ("+generator.getPos().getX()+","+generator.getPos().getY()+")");

            //Generators out of wires' sight won't do anything
            if(generator.getPos().getX() > wires.getWidth()) continue;
            if(generator.getPos().getY() > wires.getHeight()) continue;

            stack.add(generator.getPos());
        }

        for (int i = 0; i < generators.size(); i++) {
            Generator generator = generators.get(i);
            Vector2D pos = generator.getPos();

            //Generators out of wires' sight won't do anything
            if(generator.getPos().getX() > wires.getWidth()) continue;
            if(generator.getPos().getY() > wires.getHeight()) continue;

            if(generator.getOrientation() == Orientation.HORIZONTALLY){
                if(wires.get(pos).getRightWire() == Wire.State.LOW){
                    Wire wire = wires.get(pos);
                    Wire newWire = new Wire(Wire.State.HIGH, wire.getDownWire(), wire.isTouching());
                    wires.set(pos, newWire);

                    stack.add(new Vector2D(pos.getX()+1, pos.getY()));
                }
                if(pos.getX() - 1 >= 0
                        && wires.get(new Vector2D(pos.getX()-1, pos.getY())).getRightWire() == Wire.State.LOW){
                    Wire wire = wires.get(new Vector2D(pos.getX()-1, pos.getY()));
                    Wire newWire = new Wire(Wire.State.HIGH, wire.getDownWire(), wire.isTouching());
                    wires.set(new Vector2D(pos.getX()-1, pos.getY()), newWire);

                    stack.add(new Vector2D(pos.getX()-1, pos.getY()));
                }
            }
            else{
                if(wires.get(pos).getDownWire() == Wire.State.LOW){
                    Wire wire = wires.get(pos);
                    Wire newWire = new Wire(wire.getRightWire(), Wire.State.HIGH, wire.isTouching());
                    wires.set(pos, newWire);

                    stack.add(new Vector2D(pos.getX(), pos.getY()+1));
                }
                if(pos.getY() - 1 >= 0 &&
                        wires.get(new Vector2D(pos.getX(), pos.getY()-1)).getDownWire() == Wire.State.LOW){
                    Wire wire = wires.get(new Vector2D(pos.getX(), pos.getY()-1));
                    Wire newWire = new Wire(wire.getRightWire(), Wire.State.HIGH, wire.isTouching());
                    wires.set(new Vector2D(pos.getX(), pos.getY()-1), newWire);

                    stack.add(new Vector2D(pos.getX(), pos.getY()-1));
                }
            }
        }

        //TODO: document throws in WireGrid

        while(!stack.isEmpty()){
            Vector2D pos = stack.pop();
            int x = pos.getX();
            int y = pos.getY();

            if(x+1 < wires.getWidth()
                    && getState(new Vector2D(x,y), Orientation.HORIZONTALLY) == LogicState.HIGH
                    && wires.get(new Vector2D(x+1, y)).getRightWire() == Wire.State.LOW){
                Wire wire = wires.get(new Vector2D(pos.getX()+1, pos.getY()));
                Wire newWire = new Wire(Wire.State.HIGH, wire.getDownWire(), wire.isTouching());
                wires.set(new Vector2D(pos.getX()+1, pos.getY()), newWire);

                stack.add(new Vector2D(x+1, y));
            }
            if(y+1 < wires.getHeight()
                    && getState(new Vector2D(x,y), Orientation.VERTICALLY) == LogicState.HIGH
                    && wires.get(new Vector2D(x, y+1)).getDownWire() == Wire.State.LOW){
                Wire wire = wires.get(new Vector2D(pos.getX(), pos.getY()+1));
                Wire newWire = new Wire(wire.getRightWire(), Wire.State.HIGH, wire.isTouching());
                wires.set(new Vector2D(pos.getX(), pos.getY()+1), newWire);

                stack.add(new Vector2D(x, y+1));
            }
            if(x-1 >= 0
                    && getState(new Vector2D(x,y), Orientation.HORIZONTALLY) == LogicState.HIGH
                    && wires.get(new Vector2D(x-1, y)).getRightWire() == Wire.State.LOW){
                Wire wire = wires.get(new Vector2D(pos.getX()-1, pos.getY()));
                Wire newWire = new Wire(Wire.State.HIGH, wire.getDownWire() , wire.isTouching());
                wires.set(new Vector2D(pos.getX()-1, pos.getY()), newWire);

                stack.add(new Vector2D(x-1, y));
            }
            if(y-1 >= 0
                    && getState(new Vector2D(x,y), Orientation.VERTICALLY) == LogicState.HIGH
                    && wires.get(new Vector2D(x, y-1)).getDownWire() == Wire.State.LOW){
                Wire wire = wires.get(new Vector2D(pos.getX(), pos.getY()-1));
                Wire newWire = new Wire(wire.getRightWire(), Wire.State.HIGH, wire.isTouching());
                wires.set(new Vector2D(pos.getX(), pos.getY()-1), newWire);

                stack.add(new Vector2D(x, y-1));
            }
        }
    }

    @Override
    public LogicState getState(Vector2D pos, Orientation orientation){
        int x = pos.getX();
        int y = pos.getY();

        if(x < 0) throw new IllegalArgumentException("Illegal x: "+x+" for x >= 0");
        if(y < 0) throw new IllegalArgumentException("Illegal y: "+x+" for y >= 0");
        if(orientation == null) throw new NullPointerException("Argument 'orientation' is null");

        Wire wire = getElement(pos);

        if(wire.isTouching() == Wire.WireCrossing.TOUCHING){
            if(wire.getDownWire() == Wire.State.HIGH) return LogicState.HIGH;
            if(wire.getRightWire() == Wire.State.HIGH) return LogicState.HIGH;
            if(y-1>=0){
                Wire upperWire = getElement(new Vector2D(x, y-1));
                if(upperWire.getDownWire() == Wire.State.HIGH) return LogicState.HIGH;
            }
            if(x-1>=0){
                Wire wireToLeft = getElement(new Vector2D(x-1, y));
                if(wireToLeft.getRightWire() == Wire.State.HIGH)
                    return LogicState.HIGH; }
        }
        else{
            if(orientation == Orientation.VERTICALLY){
                if(wire.getDownWire() == Wire.State.HIGH) return LogicState.HIGH;
                if(y-1>=0){
                    Wire upperWire = getElement(new Vector2D(x, y-1));
                    if(upperWire.getDownWire() == Wire.State.HIGH) return LogicState.HIGH;
                }
            }
            else{
                if(wire.getRightWire() == Wire.State.HIGH) return LogicState.HIGH;
                if(x-1>=0){
                    Wire wireToLeft = getElement(new Vector2D(x-1, y));
                    if(wireToLeft.getRightWire() == Wire.State.HIGH) return LogicState.HIGH;
                }
            }
        }
        return LogicState.LOW;
    }

    @Override
    public Wire getElement(Vector2D pos) {
        return wires.get(pos);
    }

    @Override
    public Iterator2D<Wire> getIterator() {
        return new MainIterator();
    }

    @Override
    public void updateWire(Vector2D pos, Orientation orientation, Wire.State state) {
        Wire wire = wires.get(pos);
        if(orientation == Orientation.HORIZONTALLY){
            Wire newWire = new Wire(state, wire.getDownWire(), wire.isTouching());
            wires.set(pos, newWire);
        }
        else {
            Wire newWire = new Wire(wire.getRightWire(), state, wire.isTouching());
            wires.set(pos, newWire);
        }
    }

    @Override
    public void updateCrossing(Vector2D pos, Wire.WireCrossing crossing) {
        Wire wire = wires.get(pos);
        Wire newWire = new Wire(wire.getRightWire(), wire.getDownWire(), crossing);
        wires.set(pos, newWire);
    }

    //TODO: docs - not static because it needs access to wires, if it was static it would need an instance of the wireGrid. Non-static has it by default.
    private class MainIterator implements Iterator2D<Wire>{
        int currentX = -1;
        int currentY = 0;

        @Override
        public boolean hasNext() {
            if(currentX < wires.getWidth() - 1) return true;
            else{
                if(currentY < wires.getHeight() - 1) return true;
                else return false;
            }
        }

        @Override
        public Wire next() {
            if(!hasNext()) throw new NoSuchElementException("WireGrid has no more elements");

            if(currentX < wires.getWidth() - 1) currentX++;
            else{
                currentY++;
                currentX = 0;
            }
            return wires.get(new Vector2D(currentX, currentY));
        }

        @Override
        public Vector2D currentPosition() {
            return new Vector2D(currentX, currentY);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        String lowStateColor = "\u001b[38;5;245m";
        String highStateColor = "\u001b[33;1m";
        String reset = "\u001b[0m";

        for (int i = 0; i < wires.getHeight(); i++) {
            for (int j = 0; j < wires.getWidth(); j++) {
                if(wires.get(new Vector2D(j, i)).isTouching() == Wire.WireCrossing.TOUCHING) stringBuilder.append("*");
                else stringBuilder.append("+");

                if(wires.get(new Vector2D(j, i)).getRightWire() == Wire.State.HIGH) stringBuilder.append(highStateColor).append("-").append(reset);
                else if(wires.get(new Vector2D(j, i)).getRightWire() == Wire.State.LOW) stringBuilder.append(lowStateColor).append("-").append(reset);
                else stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
            for (int j = 0; j < wires.getWidth(); j++) {
                if(wires.get(new Vector2D(j, i)).getDownWire() == Wire.State.HIGH) stringBuilder.append(highStateColor).append("| ").append(reset);
                else if(wires.get(new Vector2D(j, i)).getDownWire() == Wire.State.LOW) stringBuilder.append(lowStateColor).append("| ").append(reset);
                else stringBuilder.append("  ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}

//TODO: refactor into smaller functions
