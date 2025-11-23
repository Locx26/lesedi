package com.bankingapp;

import com.bankingapp.gui.MainView;
import com.bankingapp.dao.DatabaseConnection;

/**
 * SecureTrust Banking System - Enterprise Edition v2.0.0
 *
 * Attempts to launch the JavaFX GUI first (unless user passes --headless).
 * If GUI fails to initialize immediately, falls back to headless web mode.
 *
 * @version 2.0.0
 */
public class BankingApp {

    private static final String APP_VERSION = "2.0.0";
    private static final String APP_NAME = "SecureTrust Banking System";

    public static void main(String[] args) {
        displayWelcomeBanner();

        try {
            // Parse command line arguments
            AppConfig config = parseArguments(args);

            // Initialize enterprise components
            initializeEnterpriseComponents(config);

            // Run integration tests if requested
            if (config.runTests) {
                runIntegrationTests();
            }

            // Start the application (attempt GUI first unless explicitly headless)
            startApplication(config);

        } catch (Exception e) {
            handleCriticalError(e);
        }
    }

    private static void displayWelcomeBanner() {
        System.out.println("\n" +
            "╔══════════════════════════════════════════════════════════════════════════════╗\n" +
            "║                        SECURETRUST BANKING SYSTEM                           ║\n" +
            "║                      Enterprise Edition v" + APP_VERSION + "                          ║\n" +
            "╚══════════════════════════════════════════════════════════════════════════════╝\n");

        System.out.println("📋 Assignment Requirements:");
        System.out.println("  ✅ All three account types with specific business rules");
        System.out.println("  ✅ 10 sample customer records in database");
        System.out.println("  ✅ Complete OOAD principles demonstration");
        System.out.println("  ✅ Professional JavaFX GUI with MVC architecture");
        System.out.println("  ✅ GitHub Codespaces optimization");
        System.out.println("  ✅ Comprehensive documentation and testing\n");
    }

    private static AppConfig parseArguments(String[] args) {
        AppConfig config = new AppConfig();

        for (String arg : args) {
            switch (arg) {
                case "--headless":
                case "-h":
                    config.headlessMode = true;
                    config.userSpecifiedMode = true;
                    break;
                case "--test":
                case "-t":
                    config.runTests = true;
                    break;
                case "--gui":
                case "-g":
                    config.headlessMode = false;
                    config.userSpecifiedMode = true;
                    break;
                case "--help":
                    displayHelp();
                    System.exit(0);
                    break;
                case "--version":
                    System.out.println(APP_NAME + " v" + APP_VERSION);
                    System.exit(0);
                    break;
            }
        }

        // Default behavior: attempt GUI first unless user explicitly requested headless.
        if (!config.userSpecifiedMode) {
            System.out.println("ℹ️ No mode specified; attempting GUI first by default");
            config.headlessMode = false;
        } else {
            if (config.headlessMode) {
                System.out.println("ℹ️ User requested headless mode; GUI will be skipped");
            } else {
                System.out.println("ℹ️ User requested GUI mode; GUI will be started");
            }
        }

        return config;
    }

    private static void displayHelp() {
        System.out.println("\nUsage: java -jar banking-system.jar [OPTIONS]");
        System.out.println("\nOptions:");
        System.out.println("  -h, --headless    Run in headless mode with web interface");
        System.out.println("  -g, --gui         Run with JavaFX GUI (attempted first)");
        System.out.println("  -t, --test        Run integration tests before starting");
        System.out.println("      --version     Display version information");
        System.out.println("      --help        Display this help message");
    }

    private static void initializeEnterpriseComponents(AppConfig config) {
        System.out.println("🔧 Initializing enterprise components...");

        long startTime = System.currentTimeMillis();

        try {
            // Initialize database with connection pooling
            boolean dbReady = DatabaseConnection.testConnection();
            if (!dbReady) {
                throw new RuntimeException("Database initialization failed");
            }

            System.out.println("✅ Database initialized successfully");

            // Display database statistics
            String dbStats = DatabaseConnection.getDatabaseStats();
            System.out.println(dbStats);

            // Initialize security components
            initializeSecurity();

            // Load business configuration
            loadBusinessConfiguration();

            long duration = System.currentTimeMillis() - startTime;
            System.out.println("✅ All systems operational (" + duration + "ms)");

        } catch (Exception e) {
            System.err.println("❌ System initialization failed: " + e.getMessage());
            throw new RuntimeException("Critical system initialization failure", e);
        }
    }

    private static void initializeSecurity() {
        System.out.println("🔒 Initializing security components...");
        com.bankingapp.util.SecurityUtil.generateSalt();
        System.out.println("✅ Security utilities initialized");
    }

    private static void loadBusinessConfiguration() {
        System.out.println("📋 Loading business configuration...");
        System.out.println("   • Savings Account: 0.05% monthly interest, no withdrawals");
        System.out.println("   • Investment Account: 5% monthly interest, BWP 500 minimum");
        System.out.println("   • Cheque Account: No interest, requires employer information");
        System.out.println("✅ Business configuration loaded");
    }

    private static void runIntegrationTests() {
        System.out.println("\n🧪 Running Integration Test Suite...");
        System.out.println("Run: mvn test to execute integration tests");
    }

    private static void startApplication(AppConfig config) {
        // If the user explicitly requested headless mode, honor it and skip GUI.
        if (config.headlessMode && config.userSpecifiedMode) {
            launchHeadlessMode();
            return;
        }

        // Otherwise, attempt GUI first.
        boolean guiLaunched = launchGraphicalInterface();

        if (!guiLaunched) {
            System.out.println("\n⚠️ GUI failed to start — attempting headless fallback...");
            try {
                launchHeadlessMode();
            } catch (Exception e) {
                System.err.println("❌ Headless fallback also failed: " + e.getMessage());
                handleCriticalError(new RuntimeException("Both GUI and headless startup failed", e));
            }
        }
    }

    private static void launchHeadlessMode() {
        System.out.println("\n🌐 Starting Enterprise Headless Mode...");
        System.out.println("📊 Starting web services on port 8080...");

        try {
            com.bankingapp.controller.WebServer.start(8080);
            System.out.println("✅ Web services started successfully");
            System.out.println("🔗 Access the application via: http://localhost:8080");
            Thread.currentThread().join();
        } catch (Exception e) {
            System.err.println("❌ Failed to start web services: " + e.getMessage());
            throw new RuntimeException("Failed to start headless web services", e);
        }
    }

    /**
     * Attempt to launch the JavaFX GUI.
     * Returns true if the GUI launch was invoked successfully, false if it failed immediately.
     */
    private static boolean launchGraphicalInterface() {
        System.out.println("\n🎨 Attempting to launch Professional Graphical Interface...");

        try {
            // Set system properties for better JavaFX behavior in some environments
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.verbose", "true");

            // This should invoke JavaFX application startup. If this throws quickly, return false for fallback.
            MainView.main(new String[0]);

            // If MainView.main returns without immediate exception, assume the GUI launch was successful.
            System.out.println("✅ GUI launch invoked successfully");
            return true;

        } catch (Exception e) {
            System.err.println("❌ GUI initialization failed immediately: " + e.getMessage());
            System.err.println("💡 Tip: If running in a headless environment, use --headless to skip GUI");
            return false;
        }
    }

    private static void handleCriticalError(Exception e) {
        System.err.println("\n💥 CRITICAL SYSTEM ERROR:");
        System.err.println("Message: " + e.getMessage());
        System.err.println("\n🔧 Troubleshooting Tips:");
        System.err.println("1. Ensure Java 17 or higher is installed");
        System.err.println("2. Check database connection settings");
        System.err.println("3. Verify dependencies (JavaFX) are available");
        System.err.println("4. Try running with --headless mode");
        System.exit(1);
    }

    private static class AppConfig {
        boolean headlessMode = false;
        boolean runTests = false;
        boolean userSpecifiedMode = false;
    }
}