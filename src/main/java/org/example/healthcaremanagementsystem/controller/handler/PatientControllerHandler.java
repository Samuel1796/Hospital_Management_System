package org.example.healthcaremanagementsystem.controller.handler;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.example.healthcaremanagementsystem.controller.pagination.PaginationHandler;
import org.example.healthcaremanagementsystem.model.Patient;
import org.example.healthcaremanagementsystem.service.PatientService;

import java.util.List;

/**
 * Handles Patient controller actions (load, create, update, delete, search).
 * Keeps {@code PatientController} thin and focused on view wiring.
 */
public class PatientControllerHandler {

    private final PatientService patientService;
    private final ObservableList<Patient> patientList;
    private final PaginationHandler paginationHandler;
    private final PatientFormHandler formHandler;
    private final TextField txtSearch;

    public PatientControllerHandler(PatientService patientService,
                                   ObservableList<Patient> patientList,
                                   PaginationHandler paginationHandler,
                                   PatientFormHandler formHandler,
                                   TextField txtSearch,
                                   Object unusedControllerReference) {
        this.patientService = patientService;
        this.patientList = patientList;
        this.paginationHandler = paginationHandler;
        this.formHandler = formHandler;
        this.txtSearch = txtSearch;
    }

    public void loadPatientsPaginated() {
        try {
            String term = txtSearch != null ? txtSearch.getText() : null;
            int page = paginationHandler.getCurrentPage();
            int size = paginationHandler.getPageSize();

            List<Patient> patients = (term == null || term.trim().isEmpty())
                    ? patientService.getPatientsPaginated(page, size)
                    : patientService.searchPatientsPaginated(term.trim(), page, size);

            patientList.setAll(patients);

            int total = (term == null || term.trim().isEmpty())
                    ? patientService.getTotalPatientCount()
                    : patientService.getSearchCount(term.trim());
            paginationHandler.setTotalRecords(total);
        } catch (Exception e) {
            showError("Error", "Failed to load patients: " + e.getMessage());
        }
    }

    public void createPatient(TextField txtFirstName, TextField txtLastName, TextField txtEmail,
                              TextField txtPhoneNumber, javafx.scene.control.DatePicker datePickerDateOfBirth) {
        try {
            Patient patient = formHandler.createPatientFromForm();
            patientService.createPatient(patient);
            showSuccess("Success", "Patient created successfully.");
            paginationHandler.reset();
            loadPatientsPaginated();
            formHandler.clearForm();
        } catch (Exception e) {
            showError("Error", e.getMessage());
        }
    }

    public void updatePatient(Patient selectedPatient, TextField txtFirstName, TextField txtLastName, TextField txtEmail,
                              TextField txtPhoneNumber, javafx.scene.control.DatePicker datePickerDateOfBirth) {
        if (selectedPatient == null || selectedPatient.getPatientId() == null) {
            showError("Error", "Please select a patient to update.");
            return;
        }

        try {
            Patient patient = formHandler.createPatientFromForm();
            patient.setPatientId(selectedPatient.getPatientId());
            boolean updated = patientService.updatePatient(patient);
            if (updated) {
                showSuccess("Success", "Patient updated successfully.");
            } else {
                showError("Error", "Patient update failed.");
            }
            loadPatientsPaginated();
            formHandler.clearForm();
        } catch (Exception e) {
            showError("Error", e.getMessage());
        }
    }

    public void deletePatient(Patient selectedPatient) {
        if (selectedPatient == null || selectedPatient.getPatientId() == null) {
            showError("Error", "Please select a patient to delete.");
            return;
        }

        if (!showConfirmation("Confirm Delete",
                "Delete patient?",
                "Are you sure you want to delete patient ID " + selectedPatient.getPatientId() + "?")) {
            return;
        }

        try {
            boolean deleted = patientService.deletePatient(selectedPatient.getPatientId());
            if (deleted) {
                showSuccess("Success", "Patient deleted successfully.");
            } else {
                showError("Error", "Patient delete failed.");
            }
            paginationHandler.reset();
            loadPatientsPaginated();
            formHandler.clearForm();
        } catch (Exception e) {
            showError("Error", e.getMessage());
        }
    }

    public void searchPatients() {
        paginationHandler.reset();
        loadPatientsPaginated();
    }

    public void clearSearch() {
        if (txtSearch != null) txtSearch.clear();
        paginationHandler.reset();
        loadPatientsPaginated();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}

