package LogicCircuitSimulator.FxGUI.StartMenu;

import LogicCircuitSimulator.App;
import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SimulationCanvasController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class StartMenuController {

    @FXML
    void initialize(){

    }

    public void onStartButton(ActionEvent actionEvent) {
        App.loadAndSetNewScene("/FXML/Board.fxml");
        App.primaryStage.setResizable(true);

    }

    public void onExitButton(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void onHelpButton(ActionEvent actionEvent) {
        App.loadAndSetNewScene("/FXML/HelpPage.fxml");
    }
}
