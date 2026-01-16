package org.example.healthcaremanagementsystem.controller.validator;

import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

/**
 * Validator specifically for Patient form fields.
 * Follows Single Responsibility Principle by handling only patient validation.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class PatientValidator {
    
    /**
     * Validates patient form fields.
     * 
     * @param firstName First name field
     * @param lastName Last name field
     * @param email Email field
     * @param phoneNumber Phone number field
     * @param dateOfBirth Date of birth picker
     * @return ValidationResult containing validation status and error message
     */
    public static ValidationResult validatePatientForm(TextField firstName, TextField lastName,
                                                       TextField email, TextField phoneNumber,
                                                       DatePicker dateOfBirth) {
        // Validate first name
        if (!InputValidator.isNotEmpty(firstName.getText())) {
            return new ValidationResult(false, "First name is required.", firstName);
        }
        
        // Validate last name
        if (!InputValidator.isNotEmpty(lastName.getText())) {
            return new ValidationResult(false, "Last name is required.", lastName);
        }
        
        // Validate email
        if (!InputValidator.isNotEmpty(email.getText())) {
            return new ValidationResult(false, "Email is required.", email);
        }
        
        if (!InputValidator.isValidEmail(email.getText().trim())) {
            return new ValidationResult(false, "Please enter a valid email address.", email);
        }
        
        // Validate phone number
        if (!InputValidator.isNotEmpty(phoneNumber.getText())) {
            return new ValidationResult(false, "Phone number is required.", phoneNumber);
        }
        
        if (!InputValidator.isValidPhoneNumber(phoneNumber.getText().trim())) {
            return new ValidationResult(false, 
                "Please enter a valid Ghanaian phone number (e.g., 0244XXXXXX).", phoneNumber);
        }
        
        // Validate date of birth
        if (dateOfBirth.getValue() == null) {
            return new ValidationResult(false, "Date of birth is required.", dateOfBirth);
        }
        
        if (dateOfBirth.getValue().isAfter(LocalDate.now())) {
            return new ValidationResult(false, "Date of birth cannot be in the future.", dateOfBirth);
        }
        
        return new ValidationResult(true, null, null);
    }
    
    /**
     * Sets up real-time validation listeners for patient form fields.
     * 
     * @param email Email text field
     * @param phoneNumber Phone number text field
     * @param emergencyPhone Emergency phone text field (optional)
     */
    public static void setupRealTimeValidation(TextField email, TextField phoneNumber, 
                                               TextField emergencyPhone) {
        // Email validation
        email.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isValid = newValue == null || newValue.isEmpty() || 
                            InputValidator.isValidEmail(newValue);
            InputValidator.applyValidationStyle(email, isValid);
        });
        
        // Phone number validation
        phoneNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isValid = newValue == null || newValue.isEmpty() || 
                            InputValidator.isValidPhoneNumber(newValue);
            InputValidator.applyValidationStyle(phoneNumber, isValid);
        });
        
        // Emergency phone validation (if provided)
        if (emergencyPhone != null) {
            emergencyPhone.textProperty().addListener((observable, oldValue, newValue) -> {
                boolean isValid = newValue == null || newValue.isEmpty() || 
                                InputValidator.isValidPhoneNumber(newValue);
                InputValidator.applyValidationStyle(emergencyPhone, isValid);
            });
        }
    }
    
    /**
     * Result class for validation operations.
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;
        private final Control focusControl;
        
        public ValidationResult(boolean valid, String errorMessage, Control focusControl) {
            this.valid = valid;
            this.errorMessage = errorMessage;
            this.focusControl = focusControl;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
        
        public Control getFocusControl() {
            return focusControl;
        }
    }
}

