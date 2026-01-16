package org.example.healthcaremanagementsystem.controller.handler;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Handles table setup operations.
 * Follows Single Responsibility Principle by managing only table configuration.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class TableSetupHandler {
    
    /**
     * Sets up a table column with property value factory.
     * 
     * @param column Table column to configure
     * @param propertyName Property name to bind
     */
    public static <T, S> void setupColumn(TableColumn<T, S> column, String propertyName) {
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
    }
    
    /**
     * Sets up multiple table columns at once.
     * 
     * @param columns Array of column configurations (column, propertyName pairs)
     */
    @SuppressWarnings("unchecked")
    public static <T> void setupColumns(Object... columns) {
        if (columns.length % 2 != 0) {
            throw new IllegalArgumentException("Columns array must have even number of elements (column, propertyName pairs)");
        }
        
        for (int i = 0; i < columns.length; i += 2) {
            TableColumn<T, ?> column = (TableColumn<T, ?>) columns[i];
            String propertyName = (String) columns[i + 1];
            setupColumn(column, propertyName);
        }
    }
    
    /**
     * Sets up table selection handler.
     * 
     * @param tableView Table view to configure
     * @param onSelection Runnable to execute when selection changes
     */
    public static <T> void setupSelectionHandler(TableView<T> tableView, Runnable onSelection) {
        tableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    onSelection.run();
                }
            }
        );
    }
}

