package org.example.healthcaremanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.healthcaremanagementsystem.model.Appointment;
import org.example.healthcaremanagementsystem.model.Patient;
import org.example.healthcaremanagementsystem.model.Doctor;
import org.example.healthcaremanagementsystem.service.AppointmentService;
import org.example.healthcaremanagementsystem.service.PatientService;
import org.example.healthcaremanagementsystem.service.DoctorService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

/**
 * Controller for Appointment Management module.
 * Handles all appointment-related UI operations including CRUD operations.
 * Implements strict input validation to prevent invalid data from being saved.
 */
public class AppointmentController {

    @FXML
    private TextField txtSelectedPatient;

    @FXML
    private TextField txtSelectedDoctor;

    @FXML
    private Button btnSelectPatient;

    @FXML
    private Button btnSelectDoctor;

    private Patient selectedPatient;
    private Doctor selectedDoctor;

    @FXML
    private DatePicker datePickerAppointmentDate;

    @FXML
    private ComboBox<String> comboAppointmentTime;

    @FXML
    private ComboBox<String> comboAppointmentType;

    @FXML
    private ComboBox<String> comboStatus;

    @FXML
    private TextArea txtNotes;

    @FXML
    private TextField txtSearch;

    @FXML
    private TableView<AppointmentDisplay> tableViewAppointments;

    @FXML
    private TableColumn<AppointmentDisplay, Integer> colAppointmentId;

    @FXML
    private TableColumn<AppointmentDisplay, String> colPatientName;

    @FXML
    private TableColumn<AppointmentDisplay, String> colDoctorName;

    @FXML
    private TableColumn<AppointmentDisplay, String> colAppointmentDate;

    @FXML
    private TableColumn<AppointmentDisplay, String> colAppointmentType;

    @FXML
    private TableColumn<AppointmentDisplay, String> colStatus;

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
    private Button btnViewDetails;

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

    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final ObservableList<AppointmentDisplay> appointmentList;
    private final ObservableList<Patient> patientList;
    private final ObservableList<Doctor> doctorList;
    private Appointment selectedAppointment;

    // Pagination state
    private int currentPage = 0;
    private static final int PAGE_SIZE = 10;
    private int totalRecords = 0;
    private int totalPages = 0;
    private String currentSearchTerm = "";

    // Time slots for appointments (30-minute intervals)
    private static final String[] TIME_SLOTS = {
            "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
            "11:00", "11:30", "12:00", "12:30", "13:00", "13:30",
            "14:00", "14:30", "15:00", "15:30", "16:00", "16:30",
            "17:00", "17:30"
    };

    /**
     * Constructor initializes services and observable lists.
     */
    public AppointmentController() {
        this.appointmentService = new AppointmentService();
        this.patientService = new PatientService();
        this.doctorService = new DoctorService();
        this.appointmentList = FXCollections.observableArrayList();
        this.patientList = FXCollections.observableArrayList();
        this.doctorList = FXCollections.observableArrayList();
    }

    /**
     * Initializes the controller and sets up UI components.
     */
    @FXML
    private void initialize() {
        setupTableColumns();
        setupComboBoxes();
        setupPlaceholders();
        setupValidation();
        loadPatients();
        loadDoctors();
        loadAppointmentsPaginated();
        setupTableSelection();
        setupButtonActions();
    }

    /**
     * Sets up table column bindings.
     */
    private void setupTableColumns() {
        colAppointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colDoctorName.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        colAppointmentDate.setCellValueFactory(new PropertyValueFactory<>("appointmentDateFormatted"));
        colAppointmentType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableViewAppointments.setItems(appointmentList);
    }

    /**
     * Sets up combo boxes with predefined values.
     */
    private void setupComboBoxes() {
        // Appointment types
        comboAppointmentType.getItems().addAll("Consultation", "Follow-up", "Emergency", "Check-up");

        // Status options
        comboStatus.getItems().addAll("Scheduled", "Completed", "Cancelled", "No-show");

        // Time slots
        comboAppointmentTime.getItems().addAll(TIME_SLOTS);
    }

    /**
     * Opens searchable dialog to select a patient.
     */
    @FXML
    private void selectPatient() {
        try {
            SearchableSelectionDialog<Patient> dialog = SearchableSelectionDialog.createPatientDialog(patientList);
            Patient patient = dialog.showAndWait();
            if (patient != null) {
                selectedPatient = patient;
                txtSelectedPatient.setText(patient.getFullName() + " (ID: " + patient.getPatientId() + ")");
            }
        } catch (Exception e) {
            showError("Error", "Failed to open patient selection dialog: " + e.getMessage());
        }
    }

    /**
     * Opens searchable dialog to select a doctor.
     */
    @FXML
    private void selectDoctor() {
        try {
            SearchableSelectionDialog<Doctor> dialog = SearchableSelectionDialog.createDoctorDialog(doctorList);
            Doctor doctor = dialog.showAndWait();
            if (doctor != null) {
                selectedDoctor = doctor;
                txtSelectedDoctor.setText(doctor.getFullName() + " - " + doctor.getSpecialization());
            }
        } catch (Exception e) {
            showError("Error", "Failed to open doctor selection dialog: " + e.getMessage());
        }
    }

    /**
     * Sets up placeholders for input fields.
     */
    private void setupPlaceholders() {
        txtNotes.setPromptText("Enter appointment notes...");
        txtSearch.setPromptText("Search by patient or doctor name...");
        datePickerAppointmentDate.setPromptText("DD/MM/YYYY");
    }

    /**
     * Sets up input validation with real-time feedback.
     */
    private void setupValidation() {
        // Date validation - cannot be in the past
        datePickerAppointmentDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.isBefore(LocalDate.now())) {
                datePickerAppointmentDate.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
            } else {
                datePickerAppointmentDate.setStyle("");
            }
        });
    }

    /**
     * Loads all patients from database.
     */
    private void loadPatients() {
        try {
            patientList.clear();
            patientList.addAll(patientService.getAllPatients());
        } catch (Exception e) {
            showError("Error", "Failed to load patients: " + e.getMessage());
        }
    }

    /**
     * Loads all doctors from database.
     */
    private void loadDoctors() {
        try {
            doctorList.clear();
            doctorList.addAll(doctorService.getAllDoctors());
        } catch (Exception e) {
            showError("Error", "Failed to load doctors: " + e.getMessage());
        }
    }

    /**
     * Loads appointments with pagination.
     */
    private void loadAppointmentsPaginated() {
        try {
            if (currentSearchTerm.isEmpty()) {
                totalRecords = appointmentService.getTotalAppointmentCount();
                appointmentList.clear();
                var appointments = appointmentService.getAppointmentsPaginated(currentPage, PAGE_SIZE);
                for (Appointment apt : appointments) {
                    appointmentList.add(new AppointmentDisplay(apt, findPatientById(apt.getPatientId()),
                            findDoctorById(apt.getDoctorId())));
                }
            } else {
                // Search with client-side filtering then paginate
                var allAppointments = appointmentService.getAllAppointments();
                var filteredList = new java.util.ArrayList<AppointmentDisplay>();

                for (Appointment apt : allAppointments) {
                    Patient patient = findPatientById(apt.getPatientId());
                    Doctor doctor = findDoctorById(apt.getDoctorId());

                    String searchLower = currentSearchTerm.toLowerCase();
                    boolean matches = false;
                    if (patient != null && (patient.getFirstName().toLowerCase().contains(searchLower) ||
                            patient.getLastName().toLowerCase().contains(searchLower))) {
                        matches = true;
                    } else if (doctor != null && (doctor.getFirstName().toLowerCase().contains(searchLower) ||
                            doctor.getLastName().toLowerCase().contains(searchLower))) {
                        matches = true;
                    }

                    if (matches) {
                        filteredList.add(new AppointmentDisplay(apt, patient, doctor));
                    }
                }

                totalRecords = filteredList.size();
                appointmentList.clear();

                // Paginate the filtered list
                int start = currentPage * PAGE_SIZE;
                int end = Math.min(start + PAGE_SIZE, filteredList.size());
                if (start < filteredList.size()) {
                    appointmentList.addAll(filteredList.subList(start, end));
                }
            }
            updatePaginationControls();
        } catch (Exception e) {
            showError("Error", "Failed to load appointments: " + e.getMessage());
        }
    }

    /**
     * Updates pagination control states.
     */
    private void updatePaginationControls() {
        totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);
        if (totalPages == 0)
            totalPages = 1;

        if (lblPageInfo != null) {
            lblPageInfo.setText("Page " + (currentPage + 1) + " of " + totalPages);
        }
        if (lblRecordInfo != null) {
            lblRecordInfo.setText(totalRecords + " records");
        }

        if (btnFirstPage != null)
            btnFirstPage.setDisable(currentPage == 0);
        if (btnPrevPage != null)
            btnPrevPage.setDisable(currentPage == 0);
        if (btnNextPage != null)
            btnNextPage.setDisable(currentPage >= totalPages - 1);
        if (btnLastPage != null)
            btnLastPage.setDisable(currentPage >= totalPages - 1);
    }

    @FXML
    private void goToFirstPage() {
        if (currentPage > 0) {
            currentPage = 0;
            loadAppointmentsPaginated();
        }
    }

    @FXML
    private void goToPreviousPage() {
        if (currentPage > 0) {
            currentPage--;
            loadAppointmentsPaginated();
        }
    }

    @FXML
    private void goToNextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            loadAppointmentsPaginated();
        }
    }

    @FXML
    private void goToLastPage() {
        if (currentPage < totalPages - 1) {
            currentPage = totalPages - 1;
            loadAppointmentsPaginated();
        }
    }

    /**
     * Finds a patient by ID.
     */
    private Patient findPatientById(Integer patientId) {
        return patientList.stream()
                .filter(p -> p.getPatientId().equals(patientId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds a doctor by ID.
     */
    private Doctor findDoctorById(Integer doctorId) {
        return doctorList.stream()
                .filter(d -> d.getDoctorId().equals(doctorId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Sets up table row selection handler.
     */
    private void setupTableSelection() {
        tableViewAppointments.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        selectedAppointment = newValue.getAppointment();
                        populateForm(newValue.getAppointment());
                        btnUpdate.setDisable(false);
                        btnDelete.setDisable(false);
                        btnCreate.setDisable(true);
                        if (btnViewDetails != null)
                            btnViewDetails.setDisable(false);
                    }
                });
    }

    /**
     * Sets up button event handlers.
     */
    @FXML
    private void setupButtonActions() {
        btnCreate.setOnAction(e -> createAppointment());
        btnUpdate.setOnAction(e -> updateAppointment());
        btnDelete.setOnAction(e -> deleteAppointment());
        btnSearch.setOnAction(e -> searchAppointments());
        btnClear.setOnAction(e -> clearForm());
        btnClearSearch.setOnAction(e -> clearSearch());

        if (btnViewDetails != null) {
            btnViewDetails.setOnAction(e -> openAppointmentDetails());
        }
    }

    @FXML
    private void openAppointmentDetails() {
        if (selectedAppointment == null) {
            showError("Error", "Please select an appointment to view details.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/healthcaremanagementsystem/appointment-details.fxml"));
            Parent root = loader.load();

            AppointmentDetailsController controller = loader.getController();
            controller.setAppointment(selectedAppointment);

            Stage stage = new Stage();
            stage.setTitle("Appointment Details - Clinical Workflow");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            showError("Error", "Failed to open appointment details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates a new appointment record with strict validation.
     * Prevents invalid data from being saved to database.
     */
    @FXML
    private void createAppointment() {
        // Validate form before attempting to create
        if (!validateForm()) {
            return; // Stop here - do not proceed with database operation
        }

        try {
            Appointment appointment = createAppointmentFromForm();
            Appointment created = appointmentService.createAppointment(appointment);

            // Refresh the list
            currentPage = 0;
            currentSearchTerm = "";
            txtSearch.clear();
            loadAppointmentsPaginated();
            clearForm();
            showSuccess("Success", "Appointment created successfully!");
        } catch (IllegalArgumentException e) {
            // Catch validation errors from service layer
            showError("Validation Error", e.getMessage());
        } catch (Exception e) {
            showError("Error", "Failed to create appointment: " + e.getMessage());
        }
    }

    /**
     * Updates an existing appointment record with strict validation.
     * Prevents invalid data from being saved to database.
     */
    @FXML
    private void updateAppointment() {
        if (selectedAppointment == null) {
            showError("Error", "Please select an appointment to update.");
            return;
        }

        // Validate form before attempting to update
        if (!validateForm()) {
            return; // Stop here - do not proceed with database operation
        }

        try {
            Appointment appointment = createAppointmentFromForm();
            appointment.setAppointmentId(selectedAppointment.getAppointmentId());

            if (appointmentService.updateAppointment(appointment)) {
                loadAppointmentsPaginated();
                clearForm();
                showSuccess("Success", "Appointment updated successfully!");
            }
        } catch (IllegalArgumentException e) {
            // Catch validation errors from service layer
            showError("Validation Error", e.getMessage());
        } catch (Exception e) {
            showError("Error", "Failed to update appointment: " + e.getMessage());
        }
    }

    /**
     * Deletes the selected appointment record.
     */
    @FXML
    private void deleteAppointment() {
        if (selectedAppointment == null) {
            showError("Error", "Please select an appointment to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Appointment");
        alert.setContentText("Are you sure you want to delete this appointment?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                if (appointmentService.deleteAppointment(selectedAppointment.getAppointmentId())) {
                    loadAppointmentsPaginated();
                    clearForm();
                    showSuccess("Success", "Appointment deleted successfully!");
                }
            } catch (Exception e) {
                showError("Error", "Failed to delete appointment: " + e.getMessage());
            }
        }
    }

    /**
     * Searches for appointments by patient or doctor name.
     */
    @FXML
    private void searchAppointments() {
        currentSearchTerm = txtSearch.getText().trim();
        currentPage = 0; // Reset to first page when searching
        loadAppointmentsPaginated();
    }

    /**
     * Clears search and displays all appointments.
     */
    @FXML
    private void clearSearch() {
        txtSearch.clear();
        currentSearchTerm = "";
        currentPage = 0;
        loadAppointmentsPaginated();
    }

    /**
     * Validates all form fields before submission.
     * Returns false if validation fails, preventing database operations.
     * 
     * @return true if all fields are valid, false otherwise
     */
    private boolean validateForm() {
        // Validate patient selection
        if (selectedPatient == null) {
            showError("Validation Error", "Please select a patient.");
            btnSelectPatient.requestFocus();
            return false; // Block database operation
        }

        // Validate doctor selection
        if (selectedDoctor == null) {
            showError("Validation Error", "Please select a doctor.");
            btnSelectDoctor.requestFocus();
            return false; // Block database operation
        }

        // Validate appointment date
        if (datePickerAppointmentDate.getValue() == null) {
            showError("Validation Error", "Appointment date is required.");
            datePickerAppointmentDate.requestFocus();
            return false; // Block database operation
        }

        // Validate date is not in the past
        if (datePickerAppointmentDate.getValue().isBefore(LocalDate.now())) {
            showError("Validation Error", "Appointment date cannot be in the past.");
            datePickerAppointmentDate.requestFocus();
            return false; // Block database operation
        }

        // Validate time selection
        if (comboAppointmentTime.getValue() == null || comboAppointmentTime.getValue().isEmpty()) {
            showError("Validation Error", "Appointment time is required.");
            comboAppointmentTime.requestFocus();
            return false; // Block database operation
        }

        // Validate appointment type
        if (comboAppointmentType.getValue() == null || comboAppointmentType.getValue().isEmpty()) {
            showError("Validation Error", "Appointment type is required.");
            comboAppointmentType.requestFocus();
            return false; // Block database operation
        }

        // Validate status
        if (comboStatus.getValue() == null || comboStatus.getValue().isEmpty()) {
            showError("Validation Error", "Appointment status is required.");
            comboStatus.requestFocus();
            return false; // Block database operation
        }

        // Validate date and time combination is not in the past
        LocalDate selectedDate = datePickerAppointmentDate.getValue();
        String selectedTime = comboAppointmentTime.getValue();
        LocalDateTime appointmentDateTime = LocalDateTime.of(selectedDate, LocalTime.parse(selectedTime));

        if (appointmentDateTime.isBefore(LocalDateTime.now())) {
            showError("Validation Error", "Appointment date and time cannot be in the past.");
            datePickerAppointmentDate.requestFocus();
            return false; // Block database operation
        }

        return true; // All validations passed
    }

    /**
     * Creates an Appointment object from form fields.
     * Only called after validation passes.
     * 
     * @return Appointment object with form data
     */
    private Appointment createAppointmentFromForm() {
        Appointment appointment = new Appointment();
        appointment.setPatientId(selectedPatient.getPatientId());
        appointment.setDoctorId(selectedDoctor.getDoctorId());

        // Combine date and time
        LocalDate selectedDate = datePickerAppointmentDate.getValue();
        String selectedTime = comboAppointmentTime.getValue();
        LocalDateTime appointmentDateTime = LocalDateTime.of(selectedDate, LocalTime.parse(selectedTime));
        appointment.setAppointmentDate(appointmentDateTime);

        appointment.setAppointmentType(comboAppointmentType.getValue());
        appointment.setStatus(comboStatus.getValue());
        appointment.setNotes(txtNotes.getText().trim());

        return appointment;
    }

    /**
     * Populates form fields with appointment data.
     * 
     * @param appointment Appointment object to populate from
     */
    private void populateForm(Appointment appointment) {
        // Set patient
        Patient patient = findPatientById(appointment.getPatientId());
        if (patient != null) {
            selectedPatient = patient;
            txtSelectedPatient.setText(patient.getFullName() + " (ID: " + patient.getPatientId() + ")");
        }

        // Set doctor
        Doctor doctor = findDoctorById(appointment.getDoctorId());
        if (doctor != null) {
            selectedDoctor = doctor;
            txtSelectedDoctor.setText(doctor.getFullName() + " - " + doctor.getSpecialization());
        }

        // Set date and time
        if (appointment.getAppointmentDate() != null) {
            datePickerAppointmentDate.setValue(appointment.getAppointmentDate().toLocalDate());
            String time = appointment.getAppointmentDate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            comboAppointmentTime.setValue(time);
        }

        comboAppointmentType.setValue(appointment.getAppointmentType());
        comboStatus.setValue(appointment.getStatus());
        txtNotes.setText(appointment.getNotes());
    }

    /**
     * Clears all form fields and resets selection.
     */
    @FXML
    private void clearForm() {
        selectedPatient = null;
        selectedDoctor = null;
        txtSelectedPatient.clear();
        txtSelectedDoctor.clear();
        datePickerAppointmentDate.setValue(null);
        comboAppointmentTime.setValue(null);
        comboAppointmentType.setValue(null);
        comboStatus.setValue(null);
        txtNotes.clear();
        txtSearch.clear();

        tableViewAppointments.getSelectionModel().clearSelection();
        selectedAppointment = null;
        btnCreate.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        if (btnViewDetails != null)
            btnViewDetails.setDisable(true);
    }

    /**
     * Shows a success alert dialog.
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
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Inner class for displaying appointments in table with patient/doctor names.
     */
    public static class AppointmentDisplay {
        private final Appointment appointment;
        private final String patientName;
        private final String doctorName;
        private final String appointmentDateFormatted;

        public AppointmentDisplay(Appointment appointment, Patient patient, Doctor doctor) {
            this.appointment = appointment;
            this.patientName = patient != null ? patient.getFullName() : "N/A";
            this.doctorName = doctor != null ? doctor.getFullName() : "N/A";

            if (appointment.getAppointmentDate() != null) {
                this.appointmentDateFormatted = appointment.getAppointmentDate()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            } else {
                this.appointmentDateFormatted = "N/A";
            }
        }

        public Appointment getAppointment() {
            return appointment;
        }

        public Integer getAppointmentId() {
            return appointment.getAppointmentId();
        }

        public String getPatientName() {
            return patientName;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public String getAppointmentDateFormatted() {
            return appointmentDateFormatted;
        }

        public String getAppointmentType() {
            return appointment.getAppointmentType();
        }

        public String getStatus() {
            return appointment.getStatus();
        }
    }
}
