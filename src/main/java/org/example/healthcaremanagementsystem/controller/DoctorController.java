package org.example.healthcaremanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.healthcaremanagementsystem.controller.base.BaseController;
import org.example.healthcaremanagementsystem.controller.handler.DoctorFormHandler;
import org.example.healthcaremanagementsystem.controller.handler.DoctorControllerHandler;
import org.example.healthcaremanagementsystem.controller.handler.DoctorSetupHandler;
import org.example.healthcaremanagementsystem.controller.handler.TableSetupHandler;
import org.example.healthcaremanagementsystem.controller.pagination.PaginationHandler;
import org.example.healthcaremanagementsystem.model.Doctor;
import org.example.healthcaremanagementsystem.model.Department;
import org.example.healthcaremanagementsystem.service.DoctorService;
import org.example.healthcaremanagementsystem.dao.DepartmentDAO;
import org.example.healthcaremanagementsystem.dao.DepartmentDAOImpl;

/**
 * Controller for Doctor Management module.
 * Follows Single Responsibility Principle - coordinates between view and service layers.
 * All operations are delegated to specialized handlers.
 * 
 * @author Healthcare Management System Team
 * @version 4.0
 */
public class DoctorController extends BaseController {

    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhoneNumber;
    @FXML private ComboBox<String> comboSpecialization;
    @FXML private ComboBox<Department> comboDepartment;
    @FXML private TextField txtLicenseNumber;
    @FXML private DatePicker datePickerHireDate;
    @FXML private ComboBox<String> comboStatus;
    @FXML private TextField txtSearch;

    @FXML private TableView<Doctor> tableViewDoctors;
    @FXML private TableColumn<Doctor, Integer> colDoctorId;
    @FXML private TableColumn<Doctor, String> colFirstName;
    @FXML private TableColumn<Doctor, String> colLastName;
    @FXML private TableColumn<Doctor, String> colSpecialization;
    @FXML private TableColumn<Doctor, String> colDepartment;
    @FXML private TableColumn<Doctor, String> colStatus;

    @FXML private Button btnCreate;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnSearch;
    @FXML private Button btnClear;
    @FXML private Button btnClearSearch;

    // Pagination components
    @FXML private Button btnFirstPage;
    @FXML private Button btnPrevPage;
    @FXML private Button btnNextPage;
    @FXML private Button btnLastPage;
    @FXML private Label lblPageInfo;
    @FXML private Label lblRecordInfo;
    @FXML private ComboBox<Integer> comboPageSize;

    private final DoctorService doctorService;
    private final DepartmentDAO departmentDAO;
    private final ObservableList<Doctor> doctorList;
    private final ObservableList<Department> departmentList;
    private Doctor selectedDoctor;
    private PaginationHandler paginationHandler;
    private DoctorFormHandler formHandler;
    private DoctorControllerHandler controllerHandler;

    public DoctorController() {
        this.doctorService = new DoctorService();
        this.departmentDAO = new DepartmentDAOImpl();
        this.doctorList = FXCollections.observableArrayList();
        this.departmentList = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        initializeHandlers();
        setupUI();
        controllerHandler.loadDepartments(comboDepartment);
        loadDoctorsPaginated();
    }

    /**
     * Initializes all handlers.
     */
    private void initializeHandlers() {
        formHandler = new DoctorFormHandler(
            txtFirstName, txtLastName, txtEmail, txtPhoneNumber,
            comboSpecialization, comboDepartment, txtLicenseNumber,
            datePickerHireDate, comboStatus
        );
        
        paginationHandler = new PaginationHandler(
            btnFirstPage, btnPrevPage, btnNextPage, btnLastPage,
            lblPageInfo, lblRecordInfo, comboPageSize
        );
        
        controllerHandler = new DoctorControllerHandler(
            doctorService, departmentDAO, doctorList, departmentList,
            paginationHandler, formHandler, txtSearch, this
        );
    }

    /**
     * Sets up all UI components.
     */
    private void setupUI() {
        setupTableColumns();
        DoctorSetupHandler.setupComboBoxes(comboStatus, comboSpecialization, comboDepartment);
        DoctorSetupHandler.setupPlaceholders(txtFirstName, txtLastName, txtEmail, txtPhoneNumber,
            txtLicenseNumber, txtSearch, datePickerHireDate);
        DoctorSetupHandler.setupValidation(txtEmail, txtPhoneNumber);
        DoctorSetupHandler.setupDepartmentColumn(colDepartment, controllerHandler);
        DoctorSetupHandler.setupTableSelection(tableViewDoctors, formHandler, controllerHandler,
            btnUpdate, btnDelete, btnCreate, doctor -> selectedDoctor = doctor);
        DoctorSetupHandler.setupButtonActions(btnCreate, btnUpdate, btnDelete, btnSearch, btnClear,
            btnClearSearch, this::createDoctor, this::updateDoctor, this::deleteDoctor,
            this::searchDoctors, this::clearForm, this::clearSearch);
        DoctorSetupHandler.setupPaginationButtons(btnFirstPage, btnPrevPage, btnNextPage, btnLastPage,
            this::goToFirstPage, this::goToPreviousPage, this::goToNextPage, this::goToLastPage);
    }

    /**
     * Sets up table column bindings.
     */
    private void setupTableColumns() {
        TableSetupHandler.setupColumns(
            colDoctorId, "doctorId",
            colFirstName, "firstName",
            colLastName, "lastName",
            colSpecialization, "specialization",
            colStatus, "status"
        );
        tableViewDoctors.setItems(doctorList);
    }

    /**
     * Loads doctors with pagination.
     */
    private void loadDoctorsPaginated() {
        controllerHandler.loadDoctorsPaginated();
    }

    /**
     * Pagination navigation methods.
     */
    @FXML private void goToFirstPage() {
        paginationHandler.goToFirstPage();
        loadDoctorsPaginated();
    }

    @FXML private void goToPreviousPage() {
        paginationHandler.goToPreviousPage();
        loadDoctorsPaginated();
    }

    @FXML private void goToNextPage() {
        paginationHandler.goToNextPage();
        loadDoctorsPaginated();
    }

    @FXML private void goToLastPage() {
        paginationHandler.goToLastPage();
        loadDoctorsPaginated();
    }

    @FXML
    private void changePageSize() {
        if (comboPageSize.getValue() != null) {
            paginationHandler.setPageSize(comboPageSize.getValue());
            loadDoctorsPaginated();
        }
    }

    /**
     * Creates a new doctor record.
     */
    @FXML
    private void createDoctor() {
        controllerHandler.createDoctor(txtFirstName, txtLastName, txtEmail, txtPhoneNumber,
            comboSpecialization, comboDepartment, txtLicenseNumber, comboStatus);
        selectedDoctor = null;
    }

    /**
     * Updates an existing doctor record.
     */
    @FXML
    private void updateDoctor() {
        controllerHandler.updateDoctor(selectedDoctor, txtFirstName, txtLastName, txtEmail, txtPhoneNumber,
            comboSpecialization, comboDepartment, txtLicenseNumber, comboStatus);
        selectedDoctor = null;
    }

    /**
     * Deletes the selected doctor record.
     */
    @FXML
    private void deleteDoctor() {
        controllerHandler.deleteDoctor(selectedDoctor);
        selectedDoctor = null;
    }

    /**
     * Searches for doctors by name.
     */
    @FXML
    private void searchDoctors() {
        controllerHandler.searchDoctors();
    }

    /**
     * Clears search and displays all doctors.
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
        DoctorSetupHandler.clearFormAndReset(formHandler, txtSearch, tableViewDoctors,
            btnCreate, btnUpdate, btnDelete, doctor -> selectedDoctor = doctor);
    }
}
