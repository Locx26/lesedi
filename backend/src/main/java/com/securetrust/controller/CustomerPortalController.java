package com.securetrust.controller;

import com.securetrust.repository.AccountRepository;
import com.securetrust.repository.CustomerRepository;
import com.securetrust.repository.TransactionRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerPortalController {
    
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    
    public CustomerPortalController(CustomerRepository customerRepository,
                                   AccountRepository accountRepository,
                                   TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }
    
    @GetMapping("/customer-portal")
    public String customerPortal(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        // Check if user is a customer (not admin)
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (isAdmin != null && isAdmin) {
            return "redirect:/dashboard";
        }
        
        Long customerId = (Long) session.getAttribute("customerId");
        if (customerId == null) {
            return "redirect:/login";
        }
        
        var customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            session.invalidate();
            return "redirect:/login";
        }
        
        var customer = customerOpt.get();
        var accounts = accountRepository.findByCustomerId(customerId);
        customer.setAccounts(accounts);
        
        // Calculate total balance
        double totalBalance = accounts.stream()
                .mapToDouble(a -> a.getBalance())
                .sum();
        
        // Get recent transactions
        var transactions = transactionRepository.findByCustomerId(customerId);
        
        model.addAttribute("customer", customer);
        model.addAttribute("accounts", accounts);
        model.addAttribute("totalBalance", totalBalance);
        model.addAttribute("transactions", transactions);
        
        return "customer-portal";
    }
}
