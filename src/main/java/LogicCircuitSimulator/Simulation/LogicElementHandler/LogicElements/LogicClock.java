package LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements;

import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.Geometry.LogicElementGeometry;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.Geometry.ZeroInOneOut;
import LogicCircuitSimulator.Simulation.LogicElementVisitor;
import LogicCircuitSimulator.Simulation.LogicState;
import LogicCircuitSimulator.Simulation.Rotation;

import java.util.ArrayList;
import java.util.List;

public class LogicClock extends LogicElement{
    private final int SIGNAL_LENGTH = 5;
    private final int GAP_LENGTH = 15;
    int timer = 0;
    private LogicState state = LogicState.LOW;

    public LogicClock(int x, int y, Rotation rot) {
        super(x, y, rot);
    }

    @Override
    public String getName() {
        return "CLK";
    }

    @Override
    public LogicElementGeometry getNewGeometry() {
        return new ZeroInOneOut();
    }

    @Override
    public List<LogicState> computeLocalValues(List<LogicState> states) {
        List<LogicState> outputStates = new ArrayList<>();
        if(state==LogicState.HIGH && timer == SIGNAL_LENGTH){
            state = LogicState.LOW;
            outputStates.add(state);
            timer = 0;
        }
        else if(state == LogicState.LOW && timer == GAP_LENGTH){
            state = LogicState.HIGH;
            outputStates.add(state);
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
