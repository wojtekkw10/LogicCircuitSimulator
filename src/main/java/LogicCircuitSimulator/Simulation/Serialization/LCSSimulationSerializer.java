package LogicCircuitSimulator.Simulation.Serialization;

import LogicCircuitSimulator.Simulation.LCSSimulation;

public interface LCSSimulationSerializer {
    String serialize(LCSSimulation simulation);
    LCSSimulation deserialize(String simulation) throws InvalidSerializedSimulationException;
}
