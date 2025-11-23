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

# Parse args: treat --headless as request but in Codespaces we will run GUI inside a virtual display
FORCE_HEADLESS=false
for arg in "$@"; do
  case "$arg" in
    --headless|-h)
      FORCE_HEADLESS=true;
      ;;
  esac
done

# Check if we're in Codespaces
check_environment() {
    if [ -n "$CODESPACES" ] || [ -n "$CODESPACE_NAME" ]; then
        print_info "Running in GitHub Codespaces"
        return 0
    else
        print_warning "Not in Codespaces - some features may not work"
        return 1
    fi
}

start_xvfb_and_vnc() {
    print_info "Starting virtual display (Xvfb) on :99..."
    Xvfb :99 -screen 0 1280x720x24 >/tmp/xvfb.log 2>&1 &
    XVFB_PID=$!
    export DISPLAY=:99
    sleep 0.5

    # Start x11vnc if available so the Codespace can expose a VNC view
    if command -v x11vnc >/dev/null 2>&1; then
        print_info "Starting x11vnc on :99..."
        x11vnc -display :99 -forever -nopw -shared -bg -o x11vnc.log || print_warning "x11vnc failed to start"
    else
        print_warning "x11vnc not installed. Install x11vnc to enable VNC access."
    fi

    # Start websockify/noVNC if available (serve VNC over websocket on port 6080)
    if command -v websockify >/dev/null 2>&1; then
        if [ -d "/usr/share/novnc" ]; then
            NOVNC_DIR="/usr/share/novnc"
        elif [ -d "/opt/novnc" ]; then
            NOVNC_DIR="/opt/novnc"
        else
            NOVNC_DIR=""
        fi

        if [ -n "$NOVNC_DIR" ]; then
            print_info "Starting websockify (noVNC) on port 6080..."
            websockify --web "$NOVNC_DIR" 6080 localhost:5900 >/tmp/websockify.log 2>&1 &
            WEBSOCKIFY_PID=$!
        else
            print_warning "noVNC web files not found; websockify will still proxy if available."
            websockify 6080 localhost:5900 >/tmp/websockify.log 2>&1 &
            WEBSOCKIFY_PID=$!
        fi
    else
        print_warning "websockify not installed. Install websockify/noVNC to access VNC via browser."
    fi

    # Small wait to ensure services are up
    sleep 1
}

# Start the application
start_application() {
    print_info "Starting Banking System..."

    if check_environment; then
        # Codespaces mode - run GUI inside virtual display and expose VNC
        print_info "Codespaces detected. Running GUI inside virtual display so --headless will behave like GUI."

        start_xvfb_and_vnc

        print_info "Building project (mvn package -DskipTests)..."
        mvn -DskipTests package
        if [ $? -ne 0 ]; then
            print_error "Maven build failed"
            exit 1
        fi
        print_success "Build complete"

        # Use target/lib dependencies if they exist (pom copies runtime deps there)
        CP="target/classes"
        if [ -d "target/lib" ]; then
            CP="$CP:target/lib/*"
        fi

        # Prefer mvn exec:java (classpath with Maven-provided OpenJFX) first
        print_info "Launching MainView via Maven exec:java to ensure OpenJFX deps are on classpath"
        mvn -Dexec.mainClass="com.bankingapp.gui.MainView" exec:java
        MVN_EXIT=$?
        if [ $MVN_EXIT -eq 0 ]; then
            exit 0
        else
            print_warning "mvn exec:java failed (exit $MVN_EXIT); falling back to direct java execution"
        fi

        # Ensure JavaFX module path from typical Codespaces location if present
        JAVAFX_PATH="/opt/javafx-sdk/lib"
        if [ -d "$JAVAFX_PATH" ]; then
            print_info "Using JavaFX modules from $JAVAFX_PATH"
            print_info "Launching GUI (MainView) inside virtual display using JavaFX SDK..."
            java -cp "$CP" \
                 --module-path "$JAVAFX_PATH" \
                 --add-modules javafx.controls,javafx.fxml,javafx.graphics \
                 --add-opens javafx.graphics/javafx.scene=ALL-UNNAMED \
                 com.bankingapp.gui.MainView
        else
            print_info "JavaFX modules not found at $JAVAFX_PATH. Trying to run from classpath (target/lib) as last resort."
            java -cp "$CP" com.bankingapp.gui.MainView
        fi

    else
        # Local mode: behave as usual. Honor explicit --headless if provided.
        if [ "$FORCE_HEADLESS" = true ]; then
            print_info "Starting in explicit headless mode (web interface)..."
            mvn clean compile exec:java -Dexec.mainClass="com.bankingapp.BankingApp" -Dexec.args="--headless"
        else
            print_info "Starting locally with GUI (MainView)..."
            mvn -Dexec.mainClass="com.bankingapp.gui.MainView" exec:java
        fi
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

    start_application "$@"
}

# Run main function
main "$@"