package LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.EventHandlers;

import LogicCircuitSimulator.FxGUI.CircuitGrid.BoardMouseSpecifiers.MouseLogicElementSpecifier;
import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.BoardDTO;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.ButtonLogicElement;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.ToggleOff;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.ToggleOn;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Vector2D;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class LogicElementEventHandler {
    private final BoardDTO boardDTO;

    public LogicElementEventHandler(BoardDTO boardDTO){
        this.boardDTO = boardDTO;
        initialize();
    }

    private void initialize(){
        EventHandler<KeyEvent> onKeyReleasedEventHandler = event -> {
            if(event.getCode() == KeyCode.R){
                if(boardDTO.getIsLogicGateLifted().get()){
                    LogicElement dragged = boardDTO.getLogicGateDragged();

                    if(dragged.getGeometry().getRotation() == Rotation.RIGHT){
                        boardDTO.getLogicGateDragged().setRotation(Rotation.DOWN);
                    }
                    else if(dragged.getGeometry().getRotation() == Rotation.DOWN){
                        boardDTO.getLogicGateDragged().setRotation(Rotation.LEFT);
                    }
                    else if(dragged.getGeometry().getRotation() == Rotation.LEFT){
                        boardDTO.getLogicGateDragged().setRotation(Rotation.UP);
                    }
                    else if(dragged.getGeometry().getRotation() == Rotation.UP){
                        boardDTO.getLogicGateDragged().setRotation(Rotation.RIGHT);
                    }
                }
                else{
                    new MouseLogicElementSpecifier(boardDTO.getSimulation()){
                        @Override
                        public void doAction() {
                            rotateLogicElementClockwise();
                        }
                    }.getElementFromMousePosition(boardDTO.getLastMousePosition(), boardDTO.getProjection2D());
                }

            }
        };
        boardDTO.getCanvas().addEventFilter(KeyEvent.KEY_RELEASED, onKeyReleasedEventHandler);

        EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
            if(event.getButton() == MouseButton.PRIMARY){
                new MouseLogicElementSpecifier(boardDTO.getSimulation()){
                    @Override
                    public void doAction() {
                        boardDTO.setLogicGateDragged(getLogicElement());
                        boardDTO.getLogicGateDragged().setPosition(getPosition());
                        boardDTO.getIsLogicGateLifted().set(true);
                        boardDTO.setRelativeMouseToLogicGatePos(getRelativeMousePos());
                        removeLogicElement();
                    }
                }.getElementFromMousePosition(new Vector2D(event.getX(), event.getY()), boardDTO.getProjection2D());
            }
        };
        boardDTO.getCanvas().addEventFilter(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);

        //ON MOUSE RELEASED
        EventHandler<MouseEvent> onMouseReleasedEventHandler = event -> {
            Vector2D mousePos = new Vector2D(event.getX(), event.getY());
            if(boardDTO.getIsLogicGateLifted().get() && event.getButton() == MouseButton.PRIMARY){
                new MouseLogicElementSpecifier(boardDTO.getSimulation()){
                    @Override
                    public void doAction() {
                        boardDTO.getLogicGateDragged().setPosition(getPosition());
                        boardDTO.getSimulation().getLogicElementHandler().add(boardDTO.getLogicGateDragged());
                        boardDTO.getIsLogicGateLifted().set(false);
                    }
                }.getElementPosFromElementAndMousePosition(mousePos,  boardDTO.getProjection2D(), boardDTO.getLogicGateDragged(), boardDTO.getRelativeMouseToLogicGatePos());

                new MouseLogicElementSpecifier(boardDTO.getSimulation()){
                    @Override
                    public void doAction() {
                        LogicElement selectedElement = getLogicElement();

                        if(selectedElement.getName().equals("TGL_ON")){
                            Vector2D pos = getPosition();
                            removeLogicElement();
                            LogicElement toggleOff = new ToggleOff((int)pos.getX(), (int)pos.getY(), selectedElement.getRotation());
                            boardDTO.getSimulation().getLogicElementHandler().add(toggleOff);
                        }
                        else if(selectedElement.getName().equals("TGL_OFF")){
                            Vector2D pos = getPosition();
                            removeLogicElement();
                            LogicElement toggleOn = new ToggleOn((int)pos.getX(), (int)pos.getY(), selectedElement.getRotation());
                            boardDTO.getSimulation().getLogicElementHandler().add(toggleOn);
                        }
                        else if(selectedElement.getName().equals("BTN")){
                            Vector2D pos = getPosition();
                            removeLogicElement();
                            LogicElement toggleOn = new ButtonLogicElement((int)pos.getX(), (int)pos.getY(), selectedElement.getRotation(), true);
                            boardDTO.getSimulation().getLogicElementHandler().add(toggleOn);
                        }
                    }
                }.getElementFromMousePosition(mousePos, boardDTO.getProjection2D());
            }
            else if(event.getButton() == MouseButton.SECONDARY && event.isStillSincePress()){
                new MouseLogicElementSpecifier(boardDTO.getSimulation()){
                    @Override
                    public void doAction() {
                        removeLogicElement();
                    }
                }.getElementFromMousePosition(new Vector2D(event.getX(), event.getY()), boardDTO.getProjection2D());
            }
        };
        boardDTO.getCanvas().addEventFilter(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);


        EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
            boardDTO.setLastMousePosition(new Vector2D(event.getX(), event.getY()));

            if(event.getButton() == MouseButton.SECONDARY){
                new MouseLogicElementSpecifier(boardDTO.getSimulation()){
                    @Override
                    public void doAction() {
                        removeLogicElement();
                    }
                }.getElementFromMousePosition(new Vector2D(event.getX(), event.getY()), boardDTO.getProjection2D());
            }
        };
        boardDTO.getCanvas().addEventFilter(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
    }
}
