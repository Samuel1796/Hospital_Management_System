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
    
    private final DoctorDAO doctorDAO;
    private final CacheManager cacheManager;
    
    public DoctorService() {
        this.doctorDAO = new DoctorDAOImpl();
        this.cacheManager = new CacheManager();
    }
    
    public Doctor createDoctor(Doctor doctor) throws Exception {
        validateDoctor(doctor);
        
        // Check for duplicate email
        if (doctorDAO.emailExists(doctor.getEmail(), null)) {
            throw new IllegalArgumentException("A doctor with email '" + doctor.getEmail() + "' already exists.");
        }
        
        // Check for duplicate license number
        if (doctorDAO.licenseNumberExists(doctor.getLicenseNumber(), null)) {
            throw new IllegalArgumentException("A doctor with license number '" + doctor.getLicenseNumber() + "' already exists.");
        }
        
        // Check for duplicate phone number
        if (doctorDAO.phoneNumberExists(doctor.getPhoneNumber(), null)) {
            throw new IllegalArgumentException("A doctor with phone number '" + doctor.getPhoneNumber() + "' already exists.");
        }
        
        // Reset sequence to ensure sequential IDs
        doctorDAO.resetSequence();
        
        Doctor createdDoctor = doctorDAO.create(doctor);
        cacheManager.cacheDoctor(createdDoctor.getDoctorId(), createdDoctor);
        
        return createdDoctor;
    }
    
    public Optional<Doctor> getDoctorById(Integer doctorId) throws Exception {
        Object cachedDoctor = cacheManager.getDoctor(doctorId);
        if (cachedDoctor != null) {
            return Optional.of((Doctor) cachedDoctor);
        }
        
        Optional<Doctor> doctor = doctorDAO.findById(doctorId);
        doctor.ifPresent(d -> cacheManager.cacheDoctor(d.getDoctorId(), d));
        
        return doctor;
    }
    
    public List<Doctor> getAllDoctors() throws Exception {
        List<Doctor> doctors = doctorDAO.findAll();
        
        SortingUtil.sort(doctors, (d1, d2) -> {
            int lastNameCompare = d1.getLastName().compareToIgnoreCase(d2.getLastName());
            if (lastNameCompare != 0) {
                return lastNameCompare;
            }
            return d1.getFirstName().compareToIgnoreCase(d2.getFirstName());
        });
        
        return doctors;
    }
    
    public boolean updateDoctor(Doctor doctor) throws Exception {
        validateDoctor(doctor);
        
        // Check for duplicate email (excluding current doctor)
        if (doctorDAO.emailExists(doctor.getEmail(), doctor.getDoctorId())) {
            throw new IllegalArgumentException("A doctor with email '" + doctor.getEmail() + "' already exists.");
        }
        
        // Check for duplicate license number (excluding current doctor)
        if (doctorDAO.licenseNumberExists(doctor.getLicenseNumber(), doctor.getDoctorId())) {
            throw new IllegalArgumentException("A doctor with license number '" + doctor.getLicenseNumber() + "' already exists.");
        }
        
        // Check for duplicate phone number (excluding current doctor)
        if (doctorDAO.phoneNumberExists(doctor.getPhoneNumber(), doctor.getDoctorId())) {
            throw new IllegalArgumentException("A doctor with phone number '" + doctor.getPhoneNumber() + "' already exists.");
        }
        
        boolean updated = doctorDAO.update(doctor);
        if (updated) {
            cacheManager.invalidateDoctor(doctor.getDoctorId());
        }
        
        return updated;
    }
    
    public boolean deleteDoctor(Integer doctorId) throws Exception {
        boolean deleted = doctorDAO.delete(doctorId);
        if (deleted) {
            cacheManager.invalidateDoctor(doctorId);
            // Reset sequence for sequential IDs
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

