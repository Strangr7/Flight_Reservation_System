package com.flightreservation.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.flightreservation.model.Seats;
import com.flightreservation.model.enums.FlightClasses;
import com.flightreservation.util.HibernateUtil;

public class SeatDAO {
    private static final Logger logger = LoggerFactory.getLogger(SeatDAO.class);
    
    public List<Seats> getAvailableSeats(int flightId, String className) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Convert String to FlightClasses enum
            FlightClasses flightClass;
            try {
                // Match the String to enum name (e.g., "Business" -> BUSINESS)
                flightClass = FlightClasses.valueOf(className.toUpperCase().replace(" ", "_"));
            } catch (IllegalArgumentException e) {
                logger.error("Invalid className: {}", className, e);
                return List.of(); // Return empty list if className is invalid
            }

            String hql = "FROM Seats s WHERE s.flight.flightId = :flightId AND s.seatClass.className = :className AND s.isAvailable = true";
            Query<Seats> query = session.createQuery(hql, Seats.class);
            query.setParameter("flightId", flightId);
            query.setParameter("className", flightClass); // Pass enum value
            List<Seats> seats = query.list();
            logger.info("Found {} available seats for flightId={}, class={}", seats.size(), flightId, className);
            return seats;
        } catch (Exception e) {
            logger.error("Error fetching available seats: {}", e.getMessage(), e);
            return List.of();
        }
    }
}