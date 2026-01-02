package org.example.healthcaremanagementsystem.model;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing an Appointment entity.
 * Manages patient-doctor appointment scheduling and follows DTO pattern.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class Appointment {
    
    private Integer appointmentId;
    private Integer patientId;
    private Integer doctorId;
    private LocalDateTime appointmentDate;
    private String appointmentType; // Consultation, Follow-up, Emergency, etc.
    private String status; // Scheduled, Completed, Cancelled, No-show
    private String notes;
    private LocalDateTime createdAt;
    
    /**
     * Default constructor.
     */
    public Appointment() {
    }
    
    /**
     * Parameterized constructor for creating a new appointment.
     * 
     * @param patientId ID of the patient
     * @param doctorId ID of the doctor
     * @param appointmentDate Date and time of the appointment
     * @param appointmentType Type of appointment
     * @param status Current status of the appointment
     * @param notes Additional notes about the appointment
     */
    public Appointment(Integer patientId, Integer doctorId,
                       LocalDateTime appointmentDate, String appointmentType,
                       String status, String notes) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.appointmentType = appointmentType;
        this.status = status;
        this.notes = notes;
    }
    
    /**
     * Full constructor including appointment ID (for existing records).
     * 
     * @param appointmentId Unique appointment identifier
     * @param patientId ID of the patient
     * @param doctorId ID of the doctor
     * @param appointmentDate Date and time of the appointment
     * @param appointmentType Type of appointment
     * @param status Current status of the appointment
     * @param notes Additional notes about the appointment
     * @param createdAt Timestamp when appointment was created
     */
    public Appointment(Integer appointmentId, Integer patientId, Integer doctorId,
                       LocalDateTime appointmentDate, String appointmentType,
                       String status, String notes, LocalDateTime createdAt) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.appointmentType = appointmentType;
        this.status = status;
        this.notes = notes;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    
    public Integer getAppointmentId() {
        return appointmentId;
    }
    
    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }
    
    public Integer getPatientId() {
        return patientId;
    }
    
    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }
    
    public Integer getDoctorId() {
        return doctorId;
    }
    
    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }
    
    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }
    
    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    
    public String getAppointmentType() {
        return appointmentType;
    }
    
    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", appointmentDate=" + appointmentDate +
                ", appointmentType='" + appointmentType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

