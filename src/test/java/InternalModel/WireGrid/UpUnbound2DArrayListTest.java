package InternalModel.WireGrid;

import InternalModel.Vector2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpUnbound2DArrayListTest {
    @Test
    void setAndGetElementTest(){
        UpUnbound2DArrayList<Node> array = new UpUnbound2DArrayList<>(Node::new);

        array.set(new Vector2D(100, 100), new Node(Node.State.HIGH, Node.State.LOW, Node.WireCrossing.TOUCHING));

        Node node = array.get(new Vector2D(100, 100));
        assertEquals(Node.State.HIGH, node.getRightWire());
        assertEquals(Node.State.LOW, node.getDownWire());
        assertEquals(Node.WireCrossing.TOUCHING, node.isTouching());

        node = array.get(new Vector2D(50,50));
        assertEquals(Node.State.NONE, node.getRightWire());
        assertEquals(Node.State.NONE, node.getDownWire());
        assertEquals(Node.WireCrossing.NOT_TOUCHING, node.isTouching());

        node = array.get(new Vector2D(1000,1000));
        assertEquals(Node.State.NONE, node.getRightWire());
        assertEquals(Node.State.NONE, node.getDownWire());
        assertEquals(Node.WireCrossing.NOT_TOUCHING, node.isTouching());

        assertThrows(IllegalArgumentException.class, () -> {
            array.get(new Vector2D(-1, 0));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            array.get(new Vector2D(0, -1));
        });


    }

}