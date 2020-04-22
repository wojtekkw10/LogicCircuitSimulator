package InternalModel.WireGrid;

final public class  Wire{
    public enum State{
        HIGH,
        LOW,
        NONE
    }

    public enum WireCrossing{
        TOUCHING,
        NOT_TOUCHING,
    }

    private State right;
    private State down;

    private WireCrossing isTouching;

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

    public final void setRightWire(State right) {
        this.right = right;
    }

    public final void setDownWire(State down) {
        this.down = down;
    }

    public final State getRightWire() {
        return right;
    }

    public final State getDownWire() {
        return down;
    }

    public void setIsTouching(WireCrossing isTouching) {
        this.isTouching = isTouching;
    }

    public WireCrossing isTouching() {
        return isTouching;
    }
}
