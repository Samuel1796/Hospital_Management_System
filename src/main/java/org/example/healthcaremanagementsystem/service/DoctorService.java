package org.example.healthcaremanagementsystem.service;

import org.example.healthcaremanagementsystem.dao.DoctorDAO;
import org.example.healthcaremanagementsystem.dao.DoctorDAOImpl;
import org.example.healthcaremanagementsystem.model.Doctor;
import org.example.healthcaremanagementsystem.util.CacheManager;
import org.example.healthcaremanagementsystem.util.SortingUtil;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Doctor business logic.
 * Handles doctor-related operations with caching and validation.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class DoctorService {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(DoctorService.class.getName());
    private final DoctorDAO doctorDAO;
    private final CacheManager cacheManager;

    public DoctorService() {
        this.doctorDAO = new DoctorDAOImpl();
        this.cacheManager = CacheManager.getInstance();
    }

    public Doctor createDoctor(Doctor doctor) throws Exception {
        validateDoctor(doctor);

        if (doctorDAO.emailExists(doctor.getEmail(), null)) {
            throw new IllegalArgumentException("A doctor with email '" + doctor.getEmail() + "' already exists.");
        }

        if (doctorDAO.licenseNumberExists(doctor.getLicenseNumber(), null)) {
            throw new IllegalArgumentException(
                    "A doctor with license number '" + doctor.getLicenseNumber() + "' already exists.");
        }

        if (doctorDAO.phoneNumberExists(doctor.getPhoneNumber(), null)) {
            throw new IllegalArgumentException(
                    "A doctor with phone number '" + doctor.getPhoneNumber() + "' already exists.");
        }

        doctorDAO.resetSequence();

        Doctor createdDoctor = doctorDAO.create(doctor);
        cacheManager.cacheDoctor(createdDoctor.getDoctorId(), createdDoctor);

        // Invalidate list caches
        cacheManager.invalidateQuery("all_doctors");
        cacheManager.invalidateQueryPattern("doctors_page_");

        return createdDoctor;
    }

    public Optional<Doctor> getDoctorById(Integer doctorId) throws Exception {
        // ... logging could be added here too, but prioritized list caches ...
        Object cachedDoctor = cacheManager.getDoctor(doctorId);
        if (cachedDoctor != null) {
            return Optional.of((Doctor) cachedDoctor);
        }

        Optional<Doctor> doctor = doctorDAO.findById(doctorId);
        doctor.ifPresent(d -> cacheManager.cacheDoctor(d.getDoctorId(), d));

        return doctor;
    }

    public List<Doctor> getAllDoctors() throws Exception {
        long startTime = System.currentTimeMillis();
        @SuppressWarnings("unchecked")
        List<Doctor> cachedDoctors = (List<Doctor>) cacheManager.getQuery("all_doctors");
        if (cachedDoctors != null) {
            long duration = System.currentTimeMillis() - startTime;
            logger.info(
                    String.format("[CACHE] HIT - All Doctors - %d records - %d ms", cachedDoctors.size(), duration));
            return cachedDoctors;
        }

        logger.info("[CACHE] MISS - All Doctors - Fetching from DB");
        List<Doctor> doctors = doctorDAO.findAll();

        SortingUtil.sort(doctors, (d1, d2) -> {
            int lastNameCompare = d1.getLastName().compareToIgnoreCase(d2.getLastName());
            if (lastNameCompare != 0) {
                return lastNameCompare;
            }
            return d1.getFirstName().compareToIgnoreCase(d2.getFirstName());
        });

        cacheManager.cacheQuery("all_doctors", doctors);
        long duration = System.currentTimeMillis() - startTime;
        logger.info(String.format("[CACHE] STORED - All Doctors - %d records - %d ms", doctors.size(), duration));
        return doctors;
    }

    public boolean updateDoctor(Doctor doctor) throws Exception {
        validateDoctor(doctor);

        if (doctorDAO.emailExists(doctor.getEmail(), doctor.getDoctorId())) {
            throw new IllegalArgumentException("A doctor with email '" + doctor.getEmail() + "' already exists.");
        }

        if (doctorDAO.licenseNumberExists(doctor.getLicenseNumber(), doctor.getDoctorId())) {
            throw new IllegalArgumentException(
                    "A doctor with license number '" + doctor.getLicenseNumber() + "' already exists.");
        }

        if (doctorDAO.phoneNumberExists(doctor.getPhoneNumber(), doctor.getDoctorId())) {
            throw new IllegalArgumentException(
                    "A doctor with phone number '" + doctor.getPhoneNumber() + "' already exists.");
        }

        boolean updated = doctorDAO.update(doctor);
        if (updated) {
            cacheManager.invalidateDoctor(doctor.getDoctorId());
            cacheManager.invalidateQuery("all_doctors");
            cacheManager.invalidateQueryPattern("doctors_page_");
        }

        return updated;
    }

    public boolean deleteDoctor(Integer doctorId) throws Exception {
        boolean deleted = doctorDAO.delete(doctorId);
        if (deleted) {
            cacheManager.invalidateDoctor(doctorId);
            cacheManager.invalidateQuery("all_doctors");
            cacheManager.invalidateQueryPattern("doctors_page_");
            doctorDAO.resetSequence();
        }
        return deleted;
    }

    public List<Doctor> getDoctorsByDepartment(Integer departmentId) throws Exception {
        return doctorDAO.findByDepartment(departmentId);
    }

    public List<Doctor> searchDoctorsByName(String name) throws Exception {
        if (name == null || name.trim().isEmpty()) {
            return getAllDoctors();
        }
        return doctorDAO.searchByName(name);
    }

    public List<Doctor> getDoctorsPaginated(int page, int size) throws Exception {
        long startTime = System.currentTimeMillis();
        String cacheKey = "doctors_page_" + page + "_size_" + size;

        @SuppressWarnings("unchecked")
        List<Doctor> cached = (List<Doctor>) cacheManager.getQuery(cacheKey);

        if (cached != null) {
            long duration = System.currentTimeMillis() - startTime;
            logger.info(
                    String.format("[CACHE] HIT - Doctors Page %d - %d records - %d ms", page, cached.size(), duration));
            return cached;
        }

        logger.info(String.format("[CACHE] MISS - Doctors Page %d - Fetching from DB", page));
        int offset = page * size;
        List<Doctor> doctors = doctorDAO.findAllPaginated(offset, size);

        cacheManager.cacheQuery(cacheKey, doctors);
        long duration = System.currentTimeMillis() - startTime;
        logger.info(
                String.format("[CACHE] STORED - Doctors Page %d - %d records - %d ms", page, doctors.size(), duration));

        return doctors;
    }

    public List<Doctor> searchDoctorsPaginated(String name, int page, int size) throws Exception {
        if (name == null || name.trim().isEmpty()) {
            return getDoctorsPaginated(page, size);
        }
        int offset = page * size;
        return doctorDAO.searchByNamePaginated(name, offset, size);
    }

    public int getTotalDoctorCount() throws Exception {
        return doctorDAO.getCount();
    }

    public int getSearchCount(String name) throws Exception {
        if (name == null || name.trim().isEmpty()) {
            return getTotalDoctorCount();
        }
        return doctorDAO.searchByName(name).size();
    }

    private void validateDoctor(Doctor doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null.");
        }

        if (doctor.getFirstName() == null || doctor.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor first name is required.");
        }

        if (doctor.getLastName() == null || doctor.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor last name is required.");
        }

        if (doctor.getLicenseNumber() == null || doctor.getLicenseNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("License number is required.");
        }

        if (doctor.getDepartmentId() == null) {
            throw new IllegalArgumentException("Department ID is required.");
        }
    }
}
