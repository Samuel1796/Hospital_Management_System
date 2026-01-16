package org.example.healthcaremanagementsystem.controller.validator;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

/**
 * Validator specifically for Doctor form fields.
 * Follows Single Responsibility Principle by handling only doctor validation.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class DoctorValidator {
    
    /**
     * Validates doctor form fields.
     * 
     * @param firstName First name field
     * @param lastName Last name field
     * @param email Email field
     * @param phoneNumber Phone number field
     * @param specialization Specialization combo box
     * @param department Department combo box
     * @param licenseNumber License number field
     * @param status Status combo box
     * @return ValidationResult containing validation status and error message
     */
    public static PatientValidator.ValidationResult validateDoctorForm(
            TextField firstName, TextField lastName, TextField email, TextField phoneNumber,
            ComboBox<String> specialization, ComboBox<?> department, 
            TextField licenseNumber, ComboBox<String> status) {
        
        // Validate first name
        if (!InputValidator.isNotEmpty(firstName.getText())) {
            return new PatientValidator.ValidationResult(false, "First name is required.", firstName);
        }
        
        // Validate last name
        if (!InputValidator.isNotEmpty(lastName.getText())) {
            return new PatientValidator.ValidationResult(false, "Last name is required.", lastName);
        }
        
        // Validate email
        if (!InputValidator.isNotEmpty(email.getText())) {
            return new PatientValidator.ValidationResult(false, "Email is required.", email);
        }
        
        if (!InputValidator.isValidEmail(email.getText().trim())) {
            return new PatientValidator.ValidationResult(false, 
                "Please enter a valid email address.", email);
        }
        
        // Validate phone number
        if (!InputValidator.isNotEmpty(phoneNumber.getText())) {
            return new PatientValidator.ValidationResult(false, "Phone number is required.", phoneNumber);
        }
        
        if (!InputValidator.isValidPhoneNumber(phoneNumber.getText().trim())) {
            return new PatientValidator.ValidationResult(false, 
                "Please enter a valid Ghanaian phone number (e.g., 0244XXXXXX).", phoneNumber);
        }
        
        // Validate specialization
        if (specialization.getValue() == null || specialization.getValue().isEmpty()) {
            return new PatientValidator.ValidationResult(false, 
                "Specialization is required.", specialization);
        }
        
        // Validate department
        if (department.getValue() == null) {
            return new PatientValidator.ValidationResult(false, 
                "Department is required.", department);
        }
        
        // Validate license number
        if (!InputValidator.isNotEmpty(licenseNumber.getText())) {
            return new PatientValidator.ValidationResult(false, 
                "License number is required.", licenseNumber);
        }
        
        // Validate status
        if (status.getValue() == null || status.getValue().isEmpty()) {
            return new PatientValidator.ValidationResult(false, 
                "Status is required.", status);
        }
        
        return new PatientValidator.ValidationResult(true, null, null);
    }
    
    /**
     * Sets up real-time validation listeners for doctor form fields.
     * 
     * @param email Email text field
     * @param phoneNumber Phone number text field
     */
    public static void setupRealTimeValidation(TextField email, TextField phoneNumber) {
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
    }
}

