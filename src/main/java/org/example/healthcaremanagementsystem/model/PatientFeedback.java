package org.example.healthcaremanagementsystem.model;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing a PatientFeedback entity.
 * Manages patient feedback and ratings for services.
 * Follows DTO pattern for data transfer between layers.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class PatientFeedback {
    
    private Integer feedbackId;
    private Integer patientId;
    private Integer appointmentId;
    private Integer doctorId;
    private Integer rating; // 1-5 scale
    private String comment;
    private LocalDateTime feedbackDate;
    private String category; // Service, Treatment, Facility, etc.
    
    /**
     * Default constructor.
     */
    public PatientFeedback() {
    }
    
    /**
     * Parameterized constructor for creating new feedback.
     * 
     * @param patientId ID of the patient providing feedback
     * @param appointmentId ID of the associated appointment
     * @param doctorId ID of the doctor being rated
     * @param rating Rating score (1-5)
     * @param comment Feedback comment
     * @param category Category of feedback
     */
    public PatientFeedback(Integer patientId, Integer appointmentId,
                           Integer doctorId, Integer rating, String comment,
                           String category) {
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.rating = rating;
        this.comment = comment;
        this.category = category;
    }
    
    /**
     * Full constructor including feedback ID (for existing records).
     * 
     * @param feedbackId Unique feedback identifier
     * @param patientId ID of the patient providing feedback
     * @param appointmentId ID of the associated appointment
     * @param doctorId ID of the doctor being rated
     * @param rating Rating score (1-5)
     * @param comment Feedback comment
     * @param feedbackDate Date and time when feedback was submitted
     * @param category Category of feedback
     */
    public PatientFeedback(Integer feedbackId, Integer patientId,
                           Integer appointmentId, Integer doctorId,
                           Integer rating, String comment,
                           LocalDateTime feedbackDate, String category) {
        this.feedbackId = feedbackId;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.rating = rating;
        this.comment = comment;
        this.feedbackDate = feedbackDate;
        this.category = category;
    }
    
    // Getters and Setters
    
    public Integer getFeedbackId() {
        return feedbackId;
    }
    
    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }
    
    public Integer getPatientId() {
        return patientId;
    }
    
    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }
    
    public Integer getAppointmentId() {
        return appointmentId;
    }
    
    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }
    
    public Integer getDoctorId() {
        return doctorId;
    }
    
    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public LocalDateTime getFeedbackDate() {
        return feedbackDate;
    }
    
    public void setFeedbackDate(LocalDateTime feedbackDate) {
        this.feedbackDate = feedbackDate;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    @Override
    public String toString() {
        return "PatientFeedback{" +
                "feedbackId=" + feedbackId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", rating=" + rating +
                ", category='" + category + '\'' +
                ", feedbackDate=" + feedbackDate +
                '}';
    }
}

