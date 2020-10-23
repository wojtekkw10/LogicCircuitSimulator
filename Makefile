run:
	./gradlew run

check:
	./gradlew shadowjar
	java -jar build/libs/LogicCircuitSimulator-all.jar Simulator.jar

install:
	./gradlew shadowjar
	cp build/libs/LogicCircuitSimulator-all.jar Simulator.jar
	chmod +x Simulator.jar

install_win:
	./gradlew createExe
	cp build/launch4j/Simulator.exe Simulator.exe

install_jlink:
	./gradlew jlink
	cp build/image image
