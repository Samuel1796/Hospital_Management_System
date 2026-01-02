package org.example.healthcaremanagementsystem.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a MedicalInventory entity.
 * Manages medical supplies, medications, and equipment inventory.
 * Follows DTO pattern for data transfer between layers.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class MedicalInventory {
    
    private Integer inventoryId;
    private String itemName;
    private String itemType; // Medication, Equipment, Supply
    private String category; // e.g., Antibiotic, Pain Relief, Surgical Equipment
    private Integer quantity;
    private Integer reorderLevel; // Minimum quantity before reordering
    private BigDecimal unitPrice;
    private String supplier;
    private LocalDate expiryDate; // For medications
    private String storageLocation;
    private String status; // Available, Low Stock, Out of Stock, Expired
    
    /**
     * Default constructor.
     */
    public MedicalInventory() {
    }
    
    /**
     * Parameterized constructor for creating new inventory item.
     * 
     * @param itemName Name of the inventory item
     * @param itemType Type of item (Medication, Equipment, Supply)
     * @param category Category classification
     * @param quantity Current quantity in stock
     * @param reorderLevel Minimum quantity threshold for reordering
     * @param unitPrice Price per unit
     * @param supplier Supplier name
     * @param expiryDate Expiry date (for medications)
     * @param storageLocation Physical storage location
     * @param status Current status
     */
    public MedicalInventory(String itemName, String itemType, String category,
                            Integer quantity, Integer reorderLevel,
                            BigDecimal unitPrice, String supplier,
                            LocalDate expiryDate, String storageLocation,
                            String status) {
        this.itemName = itemName;
        this.itemType = itemType;
        this.category = category;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
        this.unitPrice = unitPrice;
        this.supplier = supplier;
        this.expiryDate = expiryDate;
        this.storageLocation = storageLocation;
        this.status = status;
    }
    
    /**
     * Full constructor including inventory ID (for existing records).
     * 
     * @param inventoryId Unique inventory identifier
     * @param itemName Name of the inventory item
     * @param itemType Type of item (Medication, Equipment, Supply)
     * @param category Category classification
     * @param quantity Current quantity in stock
     * @param reorderLevel Minimum quantity threshold for reordering
     * @param unitPrice Price per unit
     * @param supplier Supplier name
     * @param expiryDate Expiry date (for medications)
     * @param storageLocation Physical storage location
     * @param status Current status
     */
    public MedicalInventory(Integer inventoryId, String itemName, String itemType,
                            String category, Integer quantity, Integer reorderLevel,
                            BigDecimal unitPrice, String supplier, LocalDate expiryDate,
                            String storageLocation, String status) {
        this.inventoryId = inventoryId;
        this.itemName = itemName;
        this.itemType = itemType;
        this.category = category;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
        this.unitPrice = unitPrice;
        this.supplier = supplier;
        this.expiryDate = expiryDate;
        this.storageLocation = storageLocation;
        this.status = status;
    }
    
    // Getters and Setters
    
    public Integer getInventoryId() {
        return inventoryId;
    }
    
    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }
    
    public String getItemName() {
        return itemName;
    }
    
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
    public String getItemType() {
        return itemType;
    }
    
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public Integer getReorderLevel() {
        return reorderLevel;
    }
    
    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public String getSupplier() {
        return supplier;
    }
    
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public String getStorageLocation() {
        return storageLocation;
    }
    
    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Checks if the item needs to be reordered based on quantity.
     * 
     * @return true if quantity is at or below reorder level
     */
    public boolean needsReorder() {
        return quantity <= reorderLevel;
    }
    
    /**
     * Checks if the item has expired (for medications).
     * 
     * @return true if expiry date has passed
     */
    public boolean isExpired() {
        if (expiryDate == null) {
            return false;
        }
        return expiryDate.isBefore(java.time.LocalDate.now());
    }
    
    @Override
    public String toString() {
        return "MedicalInventory{" +
                "inventoryId=" + inventoryId +
                ", itemName='" + itemName + '\'' +
                ", itemType='" + itemType + '\'' +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                ", status='" + status + '\'' +
                '}';
    }
}

