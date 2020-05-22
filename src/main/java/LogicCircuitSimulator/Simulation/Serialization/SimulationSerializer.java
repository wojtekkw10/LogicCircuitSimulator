package LogicCircuitSimulator.Simulation.Serialization;

import LogicCircuitSimulator.Simulation.Simulation;

public interface SimulationSerializer {
    String serialize(Simulation simulation);
    Simulation deserialize(String simulation);
}
