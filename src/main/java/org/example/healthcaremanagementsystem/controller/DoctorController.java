package org.example.healthcaremanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.healthcaremanagementsystem.model.Doctor;
import org.example.healthcaremanagementsystem.model.Department;
import org.example.healthcaremanagementsystem.service.DoctorService;
import org.example.healthcaremanagementsystem.dao.DepartmentDAO;
import org.example.healthcaremanagementsystem.dao.DepartmentDAOImpl;

/**
 * Controller for Doctor Management module.
 * Handles all doctor-related UI operations including CRUD operations.
 * Follows MVC pattern by separating view logic from business logic.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class DoctorController {
    
    @FXML
    private TextField txtFirstName;
    
    @FXML
    private TextField txtLastName;
    
    @FXML
    private TextField txtEmail;
    
    @FXML
    private TextField txtPhoneNumber;
    
    @FXML
    private ComboBox<String> comboSpecialization;
    
    @FXML
    private ComboBox<Department> comboDepartment;
    
    @FXML
    private TextField txtLicenseNumber;
    
    @FXML
    private DatePicker datePickerHireDate;
    
    @FXML
    private ComboBox<String> comboStatus;
    
    @FXML
    private TextField txtSearch;
    
    @FXML
    private TableView<Doctor> tableViewDoctors;
    
    @FXML
    private TableColumn<Doctor, Integer> colDoctorId;
    
    @FXML
    private TableColumn<Doctor, String> colFirstName;
    
    @FXML
    private TableColumn<Doctor, String> colLastName;
    
    @FXML
    private TableColumn<Doctor, String> colSpecialization;
    
    @FXML
    private TableColumn<Doctor, String> colDepartment;
    
    @FXML
    private TableColumn<Doctor, String> colStatus;
    
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
    
    private final DoctorService doctorService;
    private final DepartmentDAO departmentDAO;
    private final ObservableList<Doctor> doctorList;
    private final ObservableList<Department> departmentList;
    private Doctor selectedDoctor;
    
    /**
     * Constructor initializes services and observable lists.
     */
    public DoctorController() {
        this.doctorService = new DoctorService();
        this.departmentDAO = new DepartmentDAOImpl();
        this.doctorList = FXCollections.observableArrayList();
        this.departmentList = FXCollections.observableArrayList();
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
        loadDepartments();
        loadAllDoctors();
        setupTableSelection();
        setupButtonActions();
    }
    
    /**
     * Sets up table column bindings.
     */
    private void setupTableColumns() {
        colDoctorId.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Custom cell factory for department name
        colDepartment.setCellValueFactory(cellData -> {
            Doctor doctor = cellData.getValue();
            if (doctor.getDepartmentId() != null) {
                Department dept = findDepartmentById(doctor.getDepartmentId());
                if (dept != null) {
                    return new javafx.beans.property.SimpleStringProperty(dept.getDepartmentName());
                }
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
        
        tableViewDoctors.setItems(doctorList);
    }
    
    /**
     * Sets up combo boxes with predefined values.
     */
    private void setupComboBoxes() {
        // Status options
        comboStatus.getItems().addAll("Active", "Inactive", "On Leave");
        
        // Specialization options (5 common medical specializations)
        comboSpecialization.getItems().addAll(
            "Cardiology",
            "Neurology",
            "Orthopedics",
            "Pediatrics",
            "General Medicine"
        );
        
        // Department combo box will be populated from database
        comboDepartment.setCellFactory(listView -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDepartmentName());
            }
        });
        comboDepartment.setButtonCell(new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDepartmentName());
            }
        });
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
        
        // Phone number validation (Ghanaian format)
        txtPhoneNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty() && !isValidPhoneNumber(newValue)) {
                txtPhoneNumber.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
            } else {
                txtPhoneNumber.setStyle("");
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
        txtLicenseNumber.setPromptText("LIC-XXXX-XXX");
        txtSearch.setPromptText("Search by name...");
        datePickerHireDate.setPromptText("DD/MM/YYYY");
    }
    
    /**
     * Loads all departments from database.
     */
    private void loadDepartments() {
        try {
            departmentList.clear();
            departmentList.addAll(departmentDAO.findAll());
            comboDepartment.setItems(departmentList);
        } catch (Exception e) {
            showError("Error", "Failed to load departments: " + e.getMessage());
        }
    }
    
    /**
     * Finds a department by ID.
     * 
     * @param departmentId Department ID
     * @return Department object or null if not found
     */
    private Department findDepartmentById(Integer departmentId) {
        return departmentList.stream()
                .filter(dept -> dept.getDepartmentId().equals(departmentId))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Sets up table row selection handler.
     */
    private void setupTableSelection() {
        tableViewDoctors.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    selectedDoctor = newValue;
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
        btnCreate.setOnAction(e -> createDoctor());
        btnUpdate.setOnAction(e -> updateDoctor());
        btnDelete.setOnAction(e -> deleteDoctor());
        btnSearch.setOnAction(e -> searchDoctors());
        btnClear.setOnAction(e -> clearForm());
        btnClearSearch.setOnAction(e -> clearSearch());
    }
    
    /**
     * Loads all doctors from the database.
     */
    private void loadAllDoctors() {
        try {
            doctorList.clear();
            doctorList.addAll(doctorService.getAllDoctors());
        } catch (Exception e) {
            showError("Error", "Failed to load doctors: " + e.getMessage());
        }
    }
    
    /**
     * Creates a new doctor record with strict validation.
     * Prevents invalid data from being saved to database.
     */
    @FXML
    private void createDoctor() {
        // Validate form before attempting to create
        if (!validateForm()) {
            return; // Stop here - do not proceed with database operation
        }
        
        try {
            Doctor doctor = createDoctorFromForm();
            Doctor created = doctorService.createDoctor(doctor);
            loadAllDoctors(); // Reload to get sequential IDs
            clearForm();
            showSuccess("Success", "Doctor created successfully! (ID: " + created.getDoctorId() + ")");
        } catch (IllegalArgumentException e) {
            // Catch validation errors from service layer (including duplicates)
            showError("Validation Error", e.getMessage());
        } catch (Exception e) {
            showError("Error", "Failed to create doctor: " + e.getMessage());
        }
    }
    
    /**
     * Updates an existing doctor record with strict validation.
     * Prevents invalid data from being saved to database.
     */
    @FXML
    private void updateDoctor() {
        if (selectedDoctor == null) {
            showError("Error", "Please select a doctor to update.");
            return;
        }
        
        // Validate form before attempting to update
        if (!validateForm()) {
            return; // Stop here - do not proceed with database operation
        }
        
        try {
            Doctor doctor = createDoctorFromForm();
            doctor.setDoctorId(selectedDoctor.getDoctorId());
            
            if (doctorService.updateDoctor(doctor)) {
                loadAllDoctors(); // Reload to refresh the table
                clearForm();
                showSuccess("Success", "Doctor updated successfully!");
            }
        } catch (IllegalArgumentException e) {
            // Catch validation errors from service layer (including duplicates)
            showError("Validation Error", e.getMessage());
        } catch (Exception e) {
            showError("Error", "Failed to update doctor: " + e.getMessage());
        }
    }
    
    /**
     * Deletes the selected doctor record.
     */
    @FXML
    private void deleteDoctor() {
        if (selectedDoctor == null) {
            showError("Error", "Please select a doctor to delete.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Doctor");
        alert.setContentText("Are you sure you want to delete this doctor? This action cannot be undone.");
        
        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                if (doctorService.deleteDoctor(selectedDoctor.getDoctorId())) {
                    loadAllDoctors(); // Reload to refresh IDs
                    clearForm();
                    showSuccess("Success", "Doctor deleted successfully!");
                }
            } catch (Exception e) {
                showError("Error", "Failed to delete doctor: " + e.getMessage());
            }
        }
    }
    
    /**
     * Searches for doctors by name.
     */
    @FXML
    private void searchDoctors() {
        String searchTerm = txtSearch.getText().trim();
        
        try {
            doctorList.clear();
            if (searchTerm.isEmpty()) {
                doctorList.addAll(doctorService.getAllDoctors());
            } else {
                doctorList.addAll(doctorService.searchDoctorsByName(searchTerm));
            }
        } catch (Exception e) {
            showError("Error", "Failed to search doctors: " + e.getMessage());
        }
    }
    
    /**
     * Clears search and displays all doctors.
     */
    @FXML
    private void clearSearch() {
        txtSearch.clear();
        loadAllDoctors();
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
        
        if (comboSpecialization.getValue() == null) {
            showError("Validation Error", "Specialization is required.");
            comboSpecialization.requestFocus();
            return false;
        }
        
        if (comboDepartment.getValue() == null) {
            showError("Validation Error", "Department is required.");
            comboDepartment.requestFocus();
            return false;
        }
        
        if (txtLicenseNumber.getText().trim().isEmpty()) {
            showError("Validation Error", "License number is required.");
            txtLicenseNumber.requestFocus();
            return false;
        }
        
        if (comboStatus.getValue() == null) {
            showError("Validation Error", "Status is required.");
            comboStatus.requestFocus();
            return false;
        }
        
        return true;
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
     * 
     * @param phone Phone number to validate
     * @return true if valid phone format
     */
    private boolean isValidPhoneNumber(String phone) {
        String cleaned = phone.replaceAll("[\\s-]", "");
        String phoneRegex = "^(0[2-5]\\d{8}|030\\d{7})$";
        return cleaned.matches(phoneRegex);
    }
    
    /**
     * Creates a Doctor object from form fields.
     * 
     * @return Doctor object with form data
     */
    private Doctor createDoctorFromForm() {
        Doctor doctor = new Doctor();
        doctor.setFirstName(txtFirstName.getText().trim());
        doctor.setLastName(txtLastName.getText().trim());
        doctor.setEmail(txtEmail.getText().trim());
        doctor.setPhoneNumber(txtPhoneNumber.getText().trim());
        doctor.setSpecialization(comboSpecialization.getValue());
        doctor.setDepartmentId(comboDepartment.getValue().getDepartmentId());
        doctor.setLicenseNumber(txtLicenseNumber.getText().trim());
        doctor.setHireDate(datePickerHireDate.getValue());
        doctor.setStatus(comboStatus.getValue());
        return doctor;
    }
    
    /**
     * Populates form fields with doctor data.
     * 
     * @param doctor Doctor object to populate from
     */
    private void populateForm(Doctor doctor) {
        txtFirstName.setText(doctor.getFirstName());
        txtLastName.setText(doctor.getLastName());
        txtEmail.setText(doctor.getEmail());
        txtPhoneNumber.setText(doctor.getPhoneNumber());
        comboSpecialization.setValue(doctor.getSpecialization());
        txtLicenseNumber.setText(doctor.getLicenseNumber());
        datePickerHireDate.setValue(doctor.getHireDate());
        comboStatus.setValue(doctor.getStatus());
        
        // Set department
        Department dept = findDepartmentById(doctor.getDepartmentId());
        if (dept != null) {
            comboDepartment.setValue(dept);
        }
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
        comboSpecialization.setValue(null);
        txtLicenseNumber.clear();
        datePickerHireDate.setValue(null);
        comboDepartment.setValue(null);
        comboStatus.setValue(null);
        txtSearch.clear();
        
        tableViewDoctors.getSelectionModel().clearSelection();
        selectedDoctor = null;
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

