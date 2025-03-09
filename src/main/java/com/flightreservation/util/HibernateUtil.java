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
    private static volatile SessionFactory sessionFactory;
    private static final Object LOCK = new Object();

    private HibernateUtil() {
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

            // Load configuration and mappings from hibernate.cfg.xml
            registry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .build();

            // Build SessionFactory (no manual addAnnotatedClass since mappings are in config)
            sessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();

            logger.info("Hibernate SessionFactory initialized successfully.");

            // Validate connection
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
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

    public static void reset() {
        shutdown();
        logger.warn("HibernateUtil reset. Will reinitialize on next use.");
    }
}