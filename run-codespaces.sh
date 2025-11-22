#!/bin/bash

echo "=== SecureTrust Banking System - OOAD Assignment ==="
echo "GitHub Codespaces Optimized Version"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

print_info() { echo -e "${BLUE}[INFO]${NC} $1"; }
print_success() { echo -e "${GREEN}[SUCCESS]${NC} $1"; }
print_warning() { echo -e "${YELLOW}[WARNING]${NC} $1"; }
print_error() { echo -e "${RED}[ERROR]${NC} $1"; }

# Check if we're in Codespaces
check_environment() {
    if [ -n "$CODESPACES" ]; then
        print_info "Running in GitHub Codespaces"
        return 0
    else
        print_warning "Not in Codespaces - some features may not work"
        return 1
    fi
}

# Start the application
start_application() {
    print_info "Starting Banking System..."
   
    if check_environment; then
        # Codespaces mode - start with virtual display
        print_info "Starting virtual display for JavaFX..."
        Xvfb :99 -screen 0 1024x768x24 &
        export DISPLAY=:99
        export JAVAFX_PATH="/opt/javafx-sdk/lib"
       
        print_info "Compiling project..."
        mvn clean compile
       
        if [ $? -eq 0 ]; then
            print_success "Compilation successful"
            print_info "Starting application in headless mode with web interface..."
            java -cp "target/classes:src/main/resources" \
                 --module-path $JAVAFX_PATH \
                 --add-modules javafx.controls,javafx.fxml,javafx.graphics \
                 --add-opens javafx.graphics/javafx.scene=ALL-UNNAMED \
                 com.bankingapp.BankingApp --headless
        else
            print_error "Compilation failed"
            exit 1
        fi
    else
        # Local mode
        print_info "Starting in local mode..."
        mvn clean compile exec:java -Dexec.mainClass="com.bankingapp.BankingApp"
    fi
}

# Main execution
main() {
    echo "=================================================="
    echo "BSC Computer Systems Engineering - Year 2"
    echo "CSE202 - Object Oriented Analysis & Design with Java"
    echo "Assignment: Banking System Implementation"
    echo "=================================================="
    echo ""
   
    start_application
}

# Run main function
main "$@"