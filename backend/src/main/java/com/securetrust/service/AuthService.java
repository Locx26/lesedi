package com.securetrust.service;

import com.securetrust.model.Customer;
import com.securetrust.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final CustomerRepository customerRepository;
    
    public AuthService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    public boolean login(String email, String password) {
        return "admin@securetrust.com".equals(email) && "Admin123!".equals(password);
    }
    
    public boolean isAdmin(String email) {
        return "admin@securetrust.com".equals(email);
    }
    
    public Optional<Customer> loginAsCustomer(String email, String password) {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent() && password != null && password.equals(customer.get().getPassword())) {
            return customer;
        }
        return Optional.empty();
    }
}
