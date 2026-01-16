package org.example.healthcaremanagementsystem.controller.handler;

import javafx.scene.control.*;
import org.example.healthcaremanagementsystem.model.Patient;

/**
 * Handles Patient form operations (create, populate, clear).
 * Follows Single Responsibility Principle by managing only patient form state.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class PatientFormHandler {
    
    private final TextField txtFirstName;
    private final TextField txtLastName;
    private final TextField txtEmail;
    private final TextField txtPhoneNumber;
    private final DatePicker datePickerDateOfBirth;
    private final TextArea txtAddress;
    private final ComboBox<String> comboGender;
    private final ComboBox<String> comboBloodGroup;
    private final TextField txtEmergencyContact;
    private final TextField txtEmergencyPhone;
    
    public PatientFormHandler(TextField txtFirstName, TextField txtLastName,
                             TextField txtEmail, TextField txtPhoneNumber,
                             DatePicker datePickerDateOfBirth, TextArea txtAddress,
                             ComboBox<String> comboGender, ComboBox<String> comboBloodGroup,
                             TextField txtEmergencyContact, TextField txtEmergencyPhone) {
        this.txtFirstName = txtFirstName;
        this.txtLastName = txtLastName;
        this.txtEmail = txtEmail;
        this.txtPhoneNumber = txtPhoneNumber;
        this.datePickerDateOfBirth = datePickerDateOfBirth;
        this.txtAddress = txtAddress;
        this.comboGender = comboGender;
        this.comboBloodGroup = comboBloodGroup;
        this.txtEmergencyContact = txtEmergencyContact;
        this.txtEmergencyPhone = txtEmergencyPhone;
    }
    
    /**
     * Creates a Patient object from form fields.
     */
    public Patient createPatientFromForm() {
        Patient patient = new Patient();
        patient.setFirstName(txtFirstName.getText().trim());
        patient.setLastName(txtLastName.getText().trim());
        patient.setEmail(txtEmail.getText().trim());
        patient.setPhoneNumber(txtPhoneNumber.getText().trim());
        patient.setDateOfBirth(datePickerDateOfBirth.getValue());
        patient.setAddress(txtAddress.getText().trim());
        patient.setGender(comboGender.getValue());
        patient.setBloodGroup(comboBloodGroup.getValue());
        patient.setEmergencyContact(txtEmergencyContact.getText().trim());
        patient.setEmergencyPhone(txtEmergencyPhone.getText().trim());
        return patient;
    }
    
    /**
     * Populates form fields with patient data.
     */
    public void populateForm(Patient patient) {
        txtFirstName.setText(patient.getFirstName());
        txtLastName.setText(patient.getLastName());
        txtEmail.setText(patient.getEmail());
        txtPhoneNumber.setText(patient.getPhoneNumber());
        datePickerDateOfBirth.setValue(patient.getDateOfBirth());
        txtAddress.setText(patient.getAddress());
        comboGender.setValue(patient.getGender());
        comboBloodGroup.setValue(patient.getBloodGroup());
        txtEmergencyContact.setText(patient.getEmergencyContact());
        txtEmergencyPhone.setText(patient.getEmergencyPhone());
    }
    
    /**
     * Clears all form fields.
     */
    public void clearForm() {
        FormHandler.clearTextFields(txtFirstName, txtLastName, txtEmail, txtPhoneNumber,
            txtEmergencyContact, txtEmergencyPhone);
        FormHandler.clearTextAreas(txtAddress);
        FormHandler.clearDatePickers(datePickerDateOfBirth);
        FormHandler.clearComboBoxes(comboGender, comboBloodGroup);
    }
}

