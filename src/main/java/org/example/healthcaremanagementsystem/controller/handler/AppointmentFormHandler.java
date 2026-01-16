package org.example.healthcaremanagementsystem.controller.handler;

import javafx.scene.control.*;
import org.example.healthcaremanagementsystem.model.Appointment;
import org.example.healthcaremanagementsystem.model.Patient;
import org.example.healthcaremanagementsystem.model.Doctor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Handles Appointment form operations (create, populate, clear).
 * Follows Single Responsibility Principle by managing only appointment form state.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class AppointmentFormHandler {
    
    private final TextField txtSelectedPatient;
    private final TextField txtSelectedDoctor;
    private final DatePicker datePickerAppointmentDate;
    private final ComboBox<String> comboAppointmentTime;
    private final ComboBox<String> comboAppointmentType;
    private final ComboBox<String> comboStatus;
    private final TextArea txtNotes;
    
    public AppointmentFormHandler(TextField txtSelectedPatient, TextField txtSelectedDoctor,
                                 DatePicker datePickerAppointmentDate, ComboBox<String> comboAppointmentTime,
                                 ComboBox<String> comboAppointmentType, ComboBox<String> comboStatus,
                                 TextArea txtNotes) {
        this.txtSelectedPatient = txtSelectedPatient;
        this.txtSelectedDoctor = txtSelectedDoctor;
        this.datePickerAppointmentDate = datePickerAppointmentDate;
        this.comboAppointmentTime = comboAppointmentTime;
        this.comboAppointmentType = comboAppointmentType;
        this.comboStatus = comboStatus;
        this.txtNotes = txtNotes;
    }
    
    /**
     * Creates an Appointment object from form fields.
     */
    public Appointment createAppointmentFromForm(Patient patient, Doctor doctor) {
        Appointment appointment = new Appointment();
        appointment.setPatientId(patient.getPatientId());
        appointment.setDoctorId(doctor.getDoctorId());

        LocalDate selectedDate = datePickerAppointmentDate.getValue();
        String selectedTime = comboAppointmentTime.getValue();
        LocalDateTime appointmentDateTime = LocalDateTime.of(selectedDate, LocalTime.parse(selectedTime));
        appointment.setAppointmentDate(appointmentDateTime);

        appointment.setAppointmentType(comboAppointmentType.getValue());
        appointment.setStatus(comboStatus.getValue());
        appointment.setNotes(txtNotes.getText().trim());

        return appointment;
    }
    
    /**
     * Populates form fields with appointment data.
     */
    public void populateForm(Appointment appointment, Patient patient, Doctor doctor) {
        if (patient != null) {
            txtSelectedPatient.setText(patient.getFullName() + " (ID: " + patient.getPatientId() + ")");
        }

        if (doctor != null) {
            txtSelectedDoctor.setText(doctor.getFullName() + " - " + doctor.getSpecialization());
        }

        if (appointment.getAppointmentDate() != null) {
            datePickerAppointmentDate.setValue(appointment.getAppointmentDate().toLocalDate());
            String time = appointment.getAppointmentDate().toLocalTime()
                .format(DateTimeFormatter.ofPattern("HH:mm"));
            comboAppointmentTime.setValue(time);
        }

        comboAppointmentType.setValue(appointment.getAppointmentType());
        comboStatus.setValue(appointment.getStatus());
        txtNotes.setText(appointment.getNotes());
    }
    
    /**
     * Clears all form fields.
     */
    public void clearForm() {
        FormHandler.clearTextFields(txtSelectedPatient, txtSelectedDoctor);
        FormHandler.clearTextAreas(txtNotes);
        FormHandler.clearDatePickers(datePickerAppointmentDate);
        FormHandler.clearComboBoxes(comboAppointmentTime, comboAppointmentType, comboStatus);
    }
    
    /**
     * Sets selected patient display.
     */
    public void setSelectedPatient(Patient patient) {
        if (patient != null) {
            txtSelectedPatient.setText(patient.getFullName() + " (ID: " + patient.getPatientId() + ")");
        }
    }
    
    /**
     * Sets selected doctor display.
     */
    public void setSelectedDoctor(Doctor doctor) {
        if (doctor != null) {
            txtSelectedDoctor.setText(doctor.getFullName() + " - " + doctor.getSpecialization());
        }
    }
}

