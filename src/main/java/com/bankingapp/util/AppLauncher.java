package com.bankingapp.util;

/**
 * Application Launcher - Utility class for starting the banking application
 */
public class AppLauncher {
   
    public static void printStartupInfo() {
        System.out.println("=========================================");
        System.out.println("   SecureTrust Banking System v1.0");
        System.out.println("=========================================");
        System.out.println("Architecture: MVC Pattern");
        System.out.println("Layers: View - Controller - Service - Model");
        System.out.println("Framework: JavaFX 17");
        System.out.println("Build: Gradle");
        System.out.println("=========================================");
        System.out.println();
       
        System.out.println("Sample Login Credentials:");
        System.out.println("Customer: john_doe / password123");
        System.out.println("Bank Teller: teller1 / teller123");
        System.out.println("Administrator: admin / admin123");
        System.out.println();
    }
   
    public static void checkDependencies() {
        try {
            Class.forName("javafx.application.Application");
            System.out.println("✅ JavaFX dependencies are available");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ JavaFX not found in classpath");
            System.err.println("Please ensure JavaFX is properly configured");
            System.exit(1);
        }
       
        try {
            Class.forName("org.junit.jupiter.api.Test");
            System.out.println("✅ Testing dependencies are available");
        } catch (ClassNotFoundException e) {
            System.out.println("⚠️  Testing dependencies not found (optional for runtime)");
        }
    }
}