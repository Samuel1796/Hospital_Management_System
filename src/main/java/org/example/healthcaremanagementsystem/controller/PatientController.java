package org.example.healthcaremanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.healthcaremanagementsystem.model.Patient;
import org.example.healthcaremanagementsystem.service.PatientService;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Controller for Patient Management module.
 * Handles all patient-related UI operations including CRUD operations with
 * pagination.
 */
public class PatientController {

    // Form fields
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPhoneNumber;
    @FXML
    private DatePicker datePickerDateOfBirth;
    @FXML
    private TextArea txtAddress;
    @FXML
    private ComboBox<String> comboGender;
    @FXML
    private ComboBox<String> comboBloodGroup;
    @FXML
    private TextField txtEmergencyContact;
    @FXML
    private TextField txtEmergencyPhone;
    @FXML
    private TextField txtSearch;

    // Table components
    @FXML
    private TableView<Patient> tableViewPatients;
    @FXML
    private TableColumn<Patient, Integer> colPatientId;
    @FXML
    private TableColumn<Patient, String> colFirstName;
    @FXML
    private TableColumn<Patient, String> colLastName;
    @FXML
    private TableColumn<Patient, String> colEmail;
    @FXML
    private TableColumn<Patient, String> colPhone;

    // Buttons
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnClearSearch;
    @FXML
    private Button btnHistory;

    // Pagination components
    @FXML
    private Button btnFirstPage;
    @FXML
    private Button btnPrevPage;
    @FXML
    private Button btnNextPage;
    @FXML
    private Button btnLastPage;
    @FXML
    private Label lblPageInfo;
    @FXML
    private Label lblRecordInfo;
    @FXML
    private ComboBox<Integer> comboPageSize;

    private final PatientService patientService;
    private final ObservableList<Patient> patientList;
    private Patient selectedPatient;

    // Pagination state
    private int currentPage = 0;
    private int pageSize = 10;
    private int totalRecords = 0;
    private int totalPages = 0;
    private String currentSearchTerm = "";

    public PatientController() {
        this.patientService = new PatientService();
        this.patientList = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        setupTableColumns();
        setupComboBoxes();
        setupPaginationComboBox();
        setupValidation();
        setupPlaceholders();
        setupTableSelection();
        setupButtonActions();
        loadPatientsPaginated();
    }

    private void setupPaginationComboBox() {
        comboPageSize.getItems().addAll(5, 10, 20, 50, 100);
        comboPageSize.setValue(10);
    }

    @FXML
    private void changePageSize() {
        if (comboPageSize.getValue() != null) {
            pageSize = comboPageSize.getValue();
            currentPage = 0; // Reset to first page
            loadPatientsPaginated();
        }
    }

    @FXML
    private void goToFirstPage() {
        if (currentPage > 0) {
            currentPage = 0;
            loadPatientsPaginated();
        }
    }

    @FXML
    private void goToPreviousPage() {
        if (currentPage > 0) {
            currentPage--;
            loadPatientsPaginated();
        }
    }

    @FXML
    private void goToNextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            loadPatientsPaginated();
        }
    }

    @FXML
    private void goToLastPage() {
        if (currentPage < totalPages - 1) {
            currentPage = totalPages - 1;
            loadPatientsPaginated();
        }
    }

    private void updatePaginationControls() {
        totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        if (totalPages == 0)
            totalPages = 1;

        lblPageInfo.setText("Page " + (currentPage + 1) + " of " + totalPages);
        lblRecordInfo.setText(totalRecords + " records");

        btnFirstPage.setDisable(currentPage == 0);
        btnPrevPage.setDisable(currentPage == 0);
        btnNextPage.setDisable(currentPage >= totalPages - 1);
        btnLastPage.setDisable(currentPage >= totalPages - 1);
    }

    private void loadPatientsPaginated() {
        try {
            if (currentSearchTerm.isEmpty()) {
                totalRecords = patientService.getTotalPatientCount();
                patientList.clear();
                patientList.addAll(patientService.getPatientsPaginated(currentPage, pageSize));
            } else {
                totalRecords = patientService.getSearchCount(currentSearchTerm);
                patientList.clear();
                patientList.addAll(patientService.searchPatientsPaginated(currentSearchTerm, currentPage, pageSize));
            }
            updatePaginationControls();
        } catch (Exception e) {
            showError("Error", "Failed to load patients: " + e.getMessage());
        }
    }

    private void setupValidation() {
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty() && !isValidEmail(newValue)) {
                txtEmail.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
            } else {
                txtEmail.setStyle("");
            }
        });

        txtPhoneNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty() && !isValidPhoneNumber(newValue)) {
                txtPhoneNumber.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
            } else {
                txtPhoneNumber.setStyle("");
            }
        });

        txtEmergencyPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty() && !isValidPhoneNumber(newValue)) {
                txtEmergencyPhone.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
            } else {
                txtEmergencyPhone.setStyle("");
            }
        });
    }

    private void setupPlaceholders() {
        txtFirstName.setPromptText("Enter first name");
        txtLastName.setPromptText("Enter last name");
        txtEmail.setPromptText("example@email.com");
        txtPhoneNumber.setPromptText("0244XXXXXX");
        txtEmergencyPhone.setPromptText("0244XXXXXX");
        txtAddress.setPromptText("Enter full address");
        txtEmergencyContact.setPromptText("Emergency contact name");
        txtSearch.setPromptText("Search by name...");
        datePickerDateOfBirth.setPromptText("DD/MM/YYYY");
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhoneNumber(String phone) {
        String cleaned = phone.replaceAll("[\\s-]", "");
        String phoneRegex = "^(0[2-5]\\d{8}|030\\d{7})$";
        return cleaned.matches(phoneRegex);
    }

    private void setupTableColumns() {
        colPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        tableViewPatients.setItems(patientList);
    }

    private void setupComboBoxes() {
        comboGender.getItems().addAll("Male", "Female", "Other");
        comboBloodGroup.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
    }

    private void setupTableSelection() {
        tableViewPatients.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        selectedPatient = newValue;
                        populateForm(newValue);
                        btnUpdate.setDisable(false);
                        btnDelete.setDisable(false);
                        btnCreate.setDisable(true);
                        if (btnHistory != null)
                            btnHistory.setDisable(false);
                    }
                });
    }

    private void setupButtonActions() {
        btnCreate.setOnAction(e -> createPatient());
        btnUpdate.setOnAction(e -> updatePatient());
        btnDelete.setOnAction(e -> deletePatient());
        btnSearch.setOnAction(e -> searchPatients());
        btnClear.setOnAction(e -> clearForm());
        if (btnClearSearch != null) {
            btnClearSearch.setOnAction(e -> clearSearch());
        }
    }

    @FXML
    private void clearSearch() {
        txtSearch.clear();
        currentSearchTerm = "";
        currentPage = 0;
        loadPatientsPaginated();
    }

    @FXML
    private void createPatient() {
        if (!validateForm()) {
            return;
        }

        try {
            Patient patient = createPatientFromForm();
            Patient created = patientService.createPatient(patient);
            org.example.healthcaremanagementsystem.util.SystemLogger.getInstance().log("PATIENT",
                    "Created new patient: " + created.getFirstName() + " " + created.getLastName());
            currentPage = 0; // Go to first page to see new record
            currentSearchTerm = "";
            txtSearch.clear();
            loadPatientsPaginated();
            clearForm();
            showSuccess("Success", "Patient created successfully! (ID: " + created.getPatientId() + ")");
        } catch (IllegalArgumentException e) {
            showError("Validation Error", e.getMessage());
        } catch (Exception e) {
            showError("Error", "Failed to create patient: " + e.getMessage());
        }
    }

    private boolean validateForm() {
        if (txtFirstName.getText().trim().isEmpty()) {
            showError("Validation Error", "First name is required.");
            txtFirstName.requestFocus();
            return false;
        }

        if (txtLastName.getText().trim().isEmpty()) {
            showError("Validation Error", "Last name is required.");
            txtLastName.requestFocus();
            return false;
        }

        if (txtEmail.getText().trim().isEmpty()) {
            showError("Validation Error", "Email is required.");
            txtEmail.requestFocus();
            return false;
        }

        if (!isValidEmail(txtEmail.getText().trim())) {
            showError("Validation Error", "Please enter a valid email address.");
            txtEmail.requestFocus();
            return false;
        }

        if (txtPhoneNumber.getText().trim().isEmpty()) {
            showError("Validation Error", "Phone number is required.");
            txtPhoneNumber.requestFocus();
            return false;
        }

        if (!isValidPhoneNumber(txtPhoneNumber.getText().trim())) {
            showError("Validation Error", "Please enter a valid Ghanaian phone number (e.g., 0244XXXXXX).");
            txtPhoneNumber.requestFocus();
            return false;
        }

        if (datePickerDateOfBirth.getValue() == null) {
            showError("Validation Error", "Date of birth is required.");
            datePickerDateOfBirth.requestFocus();
            return false;
        }

        if (datePickerDateOfBirth.getValue().isAfter(LocalDate.now())) {
            showError("Validation Error", "Date of birth cannot be in the future.");
            datePickerDateOfBirth.requestFocus();
            return false;
        }

        return true;
    }

    @FXML
    private void updatePatient() {
        if (selectedPatient == null) {
            showError("Error", "Please select a patient to update.");
            return;
        }

        if (!validateForm()) {
            return;
        }

        try {
            Patient patient = createPatientFromForm();
            patient.setPatientId(selectedPatient.getPatientId());

            if (patientService.updatePatient(patient)) {
                org.example.healthcaremanagementsystem.util.SystemLogger.getInstance().log("PATIENT",
                        "Updated patient ID: " + patient.getPatientId());
                loadPatientsPaginated();
                clearForm();
                showSuccess("Success", "Patient updated successfully!");
            }
        } catch (IllegalArgumentException e) {
            showError("Validation Error", e.getMessage());
        } catch (Exception e) {
            showError("Error", "Failed to update patient: " + e.getMessage());
        }
    }

    @FXML
    private void deletePatient() {
        if (selectedPatient == null) {
            showError("Error", "Please select a patient to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Patient");
        alert.setContentText("Are you sure you want to delete this patient? This action cannot be undone.");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                if (patientService.deletePatient(selectedPatient.getPatientId())) {
                    org.example.healthcaremanagementsystem.util.SystemLogger.getInstance().log("PATIENT",
                            "Deleted patient ID: " + selectedPatient.getPatientId());
                    loadPatientsPaginated();
                    clearForm();
                    showSuccess("Success", "Patient deleted successfully!");
                }
            } catch (Exception e) {
                showError("Error", "Failed to delete patient: " + e.getMessage());
            }
        }
    }

    @FXML
    private void searchPatients() {
        currentSearchTerm = txtSearch.getText().trim();
        currentPage = 0; // Reset to first page when searching
        loadPatientsPaginated();
    }

    private Patient createPatientFromForm() {
        Patient patient = new Patient();
        patient.setFirstName(txtFirstName.getText().trim());
        patient.setLastName(txtLastName.getText().trim());
        patient.setEmail(txtEmail.getText().trim());
        patient.setPhoneNumber(txtPhoneNumber.getText().trim());
        patient.setDateOfBirth(datePickerDateOfBirth.getValue());
        patient.setAddress(txtAddress.getText().trim());
        patient.setGender(comboGender.getValue());
        patient.setBloodGroup(comboBloodGroup.getValue());
        patient.setEmergencyContact(txtEmergencyContact.getText().trim());
        patient.setEmergencyPhone(txtEmergencyPhone.getText().trim());
        return patient;
    }

    private void populateForm(Patient patient) {
        txtFirstName.setText(patient.getFirstName());
        txtLastName.setText(patient.getLastName());
        txtEmail.setText(patient.getEmail());
        txtPhoneNumber.setText(patient.getPhoneNumber());
        datePickerDateOfBirth.setValue(patient.getDateOfBirth());
        txtAddress.setText(patient.getAddress());
        comboGender.setValue(patient.getGender());
        comboBloodGroup.setValue(patient.getBloodGroup());
        txtEmergencyContact.setText(patient.getEmergencyContact());
        txtEmergencyPhone.setText(patient.getEmergencyPhone());
    }

    @FXML
    private void clearForm() {
        txtFirstName.clear();
        txtLastName.clear();
        txtEmail.clear();
        txtPhoneNumber.clear();
        datePickerDateOfBirth.setValue(null);
        txtAddress.clear();
        comboGender.setValue(null);
        comboBloodGroup.setValue(null);
        txtEmergencyContact.clear();
        txtEmergencyPhone.clear();

        tableViewPatients.getSelectionModel().clearSelection();
        selectedPatient = null;
        btnCreate.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnDelete.setDisable(true);
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
}
