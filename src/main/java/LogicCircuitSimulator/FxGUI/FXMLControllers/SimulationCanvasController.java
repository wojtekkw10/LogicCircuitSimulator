package LogicCircuitSimulator.FxGUI.FXMLControllers;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;

import java.util.concurrent.*;

public class SimulationCanvasController {
    @FXML
    private Canvas mainSimulationCanvas;
    public AnchorPane mainSimulationAnchorPane;

    private BoardDTO boardDTO;
    private BoardDrawer boardDrawer;

    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final Runnable simulationTask = () -> {
        boardDTO.getSimulation().runOnce();
        boardDTO.getUpdatesSinceLastFrame().getAndIncrement();
    };

    @FXML
    void initialize(){
        boardDTO = new BoardDTO(mainSimulationCanvas);
        BoardDTO.SyncMode syncMode = boardDTO.getSyncMode();
        boardDrawer = new BoardDrawer(boardDTO, mainSimulationAnchorPane);
        new BoardEventHandler(boardDTO);

        if(syncMode == BoardDTO.SyncMode.NOT_SYNCHRONIZED){
            double TARGET_UPS = boardDTO.getTARGET_UPS();
            executor.scheduleAtFixedRate(simulationTask, 0, (long) (1.0/TARGET_UPS*1e6), TimeUnit.MICROSECONDS);
        }

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(boardDTO.isUpsChanged() && syncMode == BoardDTO.SyncMode.NOT_SYNCHRONIZED){
                    executor.shutdownNow();
                    executor = Executors.newSingleThreadScheduledExecutor();
                    executor.scheduleAtFixedRate(simulationTask, 0, (long) (1.0/boardDTO.getTARGET_UPS()*1e6), TimeUnit.MICROSECONDS);
                    boardDTO.setUpsChanged(false);
                }
                if(syncMode == BoardDTO.SyncMode.SYNCHRONIZED){
                    simulationTask.run();
                }
                boardDrawer.draw(now);
            }
        }.start();
    }

    public void shutdown(){
        executor.shutdownNow();
    }
}


