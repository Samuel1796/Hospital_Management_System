package org.example.healthcaremanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.healthcaremanagementsystem.controller.base.BaseController;
import org.example.healthcaremanagementsystem.controller.handler.PatientFormHandler;
import org.example.healthcaremanagementsystem.controller.handler.PatientControllerHandler;
import org.example.healthcaremanagementsystem.controller.handler.PatientSetupHandler;
import org.example.healthcaremanagementsystem.controller.handler.TableSetupHandler;
import org.example.healthcaremanagementsystem.controller.pagination.PaginationHandler;
import org.example.healthcaremanagementsystem.model.Patient;
import org.example.healthcaremanagementsystem.service.PatientService;

/**
 * Controller for Patient Management module.
 * Follows Single Responsibility Principle - coordinates between view and service layers.
 * All operations are delegated to specialized handlers.
 * 
 * @author Healthcare Management System Team
 * @version 4.0
 */
public class PatientController extends BaseController {

    // Form fields
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhoneNumber;
    @FXML private DatePicker datePickerDateOfBirth;
    @FXML private TextArea txtAddress;
    @FXML private ComboBox<String> comboGender;
    @FXML private ComboBox<String> comboBloodGroup;
    @FXML private TextField txtEmergencyContact;
    @FXML private TextField txtEmergencyPhone;
    @FXML private TextField txtSearch;

    // Table components
    @FXML private TableView<Patient> tableViewPatients;
    @FXML private TableColumn<Patient, Integer> colPatientId;
    @FXML private TableColumn<Patient, String> colFirstName;
    @FXML private TableColumn<Patient, String> colLastName;
    @FXML private TableColumn<Patient, String> colEmail;
    @FXML private TableColumn<Patient, String> colPhone;

    // Buttons
    @FXML private Button btnCreate;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnSearch;
    @FXML private Button btnClear;
    @FXML private Button btnClearSearch;
    @FXML private Button btnHistory;

    // Pagination components
    @FXML private Button btnFirstPage;
    @FXML private Button btnPrevPage;
    @FXML private Button btnNextPage;
    @FXML private Button btnLastPage;
    @FXML private Label lblPageInfo;
    @FXML private Label lblRecordInfo;
    @FXML private ComboBox<Integer> comboPageSize;

    private final PatientService patientService;
    private final ObservableList<Patient> patientList;
    private Patient selectedPatient;
    private PaginationHandler paginationHandler;
    private PatientFormHandler formHandler;
    private PatientControllerHandler controllerHandler;

    public PatientController() {
        this.patientService = new PatientService();
        this.patientList = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        initializeHandlers();
        setupUI();
        loadPatientsPaginated();
    }

    /**
     * Initializes all handlers.
     */
    private void initializeHandlers() {
        formHandler = new PatientFormHandler(
            txtFirstName, txtLastName, txtEmail, txtPhoneNumber,
            datePickerDateOfBirth, txtAddress, comboGender, comboBloodGroup,
            txtEmergencyContact, txtEmergencyPhone
        );
        
        paginationHandler = new PaginationHandler(
            btnFirstPage, btnPrevPage, btnNextPage, btnLastPage,
            lblPageInfo, lblRecordInfo, comboPageSize
        );
        
        controllerHandler = new PatientControllerHandler(
            patientService, patientList, paginationHandler,
            formHandler, txtSearch, this
        );
    }

    /**
     * Sets up all UI components.
     */
    private void setupUI() {
        setupTableColumns();
        PatientSetupHandler.setupComboBoxes(comboGender, comboBloodGroup);
        PatientSetupHandler.setupPlaceholders(txtFirstName, txtLastName, txtEmail, txtPhoneNumber,
            txtEmergencyContact, txtEmergencyPhone, txtAddress, txtSearch, datePickerDateOfBirth);
        PatientSetupHandler.setupValidation(txtEmail, txtPhoneNumber, txtEmergencyPhone);
        PatientSetupHandler.setupTableSelection(tableViewPatients, formHandler, btnUpdate, btnDelete,
            btnCreate, btnHistory, patient -> selectedPatient = patient);
        PatientSetupHandler.setupButtonActions(btnCreate, btnUpdate, btnDelete, btnSearch, btnClear,
            btnClearSearch, this::createPatient, this::updatePatient, this::deletePatient,
            this::searchPatients, this::clearForm, this::clearSearch);
        PatientSetupHandler.setupPaginationButtons(btnFirstPage, btnPrevPage, btnNextPage, btnLastPage,
            this::goToFirstPage, this::goToPreviousPage, this::goToNextPage, this::goToLastPage);
    }

    /**
     * Sets up table column bindings.
     */
    private void setupTableColumns() {
        TableSetupHandler.setupColumns(
            colPatientId, "patientId",
            colFirstName, "firstName",
            colLastName, "lastName",
            colEmail, "email",
            colPhone, "phoneNumber"
        );
        tableViewPatients.setItems(patientList);
    }

    /**
     * Loads patients with pagination.
     */
    private void loadPatientsPaginated() {
        controllerHandler.loadPatientsPaginated();
    }

    /**
     * Pagination navigation methods.
     */
    @FXML private void goToFirstPage() {
        paginationHandler.goToFirstPage();
        loadPatientsPaginated();
    }

    @FXML private void goToPreviousPage() {
        paginationHandler.goToPreviousPage();
        loadPatientsPaginated();
    }

    @FXML private void goToNextPage() {
        paginationHandler.goToNextPage();
        loadPatientsPaginated();
    }

    @FXML private void goToLastPage() {
        paginationHandler.goToLastPage();
        loadPatientsPaginated();
    }

    @FXML
    private void changePageSize() {
        if (comboPageSize.getValue() != null) {
            paginationHandler.setPageSize(comboPageSize.getValue());
            loadPatientsPaginated();
        }
    }

    /**
     * Creates a new patient record.
     */
    @FXML
    private void createPatient() {
        controllerHandler.createPatient(txtFirstName, txtLastName, txtEmail, txtPhoneNumber, datePickerDateOfBirth);
        selectedPatient = null;
    }

    /**
     * Updates an existing patient record.
     */
    @FXML
    private void updatePatient() {
        controllerHandler.updatePatient(selectedPatient, txtFirstName, txtLastName, txtEmail, txtPhoneNumber, datePickerDateOfBirth);
        selectedPatient = null;
    }

    /**
     * Deletes the selected patient record.
     */
    @FXML
    private void deletePatient() {
        controllerHandler.deletePatient(selectedPatient);
        selectedPatient = null;
    }

    /**
     * Searches for patients by name.
     */
    @FXML
    private void searchPatients() {
        controllerHandler.searchPatients();
    }

    /**
     * Clears search and displays all patients.
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
        PatientSetupHandler.clearFormAndReset(formHandler, txtSearch, tableViewPatients,
            btnCreate, btnUpdate, btnDelete, btnHistory, patient -> selectedPatient = patient);
    }
}
