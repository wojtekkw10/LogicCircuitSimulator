package LogicCircuitSimulator.FxGUI.HelpPage;

import LogicCircuitSimulator.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class HelpPageController {
    public ScrollPane scrollPane;

    @FXML
    void initialize(){
        scrollPane.setVvalue(1);
    }


    public void goBackButton(ActionEvent actionEvent) {
        App.loadAndSetNewScene("/FXML/StartMenu.fxml");
    }
}
