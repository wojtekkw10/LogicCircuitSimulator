package LogicCircuitSimulator.Simulation.ExternalDataStorage;

public interface ExternalDataStorage {
    void save(String name, String data);
    String load(String name);
}
