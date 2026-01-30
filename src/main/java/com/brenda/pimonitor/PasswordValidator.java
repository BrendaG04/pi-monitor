package com.brenda.pimonitor;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
	Password strength validator ~ requirements to prevent weak passwords
*/


@Component
public class PasswordValidator {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 100;

    public ValidationResult validate(String password) {
        List<String> errors = new ArrayList<>();

        if (password == null || password.isEmpty()) {
            errors.add("Password is required");
            return new ValidationResult(false, errors);
        }

        // Length check
        if (password.length() < MIN_LENGTH) {
            errors.add("Password must be at least " + MIN_LENGTH + " characters");
        }

        if (password.length() > MAX_LENGTH) {
            errors.add("Password must not exceed " + MAX_LENGTH + " characters");
        }

        // Must contain uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            errors.add("Password must contain at least one uppercase letter");
        }

        // Must contain lowercase letter
        if (!password.matches(".*[a-z].*")) {
            errors.add("Password must contain at least one lowercase letter");
        }

        // Must contain digit
        if (!password.matches(".*\\d.*")) {
            errors.add("Password must contain at least one number");
        }

        // Must contain special character
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            errors.add("Password must contain at least one special character (!@#$%^&*()_+-=[]{}etc.)");
        }

        // Check for common weak passwords
        String lowerPassword = password.toLowerCase();
        if (lowerPassword.contains("password") || 
            lowerPassword.contains("12345") ||
            lowerPassword.equals("admin123")) {
            errors.add("Password is too common - please choose a stronger password");
        }

        boolean isValid = errors.isEmpty();
        return new ValidationResult(isValid, errors);
    }

    // Inner class for validation result
    public static class ValidationResult {
        private final boolean valid;
        private final List<String> errors;

        public ValidationResult(boolean valid, List<String> errors) {
            this.valid = valid;
            this.errors = errors;
        }

        public boolean isValid() {
            return valid;
        }

        public List<String> getErrors() {
            return errors;
        }

        public String getErrorMessage() {
            return String.join(". ", errors);
        }
    }
}
