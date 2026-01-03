package org.example.healthcaremanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.healthcaremanagementsystem.model.Patient;
import org.example.healthcaremanagementsystem.service.PatientService;

import java.time.LocalDate;

/**
 * Controller for Patient Management module.
 * Handles all patient-related UI operations including CRUD operations.
 * Follows MVC pattern by separating view logic from business logic.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class PatientController {
    
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
    
    private final PatientService patientService;
    private final ObservableList<Patient> patientList;
    private Patient selectedPatient;
    
    /**
     * Constructor initializes service and observable list.
     */
    public PatientController() {
        this.patientService = new PatientService();
        this.patientList = FXCollections.observableArrayList();
    }
    
    /**
     * Initializes the controller and sets up UI components.
     */
    @FXML
    private void initialize() {
        setupTableColumns();
        setupComboBoxes();
        setupValidation();
        setupPlaceholders();
        loadAllPatients();
        setupTableSelection();
        setupButtonActions();
    }
    
    /**
     * Sets up input validation for email and phone number fields.
     */
    private void setupValidation() {
        // Email validation
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty() && !isValidEmail(newValue)) {
                txtEmail.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
            } else {
                txtEmail.setStyle("");
            }
        });
        
        // Phone number validation (Ghanaian format: 0244XXXXXX or 020XXXXXXX)
        txtPhoneNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty() && !isValidPhoneNumber(newValue)) {
                txtPhoneNumber.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
            } else {
                txtPhoneNumber.setStyle("");
            }
        });
        
        // Emergency phone validation
        txtEmergencyPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty() && !isValidPhoneNumber(newValue)) {
                txtEmergencyPhone.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
            } else {
                txtEmergencyPhone.setStyle("");
            }
        });
    }
    
    /**
     * Sets up placeholders for input fields.
     */
    private void setupPlaceholders() {
        txtFirstName.setPromptText("Enter first name");
        txtLastName.setPromptText("Enter last name");
        txtEmail.setPromptText("example@email.com");
        txtPhoneNumber.setPromptText("0244XXXXXX");
        txtEmergencyPhone.setPromptText("0244XXXXXX");
        txtAddress.setPromptText("Enter full address");
        txtEmergencyContact.setPromptText("Emergency contact name");
        txtSearch.setPromptText("Search by name...");
        
        // Set date picker prompt text via CSS or JavaFX properties
        datePickerDateOfBirth.setPromptText("DD/MM/YYYY");
    }
    
    /**
     * Validates email format.
     * 
     * @param email Email address to validate
     * @return true if valid email format
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    
    /**
     * Validates Ghanaian phone number format.
     * Accepts formats: 0244XXXXXX, 020XXXXXXX, 050XXXXXXX, etc.
     * 
     * @param phone Phone number to validate
     * @return true if valid phone format
     */
    private boolean isValidPhoneNumber(String phone) {
        // Remove spaces and dashes
        String cleaned = phone.replaceAll("[\\s-]", "");
        // Ghanaian mobile numbers: 0244XXXXXX, 020XXXXXXX, 050XXXXXXX, etc.
        // Landline: 0302XXXXXX
        String phoneRegex = "^(0[2-5]\\d{8}|030\\d{7})$";
        return cleaned.matches(phoneRegex);
    }
    
    /**
     * Sets up table column bindings.
     */
    private void setupTableColumns() {
        colPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        
        tableViewPatients.setItems(patientList);
    }
    
    /**
     * Sets up combo boxes with predefined values.
     */
    private void setupComboBoxes() {
        comboGender.getItems().addAll("Male", "Female", "Other");
        comboBloodGroup.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
    }
    
    /**
     * Sets up table row selection handler.
     */
    private void setupTableSelection() {
        tableViewPatients.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    selectedPatient = newValue;
                    populateForm(newValue);
                    btnUpdate.setDisable(false);
                    btnDelete.setDisable(false);
                    btnCreate.setDisable(true);
                }
            }
        );
    }
    
    /**
     * Sets up button event handlers.
     */
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
    
    /**
     * Clears search and displays all patients.
     */
    @FXML
    private void clearSearch() {
        txtSearch.clear();
        loadAllPatients();
    }
    
    /**
     * Loads all patients from the database.
     */
    private void loadAllPatients() {
        try {
            patientList.clear();
            patientList.addAll(patientService.getAllPatients());
        } catch (Exception e) {
            showError("Error", "Failed to load patients: " + e.getMessage());
        }
    }
    
    /**
     * Creates a new patient record with validation.
     */
    @FXML
    private void createPatient() {
        // Validate form fields
        if (!validateForm()) {
            return;
        }
        
        try {
            Patient patient = createPatientFromForm();
            Patient created = patientService.createPatient(patient);
            patientList.add(created);
            clearForm();
            showSuccess("Success", "Patient created successfully!");
        } catch (IllegalArgumentException e) {
            // Catch validation errors from service layer - prevents invalid data from being saved
            showError("Validation Error", e.getMessage());
        } catch (Exception e) {
            showError("Error", "Failed to create patient: " + e.getMessage());
        }
    }
    
    /**
     * Validates all form fields before submission.
     * 
     * @return true if all fields are valid
     */
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
    
    /**
     * Updates an existing patient record with strict validation.
     * Prevents invalid data from being saved to database.
     */
    @FXML
    private void updatePatient() {
        if (selectedPatient == null) {
            showError("Error", "Please select a patient to update.");
            return;
        }
        
        // Validate form before attempting to update
        if (!validateForm()) {
            return; // Stop here - do not proceed with database operation
        }
        
        try {
            Patient patient = createPatientFromForm();
            patient.setPatientId(selectedPatient.getPatientId());
            
            if (patientService.updatePatient(patient)) {
                int index = patientList.indexOf(selectedPatient);
                patientList.set(index, patient);
                clearForm();
                showSuccess("Success", "Patient updated successfully!");
            }
        } catch (IllegalArgumentException e) {
            // Catch validation errors from service layer
            showError("Validation Error", e.getMessage());
        } catch (Exception e) {
            showError("Error", "Failed to update patient: " + e.getMessage());
        }
    }
    
    /**
     * Deletes the selected patient record.
     */
    @FXML
    private void deletePatient() {
        if (selectedPatient == null) {
            showError("Error", "Please select a patient to delete.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Patient");
        alert.setContentText("Are you sure you want to delete this patient?");
        
        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                if (patientService.deletePatient(selectedPatient.getPatientId())) {
                    patientList.remove(selectedPatient);
                    clearForm();
                    showSuccess("Success", "Patient deleted successfully!");
                }
            } catch (Exception e) {
                showError("Error", "Failed to delete patient: " + e.getMessage());
            }
        }
    }
    
    /**
     * Searches for patients by name.
     */
    @FXML
    private void searchPatients() {
        String searchTerm = txtSearch.getText().trim();
        
        try {
            patientList.clear();
            if (searchTerm.isEmpty()) {
                patientList.addAll(patientService.getAllPatients());
            } else {
                patientList.addAll(patientService.searchPatientsByName(searchTerm));
            }
        } catch (Exception e) {
            showError("Error", "Failed to search patients: " + e.getMessage());
        }
    }
    
    /**
     * Creates a Patient object from form fields.
     * 
     * @return Patient object with form data
     */
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
    
    /**
     * Populates form fields with patient data.
     * 
     * @param patient Patient object to populate from
     */
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
    
    /**
     * Clears all form fields and resets selection.
     */
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
        txtSearch.clear();
        
        tableViewPatients.getSelectionModel().clearSelection();
        selectedPatient = null;
        btnCreate.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
    }
    
    /**
     * Shows a success alert dialog.
     * 
     * @param title Alert title
     * @param message Alert message
     */
    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Shows an error alert dialog.
     * 
     * @param title Alert title
     * @param message Alert message
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

