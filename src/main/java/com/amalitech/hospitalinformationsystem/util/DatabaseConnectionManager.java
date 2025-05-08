package com.amalitech.hospitalinformationsystem.util;

            import java.io.IOException;
            import java.io.InputStream;
            import java.sql.Connection;
            import java.sql.DriverManager;
            import java.sql.SQLException;
            import java.util.Properties;
            import java.util.logging.Level;
            import java.util.logging.Logger;

            public final class DatabaseConnectionManager {
                private static final Properties properties = new Properties();
                private static final Logger logger = Logger.getLogger(DatabaseConnectionManager.class.getName());

                static {
                    try (InputStream input = DatabaseConnectionManager.class.getClassLoader().getResourceAsStream("db-config.properties")) {
                        if (input == null) {
                            throw new IOException("Configuration file 'db-config.properties' not found.");
                        }
                        properties.load(input);
                        Class.forName(properties.getProperty("jdbc.driver"));
                    } catch (IOException | ClassNotFoundException e) {
                        logger.log(Level.SEVERE, "Failed to load database configuration: {0}", e.getMessage());
                        throw new ExceptionInInitializerError("Database configuration error: " + e.getMessage());
                    }
                }

                public static Connection getConnection() throws SQLException {
                    try {
                        return DriverManager.getConnection(
                            properties.getProperty("jdbc.url"),
                            properties.getProperty("jdbc.user"),
                            properties.getProperty("jdbc.password")
                        );
                    } catch (SQLException e) {
                        logger.log(Level.SEVERE, "Database connection error: {0}", e.getMessage());
                        throw new SQLException("Unable to connect to the database. Please check your configuration.");
                    }
                }
            }