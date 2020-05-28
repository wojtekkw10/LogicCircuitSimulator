package LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController;

import LogicCircuitSimulator.App;
import LogicCircuitSimulator.Simulation.LCSSimulation;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElement;
import LogicCircuitSimulator.Vector2D;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimulationCanvasController {
    @FXML
    private Canvas mainSimulationCanvas;
    public AnchorPane mainSimulationAnchorPane;

    private BoardDTO boardDTO;

    private ScheduledExecutorService executor;

    private final Runnable simulationTask = () -> {
        boardDTO.getSimulation().runOnce();
        boardDTO.getUpdatesSinceLastFrame().getAndIncrement();
    };

    @FXML
    void initialize(){
        App.primaryStage.setOnCloseRequest(e -> this.shutdown());
        boardDTO = new BoardDTO(mainSimulationCanvas);
        executor = boardDTO.getExecutor();
        BoardDTO.SyncMode syncMode = boardDTO.getSyncMode();
        new BoardEventHandler(boardDTO);

        if(syncMode == BoardDTO.SyncMode.NOT_SYNCHRONIZED){
            double TARGET_UPS = boardDTO.getTARGET_UPS();
            executor.scheduleAtFixedRate(simulationTask, 0, (long) (1.0/TARGET_UPS*1e6), TimeUnit.MICROSECONDS);
        }

        new AnimationTimer() {
            final BoardDrawer boardDrawer = new BoardDrawer(boardDTO, mainSimulationAnchorPane);
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

    public void setLogicGateDragged(LogicElement logicElement){
        boardDTO.setLogicGateDragged(logicElement);
        boardDTO.getIsLogicGateLifted().set(true);
        boardDTO.setRelativeMouseToLogicGatePos(new Vector2D(0.5, 0));
    }

    public LCSSimulation getSimulation(){
        return boardDTO.getSimulation();
    }

    public void shutdown(){
        executor.shutdownNow();
    }

    public void setSimulation(LCSSimulation simulation) {
        boardDTO.setSimulation(simulation);
    }
}


