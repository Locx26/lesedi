#!/usr/bin/env bash
# setup-codespace-javafx.sh — Robust Codespaces setup for SecureTrust JavaFX Banking System
# - Installs minimal deps, downloads JavaFX SDK matching project JavaFX version,
# - Configures environment variables (JAVAFX_HOME, JAVA_HOME) dynamically,
# - Creates project workspace layout if missing,
# - Adds convenient start/test scripts, and fixes quoting/paths found in the original.
set -euo pipefail

# Configuration: change if your project expects a different JavaFX version
JAVAFX_VERSION="21.0.5"
JAVAFX_SDK_ZIP="openjfx-${JAVAFX_VERSION}_linux-x64_bin-sdk.zip"
JAVAFX_DOWNLOAD_URL="https://download2.gluonhq.com/openjfx/${JAVAFX_VERSION}/${JAVAFX_SDK_ZIP}"

# Determine project directory: prefer GITHUB_WORKSPACE (Codespaces), fall back to common paths
PROJECT_DIR="${GITHUB_WORKSPACE:-/workspaces/banking-system}"
START_SCRIPT="/usr/local/bin/start-banking-app"
TEST_SCRIPT="/usr/local/bin/test-banking-app"

echo "=== SecureTrust Codespaces JavaFX Setup ==="
echo "Project dir: $PROJECT_DIR"
echo "JavaFX version to install: $JAVAFX_VERSION"

# Install required packages (noninteractive)
sudo apt-get update
sudo DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
    xvfb \
    wget \
    unzip \
    ca-certificates \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libgtk-3-0 \
    maven \
    || { echo "Package install failed"; exit 1; }

# Download JavaFX SDK if not already present under /opt/javafx-sdk
if [ ! -d /opt/javafx-sdk ] || [ ! -f "/opt/javafx-sdk/release" ]; then
    echo "📦 Downloading JavaFX SDK ${JAVAFX_VERSION}..."
    tmpzip="/tmp/${JAVAFX_SDK_ZIP}"
    wget -q -O "${tmpzip}" "${JAVAFX_DOWNLOAD_URL}" || { echo "Failed to download JavaFX SDK"; exit 1; }
    sudo unzip -q -d /opt/ "${tmpzip}"
    sudo mv "/opt/javafx-sdk-${JAVAFX_VERSION}" /opt/javafx-sdk || true
    rm -f "${tmpzip}"
    echo "✅ JavaFX SDK installed to /opt/javafx-sdk"
else
    echo "✅ JavaFX SDK already present at /opt/javafx-sdk"
fi

# Determine JAVA_HOME dynamically from the installed java (robust across environments)
if command -v java >/dev/null 2>&1; then
    JAVA_BIN="$(readlink -f "$(command -v java)")"
    JAVA_HOME_CANDIDATE="$(dirname "$(dirname "${JAVA_BIN}")")"
    export JAVA_HOME="${JAVA_HOME_CANDIDATE}"
    echo "Detected JAVA_HOME: ${JAVA_HOME}"
else
    echo "No java binary found on PATH — please ensure JDK 21 is installed"
    exit 1
fi

# Export JAVAFX_HOME for convenience
export JAVAFX_HOME="/opt/javafx-sdk"
echo "JAVAFX_HOME set to ${JAVAFX_HOME}"

# Create project directory structure if missing
mkdir -p "${PROJECT_DIR}/src/main/java/com/bankingapp/"{model,gui,controller,dao}
mkdir -p "${PROJECT_DIR}/src/main/resources/com/bankingapp/gui"

# Write start script (idempotent)
sudo tee "${START_SCRIPT}" > /dev/null <<'BASH'
#!/usr/bin/env bash
set -euo pipefail
PROJECT_DIR="${GITHUB_WORKSPACE:-/workspaces/banking-system}"
# Start Xvfb if no display
if [ -z "${DISPLAY:-}" ]; then
  echo "Starting virtual display (Xvfb :99)..."
  nohup Xvfb :99 -screen 0 1024x768x24 >/tmp/xvfb.log 2>&1 &
  export DISPLAY=:99
  sleep 0.5
fi

# Ensure we have detected java home at runtime
if command -v java >/dev/null 2>&1; then
  JAVA_BIN="$(readlink -f "$(command -v java)")"
  JAVA_HOME="$(dirname "$(dirname "${JAVA_BIN}"))"
  export JAVA_HOME
fi

export JAVAFX_HOME="/opt/javafx-sdk"

cd "${PROJECT_DIR}"

# Use mvn to build; include JavaFX module path at runtime (if running Java directly)
mvn -DskipTests package

# Determine port from env or default
PORT="${PORT:-8080}"

# Run headless BankingApp (main class determines headless/gui)
# Use classpath target/classes plus project's resources and any runtime jars present in target/lib
CLASSPATH="target/classes"
if [ -d "target/lib" ]; then
  CLASSPATH="${CLASSPATH}:target/lib/*"
fi

echo "Running BankingApp in headless mode..."
exec java -cp "${CLASSPATH}" com.bankingapp.BankingApp --headless
BASH

sudo chmod +x "${START_SCRIPT}"
echo "✅ Start script written to ${START_SCRIPT}"

# Write test script
sudo tee "${TEST_SCRIPT}" > /dev/null <<'BASH'
#!/usr/bin/env bash
set -euo pipefail
PROJECT_DIR="${GITHUB_WORKSPACE:-/workspaces/banking-system}"
cd "${PROJECT_DIR}"
mvn -q -DskipTests package && echo "✅ Build successful" || echo "❌ Build failed"
BASH

sudo chmod +x "${TEST_SCRIPT}"
echo "✅ Test script written to ${TEST_SCRIPT}"

# Friendly summary and usage tip
echo ""
echo "=== Setup complete ==="
echo "To build and start the application inside Codespaces, run:"
echo "  ${TEST_SCRIPT}      # quick build check"
echo "  ${START_SCRIPT}     # builds and starts headless server (opens Xvfb if needed)"
echo ""
echo "Notes:"
echo "- JAVAFX_HOME: /opt/javafx-sdk"
echo "- JAVA_HOME detected from runtime: ${JAVA_HOME}"
echo "- If your project uses a different JavaFX version update JAVAFX_VERSION at the top of this script."