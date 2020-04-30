package LogicCircuitSimulator.LogicElements;

import LogicCircuitSimulator.LogicElementVisitor;
import LogicCircuitSimulator.LogicState;
import LogicCircuitSimulator.Vector2D;
import com.google.common.collect.Streams;

import java.util.ArrayList;
import java.util.List;

public class LogicClock extends LogicElement{
    private LogicState state = LogicState.LOW;

    public LogicClock(int x, int y, Rotation rot) {
        super(x, y, rot);
    }

    @Override
    List<Vector2D> getLocalInputPositions() {
        return new ArrayList<>();
    }

    @Override
    List<Vector2D> getLocalOutputPositions() {
        ArrayList<Vector2D> outputPositions = new ArrayList<>();
        outputPositions.add(new Vector2D(1,0));
        return outputPositions;
    }

    @Override
    List<LogicState> computeLocalValues(List<LogicState> states) {
        List<LogicState> outputStates = new ArrayList<>();
        if(state == LogicState.HIGH){
            state = LogicState.LOW;
            outputStates.add(LogicState.HIGH);

        }
        else if(state == LogicState.LOW){
            state = LogicState.HIGH;
            outputStates.add(LogicState.LOW);
        }
        return outputStates;
    }

    @Override
    public void accept(LogicElementVisitor visitor) {
        visitor.visit(this);
    }
}
