package org.example.healthcaremanagementsystem.dao;

import org.example.healthcaremanagementsystem.model.Inventory;
import java.util.List;

public interface InventoryDAO {
    void addInventory(Inventory inventory);

    Inventory getInventoryById(int id);

    List<Inventory> getAllInventory();

    void updateInventory(Inventory inventory);

    void deleteInventory(int id);
}
