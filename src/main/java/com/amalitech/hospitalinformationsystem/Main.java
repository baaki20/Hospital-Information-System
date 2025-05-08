package com.amalitech.hospitalinformationsystem;

import com.amalitech.hospitalinformationsystem.util.DatabaseConnectionManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // 1. Try to establish a database connection
        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            System.out.println("✅ Database connection established successfully.");

            // 2. Load and show the JavaFX UI
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("patient-view.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);
            stage.setTitle("Hospital Information System");
            stage.setScene(scene);
            stage.show();

        } catch (SQLException dbEx) {
            // 3a. Handle database failure
            showDatabaseError(dbEx);
        } catch (IOException ioEx) {
            // 3b. Handle FXML loading failure
            ioEx.printStackTrace();
            Platform.exit();
        }
    }

    private void showDatabaseError(SQLException ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database Connection Error");
        alert.setHeaderText("Cannot connect to Hospital database");
        alert.setContentText("Details: " + ex.getMessage());
        alert.showAndWait();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}