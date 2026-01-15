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
 * Follows Single Responsibility Principle by handling only patient-related
 * business operations.
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
        this.cacheManager = CacheManager.getInstance();
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
            throw new IllegalArgumentException(
                    "A patient with phone number '" + patient.getPhoneNumber() + "' already exists.");
        }

        // Reset sequence to ensure sequential IDs
        patientDAO.resetSequence();

        // Create patient in database
        Patient createdPatient = patientDAO.create(patient);

        // Cache the newly created patient
        cacheManager.cachePatient(createdPatient.getPatientId(), createdPatient);

        // Invalidate list caches
        cacheManager.invalidateQuery("all_patients");
        cacheManager.invalidateQueryPattern("patients_page_");

        return createdPatient;
    }

    // ... getPatientById is fine ...

    public List<Patient> getAllPatients() throws Exception {
        long startTime = System.currentTimeMillis();
        @SuppressWarnings("unchecked")
        List<Patient> cachedPatients = (List<Patient>) cacheManager.getQuery("all_patients");
        if (cachedPatients != null) {
            long duration = System.currentTimeMillis() - startTime;
            logger.info(
                    String.format("[CACHE] HIT - All Patients - %d records - %d ms", cachedPatients.size(), duration));
            return cachedPatients;
        }

        logger.info("[CACHE] MISS - All Patients - Fetching from DB");
        List<Patient> patients = patientDAO.findAll();

        // Sort patients by last name, then first name
        SortingUtil.sort(patients, (p1, p2) -> {
            int lastNameCompare = p1.getLastName().compareToIgnoreCase(p2.getLastName());
            if (lastNameCompare != 0) {
                return lastNameCompare;
            }
            return p1.getFirstName().compareToIgnoreCase(p2.getFirstName());
        });

        cacheManager.cacheQuery("all_patients", patients);
        long duration = System.currentTimeMillis() - startTime;
        logger.info(String.format("[CACHE] STORED - All Patients - %d records - %d ms", patients.size(), duration));
        return patients;
    }

    public boolean updatePatient(Patient patient) throws Exception {
        validatePatient(patient);

        // Check for duplicate email (excluding current patient)
        if (patientDAO.emailExists(patient.getEmail(), patient.getPatientId())) {
            throw new IllegalArgumentException("A patient with email '" + patient.getEmail() + "' already exists.");
        }

        // Check for duplicate phone number (excluding current patient)
        if (patientDAO.phoneNumberExists(patient.getPhoneNumber(), patient.getPatientId())) {
            throw new IllegalArgumentException(
                    "A patient with phone number '" + patient.getPhoneNumber() + "' already exists.");
        }

        boolean updated = patientDAO.update(patient);

        if (updated) {
            // Invalidate cache to ensure data consistency
            cacheManager.invalidatePatient(patient.getPatientId());
            cacheManager.invalidateQuery("all_patients");
            cacheManager.invalidateQueryPattern("patients_page_");
        }

        return updated;
    }

    public boolean deletePatient(Integer patientId) throws Exception {
        boolean deleted = patientDAO.delete(patientId);

        if (deleted) {
            // Remove from cache
            cacheManager.invalidatePatient(patientId);
            cacheManager.invalidateQuery("all_patients");
            cacheManager.invalidateQueryPattern("patients_page_");
            // Reset sequence for sequential IDs
            patientDAO.resetSequence();
        }

        return deleted;
    }

    // ... searchPatientsByName is fine ...

    public List<Patient> getPatientsPaginated(int page, int pageSize) throws Exception {
        long startTime = System.currentTimeMillis();
        String cacheKey = "patients_page_" + page + "_size_" + pageSize;

        @SuppressWarnings("unchecked")
        List<Patient> cached = (List<Patient>) cacheManager.getQuery(cacheKey);

        if (cached != null) {
            long duration = System.currentTimeMillis() - startTime;
            logger.info(String.format("[CACHE] HIT - Patients Page %d - %d records - %d ms", page, cached.size(),
                    duration));
            return cached;
        }

        logger.info(String.format("[CACHE] MISS - Patients Page %d - Fetching from DB", page));
        List<Patient> patients = patientDAO.findAllPaginated(page, pageSize);

        cacheManager.cacheQuery(cacheKey, patients);
        long duration = System.currentTimeMillis() - startTime;
        logger.info(String.format("[CACHE] STORED - Patients Page %d - %d records - %d ms", page, patients.size(),
                duration));

        return patients;
    }

    /**
     * Searches patients by name with pagination.
     * 
     * @param name     Search term
     * @param page     Page number (0-based)
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
