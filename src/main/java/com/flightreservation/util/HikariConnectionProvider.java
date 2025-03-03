package com.flightreservation.util;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariConnectionProvider implements ConnectionProvider {
   

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(HikariConnectionProvider.class);

    @Override
    public Connection getConnection() throws SQLException {
        logger.debug("Providing connection from HikariDataSource...");
        return DatabaseConnection.getConnection(); // Delegates to your existing HikariDataSource
    }

    @Override
    public void closeConnection(Connection conn) throws SQLException {
        logger.debug("Closing connection...");
        conn.close(); // HikariCP handles pooling, so this just returns it to the pool
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true; // Allows Hibernate to release connections aggressively
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
        return false; // No unwrapping needed
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null; // No unwrapping needed
    }
}