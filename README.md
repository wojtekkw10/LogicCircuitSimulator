To make it compile in Intellij:\
File >> Project Structure >> Modules >> Dependency >> + (on left-side of window)
   
clicking the "+" sign will let you designate the directory where you have unpacked JavaFX's "lib" folder.
   
Scope is Compile (which is the default.) You can then edit this to call it JavaFX by double-clicking on the line.
   
then in:
Run >> Edit Configurations
Add this line to VM Options:
```bash
--module-path /path/to/JavaFX/lib --add-modules=javafx.controls
```
ok. This is unnecessary.