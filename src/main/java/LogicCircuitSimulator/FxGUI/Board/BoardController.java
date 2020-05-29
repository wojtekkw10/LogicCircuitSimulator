package LogicCircuitSimulator.FxGUI.Board;

import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SimulationCanvasController;
import LogicCircuitSimulator.Simulation.ExternalDataStorage.FileSystemExternalDataStorage;
import LogicCircuitSimulator.Simulation.LCSSimulation;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.*;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Simulation.Serialization.SimpleLCSSimulationSerializer;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.NumberStringConverter;

public class BoardController {

    @FXML
    public SimulationCanvasController simulationController;

    @FXML
    public AnchorPane boardAnchorPane;
    public JFXSlider upsSlider;

    @FXML
    void initialize(){
        IntegerProperty ups = simulationController.getTargetUpsProperty();
        ups.bindBidirectional(upsSlider.valueProperty());
    }

    public void onSaveButtonClicked(MouseEvent mouseEvent) {
        LCSSimulation simulation = simulationController.getSimulation();
        String serializedSimulation = new SimpleLCSSimulationSerializer().serialize(simulation);
        new FileSystemExternalDataStorage().save(null, serializedSimulation);
    }

    public void onLoadButtonClicked(MouseEvent mouseEvent) {
        String serializedSimulation = new FileSystemExternalDataStorage().load(null);

        if(serializedSimulation != null){
            LCSSimulation simulation =  new SimpleLCSSimulationSerializer().deserialize(serializedSimulation);
            simulationController.setSimulation(simulation);
        }
    }

    public void onNotButtonAction(ActionEvent actionEvent) {
        simulationController.setLogicGateDragged(new NotGate(0,0, Rotation.RIGHT));

    }

    public void onOrButtonAction(ActionEvent actionEvent) {
        simulationController.setLogicGateDragged(new OrGate(0,0, Rotation.RIGHT));
    }

    public void onAndButtonAction(ActionEvent actionEvent) {
        simulationController.setLogicGateDragged(new AndGate(0,0, Rotation.RIGHT));
    }

    public void onXorButtonAction(ActionEvent actionEvent) {
        simulationController.setLogicGateDragged(new XorGate(0,0, Rotation.RIGHT));
    }

    public void onToggleButtonAction(ActionEvent actionEvent) {
        simulationController.setLogicGateDragged(new ToggleOff(0,0, Rotation.RIGHT));
    }

    public void onButtonButtonAction(ActionEvent actionEvent) {
        simulationController.setLogicGateDragged(new ButtonLogicElement(0,0, Rotation.RIGHT));
    }

    public void onClkButtonAction(ActionEvent actionEvent) {
        simulationController.setLogicGateDragged(new LogicClock(0,0, Rotation.RIGHT));
    }

    public void onBufferButtonAction(ActionEvent actionEvent) {
        simulationController.setLogicGateDragged(new BufferGate(0,0, Rotation.RIGHT));
    }

    public void onOneButtonAction(ActionEvent actionEvent) {
        simulationController.setLogicGateDragged(new LogicOne(0,0, Rotation.RIGHT));
    }
}
