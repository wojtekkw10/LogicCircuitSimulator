package LogicCircuitSimulator.UnboundGrid;

import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.UnboundGrid.GridIterator;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.UnboundGrid.UnboundGrid;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeGrid.UnboundGrid.UnboundHashMapGrid;
import LogicCircuitSimulator.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UnboundHashMapGridTest {
    List<Vector2D> testingPositions = new ArrayList<>();
    List<Vector2D> testingEmptyPositions = new ArrayList<>();
    List<Integer> testingValues = new ArrayList<>();

    @BeforeEach
    void init(){
        testingPositions.add(new Vector2D(1,1));
        testingValues.add(1);

        testingPositions.add(new Vector2D(1_234_567, 7_654_321));
        testingValues.add(2);

        testingPositions.add(new Vector2D(234,432));
        testingValues.add(3);

        testingPositions.add(new Vector2D(10_000,1));
        testingValues.add(4);

        testingPositions.add(new Vector2D(1,10_000));
        testingValues.add(5);

        testingEmptyPositions.add(new Vector2D(0, 0));
        testingEmptyPositions.add(new Vector2D(123, 321));
        testingEmptyPositions.add(new Vector2D(432, 234));
        testingEmptyPositions.add(new Vector2D(10_000, 10_000));
    }

    @Test
    void setAndGetInFirstQuadrantTest(){
        setAndGetInQuadrantTest(testingPositions, testingEmptyPositions);
    }

    @Test
    void setAndGetInSecondQuadrantTest(){
        testingPositions = negateXs(testingPositions);
        testingEmptyPositions = negateXs(testingEmptyPositions);

        setAndGetInQuadrantTest(testingPositions, testingEmptyPositions);
    }

    @Test
    void setAndGetInThirdQuadrantTest(){
        testingPositions = negateXs(testingPositions);
        testingEmptyPositions = negateXs(testingEmptyPositions);

        testingPositions = negateYs(testingPositions);
        testingEmptyPositions = negateYs(testingEmptyPositions);

        setAndGetInQuadrantTest(testingPositions, testingEmptyPositions);
    }

    @Test
    void setAndGetInFourthQuadrantTest(){
        testingPositions = negateYs(testingPositions);
        testingEmptyPositions = negateYs(testingEmptyPositions);

        setAndGetInQuadrantTest(testingPositions, testingEmptyPositions);
    }

    void setAndGetInQuadrantTest(List<Vector2D> testingPositions, List<Vector2D> testingEmptyPositions){
        UnboundGrid<Integer> grid = new UnboundHashMapGrid<>();
        for (int i = 0; i < testingPositions.size(); i++) {
            grid.set(testingPositions.get(i), testingValues.get(i));
        }

        for (int i = 0; i < testingPositions.size(); i++) {
            assertTrue(grid.get(testingPositions.get(i)).isPresent());
            assertEquals(testingValues.get(i), grid.get(testingPositions.get(i)).get());
        }

        for (Vector2D testingEmptyPosition : testingEmptyPositions) {
            assertFalse(grid.get(testingEmptyPosition).isPresent());
        }
    }

    void removeTest(UnboundGrid<Integer> grid, List<Vector2D> testingPositions){
        for (Vector2D testingPosition : testingPositions) {
            grid.remove(testingPosition);
        }
        for (Vector2D testingPosition : testingPositions) {
            assertFalse(grid.get(testingPosition).isPresent());
        }
    }

    @Test
    void removeInFirstQuadrantTest(){
        UnboundGrid<Integer> grid = getInitializedUnboundGrid(testingPositions);
        removeTest(grid, testingPositions);
    }

    @Test
    void removeInSecondQuadrantTest(){
        testingPositions = negateXs(testingPositions);
        UnboundGrid<Integer> grid = getInitializedUnboundGrid(testingPositions);
        removeTest(grid, testingPositions);
    }

    @Test
    void removeInThirdQuadrantTest(){
        testingPositions = negateXs(testingPositions);
        testingPositions = negateYs(testingPositions);
        UnboundGrid<Integer> grid = getInitializedUnboundGrid(testingPositions);
        removeTest(grid, testingPositions);
    }

    @Test
    void removeInFourthQuadrantTest(){
        testingPositions = negateYs(testingPositions);
        UnboundGrid<Integer> grid = getInitializedUnboundGrid(testingPositions);
        removeTest(grid, testingPositions);
    }

    @Test
    void iteratorTest(){
        UnboundGrid<Integer> grid = getInitializedUnboundGrid(testingPositions);
        GridIterator<Integer> it = grid.iterator();

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


        assertEquals(15, valueSum);
        assertEquals(8909558, positionSum);
        assertEquals(5, numberOfElements);
    }

    @Test
    void throwsExceptionWhenPosIsNull(){
        UnboundGrid<Integer> grid = getInitializedUnboundGrid(testingPositions);

        assertThrows(NullPointerException.class, ()-> {
            grid.set(null, 5);
        });
    }

    private UnboundGrid<Integer> getInitializedUnboundGrid(List<Vector2D> testingPositions){
        UnboundGrid<Integer> newGrid = new UnboundHashMapGrid<>();
        for (int i = 0; i < testingPositions.size(); i++) {
            newGrid.set(testingPositions.get(i), testingValues.get(i));
        }
        return newGrid;
    }

    private List<Vector2D> negateXs(List<Vector2D> array){
        List<Vector2D> newArray = new ArrayList<>();
        for (Vector2D element : array) {
            Vector2D elementWithNegatedX = new Vector2D(-element.getX(), element.getY());
            newArray.add(elementWithNegatedX);
        }
        return newArray;
    }
    private List<Vector2D> negateYs(List<Vector2D> array){
        List<Vector2D> newArray = new ArrayList<>();
        for (Vector2D element : array) {
            Vector2D elementWithNegatedY = new Vector2D(element.getX(), -element.getY());
            newArray.add(elementWithNegatedY);
        }
        return newArray;
    }
}