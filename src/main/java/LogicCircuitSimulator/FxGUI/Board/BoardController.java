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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.NumberStringConverter;

public class BoardController {

    @FXML
    public SimulationCanvasController simulationController;

    @FXML
    public AnchorPane boardAnchorPane;
    public Slider upsSlider;
    public Label upsLabel;

    @FXML
    void initialize(){
        IntegerProperty ups = simulationController.getTargetUpsProperty();
        //ups.bindBidirectional(upsSlider.valueProperty());
        upsSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int newUps = (int)Math.pow(1.15,  newValue.doubleValue());

                if(newUps > 1_000_000){
                    upsLabel.textProperty().setValue("MAX");
                }
                else{
                    String labelText = String.format("%d", newUps);
                    upsLabel.textProperty().setValue(labelText);
                }
                ups.setValue(newUps);
                boardAnchorPane.fireEvent(new KeyEvent(boardAnchorPane, boardAnchorPane, KeyEvent.KEY_PRESSED,
                        "", "", KeyCode.TAB, false, false, false, false));
            }
        });
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
