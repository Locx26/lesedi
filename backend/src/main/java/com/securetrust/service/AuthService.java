package com.securetrust.service;

import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public boolean login(String email, String password) {
        return "admin@securetrust.com".equals(email) && "Admin123!".equals(password);
    }
}
