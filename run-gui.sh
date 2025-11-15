#!/bin/bash

echo "=== SecureTrust Banking System - GUI Launcher ==="
echo "Java Version: $(java -version 2>&1 | head -n1)"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to check command existence
check_command() {
    if ! command -v $1 &> /dev/null; then
        echo -e "${RED}Error: $1 is not installed or not in PATH${NC}"
        return 1
    fi
    return 0
}

# Function to compile and run manually
compile_and_run_manual() {
    echo -e "${YELLOW}Compiling manually...${NC}"
   
    # Create build directory
    mkdir -p build
   
    # Find all Java files
    find src/main/java -name "*.java" > sources.txt
   
    # Compile with JavaFX modules
    javac --module-path $JAVAFX_PATH \
          --add-modules javafx.controls,javafx.fxml \
          -d build @sources.txt
   
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}Compilation successful!${NC}"
        echo -e "${YELLOW}Running application...${NC}"
       
        java --module-path $JAVAFX_PATH \
             --add-modules javafx.controls,javafx.fxml \
             -cp build com.bankingapp.BankingApp
    else
        echo -e "${RED}Compilation failed!${NC}"
        exit 1
    fi
}

# Main execution
echo "1. Checking dependencies..."
check_command java || exit 1

# Try different ways to run the application
if [ -f "gradlew" ]; then
    echo "2. Using Gradle Wrapper..."
    ./gradlew run
elif command -v gradle &> /dev/null; then
    echo "2. Using system Gradle..."
    gradle run
else
    echo -e "${YELLOW}Gradle not found. Checking for JavaFX...${NC}"
   
    # Look for JavaFX in common locations
    if [ -n "$JAVAFX_PATH" ]; then
        echo "Using JavaFX from: $JAVAFX_PATH"
        compile_and_run_manual
    else
        echo -e "${RED}JavaFX not found. Please:"
        echo "1. Install Gradle OR"
        echo "2. Set JAVAFX_PATH environment variable to JavaFX lib directory"
        echo "3. Use an IDE with JavaFX support${NC}"
        exit 1
    fi
fi