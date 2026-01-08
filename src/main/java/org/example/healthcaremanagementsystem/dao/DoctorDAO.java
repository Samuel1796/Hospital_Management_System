package org.example.healthcaremanagementsystem.dao;

import org.example.healthcaremanagementsystem.model.Doctor;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Doctor entity.
 * Follows Interface Segregation Principle by defining only doctor-related operations.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public interface DoctorDAO {
    
    /**
     * Creates a new doctor record in the database.
     * 
     * @param doctor Doctor object containing doctor information
     * @return The created Doctor object with generated ID, or null if creation failed
     * @throws Exception if database operation fails
     */
    Doctor create(Doctor doctor) throws Exception;
    
    /**
     * Retrieves a doctor by their unique identifier.
     * 
     * @param doctorId Unique doctor identifier
     * @return Optional containing Doctor if found, empty otherwise
     * @throws Exception if database operation fails
     */
    Optional<Doctor> findById(Integer doctorId) throws Exception;
    
    /**
     * Retrieves all doctors from the database.
     * 
     * @return List of all Doctor objects
     * @throws Exception if database operation fails
     */
    List<Doctor> findAll() throws Exception;
    
    /**
     * Updates an existing doctor record.
     * 
     * @param doctor Doctor object with updated information
     * @return true if update was successful, false otherwise
     * @throws Exception if database operation fails
     */
    boolean update(Doctor doctor) throws Exception;
    
    /**
     * Deletes a doctor record by ID.
     * 
     * @param doctorId Unique doctor identifier
     * @return true if deletion was successful, false otherwise
     * @throws Exception if database operation fails
     */
    boolean delete(Integer doctorId) throws Exception;
    
    /**
     * Finds doctors by department ID.
     * 
     * @param departmentId Department identifier
     * @return List of doctors in the specified department
     * @throws Exception if database operation fails
     */
    List<Doctor> findByDepartment(Integer departmentId) throws Exception;
    
    /**
     * Finds doctors by specialization.
     * 
     * @param specialization Medical specialization
     * @return List of doctors with the specified specialization
     * @throws Exception if database operation fails
     */
    List<Doctor> findBySpecialization(String specialization) throws Exception;
    
    /**
     * Searches for doctors by name (case-insensitive).
     * 
     * @param name Doctor's first or last name (or partial match)
     * @return List of matching Doctor objects
     * @throws Exception if database operation fails
     */
    List<Doctor> searchByName(String name) throws Exception;
    
    /**
     * Gets the total count of doctors.
     * 
     * @return Total number of doctors
     * @throws Exception if database operation fails
     */
    int getCount() throws Exception;
    
    /**
     * Gets the count of active doctors.
     * 
     * @return Number of active doctors
     * @throws Exception if database operation fails
     */
    int getActiveCount() throws Exception;
}

