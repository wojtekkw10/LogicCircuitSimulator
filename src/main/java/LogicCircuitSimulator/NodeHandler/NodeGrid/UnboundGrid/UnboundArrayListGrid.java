package LogicCircuitSimulator.NodeHandler.NodeGrid.UnboundGrid;

import LogicCircuitSimulator.Vector2D;

import java.util.*;

public class UnboundArrayListGrid<T> implements UnboundGrid<T>{
    ArrayList<ArrayList<T>> array = new ArrayList<>();

    Vector2D shift = new Vector2D(0,0);

    public UnboundArrayListGrid(){
        for (int i = 0; i < 25; i++) {
            ArrayList<T> column = new ArrayList<>();
            for (int j = 0; j < 12; j++) {
                column.add(null);
            }
            array.add(column);
        }
    }

    @Override
    public Optional<T> get(Vector2D pos) {
        return Optional.ofNullable(array.get((int)pos.getX()).get((int)pos.getY()));
    }

    @Override
    public void set(Vector2D pos, T element) {
        array.get((int)pos.getX()).set((int)pos.getY(), element);
    }

    @Override
    public void remove(Vector2D pos) {
        array.get((int)pos.getX()).set((int)pos.getY(), null);
    }

    @Override
    public GridIterator<T> iterator() {
        return new MainGridIterator();
    }

    private class MainGridIterator implements GridIterator<T> {
        Vector2D currentPosition;
        boolean finished = false;
        boolean alreadySearched = false;
        boolean lastAnswer = false;

        @Override
        public boolean hasNext() {
            if(!alreadySearched){
                if(currentPosition == null) currentPosition = new Vector2D(0,0);

                while(hasNextPosition(currentPosition) ){
                    if(getElement(nextPosition(currentPosition)) == null){
                        currentPosition = nextPosition(currentPosition);
                    }
                    else break;
                }

                if(currentPosition.equals(new Vector2D(array.size()-1, array.get(array.size()-1).size()-1))) {
                    lastAnswer = false;
                    alreadySearched = true;
                    finished = true;
                    return false;
                }

/*                if(getElement(currentPosition) == null) {
                    finished = true;
                    lastAnswer = false;
                    return false;
                }*/

                if(hasNextPosition(currentPosition)) {
                    lastAnswer = true;
                    alreadySearched = true;
                    return true;
                }
                else {
                    finished = true;
                    lastAnswer = false;
                    alreadySearched = true;
                    return false;
                }
            }
            else{
                return lastAnswer;
            }

        }

        private T getElement(Vector2D currentPosition) {
            return array.get((int) currentPosition.getX()).get((int) currentPosition.getY());
        }

        @Override
        public T next() {
            if(finished) throw new NoSuchElementException("Unbound list has no more elements");
            currentPosition = nextPosition(currentPosition);
            alreadySearched = false;
            return getElement(currentPosition);
        }

        @Override
        public Vector2D currentPosition() {
            if(currentPosition == null){
                throw new IllegalStateException("The next() function hasn't been called yet");
            }
            return currentPosition;
        }

        //private functions

        private Vector2D nextPosition(Vector2D pos){
            if(pos.getX() < array.size() - 1) return new Vector2D(pos.getX()+1, pos.getY());
            else{
                return new Vector2D(0, pos.getY()+1);
            }
        }
        private boolean hasNextPosition(Vector2D pos){
            //System.out.println(currentPosition);
            if(pos.getX() < array.size() - 1) return true;
            else{
                if(pos.getY() < array.get((int)pos.getX()).size() - 1) return true;
                else return false;
            }
        }
    }


}
