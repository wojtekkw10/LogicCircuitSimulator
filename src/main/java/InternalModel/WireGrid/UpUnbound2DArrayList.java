package InternalModel.WireGrid;

import InternalModel.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * ArrayList that behaves like it has infinite length
 * @param <T> type of the object in the array
 */
class UpUnbound2DArrayList<T> implements UpUnbound2DList<T>{
    /**
     * Stores objects of type T
     */
    List<List<T>> array = new ArrayList<>();

    /**
     * Provides constructor for creating default (empty) objects in the array
     */
    private final Supplier<? extends T> ctor;

    /**
     * Most basic constructor. Creates an array with one element
     * @param ctor constructor of the object of type T
     */
    UpUnbound2DArrayList(Supplier<? extends T> ctor) {
        this.ctor = Objects.requireNonNull(ctor);
        List<T> column = new ArrayList<>();
        column.add(ctor.get());
        array.add(column);
    }

    /**
     * Creates an arrau of specified size
     * @param ctor constructor of the object of type T
     * @param initialWidth initial width of the array
     * @param initialHeight initial height of the array
     */
    UpUnbound2DArrayList(Supplier<? extends T> ctor, int initialWidth, int initialHeight) {
        this.ctor = Objects.requireNonNull(ctor);
        for (int i = 0; i < initialWidth; i++) {
            List<T> column = new ArrayList<>();
            for (int j = 0; j < initialHeight; j++) {
                column.add(ctor.get());
            }
            array.add(column);
        }
    }

    @Override
    public void set(Vector2D pos, T element){
        int x = pos.getX();
        int y = pos.getY();
        if(x < 0) throw new IllegalArgumentException("Illegal pos.x: "+x+" for pos.x >= 0");
        if(y < 0) throw new IllegalArgumentException("Illegal pos.y: "+x+" for pos.y >= 0");

        if(x > getWidth()) extendGridX(x-getWidth()+1);
        if(y > getHeight()) extendGridY(y-getHeight()+1);

        array.get(x).set(y, element);
    }

    @Override
    public T get(Vector2D pos){
        int x = pos.getX();
        int y = pos.getY();

        if(x < 0) throw new IllegalArgumentException("Illegal x: "+x+" for x >= 0");
        if(y < 0) throw new IllegalArgumentException("Illegal y: "+x+" for y >= 0");

        if(x >= getWidth() || y >= getHeight()) return ctor.get();

        return array.get(x).get(y);
    }

    /**
     * Increases width of the array
     * @param amount number of elements by which the array is extended
     */
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

    /**
     * Increases height of the array
     * @param amount number of elements by which the array is extended
     */
    private void extendGridY(int amount){
        for(int i=0; i<array.size(); i++){
            for (int j = 0; j < amount; j++) {
                array.get(i).add(ctor.get());
            }
        }
    }

    @Override
    public int getWidth(){
        return array.size();
    }
    @Override
    public int getHeight(){
        return array.get(0).size();
    }
}
