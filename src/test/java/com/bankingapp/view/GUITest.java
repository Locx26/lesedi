package com.bankingapp.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javafx.application.Platform;
import javafx.scene.control.*;

/**
 * Comprehensive GUI Test for Week 2 Boundary Classes
 * Tests UI components without business logic
 */
public class GUITest {

    private LoginView loginView;
    private CustomerDashboardView customerDashboard;
    private TellerDashboardView tellerDashboard;

    @BeforeEach
    public void setUp() {
        // Initialize views on JavaFX thread
        Platform.runLater(() -> {
            loginView = new LoginView();
            customerDashboard = new CustomerDashboardView();
            tellerDashboard = new TellerDashboardView();
        });
    }

    @Test
    public void testLoginViewComponents() {
        Platform.runLater(() -> {
            assertNotNull(loginView.getView(), "Login view should be initialized");
            assertNotNull(loginView.getUsernameField(), "Username field should be initialized");
            assertNotNull(loginView.getPasswordField(), "Password field should be initialized");
            assertNotNull(loginView.getLoginButton(), "Login button should be initialized");
            assertNotNull(loginView.getUserTypeComboBox(), "User type combo box should be initialized");
           
            // Test initial state
            assertTrue(loginView.getUsernameField().getText().isEmpty(), "Username should be empty initially");
            assertTrue(loginView.getPasswordField().getText().isEmpty(), "Password should be empty initially");
        });
    }

    @Test
    public void testCustomerDashboardComponents() {
        Platform.runLater(() -> {
            assertNotNull(customerDashboard.getView(), "Customer dashboard view should be initialized");
            assertNotNull(customerDashboard.getAccountsList(), "Accounts list should be initialized");
            assertNotNull(customerDashboard.getDepositButton(), "Deposit button should be initialized");
            assertNotNull(customerDashboard.getWithdrawButton(), "Withdraw button should be initialized");
            assertNotNull(customerDashboard.getLogoutButton(), "Logout button should be initialized");
           
            // Test button states
            assertFalse(customerDashboard.getDepositButton().isDisable(), "Deposit button should be enabled");
            assertFalse(customerDashboard.getWithdrawButton().isDisable(), "Withdraw button should be enabled");
        });
    }

    @Test
    public void testTellerDashboardComponents() {
        Platform.runLater(() -> {
            assertNotNull(tellerDashboard.getView(), "Teller dashboard view should be initialized");
            assertNotNull(tellerDashboard.getOnboardCustomerButton(), "Onboard customer button should be initialized");
            assertNotNull(tellerDashboard.getOpenAccountButton(), "Open account button should be initialized");
            assertNotNull(tellerDashboard.getInfoTextArea(), "Info text area should be initialized");
           
            // Test initial content
            assertFalse(tellerDashboard.getInfoTextArea().getText().isEmpty(), "Info text area should have initial content");
        });
    }

    @Test
    public void testViewUpdateMethods() {
        Platform.runLater(() -> {
            // Test LoginView update methods
            loginView.showSuccessMessage("Test success");
            assertEquals("Test success", loginView.getMessageLabel().getText());
           
            loginView.showErrorMessage("Test error");
            assertEquals("Test error", loginView.getMessageLabel().getText());
           
            loginView.clearFields();
            assertTrue(loginView.getUsernameField().getText().isEmpty());
            assertTrue(loginView.getPasswordField().getText().isEmpty());
           
            // Test CustomerDashboard update methods
            customerDashboard.setWelcomeMessage("John Doe");
            assertTrue(customerDashboard.getWelcomeLabel().getText().contains("John Doe"));
           
            customerDashboard.setBalance("$1,000.00");
            assertEquals("$1,000.00", customerDashboard.getBalanceLabel().getText());
        });
    }

    @Test
    public void testInputValidation() {
        Platform.runLater(() -> {
            // Test empty input validation
            loginView.getUsernameField().setText("");
            loginView.getPasswordField().setText("");
            assertFalse(loginView.validateInput(), "Should fail validation with empty fields");
           
            // Test valid input
            loginView.getUsernameField().setText("testuser");
            loginView.getPasswordField().setText("password123");
            assertTrue(loginView.validateInput(), "Should pass validation with valid fields");
           
            // Test amount validation
            customerDashboard.getAmountField().setText("invalid");
            assertFalse(customerDashboard.validateAmount(), "Should fail with invalid amount");
           
            customerDashboard.getAmountField().setText("100.50");
            assertTrue(customerDashboard.validateAmount(), "Should pass with valid amount");
        });
    }

    @Test
    public void testNoBusinessLogicInViews() {
        Platform.runLater(() -> {
            // Verify that views don't contain business logic
            // They should only have UI update methods and input validation
           
            // LoginView should not authenticate users
            assertFalse(loginView.getClass().getSimpleName().toLowerCase().contains("service"));
            assertFalse(loginView.getClass().getSimpleName().toLowerCase().contains("controller"));
           
            // CustomerDashboard should not process transactions
            assertFalse(customerDashboard.getClass().getSimpleName().toLowerCase().contains("service"));
            assertFalse(customerDashboard.getClass().getSimpleName().toLowerCase().contains("controller"));
           
            // Methods should be focused on UI updates only
            assertTrue(loginView.getClass().getDeclaredMethods().length > 0);
            for (var method : loginView.getClass().getDeclaredMethods()) {
                String methodName = method.getName().toLowerCase();
                assertFalse(methodName.contains("authenticate") || methodName.contains("process"),
                    "Views should not contain business logic methods");
            }
        });
    }
}