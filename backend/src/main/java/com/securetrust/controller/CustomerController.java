package com.securetrust.controller;

import com.securetrust.model.AccountType;
import com.securetrust.model.Customer;
import com.securetrust.model.CustomerType;
import com.securetrust.repository.AccountRepository;
import com.securetrust.repository.CustomerRepository;
import com.securetrust.repository.TransactionRepository;
import com.securetrust.service.PasswordService;
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
    private final TransactionRepository transactionRepository;
    private final PasswordService passwordService;
    
    public CustomerController(CustomerRepository customerRepository, 
                             AccountRepository accountRepository,
                             TransactionRepository transactionRepository,
                             PasswordService passwordService) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.passwordService = passwordService;
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
        
        // Calculate total balance
        double totalBalance = customers.stream()
            .flatMap(c -> c.getAccounts() != null ? c.getAccounts().stream() : java.util.stream.Stream.empty())
            .mapToDouble(a -> a.getBalance() != null ? a.getBalance() : 0.0)
            .sum();
        
        model.addAttribute("customers", customers);
        model.addAttribute("totalAccounts", totalAccounts);
        model.addAttribute("totalBalance", totalBalance);
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
        
        // Get transactions for customer's accounts
        var transactions = transactionRepository.findByCustomerId(id);
        
        model.addAttribute("customer", customer);
        model.addAttribute("transactions", transactions);
        model.addAttribute("accountTypes", AccountType.values());
        model.addAttribute("customerTypes", CustomerType.values());
        return "customer-details";
    }
    
    @PostMapping("/add")
    public String addCustomer(@RequestParam(required = false) String firstName,
                             @RequestParam(required = false) String surname,
                             @RequestParam(required = false) String address,
                             @RequestParam(required = false) String phoneNumber,
                             @RequestParam String email,
                             @RequestParam String customerType,
                             @RequestParam(required = false) String companyName,
                             @RequestParam(required = false) String password,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        try {
            Customer customer = new Customer();
            // Generate unique customer ID using timestamp to avoid duplicates
            String customerId = "CUST" + System.currentTimeMillis() % 1000000;
            customer.setCustomerId(customerId);
            customer.setCustomerType(CustomerType.valueOf(customerType));
            
            if (customer.getCustomerType() == CustomerType.COMPANY) {
                if (companyName == null || companyName.trim().isEmpty()) {
                    throw new IllegalArgumentException("Company name is required for company customers");
                }
                customer.setCompanyName(companyName);
            }
            
            customer.setFirstName(firstName);
            customer.setSurname(surname);
            customer.setAddress(address);
            customer.setPhoneNumber(phoneNumber);
            customer.setEmail(email);
            customer.setPassword(passwordService.hashPassword(password));
            
            customerRepository.save(customer);
            redirectAttributes.addFlashAttribute("successMessage", "Customer added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/customers";
    }
    
    @PostMapping("/{id}/update")
    public String updateCustomer(@PathVariable Long id,
                                 @RequestParam String firstName,
                                 @RequestParam String surname,
                                 @RequestParam(required = false) String address,
                                 @RequestParam(required = false) String phoneNumber,
                                 @RequestParam String email,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        try {
            var existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
            
            existingCustomer.setFirstName(firstName);
            existingCustomer.setSurname(surname);
            existingCustomer.setAddress(address);
            existingCustomer.setPhoneNumber(phoneNumber);
            existingCustomer.setEmail(email);
            
            customerRepository.save(existingCustomer);
            redirectAttributes.addFlashAttribute("successMessage", "Customer updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/customers/" + id;
    }
    
    @PostMapping("/{id}/delete")
    public String deleteCustomer(@PathVariable Long id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        try {
            var customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
            
            // Check if customer has accounts with balance
            var accounts = accountRepository.findByCustomerId(id);
            double totalBalance = accounts.stream().mapToDouble(a -> a.getBalance()).sum();
            
            if (totalBalance > 0) {
                throw new IllegalArgumentException("Cannot delete customer with account balance. Please close all accounts first.");
            }
            
            customerRepository.delete(customer);
            redirectAttributes.addFlashAttribute("successMessage", "Customer deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/customers";
    }
}
