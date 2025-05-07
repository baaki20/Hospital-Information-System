// PatientDao.java
package com.amalitech.hospitalinformationsystem.dao;

import com.amalitech.hospitalinformationsystem.model.Patient;
import com.amalitech.hospitalinformationsystem.util.DatabaseConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDao {

    // 1. INSERT a new Patient
    public long insert(Patient patient) throws SQLException {
        String sql = """
            INSERT INTO patients(first_name, last_name, address, phone)
            VALUES (?, ?, ?, ?)
            RETURNING patient_id
            """;

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, patient.getFirstName());
            ps.setString(2, patient.getLastName());
            ps.setString(3, patient.getAddress());
            ps.setString(4, patient.getPhone());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long newId = rs.getLong(1);
                    patient.setPatientId(newId);
                    return newId;
                } else {
                    throw new SQLException("Inserting patient failed, no ID obtained.");
                }
            }
        }
    }

    // 2. SELECT all Patients
    public List<Patient> findAll() throws SQLException {
        String sql = "SELECT patient_id, first_name, last_name, address, phone FROM patients";
        List<Patient> list = new ArrayList<>();

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Patient(
                        rs.getLong("patient_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("address"),
                        rs.getString("phone")
                ));
            }
        }

        return list;
    }

    // 3. UPDATE an existing Patient
    public boolean update(Patient patient) throws SQLException {
        String sql = """
            UPDATE patients
               SET first_name = ?, last_name = ?, address = ?, phone = ?
             WHERE patient_id = ?
            """;

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, patient.getFirstName());
            ps.setString(2, patient.getLastName());
            ps.setString(3, patient.getAddress());
            ps.setString(4, patient.getPhone());
            ps.setLong(5,   patient.getPatientId());

            return ps.executeUpdate() == 1;
        }
    }

    // 4. DELETE a Patient by ID
    public boolean delete(long patientId) throws SQLException {
        String sql = "DELETE FROM patients WHERE patient_id = ?";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, patientId);
            return ps.executeUpdate() == 1;
        }
    }

    // Optional: find by ID
    public Patient findById(long patientId) throws SQLException {
        String sql = """
            SELECT patient_id, first_name, last_name, address, phone
              FROM patients
             WHERE patient_id = ?
            """;

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Patient(
                            rs.getLong("patient_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("address"),
                            rs.getString("phone")
                    );
                }
                return null;
            }
        }
    }
}
