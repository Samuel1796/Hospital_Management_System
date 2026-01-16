package org.example.healthcaremanagementsystem.controller.validator;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Validator specifically for Appointment form fields.
 * Follows Single Responsibility Principle by handling only appointment validation.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class AppointmentValidator {
    
    /**
     * Validates appointment form fields.
     * 
     * @param patientSelected Whether a patient is selected
     * @param doctorSelected Whether a doctor is selected
     * @param appointmentDate Date picker for appointment date
     * @param appointmentTime Time combo box
     * @param appointmentType Appointment type combo box
     * @param status Status combo box
     * @return ValidationResult containing validation status and error message
     */
    public static PatientValidator.ValidationResult validateAppointmentForm(
            boolean patientSelected, boolean doctorSelected,
            DatePicker appointmentDate, ComboBox<String> appointmentTime,
            ComboBox<String> appointmentType, ComboBox<String> status) {
        
        // Validate patient selection
        if (!patientSelected) {
            return new PatientValidator.ValidationResult(false, 
                "Please select a patient.", null);
        }
        
        // Validate doctor selection
        if (!doctorSelected) {
            return new PatientValidator.ValidationResult(false, 
                "Please select a doctor.", null);
        }
        
        // Validate appointment date
        if (appointmentDate.getValue() == null) {
            return new PatientValidator.ValidationResult(false, 
                "Appointment date is required.", appointmentDate);
        }
        
        // Validate date is not in the past
        if (appointmentDate.getValue().isBefore(LocalDate.now())) {
            return new PatientValidator.ValidationResult(false, 
                "Appointment date cannot be in the past.", appointmentDate);
        }
        
        // Validate time selection
        if (appointmentTime.getValue() == null || appointmentTime.getValue().isEmpty()) {
            return new PatientValidator.ValidationResult(false, 
                "Appointment time is required.", appointmentTime);
        }
        
        // Validate appointment type
        if (appointmentType.getValue() == null || appointmentType.getValue().isEmpty()) {
            return new PatientValidator.ValidationResult(false, 
                "Appointment type is required.", appointmentType);
        }
        
        // Validate status
        if (status.getValue() == null || status.getValue().isEmpty()) {
            return new PatientValidator.ValidationResult(false, 
                "Appointment status is required.", status);
        }
        
        // Validate date and time combination is not in the past
        LocalDate selectedDate = appointmentDate.getValue();
        String selectedTime = appointmentTime.getValue();
        try {
            LocalDateTime appointmentDateTime = LocalDateTime.of(selectedDate, 
                LocalTime.parse(selectedTime));
            
            if (appointmentDateTime.isBefore(LocalDateTime.now())) {
                return new PatientValidator.ValidationResult(false, 
                    "Appointment date and time cannot be in the past.", appointmentDate);
            }
        } catch (Exception e) {
            return new PatientValidator.ValidationResult(false, 
                "Invalid time format.", appointmentTime);
        }
        
        return new PatientValidator.ValidationResult(true, null, null);
    }
    
    /**
     * Sets up real-time validation for appointment date.
     * 
     * @param appointmentDate Date picker to validate
     */
    public static void setupRealTimeValidation(DatePicker appointmentDate) {
        appointmentDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.isBefore(LocalDate.now())) {
                InputValidator.applyValidationStyle(appointmentDate, false);
            } else {
                InputValidator.applyValidationStyle(appointmentDate, true);
            }
        });
    }
}

