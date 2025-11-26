package com.securetrust.controller;

import com.securetrust.model.AccountType;
import com.securetrust.repository.AccountRepository;
import com.securetrust.repository.CustomerRepository;
import com.securetrust.service.AccountService;
import com.securetrust.service.BankingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/accounts")
public class AccountController {
    
    private final AccountService accountService;
    private final BankingService bankingService;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    
    public AccountController(AccountService accountService, BankingService bankingService,
                            AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountService = accountService;
        this.bankingService = bankingService;
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }
    
    @GetMapping
    public String accountsPage(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        var accounts = accountRepository.findAll();
        var customers = customerRepository.findAll();
        
        model.addAttribute("accounts", accounts);
        model.addAttribute("customers", customers);
        model.addAttribute("accountTypes", AccountType.values());
        return "accounts";
    }
    
    @PostMapping("/open")
    public String openAccount(@RequestParam Long customerId,
                              @RequestParam String accountType,
                              @RequestParam(required = false) Double initialDeposit,
                              @RequestParam(required = false) String branch,
                              @RequestParam(required = false) String employer,
                              @RequestParam(required = false) String employerAddress,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        try {
            AccountType type = AccountType.valueOf(accountType);
            var account = accountService.openAccount(customerId, type, initialDeposit, branch, employer, employerAddress);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Account " + account.getAccountNumber() + " opened successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/customers/" + customerId;
    }
    
    @PostMapping("/{accountNumber}/close")
    public String closeAccount(@PathVariable String accountNumber,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        try {
            accountService.closeAccount(accountNumber);
            redirectAttributes.addFlashAttribute("successMessage", "Account closed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/accounts";
    }
    
    @PostMapping("/pay-interest")
    public String payInterest(HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        try {
            int count = bankingService.payInterestToAllAccounts();
            redirectAttributes.addFlashAttribute("successMessage", 
                "Interest paid to " + count + " accounts successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/dashboard";
    }
}
