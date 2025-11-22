#!/bin/bash

echo "=== Setting up GitHub Codespaces for JavaFX Banking System ==="

# Install dependencies
sudo apt update
sudo apt install -y xvfb libxext6 libxrender1 libxtst6 libxi6 libgtk-3-0

# Download and install JavaFX
wget -q https://download2.gluonhq.com/openjfx/17.0.8/openjfx-17.0.8_linux-x64_bin-sdk.zip
unzip -q openjfx-17.0.8_linux-x64_bin-sdk.zip -d /opt/
mv /opt/javafx-sdk-17.0.8 /opt/javafx-sdk
rm openjfx-17.0.8_linux-x64_bin-sdk.zip

# Set environment variables
echo "export JAVAFX_HOME=/opt/javafx-sdk" >> ~/.bashrc
echo "export JAVA_HOME=/usr/lib/jvm/ms-openjdk-17" >> ~/.bashrc
echo "export PATH=\$JAVA_HOME/bin:\$PATH" >> ~/.bashrc

# Create startup script for JavaFX with virtual display
cat > /usr/local/bin/start-banking-app << 'EOF'
#!/bin/bash
# Start virtual display for JavaFX
Xvfb :99 -screen 0 1024x768x24 &
export DISPLAY=:99
export JAVAFX_PATH="/opt/javafx-sdk/lib"

# Run the banking application
cd /workspaces/banking-system
mvn clean compile exec:java -Dexec.mainClass="com.bankingapp.BankingApp" \
    -Dexec.args="--headless" \
    -Dprism.verbose=true \
    -Dprism.order=sw
EOF

chmod +x /usr/local/bin/start-banking-app

echo "=== Codespaces setup complete ==="
echo "JavaFX SDK installed to: /opt/javafx-sdk"
echo "Run 'start-banking-app' to launch the application"