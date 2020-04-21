package InternalModel;

public class Wire{
    enum WireState{
        HIGH, LOW, NONE
    }
    WireState right;
    WireState down;

    enum WireCrossing{
        TOUCHING,
        NOT_TOUCHING,
    }

    WireCrossing isTouching;

    Wire(){
        right = WireState.NONE;
        down = WireState.NONE;
        isTouching = WireCrossing.NOT_TOUCHING;
    }

    Wire(WireState right, WireState down, WireCrossing wireCrossing){
        this.right = right;
        this.down = down;
        this.isTouching = wireCrossing;
    }
}
