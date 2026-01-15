package org.example.healthcaremanagementsystem.dao;

import org.example.healthcaremanagementsystem.model.Prescription;
import java.util.List;

public interface PrescriptionDAO {
    List<Prescription> getPrescriptionsByPatientId(int patientId);

    List<Prescription> getPrescriptionsByDoctorId(int doctorId);

    void addPrescription(Prescription prescription);
}
