package org.example.healthcaremanagementsystem.controller.handler;

import javafx.scene.control.*;

import java.util.function.Consumer;

/**
 * Handles Patient UI setup operations (combo boxes, validation, selection, buttons).
 * Kept lightweight so controllers remain focused on orchestration.
 */
public final class PatientSetupHandler {

    private PatientSetupHandler() {
    }

    public static void setupComboBoxes(ComboBox<String> comboGender, ComboBox<String> comboBloodGroup) {
        if (comboGender != null && comboGender.getItems().isEmpty()) {
            comboGender.getItems().addAll("Male", "Female", "Other");
        }
        if (comboBloodGroup != null && comboBloodGroup.getItems().isEmpty()) {
            comboBloodGroup.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        }
    }

    public static void setupPlaceholders(TextField txtFirstName, TextField txtLastName, TextField txtEmail,
                                         TextField txtPhoneNumber, TextField txtEmergencyContact,
                                         TextField txtEmergencyPhone, TextArea txtAddress, TextField txtSearch,
                                         DatePicker datePickerDateOfBirth) {
        if (txtFirstName != null) txtFirstName.setPromptText("First name");
        if (txtLastName != null) txtLastName.setPromptText("Last name");
        if (txtEmail != null) txtEmail.setPromptText("Email");
        if (txtPhoneNumber != null) txtPhoneNumber.setPromptText("Phone number");
        if (txtEmergencyContact != null) txtEmergencyContact.setPromptText("Emergency contact");
        if (txtEmergencyPhone != null) txtEmergencyPhone.setPromptText("Emergency phone");
        if (txtAddress != null) txtAddress.setPromptText("Address");
        if (txtSearch != null) txtSearch.setPromptText("Search by name...");
        if (datePickerDateOfBirth != null) datePickerDateOfBirth.setPromptText("Date of birth");
    }

    public static void setupValidation(TextField txtEmail, TextField txtPhoneNumber, TextField txtEmergencyPhone) {
        // Keep validation non-blocking and compilation-safe; heavy validation lives in services/validators.
        if (txtEmail != null) {
            txtEmail.textProperty().addListener((obs, oldV, newV) -> {
                if (newV != null && !newV.isBlank() && !newV.contains("@")) {
                    txtEmail.setStyle("-fx-border-color: #e57373;");
                } else {
                    txtEmail.setStyle(null);
                }
            });
        }
        addPhoneStyleValidator(txtPhoneNumber);
        addPhoneStyleValidator(txtEmergencyPhone);
    }

    private static void addPhoneStyleValidator(TextField field) {
        if (field == null) return;
        field.textProperty().addListener((obs, oldV, newV) -> {
            if (newV != null && !newV.isBlank() && newV.replaceAll("[\\s+()-]", "").length() < 7) {
                field.setStyle("-fx-border-color: #e57373;");
            } else {
                field.setStyle(null);
            }
        });
    }

    public static <T> void setupTableSelection(TableView<T> tableView, PatientFormHandler formHandler,
                                               Button btnUpdate, Button btnDelete, Button btnCreate,
                                               Button btnHistory, Consumer<T> onSelection) {
        if (btnUpdate != null) btnUpdate.setDisable(true);
        if (btnDelete != null) btnDelete.setDisable(true);
        if (btnHistory != null) btnHistory.setDisable(true);

        if (tableView == null) return;

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (onSelection != null) onSelection.accept(newV);

            boolean hasSelection = newV != null;
            if (btnCreate != null) btnCreate.setDisable(hasSelection);
            if (btnUpdate != null) btnUpdate.setDisable(!hasSelection);
            if (btnDelete != null) btnDelete.setDisable(!hasSelection);
            if (btnHistory != null) btnHistory.setDisable(!hasSelection);

            if (hasSelection && formHandler != null) {
                //noinspection unchecked
                formHandler.populateForm((org.example.healthcaremanagementsystem.model.Patient) newV);
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

    public static <T> void clearFormAndReset(PatientFormHandler formHandler, TextField txtSearch,
                                            TableView<T> tableView, Button btnCreate, Button btnUpdate,
                                            Button btnDelete, Button btnHistory, Consumer<T> onSelection) {
        if (formHandler != null) formHandler.clearForm();
        if (txtSearch != null) txtSearch.clear();
        if (tableView != null) tableView.getSelectionModel().clearSelection();
        if (btnCreate != null) btnCreate.setDisable(false);
        if (btnUpdate != null) btnUpdate.setDisable(true);
        if (btnDelete != null) btnDelete.setDisable(true);
        if (btnHistory != null) btnHistory.setDisable(true);
        if (onSelection != null) onSelection.accept(null);
    }
}

