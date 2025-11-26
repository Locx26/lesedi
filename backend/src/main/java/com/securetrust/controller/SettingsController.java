package com.securetrust.controller;

import com.securetrust.repository.CustomerRepository;
import com.securetrust.service.PasswordService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SettingsController {
    
    private final CustomerRepository customerRepository;
    private final PasswordService passwordService;
    
    public SettingsController(CustomerRepository customerRepository, PasswordService passwordService) {
        this.customerRepository = customerRepository;
        this.passwordService = passwordService;
    }
    
    @GetMapping("/notifications")
    public String notifications(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        return "notifications";
    }
    
    @GetMapping("/settings")
    public String settings(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        return "settings";
    }
    
    @PostMapping("/settings/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        // Validate passwords match
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "New passwords do not match");
            return "redirect:/settings";
        }
        
        // Validate password length
        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password must be at least 6 characters long");
            return "redirect:/settings";
        }
        
        String email = (String) session.getAttribute("user");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        
        if (isAdmin != null && isAdmin) {
            // Admin password change - admin password is hardcoded so we can't change it
            if (!"Admin123!".equals(currentPassword)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Current password is incorrect");
                return "redirect:/settings";
            }
            // Note: Cannot actually change admin password as it's hardcoded
            redirectAttributes.addFlashAttribute("errorMessage", "Admin password cannot be changed through settings");
        } else {
            // Customer password change
            var customerOpt = customerRepository.findByEmail(email);
            if (customerOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Customer not found");
                return "redirect:/settings";
            }
            
            var customer = customerOpt.get();
            if (customer.getPassword() == null || !passwordService.verifyPassword(currentPassword, customer.getPassword())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Current password is incorrect");
                return "redirect:/settings";
            }
            
            customer.setPassword(passwordService.hashPassword(newPassword));
            customerRepository.save(customer);
            redirectAttributes.addFlashAttribute("successMessage", "Password updated successfully");
        }
        
        return "redirect:/settings";
    }
}
