package org.example.healthcaremanagementsystem.model;

import java.time.LocalDate;

public class Inventory {
    private int id;
    private String itemName;
    private int stockQuantity;
    private double price;
    private LocalDate expiryDate;

    public Inventory() {
    }

    public Inventory(int id, String itemName, int stockQuantity, double price, LocalDate expiryDate) {
        this.id = id;
        this.itemName = itemName;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.expiryDate = expiryDate;
    }

    public Inventory(String itemName, int stockQuantity, double price, LocalDate expiryDate) {
        this.itemName = itemName;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.expiryDate = expiryDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}
