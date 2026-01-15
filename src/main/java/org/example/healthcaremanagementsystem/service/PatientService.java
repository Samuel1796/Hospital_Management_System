package org.example.healthcaremanagementsystem.service;

import org.example.healthcaremanagementsystem.dao.PatientDAO;
import org.example.healthcaremanagementsystem.dao.PatientDAOImpl;
import org.example.healthcaremanagementsystem.model.Patient;
import org.example.healthcaremanagementsystem.util.CacheManager;
import org.example.healthcaremanagementsystem.util.SearchUtil;
import org.example.healthcaremanagementsystem.util.SortingUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Service layer for Patient business logic.
 * Follows Single Responsibility Principle by handling only patient-related business operations.
 * Acts as a bridge between Controller and DAO layers.
 * Implements caching and search optimization.
 *
 */
public class PatientService {
    
    private static final Logger logger = Logger.getLogger(PatientService.class.getName());
    private final PatientDAO patientDAO;
    private final CacheManager cacheManager;
    
    /**
     * Constructor initializes DAO and cache manager.
     * Follows Dependency Injection principle.
     */
    public PatientService() {
        this.patientDAO = new PatientDAOImpl();
        this.cacheManager = new CacheManager();
    }
    
    /**
     * Creates a new patient with validation.
     * 
     * @param patient Patient object to create
     * @return Created patient with generated ID
     * @throws Exception if validation fails or database operation fails
     */
    public Patient createPatient(Patient patient) throws Exception {
        // Validate patient data
        validatePatient(patient);
        
        // Check for duplicate email
        if (patientDAO.emailExists(patient.getEmail(), null)) {
            throw new IllegalArgumentException("A patient with email '" + patient.getEmail() + "' already exists.");
        }
        
        // Check for duplicate phone number
        if (patientDAO.phoneNumberExists(patient.getPhoneNumber(), null)) {
            throw new IllegalArgumentException("A patient with phone number '" + patient.getPhoneNumber() + "' already exists.");
        }
        
        // Reset sequence to ensure sequential IDs
        patientDAO.resetSequence();
        
        // Create patient in database
        Patient createdPatient = patientDAO.create(patient);
        
        // Cache the newly created patient
        cacheManager.cachePatient(createdPatient.getPatientId(), createdPatient);
        
        return createdPatient;
    }
    
    /**
     * Retrieves a patient by ID, checking cache first.
     * Demonstrates caching optimization pattern.
     * 
     * @param patientId Patient identifier
     * @return Optional containing Patient if found
     * @throws Exception if database operation fails
     */
    public Optional<Patient> getPatientById(Integer patientId) throws Exception {
        long startTime = System.currentTimeMillis();
        // Check cache first (hash-based lookup - O(1))
        Object cachedPatient = cacheManager.getPatient(patientId);
        if (cachedPatient != null) {
            long duration = System.currentTimeMillis() - startTime;
            logger.info(String.format("[CACHE] HIT - Patient (ID: %d) - %d ms - Hit Rate: %.2f%%", 
                patientId, duration, cacheManager.getPatientCacheHitRate()));
            return Optional.of((Patient) cachedPatient);
        }
        
        // Cache miss - query database
        logger.info(String.format("[CACHE] MISS - Patient (ID: %d)", patientId));
        Optional<Patient> patient = patientDAO.findById(patientId);
        
        // Cache the result for future lookups
        patient.ifPresent(p -> {
            cacheManager.cachePatient(p.getPatientId(), p);
            long duration = System.currentTimeMillis() - startTime;
            logger.info(String.format("[CACHE] STORED - Patient (ID: %d) - Total: %d ms", patientId, duration));
        });
        
        return patient;
    }
    
    /**
     * Retrieves all patients, sorted by name.
     * Applies sorting algorithm for ordered results.
     * 
     * @return Sorted list of all patients
     * @throws Exception if database operation fails
     */
    public List<Patient> getAllPatients() throws Exception {
        List<Patient> patients = patientDAO.findAll();
        
        // Sort patients by last name, then first name
        SortingUtil.sort(patients, (p1, p2) -> {
            int lastNameCompare = p1.getLastName().compareToIgnoreCase(p2.getLastName());
            if (lastNameCompare != 0) {
                return lastNameCompare;
            }
            return p1.getFirstName().compareToIgnoreCase(p2.getFirstName());
        });
        
        return patients;
    }
    
    /**
     * Updates an existing patient.
     * Invalidates cache to ensure data consistency.
     * 
     * @param patient Patient with updated information
     * @return true if update successful
     * @throws Exception if validation fails or database operation fails
     */
    public boolean updatePatient(Patient patient) throws Exception {
        validatePatient(patient);
        
        // Check for duplicate email (excluding current patient)
        if (patientDAO.emailExists(patient.getEmail(), patient.getPatientId())) {
            throw new IllegalArgumentException("A patient with email '" + patient.getEmail() + "' already exists.");
        }
        
        // Check for duplicate phone number (excluding current patient)
        if (patientDAO.phoneNumberExists(patient.getPhoneNumber(), patient.getPatientId())) {
            throw new IllegalArgumentException("A patient with phone number '" + patient.getPhoneNumber() + "' already exists.");
        }
        
        boolean updated = patientDAO.update(patient);
        
        if (updated) {
            // Invalidate cache to ensure fresh data on next access
            cacheManager.invalidatePatient(patient.getPatientId());
        }
        
        return updated;
    }
    
    /**
     * Deletes a patient by ID.
     * Resets sequence after deletion for sequential IDs.
     * 
     * @param patientId Patient identifier
     * @return true if deletion successful
     * @throws Exception if database operation fails
     */
    public boolean deletePatient(Integer patientId) throws Exception {
        boolean deleted = patientDAO.delete(patientId);
        
        if (deleted) {
            // Remove from cache
            cacheManager.invalidatePatient(patientId);
            // Reset sequence for sequential IDs
            patientDAO.resetSequence();
        }
        
        return deleted;
    }
    
    /**
     * Searches for patients by name (case-insensitive).
     * Uses optimized database search with indexing.
     * 
     * @param name Search term (first or last name)
     * @return List of matching patients
     * @throws Exception if database operation fails
     */
    public List<Patient> searchPatientsByName(String name) throws Exception {
        if (name == null || name.trim().isEmpty()) {
            return getAllPatients();
        }
        
        // Use DAO's optimized search (leverages database indexes)
        return patientDAO.searchByName(name);
    }
    
    /**
     * Retrieves patients with pagination.
     * 
     * @param page Page number (0-based)
     * @param pageSize Number of records per page
     * @return List of patients for the specified page
     * @throws Exception if database operation fails
     */
    public List<Patient> getPatientsPaginated(int page, int pageSize) throws Exception {
        return patientDAO.findAllPaginated(page, pageSize);
    }
    
    /**
     * Searches patients by name with pagination.
     * 
     * @param name Search term
     * @param page Page number (0-based)
     * @param pageSize Number of records per page
     * @return List of matching patients for the specified page
     * @throws Exception if database operation fails
     */
    public List<Patient> searchPatientsPaginated(String name, int page, int pageSize) throws Exception {
        if (name == null || name.trim().isEmpty()) {
            return getPatientsPaginated(page, pageSize);
        }
        return patientDAO.searchByNamePaginated(name, page, pageSize);
    }
    
    /**
     * Gets the total count of patients for pagination.
     * 
     * @return Total number of patients
     * @throws Exception if database operation fails
     */
    public int getTotalPatientCount() throws Exception {
        return patientDAO.getCount();
    }
    
    /**
     * Gets the count of patients matching a search term.
     * 
     * @param name Search term
     * @return Count of matching patients
     * @throws Exception if database operation fails
     */
    public int getSearchCount(String name) throws Exception {
        if (name == null || name.trim().isEmpty()) {
            return patientDAO.getCount();
        }
        return patientDAO.getSearchCount(name);
    }
    
    /**
     * Validates patient data before database operations.
     * 
     * @param patient Patient to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validatePatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null.");
        }
        
        if (patient.getFirstName() == null || patient.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient first name is required.");
        }
        
        if (patient.getLastName() == null || patient.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient last name is required.");
        }
        
        if (patient.getEmail() == null || patient.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient email is required.");
        }
        
        if (patient.getPhoneNumber() == null || patient.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient phone number is required.");
        }
        
        // Basic email validation
        if (!patient.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }
}

