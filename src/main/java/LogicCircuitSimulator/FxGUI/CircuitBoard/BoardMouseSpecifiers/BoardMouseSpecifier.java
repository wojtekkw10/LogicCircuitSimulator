package LogicCircuitSimulator.FxGUI.CircuitBoard.BoardMouseSpecifiers;

public interface BoardMouseSpecifier<T> {
    void removeSpecifiedElement();
    T getSpecifiedElement();
    void setElementAtThisPosition(T element);
}
