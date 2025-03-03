package com.flightreservation.util;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HibernateTest {
    private static final Logger logger = LoggerFactory.getLogger(HibernateTest.class);

    @Test
    public void testHibernateInitialization() {
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            assertNotNull(sessionFactory, "SessionFactory should not be null");
            logger.info("SUCCESS: Hibernate initialized successfully!");
        } catch (Exception ex) {
            logger.error("ERROR: Hibernate initialization failed!", ex);
            throw new RuntimeException("Hibernate initialization failed", ex);
        }
    }
}