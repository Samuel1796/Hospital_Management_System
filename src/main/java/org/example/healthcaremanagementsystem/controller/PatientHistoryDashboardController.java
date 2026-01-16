package org.example.healthcaremanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.example.healthcaremanagementsystem.model.Patient;
import org.example.healthcaremanagementsystem.service.PatientService;

import java.util.List;
import java.util.stream.Collectors;

public class PatientHistoryDashboardController {

    @FXML
    private ListView<PatientDisplay> listPatients;
    @FXML
    private TextField txtSearch;

    // Injected controller for the included FXML
    // The name must be the fx:id of the include + "Controller"
    @FXML
    private HistoryController historyViewController;

    private final PatientService patientService;
    private final ObservableList<PatientDisplay> patientList;
    private List<Patient> allPatients;

    public PatientHistoryDashboardController() {
        this.patientService = new PatientService();
        this.patientList = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        loadPatients();
        setupSearch();
        setupSelection();
    }

    private void loadPatients() {
        try {
            allPatients = patientService.getAllPatients();
            updateList(allPatients);
        } catch (Exception e) {
            e.printStackTrace(); // Handle error gracefully in real app
        }
    }

    private void updateList(List<Patient> patients) {
        patientList.clear();
        patientList.addAll(patients.stream()
                .map(PatientDisplay::new)
                .collect(Collectors.toList()));
        listPatients.setItems(patientList);
    }

    private void setupSearch() {
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                updateList(allPatients);
            } else {
                String lower = newValue.toLowerCase();
                List<Patient> filtered = allPatients.stream()
                        .filter(p -> p.getFirstName().toLowerCase().contains(lower) ||
                                p.getLastName().toLowerCase().contains(lower))
                        .collect(Collectors.toList());
                updateList(filtered);
            }
        });
    }

    private void setupSelection() {
        listPatients.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Initialize the embedded history view with the selected patient
                historyViewController.initData(newVal.getPatient().getPatientId(), "PATIENT");
            }
        });
    }

    // Helper class for displaying consistent strings in ListView
    private static class PatientDisplay {
        private final Patient patient;

        public PatientDisplay(Patient patient) {
            this.patient = patient;
        }

        public Patient getPatient() {
            return patient;
        }

        @Override
        public String toString() {
            return patient.getFullName() + " (ID: " + patient.getPatientId() + ")";
        }
    }
}
