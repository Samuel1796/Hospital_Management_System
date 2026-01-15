package org.example.healthcaremanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.example.healthcaremanagementsystem.dao.*;
import org.example.healthcaremanagementsystem.model.*;
import org.example.healthcaremanagementsystem.util.SystemLogger;

import java.time.LocalDate;
import java.util.List;

public class AppointmentDetailsController {

    @FXML
    private Label lblAppointmentId;
    @FXML
    private Label lblPatientName;
    @FXML
    private Label lblDoctorName;
    @FXML
    private Label lblDateTime;
    @FXML
    private Label lblType;
    @FXML
    private Label lblStatus;

    // Updated to use ComboBox for Inventory
    @FXML
    private ComboBox<Inventory> comboMedication;
    @FXML
    private TextField txtDosage;
    @FXML
    private TextField txtFrequency;
    @FXML
    private TextField txtDuration;

    @FXML
    private TextArea txtNotes;
    @FXML
    private Button btnComplete;

    private Appointment currentAppointment;
    private final AppointmentDAO appointmentDAO;
    private final PrescriptionDAO prescriptionDAO;
    private final MedicalLogDAO medicalLogDAO;
    private final InventoryDAO inventoryDAO; // Added InventoryDAO

    public AppointmentDetailsController() {
        this.appointmentDAO = new AppointmentDAOImpl();
        this.prescriptionDAO = new PrescriptionDAOImpl();
        this.medicalLogDAO = new MedicalLogDAO();
        this.inventoryDAO = new InventoryDAOImpl();
    }

    public void setAppointment(Appointment appointment) {
        this.currentAppointment = appointment;
        updateUI();
        loadMedications(); // Load inventory items
    }

    private void updateUI() {
        if (currentAppointment == null)
            return;

        lblAppointmentId.setText("ID: #" + currentAppointment.getAppointmentId());
        lblPatientName.setText("Patient ID: " + currentAppointment.getPatientId());
        lblDoctorName.setText("Doctor ID: " + currentAppointment.getDoctorId());

        lblDateTime.setText(currentAppointment.getAppointmentDate().toString());
        lblType.setText(currentAppointment.getAppointmentType());
        lblStatus.setText(currentAppointment.getStatus());

        if ("Completed".equalsIgnoreCase(currentAppointment.getStatus())) {
            btnComplete.setDisable(true);
        }
    }

    private void loadMedications() {
        try {
            List<Inventory> drugs = inventoryDAO.getAllInventory();
            comboMedication.setItems(FXCollections.observableArrayList(drugs));

            // Configure how items are displayed in the ComboBox
            comboMedication.setConverter(new StringConverter<Inventory>() {
                @Override
                public String toString(Inventory object) {
                    return object == null ? "" : object.getItemName() + " (Qty: " + object.getStockQuantity() + ")";
                }

                @Override
                public Inventory fromString(String string) {
                    return null; // Not needed for selection
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSavePrescription() {
        if (currentAppointment == null)
            return;
        if (!validatePrescription())
            return;

        try {
            Inventory selectedDrug = comboMedication.getValue();

            String dosage = txtDosage.getText().trim();
            String frequency = txtFrequency.getText().trim();
            String duration = txtDuration.getText().trim();
            String medicationName = selectedDrug.getItemName();

            // Construct instructions string for backward compatibility or display
            String instructions = String.format("Rx: %s\nDosage: %s\nFreq: %s\nDur: %s",
                    medicationName, dosage, frequency, duration);

            Prescription p = new Prescription();
            p.setPatientId(currentAppointment.getPatientId());
            p.setDoctorId(currentAppointment.getDoctorId());
            p.setAppointmentId(currentAppointment.getAppointmentId());
            p.setPrescriptionDate(LocalDate.now());
            p.setDiagnosis("Consultation #" + currentAppointment.getAppointmentId());
            p.setInstructions(instructions);
            p.setValidUntil(LocalDate.now().plusDays(30));

            // New Fields
            p.setMedicationName(medicationName);
            p.setDosage(dosage);
            p.setFrequency(frequency);
            p.setDuration(duration);

            prescriptionDAO.addPrescription(p);

            SystemLogger.getInstance().log("CLINICAL",
                    "Prescribed " + medicationName + " for Appointment #" + currentAppointment.getAppointmentId());
            showAlert(Alert.AlertType.INFORMATION, "Success", "Prescription saved successfully.");
            clearPrescriptionForm();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save prescription: " + e.getMessage());
        }
    }

    @FXML
    private void handleSaveMedicalLog() {
        if (currentAppointment == null)
            return;

        String content = txtNotes.getText().trim();
        if (content.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation", "Medical notes cannot be empty.");
            return;
        }

        try {
            MedicalLog log = new MedicalLog(
                    currentAppointment.getPatientId(),
                    currentAppointment.getDoctorId(),
                    "Consultation Note",
                    content,
                    new java.util.Date() // Current timestamp
            );

            medicalLogDAO.addLog(log);

            SystemLogger.getInstance().log("CLINICAL",
                    "Added medical log for Appointment #" + currentAppointment.getAppointmentId());
            showAlert(Alert.AlertType.INFORMATION, "Success", "Medical log saved successfully.");
            txtNotes.clear();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save medical log: " + e.getMessage());
        }
    }

    @FXML
    private void handleCompleteAppointment() {
        if (currentAppointment == null)
            return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Mark this appointment as Completed? This will close the case.", ButtonType.YES,
                ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    currentAppointment.setStatus("Completed");
                    appointmentDAO.update(currentAppointment);
                    lblStatus.setText("Completed");
                    btnComplete.setDisable(true);
                    SystemLogger.getInstance().log("APPOINTMENT",
                            "Completed Consultation #" + currentAppointment.getAppointmentId());
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update appointment: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) lblAppointmentId.getScene().getWindow();
        stage.close();
    }

    private boolean validatePrescription() {
        if (comboMedication.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation", "Please select a medication from inventory.");
            return false;
        }
        if (txtDosage.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation", "Dosage is required.");
            return false;
        }
        return true;
    }

    private void clearPrescriptionForm() {
        comboMedication.setValue(null);
        txtDosage.clear();
        txtFrequency.clear();
        txtDuration.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        if (type == null)
            type = Alert.AlertType.INFORMATION;
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
