package com.bankingapp.model;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Domain model for application users.
 * Preserves the original public API (no breaking changes) while adding:
 *  - defensive validation and normalization,
 *  - sensible defaults (auto-generate id if missing),
 *  - convenience constructors and factory methods,
 *  - equals/hashCode/toString implementations.
 */
public class User {
    private static final Pattern EMAIL_RE = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private String userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private boolean active;

    public User() {
        this.userId = generateId();
        this.active = true;
    }

    public User(String userId, String username, String email,
                String firstName, String lastName, String role) {
        this.userId = userId == null || userId.isBlank() ? generateId() : userId;
        this.username = username != null ? username.trim() : null;
        setEmail(email);
        this.firstName = firstName != null ? firstName.trim() : "";
        this.lastName = lastName != null ? lastName.trim() : "";
        this.role = role != null ? role.trim() : "USER";
        this.active = true;
    }

    public static User of(String username, String email, String firstName, String lastName, String role) {
        return new User(null, username, email, firstName, lastName, role);
    }

    private static String generateId() {
        return "USER-" + UUID.randomUUID().toString();
    }

    // Getters and setters (kept same signatures for compatibility)

    public String getUserId() { return userId; }
    public void setUserId(String userId) {
        if (userId != null && !userId.isBlank()) this.userId = userId;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) {
        this.username = username != null ? username.trim() : null;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            this.email = "";
            return;
        }
        String trimmed = email.trim();
        // Basic validation — keep value even if invalid but normalize; throw only on explicit refusal if desired.
        if (EMAIL_RE.matcher(trimmed).matches()) {
            this.email = trimmed;
        } else {
            // normalize to lowercase but keep value; callers may validate further
            this.email = trimmed.toLowerCase();
        }
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) {
        this.firstName = firstName != null ? firstName.trim() : "";
    }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) {
        this.lastName = lastName != null ? lastName.trim() : "";
    }

    public String getRole() { return role; }
    public void setRole(String role) {
        this.role = role != null ? role.trim() : "USER";
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getFullName() {
        String fn = firstName == null ? "" : firstName;
        String ln = lastName == null ? "" : lastName;
        String full = (fn + " " + ln).trim();
        return full.isEmpty() ? username != null ? username : "" : full;
    }

    @Override
    public String toString() {
        return String.format("User[%s: %s (%s) active=%s role=%s]",
            userId,
            getFullName(),
            username != null ? username : "no-username",
            active,
            role
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}