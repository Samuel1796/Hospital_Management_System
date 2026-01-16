package org.example.healthcaremanagementsystem.controller.handler;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.example.healthcaremanagementsystem.controller.pagination.PaginationHandler;
import org.example.healthcaremanagementsystem.dao.DepartmentDAO;
import org.example.healthcaremanagementsystem.model.Department;
import org.example.healthcaremanagementsystem.model.Doctor;
import org.example.healthcaremanagementsystem.service.DoctorService;

import java.util.List;
import java.util.Objects;

/**
 * Handles Doctor controller actions (load, create, update, delete, search).
 */
public class DoctorControllerHandler {

    private final DoctorService doctorService;
    private final DepartmentDAO departmentDAO;
    private final ObservableList<Doctor> doctorList;
    private final ObservableList<Department> departmentList;
    private final PaginationHandler paginationHandler;
    private final DoctorFormHandler formHandler;
    private final TextField txtSearch;

    public DoctorControllerHandler(DoctorService doctorService,
                                  DepartmentDAO departmentDAO,
                                  ObservableList<Doctor> doctorList,
                                  ObservableList<Department> departmentList,
                                  PaginationHandler paginationHandler,
                                  DoctorFormHandler formHandler,
                                  TextField txtSearch,
                                  Object unusedControllerReference) {
        this.doctorService = doctorService;
        this.departmentDAO = departmentDAO;
        this.doctorList = doctorList;
        this.departmentList = departmentList;
        this.paginationHandler = paginationHandler;
        this.formHandler = formHandler;
        this.txtSearch = txtSearch;
    }

    public void loadDepartments(ComboBox<Department> comboDepartment) {
        try {
            List<Department> departments = departmentDAO.findAll();
            departmentList.setAll(departments);
            if (comboDepartment != null) {
                comboDepartment.setItems(departmentList);
            }
        } catch (Exception e) {
            showError("Error", "Failed to load departments: " + e.getMessage());
        }
    }

    public void loadDoctorsPaginated() {
        try {
            String term = txtSearch != null ? txtSearch.getText() : null;
            int page = paginationHandler.getCurrentPage();
            int size = paginationHandler.getPageSize();

            List<Doctor> doctors = (term == null || term.trim().isEmpty())
                    ? doctorService.getDoctorsPaginated(page, size)
                    : doctorService.searchDoctorsPaginated(term.trim(), page, size);

            doctorList.setAll(doctors);

            int total = (term == null || term.trim().isEmpty())
                    ? doctorService.getTotalDoctorCount()
                    : doctorService.getSearchCount(term.trim());
            paginationHandler.setTotalRecords(total);
        } catch (Exception e) {
            showError("Error", "Failed to load doctors: " + e.getMessage());
        }
    }

    public void createDoctor(TextField txtFirstName, TextField txtLastName, TextField txtEmail, TextField txtPhoneNumber,
                             ComboBox<String> comboSpecialization, ComboBox<Department> comboDepartment,
                             TextField txtLicenseNumber, ComboBox<String> comboStatus) {
        try {
            if (comboDepartment == null || comboDepartment.getValue() == null) {
                showError("Error", "Please select a department.");
                return;
            }
            Doctor doctor = formHandler.createDoctorFromForm();
            doctorService.createDoctor(doctor);
            showSuccess("Success", "Doctor created successfully.");
            paginationHandler.reset();
            loadDoctorsPaginated();
            formHandler.clearForm();
        } catch (Exception e) {
            showError("Error", e.getMessage());
        }
    }

    public void updateDoctor(Doctor selectedDoctor, TextField txtFirstName, TextField txtLastName, TextField txtEmail,
                             TextField txtPhoneNumber, ComboBox<String> comboSpecialization,
                             ComboBox<Department> comboDepartment, TextField txtLicenseNumber,
                             ComboBox<String> comboStatus) {
        if (selectedDoctor == null || selectedDoctor.getDoctorId() == null) {
            showError("Error", "Please select a doctor to update.");
            return;
        }
        try {
            if (comboDepartment == null || comboDepartment.getValue() == null) {
                showError("Error", "Please select a department.");
                return;
            }
            Doctor doctor = formHandler.createDoctorFromForm();
            doctor.setDoctorId(selectedDoctor.getDoctorId());
            boolean updated = doctorService.updateDoctor(doctor);
            if (updated) {
                showSuccess("Success", "Doctor updated successfully.");
            } else {
                showError("Error", "Doctor update failed.");
            }
            loadDoctorsPaginated();
            formHandler.clearForm();
        } catch (Exception e) {
            showError("Error", e.getMessage());
        }
    }

    public void deleteDoctor(Doctor selectedDoctor) {
        if (selectedDoctor == null || selectedDoctor.getDoctorId() == null) {
            showError("Error", "Please select a doctor to delete.");
            return;
        }

        if (!showConfirmation("Confirm Delete",
                "Delete doctor?",
                "Are you sure you want to delete doctor ID " + selectedDoctor.getDoctorId() + "?")) {
            return;
        }

        try {
            boolean deleted = doctorService.deleteDoctor(selectedDoctor.getDoctorId());
            if (deleted) {
                showSuccess("Success", "Doctor deleted successfully.");
            } else {
                showError("Error", "Doctor delete failed.");
            }
            paginationHandler.reset();
            loadDoctorsPaginated();
            formHandler.clearForm();
        } catch (Exception e) {
            showError("Error", e.getMessage());
        }
    }

    public void searchDoctors() {
        paginationHandler.reset();
        loadDoctorsPaginated();
    }

    public void clearSearch() {
        if (txtSearch != null) txtSearch.clear();
        paginationHandler.reset();
        loadDoctorsPaginated();
    }

    public Department findDepartmentById(Integer departmentId) {
        if (departmentId == null) return null;
        return departmentList.stream()
                .filter(d -> Objects.equals(d.getDepartmentId(), departmentId))
                .findFirst()
                .orElse(null);
    }

    public String getDepartmentNameForDoctor(Doctor doctor) {
        if (doctor == null) return "";
        Department dept = findDepartmentById(doctor.getDepartmentId());
        return dept == null ? "" : dept.getDepartmentName();
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

