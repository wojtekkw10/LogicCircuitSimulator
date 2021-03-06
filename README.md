![LCS Logo](src/main/resources/other/logo_grey_low_res.png) 
# Logic Circuit Simulator
Logic Circuit Simulator is a desktop application that lets the user create complex logic circuits and simulate them in real time. <br><br>
![Start](src/main/resources/Github/Start.png)
![Simulation](src/main/resources/Github/Example.png)

# How to run
You need to:
1. Be on linux
2. Download the program by clicking [here](https://github.com/wojtekkw10/LogicCircuitSimulator/archive/master.zip)
3. Install openjdk-11 e.g. with 
    ```bash
    sudo apt install openjdk-11-jdk
    ```
4. Run command 
    ```bash
    ./gradlew run
    ````
    from the main directory

# Controls
## Gates
Right-click - remove <br>
R: rotate
## Wires
Left-click and drag - place a wire <br>
Right-click and drag: remove a wire 

## Other
Shift: select <br>
P: toggle FPS/UPS stats <br>
Ctrl-x - Cut <br>
Ctrl-c - Copy <br>
Ctrl-v - Paste <br>
Ctrl-s - Save

# Changelog
### 02.06.2020 - Version 0.0.2
- Added the toolbox on the left
- Added saving and loading from file
- Added UPS slider
- Added Copy/Paste 
### 20.05.2020 - Version 0.0.1
- Adding and removing wires
- Adding, moving, rotating and removing logic gates 
- Available gates: clock, one, buffer, not, or, not, xor
- Help Page
