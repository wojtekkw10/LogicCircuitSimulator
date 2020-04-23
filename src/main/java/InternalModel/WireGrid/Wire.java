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

    //TODO: docs gdyby nie wire to bysmy musieli miec osobną klasę na crossing i na wire
    //  musielibysmy miec dwie rózne tablice na to
    //  przy wyciaganiu trzeba by miec iterator zwracający osobno pozycje, crossing, i wire up i right // czyli tak jak jest teraz
    //  albo pozycje, crossing, tylko jeden wire w zaleznosci od wiersza (wtedy zwrocimy ten sam crossing dla down i right
}
