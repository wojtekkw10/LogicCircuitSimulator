run:
	gradle shadowjar
	java -jar build/libs/LogicCircuitSimulator-all.jar

install:
	gradle shadowjar
	cp build/libs/LogicCircuitSimulator-all.jar Simulator.jar
	chmod +x Simulator.jar
