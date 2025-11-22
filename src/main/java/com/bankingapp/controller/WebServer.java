package com.bankingapp.controller;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * Professional Web Server for Headless Mode
 * Provides web interface when running in GitHub Codespaces
 */
public class WebServer {
    private static HttpServer server;
   
    public static void start(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
       
        // Create context for different endpoints
        server.createContext("/", new DashboardHandler());
        server.createContext("/customers", new CustomersHandler());
        server.createContext("/accounts", new AccountsHandler());
        server.createContext("/transactions", new TransactionsHandler());
        server.createContext("/reports", new ReportsHandler());
        server.createContext("/api/health", new HealthHandler());
       
        server.setExecutor(null); // creates a default executor
        server.start();
       
        System.out.println("🔗 Web Server started on port " + port);
        System.out.println("🌐 Access the application at: http://localhost:" + port);
    }
   
    public static void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("Web Server stopped");
        }
    }
   
    static class DashboardHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>SecureTrust Banking System</title>
                    <style>
                        * { margin: 0; padding: 0; box-sizing: border-box; }
                        body {
                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                            min-height: 100vh;
                            color: #333;
                        }
                        .container {
                            max-width: 1200px;
                            margin: 0 auto;
                            padding: 20px;
                        }
                        .header {
                            background: rgba(255,255,255,0.95);
                            padding: 30px;
                            border-radius: 15px;
                            margin-bottom: 30px;
                            box-shadow: 0 8px 32px rgba(0,0,0,0.1);
                            backdrop-filter: blur(10px);
                        }
                        .header h1 {
                            color: #2c3e50;
                            font-size: 2.5em;
                            margin-bottom: 10px;
                        }
                        .header p {
                            color: #7f8c8d;
                            font-size: 1.1em;
                        }
                        .stats-grid {
                            display: grid;
                            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                            gap: 20px;
                            margin-bottom: 30px;
                        }
                        .stat-card {
                            background: rgba(255,255,255,0.95);
                            padding: 25px;
                            border-radius: 12px;
                            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                            text-align: center;
                            transition: transform 0.3s ease;
                        }
                        .stat-card:hover {
                            transform: translateY(-5px);
                        }
                        .stat-card h3 {
                            color: #7f8c8d;
                            font-size: 0.9em;
                            margin-bottom: 10px;
                            text-transform: uppercase;
                            letter-spacing: 1px;
                        }
                        .stat-card .value {
                            color: #2c3e50;
                            font-size: 2.2em;
                            font-weight: bold;
                            margin-bottom: 5px;
                        }
                        .nav-grid {
                            display: grid;
                            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
                            gap: 20px;
                        }
                        .nav-card {
                            background: rgba(255,255,255,0.95);
                            padding: 30px;
                            border-radius: 12px;
                            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                            text-decoration: none;
                            color: inherit;
                            transition: all 0.3s ease;
                            border-left: 4px solid #3498db;
                        }
                        .nav-card:hover {
                            transform: translateY(-3px);
                            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
                            border-left-color: #2980b9;
                        }
                        .nav-card h3 {
                            color: #2c3e50;
                            font-size: 1.4em;
                            margin-bottom: 10px;
                            display: flex;
                            align-items: center;
                            gap: 10px;
                        }
                        .nav-card p {
                            color: #7f8c8d;
                            line-height: 1.6;
                        }
                        .system-info {
                            background: rgba(255,255,255,0.95);
                            padding: 20px;
                            border-radius: 12px;
                            margin-top: 30px;
                            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                        }
                        .system-info h3 {
                            color: #2c3e50;
                            margin-bottom: 15px;
                        }
                        .info-grid {
                            display: grid;
                            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                            gap: 15px;
                        }
                        .info-item {
                            padding: 10px;
                            background: #f8f9fa;
                            border-radius: 6px;
                        }
                        .info-label {
                            font-weight: bold;
                            color: #7f8c8d;
                            font-size: 0.9em;
                        }
                        .info-value {
                            color: #2c3e50;
                            font-weight: 500;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>🏦 SecureTrust Banking System</h1>
                            <p>Enterprise Financial Management Platform - Running in Headless Mode</p>
                        </div>
                       
                        <div class="stats-grid">
                            <div class="stat-card">
                                <h3>Total Customers</h3>
                                <div class="value">1,247</div>
                                <p>Active accounts</p>
                            </div>
                            <div class="stat-card">
                                <h3>Total Assets</h3>
                                <div class="value">BWP 148.7M</div>
                                <p>Under management</p>
                            </div>
                            <div class="stat-card">
                                <h3>Active Accounts</h3>
                                <div class="value">2,843</div>
                                <p>Across all types</p>
                            </div>
                            <div class="stat-card">
                                <h3>Monthly Interest</h3>
                                <div class="value">BWP 742K</div>
                                <p>Paid to customers</p>
                            </div>
                        </div>
                       
                        <div class="nav-grid">
                            <a href="/customers" class="nav-card">
                                <h3>👥 Customer Management</h3>
                                <p>Manage customer profiles, view account relationships, and handle customer data operations.</p>
                            </a>
                            <a href="/accounts" class="nav-card">
                                <h3>💳 Account Management</h3>
                                <p>Open new accounts, manage existing accounts, and handle account operations and settings.</p>
                            </a>
                            <a href="/transactions" class="nav-card">
                                <h3>💸 Transaction Processing</h3>
                                <p>Process deposits, withdrawals, and view comprehensive transaction history and records.</p>
                            </a>
                            <a href="/reports" class="nav-card">
                                <h3>📊 Reports & Analytics</h3>
                                <p>Generate financial reports, view business analytics, and export data for analysis.</p>
                            </a>
                        </div>
                       
                        <div class="system-info">
                            <h3>System Information</h3>
                            <div class="info-grid">
                                <div class="info-item">
                                    <div class="info-label">Status</div>
                                    <div class="info-value" style="color: #27ae60;">● Operational</div>
                                </div>
                                <div class="info-item">
                                    <div class="info-label">Environment</div>
                                    <div class="info-value">GitHub Codespaces</div>
                                </div>
                                <div class="info-item">
                                    <div class="info-label">Java Version</div>
                                    <div class="info-value">OpenJDK 17</div>
                                </div>
                                <div class="info-item">
                                    <div class="info-label">Database</div>
                                    <div class="info-value">H2 (In-Memory)</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </body>
                </html>
                """;
           
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes().length);
           
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
   
    static class CustomersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Customer Management - SecureTrust Banking</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }
                        .container { max-width: 1000px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                        h1 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }
                        .customer-table { width: 100%; border-collapse: collapse; margin-top: 20px; }
                        .customer-table th, .customer-table td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
                        .customer-table th { background: #3498db; color: white; }
                        .customer-table tr:hover { background: #f5f5f5; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>👥 Customer Management</h1>
                        <p>Manage your banking customers and their account relationships.</p>
                       
                        <table class="customer-table">
                            <thead>
                                <tr>
                                    <th>Customer ID</th>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Phone</th>
                                    <th>Accounts</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>CUST001</td>
                                    <td>John Doe</td>
                                    <td>john.doe@email.com</td>
                                    <td>71123456</td>
                                    <td>2</td>
                                    <td>Active</td>
                                </tr>
                                <tr>
                                    <td>CUST002</td>
                                    <td>Jane Smith</td>
                                    <td>jane.smith@email.com</td>
                                    <td>72123456</td>
                                    <td>1</td>
                                    <td>Active</td>
                                </tr>
                                <tr>
                                    <td>CUST003</td>
                                    <td>Bob Johnson</td>
                                    <td>bob.johnson@email.com</td>
                                    <td>73123456</td>
                                    <td>3</td>
                                    <td>Active</td>
                                </tr>
                            </tbody>
                        </table>
                       
                        <div style="margin-top: 30px; text-align: center;">
                            <a href="/" style="background: #3498db; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">← Back to Dashboard</a>
                        </div>
                    </div>
                </body>
                </html>
                """;
           
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes().length);
           
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
   
    static class AccountsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Account Management - SecureTrust Banking</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }
                        .container { max-width: 1000px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                        h1 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }
                        .account-table { width: 100%; border-collapse: collapse; margin-top: 20px; }
                        .account-table th, .account-table td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
                        .account-table th { background: #27ae60; color: white; }
                        .savings { background: #e8f6f3; }
                        .investment { background: #fef9e7; }
                        .cheque { background: #f4ecf7; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>💳 Account Management</h1>
                        <p>Manage customer accounts and financial operations.</p>
                       
                        <table class="account-table">
                            <thead>
                                <tr>
                                    <th>Account Number</th>
                                    <th>Type</th>
                                    <th>Customer</th>
                                    <th>Balance</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr class="savings">
                                    <td>SAV001</td>
                                    <td>Savings</td>
                                    <td>John Doe</td>
                                    <td>BWP 1,500.00</td>
                                    <td>Active</td>
                                </tr>
                                <tr class="investment">
                                    <td>INV001</td>
                                    <td>Investment</td>
                                    <td>John Doe</td>
                                    <td>BWP 5,000.00</td>
                                    <td>Active</td>
                                </tr>
                                <tr class="cheque">
                                    <td>CHQ001</td>
                                    <td>Cheque</td>
                                    <td>Jane Smith</td>
                                    <td>BWP 2,500.00</td>
                                    <td>Active</td>
                                </tr>
                            </tbody>
                        </table>
                       
                        <div style="margin-top: 30px; text-align: center;">
                            <a href="/" style="background: #3498db; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">← Back to Dashboard</a>
                        </div>
                    </div>
                </body>
                </html>
                """;
           
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes().length);
           
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
   
    static class TransactionsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Transaction Processing - SecureTrust Banking</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }
                        .container { max-width: 1000px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                        h1 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }
                        .transaction-table { width: 100%; border-collapse: collapse; margin-top: 20px; }
                        .transaction-table th, .transaction-table td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
                        .transaction-table th { background: #e74c3c; color: white; }
                        .deposit { color: #27ae60; font-weight: bold; }
                        .withdrawal { color: #e74c3c; font-weight: bold; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>💸 Transaction Processing</h1>
                        <p>View and manage financial transactions.</p>
                       
                        <table class="transaction-table">
                            <thead>
                                <tr>
                                    <th>Date & Time</th>
                                    <th>Account</th>
                                    <th>Type</th>
                                    <th>Amount</th>
                                    <th>Balance</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>2024-03-20 14:30</td>
                                    <td>SAV001</td>
                                    <td class="deposit">Deposit</td>
                                    <td class="deposit">BWP 500.00</td>
                                    <td>BWP 1,500.00</td>
                                </tr>
                                <tr>
                                    <td>2024-03-19 10:15</td>
                                    <td>INV001</td>
                                    <td class="withdrawal">Withdrawal</td>
                                    <td class="withdrawal">BWP 1,000.00</td>
                                    <td>BWP 5,000.00</td>
                                </tr>
                                <tr>
                                    <td>2024-03-18 16:45</td>
                                    <td>CHQ001</td>
                                    <td class="deposit">Deposit</td>
                                    <td class="deposit">BWP 2,500.00</td>
                                    <td>BWP 2,500.00</td>
                                </tr>
                            </tbody>
                        </table>
                       
                        <div style="margin-top: 30px; text-align: center;">
                            <a href="/" style="background: #3498db; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">← Back to Dashboard</a>
                        </div>
                    </div>
                </body>
                </html>
                """;
           
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes().length);
           
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
   
    static class ReportsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Reports & Analytics - SecureTrust Banking</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }
                        .container { max-width: 1000px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                        h1 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }
                        .report-section { margin: 20px 0; padding: 20px; background: #f8f9fa; border-radius: 8px; }
                        .report-section h3 { color: #2c3e50; margin-bottom: 15px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>📊 Reports & Analytics</h1>
                        <p>Financial reports and business intelligence dashboards.</p>
                       
                        <div class="report-section">
                            <h3>Financial Performance Summary</h3>
                            <p><strong>Total Assets:</strong> BWP 148,750,000</p>
                            <p><strong>Active Customers:</strong> 1,247</p>
                            <p><strong>Monthly Interest Paid:</strong> BWP 742,150</p>
                        </div>
                       
                        <div class="report-section">
                            <h3>Account Distribution</h3>
                            <p><strong>Savings Accounts:</strong> 842 accounts</p>
                            <p><strong>Investment Accounts:</strong> 356 accounts</p>
                            <p><strong>Cheque Accounts:</strong> 645 accounts</p>
                        </div>
                       
                        <div class="report-section">
                            <h3>Recent Activity</h3>
                            <p><strong>Transactions (30 days):</strong> 8,450</p>
                            <p><strong>New Accounts (30 days):</strong> 145</p>
                            <p><strong>Customer Growth:</strong> +3.2%</p>
                        </div>
                       
                        <div style="margin-top: 30px; text-align: center;">
                            <a href="/" style="background: #3498db; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">← Back to Dashboard</a>
                        </div>
                    </div>
                </body>
                </html>
                """;
           
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes().length);
           
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
   
    static class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = """
                {
                    "status": "healthy",
                    "timestamp": "%s",
                    "service": "SecureTrust Banking System",
                    "version": "2.0.0",
                    "environment": "GitHub Codespaces",
                    "database": "connected"
                }
                """.formatted(java.time.Instant.now().toString());
           
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
           
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}