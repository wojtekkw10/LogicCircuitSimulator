package InternalModel.WireGrid;

import InternalModel.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class UpUnbound2DArrayList<T> {
    List<List<T>> array = new ArrayList<>();
    private final Supplier<? extends T> ctor;

    UpUnbound2DArrayList(Supplier<? extends T> ctor) {
        this.ctor = Objects.requireNonNull(ctor);
        List<T> column = new ArrayList<>();
        column.add(ctor.get());
        array.add(column);
    }

    public void set(Vector2D pos, T element){
        int x = pos.getX();
        int y = pos.getY();
        if(x < 0) throw new IllegalArgumentException("Illegal pos.x: "+x+" for pos.x >= 0");
        if(y < 0) throw new IllegalArgumentException("Illegal pos.y: "+x+" for pos.y >= 0");

        if(x > getWidth()) extendGridX(x-getWidth()+1);
        if(y > getHeight()) extendGridY(y-getHeight()+1);

        array.get(x).set(y, element);
    }

    public T get(Vector2D pos){
        int x = pos.getX();
        int y = pos.getY();

        if(x < 0) throw new IllegalArgumentException("Illegal x: "+x+" for x >= 0");
        if(y < 0) throw new IllegalArgumentException("Illegal y: "+x+" for y >= 0");

        if(x >= getWidth() || y >= getHeight()) return ctor.get();

        return array.get(x).get(y);
    }

    private void extendGridX(int amount){
        int columnSize = array.get(0).size();
        for(int i=0; i<amount; i++){
            List<T> column = new ArrayList<>();
            for (int j = 0; j < columnSize; j++) {
                column.add(ctor.get());
            }
            array.add(column);
        }
    }
    private void extendGridY(int amount){
        for(int i=0; i<array.size(); i++){
            for (int j = 0; j < amount; j++) {
                array.get(i).add(ctor.get());
            }
        }
    }

    private int getWidth(){
        return array.size();
    }
    private int getHeight(){
        return array.get(0).size();
    }
}
