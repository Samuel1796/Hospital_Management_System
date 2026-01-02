package org.example.healthcaremanagementsystem.model;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a Patient entity.
 * Follows DTO pattern to transfer data between layers.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class Patient {
    
    private Integer patientId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String address;
    private String gender;
    private String bloodGroup;
    private String emergencyContact;
    private String emergencyPhone;
    
    /**
     * Default constructor.
     */
    public Patient() {
    }
    
    /**
     * Parameterized constructor for creating a new patient.
     * 
     * @param firstName Patient's first name
     * @param lastName Patient's last name
     * @param email Patient's email address
     * @param phoneNumber Patient's phone number
     * @param dateOfBirth Patient's date of birth
     * @param address Patient's address
     * @param gender Patient's gender
     * @param bloodGroup Patient's blood group
     * @param emergencyContact Emergency contact name
     * @param emergencyPhone Emergency contact phone
     */
    public Patient(String firstName, String lastName, String email, 
                   String phoneNumber, LocalDate dateOfBirth, String address,
                   String gender, String bloodGroup, String emergencyContact,
                   String emergencyPhone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.emergencyContact = emergencyContact;
        this.emergencyPhone = emergencyPhone;
    }
    
    /**
     * Full constructor including patient ID (for existing records).
     * 
     * @param patientId Unique patient identifier
     * @param firstName Patient's first name
     * @param lastName Patient's last name
     * @param email Patient's email address
     * @param phoneNumber Patient's phone number
     * @param dateOfBirth Patient's date of birth
     * @param address Patient's address
     * @param gender Patient's gender
     * @param bloodGroup Patient's blood group
     * @param emergencyContact Emergency contact name
     * @param emergencyPhone Emergency contact phone
     */
    public Patient(Integer patientId, String firstName, String lastName, 
                   String email, String phoneNumber, LocalDate dateOfBirth,
                   String address, String gender, String bloodGroup,
                   String emergencyContact, String emergencyPhone) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.emergencyContact = emergencyContact;
        this.emergencyPhone = emergencyPhone;
    }
    
    // Getters and Setters
    
    public Integer getPatientId() {
        return patientId;
    }
    
    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
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
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getBloodGroup() {
        return bloodGroup;
    }
    
    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
    
    public String getEmergencyContact() {
        return emergencyContact;
    }
    
    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
    
    public String getEmergencyPhone() {
        return emergencyPhone;
    }
    
    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }
    
    /**
     * Returns the full name of the patient.
     * 
     * @return Concatenated first and last name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + patientId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                '}';
    }
}

