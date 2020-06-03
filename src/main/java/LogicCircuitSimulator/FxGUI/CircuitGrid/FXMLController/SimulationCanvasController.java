package LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController;

import LogicCircuitSimulator.App;
import LogicCircuitSimulator.Simulation.LCSSimulation;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElement;
import LogicCircuitSimulator.Vector2D;
import javafx.animation.AnimationTimer;
import javafx.beans.property.IntegerProperty;
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
    private final BoardDTO.SyncMode syncMode = BoardDTO.SyncMode.SYNCHRONIZED;
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
        new BoardEventHandler(boardDTO);

        if(syncMode == BoardDTO.SyncMode.NOT_SYNCHRONIZED){
            double TARGET_UPS = boardDTO.getTARGET_UPS();
            executor.scheduleAtFixedRate(simulationTask, 0, (long) (1.0/TARGET_UPS*1e6), TimeUnit.MICROSECONDS);
        }

        new AnimationTimer() {
            final BoardDrawer boardDrawer = new BoardDrawer(boardDTO, mainSimulationAnchorPane);
            double accruedIterations = 0;
            @Override
            public void handle(long now) {
                if(boardDTO.isUpsChanged() && syncMode == BoardDTO.SyncMode.NOT_SYNCHRONIZED){
                    executor.shutdownNow();
                    executor = Executors.newSingleThreadScheduledExecutor();
                    executor.scheduleAtFixedRate(simulationTask, 0, (long) (1.0/boardDTO.getTARGET_UPS()*1e6), TimeUnit.MICROSECONDS);
                    boardDTO.setUpsChanged(false);
                }
                boardDrawer.draw(now);

                if(syncMode == BoardDTO.SyncMode.SYNCHRONIZED){
                    if(boardDTO.getTARGET_UPS() < 1000000){
                        for (int i = 0; i < boardDTO.getTARGET_UPS()/60; i++) {
                            simulationTask.run();
                            if(System.nanoTime() > now + (1e9/60)) break;
                        }
                        accruedIterations += boardDTO.getTARGET_UPS()/60.0 - (int)(boardDTO.getTARGET_UPS()/60) ;

                        if(accruedIterations > 1){
                            for (int i = 0; i < (int)accruedIterations; i++) {
                                simulationTask.run();
                            }
                            accruedIterations = accruedIterations - (int)accruedIterations;
                        }

                    }
                    else{
                        while(System.nanoTime() < now + (1e9/60)){
                            simulationTask.run();
                        }
                    }
                }

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
    public IntegerProperty getTargetUpsProperty(){
        return this.boardDTO.getUPSProperty();
    }

    public void setPasted(SelectionDTO selectionDTO){
        boardDTO.setPasted(selectionDTO);
        boardDTO.setShouldDrawPastedSystem(true);
    }
}


