package com.bankingapp.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MainView extends Application {

    private static final Path READY_FILE = Paths.get(System.getProperty("user.dir"), "gui.ready");

    @Override
    public void start(Stage stage) {
        try {
            // Force software rendering in virtual displays
            System.setProperty("prism.order", "sw");

            URL fxml = getClass().getResource("/com/bankingapp/gui/main.fxml");
            if (fxml == null) {
                System.err.println("FATAL: main.fxml not found at /com/bankingapp/gui/main.fxml");
                throw new IllegalStateException("Missing FXML resource: /com/bankingapp/gui/main.fxml");
            }

            Parent root = FXMLLoader.load(fxml);
            Scene scene = new Scene(root);

            URL css = getClass().getResource("/com/bankingapp/gui/styles.css");
            if (css != null) {
                scene.getStylesheets().add(css.toExternalForm());
            } else {
                System.out.println("INFO: styles.css not found; continuing without stylesheet.");
            }

            stage.setTitle("SecureTrust Banking System • Enterprise Edition v2.0");
            stage.setWidth(1400);
            stage.setHeight(900);
            stage.setMinWidth(1000);
            stage.setMinHeight(700);
            stage.centerOnScreen();
            stage.setScene(scene);
            stage.show();

            // Write readiness marker
            try {
                Files.writeString(READY_FILE, "ready\n", StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                System.out.println("WROTE readiness marker: " + READY_FILE.toAbsolutePath());
            } catch (Exception ex) {
                System.err.println("Warning: failed to write readiness marker: " + ex.getMessage());
            }

            System.out.println("SECURETRUST GUI LAUNCHED SUCCESSFULLY");

        } catch (Throwable t) {
            System.err.println("ERROR: Failed to initialize GUI: " + t.getClass().getName() + ": " + t.getMessage());
            t.printStackTrace();
            Platform.exit();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
