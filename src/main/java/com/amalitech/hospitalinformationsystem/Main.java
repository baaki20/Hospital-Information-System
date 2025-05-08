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
        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            System.out.println("✅ Database connection established successfully.");

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("patient-view.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);
            stage.setTitle("Hospital Information System");
            stage.setScene(scene);
            stage.show();
        } catch (SQLException dbEx) {
            showCriticalError("Database Connection Error", "Cannot connect to the database. Please check your configuration.", dbEx);
        } catch (IOException ioEx) {
            showCriticalError("System Error", "Failed to load the application. Please contact support.", ioEx);
        }
    }

    private void showCriticalError(String title, String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}