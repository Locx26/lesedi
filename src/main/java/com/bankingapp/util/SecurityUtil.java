package com.bankingapp.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Logger;

/**
 * Professional Security Utility Class
 * Provides security functions for banking applications
 */
public class SecurityUtil {
   
    private static final Logger logger = Logger.getLogger(SecurityUtil.class.getName());
    private static final SecureRandom random = new SecureRandom();
   
    // Password requirements
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 128;
   
    /**
     * Hash a password with salt using SHA-256
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String combined = password + salt;
            byte[] hash = digest.digest(combined.getBytes());
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            logger.severe("SHA-256 algorithm not available: " + e.getMessage());
            throw new RuntimeException("Security algorithm not available", e);
        }
    }
   
    /**
     * Generate a random salt for password hashing
     */
    public static String generateSalt() {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
   
    /**
     * Validate password against security requirements
     */
    public static boolean isPasswordSecure(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }
       
        if (password.length() > MAX_PASSWORD_LENGTH) {
            return false;
        }
       
        // Check for at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
       
        // Check for at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
       
        // Check for at least one digit
        if (!password.matches(".*\\d.*")) {
            return false;
        }
       
        // Check for at least one special character
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            return false;
        }
       
        return true;
    }
   
    /**
     * Generate a secure random token for session management
     */
    public static String generateSessionToken() {
        byte[] token = new byte[32];
        random.nextBytes(token);
        return bytesToHex(token);
    }
   
    /**
     * Mask sensitive data for logging (e.g., account numbers)
     */
    public static String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 8) {
            return "***";
        }
       
        String prefix = accountNumber.substring(0, 3);
        String suffix = accountNumber.substring(accountNumber.length() - 3);
        return prefix + "***" + suffix;
    }
   
    /**
     * Mask phone numbers for display
     */
    public static String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 4) {
            return "***";
        }
       
        String lastFour = phoneNumber.substring(phoneNumber.length() - 4);
        return "***" + lastFour;
    }
   
    /**
     * Validate input against SQL injection patterns
     */
    public static boolean isSqlInjectionSafe(String input) {
        if (input == null) return true;
       
        // Common SQL injection patterns
        String[] dangerousPatterns = {
            "--", ";", "'", "\"", "/*", "*/", "@@", "char(", "nchar(", "varchar(",
            "nvarchar(", "alter", "create", "delete", "drop", "exec", "execute",
            "insert", "select", "update", "union", "xp_", "sp_"
        };
       
        String lowerInput = input.toLowerCase();
        for (String pattern : dangerousPatterns) {
            if (lowerInput.contains(pattern)) {
                logger.warning("Potential SQL injection detected: " + pattern + " in input: " + maskSensitiveInput(input));
                return false;
            }
        }
       
        return true;
    }
   
    /**
     * Mask sensitive input for logging
     */
    private static String maskSensitiveInput(String input) {
        if (input == null) return null;
        if (input.length() <= 10) return "***";
        return input.substring(0, 5) + "***" + input.substring(input.length() - 5);
    }
   
    /**
     * Generate a secure random number for OTP
     */
    public static String generateOTP() {
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }
   
    /**
     * Validate email for security (basic pattern + length check)
     */
    public static boolean isEmailSecure(String email) {
        if (email == null || email.length() > 254) { // RFC 5321 limit
            return false;
        }
       
        // Basic email pattern check
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
   
    /**
     * Convert byte array to hexadecimal string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
   
    /**
     * Log security event
     */
    public static void logSecurityEvent(String event, String user, String details) {
        String maskedUser = user != null ? maskSensitiveInput(user) : "unknown";
        logger.info(String.format("SECURITY_EVENT: %s - User: %s - Details: %s",
                                event, maskedUser, details));
    }
   
    /**
     * Validate file upload for security (basic check)
     */
    public static boolean isFileTypeSafe(String fileName) {
        if (fileName == null) return false;
       
        String lowerFileName = fileName.toLowerCase();
        String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".pdf", ".txt"};
       
        for (String ext : allowedExtensions) {
            if (lowerFileName.endsWith(ext)) {
                return true;
            }
        }
       
        return false;
    }
}