package LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.EventHandlers;

import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.BoardDTO;
import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.SelectionDTO;
import LogicCircuitSimulator.FxGUI.CircuitGrid.FXMLController.Simulation2DSelector;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElementHandler;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElementFactory;
import LogicCircuitSimulator.Simulation.NodeHandler.Node;
import LogicCircuitSimulator.Simulation.NodeHandler.NodeHandler;
import LogicCircuitSimulator.Simulation.NodeHandler.WireState;
import LogicCircuitSimulator.Vector2D;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class SelectionEventHandling {
    private final BoardDTO boardDTO;

    public SelectionEventHandling(BoardDTO boardDTO){
        this.boardDTO = boardDTO;
        initialize();
    }

    private void initialize(){
        EventHandler<KeyEvent> onKeyReleasedEventHandler = event -> {
            if(event.getCode() == KeyCode.V && event.isShortcutDown()){
                boardDTO.setPasted(new SelectionDTO(boardDTO.getCopied()));
                boardDTO.setShouldDrawSelectionRect(false);
                boardDTO.setShouldDrawPastedSystem(true);
            }
            else if(event.getCode() == KeyCode.X && event.isShortcutDown()){
                boardDTO.setCopied(new SelectionDTO(boardDTO.getSelected()));
                boardDTO.setShouldDrawPastedSystem(false);
                boardDTO.setShouldDrawSelectionRect(false);

                List<LogicElement> selectedLogicElements = boardDTO.getSelected().getLogicElementsAsList();
                List<Node> selectedNodes = boardDTO.getSelected().getNodesAsList();
                for (int i = 0; i < selectedNodes.size(); i++) {
                    Vector2D pos = selectedNodes.get(i).getPosition();
                    boardDTO.getSimulation().getNodeHandler().setDownWire(pos, WireState.NONE);
                    boardDTO.getSimulation().getNodeHandler().setRightWire(pos, WireState.NONE);
                }
                for (int i = 0; i < selectedLogicElements.size(); i++) {
                    Vector2D pos = selectedLogicElements.get(i).getPosition();
                    boardDTO.getSimulation().getLogicElementHandler().remove(pos);
                }
            }
            else if(event.getCode() == KeyCode.BACK_SPACE){
                List<LogicElement> selectedLogicElements = boardDTO.getSelected().getLogicElementsAsList();
                List<Node> selectedNodes = boardDTO.getSelected().getNodesAsList();
                for (int i = 0; i < selectedNodes.size(); i++) {
                    Vector2D pos = selectedNodes.get(i).getPosition();
                    boardDTO.getSimulation().getNodeHandler().setDownWire(pos, WireState.NONE);
                    boardDTO.getSimulation().getNodeHandler().setRightWire(pos, WireState.NONE);
                }
                for (int i = 0; i < selectedLogicElements.size(); i++) {
                    Vector2D pos = selectedLogicElements.get(i).getPosition();
                    boardDTO.getSimulation().getLogicElementHandler().remove(pos);
                }
                boardDTO.setShouldDrawSelectionRect(false);
            }
            else if(event.getCode() == KeyCode.C && event.isShortcutDown()){
                boardDTO.setCopied(new SelectionDTO(boardDTO.getSelected()));
                boardDTO.setShouldDrawSelectionRect(false);

            }
        };
        boardDTO.getCanvas().addEventFilter(KeyEvent.KEY_RELEASED, onKeyReleasedEventHandler);

        EventHandler<MouseEvent> onMouseReleasedEventHandler = event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.isStillSincePress()) {
                if(boardDTO.shouldDrawPastedObjects()){
                    boardDTO.setShouldDrawPastedSystem(false);

                    //transfer from clipboard to simulation
                    List<LogicElement> pastedLogicElements = boardDTO.getPasted().getLogicElementsAsList();
                    LogicElementHandler logicElements = boardDTO.getSimulation().getLogicElementHandler();
                    for (LogicElement element : pastedLogicElements) {
                        logicElements.add(LogicElementFactory.instance(element));
                    }

                    List<Node> pastedNodes = boardDTO.getPasted().getNodesAsList();
                    NodeHandler nodes = boardDTO.getSimulation().getNodeHandler();
                    for (Node pastedNode : pastedNodes) {
                        nodes.setNode(new Node(pastedNode));
                    }
                }
            }
            else if(event.isShiftDown()){
                boardDTO.setSelecting(false);
            }

        };
        boardDTO.getCanvas().addEventFilter(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);


        EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
            if(event.getButton() == MouseButton.PRIMARY && event.isShiftDown()){
                if(!boardDTO.isSelecting()){
                    boardDTO.setSelected(new SelectionDTO());
                    boardDTO.setSelecting(true);
                    boardDTO.setShouldDrawSelectionRect(true);
                    Vector2D leftUpper = boardDTO.getProjection2D().projectBack(new Vector2D(event.getX(), event.getY()));
                    boardDTO.setSelectFirstPoint(leftUpper);
                    boardDTO.setSelectSecondPoint(leftUpper);
                }
                else{
                    Vector2D secondPoint = boardDTO.getProjection2D().projectBack(new Vector2D(event.getX(), event.getY()));
                    Vector2D firstPoint = boardDTO.getSelectFirstPoint();

                    Vector2D upperLeft;
                    Vector2D bottomRight;

                    if(firstPoint.getX() < secondPoint.getX() && firstPoint.getY() < secondPoint.getY()){
                        upperLeft = firstPoint;
                        bottomRight = secondPoint;
                    }
                    else if(firstPoint.getX() > secondPoint.getX() && firstPoint.getY() < secondPoint.getY()){
                        upperLeft = new Vector2D(secondPoint.getX(), firstPoint.getY());
                        bottomRight = new Vector2D(firstPoint.getX(), secondPoint.getY());
                    }
                    else if(firstPoint.getX() < secondPoint.getX() && firstPoint.getY() > secondPoint.getY()){
                        upperLeft = new Vector2D(firstPoint.getX(), secondPoint.getY());
                        bottomRight = new Vector2D(secondPoint.getX(), firstPoint.getY());
                    }
                    else{
                        upperLeft = new Vector2D(secondPoint);
                        bottomRight = new Vector2D(firstPoint);
                    }

                    if(!boardDTO.getSelectBottomRight().equals(bottomRight) || !boardDTO.getSelectUpperLeft().equals(upperLeft)){
                        boardDTO.setSelectBottomRight(bottomRight);
                        boardDTO.setSelectUpperLeft(upperLeft);
                        Simulation2DSelector selector = new Simulation2DSelector(boardDTO);
                        SelectionDTO selected = selector.getSelectedObjects();
                        boardDTO.setSelected(selected);
                    }

                    boardDTO.setSelectBottomRight(bottomRight);
                    boardDTO.setSelectUpperLeft(upperLeft);
                }
            }
        };
        boardDTO.getCanvas().addEventFilter(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
    }
}
