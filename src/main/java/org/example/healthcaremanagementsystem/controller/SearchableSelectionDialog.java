package org.example.healthcaremanagementsystem.controller;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.healthcaremanagementsystem.model.Doctor;
import org.example.healthcaremanagementsystem.model.Patient;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Professional searchable dialog for selecting patients or doctors.
 * Provides a table view with search functionality instead of dropdowns.
 */
public class SearchableSelectionDialog<T> {
    
    private final Stage dialog;
    private final TableView<T> tableView;
    private final TextField searchField;
    private final FilteredList<T> filteredList;
    private T selectedItem;
    private final Function<T, String> searchFunction;
    
    /**
     * Creates a searchable selection dialog.
     * 
     * @param title Dialog title
     * @param items List of items to display
     * @param searchFunction Function to extract searchable text from items
     * @param columns Column definitions for the table
     */
    @SafeVarargs
    public SearchableSelectionDialog(String title, ObservableList<T> items, 
                                     Function<T, String> searchFunction,
                                     TableColumnDefinition<T>... columns) {
        this.searchFunction = searchFunction;
        this.filteredList = new FilteredList<>(items, p -> true);
        
        // Create dialog stage
        dialog = new Stage();
        dialog.setTitle(title);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(true);
        
        // Create table view
        tableView = new TableView<>(filteredList);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        // Add columns
        List<TableColumnDefinition<T>> columnList = Arrays.asList(columns);
        for (TableColumnDefinition<T> colDef : columnList) {
            TableColumn<T, Object> column = new TableColumn<>(colDef.header);
            column.setCellValueFactory(new PropertyValueFactory<>(colDef.property));
            column.setPrefWidth(colDef.width);
            tableView.getColumns().add(column);
        }
        
        // Double-click to select
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                selectItem();
            }
        });
        
        // Create search field
        searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterItems(newValue);
        });
        
        // Create buttons
        Button selectButton = new Button("Select");
        selectButton.setDefaultButton(true);
        selectButton.setOnAction(e -> selectItem());
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true);
        cancelButton.setOnAction(e -> dialog.close());
        
        // Layout
        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().addAll(selectButton, cancelButton);
        
        VBox searchBox = new VBox(10);
        searchBox.setPadding(new Insets(15));
        searchBox.getChildren().addAll(
            new Label("Search:"),
            searchField
        );
        
        BorderPane root = new BorderPane();
        root.setTop(searchBox);
        root.setCenter(tableView);
        root.setBottom(buttonBox);
        
        Scene scene = new Scene(root, 700, 500);
        dialog.setScene(scene);
        
        // Apply styles
        tableView.setStyle("-fx-background-color: #ffffff;");
        searchField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cfd8dc; -fx-border-radius: 3; -fx-background-radius: 3;");
    }
    
    /**
     * Filters items based on search text.
     */
    private void filterItems(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            filteredList.setPredicate(item -> true);
        } else {
            String lowerSearch = searchText.toLowerCase();
            filteredList.setPredicate(item -> {
                String searchableText = searchFunction.apply(item);
                return searchableText != null && searchableText.toLowerCase().contains(lowerSearch);
            });
        }
    }
    
    /**
     * Selects the currently selected item and closes dialog.
     */
    private void selectItem() {
        selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            dialog.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select an item from the list.");
            alert.showAndWait();
        }
    }
    
    /**
     * Shows the dialog and returns the selected item.
     * 
     * @return Selected item or null if cancelled
     */
    public T showAndWait() {
        dialog.showAndWait();
        return selectedItem;
    }
    
    /**
     * Column definition helper class.
     */
    public static class TableColumnDefinition<T> {
        final String header;
        final String property;
        final double width;
        
        public TableColumnDefinition(String header, String property, double width) {
            this.header = header;
            this.property = property;
            this.width = width;
        }
    }
    
    /**
     * Creates a dialog for selecting patients.
     */
    public static SearchableSelectionDialog<Patient> createPatientDialog(ObservableList<Patient> patients) {
        return new SearchableSelectionDialog<>(
            "Select Patient",
            patients,
            p -> p.getFirstName() + " " + p.getLastName() + " " + p.getEmail() + " " + p.getPhoneNumber(),
            new TableColumnDefinition<>("ID", "patientId", 60),
            new TableColumnDefinition<>("First Name", "firstName", 150),
            new TableColumnDefinition<>("Last Name", "lastName", 150),
            new TableColumnDefinition<>("Email", "email", 200),
            new TableColumnDefinition<>("Phone", "phoneNumber", 120)
        );
    }
    
    /**
     * Creates a dialog for selecting doctors.
     */
    public static SearchableSelectionDialog<Doctor> createDoctorDialog(ObservableList<Doctor> doctors) {
        return new SearchableSelectionDialog<>(
            "Select Doctor",
            doctors,
            d -> d.getFirstName() + " " + d.getLastName() + " " + d.getSpecialization() + " " + d.getEmail(),
            new TableColumnDefinition<>("ID", "doctorId", 60),
            new TableColumnDefinition<>("First Name", "firstName", 120),
            new TableColumnDefinition<>("Last Name", "lastName", 120),
            new TableColumnDefinition<>("Specialization", "specialization", 150),
            new TableColumnDefinition<>("Email", "email", 200),
            new TableColumnDefinition<>("Status", "status", 100)
        );
    }
}
