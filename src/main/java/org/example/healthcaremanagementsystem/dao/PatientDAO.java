package org.example.healthcaremanagementsystem.dao;

import org.example.healthcaremanagementsystem.model.Patient;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Patient entity.
 * Follows Interface Segregation Principle by defining only patient-related operations.
 * Supports CRUD operations and search functionality.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public interface PatientDAO {
    
    /**
     * Creates a new patient record in the database.
     * 
     * @param patient Patient object containing patient information
     * @return The created Patient object with generated ID, or null if creation failed
     * @throws Exception if database operation fails
     */
    Patient create(Patient patient) throws Exception;
    
    /**
     * Retrieves a patient by their unique identifier.
     * 
     * @param patientId Unique patient identifier
     * @return Optional containing Patient if found, empty otherwise
     * @throws Exception if database operation fails
     */
    Optional<Patient> findById(Integer patientId) throws Exception;
    
    /**
     * Retrieves all patients from the database.
     * 
     * @return List of all Patient objects
     * @throws Exception if database operation fails
     */
    List<Patient> findAll() throws Exception;
    
    /**
     * Updates an existing patient record.
     * 
     * @param patient Patient object with updated information
     * @return true if update was successful, false otherwise
     * @throws Exception if database operation fails
     */
    boolean update(Patient patient) throws Exception;
    
    /**
     * Deletes a patient record by ID.
     * 
     * @param patientId Unique patient identifier
     * @return true if deletion was successful, false otherwise
     * @throws Exception if database operation fails
     */
    boolean delete(Integer patientId) throws Exception;
    
    /**
     * Searches for patients by name (case-insensitive).
     * Uses database indexing for optimized search performance.
     * 
     * @param name Patient's first or last name (or partial match)
     * @return List of matching Patient objects
     * @throws Exception if database operation fails
     */
    List<Patient> searchByName(String name) throws Exception;
    
    /**
     * Searches for a patient by email address.
     * 
     * @param email Patient's email address
     * @return Optional containing Patient if found, empty otherwise
     * @throws Exception if database operation fails
     */
    Optional<Patient> findByEmail(String email) throws Exception;
    
    /**
     * Searches for patients by phone number.
     * 
     * @param phoneNumber Patient's phone number
     * @return List of matching Patient objects
     * @throws Exception if database operation fails
     */
    List<Patient> findByPhoneNumber(String phoneNumber) throws Exception;
}

