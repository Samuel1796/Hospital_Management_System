package org.example.healthcaremanagementsystem.controller.handler;

import javafx.scene.control.*;
import org.example.healthcaremanagementsystem.model.Doctor;
import org.example.healthcaremanagementsystem.model.Department;

/**
 * Handles Doctor form operations (create, populate, clear).
 * Follows Single Responsibility Principle by managing only doctor form state.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class DoctorFormHandler {
    
    private final TextField txtFirstName;
    private final TextField txtLastName;
    private final TextField txtEmail;
    private final TextField txtPhoneNumber;
    private final ComboBox<String> comboSpecialization;
    private final ComboBox<Department> comboDepartment;
    private final TextField txtLicenseNumber;
    private final DatePicker datePickerHireDate;
    private final ComboBox<String> comboStatus;
    
    public DoctorFormHandler(TextField txtFirstName, TextField txtLastName,
                             TextField txtEmail, TextField txtPhoneNumber,
                             ComboBox<String> comboSpecialization, ComboBox<Department> comboDepartment,
                             TextField txtLicenseNumber, DatePicker datePickerHireDate,
                             ComboBox<String> comboStatus) {
        this.txtFirstName = txtFirstName;
        this.txtLastName = txtLastName;
        this.txtEmail = txtEmail;
        this.txtPhoneNumber = txtPhoneNumber;
        this.comboSpecialization = comboSpecialization;
        this.comboDepartment = comboDepartment;
        this.txtLicenseNumber = txtLicenseNumber;
        this.datePickerHireDate = datePickerHireDate;
        this.comboStatus = comboStatus;
    }
    
    /**
     * Creates a Doctor object from form fields.
     */
    public Doctor createDoctorFromForm() {
        Doctor doctor = new Doctor();
        doctor.setFirstName(txtFirstName.getText().trim());
        doctor.setLastName(txtLastName.getText().trim());
        doctor.setEmail(txtEmail.getText().trim());
        doctor.setPhoneNumber(txtPhoneNumber.getText().trim());
        doctor.setSpecialization(comboSpecialization.getValue());
        doctor.setDepartmentId(comboDepartment.getValue().getDepartmentId());
        doctor.setLicenseNumber(txtLicenseNumber.getText().trim());
        doctor.setHireDate(datePickerHireDate.getValue());
        doctor.setStatus(comboStatus.getValue());
        return doctor;
    }
    
    /**
     * Populates form fields with doctor data.
     */
    public void populateForm(Doctor doctor, Department department) {
        txtFirstName.setText(doctor.getFirstName());
        txtLastName.setText(doctor.getLastName());
        txtEmail.setText(doctor.getEmail());
        txtPhoneNumber.setText(doctor.getPhoneNumber());
        comboSpecialization.setValue(doctor.getSpecialization());
        txtLicenseNumber.setText(doctor.getLicenseNumber());
        datePickerHireDate.setValue(doctor.getHireDate());
        comboStatus.setValue(doctor.getStatus());
        if (department != null) {
            comboDepartment.setValue(department);
        }
    }
    
    /**
     * Clears all form fields.
     */
    public void clearForm() {
        FormHandler.clearTextFields(txtFirstName, txtLastName, txtEmail, txtPhoneNumber, txtLicenseNumber);
        FormHandler.clearDatePickers(datePickerHireDate);
        FormHandler.clearComboBoxes(comboSpecialization, comboDepartment, comboStatus);
    }
}

