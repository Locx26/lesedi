package com.securetrust.controller;

import com.securetrust.model.Transaction;
import com.securetrust.repository.AccountRepository;
import com.securetrust.repository.TransactionRepository;
import com.securetrust.service.BankingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/transactions")
public class TransactionController {
    
    private final BankingService bankingService;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    
    public TransactionController(BankingService bankingService, 
                                 TransactionRepository transactionRepository,
                                 AccountRepository accountRepository) {
        this.bankingService = bankingService;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }
    
    @GetMapping
    public String transactionsPage(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        var recentTransactions = transactionRepository.findTop10ByOrderByTransactionDateDesc();
        var accounts = accountRepository.findAll();
        
        model.addAttribute("transactions", recentTransactions);
        model.addAttribute("accounts", accounts);
        return "transactions";
    }
    
    @PostMapping("/deposit")
    public String deposit(@RequestParam String accountNumber,
                         @RequestParam Double amount,
                         @RequestParam(required = false) String description,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        try {
            bankingService.deposit(accountNumber, amount, description);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Deposit of BWP " + String.format("%,.2f", amount) + " successful!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/transactions";
    }
    
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accountNumber,
                          @RequestParam Double amount,
                          @RequestParam(required = false) String description,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        try {
            bankingService.withdraw(accountNumber, amount, description);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Withdrawal of BWP " + String.format("%,.2f", amount) + " successful!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/transactions";
    }
    
    @PostMapping("/transfer")
    public String transfer(@RequestParam String fromAccount,
                          @RequestParam String toAccount,
                          @RequestParam Double amount,
                          @RequestParam(required = false) String description,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        try {
            bankingService.transfer(fromAccount, toAccount, amount, description);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Transfer of BWP " + String.format("%,.2f", amount) + " successful!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/transactions";
    }
    
    @GetMapping("/account/{accountNumber}")
    public String accountTransactions(@PathVariable String accountNumber,
                                     HttpSession session,
                                     Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        var account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        var transactions = transactionRepository.findByAccountAccountNumberOrderByTransactionDateDesc(accountNumber);
        
        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);
        return "account-transactions";
    }
}
