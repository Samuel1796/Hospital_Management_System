package org.example.healthcaremanagementsystem.controller.handler;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Handles form operations (clear, populate, etc.).
 * Follows Single Responsibility Principle by managing only form state operations.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class FormHandler {
    
    /**
     * Clears text fields.
     * 
     * @param fields Text fields to clear
     */
    public static void clearTextFields(TextField... fields) {
        for (TextField field : fields) {
            if (field != null) {
                field.clear();
            }
        }
    }
    
    /**
     * Clears combo boxes.
     * 
     * @param comboBoxes Combo boxes to clear
     */
    @SuppressWarnings("rawtypes")
    public static void clearComboBoxes(ComboBox... comboBoxes) {
        for (ComboBox<?> comboBox : comboBoxes) {
            if (comboBox != null) {
                comboBox.setValue(null);
            }
        }
    }
    
    /**
     * Clears date pickers.
     * 
     * @param datePickers Date pickers to clear
     */
    public static void clearDatePickers(DatePicker... datePickers) {
        for (DatePicker datePicker : datePickers) {
            if (datePicker != null) {
                datePicker.setValue(null);
            }
        }
    }
    
    /**
     * Clears text areas.
     * 
     * @param textAreas Text areas to clear
     */
    public static void clearTextAreas(TextArea... textAreas) {
        for (TextArea textArea : textAreas) {
            if (textArea != null) {
                textArea.clear();
            }
        }
    }
}

