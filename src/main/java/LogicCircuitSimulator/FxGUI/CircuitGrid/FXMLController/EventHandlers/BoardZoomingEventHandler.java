package LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.EventHandlers;

import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.BoardDTO;
import javafx.event.EventHandler;
import javafx.scene.input.ScrollEvent;

public class BoardZoomingEventHandler {
    private final BoardDTO boardDTO;
    private final double SCALING_FACTOR = 0.05;

    public BoardZoomingEventHandler(BoardDTO boardDTO){
        this.boardDTO = boardDTO;
        initialize();
    }

    private void initialize(){
        double MAX_ZOOM = boardDTO.getMAX_ZOOM();
        double MIN_ZOOM = boardDTO.getMIN_ZOOM();

        // ON SCROLL
        EventHandler<ScrollEvent> onScrollEventHandler = event -> {
            double currentScale = boardDTO.getProjection2D().getScale();
            if(event.getDeltaY()>0 && currentScale < MAX_ZOOM) {
                boardDTO.getProjection2D().scale(1+SCALING_FACTOR, boardDTO.getLastMousePosition());
            }
            else if(event.getDeltaY()<0 && currentScale > MIN_ZOOM) {
                boardDTO.getProjection2D().scale(1-SCALING_FACTOR, boardDTO.getLastMousePosition());
            }
        };
        boardDTO.getCanvas().addEventFilter(ScrollEvent.SCROLL, onScrollEventHandler);
    }

}
