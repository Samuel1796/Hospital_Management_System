package org.example.healthcaremanagementsystem.controller.handler;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.example.healthcaremanagementsystem.controller.pagination.PaginationHandler;
import org.example.healthcaremanagementsystem.model.Appointment;
import org.example.healthcaremanagementsystem.model.Doctor;
import org.example.healthcaremanagementsystem.model.Patient;
import org.example.healthcaremanagementsystem.service.AppointmentService;
import org.example.healthcaremanagementsystem.service.DoctorService;
import org.example.healthcaremanagementsystem.service.PatientService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Handles Appointment controller actions (load, create, update, delete, search).
 */
public class AppointmentControllerHandler {

    private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final ObservableList<Patient> patientList;
    private final ObservableList<Doctor> doctorList;
    private final ObservableList<AppointmentDisplay> appointmentList;
    private final PaginationHandler paginationHandler;
    private final AppointmentFormHandler formHandler;
    private final TextField txtSearch;

    // Keep full loaded appointments in-memory for selection & search display mapping.
    private final List<Appointment> lastLoadedAppointments = new ArrayList<>();

    public AppointmentControllerHandler(AppointmentService appointmentService,
                                       PatientService patientService,
                                       DoctorService doctorService,
                                       ObservableList<Patient> patientList,
                                       ObservableList<Doctor> doctorList,
                                       ObservableList<AppointmentDisplay> appointmentList,
                                       PaginationHandler paginationHandler,
                                       AppointmentFormHandler formHandler,
                                       TextField txtSearch,
                                       Object unusedControllerReference) {
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.patientList = patientList;
        this.doctorList = doctorList;
        this.appointmentList = appointmentList;
        this.paginationHandler = paginationHandler;
        this.formHandler = formHandler;
        this.txtSearch = txtSearch;
    }

    public void loadPatients() {
        try {
            patientList.setAll(patientService.getAllPatients());
        } catch (Exception e) {
            showError("Error", "Failed to load patients: " + e.getMessage());
        }
    }

    public void loadDoctors() {
        try {
            doctorList.setAll(doctorService.getAllDoctors());
        } catch (Exception e) {
            showError("Error", "Failed to load doctors: " + e.getMessage());
        }
    }

    public Patient findPatientById(Integer patientId) {
        if (patientId == null) return null;
        return patientList.stream()
                .filter(p -> Objects.equals(p.getPatientId(), patientId))
                .findFirst()
                .orElse(null);
    }

    public Doctor findDoctorById(Integer doctorId) {
        if (doctorId == null) return null;
        return doctorList.stream()
                .filter(d -> Objects.equals(d.getDoctorId(), doctorId))
                .findFirst()
                .orElse(null);
    }

    public Appointment getAppointmentById(Integer appointmentId) {
        if (appointmentId == null) return null;
        return lastLoadedAppointments.stream()
                .filter(a -> Objects.equals(a.getAppointmentId(), appointmentId))
                .findFirst()
                .orElse(null);
    }

    public void loadAppointmentsPaginated() {
        try {
            String term = txtSearch != null ? txtSearch.getText() : null;
            if (term != null) term = term.trim();

            if (term == null || term.isEmpty()) {
                int page = paginationHandler.getCurrentPage();
                int size = paginationHandler.getPageSize();

                List<Appointment> appointments = appointmentService.getAppointmentsPaginated(page, size);
                lastLoadedAppointments.clear();
                lastLoadedAppointments.addAll(appointments);

                appointmentList.setAll(toDisplays(appointments));
                paginationHandler.setTotalRecords(appointmentService.getTotalAppointmentCount());
                return;
            }

            // No DB-backed search exists; do in-memory search with manual pagination.
            List<Appointment> all = appointmentService.getAllAppointments();
            List<Appointment> filtered = new ArrayList<>();
            for (Appointment a : all) {
                Patient p = findPatientById(a.getPatientId());
                Doctor d = findDoctorById(a.getDoctorId());
                String hay = (p != null ? p.getFullName() : "") + " " + (d != null ? d.getFullName() : "");
                if (hay.toLowerCase().contains(term.toLowerCase())) {
                    filtered.add(a);
                }
            }

            paginationHandler.setTotalRecords(filtered.size());
            int page = paginationHandler.getCurrentPage();
            int size = paginationHandler.getPageSize();
            int from = Math.min(page * size, filtered.size());
            int to = Math.min(from + size, filtered.size());
            List<Appointment> pageItems = filtered.subList(from, to);

            lastLoadedAppointments.clear();
            lastLoadedAppointments.addAll(pageItems);
            appointmentList.setAll(toDisplays(pageItems));
        } catch (Exception e) {
            showError("Error", "Failed to load appointments: " + e.getMessage());
        }
    }

    public void createAppointment(Patient selectedPatient, Doctor selectedDoctor,
                                  DatePicker datePickerAppointmentDate,
                                  ComboBox<String> comboAppointmentTime,
                                  ComboBox<String> comboAppointmentType,
                                  ComboBox<String> comboStatus) {
        if (selectedPatient == null) {
            showError("Error", "Please select a patient.");
            return;
        }
        if (selectedDoctor == null) {
            showError("Error", "Please select a doctor.");
            return;
        }

        try {
            Appointment appointment = formHandler.createAppointmentFromForm(selectedPatient, selectedDoctor);
            appointmentService.createAppointment(appointment);
            showSuccess("Success", "Appointment created successfully.");
            paginationHandler.reset();
            loadAppointmentsPaginated();
            formHandler.clearForm();
        } catch (Exception e) {
            showError("Error", e.getMessage());
        }
    }

    public void updateAppointment(Appointment selectedAppointment, Patient selectedPatient, Doctor selectedDoctor,
                                  DatePicker datePickerAppointmentDate,
                                  ComboBox<String> comboAppointmentTime,
                                  ComboBox<String> comboAppointmentType,
                                  ComboBox<String> comboStatus) {
        if (selectedAppointment == null || selectedAppointment.getAppointmentId() == null) {
            showError("Error", "Please select an appointment to update.");
            return;
        }
        if (selectedPatient == null) {
            showError("Error", "Please select a patient.");
            return;
        }
        if (selectedDoctor == null) {
            showError("Error", "Please select a doctor.");
            return;
        }

        try {
            Appointment appointment = formHandler.createAppointmentFromForm(selectedPatient, selectedDoctor);
            appointment.setAppointmentId(selectedAppointment.getAppointmentId());
            boolean updated = appointmentService.updateAppointment(appointment);
            if (updated) {
                showSuccess("Success", "Appointment updated successfully.");
            } else {
                showError("Error", "Appointment update failed.");
            }
            loadAppointmentsPaginated();
            formHandler.clearForm();
        } catch (Exception e) {
            showError("Error", e.getMessage());
        }
    }

    public void deleteAppointment(Appointment selectedAppointment) {
        if (selectedAppointment == null || selectedAppointment.getAppointmentId() == null) {
            showError("Error", "Please select an appointment to delete.");
            return;
        }

        if (!showConfirmation("Confirm Delete",
                "Delete appointment?",
                "Are you sure you want to delete appointment ID " + selectedAppointment.getAppointmentId() + "?")) {
            return;
        }

        try {
            boolean deleted = appointmentService.deleteAppointment(selectedAppointment.getAppointmentId());
            if (deleted) {
                showSuccess("Success", "Appointment deleted successfully.");
            } else {
                showError("Error", "Appointment delete failed.");
            }
            paginationHandler.reset();
            loadAppointmentsPaginated();
            formHandler.clearForm();
        } catch (Exception e) {
            showError("Error", e.getMessage());
        }
    }

    public void searchAppointments() {
        paginationHandler.reset();
        loadAppointmentsPaginated();
    }

    public void clearSearch() {
        if (txtSearch != null) txtSearch.clear();
        paginationHandler.reset();
        loadAppointmentsPaginated();
    }

    private List<AppointmentDisplay> toDisplays(List<Appointment> appointments) {
        List<AppointmentDisplay> displays = new ArrayList<>(appointments.size());
        for (Appointment a : appointments) {
            Patient p = findPatientById(a.getPatientId());
            Doctor d = findDoctorById(a.getDoctorId());
            displays.add(new AppointmentDisplay(
                    a.getAppointmentId(),
                    p != null ? p.getFullName() : ("Patient #" + a.getPatientId()),
                    d != null ? d.getFullName() : ("Doctor #" + a.getDoctorId()),
                    a.getAppointmentDate() == null ? "" : a.getAppointmentDate().format(DATE_TIME_FMT),
                    a.getAppointmentType(),
                    a.getStatus()
            ));
        }
        return displays;
    }

    public static class AppointmentDisplay {
        private final Integer appointmentId;
        private final String patientName;
        private final String doctorName;
        private final String appointmentDateFormatted;
        private final String appointmentType;
        private final String status;

        public AppointmentDisplay(Integer appointmentId,
                                  String patientName,
                                  String doctorName,
                                  String appointmentDateFormatted,
                                  String appointmentType,
                                  String status) {
            this.appointmentId = appointmentId;
            this.patientName = patientName;
            this.doctorName = doctorName;
            this.appointmentDateFormatted = appointmentDateFormatted;
            this.appointmentType = appointmentType;
            this.status = status;
        }

        public Integer getAppointmentId() {
            return appointmentId;
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
            return appointmentType;
        }

        public String getStatus() {
            return status;
        }
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

