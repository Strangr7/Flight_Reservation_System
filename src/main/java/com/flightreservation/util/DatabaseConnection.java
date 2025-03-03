package com.flightreservation.util;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnection {
	private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
	private static String URL;
	private static String USER;
	private static String PASSWORD;

	private DatabaseConnection() {
		// Private constructor to prevent instantiation
	}

	public static synchronized void initialize() {
		if (URL != null && USER != null && PASSWORD != null) {
			return; // Already initialized
		}

		try {
			// Load environment variables
			String envFilePath = System.getProperty("env.file", ".env");
			logger.info("Looking for .env at: " + new File(envFilePath).getAbsolutePath());
			Map<String, String> env = EnvLoader.loadEnv(envFilePath);

			URL = env.getOrDefault("DB_URL", "").trim();
			USER = env.getOrDefault("DB_USERNAME", "").trim();
			PASSWORD = env.getOrDefault("DB_PASSWORD", "").trim();
			logger.info("Loaded DB_URL='{}', DB_USERNAME='{}', DB_PASSWORD='{}'", URL, USER, PASSWORD);

			if (URL.isEmpty() || USER.isEmpty() || PASSWORD.isEmpty()) {
				logger.error("Missing database credentials! Check .env file.");
				throw new IllegalStateException("Database credentials not provided in .env file.");
			}

			logger.info("Loaded database configuration successfully.");

			// Test the connection
			if (!testDatabaseConnection()) {
				throw new SQLException("Database connection test failed.");
			}

		} catch (IOException e) {
			logger.error("Failed to read .env file!", e);
			throw new IllegalStateException("Error loading environment variables from .env", e);
		} catch (SQLException e) {
			logger.error("Database connection test failed!", e);
			throw new IllegalStateException("Cannot establish database connection.", e);
		}
	}

	public static Connection getConnection() throws SQLException {
		if (URL == null || USER == null || PASSWORD == null) {
			initialize();
		}
		logger.info("Retrieving a database connection...");
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	private static boolean testDatabaseConnection() {
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
			boolean isValid = conn.isValid(2);
			if (isValid) {
				logger.info("Database connection test successful!");
			} else {
				logger.error("Invalid database connection.");
			}
			return isValid;
		} catch (SQLException e) {
			logger.error("Error testing database connection!", e);
			return false;
		}
	}

	// Optional: Getter for Hibernate configuration
	public static String getUrl() {
		if (URL == null)
			initialize();
		return URL;
	}

	public static String getUsername() {
		if (USER == null)
			initialize();
		return USER;
	}

	public static String getPassword() {
		if (PASSWORD == null)
			initialize();
		return PASSWORD;
	}
}