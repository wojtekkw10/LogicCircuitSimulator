package LogicCircuitSimulator.Simulation.ExternalDataStorage;

import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static LogicCircuitSimulator.App.primaryStage;

public class FileSystemExternalDataStorage implements ExternalDataStorage{
    @Override
    public void save(String name, String data) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showSaveDialog(primaryStage);

        if(selectedFile != null){
            try(FileWriter fr = new FileWriter(selectedFile)){
                fr.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String load(String name) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if(selectedFile != null){
            try {
                Scanner scanner = new Scanner(selectedFile);
                StringBuilder data = new StringBuilder();
                while(scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    data.append(line).append("\n");
                }
                return data.toString();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
