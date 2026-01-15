package org.example.healthcaremanagementsystem.dao;

import org.example.healthcaremanagementsystem.config.DatabaseConfig;
import org.example.healthcaremanagementsystem.model.Prescription;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrescriptionDAOImpl implements PrescriptionDAO {
    private static final Logger logger = Logger.getLogger(PrescriptionDAOImpl.class.getName());

    public PrescriptionDAOImpl() {
        createTableIfNotExists();
        alterTableToAddColumns();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS prescriptions (" +
                "prescription_id SERIAL PRIMARY KEY, " +
                "patient_id INT NOT NULL, " +
                "doctor_id INT NOT NULL, " +
                "appointment_id INT, " +
                "prescription_date DATE NOT NULL, " +
                "diagnosis TEXT, " +
                "instructions TEXT, " +
                "medication_name VARCHAR(255), " +
                "dosage VARCHAR(100), " +
                "frequency VARCHAR(100), " +
                "duration VARCHAR(100), " +
                "valid_until DATE)";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating prescriptions table", e);
        }
    }

    private void alterTableToAddColumns() {
        String[] columns = {
                "ALTER TABLE prescriptions ADD COLUMN IF NOT EXISTS medication_name VARCHAR(255)",
                "ALTER TABLE prescriptions ADD COLUMN IF NOT EXISTS dosage VARCHAR(100)",
                "ALTER TABLE prescriptions ADD COLUMN IF NOT EXISTS frequency VARCHAR(100)",
                "ALTER TABLE prescriptions ADD COLUMN IF NOT EXISTS duration VARCHAR(100)",
                "ALTER TABLE prescriptions ADD COLUMN IF NOT EXISTS diagnosis TEXT",
                "ALTER TABLE prescriptions ADD COLUMN IF NOT EXISTS valid_until DATE",
                "ALTER TABLE prescriptions ADD COLUMN IF NOT EXISTS appointment_id INT"
        };

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                Statement stmt = conn.createStatement()) {
            for (String sql : columns) {
                try {
                    stmt.execute(sql);
                } catch (SQLException e) {
                    // Ignore if column already exists (for older Postgres without IF NOT EXISTS)
                    logger.warning("Could not add column: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating prescriptions table schema", e);
        }
    }

    @Override
    public List<Prescription> getPrescriptionsByPatientId(int patientId) {
        List<Prescription> list = new ArrayList<>();
        String sql = "SELECT * FROM prescriptions WHERE patient_id = ? ORDER BY prescription_date DESC";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToPrescription(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching prescriptions for patient " + patientId, e);
        }
        return list;
    }

    @Override
    public List<Prescription> getPrescriptionsByDoctorId(int doctorId) {
        List<Prescription> list = new ArrayList<>();
        String sql = "SELECT * FROM prescriptions WHERE doctor_id = ? ORDER BY prescription_date DESC";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToPrescription(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching prescriptions for doctor " + doctorId, e);
        }
        return list;
    }

    @Override
    public void addPrescription(Prescription prescription) {
        String sql = "INSERT INTO prescriptions (patient_id, doctor_id, appointment_id, prescription_date, " +
                "diagnosis, instructions, medication_name, dosage, frequency, duration, valid_until) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, prescription.getPatientId());
            pstmt.setInt(2, prescription.getDoctorId());
            if (prescription.getAppointmentId() != null) {
                pstmt.setInt(3, prescription.getAppointmentId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setDate(4, java.sql.Date.valueOf(prescription.getPrescriptionDate()));
            pstmt.setString(5, prescription.getDiagnosis());
            pstmt.setString(6, prescription.getInstructions());
            pstmt.setString(7, prescription.getMedicationName());
            pstmt.setString(8, prescription.getDosage());
            pstmt.setString(9, prescription.getFrequency());
            pstmt.setString(10, prescription.getDuration());
            if (prescription.getValidUntil() != null) {
                pstmt.setDate(11, java.sql.Date.valueOf(prescription.getValidUntil()));
            } else {
                pstmt.setNull(11, Types.DATE);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding prescription", e);
            throw new RuntimeException("Failed to add prescription", e);
        }
    }

    private Prescription mapResultSetToPrescription(ResultSet rs) throws SQLException {
        Prescription p = new Prescription();
        p.setPrescriptionId(rs.getInt("prescription_id"));
        p.setPatientId(rs.getInt("patient_id"));
        p.setDoctorId(rs.getInt("doctor_id"));
        p.setAppointmentId(rs.getObject("appointment_id") != null ? rs.getInt("appointment_id") : null);
        p.setPrescriptionDate(rs.getDate("prescription_date").toLocalDate());
        p.setDiagnosis(rs.getString("diagnosis"));
        p.setInstructions(rs.getString("instructions"));
        p.setMedicationName(rs.getString("medication_name"));
        p.setDosage(rs.getString("dosage"));
        p.setFrequency(rs.getString("frequency"));
        p.setDuration(rs.getString("duration"));
        if (rs.getDate("valid_until") != null) {
            p.setValidUntil(rs.getDate("valid_until").toLocalDate());
        }
        return p;
    }
}
