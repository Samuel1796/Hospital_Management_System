package org.example.healthcaremanagementsystem.model;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a Doctor entity.
 * Encapsulates doctor information and follows DTO pattern.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class Doctor {
    
    private Integer doctorId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String specialization;
    private Integer departmentId;
    private String licenseNumber;
    private LocalDate hireDate;
    private String status; // Active, Inactive, On Leave
    
    /**
     * Default constructor.
     */
    public Doctor() {
    }
    
    /**
     * Parameterized constructor for creating a new doctor.
     * 
     * @param firstName Doctor's first name
     * @param lastName Doctor's last name
     * @param email Doctor's email address
     * @param phoneNumber Doctor's phone number
     * @param specialization Doctor's medical specialization
     * @param departmentId Department ID the doctor belongs to
     * @param licenseNumber Medical license number
     * @param hireDate Date when doctor was hired
     * @param status Current employment status
     */
    public Doctor(String firstName, String lastName, String email,
                  String phoneNumber, String specialization, Integer departmentId,
                  String licenseNumber, LocalDate hireDate, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.specialization = specialization;
        this.departmentId = departmentId;
        this.licenseNumber = licenseNumber;
        this.hireDate = hireDate;
        this.status = status;
    }
    
    /**
     * Full constructor including doctor ID (for existing records).
     * 
     * @param doctorId Unique doctor identifier
     * @param firstName Doctor's first name
     * @param lastName Doctor's last name
     * @param email Doctor's email address
     * @param phoneNumber Doctor's phone number
     * @param specialization Doctor's medical specialization
     * @param departmentId Department ID the doctor belongs to
     * @param licenseNumber Medical license number
     * @param hireDate Date when doctor was hired
     * @param status Current employment status
     */
    public Doctor(Integer doctorId, String firstName, String lastName,
                  String email, String phoneNumber, String specialization,
                  Integer departmentId, String licenseNumber, LocalDate hireDate,
                  String status) {
        this.doctorId = doctorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.specialization = specialization;
        this.departmentId = departmentId;
        this.licenseNumber = licenseNumber;
        this.hireDate = hireDate;
        this.status = status;
    }
    
    // Getters and Setters
    
    public Integer getDoctorId() {
        return doctorId;
    }
    
    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public Integer getDepartmentId() {
        return departmentId;
    }
    
    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
    
    public String getLicenseNumber() {
        return licenseNumber;
    }
    
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
    
    public LocalDate getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Returns the full name of the doctor.
     * 
     * @return Concatenated first and last name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId=" + doctorId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", specialization='" + specialization + '\'' +
                ", departmentId=" + departmentId +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

