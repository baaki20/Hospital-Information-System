package com.amalitech.hospitalinformationsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages JDBC connections to the Hospital database.
 */
public final class DatabaseConnectionManager {
    // 1. Configuration (consider moving to a properties file or env vars)
    private static final String JDBC_URL      = "jdbc:postgresql://localhost:5431/Hospital Information System";
    private static final String JDBC_USER     = "postgres";
    private static final String JDBC_PASSWORD = "baaki123";
    private static final String JDBC_DRIVER   = "org.postgresql.Driver";

    // 2. Prevent instantiation
    private DatabaseConnectionManager() { }

    // 3. Register driver once
    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            // Fatal: driver not on classpath
            throw new ExceptionInInitializerError(
                    "Unable to load JDBC driver [" + JDBC_DRIVER + "]: " + e.getMessage()
            );
        }
    }

    /**
     * Obtains a new Connection. Caller is responsible for closing it.
     *
     * @return an open {@link Connection}
     * @throws SQLException if connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }
}
