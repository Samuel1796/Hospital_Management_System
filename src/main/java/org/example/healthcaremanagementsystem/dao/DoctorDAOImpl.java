package org.example.healthcaremanagementsystem.dao;

import org.example.healthcaremanagementsystem.config.DatabaseConfig;
import org.example.healthcaremanagementsystem.model.Doctor;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of DoctorDAO interface.
 * Handles all database operations for Doctor entity using parameterized queries.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class DoctorDAOImpl implements DoctorDAO {
    
    private final DatabaseConfig dbConfig;
    
    /**
     * Constructor that initializes database configuration.
     */
    public DoctorDAOImpl() {
        this.dbConfig = DatabaseConfig.getInstance();
    }
    
    @Override
    public Doctor create(Doctor doctor) throws Exception {
        String sql = "INSERT INTO doctors (first_name, last_name, email, phone_number, " +
                     "specialization, department_id, license_number, hire_date, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, doctor.getFirstName());
            pstmt.setString(2, doctor.getLastName());
            pstmt.setString(3, doctor.getEmail());
            pstmt.setString(4, doctor.getPhoneNumber());
            pstmt.setString(5, doctor.getSpecialization());
            pstmt.setInt(6, doctor.getDepartmentId());
            pstmt.setString(7, doctor.getLicenseNumber());
            pstmt.setDate(8, doctor.getHireDate() != null ? 
                         Date.valueOf(doctor.getHireDate()) : null);
            pstmt.setString(9, doctor.getStatus());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating doctor failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    doctor.setDoctorId(generatedKeys.getInt(1));
                    return doctor;
                } else {
                    throw new SQLException("Creating doctor failed, no ID obtained.");
                }
            }
        }
    }
    
    @Override
    public Optional<Doctor> findById(Integer doctorId) throws Exception {
        String sql = "SELECT * FROM doctors WHERE doctor_id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, doctorId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToDoctor(rs));
                }
            }
        }
        return Optional.empty();
    }
    
    @Override
    public List<Doctor> findAll() throws Exception {
        String sql = "SELECT * FROM doctors ORDER BY last_name, first_name";
        List<Doctor> doctors = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                doctors.add(mapResultSetToDoctor(rs));
            }
        }
        return doctors;
    }
    
    @Override
    public boolean update(Doctor doctor) throws Exception {
        String sql = "UPDATE doctors SET first_name = ?, last_name = ?, email = ?, " +
                     "phone_number = ?, specialization = ?, department_id = ?, " +
                     "license_number = ?, hire_date = ?, status = ? WHERE doctor_id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, doctor.getFirstName());
            pstmt.setString(2, doctor.getLastName());
            pstmt.setString(3, doctor.getEmail());
            pstmt.setString(4, doctor.getPhoneNumber());
            pstmt.setString(5, doctor.getSpecialization());
            pstmt.setInt(6, doctor.getDepartmentId());
            pstmt.setString(7, doctor.getLicenseNumber());
            pstmt.setDate(8, doctor.getHireDate() != null ? 
                         Date.valueOf(doctor.getHireDate()) : null);
            pstmt.setString(9, doctor.getStatus());
            pstmt.setInt(10, doctor.getDoctorId());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean delete(Integer doctorId) throws Exception {
        String sql = "DELETE FROM doctors WHERE doctor_id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, doctorId);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public List<Doctor> findByDepartment(Integer departmentId) throws Exception {
        String sql = "SELECT * FROM doctors WHERE department_id = ? ORDER BY last_name, first_name";
        List<Doctor> doctors = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, departmentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    doctors.add(mapResultSetToDoctor(rs));
                }
            }
        }
        return doctors;
    }
    
    @Override
    public List<Doctor> findBySpecialization(String specialization) throws Exception {
        String sql = "SELECT * FROM doctors WHERE specialization = ? ORDER BY last_name, first_name";
        List<Doctor> doctors = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, specialization);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    doctors.add(mapResultSetToDoctor(rs));
                }
            }
        }
        return doctors;
    }
    
    @Override
    public List<Doctor> searchByName(String name) throws Exception {
        String sql = "SELECT * FROM doctors WHERE LOWER(first_name) LIKE LOWER(?) " +
                     "OR LOWER(last_name) LIKE LOWER(?) ORDER BY last_name, first_name";
        
        List<Doctor> doctors = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + name + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    doctors.add(mapResultSetToDoctor(rs));
                }
            }
        }
        return doctors;
    }
    
    /**
     * Maps a ResultSet row to a Doctor object.
     * 
     * @param rs ResultSet containing doctor data
     * @return Doctor object mapped from ResultSet
     * @throws SQLException if database access error occurs
     */
    private Doctor mapResultSetToDoctor(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(rs.getInt("doctor_id"));
        doctor.setFirstName(rs.getString("first_name"));
        doctor.setLastName(rs.getString("last_name"));
        doctor.setEmail(rs.getString("email"));
        doctor.setPhoneNumber(rs.getString("phone_number"));
        doctor.setSpecialization(rs.getString("specialization"));
        doctor.setDepartmentId(rs.getInt("department_id"));
        doctor.setLicenseNumber(rs.getString("license_number"));
        
        Date hireDate = rs.getDate("hire_date");
        if (hireDate != null) {
            doctor.setHireDate(hireDate.toLocalDate());
        }
        
        doctor.setStatus(rs.getString("status"));
        
        return doctor;
    }
}

