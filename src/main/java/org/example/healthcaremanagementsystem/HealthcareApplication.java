package org.example.healthcaremanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.healthcaremanagementsystem.config.DatabaseConfig;

import java.io.IOException;

/**
 * Main application class for Healthcare Management System.
 * Initializes the JavaFX application and loads the main view.
 * 

 */
public class HealthcareApplication extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        // Test database connection before starting application
        DatabaseConfig dbConfig = DatabaseConfig.getInstance();
        if (!dbConfig.testConnection()) {
            System.err.println("Warning: Database connection test failed. " +
                             "Please ensure PostgreSQL is running and database is created.");
        }
        
        // Load main FXML view
        FXMLLoader fxmlLoader = new FXMLLoader(
            HealthcareApplication.class.getResource("main-view.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        
        stage.setTitle("Healthcare Management System");
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void stop() throws Exception {
        // Connections are now managed per-request with try-with-resources
        // No need to explicitly close a shared connection
        super.stop();
    }
    
    public static void main(String[] args) {
        launch();
    }
}

