package LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.EventHandlers;

import LogicCircuitSimulator.FxGUI.CircuitGrid.BoardMouseSpecifiers.MouseCrossingSpecifier;
import LogicCircuitSimulator.FxGUI.CircuitGrid.BoardMouseSpecifiers.MouseWireSpecifier;
import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.BoardDTO;
import LogicCircuitSimulator.Simulation.NodeHandler.Crossing;
import LogicCircuitSimulator.Simulation.NodeHandler.WireState;
import LogicCircuitSimulator.Vector2D;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class NodeEventHandler {
    private final BoardDTO boardDTO;

    public NodeEventHandler(BoardDTO boardDTO){
        this.boardDTO = boardDTO;
        initialize();
    }

    private void initialize(){
        EventHandler<MouseEvent> onMouseReleasedEventHandler = event -> {
            Vector2D mousePos = new Vector2D(event.getX(), event.getY());

            if (event.getButton() == MouseButton.PRIMARY && event.isStillSincePress()) {
                if(!boardDTO.shouldDrawPastedObjects()){
                    new MouseCrossingSpecifier(boardDTO.getSimulation()){
                        @Override
                        public void doAction() {
                            if(getNumberOfWiresAround() >= 3){
                                if (getCrossing() == Crossing.TOUCHING) updateCrossing(Crossing.NOT_TOUCHING);
                                else updateCrossing(Crossing.TOUCHING);
                            }
                        }
                    }.performTransformation(mousePos, boardDTO.getProjection2D());
                }
            }
        };
        boardDTO.getCanvas().addEventFilter(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

        EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
            boardDTO.setLastMousePosition(new Vector2D(event.getX(), event.getY()));

            if(event.getButton() == MouseButton.SECONDARY){
                new MouseWireSpecifier(boardDTO.getSimulation()){
                    @Override
                    public void doAction() {
                        this.updateWireState(WireState.NONE);
                    }
                }.performTransformation(new Vector2D(event.getX(), event.getY()), boardDTO.getProjection2D());
            }

            if(event.getButton() == MouseButton.PRIMARY && !boardDTO.getIsLogicGateLifted().get()
            && !event.isShiftDown()){
                new MouseWireSpecifier(boardDTO.getSimulation()){
                    @Override
                    public void doAction() {
                        this.updateWireState(WireState.LOW);
                    }
                }.performTransformation(new Vector2D(event.getX(), event.getY()), boardDTO.getProjection2D());
            }
        };
        boardDTO.getCanvas().addEventFilter(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
    }
}
