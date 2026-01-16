package org.example.healthcaremanagementsystem.model;

import org.bson.Document;
import java.util.Date;

public class MedicalLog {
    private String id;
    private int patientId;
    private int doctorId;
    private String action; // e.g., "Consultation", "Prescription", "Note"
    private String content;
    private Date timestamp;

    public MedicalLog() {
    }

    public MedicalLog(int patientId, int doctorId, String action, String content, Date timestamp) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.action = action;
        this.content = content;
        this.timestamp = timestamp;
    }

    public MedicalLog(Document doc) {
        if (doc.getObjectId("_id") != null)
            this.id = doc.getObjectId("_id").toString();
        this.patientId = doc.getInteger("patientId");
        this.doctorId = doc.getInteger("doctorId");
        this.action = doc.getString("action");
        this.content = doc.getString("content");
        this.timestamp = doc.getDate("timestamp");
    }

    public Document toDocument() {
        return new Document("patientId", patientId)
                .append("doctorId", doctorId)
                .append("action", action)
                .append("content", content)
                .append("timestamp", timestamp);
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
