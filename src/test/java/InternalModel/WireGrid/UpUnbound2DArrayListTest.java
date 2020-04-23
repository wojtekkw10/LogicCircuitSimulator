package InternalModel.WireGrid;

import InternalModel.Vector2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpUnbound2DArrayListTest {
    @Test
    void setAndGetElementTest(){
        UpUnbound2DArrayList<Wire> array = new UpUnbound2DArrayList<>(Wire::new);

        array.set(new Vector2D(100, 100), new Wire(Wire.State.HIGH, Wire.State.LOW, Wire.WireCrossing.TOUCHING));

        Wire wire = array.get(new Vector2D(100, 100));
        assertEquals(Wire.State.HIGH, wire.getRightWire());
        assertEquals(Wire.State.LOW, wire.getDownWire());
        assertEquals(Wire.WireCrossing.TOUCHING, wire.isTouching());

        wire = array.get(new Vector2D(50,50));
        assertEquals(Wire.State.NONE, wire.getRightWire());
        assertEquals(Wire.State.NONE, wire.getDownWire());
        assertEquals(Wire.WireCrossing.NOT_TOUCHING, wire.isTouching());

        wire = array.get(new Vector2D(1000,1000));
        assertEquals(Wire.State.NONE, wire.getRightWire());
        assertEquals(Wire.State.NONE, wire.getDownWire());
        assertEquals(Wire.WireCrossing.NOT_TOUCHING, wire.isTouching());

        assertThrows(IllegalArgumentException.class, () -> {
            array.get(new Vector2D(-1, 0));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            array.get(new Vector2D(0, -1));
        });


    }

}