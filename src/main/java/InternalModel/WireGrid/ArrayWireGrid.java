package InternalModel.WireGrid;

import InternalModel.LogicState;
import InternalModel.Orientation;
import InternalModel.Vector2D;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public final class ArrayWireGrid implements WireGrid{
    List<List<Wire>> wires = new ArrayList<>();

    /**
     * Creates a wireGrid of given width and height
     * @param initialWidth initial width of the grid
     * @param initialHeight initial height of the grid
     * @throws IllegalArgumentException if specified width or height is less than 1
     */
    public ArrayWireGrid(int initialWidth, int initialHeight){
        if(initialWidth < 1) throw new IllegalArgumentException("Illegal initialWidth: "+initialWidth+" for initialWidth > 0");
        if(initialHeight < 1) throw new IllegalArgumentException("Illegal initialHeight: "+initialHeight+" for initialHeight > 0");

        for (int i = 0; i < initialWidth; i++) {
            List<Wire> column = new ArrayList<>();
            for (int j = 0; j < initialHeight; j++) {
                column.add(new Wire());
            }
            wires.add(column);
        }
    }

    public void setElement(Vector2D pos, Wire wire){
        int x = pos.getX();
        int y = pos.getY();
        if(x < 0) throw new IllegalArgumentException("Illegal x: "+x+" for x >= 0");
        if(y < 0) throw new IllegalArgumentException("Illegal y: "+x+" for y >= 0");

        if(x > getWidth()) extendGridX(x-getWidth()+1);
        if(y > getHeight()) extendGridY(y-getHeight()+1);

        wires.get(x).set(y, wire);
    }

    private void extendGridX(int amount){
        int columnSize = wires.get(0).size();
        for(int i=0; i<amount; i++){
            List<Wire> column = new ArrayList<>();
            for (int j = 0; j < columnSize; j++) {
                column.add(new Wire());
            }
            wires.add(column);
        }
    }
    private void extendGridY(int amount){
        for(int i=0; i<wires.size(); i++){
            for (int j = 0; j < amount; j++) {
                wires.get(i).add(new Wire());
            }
        }
    }

    public void resetWiresToLow(){
        for (int i = 0; i < wires.size(); i++) {
            for (int j = 0; j < wires.get(i).size(); j++) {
                if(wires.get(i).get(j).getRightWire() != Wire.State.NONE) wires.get(i).get(j).setRightWire(Wire.State.LOW);
                if(wires.get(i).get(j).getDownWire() != Wire.State.NONE) wires.get(i).get(j).setDownWire(Wire.State.LOW);
            }
        }
    }

    //TODO: throw exception when generator is at non-touching crossing
    //TODO: distinguish between touching and not-touching crossings
    public void propagateGenerators(List<Vector2D> generators){
        Deque<Vector2D> stack = new ArrayDeque<>();
        for (int i = 0; i < generators.size(); i++) {
            stack.addAll(generators);
        }

        while(!stack.isEmpty()){
            Vector2D pos = stack.pop();
            Wire current = wires.get(pos.getX()).get(pos.getY());
            if(pos.getX()+1 < wires.size()){
                if(current.getRightWire() == Wire.State.LOW){
                    current.setRightWire(Wire.State.HIGH);
                    stack.add(new Vector2D(pos.getX()+1, pos.getY()));
                }
            }


            if(pos.getY()+1 < wires.get(0).size()){
                if(current.getDownWire() == Wire.State.LOW){
                    current.setDownWire(Wire.State.HIGH);
                    stack.add(new Vector2D(pos.getX(), pos.getY()+1));
                }
            }


            if(pos.getX()-1 >= 0){
                Wire wireToLeft = wires.get(pos.getX()-1).get(pos.getY());
                if(wireToLeft.getRightWire() == Wire.State.LOW) {
                    wireToLeft.setRightWire(Wire.State.HIGH);
                    stack.add(new Vector2D(pos.getX()-1, pos.getY()));
                }
            }

            if(pos.getY()-1>=0){
                Wire upperWire = wires.get(pos.getX()).get(pos.getY()-1);
                if(upperWire.getDownWire() == Wire.State.LOW) {
                    upperWire.setDownWire(Wire.State.HIGH);
                    stack.add(new Vector2D(pos.getX(), pos.getY()-1));
                }
            }


        }
    }

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
                if(wireToLeft.getRightWire() == Wire.State.HIGH) return LogicState.HIGH;
            }
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
        int x = pos.getX();
        int y = pos.getY();
        if(x < 0) throw new IllegalArgumentException("Illegal x: "+x+" for x >= 0");
        if(y < 0) throw new IllegalArgumentException("Illegal y: "+y+" for y >= 0");

        if(x > getWidth() || y > getHeight()) return new Wire();
        return wires.get(x).get(y);
    }

    @Override
    public int getWidth() {
        return wires.size();
    }

    @Override
    public int getHeight() {
        return wires.get(0).size();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        String lowStateColor = "\u001b[38;5;245m";
        String highStateColor = "\u001b[33;1m";
        String reset = "\u001b[0m";

        for (int i = 0; i < wires.get(0).size(); i++) {
            for (int j = 0; j < wires.size(); j++) {
                if(wires.get(j).get(i).isTouching() == Wire.WireCrossing.TOUCHING) stringBuilder.append("*");
                else if(wires.get(j).get(i).isTouching() == Wire.WireCrossing.NOT_TOUCHING) stringBuilder.append("+");
                else stringBuilder.append(".");

                if(wires.get(j).get(i).getRightWire() == Wire.State.HIGH) stringBuilder.append(highStateColor).append("-").append(reset);
                else if(wires.get(j).get(i).getRightWire() == Wire.State.LOW) stringBuilder.append(lowStateColor).append("-").append(reset);
                else stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
            for (int j = 0; j < wires.size(); j++) {
                if(wires.get(j).get(i).getDownWire() == Wire.State.HIGH) stringBuilder.append(highStateColor).append("| ").append(reset);
                else if(wires.get(j).get(i).getDownWire() == Wire.State.LOW) stringBuilder.append(lowStateColor).append("| ").append(reset);
                else stringBuilder.append("  ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
