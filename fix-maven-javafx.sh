#!/bin/bash
# Maven JavaFX Fixer for VS Code

echo "=== Fixing Maven JavaFX in VS Code ==="

# Create Maven wrapper if needed
if [ ! -f "mvnw" ]; then
    cat > mvnw << 'EOF'
#!/bin/sh
# ----------------------------------------------------------------------------
# Maven Start Up Batch script
#
# Required ENV vars:
# JAVA_HOME - location of a JDK home dir
# ----------------------------------------------------------------------------

if [ -z "$JAVA_HOME" ] ; then
  echo "Error: JAVA_HOME not found in your environment."
  echo "Please set the JAVA_HOME variable in your environment."
  exit 1
fi

# Use Java from JAVA_HOME
JAVA_CMD="$JAVA_HOME/bin/java"

# Execute Maven
exec "$JAVA_CMD" $MAVEN_OPTS -classpath "$MAVEN_HOME/boot/plexus-classworlds-2.6.0.jar" \
  "-Dclassworlds.conf=$MAVEN_HOME/bin/m2.conf" \
  "-Dmaven.home=$MAVEN_HOME" \
  "-Dlibrary.jansi.path=$MAVEN_HOME/lib/jansi-native" \
  "-Dmaven.multiModuleProjectDirectory=$MAVEN_PROJECTBASEDIR" \
  org.codehaus.plexus.classworlds.launcher.Launcher "$@"
EOF
    chmod +x mvnw
    echo "Created Maven wrapper"
fi

# Create a simple test to verify JavaFX
cat > src/main/java/com/bankingapp/JavaFXTest.java << 'EOF'
package com.bankingapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class JavaFXTest extends Application {
    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
EOF

echo "Created JavaFX test file"

# Create environment setup script
cat > setup-environment.sh << 'EOF'
#!/bin/bash
# Setup environment for Maven + JavaFX

export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java))))
export PATH=$JAVA_HOME/bin:$PATH

# Try to find JavaFX
if [ -z "$JAVAFX_HOME" ]; then
    if [ -d "./javafx-sdk-17.0.8" ]; then
        export JAVAFX_HOME="./javafx-sdk-17.0.8"
    elif [ -d "./javafx" ]; then
        export JAVAFX_HOME="./javafx"
    elif [ -d "/usr/share/openjfx" ]; then
        export JAVAFX_HOME="/usr/share/openjfx"
    fi
fi

if [ -n "$JAVAFX_HOME" ]; then
    echo "JavaFX found at: $JAVAFX_HOME"
    export MAVEN_OPTS="--module-path $JAVAFX_HOME/lib --add-modules javafx.controls,javafx.fxml"
else
    echo "Warning: JavaFX not found. Please set JAVAFX_HOME environment variable."
fi

echo "Environment setup complete"
EOF

chmod +x setup-environment.sh

echo ""
echo "=== Fixes Applied ==="
echo "✅ Maven wrapper created"
echo "✅ JavaFX test file created"
echo "✅ Environment setup script created"
echo ""
echo "Next steps:"
echo "1. Run: ./setup-environment.sh"
echo "2. Run: mvn clean compile"
echo "3. Run: mvn javafx:run"
echo ""
echo "If JavaFX is still missing, download from:"
echo "https://gluonhq.com/products/javafx/"
echo "And extract to ./javafx-sdk-17.0.8/"