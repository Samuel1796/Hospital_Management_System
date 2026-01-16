package org.example.healthcaremanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.healthcaremanagementsystem.dao.InventoryDAO;
import org.example.healthcaremanagementsystem.dao.InventoryDAOImpl;
import org.example.healthcaremanagementsystem.model.Inventory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class InventoryController {

    @FXML
    private TableView<Inventory> inventoryTable;
    @FXML
    private TableColumn<Inventory, Integer> colId;
    @FXML
    private TableColumn<Inventory, String> colItemName;
    @FXML
    private TableColumn<Inventory, Integer> colStockQuantity;
    @FXML
    private TableColumn<Inventory, Double> colPrice;
    @FXML
    private TableColumn<Inventory, LocalDate> colExpiryDate;

    @FXML
    private TextField txtItemName;
    @FXML
    private TextField txtStockQuantity;
    @FXML
    private TextField txtPrice;
    @FXML
    private DatePicker dpExpiryDate;

    // Pagination Controls
    @FXML
    private Button btnPrevPage;
    @FXML
    private Button btnNextPage;
    @FXML
    private Label lblPageInfo;

    private final InventoryDAO inventoryDAO;
    private final int ITEMS_PER_PAGE = 10;
    private int currentPage = 0;
    private int totalItems = 0;

    public InventoryController() {
        this.inventoryDAO = new InventoryDAOImpl();
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colStockQuantity.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colExpiryDate.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));

        loadInventory();

        // Listen for selection changes to populate form
        inventoryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });
    }

    private void loadInventory() {
        List<Inventory> allItems = inventoryDAO.getAllInventory();
        totalItems = allItems.size();

        int fromIndex = currentPage * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, totalItems);

        if (fromIndex >= totalItems && totalItems > 0) {
            currentPage = 0;
            fromIndex = 0;
            toIndex = Math.min(ITEMS_PER_PAGE, totalItems);
        } else if (totalItems == 0) {
            fromIndex = 0;
            toIndex = 0;
        }

        List<Inventory> pageItems = allItems.subList(fromIndex, toIndex);
        inventoryTable.setItems(FXCollections.observableArrayList(pageItems));
        updatePaginationControls();
    }

    private void updatePaginationControls() {
        int maxPage = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
        if (maxPage == 0)
            maxPage = 1;

        lblPageInfo.setText(String.format("Page %d of %d", currentPage + 1, maxPage));

        if (btnPrevPage != null)
            btnPrevPage.setDisable(currentPage == 0);
        if (btnNextPage != null)
            btnNextPage.setDisable(currentPage >= maxPage - 1);
    }

    @FXML
    private void handlePrevPage() {
        if (currentPage > 0) {
            currentPage--;
            loadInventory();
        }
    }

    @FXML
    private void handleNextPage() {
        int maxPage = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
        if (currentPage < maxPage - 1) {
            currentPage++;
            loadInventory();
        }
    }

    private void populateForm(Inventory inventory) {
        txtItemName.setText(inventory.getItemName());
        txtStockQuantity.setText(String.valueOf(inventory.getStockQuantity()));
        txtPrice.setText(String.valueOf(inventory.getPrice()));
        dpExpiryDate.setValue(inventory.getExpiryDate());
    }

    @FXML
    private void clearForm() {
        txtItemName.clear();
        txtStockQuantity.clear();
        txtPrice.clear();
        dpExpiryDate.setValue(null);
        inventoryTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void addItem() {
        if (!validateInput())
            return;

        Inventory newItem = new Inventory(
                txtItemName.getText(),
                Integer.parseInt(txtStockQuantity.getText()),
                Double.parseDouble(txtPrice.getText()),
                dpExpiryDate.getValue());

        inventoryDAO.addInventory(newItem);
        org.example.healthcaremanagementsystem.util.SystemLogger.getInstance().log("INVENTORY",
                "Added new item: " + newItem.getItemName());
        loadInventory(); // Real-time update
        clearForm();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Item added successfully!");
    }

    @FXML
    private void updateItem() {
        Inventory selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item to update.");
            return;
        }
        if (!validateInput())
            return;

        selected.setItemName(txtItemName.getText());
        selected.setStockQuantity(Integer.parseInt(txtStockQuantity.getText()));
        selected.setPrice(Double.parseDouble(txtPrice.getText()));
        selected.setExpiryDate(dpExpiryDate.getValue());

        inventoryDAO.updateInventory(selected);
        org.example.healthcaremanagementsystem.util.SystemLogger.getInstance().log("INVENTORY",
                "Updated item: " + selected.getItemName());
        loadInventory(); // Real-time update
        clearForm();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Item updated successfully!");
    }

    @FXML
    private void deleteItem() {
        Inventory selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setContentText("Are you sure you want to delete " + selected.getItemName() + "?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            inventoryDAO.deleteInventory(selected.getId());
            org.example.healthcaremanagementsystem.util.SystemLogger.getInstance().log("INVENTORY",
                    "Deleted item: " + selected.getItemName());
            loadInventory(); // Real-time update
            clearForm();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Item deleted successfully!");
        }
    }

    private boolean validateInput() {
        try {
            if (txtItemName.getText().isEmpty() || txtStockQuantity.getText().isEmpty() || txtPrice.getText().isEmpty()
                    || dpExpiryDate.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
                return false;
            }
            Integer.parseInt(txtStockQuantity.getText());
            Double.parseDouble(txtPrice.getText());
            return true;
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Quantity must be an integer and Price must be a number.");
            return false;
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
