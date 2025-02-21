package com.flightreservation.util;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnection {
	private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);

	private static String URL;
	private static String USER;
	private static String PASSWORD;

	static {
		try {
			
			Map<String, String> env = loadEnv(".env");

			URL = env.getOrDefault("DB_URL", "").trim();
			USER = env.getOrDefault("DB_USERNAME", "").trim();
			PASSWORD = env.getOrDefault("DB_PASSWORD", "").trim();

			if (URL.isEmpty() || USER.isEmpty() || PASSWORD.isEmpty()) {
				throw new IllegalStateException("ERROR: Missing database credentials in .env file!");
			}

			logger.info("Loaded database URL: {}", URL);
			logger.info("Loaded database user: {}", USER);

			// Load MySQL JDBC Driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			logger.info("MySQL JDBC Driver loaded successfully.");
		} catch (IOException e) {
			logger.error("ERROR: Failed to read .env file", e);
		} catch (ClassNotFoundException e) {
			logger.error("ERROR: MySQL JDBC Driver not found.", e);
		} catch (IllegalStateException e) {
			logger.error(e.getMessage());
		}
	}

	public static Connection getConnection() throws SQLException {
		logger.info("Connecting to database...");
		Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
		logger.info("SUCCESS: Database connection established!");
		return connection;
	}

	private static Map<String, String> loadEnv(String filename) throws IOException {
		Map<String, String> envMap = new HashMap<>();
		File envFile = new File(filename);

		if (!envFile.exists()) {
			logger.warn("WARNING: .env file not found. Using default system environment variables.");
			return System.getenv();
		}

		try (BufferedReader br = new BufferedReader(new FileReader(envFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.startsWith("#") && line.contains("=")) {
					String[] parts = line.split("=", 2);
					envMap.put(parts[0].trim(), parts[1].trim());
				}
			}
		}
		return envMap;
	}

	public static void main(String[] args) {
		try {
			getConnection();
		} catch (SQLException e) {
			logger.error("❌ ERROR: Database connection failed!", e);
		}
	}
}
