package org.example.healthcaremanagementsystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main controller for the Healthcare Management System application.
 * Handles navigation between different modules.
 * Follows Single Responsibility Principle by managing only navigation logic.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class MainController {
    
    @FXML
    private Button btnPatientManagement;
    
    @FXML
    private Button btnDoctorManagement;
    
    @FXML
    private Button btnAppointmentManagement;
    
    @FXML
    private Button btnViewReports;
    
    /**
     * Initializes the controller.
     * Called automatically by JavaFX after FXML loading.
     */
    @FXML
    private void initialize() {
        // Setup button actions
        setupButtonActions();
    }
    
    /**
     * Sets up event handlers for navigation buttons.
     */
    private void setupButtonActions() {
        // Button actions are handled directly via FXML onAction attributes
        // This method is kept for potential programmatic setup if needed
    }
    
    /**
     * Opens the Patient Management module.
     * Called from FXML onAction attribute.
     */
    @FXML
    public void openPatientManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthcaremanagementsystem/patient-management.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Patient Management");
            stage.setScene(new Scene(root, 1000, 700));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Opens the Doctor Management module.
     * Called from FXML onAction attribute.
     */
    @FXML
    public void openDoctorManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthcaremanagementsystem/doctor-management.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Doctor Management");
            stage.setScene(new Scene(root, 1200, 700));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Opens the Appointment Management module.
     * Called from FXML onAction attribute.
     */
    @FXML
    public void openAppointmentManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthcaremanagementsystem/appointment-management.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Appointment Management");
            stage.setScene(new Scene(root, 1000, 700));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Opens the Reports module.
     * Called from FXML onAction attribute.
     */
    @FXML
    public void openReports() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthcaremanagementsystem/performance-analytics.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Performance & Analytics");
            stage.setScene(new Scene(root, 1200, 800));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

