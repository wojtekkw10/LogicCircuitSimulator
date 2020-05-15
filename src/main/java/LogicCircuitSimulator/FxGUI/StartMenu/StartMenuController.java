package LogicCircuitSimulator.FxGUI.StartMenu;

import LogicCircuitSimulator.App;
import LogicCircuitSimulator.FxGUI.CircuitBoard.FXMLController.SimulationCanvasController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class StartMenuController {

    @FXML
    void initialize(){

    }

    public void onStartButton(ActionEvent actionEvent) {
        FXMLLoader loader = App.loadAndSetNewScene("/FXML/SimulationCanvas.fxml");

        // cleanup controller resources when window closes:
        SimulationCanvasController controller = loader.getController();
        App.primaryStage.setOnCloseRequest(e -> controller.shutdown());
    }

    public void onExitButton(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void onHelpButton(ActionEvent actionEvent) {
        App.loadAndSetNewScene("/FXML/HelpPage.fxml");
    }
}
