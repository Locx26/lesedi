package com.bankingapp.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Professional Security Utility Class
 * - Uses PBKDF2WithHmacSHA256 for password hashing (salted, iterated)
 * - Provides constant-time verification
 * - Safer token generation and masking helpers
 * - Basic validation helpers (email, file types, OTP)
 *
 * Notes:
 * - Prefer prepared statements / parameterized queries rather than relying on isSqlInjectionSafe().
 * - The password hash format produced by hashPassword(...) is:
 *     pbkdf2$<iterations>$<base64-salt>$<base64-hash>
 */
public final class SecurityUtil {

    private static final Logger logger = Logger.getLogger(SecurityUtil.class.getName());
    private static final SecureRandom random = new SecureRandom();

    // Password hashing parameters
    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int DEFAULT_ITERATIONS = 100_000;
    private static final int KEY_LENGTH_BITS = 256; // derived key length

    // OTP parameters
    private static final int OTP_DIGITS = 6;
    private static final int OTP_MIN = (int) Math.pow(10, OTP_DIGITS - 1);
    private static final int OTP_RANGE = (int) Math.pow(10, OTP_DIGITS) - OTP_MIN;

    // Password policy
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 128;
    private static final Pattern UPPERCASE = Pattern.compile(".*[A-Z].*");
    private static final Pattern LOWERCASE = Pattern.compile(".*[a-z].*");
    private static final Pattern DIGIT = Pattern.compile(".*\\d.*");
    private static final Pattern SPECIAL = Pattern.compile(".*[^A-Za-z0-9].*");

    // Email pattern (basic)
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Allowed upload file extensions (lowercase)
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".pdf", ".txt"};

    private SecurityUtil() { /* utility */ }

    /**
     * Hash a password using PBKDF2(HmacSHA256). Returns a formatted string:
     *   pbkdf2$<iterations>$<base64-salt>$<base64-hash>
     *
     * Salt is generated if null or empty; supply the salt if you need deterministic output.
     */
    public static String hashPassword(String password, String saltBase64) {
        if (password == null) throw new IllegalArgumentException("password is required");

        byte[] salt;
        if (saltBase64 == null || saltBase64.isEmpty()) {
            salt = generateRawSalt();
        } else {
            salt = Base64.getDecoder().decode(saltBase64);
        }

        byte[] dk = pbkdf2(password.toCharArray(), salt, DEFAULT_ITERATIONS, KEY_LENGTH_BITS);
        String hashB64 = Base64.getEncoder().encodeToString(dk);
        String saltB64 = Base64.getEncoder().encodeToString(salt);

        return String.format("pbkdf2$%d$%s$%s", DEFAULT_ITERATIONS, saltB64, hashB64);
    }

    /**
     * Verify a password against a stored hash in the expected format:
     *   pbkdf2$<iterations>$<base64-salt>$<base64-hash>
     *
     * Returns true if the password matches, false otherwise.
     */
    public static boolean verifyPassword(String password, String stored) {
        if (password == null || stored == null) return false;
        try {
            String[] parts = stored.split("\\$");
            if (parts.length != 4 || !"pbkdf2".equals(parts[0])) {
                logger.fine("Stored password hash in unexpected format");
                return false;
            }
            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expected = Base64.getDecoder().decode(parts[3]);

            byte[] actual = pbkdf2(password.toCharArray(), salt, iterations, expected.length * 8);
            return constantTimeEquals(expected, actual);
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Error verifying password: " + ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Generate a random salt (raw bytes) - caller can Base64-encode as needed.
     */
    private static byte[] generateRawSalt() {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Generate Base64-encoded salt string.
     */
    public static String generateSalt() {
        return Base64.getEncoder().encodeToString(generateRawSalt());
    }

    /**
     * PBKDF2 helper - returns derived key bytes.
     */
    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int keyLengthBits) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLengthBits);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.log(Level.SEVERE, "PBKDF2 algorithm not available", e);
            throw new IllegalStateException("Hashing algorithm unavailable", e);
        }
    }

    /**
     * Constant-time byte array comparison to avoid timing attacks.
     */
    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a == null || b == null) return false;
        if (a.length != b.length) return false;
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }

    /**
     * Basic password policy check. Returns true if the password meets minimum rules.
     * Note: consider pushing policy enforcement into registration/change-password flows
     * with user-friendly messages rather than a boolean.
     */
    public static boolean isPasswordSecure(String password) {
        if (password == null) return false;
        int len = password.length();
        if (len < MIN_PASSWORD_LENGTH || len > MAX_PASSWORD_LENGTH) return false;
        if (!UPPERCASE.matcher(password).matches()) return false;
        if (!LOWERCASE.matcher(password).matches()) return false;
        if (!DIGIT.matcher(password).matches()) return false;
        if (!SPECIAL.matcher(password).matches()) return false;
        return true;
    }

    /**
     * Generate a secure random session token encoded in URL-safe Base64 (no padding).
     */
    public static String generateSessionToken() {
        byte[] token = new byte[32];
        random.nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
    }

    /**
     * Mask an account number for logs/UI: show first 3 and last 3 characters, mask middle.
     */
    public static String maskAccountNumber(String accountNumber) {
        if (accountNumber == null) return "***";
        String cleaned = accountNumber.trim();
        int len = cleaned.length();
        if (len <= 6) return "***";
        String prefix = cleaned.substring(0, 3);
        String suffix = cleaned.substring(len - 3);
        return prefix + "***" + suffix;
    }

    /**
     * Mask phone number showing only last 4 digits.
     */
    public static String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) return "***";
        String cleaned = phoneNumber.trim();
        int len = cleaned.length();
        if (len <= 4) return "***";
        return "***" + cleaned.substring(len - 4);
    }

    /**
     * Simple heuristic to detect suspicious input patterns that often indicate injection attempts.
     * This is a best-effort helper only — always use prepared statements / parameterized queries.
     */
    public static boolean isSqlInjectionSafe(String input) {
        if (input == null) return true;
        String lower = input.toLowerCase();
        // quick rejects for dangerous tokens — do not log full input
        String[] suspicious = {"--", ";--", ";", "/*", "*/", " or ", " union ", " drop ", " delete ", " insert ", " update ", " exec ", " xp_", " sp_"};
        for (String s : suspicious) {
            if (lower.contains(s)) {
                logger.warning("Potential SQL injection pattern detected (masked): " + maskSensitiveInput(input));
                return false;
            }
        }
        return true;
    }

    /**
     * Mask a sensitive input for logging: keep short prefix/suffix only.
     */
    private static String maskSensitiveInput(String input) {
        if (input == null) return null;
        if (input.length() <= 10) return "***";
        return input.substring(0, 4) + "..." + input.substring(input.length() - 4);
    }

    /**
     * Generate a numeric OTP with fixed digits as a zero-padded string.
     */
    public static String generateOTP() {
        int value = OTP_MIN + random.nextInt(OTP_RANGE);
        return String.format("%0" + OTP_DIGITS + "d", value);
    }

    /**
     * Basic email validation.
     */
    public static boolean isEmailSecure(String email) {
        if (email == null) return false;
        if (email.length() > 254) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validate file upload by extension (case-insensitive).
     */
    public static boolean isFileTypeSafe(String fileName) {
        if (fileName == null) return false;
        String lower = fileName.toLowerCase();
        for (String ext : ALLOWED_EXTENSIONS) {
            if (lower.endsWith(ext)) return true;
        }
        return false;
    }

    /**
     * Log a security event. Details should be non-sensitive; sensitive fields must be masked.
     */
    public static void logSecurityEvent(String event, String userIdentifier, String details) {
        String when = Instant.now().toString();
        String maskedUser = userIdentifier == null ? "unknown" : maskSensitiveInput(userIdentifier);
        String safeDetails = details == null ? "" : details.length() > 200 ? details.substring(0, 200) + "..." : details;
        logger.info(String.format("SECURITY_EVENT [%s]: %s - User: %s - Details: %s", when, event, maskedUser, safeDetails));
    }
}