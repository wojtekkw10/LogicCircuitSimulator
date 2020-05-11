package LogicCircuitSimulator.FxGUI.FXMLControllers;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;

import java.util.concurrent.*;

public class SimulationCanvasController {
    @FXML
    public Canvas mainSimulationCanvas;
    public AnchorPane mainSimulationAnchorPane;

    BoardDTO boardDTO;
    BoardDrawer boardDrawer;
    BoardEventHandler boardEventHandler;

    @FXML
    void initialize(){
        boardDTO = new BoardDTO(mainSimulationAnchorPane, mainSimulationCanvas);
        BoardDTO.SyncMode syncMode = boardDTO.getSyncMode();
        boardDrawer = new BoardDrawer(boardDTO);
        boardEventHandler = new BoardEventHandler(boardDTO);

        if(syncMode == BoardDTO.SyncMode.NOT_SYNCHRONIZED){
            Runnable simulationTask = boardDTO.getSimulationTask();
            double TARGET_UPS = boardDTO.getTARGET_UPS();
            boardDTO.getExecutor().scheduleAtFixedRate(simulationTask, 0, (long) (1.0/TARGET_UPS*1e6), TimeUnit.MICROSECONDS);
        }

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(syncMode == BoardDTO.SyncMode.SYNCHRONIZED){
                    boardDTO.getSimulationTask().run();
                }
                boardDrawer.draw(now);
            }
        }.start();
    }

    public void shutdown(){
        boardDTO.getExecutor().shutdownNow();
    }
}


