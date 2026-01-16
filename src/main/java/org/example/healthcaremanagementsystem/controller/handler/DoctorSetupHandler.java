package org.example.healthcaremanagementsystem.controller.handler;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.example.healthcaremanagementsystem.model.Department;
import org.example.healthcaremanagementsystem.model.Doctor;

import java.util.function.Consumer;

/**
 * Handles Doctor UI setup operations.
 */
public final class DoctorSetupHandler {

    private DoctorSetupHandler() {
    }

    public static void setupComboBoxes(ComboBox<String> comboStatus,
                                       ComboBox<String> comboSpecialization,
                                       ComboBox<Department> comboDepartment) {
        if (comboStatus != null && comboStatus.getItems().isEmpty()) {
            comboStatus.getItems().addAll("Active", "Inactive", "On Leave");
        }
        if (comboSpecialization != null && comboSpecialization.getItems().isEmpty()) {
            comboSpecialization.getItems().addAll(
                    "General Practice",
                    "Cardiology",
                    "Dermatology",
                    "Neurology",
                    "Orthopedics",
                    "Pediatrics",
                    "Radiology",
                    "Surgery"
            );
        }
        if (comboDepartment != null) {
            comboDepartment.setConverter(new StringConverter<>() {
                @Override
                public String toString(Department dept) {
                    return dept == null ? "" : dept.getDepartmentName();
                }

                @Override
                public Department fromString(String string) {
                    return comboDepartment.getItems().stream()
                            .filter(d -> d != null && d.getDepartmentName() != null
                                    && d.getDepartmentName().equalsIgnoreCase(string))
                            .findFirst()
                            .orElse(null);
                }
            });
        }
    }

    public static void setupPlaceholders(TextField txtFirstName, TextField txtLastName, TextField txtEmail,
                                         TextField txtPhoneNumber, TextField txtLicenseNumber, TextField txtSearch,
                                         DatePicker datePickerHireDate) {
        if (txtFirstName != null) txtFirstName.setPromptText("First name");
        if (txtLastName != null) txtLastName.setPromptText("Last name");
        if (txtEmail != null) txtEmail.setPromptText("Email");
        if (txtPhoneNumber != null) txtPhoneNumber.setPromptText("Phone number");
        if (txtLicenseNumber != null) txtLicenseNumber.setPromptText("License number");
        if (txtSearch != null) txtSearch.setPromptText("Search by name...");
        if (datePickerHireDate != null) datePickerHireDate.setPromptText("Hire date");
    }

    public static void setupValidation(TextField txtEmail, TextField txtPhoneNumber) {
        if (txtEmail != null) {
            txtEmail.textProperty().addListener((obs, oldV, newV) -> {
                if (newV != null && !newV.isBlank() && !newV.contains("@")) {
                    txtEmail.setStyle("-fx-border-color: #e57373;");
                } else {
                    txtEmail.setStyle(null);
                }
            });
        }
        if (txtPhoneNumber != null) {
            txtPhoneNumber.textProperty().addListener((obs, oldV, newV) -> {
                if (newV != null && !newV.isBlank() && newV.replaceAll("[\\s+()-]", "").length() < 7) {
                    txtPhoneNumber.setStyle("-fx-border-color: #e57373;");
                } else {
                    txtPhoneNumber.setStyle(null);
                }
            });
        }
    }

    public static void setupDepartmentColumn(TableColumn<Doctor, String> colDepartment,
                                             DoctorControllerHandler controllerHandler) {
        if (colDepartment == null || controllerHandler == null) return;
        colDepartment.setCellValueFactory(cell -> {
            Doctor doctor = cell.getValue();
            String name = controllerHandler.getDepartmentNameForDoctor(doctor);
            return new SimpleStringProperty(name);
        });
    }

    public static void setupTableSelection(TableView<Doctor> tableView, DoctorFormHandler formHandler,
                                           DoctorControllerHandler controllerHandler,
                                           Button btnUpdate, Button btnDelete, Button btnCreate,
                                           Consumer<Doctor> onSelection) {
        if (btnUpdate != null) btnUpdate.setDisable(true);
        if (btnDelete != null) btnDelete.setDisable(true);

        if (tableView == null) return;

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (onSelection != null) onSelection.accept(newV);
            boolean hasSelection = newV != null;
            if (btnCreate != null) btnCreate.setDisable(hasSelection);
            if (btnUpdate != null) btnUpdate.setDisable(!hasSelection);
            if (btnDelete != null) btnDelete.setDisable(!hasSelection);

            if (hasSelection && formHandler != null && controllerHandler != null) {
                Department dept = controllerHandler.findDepartmentById(newV.getDepartmentId());
                formHandler.populateForm(newV, dept);
            }
        });
    }

    public static void setupButtonActions(Button btnCreate, Button btnUpdate, Button btnDelete,
                                          Button btnSearch, Button btnClear, Button btnClearSearch,
                                          Runnable onCreate, Runnable onUpdate, Runnable onDelete,
                                          Runnable onSearch, Runnable onClearForm, Runnable onClearSearch) {
        if (btnCreate != null && onCreate != null) btnCreate.setOnAction(e -> onCreate.run());
        if (btnUpdate != null && onUpdate != null) btnUpdate.setOnAction(e -> onUpdate.run());
        if (btnDelete != null && onDelete != null) btnDelete.setOnAction(e -> onDelete.run());
        if (btnSearch != null && onSearch != null) btnSearch.setOnAction(e -> onSearch.run());
        if (btnClear != null && onClearForm != null) btnClear.setOnAction(e -> onClearForm.run());
        if (btnClearSearch != null && onClearSearch != null) btnClearSearch.setOnAction(e -> onClearSearch.run());
    }

    public static void setupPaginationButtons(Button btnFirstPage, Button btnPrevPage,
                                              Button btnNextPage, Button btnLastPage,
                                              Runnable first, Runnable prev, Runnable next, Runnable last) {
        if (btnFirstPage != null && first != null) btnFirstPage.setOnAction(e -> first.run());
        if (btnPrevPage != null && prev != null) btnPrevPage.setOnAction(e -> prev.run());
        if (btnNextPage != null && next != null) btnNextPage.setOnAction(e -> next.run());
        if (btnLastPage != null && last != null) btnLastPage.setOnAction(e -> last.run());
    }

    public static void clearFormAndReset(DoctorFormHandler formHandler, TextField txtSearch,
                                         TableView<Doctor> tableView, Button btnCreate, Button btnUpdate,
                                         Button btnDelete, Consumer<Doctor> onSelection) {
        if (formHandler != null) formHandler.clearForm();
        if (txtSearch != null) txtSearch.clear();
        if (tableView != null) tableView.getSelectionModel().clearSelection();
        if (btnCreate != null) btnCreate.setDisable(false);
        if (btnUpdate != null) btnUpdate.setDisable(true);
        if (btnDelete != null) btnDelete.setDisable(true);
        if (onSelection != null) onSelection.accept(null);
    }
}

