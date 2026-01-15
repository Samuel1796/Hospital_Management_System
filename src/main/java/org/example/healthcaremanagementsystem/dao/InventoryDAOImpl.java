package org.example.healthcaremanagementsystem.dao;

import org.example.healthcaremanagementsystem.config.DatabaseConfig;
import org.example.healthcaremanagementsystem.model.Inventory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAOImpl implements InventoryDAO {

    public InventoryDAOImpl() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS inventory (" +
                "id SERIAL PRIMARY KEY," +
                "item_name VARCHAR(100) NOT NULL," +
                "stock_quantity INT NOT NULL," +
                "price DECIMAL(10, 2) NOT NULL," +
                "expiry_date DATE" +
                ")";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addInventory(Inventory inventory) {
        String sql = "INSERT INTO inventory (item_name, stock_quantity, price, expiry_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, inventory.getItemName());
            pstmt.setInt(2, inventory.getStockQuantity());
            pstmt.setDouble(3, inventory.getPrice());
            pstmt.setDate(4, Date.valueOf(inventory.getExpiryDate()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Inventory getInventoryById(int id) {
        String sql = "SELECT * FROM inventory WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Inventory(
                            rs.getInt("id"),
                            rs.getString("item_name"),
                            rs.getInt("stock_quantity"),
                            rs.getDouble("price"),
                            rs.getDate("expiry_date").toLocalDate());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Inventory> getAllInventory() {
        List<Inventory> list = new ArrayList<>();
        String sql = "SELECT * FROM inventory ORDER BY id";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Inventory(
                        rs.getInt("id"),
                        rs.getString("item_name"),
                        rs.getInt("stock_quantity"),
                        rs.getDouble("price"),
                        rs.getDate("expiry_date").toLocalDate()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void updateInventory(Inventory inventory) {
        String sql = "UPDATE inventory SET item_name = ?, stock_quantity = ?, price = ?, expiry_date = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, inventory.getItemName());
            pstmt.setInt(2, inventory.getStockQuantity());
            pstmt.setDouble(3, inventory.getPrice());
            pstmt.setDate(4, Date.valueOf(inventory.getExpiryDate()));
            pstmt.setInt(5, inventory.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteInventory(int id) {
        String sql = "DELETE FROM inventory WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
