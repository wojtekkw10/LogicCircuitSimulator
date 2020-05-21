![LCS Logo](src/main/resources/other/logo_grey_low_res.png) 
# Logic Circuit Simulator
Logic Circuit Simulator is a desktop application that allows for creating complex logic circuits and simulating them in real time. 

# Installation
1. Download the repository or click [here](https://github.com/wojtekkw10/LogicCircuitSimulator/archive/master.zip).
2. Unpack it in a desired directory
3. If you're on windows, go to ``simulator-win/bin`` directory
4. If you're on linux, go to ``simulator-lin/bin`` directory
5. Run the simulator.bat file

Note: This will get you the lastest release version. If you want the latest development version you need to:
1. Be on linux
2. Install openjdk-14 e.g. with 
    ```bash
    sudo apt install openjdk-14-jdk
    ```
3. Run command 
    ```bash
    ./gradlew run
    ````
    from the main directory

# Controls
## Gates
To add a gate:\
Num 1: clock \
Num 2: buffer gate\
Num 3: logic constant one\
Num 4: NOT gate\
Num 5: OR gate\
Num 6: AND gate\
Num 7: XOR gate\
Right-click - remove a gate\
R: rotate
## Wires
Left-click and drag - place a wire \
Left-click on a wire and drag: remove a wire 

## Other
K: toggle UPS (Updates Per Second) limit\
P: toggle FPS/UPS stats

# Changelog
20.05.2020 - Version 0.0.1
- Adding and removing wires
- Adding, moving, rotating and removing logic gates 
- Available gates: clock, one, buffer, not, or, not, xor
- Help Page
