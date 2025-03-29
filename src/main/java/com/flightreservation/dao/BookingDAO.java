package com.flightreservation.dao;

import java.time.LocalDate;
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
    
    public long countTodayBookings() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(b) FROM Bookings b " +
                    "WHERE FUNCTION('DATE', b.flights.departureTime) = CURRENT_DATE";
        Query<Long> query = session.createQuery(hql, Long.class);
        return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error counting today's bookings", e);
            return 0;
        }
    }
    
    public long countYesterdayBookings() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        	String hql = "SELECT COUNT(b) FROM Bookings b " +
                    "WHERE CAST(b.flights.departureTime AS date) = " +
                    "CAST(CURRENT_TIMESTAMP AS date) - 1";
        Query<Long> query = session.createQuery(hql, Long.class);
        return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error counting yesterday's bookings", e);
            return 0;
        }
    }
    
    public double getTodayRevenue() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {         
        	String hql = "SELECT COALESCE(SUM(fp.basePrice + fp.dynamicPrice), 0.0) " +
                    "FROM Bookings b " +
                    "JOIN b.flights f " +  // Changed from 'flight' to 'flights'
                    "JOIN FlightPrices fp ON fp.flight.flightId = f.flightId " +
                    "JOIN b.seats s " +  // Changed from 'seat' to 'seats'
                    "JOIN s.seatClass sc " +
                    "WHERE sc.classId = fp.flightClass.classId " +
                    "AND DATE(f.departureTime) = CURRENT_DATE";
            
            Query<Double> query = session.createQuery(hql, Double.class);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error calculating today's revenue", e);
            return 0.0;
        }
    }
    
    public List<Object[]> getLast7DaysBookingCounts() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusDays(6);
            
            String hql = "SELECT DATE(f.departureTime), COUNT(b) " +
                       "FROM Bookings b " +
                       "JOIN b.flights f " +
                       "WHERE f.departureTime BETWEEN :startDate AND :endDate " +
                       "GROUP BY DATE(f.departureTime) " +
                       "ORDER BY DATE(f.departureTime)";
            
            return session.createQuery(hql, Object[].class)
                .setParameter("startDate", startDate.atStartOfDay())
                .setParameter("endDate", endDate.plusDays(1).atStartOfDay())
                .getResultList();
        }
    }
    
}
