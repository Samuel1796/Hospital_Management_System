package org.example.healthcaremanagementsystem.controller.validator;

import javafx.scene.control.Control;

/**
 * Input validation utility class.
 * Follows Single Responsibility Principle by handling only input validation logic.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class InputValidator {
    
    /**
     * Validates email format using regex pattern.
     * 
     * @param email Email address to validate
     * @return true if valid email format, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    
    /**
     * Validates Ghanaian phone number format.
     * Accepts formats: 0244XXXXXX, 020XXXXXXX, 050XXXXXXX, 0302XXXXXX
     * 
     * @param phone Phone number to validate
     * @return true if valid phone format, false otherwise
     */
    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        // Remove spaces and dashes
        String cleaned = phone.replaceAll("[\\s-]", "");
        // Ghanaian mobile numbers: 0244XXXXXX, 020XXXXXXX, 050XXXXXXX, etc.
        // Landline: 0302XXXXXX
        String phoneRegex = "^(0[2-5]\\d{8}|030\\d{7})$";
        return cleaned.matches(phoneRegex);
    }
    
    /**
     * Validates that a string is not null or empty.
     * 
     * @param value String to validate
     * @return true if not null and not empty, false otherwise
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    /**
     * Applies visual validation feedback to a control.
     * Sets red border if invalid, clears style if valid.
     * 
     * @param control Control to apply validation feedback to
     * @param isValid Whether the control value is valid
     */
    public static void applyValidationStyle(Control control, boolean isValid) {
        if (isValid) {
            control.setStyle("");
        } else {
            control.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
        }
    }
}

