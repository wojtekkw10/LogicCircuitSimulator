package InternalModel.WireGrid.Unbound2DList;

import InternalModel.Vector2D;
import InternalModel.WireGrid.Iterator2D;

import java.util.*;

public class Unbound2DListHashMap<T> implements Unbound2DList<T> {
    Map<Vector2D, T> hashMap = new HashMap<>();

    @Override
    public Optional<T> get(Vector2D pos) {
        return Optional.ofNullable(hashMap.get(pos));
    }

    @Override
    public void set(Vector2D pos, T element) {
        hashMap.put(pos, element);
    }

    @Override
    public void remove(Vector2D pos) {
        hashMap.remove(pos);
    }

    @Override
    public Iterator2D<T> iterator() {
        return new MainIterator();
    }

    private class MainIterator implements Iterator2D<T>{
        Vector2D currentPosition;
        Iterator<Map.Entry<Vector2D, T>> entryIterator = hashMap.entrySet().iterator();

        @Override
        public boolean hasNext() {
            return entryIterator.hasNext();
        }

        @Override
        public T next() {
            if(!hasNext()) throw new NoSuchElementException("Unbound list has no more elements");
            Map.Entry<Vector2D, T> element = entryIterator.next();
            currentPosition = element.getKey();
            return element.getValue();
        }

        @Override
        public Vector2D currentPosition() {
            if(currentPosition == null){
                throw new IllegalStateException("The next() function hasn't been called yet");
            }
            return currentPosition;
        }
    }
}