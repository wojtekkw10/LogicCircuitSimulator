package LogicCircuitSimulator.FxGUI.StartMenu;

import LogicCircuitSimulator.App;
import LogicCircuitSimulator.FxGUI.Board.BoardController;
import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SimulationCanvasController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static LogicCircuitSimulator.App.primaryStage;

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

    public void onLoadButton(ActionEvent actionEvent) {
        FXMLLoader loader = App.loadAndSetNewScene("/FXML/Board.fxml");
        App.primaryStage.setResizable(true);
        BoardController boardController = loader.getController();
        boardController.onLoadButtonClicked(null);
    }
}
