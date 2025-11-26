package com.securetrust.service;

import com.securetrust.model.Account;
import com.securetrust.model.AccountType;
import com.securetrust.model.Customer;
import com.securetrust.repository.AccountRepository;
import com.securetrust.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class AccountService {
    
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AtomicLong accountCounter = new AtomicLong(100);
    
    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }
    
    /**
     * Open a new account for a customer.
     * Validates business rules based on account type:
     * - Investment accounts require minimum BWP 500 initial deposit
     * - Cheque accounts require employer information
     */
    @Transactional
    public Account openAccount(Long customerId, AccountType accountType, Double initialDeposit, 
                               String branch, String employer, String employerAddress) {
        
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        
        // Validate initial deposit for Investment account
        if (accountType == AccountType.INVESTMENT && (initialDeposit == null || initialDeposit < 500.0)) {
            throw new IllegalArgumentException("Investment account requires minimum initial deposit of BWP 500.00");
        }
        
        // Validate employer info for Cheque account
        if (accountType == AccountType.CHEQUE) {
            if (employer == null || employer.trim().isEmpty()) {
                throw new IllegalArgumentException("Cheque account requires employer information");
            }
            if (employerAddress == null || employerAddress.trim().isEmpty()) {
                throw new IllegalArgumentException("Cheque account requires employer address");
            }
        }
        
        // Generate unique account number based on type
        String prefix;
        switch (accountType) {
            case SAVINGS:
                prefix = "SAV";
                break;
            case INVESTMENT:
                prefix = "INV";
                break;
            case CHEQUE:
                prefix = "CHQ";
                break;
            default:
                prefix = "ACC";
        }
        
        String accountNumber = prefix + String.format("%03d", accountCounter.incrementAndGet());
        
        // Ensure unique account number
        while (accountRepository.findByAccountNumber(accountNumber).isPresent()) {
            accountNumber = prefix + String.format("%03d", accountCounter.incrementAndGet());
        }
        
        Account account = new Account();
        account.setCustomer(customer);
        account.setAccountType(accountType);
        account.setAccountNumber(accountNumber);
        account.setBalance(initialDeposit != null ? initialDeposit : 0.0);
        account.setBranch(branch != null ? branch : "Main Branch");
        
        if (accountType == AccountType.CHEQUE) {
            account.setEmployer(employer);
            account.setEmployerAddress(employerAddress);
        }
        
        return accountRepository.save(account);
    }
    
    /**
     * Close an account (soft delete - set balance to 0).
     */
    @Transactional
    public void closeAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        if (account.getBalance() > 0) {
            throw new IllegalArgumentException("Cannot close account with positive balance. Please withdraw funds first.");
        }
        
        accountRepository.delete(account);
    }
}
