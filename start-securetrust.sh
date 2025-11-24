#!/usr/bin/env bash
# start-securetrust.sh — Improved startup script for SecureTrust Banking System
# - Builds with Maven (packages runtime dependencies to target/lib)
# - Auto-detects Codespaces / headless vs GUI
# - Uses mvn javafx:run for JavaFX runs (handles JavaFX module path)
# - Runs the fat classpath java command for headless mode (uses target/lib/*)
# - Attempts to open the default browser when running headless
# - Adds graceful shutdown handling
set -euo pipefail

APP_MAIN="com.bankingapp.BankingApp"
JAVA_FX_MAIN="com.bankingapp.gui.MainView"
JAR_DIR="target"
LIB_DIR="${JAR_DIR}/lib"

info(){ printf "ℹ️  %s\n" "$*"; }
warn(){ printf "⚠️  %s\n" "$*"; }
err(){ printf "❌ %s\n" "$*"; }

echo "🚀 SecureTrust Banking System - Startup Script"
echo "=============================================="

# Detect Codespaces
if [ -n "${CODESPACE_NAME:-}" ]; then
    info "🌐 GitHub Codespaces environment detected"
    MODE="headless"
else
    info "💻 Local development environment detected"
    # Detect GUI availability: DISPLAY on Unix, macOS, Windows typical envs
    if [ -n "${DISPLAY:-}" ] || [[ "$OSTYPE" == "darwin"* ]] || [[ "$OSTYPE" == "cygwin"* ]] || [[ "$OSTYPE" == "msys"* ]]; then
        MODE="gui"
    else
        MODE="headless"
    fi
fi

info "🎯 Starting in ${MODE} mode..."

# Build the project and copy dependencies to target/lib (maven-dependency-plugin configured)
info "🔨 Building project (mvn -DskipTests package)..."
mvn -q -DskipTests package

info "✅ Build successful"

# Ensure lib dir exists if package phase ran
if [ ! -d "${LIB_DIR}" ]; then
    warn "No ${LIB_DIR} directory found — dependencies may not have been copied. Using mvn exec fallback where appropriate."
fi

# Graceful shutdown helper
on_exit() {
    info "Shutting down SecureTrust..."
    # nothing special to kill here; Maven-run processes will exit on Ctrl+C
}
trap on_exit EXIT

# Open URL helper
open_url() {
    url="$1"
    if command -v xdg-open >/dev/null 2>&1; then
        xdg-open "$url" >/dev/null 2>&1 || true
    elif command -v open >/dev/null 2>&1; then
        open "$url" >/dev/null 2>&1 || true
    elif command -v start >/dev/null 2>&1; then
        start "$url" >/dev/null 2>&1 || true
    else
        info "Open your browser and visit: $url"
    fi
}

run_headless() {
    PORT="${PORT:-8080}"
    info "🌐 Starting web server on port ${PORT} (headless)..."

    # Prefer running the packaged classes with runtime dependencies in target/lib
    if [ -d "${LIB_DIR}" ]; then
        CP="${JAR_DIR}/classes:${LIB_DIR}/*"
        info "Using java -cp ${CP} ${APP_MAIN} --headless"
        # Run in foreground so Ctrl+C stops it
        java -cp "${CP}" "${APP_MAIN}" --headless &
        APP_PID=$!
        sleep 1
        info "🔗 Access your application at: http://localhost:${PORT}"
        open_url "http://localhost:${PORT}"
        wait "${APP_PID}"
    else
        # Fallback to mvn exec:java
        if command -v mvn >/dev/null 2>&1; then
            info "Falling back to mvn exec:java (this will run from Maven, not a standalone classpath)"
            mvn -q exec:java -Dexec.mainClass="${APP_MAIN}" -Dexec.args="--headless"
        else
            err "Cannot start headless mode: no ${LIB_DIR} and mvn not available"
            exit 2
        fi
    fi
}

run_gui() {
    info "🎨 Starting JavaFX graphical interface..."
    # Use javafx:run which handles the JavaFX module path and native libs across platforms
    if command -v mvn >/dev/null 2>&1; then
        mvn -q javafx:run -Djavafx.mainClass="${JAVA_FX_MAIN}"
    else
        warn "Maven not found. Attempting to run java directly — ensure JavaFX libs are on module-path."
        # Best-effort: attempt to run with target/lib native jars if present
        if [ -d "${LIB_DIR}" ]; then
            CP="${JAR_DIR}/classes:${LIB_DIR}/*"
            java -cp "${CP}" "${APP_MAIN}" --gui
        else
            err "Cannot start GUI: mvn not found and ${LIB_DIR} missing. Install Maven or build dependencies first."
            exit 3
        fi
    fi
}

if [ "${MODE}" = "headless" ]; then
    run_headless
else
    run_gui
fi