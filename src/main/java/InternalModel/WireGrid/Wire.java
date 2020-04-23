package InternalModel.WireGrid;

import javax.annotation.concurrent.Immutable;

/**
 * Wire class. The most basic unit in logic circuit grid.
 */
@Immutable
final public class  Wire{
    /**
     * State of the wire
     */
    public enum State{
        /**
         * Signal is in the HIGH state, represents logical 1
         */
        HIGH,
        /**
         * Signal is in the LOW state, represents logical 0
         */
        LOW,
        /**
         * There is no wire in this place
         */
        NONE
    }

    /**
     * The way two wires cross
     */
    public enum WireCrossing{
        /**
         * They're touching, signal from horizontal wire propagates to the vertical wire and vice versa
         */
        TOUCHING,
        /**
         * They're not touching, signal from horizontal wire does NOT propagate to the vertical wire and vice versa
         */
        NOT_TOUCHING,
    }

    /**
     * State of the wire to right from this point
     */
    private final State right;

    /**
     * State of the wire downwards from this point
     */
    private final State down;

    /**
     * Whether or not the wires are touching at this point
     */
    private final WireCrossing isTouching;

    public Wire(){
        right = State.NONE;
        down = State.NONE;
        isTouching = WireCrossing.NOT_TOUCHING;
    }

    public Wire(State right, State down, WireCrossing wireCrossing){
        this.right = right;
        this.down = down;
        this.isTouching = wireCrossing;
    }

    public final State getRightWire() {
        return right;
    }

    public final State getDownWire() {
        return down;
    }

    public WireCrossing isTouching() {
        return isTouching;
    }
}
