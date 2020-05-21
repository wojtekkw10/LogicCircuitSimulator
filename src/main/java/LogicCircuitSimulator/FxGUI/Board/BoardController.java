package LogicCircuitSimulator.FxGUI.Board;

import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SimulationCanvasController;
import LogicCircuitSimulator.Simulation.LogicElements.LogicClock;
import LogicCircuitSimulator.Simulation.LogicElements.LogicElement;
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

    public void logoOnMousePressed(MouseEvent mouseEvent) {
        System.out.println("LOGO PRESSED");
    }

    public void clockButtonOnAction(ActionEvent actionEvent) {
        System.out.println("CLOCK");
        isLogicElementDragged = true;
        logicElementDragged = new LogicClock(0,0, Rotation.RIGHT);
        simulationController.setLogicGateDragged(logicElementDragged);
    }
}
