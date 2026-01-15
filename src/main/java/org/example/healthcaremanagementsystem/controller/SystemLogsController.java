package org.example.healthcaremanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.example.healthcaremanagementsystem.util.SystemLogger;

public class SystemLogsController {

    @FXML
    private ListView<String> listSystemLogs;

    @FXML
    public void initialize() {
        refreshLogs();
    }

    @FXML
    private void refreshLogs() {
        if (listSystemLogs != null) {
            listSystemLogs.setItems(FXCollections.observableArrayList(SystemLogger.getInstance().getLogs()));
            // Scroll to bottom
            if (!listSystemLogs.getItems().isEmpty()) {
                listSystemLogs.scrollTo(listSystemLogs.getItems().size() - 1);
            }
        }
    }
}
