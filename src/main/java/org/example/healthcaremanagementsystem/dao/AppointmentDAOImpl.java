package org.example.healthcaremanagementsystem.dao;

import org.example.healthcaremanagementsystem.config.DatabaseConfig;
import org.example.healthcaremanagementsystem.model.Appointment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of AppointmentDAO interface.
 * Handles appointment database operations with parameterized queries.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class AppointmentDAOImpl implements AppointmentDAO {
    
    private final DatabaseConfig dbConfig;
    
    public AppointmentDAOImpl() {
        this.dbConfig = DatabaseConfig.getInstance();
    }
    
    @Override
    public Appointment create(Appointment appointment) throws Exception {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, " +
                     "appointment_type, status, notes, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, appointment.getPatientId());
            pstmt.setInt(2, appointment.getDoctorId());
            pstmt.setTimestamp(3, appointment.getAppointmentDate() != null ? 
                              Timestamp.valueOf(appointment.getAppointmentDate()) : null);
            pstmt.setString(4, appointment.getAppointmentType());
            pstmt.setString(5, appointment.getStatus());
            pstmt.setString(6, appointment.getNotes());
            pstmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating appointment failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    appointment.setAppointmentId(generatedKeys.getInt(1));
                    return appointment;
                } else {
                    throw new SQLException("Creating appointment failed, no ID obtained.");
                }
            }
        }
    }
    
    @Override
    public Optional<Appointment> findById(Integer appointmentId) throws Exception {
        String sql = "SELECT * FROM appointments WHERE appointment_id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, appointmentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAppointment(rs));
                }
            }
        }
        return Optional.empty();
    }
    
    @Override
    public List<Appointment> findAll() throws Exception {
        String sql = "SELECT * FROM appointments ORDER BY appointment_date DESC";
        List<Appointment> appointments = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        }
        return appointments;
    }
    
    @Override
    public boolean update(Appointment appointment) throws Exception {
        String sql = "UPDATE appointments SET patient_id = ?, doctor_id = ?, " +
                     "appointment_date = ?, appointment_type = ?, status = ?, notes = ? " +
                     "WHERE appointment_id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, appointment.getPatientId());
            pstmt.setInt(2, appointment.getDoctorId());
            pstmt.setTimestamp(3, appointment.getAppointmentDate() != null ? 
                              Timestamp.valueOf(appointment.getAppointmentDate()) : null);
            pstmt.setString(4, appointment.getAppointmentType());
            pstmt.setString(5, appointment.getStatus());
            pstmt.setString(6, appointment.getNotes());
            pstmt.setInt(7, appointment.getAppointmentId());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean delete(Integer appointmentId) throws Exception {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, appointmentId);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public List<Appointment> findByPatientId(Integer patientId) throws Exception {
        String sql = "SELECT * FROM appointments WHERE patient_id = ? ORDER BY appointment_date DESC";
        List<Appointment> appointments = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, patientId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapResultSetToAppointment(rs));
                }
            }
        }
        return appointments;
    }
    
    @Override
    public List<Appointment> findByDoctorId(Integer doctorId) throws Exception {
        String sql = "SELECT * FROM appointments WHERE doctor_id = ? ORDER BY appointment_date DESC";
        List<Appointment> appointments = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, doctorId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapResultSetToAppointment(rs));
                }
            }
        }
        return appointments;
    }
    
    @Override
    public List<Appointment> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        String sql = "SELECT * FROM appointments WHERE appointment_date BETWEEN ? AND ? " +
                     "ORDER BY appointment_date";
        List<Appointment> appointments = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, Timestamp.valueOf(startDate));
            pstmt.setTimestamp(2, Timestamp.valueOf(endDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapResultSetToAppointment(rs));
                }
            }
        }
        return appointments;
    }
    
    @Override
    public int getCount() throws Exception {
        String sql = "SELECT COUNT(*) FROM appointments";
        
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
    public int getCountThisMonth() throws Exception {
        String sql = "SELECT COUNT(*) FROM appointments WHERE " +
                     "EXTRACT(MONTH FROM appointment_date) = EXTRACT(MONTH FROM CURRENT_DATE) AND " +
                     "EXTRACT(YEAR FROM appointment_date) = EXTRACT(YEAR FROM CURRENT_DATE)";
        
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
    public void resetSequence() throws Exception {
        try (Connection conn = dbConfig.getConnection()) {
            // Get the max ID or set to 0 if no records exist
            String getMaxSql = "SELECT COALESCE(MAX(appointment_id), 0) FROM appointments";
            int maxId = 0;
            
            try (PreparedStatement pstmt = conn.prepareStatement(getMaxSql);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    maxId = rs.getInt(1);
                }
            }
            
            // Reset sequence to max_id + 1 (or 1 if table is empty)
            String resetSql = "SELECT setval('appointments_appointment_id_seq', ?, false)";
            try (PreparedStatement pstmt = conn.prepareStatement(resetSql)) {
                pstmt.setInt(1, maxId + 1);
                pstmt.execute();
            }
        }
    }
    
    /**
     * Maps a ResultSet row to an Appointment object.
     * 
     * @param rs ResultSet containing appointment data
     * @return Appointment object mapped from ResultSet
     * @throws SQLException if database access error occurs
     */
    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(rs.getInt("appointment_id"));
        appointment.setPatientId(rs.getInt("patient_id"));
        appointment.setDoctorId(rs.getInt("doctor_id"));
        
        Timestamp appointmentDate = rs.getTimestamp("appointment_date");
        if (appointmentDate != null) {
            appointment.setAppointmentDate(appointmentDate.toLocalDateTime());
        }
        
        appointment.setAppointmentType(rs.getString("appointment_type"));
        appointment.setStatus(rs.getString("status"));
        appointment.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            appointment.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return appointment;
    }
}

