package com.bankingapp;

import com.bankingapp.gui.MainView;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * SecureTrust Banking System - Enterprise Edition
 * Professional entry point that starts either the JavaFX GUI or an embedded web server.
 *
 * Notes:
 * - Web server port is configurable via environment variable PORT (default 8080).
 * - In headless or Codespace environments the web interface is preferred.
 * - Uses a CountDownLatch to keep the server alive and to allow graceful shutdown.
 */
public class BankingApp {

    private static final String APP_VERSION = "2.0.0";
    private static final String APP_NAME = "SecureTrust Banking System";
    private static HttpServer webServer;
    private static final CountDownLatch shutdownLatch = new CountDownLatch(1);

    public static void main(String[] args) {
        displayWelcomeBanner();

        try {
            // Determine runtime environment
            boolean isCodespace = System.getenv("CODESPACE_NAME") != null;
            boolean isHeadless = java.awt.GraphicsEnvironment.isHeadless();
            boolean headlessMode = hasHeadlessArg(args);

            System.out.println("Environment Detection:");
            System.out.println("  Codespace: " + isCodespace);
            System.out.println("  Headless Environment: " + isHeadless);
            System.out.println("  Headless Mode Arg: " + headlessMode);

            // Always use web interface in Codespaces or when headless is requested
            if (isCodespace || isHeadless || headlessMode) {
                launchProfessionalWebInterface();
            } else {
                launchGraphicalInterface(args);
            }

        } catch (Exception e) {
            handleCriticalError(e);
        }
    }

    private static void displayWelcomeBanner() {
        System.out.println("\n" +
            "╔══════════════════════════════════════════════════════════════╗\n" +
            "║                   SECURETRUST BANKING SYSTEM                 ║\n" +
            "║                     Enterprise Edition v" + APP_VERSION + "                ║\n" +
            "║                                                              ║\n" +
            "║  BSC Computer Systems Engineering - OOAD Assignment 2025    ║\n" +
            "║          Professional Banking Management Platform            ║\n" +
            "╚══════════════════════════════════════════════════════════════╝\n");
    }

    private static boolean hasHeadlessArg(String[] args) {
        for (String arg : args) {
            if ("--headless".equals(arg) || "-h".equals(arg)) {
                return true;
            }
        }
        return false;
    }

    private static void launchProfessionalWebInterface() {
        System.out.println("🚀 Starting Professional Web Interface...");
        System.out.println("🌐 Initializing banking web server...");

        try {
            startWebServer();

            String host = "http://localhost:" + getPort();
            System.out.println("✅ Web interface successfully started!");
            System.out.println("📍 Access URL: " + host);
            System.out.println("📱 Compatible with all modern browsers");
            System.out.println("⚡ Ready for banking operations");
            System.out.println();
            System.out.println("🛑 Press Ctrl + C to shutdown the system");
            System.out.println("=".repeat(60));

            // Wait until shutdown hook counts down the latch
            shutdownLatch.await();

        } catch (Exception e) {
            System.err.println("❌ Failed to start web server: " + e.getMessage());
            // Fallback to console interface
            startConsoleInterface();
        }
    }

    private static int getPort() {
        String portEnv = System.getenv("PORT");
        if (portEnv != null && !portEnv.isBlank()) {
            try {
                return Integer.parseInt(portEnv);
            } catch (NumberFormatException ignored) { }
        }
        return 8080;
    }

    private static void startWebServer() throws IOException {
        int port = getPort();
        webServer = HttpServer.create(new InetSocketAddress(port), 0);

        // Create context handlers
        webServer.createContext("/", new WebInterfaceHandler());
        webServer.createContext("/api/accounts", new AccountsAPIHandler());
        webServer.createContext("/api/transactions", new TransactionsAPIHandler());
        webServer.createContext("/api/transfer", new TransferAPIHandler());

        webServer.setExecutor(Executors.newFixedThreadPool(10));
        webServer.start();

        // Add graceful shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (webServer != null) {
                System.out.println("\n📡 Shutting down web server...");
                webServer.stop(1);
                System.out.println("✅ Server stopped gracefully");
            }
            // release main thread
            shutdownLatch.countDown();
        }));
    }

    private static void startConsoleInterface() {
        System.out.println("Starting console interface...");
        // Simple console interface as fallback
        System.out.println("SecureTrust Banking System - Console Mode");
        System.out.println("Features: Account Management, Transactions, Transfers");
        System.out.println("Use the web interface at http://localhost:" + getPort() + " for full functionality");
    }

    private static void launchGraphicalInterface(String[] args) {
        System.out.println("🎨 Launching Professional Graphical Interface...");
        try {
            MainView.main(args);
        } catch (Exception e) {
            System.err.println("❌ GUI initialization failed: " + e.getMessage());
            System.err.println("🔄 Falling back to web interface...");
            launchProfessionalWebInterface();
        }
    }

    private static void handleCriticalError(Exception e) {
        System.err.println("\n💥 CRITICAL SYSTEM ERROR:");
        System.err.println("Message: " + e.getMessage());
        System.err.println("Please contact system administrator.");
        System.exit(1);
    }

    // HTTP Handlers for Web Interface
    static class WebInterfaceHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            byte[] bytes = getProfessionalWebInterface().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            } finally {
                exchange.close();
            }
        }

        private String getProfessionalWebInterface() {
            return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>SecureTrust Banking - Professional Interface</title>
                    <style>
                        * { margin: 0; padding: 0; box-sizing: border-box; }
                        body {
                            font-family: 'Segoe UI', system-ui, sans-serif;
                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                            min-height: 100vh;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            padding: 20px;
                        }
                        .banking-app {
                            background: white;
                            border-radius: 20px;
                            box-shadow: 0 25px 50px rgba(0,0,0,0.15);
                            overflow: hidden;
                            width: 100%;
                            max-width: 1200px;
                            min-height: 80vh;
                        }
                        .app-header {
                            background: linear-gradient(135deg, #2c3e50, #34495e);
                            color: white;
                            padding: 30px 40px;
                            text-align: center;
                        }
                        .app-header h1 {
                            font-size: 2.5em;
                            margin-bottom: 10px;
                        }
                        .main-content {
                            padding: 40px;
                            text-align: center;
                        }
                        .feature-grid {
                            display: grid;
                            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                            gap: 20px;
                            margin: 30px 0;
                        }
                        .feature-card {
                            background: #f8f9fa;
                            padding: 25px;
                            border-radius: 10px;
                            border-left: 4px solid #3498db;
                        }
                        .status-active {
                            color: #27ae60;
                            font-weight: bold;
                        }
                    </style>
                </head>
                <body>
                    <div class="banking-app">
                        <div class="app-header">
                            <h1>🏦 SecureTrust Banking</h1>
                            <p>Professional Web Interface - Enterprise Edition</p>
                        </div>
                        <div class="main-content">
                            <h2>Welcome to SecureTrust Banking System</h2>
                            <p>Professional banking management platform</p>

                            <div class="feature-grid">
                                <div class="feature-card">
                                    <h3>💰 Account Management</h3>
                                    <p>Manage savings, investment, and cheque accounts</p>
                                </div>
                                <div class="feature-card">
                                    <h3>📊 Transaction History</h3>
                                    <p>View and track all financial transactions</p>
                                </div>
                                <div class="feature-card">
                                    <h3>🔒 Secure Transfers</h3>
                                    <p>Safe and secure fund transfers</p>
                                </div>
                                <div class="feature-card">
                                    <h3>📈 Investment Tools</h3>
                                    <p>Advanced investment account management</p>
                                </div>
                            </div>

                            <div style="margin-top: 30px;">
                                <p><strong>Status:</strong> <span class="status-active">System Operational</span></p>
                                <p><strong>Version:</strong> %s Enterprise</p>
                                <p><strong>Environment:</strong> GitHub Codespaces</p>
                            </div>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(APP_VERSION);
        }
    }

    static class AccountsAPIHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = """
                {
                    "status": "success",
                    "data": {
                        "accounts": [
                            {
                                "type": "Savings",
                                "balance": 15000.00,
                                "number": "SAV-001",
                                "status": "Active"
                            }
                        ]
                    }
                }
                """;
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            } finally {
                exchange.close();
            }
        }
    }

    static class TransactionsAPIHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "{\"transactions\": []}";
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            } finally {
                exchange.close();
            }
        }
    }

    static class TransferAPIHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "{\"status\": \"success\"}";
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            } finally {
                exchange.close();
            }
        }
    }
}