package com.securetrust.repository;

import com.securetrust.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountIdOrderByTransactionDateDesc(Long accountId);
    
    List<Transaction> findByAccountAccountNumberOrderByTransactionDateDesc(String accountNumber);
    
    @Query("SELECT t FROM Transaction t WHERE t.account.customer.id = :customerId ORDER BY t.transactionDate DESC")
    List<Transaction> findByCustomerId(Long customerId);
    
    List<Transaction> findTop10ByOrderByTransactionDateDesc();
}
