module LogicCircuitSimulator {
    requires javafx.controls;
    requires javafx.fxml;
    requires jsr305;
    requires ejml.simple;
    requires org.antlr.antlr4.runtime;
    exports LogicCircuitSimulator;
    exports LogicCircuitSimulator.FxGUI.StartMenu;
    opens LogicCircuitSimulator.FxGUI.StartMenu;
    opens LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController;
    opens LogicCircuitSimulator.FxGUI.HelpPage;
    opens  LogicCircuitSimulator.FxGUI.Board;
}
