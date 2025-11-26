package com.securetrust.controller;

import com.securetrust.model.Customer;
import com.securetrust.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    @GetMapping("/login") public String login() { return "login"; }

    @PostMapping("/login")
    public String doLogin(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        // First check if it's an admin login
        if (authService.login(email, password)) {
            session.setAttribute("user", email);
            session.setAttribute("isAdmin", true);
            session.setAttribute("userType", "admin");
            return "redirect:/dashboard";
        }
        
        // Then check if it's a customer login
        Optional<Customer> customer = authService.loginAsCustomer(email, password);
        if (customer.isPresent()) {
            session.setAttribute("user", email);
            session.setAttribute("isAdmin", false);
            session.setAttribute("userType", "customer");
            session.setAttribute("customerId", customer.get().getId());
            session.setAttribute("customerName", customer.get().getFullName());
            return "redirect:/customer-portal";
        }
        
        model.addAttribute("error", "Invalid credentials");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
