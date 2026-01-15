package org.example.healthcaremanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.example.healthcaremanagementsystem.model.Doctor;
import org.example.healthcaremanagementsystem.service.DoctorService;

import java.util.List;
import java.util.stream.Collectors;

public class DoctorHistoryDashboardController {

    @FXML
    private ListView<DoctorDisplay> listDoctors;
    @FXML
    private TextField txtSearch;

    // Injected controller for the included FXML
    @FXML
    private HistoryController historyViewController;

    private final DoctorService doctorService;
    private final ObservableList<DoctorDisplay> doctorList;
    private List<Doctor> allDoctors;

    public DoctorHistoryDashboardController() {
        this.doctorService = new DoctorService();
        this.doctorList = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        loadDoctors();
        setupSearch();
        setupSelection();
    }

    private void loadDoctors() {
        try {
            allDoctors = doctorService.getAllDoctors();
            updateList(allDoctors);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateList(List<Doctor> doctors) {
        doctorList.clear();
        doctorList.addAll(doctors.stream()
                .map(DoctorDisplay::new)
                .collect(Collectors.toList()));
        listDoctors.setItems(doctorList);
    }

    private void setupSearch() {
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                updateList(allDoctors);
            } else {
                String lower = newValue.toLowerCase();
                List<Doctor> filtered = allDoctors.stream()
                        .filter(d -> d.getFirstName().toLowerCase().contains(lower) ||
                                d.getLastName().toLowerCase().contains(lower) ||
                                d.getSpecialization().toLowerCase().contains(lower))
                        .collect(Collectors.toList());
                updateList(filtered);
            }
        });
    }

    private void setupSelection() {
        listDoctors.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Initialize the embedded history view with the selected doctor
                historyViewController.initData(newVal.getDoctor().getDoctorId(), "DOCTOR");
            }
        });
    }

    // Helper class for displaying consistent strings in ListView
    private static class DoctorDisplay {
        private final Doctor doctor;

        public DoctorDisplay(Doctor doctor) {
            this.doctor = doctor;
        }

        public Doctor getDoctor() {
            return doctor;
        }

        @Override
        public String toString() {
            return doctor.getFullName() + " (" + doctor.getSpecialization() + ")";
        }
    }
}
