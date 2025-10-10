package com.bankingapp.model;

public abstract class User {
    protected String userId;
    protected String passwordHash;
    protected String salt;

    public User(String userId, String password) {
        this.userId = userId;
        this.salt = generateSalt();
        this.passwordHash = hashPassword(password, this.salt);
    }

    public abstract boolean login(String username, String password);
    public abstract void logout();

    public void changePassword(String newPassword) {
        this.passwordHash = hashPassword(newPassword, this.salt);
        System.out.println("Password changed successfully for user: " + userId);
    }

    private String generateSalt() {
        return "salt_" + System.currentTimeMillis();
    }

    private String hashPassword(String password, String salt) {
        return "hashed_" + password + "_" + salt;
    }

    protected boolean verifyPassword(String inputPassword, String storedHash) {
        return hashPassword(inputPassword, this.salt).equals(storedHash);
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getPasswordHash() { return passwordHash; }
}
