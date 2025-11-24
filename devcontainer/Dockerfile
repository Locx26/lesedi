# Devcontainer base with JDK 21 + Maven
FROM mcr.microsoft.com/devcontainers/java:0-21

# Install utilities needed for virtual display + VNC/noVNC and JavaFX (openjfx)
# Use root to install packages, keep image small and safe
USER root
ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update \
 && apt-get install -y --no-install-recommends \
    xvfb \
    x11vnc \
    websockify \
    novnc \
    wget \
    unzip \
    ca-certificates \
    openjfx \
    maven \
 && apt-get clean \
 && rm -rf /var/lib/apt/lists/*

# Try to put JavaFX libs in /opt/javafx-sdk/lib for scripts that expect that path.
# On Debian/Ubuntu openjfx libs typically live under /usr/share/openjfx/lib
RUN mkdir -p /opt/javafx-sdk/lib \
    && if [ -d /usr/share/openjfx/lib ]; then cp -r /usr/share/openjfx/lib/* /opt/javafx-sdk/lib/ || true; fi

# Create workspace folder and set ownership for the default non-root user
WORKDIR /workspace
RUN mkdir -p /workspace
RUN chown -R 1000:1000 /workspace || true

# Expose common ports (web UI, VNC, noVNC)
EXPOSE 8080 5900 6080

# Switch to the non-root VS Code devcontainer user (keeps container secure)
USER vscode

# Keep a default display env for GUI tools (can be overridden at runtime)
ENV DISPLAY=:99

# Convenience: print available Java/JavaFX info when container starts (user can override CMD)
CMD ["/bin/bash", "-lc", "java -version || true; echo JAVA_HOME=$JAVA_HOME; ls -la /opt/javafx-sdk/lib || true; bash"]