package org.example.healthcaremanagementsystem.model;

/**
 * Data Transfer Object (DTO) representing a PrescriptionItem entity.
 * Represents individual medication items within a prescription.
 * Follows DTO pattern and supports composition with Prescription.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class PrescriptionItem {
    
    private Integer itemId;
    private Integer prescriptionId;
    private Integer inventoryId; // Reference to medical inventory
    private String medicationName;
    private String dosage;
    private Integer quantity;
    private String frequency; // e.g., "Twice daily", "Once a week"
    private String duration; // e.g., "7 days", "2 weeks"
    
    /**
     * Default constructor.
     */
    public PrescriptionItem() {
    }
    
    /**
     * Parameterized constructor for creating a new prescription item.
     * 
     * @param prescriptionId ID of the parent prescription
     * @param inventoryId ID of the medication in inventory
     * @param medicationName Name of the medication
     * @param dosage Medication dosage
     * @param quantity Quantity prescribed
     * @param frequency Frequency of administration
     * @param duration Duration of treatment
     */
    public PrescriptionItem(Integer prescriptionId, Integer inventoryId,
                            String medicationName, String dosage, Integer quantity,
                            String frequency, String duration) {
        this.prescriptionId = prescriptionId;
        this.inventoryId = inventoryId;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.quantity = quantity;
        this.frequency = frequency;
        this.duration = duration;
    }
    
    /**
     * Full constructor including item ID (for existing records).
     * 
     * @param itemId Unique prescription item identifier
     * @param prescriptionId ID of the parent prescription
     * @param inventoryId ID of the medication in inventory
     * @param medicationName Name of the medication
     * @param dosage Medication dosage
     * @param quantity Quantity prescribed
     * @param frequency Frequency of administration
     * @param duration Duration of treatment
     */
    public PrescriptionItem(Integer itemId, Integer prescriptionId,
                            Integer inventoryId, String medicationName,
                            String dosage, Integer quantity, String frequency,
                            String duration) {
        this.itemId = itemId;
        this.prescriptionId = prescriptionId;
        this.inventoryId = inventoryId;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.quantity = quantity;
        this.frequency = frequency;
        this.duration = duration;
    }
    
    // Getters and Setters
    
    public Integer getItemId() {
        return itemId;
    }
    
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
    
    public Integer getPrescriptionId() {
        return prescriptionId;
    }
    
    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
    }
    
    public Integer getInventoryId() {
        return inventoryId;
    }
    
    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }
    
    public String getMedicationName() {
        return medicationName;
    }
    
    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }
    
    public String getDosage() {
        return dosage;
    }
    
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public String getFrequency() {
        return frequency;
    }
    
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    
    public String getDuration() {
        return duration;
    }
    
    public void setDuration(String duration) {
        this.duration = duration;
    }
    
    @Override
    public String toString() {
        return "PrescriptionItem{" +
                "itemId=" + itemId +
                ", prescriptionId=" + prescriptionId +
                ", medicationName='" + medicationName + '\'' +
                ", dosage='" + dosage + '\'' +
                ", quantity=" + quantity +
                ", frequency='" + frequency + '\'' +
                '}';
    }
}

