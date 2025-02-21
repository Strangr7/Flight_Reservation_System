package com.flightreservation.util;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnectionTest {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionTest.class);

    @Test
    public void testConnection() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            assertNotNull("Database connection should not be null", conn);
            logger.info("SUCCESS: Database connection is established!");
        } catch (SQLException e) {
            logger.error("ERROR: Database connection failed!", e);
            throw new RuntimeException("Database connection failed", e);
        }
    }
}
