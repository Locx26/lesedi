package com.securetrust.controller;

import com.securetrust.model.Customer;
import com.securetrust.repository.AccountRepository;
import com.securetrust.repository.CustomerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    
    public CustomerController(CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }
    
    @GetMapping
    public String customersPage(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        var customers = customerRepository.findAll();
        customers.forEach(c -> c.setAccounts(accountRepository.findByCustomerId(c.getId())));
        
        // Calculate total accounts
        int totalAccounts = customers.stream()
            .mapToInt(c -> c.getAccounts() != null ? c.getAccounts().size() : 0)
            .sum();
        
        model.addAttribute("customers", customers);
        model.addAttribute("totalAccounts", totalAccounts);
        return "customers";
    }
    
    @GetMapping("/{id}")
    public String customerDetails(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        var customer = customerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        customer.setAccounts(accountRepository.findByCustomerId(id));
        
        model.addAttribute("customer", customer);
        return "customer-details";
    }
    
    @PostMapping("/add")
    public String addCustomer(@ModelAttribute Customer customer,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        try {
            customerRepository.save(customer);
            redirectAttributes.addFlashAttribute("successMessage", "Customer added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/customers";
    }
    
    @PostMapping("/{id}/update")
    public String updateCustomer(@PathVariable Long id,
                                 @ModelAttribute Customer customer,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        try {
            var existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
            
            existingCustomer.setFirstName(customer.getFirstName());
            existingCustomer.setSurname(customer.getSurname());
            existingCustomer.setAddress(customer.getAddress());
            existingCustomer.setPhoneNumber(customer.getPhoneNumber());
            existingCustomer.setEmail(customer.getEmail());
            
            customerRepository.save(existingCustomer);
            redirectAttributes.addFlashAttribute("successMessage", "Customer updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/customers/" + id;
    }
}
