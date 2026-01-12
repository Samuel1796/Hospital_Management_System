package org.example.healthcaremanagementsystem.dao;

import org.example.healthcaremanagementsystem.config.DatabaseConfig;
import org.example.healthcaremanagementsystem.model.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of PatientDAO interface.
 * Follows Single Responsibility Principle by handling only patient data access.

 */
public class PatientDAOImpl implements PatientDAO {
    
    private final DatabaseConfig dbConfig;
    
    /**
     * Constructor that initializes database configuration.
     */
    public PatientDAOImpl() {
        this.dbConfig = DatabaseConfig.getInstance();
    }
    
    @Override
    public Patient create(Patient patient) throws Exception {
        String sql = "INSERT INTO patients (first_name, last_name, email, phone_number, " +
                     "date_of_birth, address, gender, blood_group, emergency_contact, " +
                     "emergency_phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Set parameters using parameterized query
            pstmt.setString(1, patient.getFirstName());
            pstmt.setString(2, patient.getLastName());
            pstmt.setString(3, patient.getEmail());
            pstmt.setString(4, patient.getPhoneNumber());
            pstmt.setDate(5, patient.getDateOfBirth() != null ? 
                         Date.valueOf(patient.getDateOfBirth()) : null);
            pstmt.setString(6, patient.getAddress());
            pstmt.setString(7, patient.getGender());
            pstmt.setString(8, patient.getBloodGroup());
            pstmt.setString(9, patient.getEmergencyContact());
            pstmt.setString(10, patient.getEmergencyPhone());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating patient failed, no rows affected.");
            }
            
            // Retrieve generated patient ID
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    patient.setPatientId(generatedKeys.getInt(1));
                    return patient;
                } else {
                    throw new SQLException("Creating patient failed, no ID obtained.");
                }
            }
        }
    }
    
    @Override
    public Optional<Patient> findById(Integer patientId) throws Exception {
        String sql = "SELECT * FROM patients WHERE patient_id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, patientId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPatient(rs));
                }
            }
        }
        return Optional.empty();
    }
    
    @Override
    public List<Patient> findAll() throws Exception {
        String sql = "SELECT * FROM patients ORDER BY last_name, first_name";
        List<Patient> patients = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
        }
        return patients;
    }
    
    @Override
    public boolean update(Patient patient) throws Exception {
        String sql = "UPDATE patients SET first_name = ?, last_name = ?, email = ?, " +
                     "phone_number = ?, date_of_birth = ?, address = ?, gender = ?, " +
                     "blood_group = ?, emergency_contact = ?, emergency_phone = ? " +
                     "WHERE patient_id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, patient.getFirstName());
            pstmt.setString(2, patient.getLastName());
            pstmt.setString(3, patient.getEmail());
            pstmt.setString(4, patient.getPhoneNumber());
            pstmt.setDate(5, patient.getDateOfBirth() != null ? 
                         Date.valueOf(patient.getDateOfBirth()) : null);
            pstmt.setString(6, patient.getAddress());
            pstmt.setString(7, patient.getGender());
            pstmt.setString(8, patient.getBloodGroup());
            pstmt.setString(9, patient.getEmergencyContact());
            pstmt.setString(10, patient.getEmergencyPhone());
            pstmt.setInt(11, patient.getPatientId());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean delete(Integer patientId) throws Exception {
        String sql = "DELETE FROM patients WHERE patient_id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, patientId);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public List<Patient> searchByName(String name) throws Exception {
        // Case-insensitive search using ILIKE (PostgreSQL specific)
        // Index on first_name and last_name columns optimizes this query
        String sql = "SELECT * FROM patients WHERE LOWER(first_name) LIKE LOWER(?) " +
                     "OR LOWER(last_name) LIKE LOWER(?) ORDER BY last_name, first_name";
        
        List<Patient> patients = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + name + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(mapResultSetToPatient(rs));
                }
            }
        }
        return patients;
    }
    
    @Override
    public Optional<Patient> findByEmail(String email) throws Exception {
        String sql = "SELECT * FROM patients WHERE email = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPatient(rs));
                }
            }
        }
        return Optional.empty();
    }
    
    @Override
    public List<Patient> findByPhoneNumber(String phoneNumber) throws Exception {
        String sql = "SELECT * FROM patients WHERE phone_number = ?";
        List<Patient> patients = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, phoneNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(mapResultSetToPatient(rs));
                }
            }
        }
        return patients;
    }
    
    @Override
    public int getCount() throws Exception {
        String sql = "SELECT COUNT(*) FROM patients";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    @Override
    public boolean phoneNumberExists(String phoneNumber, Integer excludePatientId) throws Exception {
        String sql = excludePatientId != null 
            ? "SELECT COUNT(*) FROM patients WHERE phone_number = ? AND patient_id != ?"
            : "SELECT COUNT(*) FROM patients WHERE phone_number = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, phoneNumber);
            if (excludePatientId != null) {
                pstmt.setInt(2, excludePatientId);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean emailExists(String email, Integer excludePatientId) throws Exception {
        String sql = excludePatientId != null 
            ? "SELECT COUNT(*) FROM patients WHERE email = ? AND patient_id != ?"
            : "SELECT COUNT(*) FROM patients WHERE email = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            if (excludePatientId != null) {
                pstmt.setInt(2, excludePatientId);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    @Override
    public void resetSequence() throws Exception {
        try (Connection conn = dbConfig.getConnection()) {
            // Get the max ID or set to 0 if no records exist
            String getMaxSql = "SELECT COALESCE(MAX(patient_id), 0) FROM patients";
            int maxId = 0;
            
            try (PreparedStatement pstmt = conn.prepareStatement(getMaxSql);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    maxId = rs.getInt(1);
                }
            }
            
            // Reset sequence to max_id + 1 (or 1 if table is empty)
            String resetSql = "SELECT setval('patients_patient_id_seq', ?, false)";
            try (PreparedStatement pstmt = conn.prepareStatement(resetSql)) {
                pstmt.setInt(1, maxId + 1);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public List<Patient> findAllPaginated(int page, int pageSize) throws Exception {
        String sql = "SELECT * FROM patients ORDER BY last_name, first_name LIMIT ? OFFSET ?";
        List<Patient> patients = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, page * pageSize);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(mapResultSetToPatient(rs));
                }
            }
        }
        return patients;
    }
    
    @Override
    public List<Patient> searchByNamePaginated(String name, int page, int pageSize) throws Exception {
        String sql = "SELECT * FROM patients WHERE LOWER(first_name) LIKE LOWER(?) " +
                     "OR LOWER(last_name) LIKE LOWER(?) ORDER BY last_name, first_name LIMIT ? OFFSET ?";
        
        List<Patient> patients = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + name + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setInt(3, pageSize);
            pstmt.setInt(4, page * pageSize);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(mapResultSetToPatient(rs));
                }
            }
        }
        return patients;
    }
    
    @Override
    public int getSearchCount(String name) throws Exception {
        String sql = "SELECT COUNT(*) FROM patients WHERE LOWER(first_name) LIKE LOWER(?) " +
                     "OR LOWER(last_name) LIKE LOWER(?)";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + name + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
    /**
     * Maps a ResultSet row to a Patient object.
     * Encapsulates the mapping logic for reusability.
     * 
     * @param rs ResultSet containing patient data
     * @return Patient object mapped from ResultSet
     * @throws SQLException if database access error occurs
     */
    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setPatientId(rs.getInt("patient_id"));
        patient.setFirstName(rs.getString("first_name"));
        patient.setLastName(rs.getString("last_name"));
        patient.setEmail(rs.getString("email"));
        patient.setPhoneNumber(rs.getString("phone_number"));
        
        Date dob = rs.getDate("date_of_birth");
        if (dob != null) {
            patient.setDateOfBirth(dob.toLocalDate());
        }
        
        patient.setAddress(rs.getString("address"));
        patient.setGender(rs.getString("gender"));
        patient.setBloodGroup(rs.getString("blood_group"));
        patient.setEmergencyContact(rs.getString("emergency_contact"));
        patient.setEmergencyPhone(rs.getString("emergency_phone"));
        
        return patient;
    }
}

