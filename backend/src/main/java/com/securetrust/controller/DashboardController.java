package com.securetrust.controller;

import com.securetrust.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    private final CustomerRepository customerRepo;
    private final AccountRepository accountRepo;
    private final TransactionRepository transactionRepo;

    public DashboardController(CustomerRepository customerRepo, 
                              AccountRepository accountRepo,
                              TransactionRepository transactionRepo) {
        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        var customers = customerRepo.findAll();
        var accounts = accountRepo.findAll();
        customers.forEach(c -> c.setAccounts(accountRepo.findByCustomerId(c.getId())));
        double total = accounts.stream().mapToDouble(a -> a.getBalance()).sum();
        
        // Extract balances for the chart
        var balances = accounts.stream().map(a -> a.getBalance()).toList();
        
        // Get recent transactions
        var recentTransactions = transactionRepo.findTop10ByOrderByTransactionDateDesc();

        model.addAttribute("customers", customers);
        model.addAttribute("accounts", accounts);
        model.addAttribute("totalBalance", total);
        model.addAttribute("balances", balances);
        model.addAttribute("recentTransactions", recentTransactions);
        model.addAttribute("totalAccounts", accounts.size());
        return "dashboard";
    }
}
