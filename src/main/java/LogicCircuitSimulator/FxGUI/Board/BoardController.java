package LogicCircuitSimulator.FxGUI.Board;

import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SimulationCanvasController;
import LogicCircuitSimulator.Simulation.LogicElements.*;
import LogicCircuitSimulator.Simulation.Rotation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class BoardController {

    @FXML
    public SimulationCanvasController simulationController;

    LogicElement logicElementDragged;
    boolean isLogicElementDragged = false;

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
}
