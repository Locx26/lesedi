#!/usr/bin/env bash
set -euo pipefail

# run.sh — start SecureTrust Banking System (headless by default)
# Usage:
#   ./run.sh            # runs headless
#   ./run.sh --gui      # run GUI (if DISPLAY available)
#   ./run.sh --background  # start in background (nohup)

# Ensure we're in the repo root
cd "$(dirname "$0")"

# Build runtime classpath if missing
if [ ! -f target/classpath.txt ]; then
  echo "Building runtime classpath..."
  mvn dependency:build-classpath -DincludeScope=runtime -Dmdep.outputFile=target/classpath.txt
fi

CP=$(cat target/classpath.txt)

# Default JVM options; can be overridden by env var JAVA_OPTS
JAVA_OPTS="${JAVA_OPTS:--Dprism.order=sw}"

# Default args to app
if [ "$#" -eq 0 ]; then
  ARGS=("--headless")
else
  ARGS=("$@")
fi

# If first arg is --background, run in background and write pid
if [ "${ARGS[0]}" = "--background" ]; then
  # remove the background flag
  ARGS=("${ARGS[@]:1}")
  echo "Starting in background..."
  env -u CODESPACE_NAME nohup /usr/bin/env java $JAVA_OPTS -cp "target/classes:$CP" com.bankingapp.BankingApp "${ARGS[@]}" > server.log 2>&1 &
  echo $! > server.pid
  echo "Started (pid=$(cat server.pid)), logs -> server.log"
  exit 0
fi

# Unset CODESPACE_NAME so the application doesn't force GUI mode in Codespaces
env -u CODESPACE_NAME /usr/bin/env java $JAVA_OPTS -cp "target/classes:$CP" com.bankingapp.BankingApp "${ARGS[@]}"
