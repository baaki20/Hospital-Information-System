package com.amalitech.hospitalinformationsystem.controller;

import com.amalitech.hospitalinformationsystem.dao.PatientDao;
import com.amalitech.hospitalinformationsystem.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class PatientController implements Initializable {

    // UI Components
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextArea addressField;
    @FXML private TextField phoneField;
    @FXML private Button saveButton;
    @FXML private Button updateButton;
    @FXML private Button clearButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button refreshButton;
    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, Long> idColumn;
    @FXML private TableColumn<Patient, String> firstNameColumn;
    @FXML private TableColumn<Patient, String> lastNameColumn;
    @FXML private TableColumn<Patient, String> addressColumn;
    @FXML private TableColumn<Patient, String> phoneColumn;
    @FXML private Label statusLabel;

    // Data access
    private final PatientDao patientDao = new PatientDao();
    private final ObservableList<Patient> patientList = FXCollections.observableArrayList();
    private Patient selectedPatient;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configure table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Link data to table
        patientTable.setItems(patientList);

        // Set selection behavior
        patientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedPatient = newSelection;
            if (selectedPatient != null) {
                editButton.setDisable(false);
                deleteButton.setDisable(false);
            } else {
                editButton.setDisable(true);
                deleteButton.setDisable(true);
            }
        });

        // Initial state setup
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        updateButton.setDisable(true);

        // Load data
        loadPatientData();
    }

    /**
     * Loads all patients from the database into the table
     */
    private void loadPatientData() {
        patientList.clear();
        try {
            patientList.addAll(patientDao.findAll());
            updateStatus("Loaded " + patientList.size() + " patients");
        } catch (SQLException e) {
            showError("Error loading patients", e);
        }
    }

    /**
     * Handles Save button action - creates new patient
     */
    @FXML
    private void savePatient() {
        if (!validateInput()) {
            return;
        }

        try {
            // Create a new patient from form data
            Patient newPatient = new Patient(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    addressField.getText(),
                    phoneField.getText()
            );

            // Save to database
            long id = patientDao.insert(newPatient);

            // Update UI
            loadPatientData();
            clearForm();
            updateStatus("New patient saved with ID: " + id);
        } catch (SQLException e) {
            showError("Error saving patient", e);
        }
    }

    /**
     * Handles Update button action - updates existing patient
     */
    @FXML
    private void updatePatient() {
        if (!validateInput() || selectedPatient == null) {
            return;
        }

        try {
            // Update patient object with form data
            selectedPatient.setFirstName(firstNameField.getText());
            selectedPatient.setLastName(lastNameField.getText());
            selectedPatient.setAddress(addressField.getText());
            selectedPatient.setPhone(phoneField.getText());

            // Save to database
            boolean success = patientDao.update(selectedPatient);

            if (success) {
                // Refresh the table
                loadPatientData();
                clearForm();
                updateStatus("Patient updated successfully");
            } else {
                updateStatus("Failed to update patient");
            }
        } catch (SQLException e) {
            showError("Error updating patient", e);
        }
    }

    /**
     * Handles Edit button action - populates form with selected patient
     */
    @FXML
    private void editPatient() {
        if (selectedPatient != null) {
            firstNameField.setText(selectedPatient.getFirstName());
            lastNameField.setText(selectedPatient.getLastName());
            addressField.setText(selectedPatient.getAddress());
            phoneField.setText(selectedPatient.getPhone());

            // Switch button states
            saveButton.setDisable(true);
            updateButton.setDisable(false);
            updateStatus("Editing patient ID: " + selectedPatient.getPatientId());
        }
    }

    /**
     * Handles Delete button action - removes selected patient
     */
    @FXML
    private void deletePatient() {
        if (selectedPatient == null) {
            return;
        }

        // Confirm deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Patient");
        alert.setHeaderText("Delete Patient Record");
        alert.setContentText("Are you sure you want to delete patient: " +
                selectedPatient.getFirstName() + " " +
                selectedPatient.getLastName() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean deleted = patientDao.delete(selectedPatient.getPatientId());
                if (deleted) {
                    loadPatientData();
                    clearForm();
                    updateStatus("Patient deleted successfully");
                } else {
                    updateStatus("Failed to delete patient");
                }
            } catch (SQLException e) {
                showError("Error deleting patient", e);
            }
        }
    }

    /**
     * Clears form fields and resets UI state
     */
    @FXML
    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        addressField.clear();
        phoneField.clear();

        saveButton.setDisable(false);
        updateButton.setDisable(true);

        patientTable.getSelectionModel().clearSelection();
        selectedPatient = null;

        updateStatus("Form cleared");
    }

    /**
     * Refreshes the patient table
     */
    @FXML
    private void refreshTable() {
        loadPatientData();
    }

    /**
     * Validates user input before saving/updating
     */
    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();

        if (firstNameField.getText().trim().isEmpty()) {
            errorMessage.append("First name is required.\n");
        }

        if (lastNameField.getText().trim().isEmpty()) {
            errorMessage.append("Last name is required.\n");
        }

        if (phoneField.getText().trim().isEmpty()) {
            errorMessage.append("Phone number is required.\n");
        }

        if (errorMessage.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Please correct the following errors:");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return false;
        }

        return true;
    }

    /**
     * Updates the status label
     */
    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    /**
     * Shows error dialog
     */
    private void showError(String title, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("An error occurred");
        alert.setContentText(e.getMessage());

        // Create expandable content with stack trace
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        StringBuilder stackTrace = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            stackTrace.append(element.toString()).append("\n");
        }
        textArea.setText(stackTrace.toString());

        alert.getDialogPane().setExpandableContent(textArea);
        alert.showAndWait();
    }
}