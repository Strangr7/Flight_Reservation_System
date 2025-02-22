package com.flightreservation.util;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariConfig;

public class DatabaseConnection {
	private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
	private static HikariDataSource dataSource; // DataSource for HikariCP connection pool

	// Static block to initialize the HikariCP DataSource/
	static {
		try {
			// Load environment variables from .env file
			Map<String, String> env = loadEnv(".env");

			// Get the database URL, username, and password from environment variables
			String URL = env.getOrDefault("DB_URL", "").trim();
			String USER = env.getOrDefault("DB_USERNAME", "").trim();
			String PASSWORD = env.getOrDefault("DB_PASSWORD", "").trim();

			// Ensure that the necessary database credentials are present
			if (URL.isEmpty() || USER.isEmpty() || PASSWORD.isEmpty()) {
				throw new IllegalStateException("ERROR: Missing database credentials in .env file!");
			}

			// Log the loaded credentials (without the password)
			logger.info("Loaded database URL: {}", URL);
			logger.info("Loaded database user: {}", USER);

			// Set up HikariCP configuration
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(URL); // Set the JDBC URL
			config.setUsername(USER); // Set the database username
			config.setPassword(PASSWORD); // Set the database password
			config.setMaximumPoolSize(10); // Set maximum pool size for connections

			// Initialize the DataSource with the configuration
			dataSource = new HikariDataSource(config);
			logger.info("HikariCP DataSource initialized successfully.");

		} catch (IOException e) {
			// Log any issues encountered while reading the .env file
			logger.error("ERROR: Failed to read .env file", e);
		} catch (IllegalStateException e) {
			// Log missing or incorrect credentials
			logger.error(e.getMessage());
		}
	}

	// Method to get a connection from the HikariCP connection pool
	public static Connection getConnection() throws SQLException {
		logger.info("Connecting to database...");
		return dataSource.getConnection(); // Return a connection from the pool
	}

	// Optional: Method to shut down the HikariCP connection pool when the
	// application ends
	public static void shutdown() {
		if (dataSource != null) {
			dataSource.close(); // Close the connection pool
			logger.info("HikariCP DataSource shut down.");
		}
	}

	// Helper method to load environment variables from a .env file
	private static Map<String, String> loadEnv(String filename) throws IOException {
		Map<String, String> envMap = new HashMap<>();
		File envFile = new File(filename);

		// If the .env file doesn't exist, fall back to system environment variables
		if (!envFile.exists()) {
			logger.warn("WARNING: .env file not found. Using default system environment variables.");
			return System.getenv();
		}

		// Read the .env file and load key-value pairs into a map
		try (BufferedReader br = new BufferedReader(new FileReader(envFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.startsWith("#") && line.contains("=")) {
					String[] parts = line.split("=", 2); // Split by "=" to get key-value pairs
					envMap.put(parts[0].trim(), parts[1].trim()); // Trim the values and add to map
				}
			}
		}
		return envMap; // Return the loaded environment variables
	}

	// Main method for testing the connection and shutting down the pool
	public static void main(String[] args) {
		try (Connection conn = getConnection()) {
			// If the connection is established successfully, log success
			logger.info("SUCCESS: Database connection established!");
		} catch (SQLException e) {
			// If there's an issue with the database connection, log the error
			logger.error("❌ ERROR: Database connection failed!", e);
		} finally {
			// Always shut down the pool when done
			shutdown();
		}
	}
}
