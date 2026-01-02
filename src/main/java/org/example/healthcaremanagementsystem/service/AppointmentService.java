package org.example.healthcaremanagementsystem.service;

import org.example.healthcaremanagementsystem.dao.AppointmentDAO;
import org.example.healthcaremanagementsystem.dao.AppointmentDAOImpl;
import org.example.healthcaremanagementsystem.model.Appointment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for Appointment business logic.
 * Handles appointment scheduling and management operations.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class AppointmentService {
    
    private final AppointmentDAO appointmentDAO;
    
    public AppointmentService() {
        this.appointmentDAO = new AppointmentDAOImpl();
    }
    
    public Appointment createAppointment(Appointment appointment) throws Exception {
        validateAppointment(appointment);
        return appointmentDAO.create(appointment);
    }
    
    public Optional<Appointment> getAppointmentById(Integer appointmentId) throws Exception {
        return appointmentDAO.findById(appointmentId);
    }
    
    public List<Appointment> getAllAppointments() throws Exception {
        return appointmentDAO.findAll();
    }
    
    public boolean updateAppointment(Appointment appointment) throws Exception {
        validateAppointment(appointment);
        return appointmentDAO.update(appointment);
    }
    
    public boolean deleteAppointment(Integer appointmentId) throws Exception {
        return appointmentDAO.delete(appointmentId);
    }
    
    public List<Appointment> getAppointmentsByPatient(Integer patientId) throws Exception {
        return appointmentDAO.findByPatientId(patientId);
    }
    
    public List<Appointment> getAppointmentsByDoctor(Integer doctorId) throws Exception {
        return appointmentDAO.findByDoctorId(doctorId);
    }
    
    public List<Appointment> getAppointmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        return appointmentDAO.findByDateRange(startDate, endDate);
    }
    
    private void validateAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null.");
        }
        
        if (appointment.getPatientId() == null) {
            throw new IllegalArgumentException("Patient ID is required.");
        }
        
        if (appointment.getDoctorId() == null) {
            throw new IllegalArgumentException("Doctor ID is required.");
        }
        
        if (appointment.getAppointmentDate() == null) {
            throw new IllegalArgumentException("Appointment date is required.");
        }
        
        if (appointment.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment date cannot be in the past.");
        }
    }
}

