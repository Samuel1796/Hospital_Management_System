package org.example.healthcaremanagementsystem.controller.base;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Base controller class providing common functionality for all controllers.
 * Follows Single Responsibility Principle by handling only common UI operations.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public abstract class BaseController {
    
    /**
     * Shows a success alert dialog.
     * 
     * @param title Alert title
     * @param message Alert message
     */
    protected void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Shows an error alert dialog.
     * 
     * @param title Alert title
     * @param message Alert message
     */
    protected void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Shows a confirmation dialog.
     * 
     * @param title Dialog title
     * @param header Dialog header text
     * @param content Dialog content text
     * @return true if user confirmed, false otherwise
     */
    protected boolean showConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}

