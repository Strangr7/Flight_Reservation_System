package com.flightreservation.util;



import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnectionTest {
	// Logger instance for logging test execution details
	private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionTest.class);

	@Test
	public void testConnection() {
		try (Connection conn = DatabaseConnection.getConnection()) {
			// Assert that the connection is not null, meaning the connection to the
			// database was successful
			 assertNotNull(conn, "Database connection should not be null");

			// Log success message if the connection is established
			logger.info("SUCCESS: Database connection is established!");
		} catch (SQLException e) {
			// Log an error message if there is an issue with the database connection
			logger.error("ERROR: Database connection failed!", e);

			// Rethrow the exception as a runtime exception to indicate test failure
			throw new RuntimeException("Database connection failed", e);
		}
	}
}
