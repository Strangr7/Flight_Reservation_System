package com.flightreservation.listener;

import com.flightreservation.util.HibernateUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

@WebListener
public class ApplicationLifecycleListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationLifecycleListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Application starting up...");
        // Initialize Hibernate SessionFactory
        HibernateUtil.getSessionFactory(); 
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Application shutting down...");
        
        // 1. Shutdown Hibernate
        HibernateUtil.shutdown();
        
        // 2. Deregister JDBC drivers (fixes memory leaks)
        deregisterJdbcDrivers();
        
        // 3. Stop MySQL's abandoned connection cleanup thread
        shutdownAbandonedConnectionCleanupThread();
    }

    private void deregisterJdbcDrivers() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                logger.info("Deregistered JDBC driver: {}", driver.getClass().getName());
            } catch (SQLException e) {
                logger.warn("Failed to deregister driver: {}", driver.getClass().getName(), e);
            }
        }
    }

    private void shutdownAbandonedConnectionCleanupThread() {
        try {
            Class.forName("com.mysql.cj.jdbc.AbandonedConnectionCleanupThread")
                .getMethod("checkedShutdown")
                .invoke(null);
            logger.info("Stopped MySQL AbandonedConnectionCleanupThread");
        } catch (Exception e) {
            logger.warn("Failed to stop MySQL AbandonedConnectionCleanupThread", e);
        }
    }
}
