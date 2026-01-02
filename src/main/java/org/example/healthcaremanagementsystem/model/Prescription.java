package org.example.healthcaremanagementsystem.model;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a Prescription entity.
 * Manages prescription information and follows DTO pattern.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class Prescription {
    
    private Integer prescriptionId;
    private Integer patientId;
    private Integer doctorId;
    private Integer appointmentId;
    private LocalDate prescriptionDate;
    private String diagnosis;
    private String instructions;
    private LocalDate validUntil;
    
    /**
     * Default constructor.
     */
    public Prescription() {
    }
    
    /**
     * Parameterized constructor for creating a new prescription.
     * 
     * @param patientId ID of the patient
     * @param doctorId ID of the prescribing doctor
     * @param appointmentId ID of the associated appointment
     * @param prescriptionDate Date when prescription was issued
     * @param diagnosis Patient's diagnosis
     * @param instructions Prescription instructions
     * @param validUntil Date until which prescription is valid
     */
    public Prescription(Integer patientId, Integer doctorId, Integer appointmentId,
                        LocalDate prescriptionDate, String diagnosis,
                        String instructions, LocalDate validUntil) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.prescriptionDate = prescriptionDate;
        this.diagnosis = diagnosis;
        this.instructions = instructions;
        this.validUntil = validUntil;
    }
    
    /**
     * Full constructor including prescription ID (for existing records).
     * 
     * @param prescriptionId Unique prescription identifier
     * @param patientId ID of the patient
     * @param doctorId ID of the prescribing doctor
     * @param appointmentId ID of the associated appointment
     * @param prescriptionDate Date when prescription was issued
     * @param diagnosis Patient's diagnosis
     * @param instructions Prescription instructions
     * @param validUntil Date until which prescription is valid
     */
    public Prescription(Integer prescriptionId, Integer patientId, Integer doctorId,
                        Integer appointmentId, LocalDate prescriptionDate,
                        String diagnosis, String instructions, LocalDate validUntil) {
        this.prescriptionId = prescriptionId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.prescriptionDate = prescriptionDate;
        this.diagnosis = diagnosis;
        this.instructions = instructions;
        this.validUntil = validUntil;
    }
    
    // Getters and Setters
    
    public Integer getPrescriptionId() {
        return prescriptionId;
    }
    
    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
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
    
    public Integer getAppointmentId() {
        return appointmentId;
    }
    
    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }
    
    public LocalDate getPrescriptionDate() {
        return prescriptionDate;
    }
    
    public void setPrescriptionDate(LocalDate prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }
    
    public String getDiagnosis() {
        return diagnosis;
    }
    
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    
    public String getInstructions() {
        return instructions;
    }
    
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    
    public LocalDate getValidUntil() {
        return validUntil;
    }
    
    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }
    
    @Override
    public String toString() {
        return "Prescription{" +
                "prescriptionId=" + prescriptionId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", prescriptionDate=" + prescriptionDate +
                ", diagnosis='" + diagnosis + '\'' +
                '}';
    }
}

