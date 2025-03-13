package com.flightreservation.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import com.flightreservation.model.Bookings;
import com.flightreservation.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookingDAO {
    private static final Logger logger = LoggerFactory.getLogger(BookingDAO.class);

    public List<Bookings> getBookingsByUserId(Integer userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Bookings> query = session.createQuery("FROM Bookings WHERE users.id = :userId", Bookings.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching bookings for userId: {}", userId, e);
            return List.of(); // Return empty list instead of throwing
        }
    }
}
