package LogicCircuitSimulator.FxGUI.HelpPage;

import LogicCircuitSimulator.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class HelpPageController {
    @FXML
    void initialize(){

    }


    public void goBackButton(ActionEvent actionEvent) {
        App.loadAndSetNewScene("/FXML/StartMenu2.fxml");
    }
}
