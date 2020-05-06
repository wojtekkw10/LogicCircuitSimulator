package LogicCircuitSimulator.WireGrid;

import LogicCircuitSimulator.Vector2D;
import LogicCircuitSimulator.WireGrid.Unbound2DList.Unbound2DList;
import LogicCircuitSimulator.WireGrid.Unbound2DList.Unbound2DListHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Unbound2DListHashMapTest {
    Unbound2DList<Integer> array = new Unbound2DListHashMap<>();

    @BeforeEach
    void init(){
        array.set(new Vector2D(10, 10), 1);
        array.set(new Vector2D(-10, 10), 2);
        array.set(new Vector2D(10, -10), 3);
        array.set(new Vector2D(-10, -10), 4);
    }

    @Test
    void getTest(){
        assertTrue(array.get(new Vector2D(10, 10)).isPresent());
        assertTrue(array.get(new Vector2D(-10, 10)).isPresent());
        assertTrue(array.get(new Vector2D(10, -10)).isPresent());
        assertTrue(array.get(new Vector2D(-10, -10)).isPresent());

        assertEquals(1, array.get(new Vector2D(10, 10)).get());
        assertEquals(2, array.get(new Vector2D(-10, 10)).get());
        assertEquals(3, array.get(new Vector2D(10, -10)).get());
        assertEquals(4, array.get(new Vector2D(-10, -10)).get());

        assertFalse(array.get(new Vector2D(0, 0)).isPresent());
    }

    @Test
    void removeTest(){
        array.remove(new Vector2D(10, 10));

        assertFalse(array.get(new Vector2D(10, 10)).isPresent());
    }

    @Test
    void iteratorTest(){
        Iterator2D<Integer> it = array.iterator();

        assertThrows(IllegalStateException.class, it::currentPosition);

        int positionSum = 0;
        int valueSum = 0;
        int numberOfElements = 0;

        while(it.hasNext()){
            valueSum += it.next();
            positionSum += it.currentPosition().getX();
            positionSum += it.currentPosition().getY();
            numberOfElements++;
        }


        assertEquals(10, valueSum);
        assertEquals(0, positionSum);
        assertEquals(4, numberOfElements);
    }
}