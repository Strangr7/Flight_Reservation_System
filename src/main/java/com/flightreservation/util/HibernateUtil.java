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
	private static volatile StandardServiceRegistry registry; // Keep registry as a field
	private static final Object LOCK = new Object();

	static {
        // Add shutdown hook to ensure proper cleanup
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Executing shutdown hook for Hibernate resources");
            shutdown();
        }));
    }
	/*
	 * private HibernateUtil() { throw new
	 * AssertionError("Utility class should not be instantiated"); }
	 */

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null || sessionFactory.isClosed()) { // Check if null or closed
			synchronized (LOCK) {
				if (sessionFactory == null || sessionFactory.isClosed()) {
					logger.info("SessionFactory is null or closed, reinitializing...");
					initializeSessionFactory();
				}
			}
		}
		return sessionFactory;
	}

	private static void initializeSessionFactory() {
		try {
			logger.info("Initializing Hibernate SessionFactory...");

			// Build registry only if it doesn’t exist or was destroyed
			if (registry == null) {
				registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
			}

			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

			logger.info("Hibernate SessionFactory initialized successfully.");

			// Validate connection with a simpler approach
			try (Session session = sessionFactory.openSession()) {
				session.beginTransaction();
				int result = session.createNativeQuery("SELECT 1", Integer.class).getSingleResult();
				if (result == 1) {
					logger.info("Database connectivity verified.");
				} else {
					logger.warn("Unexpected result from connectivity test: {}", result);
				}
				session.getTransaction().commit();
			}

		} catch (Exception e) {
			logger.error("Failed to initialize Hibernate SessionFactory!", e);
			// Don’t destroy registry here; keep it for retry
			throw new IllegalStateException("Could not initialize Hibernate SessionFactory.", e);
		}
	}

	public static void shutdown() {
		if (sessionFactory != null && !sessionFactory.isClosed()) {
			synchronized (LOCK) {
				if (sessionFactory != null && !sessionFactory.isClosed()) {
					logger.info("Shutting down Hibernate SessionFactory...");
					sessionFactory.close();
//					sessionFactory = null;
				}
				if (registry != null) {
					StandardServiceRegistryBuilder.destroy(registry);
					registry = null;
					logger.info("StandardServiceRegistry destroyed.");
				}
//				logger.info("Hibernate SessionFactory shut down.");
				
			}
		}
	}

//	public static void resetSessionFactory() {
//		shutdown();
//		initializeSessionFactory();
//		logger.info("SessionFactory reset complete.");
//	}

}