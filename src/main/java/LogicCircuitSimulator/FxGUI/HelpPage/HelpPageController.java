package LogicCircuitSimulator.FxGUI.HelpPage;

import LogicCircuitSimulator.App;
import LogicCircuitSimulator.FxGUI.CircuitBoard.FXMLController.SimulationCanvasController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
public class HelpPageController {
    @FXML
    void initialize(){

    }


    public void goBackButton(ActionEvent actionEvent) {
        App.loadAndSetNewScene("/FXML/StartMenu2.fxml");
    }
}
