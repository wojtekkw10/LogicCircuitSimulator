package LogicCircuitSimulator.Simulation.LogicElements;

import LogicCircuitSimulator.Simulation.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class LogicClock extends LogicElement{
    private final int SIGNAL_LENGTH = 10;
    int timer = 0;
    private LogicState state = LogicState.LOW;

    public LogicClock(int x, int y, Rotation rot) {
        super(x, y, rot);
    }

    @Override
    public List<Vector2D> getLocalInputPositions() {
        return new ArrayList<>();
    }

    @Override
    public List<Vector2D> getLocalOutputPositions() {
        ArrayList<Vector2D> outputPositions = new ArrayList<>();
        outputPositions.add(new Vector2D(1,0));
        return outputPositions;
    }

    @Override
    public String getName() {
        return "CLK";
    }

    @Override
    public List<LogicState> computeLocalValues(List<LogicState> states) {
        List<LogicState> outputStates = new ArrayList<>();
        if(timer == SIGNAL_LENGTH){
            if(state == LogicState.HIGH){
                state = LogicState.LOW;
                outputStates.add(state);

            }
            else if(state == LogicState.LOW){
                state = LogicState.HIGH;
                outputStates.add(state);
            }
            timer = 0;
        }
        else{
            outputStates.add(state);
        }


        timer++;
        return outputStates;
    }

    @Override
    public void accept(LogicElementVisitor visitor) {
        visitor.visit(this);
    }
}
