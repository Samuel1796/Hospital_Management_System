package org.example.healthcaremanagementsystem.model;

/**
 * Data Transfer Object (DTO) representing a Department entity.
 * Represents hospital departments and follows DTO pattern.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class Department {
    
    private Integer departmentId;
    private String departmentName;
    private String description;
    private String location;
    private String phoneNumber;
    private String headDoctorId; // Can be null if no head assigned
    
    /**
     * Default constructor.
     */
    public Department() {
    }
    
    /**
     * Parameterized constructor for creating a new department.
     * 
     * @param departmentName Name of the department
     * @param description Department description
     * @param location Physical location of the department
     * @param phoneNumber Department contact number
     * @param headDoctorId ID of the department head doctor (nullable)
     */
    public Department(String departmentName, String description,
                      String location, String phoneNumber, String headDoctorId) {
        this.departmentName = departmentName;
        this.description = description;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.headDoctorId = headDoctorId;
    }
    
    /**
     * Full constructor including department ID (for existing records).
     * 
     * @param departmentId Unique department identifier
     * @param departmentName Name of the department
     * @param description Department description
     * @param location Physical location of the department
     * @param phoneNumber Department contact number
     * @param headDoctorId ID of the department head doctor (nullable)
     */
    public Department(Integer departmentId, String departmentName,
                      String description, String location, String phoneNumber,
                      String headDoctorId) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.description = description;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.headDoctorId = headDoctorId;
    }
    
    // Getters and Setters
    
    public Integer getDepartmentId() {
        return departmentId;
    }
    
    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
    
    public String getDepartmentName() {
        return departmentName;
    }
    
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getHeadDoctorId() {
        return headDoctorId;
    }
    
    public void setHeadDoctorId(String headDoctorId) {
        this.headDoctorId = headDoctorId;
    }
    
    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}

