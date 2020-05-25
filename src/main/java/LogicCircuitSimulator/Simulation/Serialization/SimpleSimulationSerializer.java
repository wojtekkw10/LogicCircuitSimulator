package LogicCircuitSimulator.Simulation.Serialization;

import LogicCircuitSimulator.Simulation.LogicElements.*;
import LogicCircuitSimulator.Simulation.NodeHandler.*;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Simulation.Simulation;
import LogicCircuitSimulator.Vector2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class SimpleSimulationSerializer implements SimulationSerializer{
    @Override
    public  String serialize(Simulation simulation) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Node> nodes = simulation.nodeIterator();
        while(nodes.hasNext()){
            Node node = nodes.next();
            stringBuilder.append("WI ");
            stringBuilder.append((int)node.getPosition().getX()).append(" ");
            stringBuilder.append((int)node.getPosition().getY()).append(" ");
            stringBuilder.append(node.getRightWire()).append(" ");
            stringBuilder.append(node.getDownWire()).append(" ");
            stringBuilder.append(node.isTouching()).append(" ");
            stringBuilder.append("\n");
        }

        Iterator<LogicElement> elements = simulation.logicElementIterator();
        while(elements.hasNext()){
            LogicElement element = elements.next();
            stringBuilder.append("LE ");
            stringBuilder.append(serializeLogicElement(element));
            stringBuilder.append("\n");

        }
        return stringBuilder.toString();
    }

    private String serializeLogicElement(LogicElement logicElement){

        return logicElement.getName() + " " +
                (int)logicElement.getPosition().getX() + " " +
                (int)logicElement.getPosition().getY() + " " +
                logicElement.getRotation();
    }

    @Override
    public Simulation deserialize(String simulation) {
        Simulation newSimulation = new Simulation();
        NodeHandler nodeHandler = new ArrayNodeHandler();
        List<LogicElement> logicElements = new ArrayList<>();

        Scanner scanner = new Scanner(simulation);

        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            String label = lineScanner.next();
            if(label.equals("WI")){
                Node node = deserializeNode(lineScanner);
                nodeHandler.setNode(node);
            }
            if(label.equals("LE")){
                LogicElement logicElement = deserializeLogicElement(lineScanner);
                logicElements.add(logicElement);
            }

        }
        newSimulation.setLogicElements(logicElements);
        newSimulation.setNodeHandler(nodeHandler);
        return newSimulation;
    }

    private Node deserializeNode(Scanner scanner){
        double x = scanner.nextDouble();
        double y = scanner.nextDouble();
        String wireStateRight = scanner.next();
        String wireStateDown = scanner.next();
        String givenCrossing = scanner.next();
        WireState right = WireState.NONE;
        if(wireStateRight.equals("LOW")) right = WireState.LOW;
        else if(wireStateRight.equals("HIGH")) right = WireState.HIGH;

        WireState down = WireState.NONE;
        if(wireStateDown.equals("LOW")) down = WireState.LOW;
        else if(wireStateDown.equals("HIGH")) down = WireState.HIGH;

        Crossing crossing = Crossing.TOUCHING;
        if(givenCrossing.equals("NOT_TOUCHING")) crossing = Crossing.NOT_TOUCHING;

        return new Node(new Vector2D(x,y), right, down, crossing);

    }
    private LogicElement deserializeLogicElement(Scanner scanner){
        String name = scanner.next();
        double x = scanner.nextDouble();
        double y = scanner.nextDouble();
        int intX = (int)x;
        int intY = (int)y;
        String stringRotation = scanner.next();

        Rotation rotation = switch (stringRotation) {
            case "DOWN" -> Rotation.DOWN;
            case "LEFT" -> Rotation.LEFT;
            case "UP" -> Rotation.UP;
            default -> Rotation.RIGHT;
        };

        LogicElement logicElement = null;
        if(name.equals("CLK")) logicElement = new LogicClock(intX, intY, rotation);
        if(name.equals("ONE")) logicElement = new LogicOne(intX, intY, rotation);
        if(name.equals("BFR")) logicElement = new BufferGate(intX, intY, rotation);
        if(name.equals("NOT")) logicElement = new NotGate(intX, intY, rotation);
        if(name.equals("OR")) logicElement = new OrGate(intX, intY, rotation);
        if(name.equals("AND")) logicElement = new AndGate(intX, intY, rotation);
        if(name.equals("XOR")) logicElement = new XorGate(intX, intY, rotation);

        return logicElement;
    }
}
