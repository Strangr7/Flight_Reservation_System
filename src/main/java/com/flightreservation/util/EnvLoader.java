package com.flightreservation.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvLoader {
    private static final Logger logger = LoggerFactory.getLogger(EnvLoader.class);
    private static final Pattern KEY_VALUE_PATTERN = Pattern.compile("^\\s*([^#=\\s]+)\\s*=\\s*(.*?)\\s*$");  // Regex for key=value parsing
    private static final String DEFAULT_ENV_FILE = ".env";

    private EnvLoader() {
        // Prevent instantiation
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Loads environment variables from a specified file or falls back to system environment variables.
     *
     * @param filename The path to the .env file (null or empty uses default ".env").
     * @return An immutable Map of environment variables.
     * @throws IOException If file reading fails critically (e.g., permissions issue).
     */
    public static Map<String, String> loadEnv(String filename) throws IOException {
        String envFilePath = (filename == null || filename.trim().isEmpty()) ? DEFAULT_ENV_FILE : filename.trim();
        File envFile = new File(envFilePath);
        Map<String, String> envMap = new HashMap<>();

        if (!envFile.exists() || !envFile.isFile()) {
            logger.warn("Environment file '{}' not found or is not a file. Falling back to system environment variables.", envFile.getAbsolutePath());
            return Collections.unmodifiableMap(new HashMap<>(System.getenv()));
        }

        if (!envFile.canRead()) {
            logger.error("Cannot read environment file '{}'. Check permissions. Falling back to system environment variables.", envFile.getAbsolutePath());
            return Collections.unmodifiableMap(new HashMap<>(System.getenv()));
        }

        logger.info("Loading environment variables from '{}'", envFile.getAbsolutePath());

        try (BufferedReader br = new BufferedReader(new FileReader(envFile, StandardCharsets.UTF_8))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                String trimmedLine = line.trim();

                // Skip empty lines or comments
                if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                    continue;
                }

                var matcher = KEY_VALUE_PATTERN.matcher(trimmedLine);
                if (matcher.matches()) {
                    String key = matcher.group(1);
                    String value = matcher.group(2);

                    if (key.isEmpty()) {
                        logger.warn("Invalid entry at line {}: Empty key in '{}'", lineNumber, trimmedLine);
                        continue;
                    }

                    envMap.put(key, value);
                    logger.debug("Loaded env: {}='{}'", key, value.length() > 50 ? value.substring(0, 47) + "..." : value);
                } else {
                    logger.warn("Skipping malformed line {}: '{}'", lineNumber, trimmedLine);
                }
            }
        } catch (IOException e) {
            logger.error("Failed to read environment file '{}'. Falling back to system environment variables.", envFile.getAbsolutePath(), e);
            return Collections.unmodifiableMap(new HashMap<>(System.getenv()));
        }

        if (envMap.isEmpty()) {
            logger.warn("No valid entries found in '{}'. Returning system environment variables.", envFile.getAbsolutePath());
            return Collections.unmodifiableMap(new HashMap<>(System.getenv()));
        }

        logger.info("Successfully loaded {} environment variables from '{}'", envMap.size(), envFile.getAbsolutePath());
        return Collections.unmodifiableMap(envMap);
    }

    /**
     * Convenience method to load from default .env file.
     *
     * @return An immutable Map of environment variables.
     * @throws IOException If file reading fails critically.
     */
    public static Map<String, String> loadEnv() throws IOException {
        return loadEnv(DEFAULT_ENV_FILE);
    }
}