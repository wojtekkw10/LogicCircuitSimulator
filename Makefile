run:
	gradle shadowjar
	java -jar build/libs/LogicCircuitSimulator-all.jar

install: install_linux install_win

install_linux:
	gradle shadowjar
	cp build/libs/LogicCircuitSimulator-all.jar Simulator.jar
	chmod +x Simulator.jar

install_win:
	gradle createExe
	cp build/launch4j/Simulator.exe Simulator.exe
