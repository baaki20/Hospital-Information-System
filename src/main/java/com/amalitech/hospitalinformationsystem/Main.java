package com.amalitech.hospitalinformationsystem;

import com.amalitech.hospitalinformationsystem.dao.PatientDao;
import com.amalitech.hospitalinformationsystem.model.Patient;
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
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // 1. Try to establish a database connection
        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            System.out.println("✅ Database connection established successfully.");

            // 2. Load and show the JavaFX UI
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(loader.load(), 320, 240);
            stage.setTitle("Hospital Information System");
            stage.setScene(scene);
            stage.show();

        } catch (SQLException dbEx) {
            // 3a. Handle database failure
            System.out.println("Heree.....");
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

    public static void main(String[] args) throws SQLException {

        PatientDao dao = new PatientDao();

        // INSERT
        Patient p = new Patient("Jane", "Doe", "123 Maple St", "555‑1234");
        long id = dao.insert(p);
        System.out.println("New patient ID: " + id);

        // SELECT ALL
        List<Patient> all = dao.findAll();
        all.forEach(pt -> System.out.println(pt.getPatientId() + ": " + pt.getFirstName()));

        // UPDATE
        p.setAddress("456 Oak Ave");
        boolean updated = dao.update(p);

        // DELETE
//        boolean deleted = dao.delete(p.getPatientId());

        launch(args);
    }
}
