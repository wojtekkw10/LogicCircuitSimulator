package InternalModel;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class WireGrid {
    List<List<Wire>> wires = new ArrayList<>();

    WireGrid(int width, int height){
        for (int i = 0; i < width; i++) {
            List<Wire> column = new ArrayList<>();
            for (int j = 0; j < height; j++) { //number of columns
                column.add(new Wire());
            }
            wires.add(column);
        }
    }

    //TODO: throw argument exception
    public void setElement(int x, int y, Wire wire){
        wires.get(x).set(y, wire);
    }

    public void resetWiresToLow(){
        for (int i = 0; i < wires.size(); i++) {
            for (int j = 0; j < wires.get(i).size(); j++) {
                if(wires.get(i).get(j).right != Wire.WireState.NONE) wires.get(i).get(j).right = Wire.WireState.LOW;
                if(wires.get(i).get(j).down != Wire.WireState.NONE) wires.get(i).get(j).down = Wire.WireState.LOW;
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
                if(current.right == Wire.WireState.LOW){
                    current.right = Wire.WireState.HIGH;
                    stack.add(new Vector2D(pos.getX()+1, pos.getY()));
                }
            }


            if(pos.getY()+1 < wires.get(0).size()){
                if(current.down == Wire.WireState.LOW){
                    current.down = Wire.WireState.HIGH;
                    stack.add(new Vector2D(pos.getX(), pos.getY()+1));
                }
            }


            if(pos.getX()-1 >= 0){
                Wire wireToLeft = wires.get(pos.getX()-1).get(pos.getY());
                if(wireToLeft.right == Wire.WireState.LOW) {
                    wireToLeft.right = Wire.WireState.HIGH;
                    stack.add(new Vector2D(pos.getX()-1, pos.getY()));
                }
            }

            if(pos.getY()-1>=0){
                Wire upperWire = wires.get(pos.getX()).get(pos.getY()-1);
                if(upperWire.down == Wire.WireState.LOW) {
                    upperWire.down = Wire.WireState.HIGH;
                    stack.add(new Vector2D(pos.getX(), pos.getY()-1));
                }
            }


        }
    }

    //We assume that input and output nodes are always touching so no reason to check for horizontal
    //TODO: throw exception for arguments
    LogicState getState(Vector2D pos){
        int x = pos.getX();
        int y = pos.getY();

        Wire wire = wires.get(x).get(y);
        if(wire.down == Wire.WireState.HIGH) return LogicState.HIGH;

        if(wire.right == Wire.WireState.HIGH) return LogicState.HIGH;

        if(y-1>=0){
            Wire upperWire = wires.get(x).get(y-1);
            if(upperWire.down == Wire.WireState.HIGH) return LogicState.HIGH;
        }

        if(x-1>=0){
            Wire wireToLeft = wires.get(x-1).get(y);
            if(wireToLeft.right == Wire.WireState.HIGH) return LogicState.HIGH;
        }


        return LogicState.LOW;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        String lowStateColor = "\u001b[38;5;245m";
        String highStateColor = "\u001b[33;1m";
        String reset = "\u001b[0m";

        for (int i = 0; i < wires.get(0).size(); i++) {
            for (int j = 0; j < wires.size(); j++) {
                if(wires.get(j).get(i).isTouching == Wire.WireCrossing.TOUCHING) stringBuilder.append("*");
                else if(wires.get(j).get(i).isTouching == Wire.WireCrossing.NOT_TOUCHING) stringBuilder.append("+");
                else stringBuilder.append(".");

                if(wires.get(j).get(i).right == Wire.WireState.HIGH) stringBuilder.append(highStateColor).append("-").append(reset);
                else if(wires.get(j).get(i).right == Wire.WireState.LOW) stringBuilder.append(lowStateColor).append("-").append(reset);
                else stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
            for (int j = 0; j < wires.size(); j++) {
                if(wires.get(j).get(i).down == Wire.WireState.HIGH) stringBuilder.append(highStateColor).append("| ").append(reset);
                else if(wires.get(j).get(i).down == Wire.WireState.LOW) stringBuilder.append(lowStateColor).append("| ").append(reset);
                else stringBuilder.append("  ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
