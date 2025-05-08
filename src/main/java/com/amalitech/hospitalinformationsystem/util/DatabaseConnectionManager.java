package com.amalitech.hospitalinformationsystem.util;

            import java.io.IOException;
            import java.io.InputStream;
            import java.sql.Connection;
            import java.sql.DriverManager;
            import java.sql.SQLException;
            import java.util.Properties;

            public final class DatabaseConnectionManager {
                private static final Properties properties = new Properties();

                static {
                    try (InputStream input = DatabaseConnectionManager.class.getClassLoader().getResourceAsStream("db-config.properties")) {
                        if (input == null) {
                            throw new IOException("Configuration file 'db-config.properties' not found.");
                        }
                        properties.load(input);
                        Class.forName(properties.getProperty("jdbc.driver"));
                    } catch (IOException | ClassNotFoundException e) {
                        throw new ExceptionInInitializerError("Failed to load database configuration: " + e.getMessage());
                    }
                }

                public static Connection getConnection() throws SQLException {
                    return DriverManager.getConnection(
                        properties.getProperty("jdbc.url"),
                        properties.getProperty("jdbc.user"),
                        properties.getProperty("jdbc.password")
                    );
                }
            }