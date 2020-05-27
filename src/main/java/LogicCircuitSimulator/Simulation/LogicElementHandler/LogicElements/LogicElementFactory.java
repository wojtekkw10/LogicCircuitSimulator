package LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements;

import LogicCircuitSimulator.Simulation.Rotation;

public class LogicElementFactory {
    public static LogicElement instance(String type, int x, int y, Rotation rotation){
        if(type.equals("CLK")) return new LogicClock(x,y,rotation);
        if(type.equals("AND")) return new AndGate(x,y,rotation);
        if(type.equals("BFR")) return new BufferGate(x,y,rotation);
        if(type.equals("BTN")) return new ButtonLogicElement(x,y,rotation);
        if(type.equals("ONE")) return new LogicOne(x,y,rotation);
        if(type.equals("NOT")) return new NotGate(x,y,rotation);
        if(type.equals("OR")) return new OrGate(x,y,rotation);
        if(type.equals("TGL_ON")) return new ToggleOn(x,y,rotation);
        if(type.equals("TGL_FF")) return new ToggleOff(x,y,rotation);
        if(type.equals("XOR")) return new XorGate(x,y,rotation);
        else return null;
    }
    public static LogicElement instance(LogicElement logicElement){
        return LogicElementFactory.instance(
                logicElement.getName(),
                (int)logicElement.getX(),
                (int)logicElement.getY(),
                logicElement.getRotation());
    }
}
