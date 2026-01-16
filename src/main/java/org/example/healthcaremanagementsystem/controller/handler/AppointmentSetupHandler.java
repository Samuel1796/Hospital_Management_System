package org.example.healthcaremanagementsystem.controller.handler;

import javafx.scene.control.*;
import org.example.healthcaremanagementsystem.model.Appointment;

import java.time.LocalDate;
import java.util.function.Consumer;

/**
 * Handles Appointment UI setup operations.
 */
public final class AppointmentSetupHandler {

    private AppointmentSetupHandler() {
    }

    public static void setupComboBoxes(ComboBox<String> comboAppointmentType,
                                       ComboBox<String> comboStatus,
                                       ComboBox<String> comboAppointmentTime) {
        if (comboAppointmentType != null && comboAppointmentType.getItems().isEmpty()) {
            comboAppointmentType.getItems().addAll("Consultation", "Follow-up", "Emergency", "Routine Checkup");
        }
        if (comboStatus != null && comboStatus.getItems().isEmpty()) {
            comboStatus.getItems().addAll("Scheduled", "Completed", "Cancelled", "No-show");
        }
        if (comboAppointmentTime != null && comboAppointmentTime.getItems().isEmpty()) {
            // Simple time slots (24-hour format) to match AppointmentFormHandler parsing.
            comboAppointmentTime.getItems().addAll(
                    "08:00", "08:30",
                    "09:00", "09:30",
                    "10:00", "10:30",
                    "11:00", "11:30",
                    "13:00", "13:30",
                    "14:00", "14:30",
                    "15:00", "15:30",
                    "16:00", "16:30"
            );
        }
    }

    public static void setupPlaceholders(TextArea txtNotes, TextField txtSearch, DatePicker datePickerAppointmentDate) {
        if (txtNotes != null) txtNotes.setPromptText("Notes (optional)");
        if (txtSearch != null) txtSearch.setPromptText("Search by patient/doctor...");
        if (datePickerAppointmentDate != null) datePickerAppointmentDate.setPromptText("Appointment date");
    }

    public static void setupValidation(DatePicker datePickerAppointmentDate) {
        if (datePickerAppointmentDate == null) return;
        datePickerAppointmentDate.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) return;
                // Disallow selecting past dates (service layer also enforces this).
                if (item.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #eeeeee;");
                }
            }
        });
    }

    public static void setupTableSelection(TableView<AppointmentControllerHandler.AppointmentDisplay> tableView,
                                           AppointmentFormHandler formHandler,
                                           AppointmentControllerHandler controllerHandler,
                                           Button btnUpdate, Button btnDelete, Button btnCreate,
                                           Button btnViewDetails,
                                           Consumer<Appointment> onSelection) {
        if (btnUpdate != null) btnUpdate.setDisable(true);
        if (btnDelete != null) btnDelete.setDisable(true);
        if (btnViewDetails != null) btnViewDetails.setDisable(true);

        if (tableView == null) return;

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            boolean hasSelection = newV != null;
            if (btnCreate != null) btnCreate.setDisable(hasSelection);
            if (btnUpdate != null) btnUpdate.setDisable(!hasSelection);
            if (btnDelete != null) btnDelete.setDisable(!hasSelection);
            if (btnViewDetails != null) btnViewDetails.setDisable(!hasSelection);

            Appointment appointment = null;
            if (hasSelection && controllerHandler != null) {
                appointment = controllerHandler.getAppointmentById(newV.getAppointmentId());
                if (appointment != null && formHandler != null) {
                    var patient = controllerHandler.findPatientById(appointment.getPatientId());
                    var doctor = controllerHandler.findDoctorById(appointment.getDoctorId());
                    formHandler.populateForm(appointment, patient, doctor);
                }
            }
            if (onSelection != null) onSelection.accept(appointment);
        });
    }

    public static void setupButtonActions(Button btnCreate, Button btnUpdate, Button btnDelete,
                                          Button btnSearch, Button btnClear, Button btnClearSearch,
                                          Button btnViewDetails,
                                          Runnable onCreate, Runnable onUpdate, Runnable onDelete,
                                          Runnable onSearch, Runnable onClearForm, Runnable onClearSearch,
                                          Runnable onViewDetails) {
        if (btnCreate != null && onCreate != null) btnCreate.setOnAction(e -> onCreate.run());
        if (btnUpdate != null && onUpdate != null) btnUpdate.setOnAction(e -> onUpdate.run());
        if (btnDelete != null && onDelete != null) btnDelete.setOnAction(e -> onDelete.run());
        if (btnSearch != null && onSearch != null) btnSearch.setOnAction(e -> onSearch.run());
        if (btnClear != null && onClearForm != null) btnClear.setOnAction(e -> onClearForm.run());
        if (btnClearSearch != null && onClearSearch != null) btnClearSearch.setOnAction(e -> onClearSearch.run());
        if (btnViewDetails != null && onViewDetails != null) btnViewDetails.setOnAction(e -> onViewDetails.run());
    }

    public static void setupPaginationButtons(Button btnFirstPage, Button btnPrevPage,
                                              Button btnNextPage, Button btnLastPage,
                                              Runnable first, Runnable prev, Runnable next, Runnable last) {
        if (btnFirstPage != null && first != null) btnFirstPage.setOnAction(e -> first.run());
        if (btnPrevPage != null && prev != null) btnPrevPage.setOnAction(e -> prev.run());
        if (btnNextPage != null && next != null) btnNextPage.setOnAction(e -> next.run());
        if (btnLastPage != null && last != null) btnLastPage.setOnAction(e -> last.run());
    }

    public static void clearFormAndReset(AppointmentFormHandler formHandler, TextField txtSearch,
                                         TableView<AppointmentControllerHandler.AppointmentDisplay> tableView,
                                         Button btnCreate, Button btnUpdate, Button btnDelete, Button btnViewDetails,
                                         Consumer<Appointment> onSelection) {
        if (formHandler != null) formHandler.clearForm();
        if (txtSearch != null) txtSearch.clear();
        if (tableView != null) tableView.getSelectionModel().clearSelection();
        if (btnCreate != null) btnCreate.setDisable(false);
        if (btnUpdate != null) btnUpdate.setDisable(true);
        if (btnDelete != null) btnDelete.setDisable(true);
        if (btnViewDetails != null) btnViewDetails.setDisable(true);
        if (onSelection != null) onSelection.accept(null);
    }
}

