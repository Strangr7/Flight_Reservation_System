package com.flightreservation.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static volatile SessionFactory sessionFactory;  // Thread-safe singleton
    private static final Object LOCK = new Object();  // For synchronized initialization

    private HibernateUtil() {
        // Prevent instantiation
        throw new AssertionError("Utility class should not be instantiated");
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (LOCK) {
                if (sessionFactory == null) {
                    initializeSessionFactory();
                }
            }
        }
        return sessionFactory;
    }

    private static void initializeSessionFactory() {
        StandardServiceRegistry registry = null;
        try {
            logger.info("Initializing Hibernate SessionFactory...");

            // Load configuration from hibernate.cfg.xml
            registry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")  // Explicitly specify config file
                    .build();

            // Build SessionFactory with entity mappings
            sessionFactory = new MetadataSources(registry)
                    .addAnnotatedClass(com.flightreservation.model.Flights.class)
                    .addAnnotatedClass(com.flightreservation.model.Stops.class)
                    .buildMetadata()
                    .buildSessionFactory();

            logger.info("Hibernate SessionFactory initialized successfully.");

            // Validate connection on startup using modern API
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                // Use a simple native SQL query with the updated API
                Number result = (Number) session.createNativeQuery("SELECT 1", Number.class)
                                               .getSingleResult();
                if (result.intValue() == 1) {
                    logger.info("Database connectivity verified.");
                } else {
                    logger.warn("Unexpected result from connectivity test: {}", result);
                }
                session.getTransaction().commit();
            }

        } catch (Exception e) {
            logger.error("Failed to initialize Hibernate SessionFactory!", e);
            if (registry != null) {
                StandardServiceRegistryBuilder.destroy(registry);
            }
            throw new IllegalStateException("Could not initialize Hibernate SessionFactory.", e);
        }
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            synchronized (LOCK) {
                if (sessionFactory != null && !sessionFactory.isClosed()) {
                    logger.info("Shutting down Hibernate SessionFactory...");
                    sessionFactory.close();
                    sessionFactory = null;
                    logger.info("Hibernate SessionFactory shut down.");
                }
            }
        }
    }

    // Optional: For testing or reinitialization (use cautiously in production)
    public static void reset() {
        shutdown();
        logger.warn("HibernateUtil reset. Will reinitialize on next use.");
    }
}