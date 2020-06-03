package LogicCircuitSimulator.FxGUI.StartMenu;

import LogicCircuitSimulator.App;
import LogicCircuitSimulator.FxGUI.Board.BoardController;
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
        App.primaryStage.setHeight(700);
        App.primaryStage.setWidth(1200);

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
