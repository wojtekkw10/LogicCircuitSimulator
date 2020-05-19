module LogicCircuitSimulator {
    requires java.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires jsr305;
    requires ejml.simple;
    exports LogicCircuitSimulator;
    opens LogicCircuitSimulator.FxGUI.StartMenu;
    opens LogicCircuitSimulator.FxGUI.CircuitBoard.FXMLController;
    opens LogicCircuitSimulator.FxGUI.HelpPage;
}