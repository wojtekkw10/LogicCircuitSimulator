/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package LogicCircuitSimulator;

import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SimulationCanvasController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App  extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    public static Stage primaryStage;
    private static final String stageTitle = "Logic Circuit Simulator";
    static App app;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        app = this;


        //System.out.println(app.getClass());
        primaryStage.setTitle(stageTitle);
        stage.getIcons().add(new Image("file:resources/other/logo_grey.png"));
        loadAndSetNewScene("/FXML/StartMenu.fxml");
    }

    public static FXMLLoader loadAndSetNewScene(String url){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(app.getClass().getResource(url));

        Parent pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert pane != null;
        Scene scene = new Scene(pane, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);

        // cleanup controller resources when window closes:
        if(loader.getController() instanceof SimulationCanvasController){
            SimulationCanvasController controller = loader.getController();
            primaryStage.setOnCloseRequest(e -> controller.shutdown());
        }
        return loader;
    }
}