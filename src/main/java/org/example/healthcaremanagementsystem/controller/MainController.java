package org.example.healthcaremanagementsystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.healthcaremanagementsystem.dao.*;

import java.io.IOException;

/**
 * Main controller for the Healthcare Management System application.
 * Handles navigation between different modules and displays dashboard statistics.
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
    
    @FXML
    private Label lblPatientCount;
    
    @FXML
    private Label lblDoctorCount;
    
    @FXML
    private Label lblAppointmentCount;
    
    private final PatientDAO patientDAO;
    private final DoctorDAO doctorDAO;
    private final AppointmentDAO appointmentDAO;
    
    public MainController() {
        this.patientDAO = new PatientDAOImpl();
        this.doctorDAO = new DoctorDAOImpl();
        this.appointmentDAO = new AppointmentDAOImpl();
    }
    
    /**
     * Initializes the controller.
     * Called automatically by JavaFX after FXML loading.
     */
    @FXML
    private void initialize() {
        loadDashboardStatistics();
    }
    
    /**
     * Loads statistics from the database and updates the dashboard labels.
     */
    private void loadDashboardStatistics() {
        try {
            // Load patient count
            int patientCount = patientDAO.getCount();
            if (lblPatientCount != null) {
                lblPatientCount.setText(String.valueOf(patientCount));
            }
            
            // Load active doctor count
            int doctorCount = doctorDAO.getActiveCount();
            if (lblDoctorCount != null) {
                lblDoctorCount.setText(String.valueOf(doctorCount));
            }
            
            // Load appointments this month
            int appointmentCount = appointmentDAO.getCountThisMonth();
            if (lblAppointmentCount != null) {
                lblAppointmentCount.setText(String.valueOf(appointmentCount));
            }
            
        } catch (Exception e) {
            System.err.println("Error loading dashboard statistics: " + e.getMessage());
            // Set default values on error
            if (lblPatientCount != null) lblPatientCount.setText("--");
            if (lblDoctorCount != null) lblDoctorCount.setText("--");
            if (lblAppointmentCount != null) lblAppointmentCount.setText("--");
        }
    }
    
    /**
     * Refreshes the dashboard statistics.
     */
    @FXML
    public void refreshDashboard() {
        loadDashboardStatistics();
    }
    
    /**
     * Opens the Patient Management module.
     */
    @FXML
    public void openPatientManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthcaremanagementsystem/patient-management.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Patient Management");
            stage.setScene(new Scene(root, 1200, 600));
            stage.setOnHidden(e -> loadDashboardStatistics()); // Refresh on close
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Opens the Doctor Management module.
     */
    @FXML
    public void openDoctorManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthcaremanagementsystem/doctor-management.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Doctor Management");
            stage.setScene(new Scene(root, 1200, 600));
            stage.setOnHidden(e -> loadDashboardStatistics()); // Refresh on close
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Opens the Appointment Management module.
     */
    @FXML
    public void openAppointmentManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthcaremanagementsystem/appointment-management.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Appointment Management");
            stage.setScene(new Scene(root, 1200, 600));
            stage.setOnHidden(e -> loadDashboardStatistics()); // Refresh on close
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Opens the Reports module.
     */
    @FXML
    public void openReports() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthcaremanagementsystem/performance-analytics.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Performance & Analytics");
            stage.setScene(new Scene(root, 1200, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
