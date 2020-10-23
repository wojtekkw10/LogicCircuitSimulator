package LogicCircuitSimulator.Simulation.Serialization;

import LogicCircuitSimulator.Simulation.LCSSimulation;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElementHandler;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElement;
import LogicCircuitSimulator.Simulation.LogicElementHandler.LogicElements.LogicElementFactory;
import LogicCircuitSimulator.Simulation.LogicElementHandler.SimpleLogicElementHandler;
import LogicCircuitSimulator.Simulation.NodeHandler.*;
import LogicCircuitSimulator.Simulation.Rotation;
import LogicCircuitSimulator.Simulation.SimpleLCSSimulation;
import LogicCircuitSimulator.Vector2D;

import java.util.Iterator;
import java.util.Scanner;

public class SimpleLCSSimulationSerializer implements LCSSimulationSerializer {
    @Override
    public  String serialize(LCSSimulation simulation) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Node> nodes = simulation.getNodeHandler().iterator();
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

        Iterator<LogicElement> elements = simulation.getLogicElementHandler().iterator();
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
    public LCSSimulation deserialize(String simulation) {
        LCSSimulation newSimulation = new SimpleLCSSimulation();
        NodeHandler nodeHandler = new ArrayNodeHandler();
        LogicElementHandler logicElements = new SimpleLogicElementHandler();

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
        newSimulation.setLogicElementHandler(logicElements);
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

/*        Rotation rotation = switch (stringRotation) {
            case "DOWN" -> Rotation.DOWN;
            case "LEFT" -> Rotation.LEFT;
            case "UP" -> Rotation.UP;
            default -> Rotation.RIGHT;
        };*/

        Rotation rotation = null;
        if(stringRotation.equals("DOWN")) rotation = Rotation.DOWN;
        else if(stringRotation.equals("LEFT")) rotation = Rotation.LEFT;
        else if(stringRotation.equals("UP")) rotation = Rotation.UP;
        else if(stringRotation.equals("RIGHT")) rotation = Rotation.RIGHT;

        return LogicElementFactory.instance(name, intX, intY, rotation);
    }
}
