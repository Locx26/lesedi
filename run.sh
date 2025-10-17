#!/bin/bash

# Compile all Java files
echo "Compiling Java files..."
find src/main/java -name "*.java" > sources.txt
javac -d build @sources.txt

# Run the GUI test
echo "Running GUI Test..."
xvfb-run java --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml -cp build com.bankingapp.test.GUITest
