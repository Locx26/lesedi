package com.securetrust.service;

import com.securetrust.model.Account;
import com.securetrust.model.Transaction;
import com.securetrust.model.TransactionType;
import com.securetrust.repository.AccountRepository;
import com.securetrust.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankingService {
    
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    
    public BankingService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }
    
    @Transactional
    public Transaction deposit(String accountNumber, Double amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(amount);
        transaction.setDescription(description != null ? description : "Deposit");
        transaction.setCategory("Deposit");
        transaction.setBalanceAfter(account.getBalance());
        
        return transactionRepository.save(transaction);
    }
    
    @Transactional
    public Transaction withdraw(String accountNumber, Double amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
        
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setAmount(amount);
        transaction.setDescription(description != null ? description : "Withdrawal");
        transaction.setCategory("Withdrawal");
        transaction.setBalanceAfter(account.getBalance());
        
        return transactionRepository.save(transaction);
    }
    
    @Transactional
    public void transfer(String fromAccountNumber, String toAccountNumber, Double amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
            .orElseThrow(() -> new IllegalArgumentException("Source account not found"));
        
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
            .orElseThrow(() -> new IllegalArgumentException("Destination account not found"));
        
        if (fromAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        
        // Debit from source account
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        accountRepository.save(fromAccount);
        
        Transaction debitTxn = new Transaction();
        debitTxn.setAccount(fromAccount);
        debitTxn.setType(TransactionType.TRANSFER_OUT);
        debitTxn.setAmount(amount);
        debitTxn.setDescription(description != null ? description : "Transfer to " + toAccountNumber);
        debitTxn.setCategory("Transfer");
        debitTxn.setBalanceAfter(fromAccount.getBalance());
        debitTxn.setReferenceAccount(toAccountNumber);
        transactionRepository.save(debitTxn);
        
        // Credit to destination account
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepository.save(toAccount);
        
        Transaction creditTxn = new Transaction();
        creditTxn.setAccount(toAccount);
        creditTxn.setType(TransactionType.TRANSFER_IN);
        creditTxn.setAmount(amount);
        creditTxn.setDescription(description != null ? description : "Transfer from " + fromAccountNumber);
        creditTxn.setCategory("Transfer");
        creditTxn.setBalanceAfter(toAccount.getBalance());
        creditTxn.setReferenceAccount(fromAccountNumber);
        transactionRepository.save(creditTxn);
    }
    
    public Double getAccountBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        return account.getBalance();
    }
}
