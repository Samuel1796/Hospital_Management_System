package org.example.healthcaremanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.TableCell;
import javafx.util.Callback;
import org.example.healthcaremanagementsystem.model.Appointment;
import org.example.healthcaremanagementsystem.model.Doctor;
import org.example.healthcaremanagementsystem.model.Patient;
import org.example.healthcaremanagementsystem.service.AppointmentService;
import org.example.healthcaremanagementsystem.service.DoctorService;
import org.example.healthcaremanagementsystem.service.PatientService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ConsultationController {

    @FXML
    private TableView<AppointmentModel> tblAppointments;
    @FXML
    private TableColumn<AppointmentModel, Integer> colId;
    @FXML
    private TableColumn<AppointmentModel, String> colPatient;
    @FXML
    private TableColumn<AppointmentModel, String> colTime;
    @FXML
    private TableColumn<AppointmentModel, String> colType;
    @FXML
    private TableColumn<AppointmentModel, String> colDoctor;
    @FXML
    private TableColumn<AppointmentModel, String> colStatus;
    @FXML
    private TableColumn<AppointmentModel, Void> colAction;
    @FXML
    private Label lblStatus;

    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    public ConsultationController() {
        this.appointmentService = new AppointmentService();
        this.patientService = new PatientService();
        this.doctorService = new DoctorService();
    }

    @FXML
    public void initialize() {
        setupTable();
        loadAppointments();
    }

    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        addButtonToTable();
    }

    private void addButtonToTable() {
        Callback<TableColumn<AppointmentModel, Void>, TableCell<AppointmentModel, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<AppointmentModel, Void> call(final TableColumn<AppointmentModel, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Start Consultation");

                    {
                        btn.setStyle(
                                "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 11px;");
                        btn.setOnAction((event) -> {
                            AppointmentModel data = getTableView().getItems().get(getIndex());
                            openConsultation(data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
        colAction.setCellFactory(cellFactory);
    }

    @FXML
    private void handleRefresh() {
        loadAppointments();
    }

    private void loadAppointments() {
        try {
            List<Appointment> all = appointmentService.getAllAppointments();
            // Filter strictly for 'Scheduled' appointments only, or potentially 'In
            // Progress'
            List<AppointmentModel> models = all.stream()
                    .filter(a -> "Scheduled".equalsIgnoreCase(a.getStatus())
                            || "In Progress".equalsIgnoreCase(a.getStatus()))
                    .map(this::convertToModel)
                    .collect(Collectors.toList());

            tblAppointments.setItems(FXCollections.observableArrayList(models));
            lblStatus.setText("Loaded " + models.size() + " active appointments.");
        } catch (Exception e) {
            lblStatus.setText("Error loading appointments: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private AppointmentModel convertToModel(Appointment a) {
        String pName = "Unknown";
        String dName = "Unknown";
        try {
            Patient p = patientService.getAllPatients().stream()
                    .filter(pat -> pat.getPatientId().equals(a.getPatientId())).findFirst().orElse(null);
            if (p != null)
                pName = p.getFullName();

            Doctor d = doctorService.getAllDoctors().stream()
                    .filter(doc -> doc.getDoctorId().equals(a.getDoctorId())).findFirst().orElse(null);
            if (d != null)
                dName = d.getFullName();
        } catch (Exception e) {
            // Ignore lookup errors
        }

        return new AppointmentModel(
                a.getAppointmentId(),
                pName,
                a.getAppointmentDate().format(DateTimeFormatter.ofPattern("HH:mm")),
                a.getAppointmentType(),
                dName,
                a.getStatus(),
                a);
    }

    private void openConsultation(AppointmentModel model) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/healthcaremanagementsystem/appointment-details.fxml"));
            Parent root = loader.load();

            AppointmentDetailsController controller = loader.getController();
            controller.setAppointment(model.getOriginalAppointment());

            Stage stage = new Stage();
            stage.setTitle("Clinical Consultation - " + model.getPatientName());
            stage.setScene(new Scene(root));
            stage.show();

            // Refresh list on close
            stage.setOnHidden(e -> loadAppointments());

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to open consultation view: " + e.getMessage());
            alert.showAndWait();
        }
    }

    // Inner class for TableView
    public static class AppointmentModel {
        private final Integer id;
        private final String patientName;
        private final String time;
        private final String type;
        private final String doctorName;
        private final String status;
        private final Appointment originalAppointment;

        public AppointmentModel(Integer id, String patientName, String time, String type, String doctorName,
                String status, Appointment originalAppointment) {
            this.id = id;
            this.patientName = patientName;
            this.time = time;
            this.type = type;
            this.doctorName = doctorName;
            this.status = status;
            this.originalAppointment = originalAppointment;
        }

        public Integer getId() {
            return id;
        }

        public String getPatientName() {
            return patientName;
        }

        public String getTime() {
            return time;
        }

        public String getType() {
            return type;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public String getStatus() {
            return status;
        }

        public Appointment getOriginalAppointment() {
            return originalAppointment;
        }
    }
}
