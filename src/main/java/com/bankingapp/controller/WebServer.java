package com.bankingapp.controller;

import com.bankingapp.dao.DatabaseConnection;
import com.bankingapp.model.*;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Professional Web Server for Banking System
 * Provides REST API and web interface for headless operation
 */
public class WebServer {

    private static final int DEFAULT_PORT = 8080;
    private static HttpServer server;
    private static BankingController controller;

    public static void start(int port) throws IOException {
        System.out.println("🚀 Starting SecureTrust Banking Web Server...");

        controller = new BankingController();

        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newFixedThreadPool(10));

        // Register API and web endpoints
        setupRoutes();

        server.start();
        System.out.println("✅ Web server started successfully on port " + port);
        System.out.println("🌐 Web Interface: http://localhost:" + port);
        System.out.println("🔗 API Base URL: http://localhost:" + port + "/api");
    }

    private static void setupRoutes() {
        // Web interface routes
        server.createContext("/", new DashboardHandler());
        server.createContext("/dashboard", new DashboardHandler());
        server.createContext("/customers", new CustomersHandler());
        server.createContext("/accounts", new AccountsHandler());
        server.createContext("/transactions", new TransactionsHandler());
        server.createContext("/reports", new ReportsHandler());

        // API routes
        server.createContext("/api/health", new HealthHandler());
        server.createContext("/api/customers", new CustomersApiHandler());
        server.createContext("/api/accounts", new AccountsApiHandler());
        server.createContext("/api/transactions", new TransactionsApiHandler());
        server.createContext("/api/reports", new ReportsApiHandler());
    }

    public static void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("🛑 Web server stopped");
        }
    }

    /* -------------------------
       Web page handlers
       ------------------------- */

    static class DashboardHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                var stats = controller.getSystemStatistics();

                String dashboard = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <title>SecureTrust Banking - Dashboard</title>
                        <style>
                            body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }
                            .header { background: #2c3e50; color: white; padding: 20px; border-radius: 10px; }
                            .stats { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; margin: 20px 0; }
                            .stat-card { background: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
                            .nav { background: white; padding: 15px; border-radius: 10px; margin: 20px 0; }
                            .nav a { margin-right: 15px; text-decoration: none; color: #3498db; }
                        </style>
                    </head>
                    <body>
                        <div class="header">
                            <h1>🏦 SecureTrust Banking System</h1>
                            <p>Enterprise Edition v2.0.0 - Web Dashboard</p>
                        </div>

                        <div class="nav">
                            <a href="/">Home</a>
                            <a href="/customers">Customers</a>
                            <a href="/accounts">Accounts</a>
                            <a href="/transactions">Transactions</a>
                            <a href="/reports">Reports</a>
                        </div>

                        <div class="stats">
                            <div class="stat-card">
                                <h3>👥 Total Customers</h3>
                                <p style="font-size: 24px; color: #2c3e50;">%d</p>
                            </div>
                            <div class="stat-card">
                                <h3>💰 Total Accounts</h3>
                                <p style="font-size: 24px; color: #2c3e50;">%d</p>
                            </div>
                            <div class="stat-card">
                                <h3>🏦 Total Assets</h3>
                                <p style="font-size: 24px; color: #27ae60;">BWP %s</p>
                            </div>
                        </div>
                    </body>
                    </html>
                    """.formatted(
                        stats.getTotalCustomers(),
                        stats.getTotalAccounts(),
                        String.format("%,.2f", stats.getTotalAssets())
                    );

                sendResponse(exchange, 200, dashboard, "text/html");
            } catch (Exception e) {
                sendResponse(exchange, 500, "{\"error\":\"Internal server error\"}", "application/json");
            }
        }
    }

    static class CustomersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String page = getWebPage("Customers Management");
            sendResponse(exchange, 200, page, "text/html");
        }
    }

    static class AccountsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String page = getWebPage("Accounts Management");
            sendResponse(exchange, 200, page, "text/html");
        }
    }

    static class TransactionsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String page = getWebPage("Transactions");
            sendResponse(exchange, 200, page, "text/html");
        }
    }

    static class ReportsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String page = getWebPage("Reports & Analytics");
            sendResponse(exchange, 200, page, "text/html");
        }
    }

    /* -------------------------
       API handlers
       ------------------------- */

    static class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, Object> health = new HashMap<>();
            health.put("status", "OK");
            health.put("service", "SecureTrust Banking System");
            health.put("version", "2.0.0");
            health.put("timestamp", System.currentTimeMillis());
            health.put("database", DatabaseConnection.testConnection() ? "CONNECTED" : "DISCONNECTED");

            String response = toJson(health);
            sendResponse(exchange, 200, response, "application/json");
        }
    }

    static class CustomersApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                Map<String, Object> response = new HashMap<>();
                response.put("customers", controller.getAllCustomers());
                response.put("count", controller.getAllCustomers().size());
                sendResponse(exchange, 200, toJson(response), "application/json");
            } else {
                sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}", "application/json");
            }
        }
    }

    static class AccountsApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                Map<String, Object> response = new HashMap<>();
                response.put("accounts", controller.getAllAccounts());
                response.put("count", controller.getAllAccounts().size());
                sendResponse(exchange, 200, toJson(response), "application/json");
            } else {
                sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}", "application/json");
            }
        }
    }

    static class TransactionsApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                // TODO: parse JSON and call controller methods to process transactions
                sendResponse(exchange, 200, "{\"status\":\"Transaction processed\"}", "application/json");
            } else {
                sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}", "application/json");
            }
        }
    }

    static class ReportsApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                String report = controller.generateFinancialReport();
                Map<String, Object> response = new HashMap<>();
                response.put("report", report);
                response.put("generated_at", System.currentTimeMillis());
                sendResponse(exchange, 200, toJson(response), "application/json");
            } else {
                sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}", "application/json");
            }
        }
    }

    /* -------------------------
       Utilities
       ------------------------- */

    private static String getWebPage(String title) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>%s - SecureTrust Banking</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }
                    .header { background: #2c3e50; color: white; padding: 20px; border-radius: 10px; }
                    .content { background: white; padding: 20px; border-radius: 10px; margin: 20px 0; }
                    .nav { background: white; padding: 15px; border-radius: 10px; margin: 20px 0; }
                    .nav a { margin-right: 15px; text-decoration: none; color: #3498db; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>🏦 SecureTrust Banking System</h1>
                    <p>%s</p>
                </div>

                <div class="nav">
                    <a href="/">Home</a>
                    <a href="/dashboard">Dashboard</a>
                    <a href="/customers">Customers</a>
                    <a href="/accounts">Accounts</a>
                    <a href="/transactions">Transactions</a>
                    <a href="/reports">Reports</a>
                </div>

                <div class="content">
                    <h2>%s</h2>
                    <p>This feature is available in the JavaFX graphical interface.</p>
                    <p>Please run the application with GUI mode for full functionality:</p>
                    <code>java -jar banking-system.jar --gui</code>
                </div>
            </body>
            </html>
            """.formatted(title, title, title);
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String response, String contentType) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static String toJson(Object obj) {
        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            StringBuilder json = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (!first) json.append(",");
                json.append("\"").append(entry.getKey()).append("\":");
                json.append(toJsonValue(entry.getValue()));
                first = false;
            }
            json.append("}");
            return json.toString();
        }
        return "\"" + escapeJson(String.valueOf(obj)) + "\"";
    }

    private static String toJsonValue(Object value) {
        if (value == null) return "null";
        if (value instanceof String) return "\"" + escapeJson((String) value) + "\"";
        if (value instanceof Number) return value.toString();
        if (value instanceof Boolean) return value.toString();
        return "\"" + escapeJson(value.toString()) + "\"";
    }

    private static String escapeJson(String str) {
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}