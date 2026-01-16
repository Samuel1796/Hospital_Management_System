package org.example.healthcaremanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.healthcaremanagementsystem.controller.base.BaseController;
import org.example.healthcaremanagementsystem.controller.handler.AppointmentFormHandler;
import org.example.healthcaremanagementsystem.controller.handler.AppointmentControllerHandler;
import org.example.healthcaremanagementsystem.controller.handler.AppointmentSetupHandler;
import org.example.healthcaremanagementsystem.controller.handler.TableSetupHandler;
import org.example.healthcaremanagementsystem.controller.pagination.PaginationHandler;
import org.example.healthcaremanagementsystem.model.Appointment;
import org.example.healthcaremanagementsystem.model.Patient;
import org.example.healthcaremanagementsystem.model.Doctor;
import org.example.healthcaremanagementsystem.service.AppointmentService;
import org.example.healthcaremanagementsystem.service.PatientService;
import org.example.healthcaremanagementsystem.service.DoctorService;

/**
 * Controller for Appointment Management module.
 * Follows Single Responsibility Principle - coordinates between view and service layers.
 * All operations are delegated to specialized handlers.
 * 
 * @author Healthcare Management System Team
 * @version 4.0
 */
public class AppointmentController extends BaseController {

    @FXML private TextField txtSelectedPatient;
    @FXML private TextField txtSelectedDoctor;
    @FXML private Button btnSelectPatient;
    @FXML private Button btnSelectDoctor;
    @FXML private DatePicker datePickerAppointmentDate;
    @FXML private ComboBox<String> comboAppointmentTime;
    @FXML private ComboBox<String> comboAppointmentType;
    @FXML private ComboBox<String> comboStatus;
    @FXML private TextArea txtNotes;
    @FXML private TextField txtSearch;
    @FXML private TableView<AppointmentControllerHandler.AppointmentDisplay> tableViewAppointments;
    @FXML private TableColumn<AppointmentControllerHandler.AppointmentDisplay, Integer> colAppointmentId;
    @FXML private TableColumn<AppointmentControllerHandler.AppointmentDisplay, String> colPatientName;
    @FXML private TableColumn<AppointmentControllerHandler.AppointmentDisplay, String> colDoctorName;
    @FXML private TableColumn<AppointmentControllerHandler.AppointmentDisplay, String> colAppointmentDate;
    @FXML private TableColumn<AppointmentControllerHandler.AppointmentDisplay, String> colAppointmentType;
    @FXML private TableColumn<AppointmentControllerHandler.AppointmentDisplay, String> colStatus;
    @FXML private Button btnCreate;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnSearch;
    @FXML private Button btnClear;
    @FXML private Button btnClearSearch;
    @FXML private Button btnViewDetails;

    // Pagination components
    @FXML private Button btnFirstPage;
    @FXML private Button btnPrevPage;
    @FXML private Button btnNextPage;
    @FXML private Button btnLastPage;
    @FXML private Label lblPageInfo;
    @FXML private Label lblRecordInfo;

    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final ObservableList<AppointmentControllerHandler.AppointmentDisplay> appointmentList;
    private final ObservableList<Patient> patientList;
    private final ObservableList<Doctor> doctorList;
    private Appointment selectedAppointment;
    private Patient selectedPatient;
    private Doctor selectedDoctor;
    private PaginationHandler paginationHandler;
    private AppointmentFormHandler formHandler;
    private AppointmentControllerHandler controllerHandler;

    public AppointmentController() {
        this.appointmentService = new AppointmentService();
        this.patientService = new PatientService();
        this.doctorService = new DoctorService();
        this.appointmentList = FXCollections.observableArrayList();
        this.patientList = FXCollections.observableArrayList();
        this.doctorList = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        initializeHandlers();
        setupUI();
        controllerHandler.loadPatients();
        controllerHandler.loadDoctors();
        loadAppointmentsPaginated();
    }

    /**
     * Initializes all handlers.
     */
    private void initializeHandlers() {
        formHandler = new AppointmentFormHandler(
            txtSelectedPatient, txtSelectedDoctor, datePickerAppointmentDate,
            comboAppointmentTime, comboAppointmentType, comboStatus, txtNotes
        );
        
        paginationHandler = new PaginationHandler(
            btnFirstPage, btnPrevPage, btnNextPage, btnLastPage,
            lblPageInfo, lblRecordInfo, null
        );
        
        controllerHandler = new AppointmentControllerHandler(
            appointmentService, patientService, doctorService,
            patientList, doctorList, appointmentList,
            paginationHandler, formHandler, txtSearch, this
        );
    }

    /**
     * Sets up all UI components.
     */
    private void setupUI() {
        setupTableColumns();
        AppointmentSetupHandler.setupComboBoxes(comboAppointmentType, comboStatus, comboAppointmentTime);
        AppointmentSetupHandler.setupPlaceholders(txtNotes, txtSearch, datePickerAppointmentDate);
        AppointmentSetupHandler.setupValidation(datePickerAppointmentDate);
        AppointmentSetupHandler.setupTableSelection(tableViewAppointments, formHandler, controllerHandler,
            btnUpdate, btnDelete, btnCreate, btnViewDetails, apt -> {
                selectedAppointment = apt;
                if (apt != null) {
                    selectedPatient = controllerHandler.findPatientById(apt.getPatientId());
                    selectedDoctor = controllerHandler.findDoctorById(apt.getDoctorId());
                }
            });
        AppointmentSetupHandler.setupButtonActions(btnCreate, btnUpdate, btnDelete, btnSearch, btnClear,
            btnClearSearch, btnViewDetails, this::createAppointment, this::updateAppointment,
            this::deleteAppointment, this::searchAppointments, this::clearForm, this::clearSearch,
            this::openAppointmentDetails);
        AppointmentSetupHandler.setupPaginationButtons(btnFirstPage, btnPrevPage, btnNextPage, btnLastPage,
            this::goToFirstPage, this::goToPreviousPage, this::goToNextPage, this::goToLastPage);
        
        if (btnSelectPatient != null) btnSelectPatient.setOnAction(e -> selectPatient());
        if (btnSelectDoctor != null) btnSelectDoctor.setOnAction(e -> selectDoctor());
    }

    /**
     * Sets up table column bindings.
     */
    private void setupTableColumns() {
        TableSetupHandler.setupColumns(
            colAppointmentId, "appointmentId",
            colPatientName, "patientName",
            colDoctorName, "doctorName",
            colAppointmentDate, "appointmentDateFormatted",
            colAppointmentType, "appointmentType",
            colStatus, "status"
        );
        tableViewAppointments.setItems(appointmentList);
    }

    /**
     * Loads appointments with pagination.
     */
    private void loadAppointmentsPaginated() {
        controllerHandler.loadAppointmentsPaginated();
    }

    /**
     * Pagination navigation methods.
     */
    @FXML private void goToFirstPage() {
        paginationHandler.goToFirstPage();
        loadAppointmentsPaginated();
    }

    @FXML private void goToPreviousPage() {
        paginationHandler.goToPreviousPage();
        loadAppointmentsPaginated();
    }

    @FXML private void goToNextPage() {
        paginationHandler.goToNextPage();
        loadAppointmentsPaginated();
    }

    @FXML private void goToLastPage() {
        paginationHandler.goToLastPage();
        loadAppointmentsPaginated();
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
                formHandler.setSelectedPatient(patient);
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
                formHandler.setSelectedDoctor(doctor);
            }
        } catch (Exception e) {
            showError("Error", "Failed to open doctor selection dialog: " + e.getMessage());
        }
    }

    /**
     * Opens appointment details window.
     */
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
     * Creates a new appointment record.
     */
    @FXML
    private void createAppointment() {
        controllerHandler.createAppointment(selectedPatient, selectedDoctor, datePickerAppointmentDate,
            comboAppointmentTime, comboAppointmentType, comboStatus);
        selectedAppointment = null;
        selectedPatient = null;
        selectedDoctor = null;
    }

    /**
     * Updates an existing appointment record.
     */
    @FXML
    private void updateAppointment() {
        controllerHandler.updateAppointment(selectedAppointment, selectedPatient, selectedDoctor,
            datePickerAppointmentDate, comboAppointmentTime, comboAppointmentType, comboStatus);
        selectedAppointment = null;
        selectedPatient = null;
        selectedDoctor = null;
    }

    /**
     * Deletes the selected appointment record.
     */
    @FXML
    private void deleteAppointment() {
        controllerHandler.deleteAppointment(selectedAppointment);
        selectedAppointment = null;
        selectedPatient = null;
        selectedDoctor = null;
    }

    /**
     * Searches for appointments by patient or doctor name.
     */
    @FXML
    private void searchAppointments() {
        controllerHandler.searchAppointments();
    }

    /**
     * Clears search and displays all appointments.
     */
    @FXML
    private void clearSearch() {
        controllerHandler.clearSearch();
    }

    /**
     * Clears all form fields and resets selection.
     */
    @FXML
    private void clearForm() {
        AppointmentSetupHandler.clearFormAndReset(formHandler, txtSearch, tableViewAppointments,
            btnCreate, btnUpdate, btnDelete, btnViewDetails, apt -> {
                selectedAppointment = apt;
                selectedPatient = null;
                selectedDoctor = null;
            });
    }
}
