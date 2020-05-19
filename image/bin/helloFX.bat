@echo off
set DIR="%~dp0"
set JAVA_EXEC="%DIR:"=%\java"
pushd %DIR% & %JAVA_EXEC% -Djdk.gtk.version=2 -p "%~dp0/../app" -m LogicCircuitSimulator/LogicCircuitSimulator.Launcher  %* & popd
