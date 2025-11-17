#!/bin/bash

echo "=== Maven + JavaFX Runner for VS Code ==="

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

print_info() { echo -e "${BLUE}[INFO]${NC} $1"; }
print_success() { echo -e "${GREEN}[SUCCESS]${NC} $1"; }
print_warning() { echo -e "${YELLOW}[WARNING]${NC} $1"; }
print_error() { echo -e "${RED}[ERROR]${NC} $1"; }

# Check if Maven is available
check_maven() {
    if ! command -v mvn &> /dev/null; then
        print_error "Maven is not installed or not in PATH"
        print_info "Download Maven from: https://maven.apache.org/download.cgi"
        exit 1
    fi
    print_success "Maven found: $(mvn --version | head -n1)"
}

# Check if JavaFX SDK is available
check_javafx() {
    local javafx_paths=(
        "/usr/share/openjfx/lib"
        "/usr/lib/jvm/openjfx/lib"
        "$HOME/javafx-sdk-17.0.8/lib"
        "./javafx-sdk-17.0.8/lib"
        "./javafx/lib"
    )
   
    for path in "${javafx_paths[@]}"; do
        if [ -f "$path/javafx.controls.jar" ]; then
            JAVAFX_PATH="$path"
            print_success "JavaFX found at: $JAVAFX_PATH"
            return 0
        fi
    done
   
    print_warning "JavaFX SDK not found in common locations"
    print_info "Download from: https://gluonhq.com/products/javafx/"
    print_info "Extract to: ./javafx-sdk-17.0.8/ or set JAVAFX_PATH environment variable"
    return 1
}

# Run with Maven and JavaFX
run_with_maven() {
    local javafx_path=${JAVAFX_PATH:-"./javafx-sdk-17.0.8/lib"}
   
    if [ ! -f "$javafx_path/javafx.controls.jar" ]; then
        print_error "JavaFX not found at: $javafx_path"
        print_info "Please set JAVAFX_PATH environment variable or download JavaFX SDK"
        exit 1
    fi
   
    print_info "Running with Maven and JavaFX from: $javafx_path"
   
    # Set environment variable for Maven
    export JAVAFX_HOME="$javafx_path"
   
    # Run Maven with JavaFX module path
    mvn compile javafx:run -Djavafx.verbose=true -Djavafx.debug=true \
        -Djavafx.platform=monocle \
        -Dprism.verbose=true \
        -Dprism.order=sw
}

# Alternative: Run with explicit module path
run_with_explicit_path() {
    local javafx_path=${JAVAFX_PATH:-"./javafx-sdk-17.0.8/lib"}
   
    print_info "Compiling with Maven..."
    mvn clean compile
   
    if [ $? -ne 0 ]; then
        print_error "Compilation failed"
        exit 1
    fi
   
    print_info "Running application..."
    java --module-path "$javafx_path" \
         --add-modules javafx.controls,javafx.fxml,javafx.graphics \
         --add-opens javafx.graphics/javafx.scene=ALL-UNNAMED \
         --add-opens javafx.controls/javafx.scene.control=ALL-UNNAMED \
         --add-opens javafx.base/javafx.collections=ALL-UNNAMED \
         -cp "target/classes:src/main/resources" \
         com.bankingapp.BankingApp
}

# Main execution
main() {
    echo "Maven + JavaFX Solution for VS Code"
    echo "===================================="
    echo ""
   
    check_maven
    check_javafx
   
    print_info "Choose run method:"
    echo "1) Maven javafx:run (Recommended)"
    echo "2) Explicit Java module path"
    echo "3) Download JavaFX automatically"
    echo ""
    read -p "Enter choice [1-3]: " choice
   
    case $choice in
        1)
            run_with_maven
            ;;
        2)
            run_with_explicit_path
            ;;
        3)
            download_javafx
            run_with_maven
            ;;
        *)
            print_info "Using default: Maven javafx:run"
            run_with_maven
            ;;
    esac
}

# Download JavaFX automatically
download_javafx() {
    print_info "Downloading JavaFX SDK..."
   
    local os=$(uname -s | tr '[:upper:]' '[:lower:]')
    local arch=$(uname -m)
   
    case "$os" in
        linux*)
            if [ "$arch" = "x86_64" ]; then
                wget -q "https://download2.gluonhq.com/openjfx/17.0.8/openjfx-17.0.8_linux-x64_bin-sdk.zip"
                unzip -q openjfx-17.0.8_linux-x64_bin-sdk.zip
                JAVAFX_PATH="./javafx-sdk-17.0.8/lib"
            fi
            ;;
        darwin*)
            wget -q "https://download2.gluonhq.com/openjfx/17.0.8/openjfx-17.0.8_osx-x64_bin-sdk.zip"
            unzip -q openjfx-17.0.8_osx-x64_bin-sdk.zip
            JAVAFX_PATH="./javafx-sdk-17.0.8/lib"
            ;;
        *)
            print_error "Unsupported OS: $os"
            exit 1
            ;;
    esac
   
    if [ -f "$JAVAFX_PATH/javafx.controls.jar" ]; then
        print_success "JavaFX downloaded and extracted"
    else
        print_error "JavaFX download failed"
        exit 1
    fi
}

# Run main function
main "$@"