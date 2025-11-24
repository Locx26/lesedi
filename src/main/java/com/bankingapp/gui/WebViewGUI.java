package com.bankingapp.gui;

import com.bankingapp.controller.WebServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * WebView GUI that hosts the headless/web interface inside a JavaFX WebView.
 * Auto-start variation: automatically attempts to start the embedded WebServer
 * if the health check fails on startup (no manual button).
 */
public class WebViewGUI extends Application {
    private WebView webView;
    private Label statusLabel;
    private ExecutorService bgExecutor;
    private volatile boolean embeddedServerStartedByThisApp = false;
    private final int HEALTH_CHECK_TIMEOUT_MS = 1500;
    private final Duration SERVER_START_POLL_INTERVAL = Duration.ofSeconds(1);
    private final int DEFAULT_PORT = 8080;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SecureTrust Banking System - Web Interface");

        bgExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "webview-bg");
            t.setDaemon(true);
            return t;
        });

        webView = new WebView();
        webView.setContextMenuEnabled(true);

        statusLabel = new Label("Initializing and checking server...");

        HBox statusBar = new HBox(10, statusLabel);
        statusBar.setPadding(new Insets(8));
        statusBar.setAlignment(Pos.CENTER_LEFT);

        BorderPane root = new BorderPane();
        root.setCenter(webView);
        root.setBottom(statusBar);

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Ensure embedded server is stopped on exit if we started it
        primaryStage.setOnCloseRequest(evt -> {
            bgExecutor.shutdownNow();
            if (embeddedServerStartedByThisApp) {
                try {
                    WebServer.stop();
                } catch (Throwable ignored) {
                }
            }
        });

        // Start the auto-start flow: check health; if not healthy, auto-start embedded server
        autoStartAndLoad();
    }

    private String getTargetUrl() {
        String configured = System.getProperty("webview.url");
        if (configured != null && !configured.isBlank()) return configured;
        String env = System.getenv("WEBVIEW_URL");
        if (env != null && !env.isBlank()) return env;
        return "http://localhost:" + DEFAULT_PORT;
    }

    private void autoStartAndLoad() {
        String baseUrl = getTargetUrl();
        String healthUrl = baseUrl.endsWith("/") ? baseUrl + "api/health" : baseUrl + "/api/health";

        statusLabel.setText("Checking server health at " + healthUrl + " ...");

        Task<Boolean> healthCheck = new Task<>() {
            @Override
            protected Boolean call() {
                return isServerHealthy(healthUrl);
            }
        };

        healthCheck.setOnSucceeded(evt -> {
            boolean healthy = healthCheck.getValue();
            if (healthy) {
                statusLabel.setText("Server healthy — loading " + baseUrl);
                loadUrl(baseUrl);
            } else {
                statusLabel.setText("Server not reachable — attempting to start embedded server...");
                startEmbeddedServerAsync(DEFAULT_PORT);
            }
        });

        healthCheck.setOnFailed(evt -> {
            statusLabel.setText("Health check failed: " + healthCheck.getException().getMessage());
            // still attempt to start server
            startEmbeddedServerAsync(DEFAULT_PORT);
        });

        bgExecutor.submit(healthCheck);
    }

    private boolean isServerHealthy(String healthUrl) {
        try {
            URL url = new URL(healthUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(HEALTH_CHECK_TIMEOUT_MS);
            con.setReadTimeout(HEALTH_CHECK_TIMEOUT_MS);
            con.setRequestMethod("GET");
            int code = con.getResponseCode();
            con.disconnect();
            return code >= 200 && code < 300;
        } catch (IOException e) {
            return false;
        }
    }

    private void loadUrl(String baseUrl) {
        Platform.runLater(() -> webView.getEngine().load(baseUrl));
    }

    private void startEmbeddedServerAsync(int port) {
        statusLabel.setText("Starting embedded server on port " + port + " ...");

        Task<Void> startTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    // Start server in this process (non-blocking call assumed)
                    WebServer.start(port);
                    embeddedServerStartedByThisApp = true;

                    // Poll health until up or timeout (15s)
                    String healthUrl = (getTargetUrl().endsWith("/") ? getTargetUrl() + "api/health" : getTargetUrl() + "/api/health");
                    long start = System.currentTimeMillis();
                    long timeoutMs = 15_000;
                    while (System.currentTimeMillis() - start < timeoutMs) {
                        if (isServerHealthy(healthUrl)) {
                            updateMessage("Server started and healthy — loading UI...");
                            loadUrl(getTargetUrl());
                            return null;
                        }
                        Thread.sleep(SERVER_START_POLL_INTERVAL.toMillis());
                    }
                    throw new IllegalStateException("Server did not become healthy within timeout");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        };

        startTask.messageProperty().addListener((obs, oldMsg, newMsg) -> Platform.runLater(() -> statusLabel.setText(newMsg)));

        startTask.setOnSucceeded(evt -> Platform.runLater(() -> statusLabel.setText("Embedded server is running. Loading web interface...")));

        startTask.setOnFailed(evt -> {
            Throwable ex = startTask.getException();
            Platform.runLater(() -> {
                statusLabel.setText("Failed to start embedded server: " + (ex != null ? ex.getMessage() : "unknown"));
                showAlert("Server Start Failed", ex != null ? ex.getMessage() : "Unknown error");
            });
        });

        bgExecutor.submit(startTask);
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle(title);
            a.setHeaderText(null);
            a.setContentText(message);
            a.showAndWait();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}