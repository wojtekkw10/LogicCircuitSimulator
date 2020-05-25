package LogicCircuitSimulator.FxGUI.Board;

import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SimulationCanvasController;
import LogicCircuitSimulator.Simulation.ExternalDataStorage.FileSystemExternalDataStorage;
import LogicCircuitSimulator.Simulation.LogicElements.*;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Simulation.Serialization.SimpleSimulationSerializer;
import LogicCircuitSimulator.Simulation.Simulation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static LogicCircuitSimulator.App.primaryStage;

public class BoardController {

    @FXML
    public SimulationCanvasController simulationController;

    @FXML
    public AnchorPane boardAnchorPane;


    @FXML
    void initialize(){
    }


    public void bfrOnMouseClicked(MouseEvent mouseEvent) {
        simulationController.setLogicGateDragged(new BufferGate(0,0, Rotation.RIGHT));

    }

    public void oneOnMouseClicked(MouseEvent mouseEvent) {
        simulationController.setLogicGateDragged(new LogicOne(0,0, Rotation.RIGHT));
    }

    public void notOnMouseClicked(MouseEvent mouseEvent) {
        simulationController.setLogicGateDragged(new NotGate(0,0, Rotation.RIGHT));
    }

    public void orOnMouseClicked(MouseEvent mouseEvent) {
        simulationController.setLogicGateDragged(new OrGate(0,0, Rotation.RIGHT));
    }

    public void andOnMouseClicked(MouseEvent mouseEvent) {
        simulationController.setLogicGateDragged(new AndGate(0,0, Rotation.RIGHT));
    }

    public void xorOnMouseClicked(MouseEvent mouseEvent) {
        simulationController.setLogicGateDragged(new XorGate(0,0, Rotation.RIGHT));
    }

    public void clkOnMouseClicked(MouseEvent mouseEvent) {
        simulationController.setLogicGateDragged(new LogicClock(0,0, Rotation.RIGHT));
    }

    public void onSaveButtonClicked(MouseEvent mouseEvent) {
        Simulation simulation = simulationController.getSimulation();
        String serializedSimulation = new SimpleSimulationSerializer().serialize(simulation);
        new FileSystemExternalDataStorage().save(null, serializedSimulation);
    }

    public void onLoadButtonClicked(MouseEvent mouseEvent) {
        String serializedSimulation = new FileSystemExternalDataStorage().load(null);

        if(serializedSimulation != null){
            Simulation simulation =  new SimpleSimulationSerializer().deserialize(serializedSimulation);
            simulationController.setSimulation(simulation);
        }
    }

    public void tglOnMouseClicked(MouseEvent mouseEvent) {
        simulationController.setLogicGateDragged(new ToggleOff(0,0, Rotation.RIGHT));

    }

    public void btnOnMouseClicked(MouseEvent mouseEvent) {
        simulationController.setLogicGateDragged(new ButtonLogicElement(0,0, Rotation.RIGHT));
    }
}
