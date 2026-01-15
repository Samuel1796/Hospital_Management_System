package org.example.healthcaremanagementsystem.dao;

import org.example.healthcaremanagementsystem.model.Appointment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Appointment entity.
 * Defines appointment-related database operations.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public interface AppointmentDAO {

    /**
     * Creates a new appointment record.
     * 
     * @param appointment Appointment object
     * @return Created Appointment with generated ID
     * @throws Exception if database operation fails
     */
    Appointment create(Appointment appointment) throws Exception;

    /**
     * Retrieves an appointment by ID.
     * 
     * @param appointmentId Unique appointment identifier
     * @return Optional containing Appointment if found
     * @throws Exception if database operation fails
     */
    Optional<Appointment> findById(Integer appointmentId) throws Exception;

    /**
     * Retrieves all appointments.
     * 
     * @return List of all appointments
     * @throws Exception if database operation fails
     */
    List<Appointment> findAll() throws Exception;

    /**
     * Retrieves all appointments with pagination.
     * 
     * @param page     Page number (0-based)
     * @param pageSize Number of records per page
     * @return List of appointments for the specified page
     * @throws Exception if database operation fails
     */
    List<Appointment> findAllPaginated(int page, int pageSize) throws Exception;

    /**
     * Updates an existing appointment.
     * 
     * @param appointment Appointment with updated information
     * @return true if update successful
     * @throws Exception if database operation fails
     */
    boolean update(Appointment appointment) throws Exception;

    /**
     * Deletes an appointment by ID.
     * 
     * @param appointmentId Unique appointment identifier
     * @return true if deletion successful
     * @throws Exception if database operation fails
     */
    boolean delete(Integer appointmentId) throws Exception;

    /**
     * Finds appointments by patient ID.
     * 
     * @param patientId Patient identifier
     * @return List of appointments for the patient
     * @throws Exception if database operation fails
     */
    List<Appointment> findByPatientId(Integer patientId) throws Exception;

    /**
     * Finds appointments by doctor ID.
     * 
     * @param doctorId Doctor identifier
     * @return List of appointments for the doctor
     * @throws Exception if database operation fails
     */
    List<Appointment> findByDoctorId(Integer doctorId) throws Exception;

    /**
     * Finds appointments within a date range.
     * 
     * @param startDate Start of date range
     * @param endDate   End of date range
     * @return List of appointments in the date range
     * @throws Exception if database operation fails
     */
    List<Appointment> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws Exception;

    /**
     * Gets the total count of appointments.
     * 
     * @return Total number of appointments
     * @throws Exception if database operation fails
     */
    int getCount() throws Exception;

    /**
     * Gets the count of appointments for the current month.
     * 
     * @return Number of appointments this month
     * @throws Exception if database operation fails
     */
    int getCountThisMonth() throws Exception;

    /**
     * Resets the appointment sequence to start from the next available ID.
     * 
     * @throws Exception if database operation fails
     */
    void resetSequence() throws Exception;
}
