package LogicCircuitSimulator.FxGUI.Board;

import LogicCircuitSimulator.BooleanExpressionParser.BooleanExpressionParser;
import LogicCircuitSimulator.BooleanExpressionParser.SimpleBooleanExpressionParser;
import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SelectionDTO;
import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SimulationCanvasController;
import LogicCircuitSimulator.Simulation.ExternalDataStorage.FileSystemExternalDataStorage;
import LogicCircuitSimulator.Simulation.LCSSimulation;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.*;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Simulation.Serialization.SimpleLCSSimulationSerializer;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.util.Optional;

import static LogicCircuitSimulator.App.primaryStage;

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
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showSaveDialog(primaryStage);
        new FileSystemExternalDataStorage().save(selectedFile, serializedSimulation);
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

    public void onInButtonAction(ActionEvent actionEvent) {
        simulationController.setLogicGateDragged(new InputGate(0,0, Rotation.RIGHT));
    }

    public void onOutButtonAction(ActionEvent actionEvent) {
        simulationController.setLogicGateDragged(new OutputGate(0,0, Rotation.RIGHT));
    }

    public void onFromBooleanExpressionAction(ActionEvent actionEvent) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setResizable(true);
        dialog.setTitle("Boolean Expression");
        dialog.setHeaderText("Enter boolean expression");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPadding(new Insets(20, 150, 10, 10));

        TextArea booleanExpressionTextArea = new TextArea();
        booleanExpressionTextArea.setText("((a AND b) AND c AND c AND d) OR d AND (((d AND e) AND (f OR g OR h OR j)) AND a) OR\n" +
                " NOT((a AND b) AND c) OR (NOT(a AND b) AND c AND c AND d) OR d AND (((d AND e)\n" +
                " AND NOT(f OR g OR h OR j)) AND a) OR ((a AND b) AND c)");
        AnchorPane.setTopAnchor(booleanExpressionTextArea, 10.0);
        AnchorPane.setRightAnchor(booleanExpressionTextArea, 10.0);
        AnchorPane.setBottomAnchor(booleanExpressionTextArea, 10.0);
        AnchorPane.setLeftAnchor(booleanExpressionTextArea, 10.0);
        anchorPane.getChildren().add(booleanExpressionTextArea);

        dialog.getDialogPane().setContent(anchorPane);

        // Request focus on the textArea by default
        Platform.runLater(booleanExpressionTextArea::requestFocus);

        // Convert the result to a string
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                if(!booleanExpressionTextArea.getText().equals(""))
                return booleanExpressionTextArea.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(booleanExpression -> {
            BooleanExpressionParser parser = new SimpleBooleanExpressionParser();
            simulationController.setPasted(parser.parse(booleanExpression));
        });
    }
}
