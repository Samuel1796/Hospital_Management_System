package org.example.healthcaremanagementsystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.healthcaremanagementsystem.dao.*;

import java.io.IOException;

/**
 * Main controller for the Healthcare Management System application.
 * Handles navigation between different modules and displays dashboard
 * statistics.
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
    private Button btnInventory;

    @FXML
    private Button btnSystemLogs;

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
        org.example.healthcaremanagementsystem.util.SystemLogger.getInstance().log("SYSTEM",
                "Dashboard initialized and statistics loaded.");
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
            if (lblPatientCount != null)
                lblPatientCount.setText("--");
            if (lblDoctorCount != null)
                lblDoctorCount.setText("--");
            if (lblAppointmentCount != null)
                lblAppointmentCount.setText("--");
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
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/healthcaremanagementsystem/patient-management.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Patient Management");
            stage.setScene(new Scene(root, 1100, 600));
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
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/healthcaremanagementsystem/doctor-management.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Doctor Management");
            stage.setScene(new Scene(root, 1100, 600));
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
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/healthcaremanagementsystem/appointment-management.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Appointment Management");
            stage.setScene(new Scene(root, 1100, 600));
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
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/healthcaremanagementsystem/performance-analytics.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Performance & Analytics");
            stage.setScene(new Scene(root, 1100, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the Inventory Management module.
     */
    @FXML
    public void openInventory() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/healthcaremanagementsystem/inventory.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Inventory Management");
            stage.setScene(new Scene(root, 1100, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the System Logs module.
     */
    @FXML
    public void openSystemLogs() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/healthcaremanagementsystem/system-logs.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("System Logs");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the Patient History Dashboard.
     */
    @FXML
    public void openPatientHistoryDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/healthcaremanagementsystem/patient-history-dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Patient History Dashboard");
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Patient History Dashboard: " + e.getMessage());
        }
    }

    /**
     * Opens the Doctor History Dashboard.
     */
    @FXML
    public void openDoctorHistoryDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/healthcaremanagementsystem/doctor-history-dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Doctor History Dashboard");
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Doctor History Dashboard: " + e.getMessage());
        }
    }

    /**
     * Opens the Clinical Consultation Dashboard.
     */
    @FXML
    public void openConsultationDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/healthcaremanagementsystem/consultation-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Clinical Consultation Dashboard");
            stage.setScene(new Scene(root, 1000, 700));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Clinical Consultation Dashboard: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
