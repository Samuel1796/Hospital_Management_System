package org.example.healthcaremanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.example.healthcaremanagementsystem.dao.MedicalLogDAO;
import org.example.healthcaremanagementsystem.dao.PrescriptionDAO;
import org.example.healthcaremanagementsystem.dao.PrescriptionDAOImpl;
import org.example.healthcaremanagementsystem.model.MedicalLog;
import org.example.healthcaremanagementsystem.model.Prescription;

import java.util.List;
import java.util.stream.Collectors;

public class HistoryController {

    @FXML
    private Label lblTitle;
    @FXML
    private ListView<String> listLogs;
    @FXML
    private ListView<String> listPrescriptions;

    private final MedicalLogDAO medicalLogDAO;
    private final PrescriptionDAO prescriptionDAO;

    public HistoryController() {
        this.medicalLogDAO = new MedicalLogDAO();
        this.prescriptionDAO = new PrescriptionDAOImpl();
    }

    public void initData(int entityId, String entityType) {
        if ("PATIENT".equals(entityType)) {
            lblTitle.setText("Patient History (ID: " + entityId + ")");
            loadPatientHistory(entityId);
        } else if ("DOCTOR".equals(entityType)) {
            lblTitle.setText("Doctor History (ID: " + entityId + ")");
            loadDoctorHistory(entityId);
        }
    }

    private void loadPatientHistory(int patientId) {
        // Load NoSQL Logs
        List<MedicalLog> logs = medicalLogDAO.getLogsByPatientId(patientId);
        List<String> logStrings = logs.stream()
                .map(log -> "[" + log.getTimestamp() + "] " + log.getAction() + ": " + log.getContent())
                .collect(Collectors.toList());
        listLogs.setItems(FXCollections.observableArrayList(logStrings));

        // Load SQL Prescriptions
        List<Prescription> prescriptions = prescriptionDAO.getPrescriptionsByPatientId(patientId);
        List<String> prescriptionStrings = prescriptions.stream()
                .map(p -> "[" + p.getPrescriptionDate() + "] Dr. " + p.getDoctorId() + ": " + p.getInstructions())
                .collect(Collectors.toList());
        listPrescriptions.setItems(FXCollections.observableArrayList(prescriptionStrings));
    }

    private void loadDoctorHistory(int doctorId) {
        // Load NoSQL Logs involved with this doctor
        List<MedicalLog> logs = medicalLogDAO.getLogsByDoctorId(doctorId);
        List<String> logStrings = logs.stream()
                .map(log -> "[" + log.getTimestamp() + "] Patient " + log.getPatientId() + " - " + log.getAction()
                        + ": " + log.getContent())
                .collect(Collectors.toList());
        listLogs.setItems(FXCollections.observableArrayList(logStrings));

        // Load SQL Prescriptions issued by this doctor
        List<Prescription> prescriptions = prescriptionDAO.getPrescriptionsByDoctorId(doctorId);
        List<String> prescriptionStrings = prescriptions.stream()
                .map(p -> "[" + p.getPrescriptionDate() + "] Patient " + p.getPatientId() + ": " + p.getInstructions())
                .collect(Collectors.toList());
        listPrescriptions.setItems(FXCollections.observableArrayList(prescriptionStrings));
    }
}
