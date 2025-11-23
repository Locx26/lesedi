FROM mcr.microsoft.com/devcontainers/java:0-21

# Install packages needed for virtual display and JavaFX
RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
    openjfx \
    xvfb \
    x11vnc \
    websockify \
    novnc \
    maven \
    wget \
    unzip \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# Copy system OpenJFX libs to /opt for compatibility with scripts
RUN mkdir -p /opt/javafx-sdk/lib && cp -r /usr/share/openjfx/lib/* /opt/javafx-sdk/lib/ || true

WORKDIR /workspace
ENV DISPLAY=:99
RUN chown -R 1000:1000 /workspace || true
USER vscode
