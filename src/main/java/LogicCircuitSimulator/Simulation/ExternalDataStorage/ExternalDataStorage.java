package LogicCircuitSimulator.Simulation.ExternalDataStorage;

import java.io.File;

public interface ExternalDataStorage {
    void save(File file, String data);
    String load(String name);
}
