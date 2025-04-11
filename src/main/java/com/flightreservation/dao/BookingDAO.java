package com.flightreservation.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.flightreservation.model.BookingSearchCriteria;
import com.flightreservation.model.Bookings;
import com.flightreservation.model.enums.BookingStatus;
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
    
    
    public List<Bookings> searchBookings(BookingSearchCriteria criteria, int offset, int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder(
                "SELECT DISTINCT b FROM Bookings b " +
                "LEFT JOIN FETCH b.passengers " +
                "LEFT JOIN FETCH b.flights f " +
                "LEFT JOIN FETCH f.departureAirport " +
                "LEFT JOIN FETCH f.destinationAirport " +
                "LEFT JOIN FETCH b.seats " +
                "LEFT JOIN FETCH b.meals "
            );
            
            List<String> conditions = new ArrayList<>();
            Map<String, Object> parameters = new HashMap<>();
            
            // Add search conditions
            if (criteria.getFlightNumber() != null && !criteria.getFlightNumber().isEmpty()) {
                conditions.add("b.flights.flightNumber LIKE :flightNumber");
                parameters.put("flightNumber", "%" + criteria.getFlightNumber() + "%");
            }
            
            if (criteria.getSearchQuery() != null && !criteria.getSearchQuery().isEmpty()) {
                conditions.add("(b.PNR LIKE :searchQuery OR b.bookingId = :searchId)");
                parameters.put("searchQuery", "%" + criteria.getSearchQuery() + "%");
                try {
                    parameters.put("searchId", Integer.parseInt(criteria.getSearchQuery()));
                } catch (NumberFormatException e) {
                    parameters.put("searchId", -1); // Will never match
                }
            }
            
            if (criteria.getStatusFilter() != null && !criteria.getStatusFilter().isEmpty()) {
                conditions.add("b.bookingStatus = :status");
                parameters.put("status", BookingStatus.valueOf(criteria.getStatusFilter()));
            }
            
            if (criteria.getDateFilter() != null && !criteria.getDateFilter().isEmpty()) {
                conditions.add("DATE(b.bookingDate) = :bookingDate");
                parameters.put("bookingDate", LocalDate.parse(criteria.getDateFilter()));
            }
            
            // Build WHERE clause
            if (!conditions.isEmpty()) {
                hql.append(" WHERE ").append(String.join(" AND ", conditions));
            }
            
            hql.append(" ORDER BY b.bookingDate DESC");
            
            // Create and execute query
            Query<Bookings> query = session.createQuery(hql.toString(), Bookings.class);
            parameters.forEach(query::setParameter);
            
            return query.setFirstResult(offset)
                       .setMaxResults(limit)
                       .getResultList();
        } catch (Exception e) {
            logger.error("Error searching bookings", e);
            return List.of();
        }
    }

    public int getFilteredBookingCount(BookingSearchCriteria criteria) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("SELECT COUNT(DISTINCT b) FROM Bookings b ");
            
            List<String> conditions = new ArrayList<>();
            Map<String, Object> parameters = new HashMap<>();
            
            if (criteria.getFlightNumber() != null && !criteria.getFlightNumber().isEmpty()) {
                conditions.add("b.flights.flightNumber LIKE :flightNumber");
                parameters.put("flightNumber", "%" + criteria.getFlightNumber() + "%");
            }
            
            if (criteria.getSearchQuery() != null && !criteria.getSearchQuery().isEmpty()) {
                conditions.add("(b.PNR LIKE :searchQuery OR b.bookingId = :searchId)");
                parameters.put("searchQuery", "%" + criteria.getSearchQuery() + "%");
                try {
                    parameters.put("searchId", Integer.parseInt(criteria.getSearchQuery()));
                } catch (NumberFormatException e) {
                    parameters.put("searchId", -1);
                }
            }
            
            if (criteria.getStatusFilter() != null && !criteria.getStatusFilter().isEmpty()) {
                conditions.add("b.bookingStatus = :status");
                parameters.put("status", BookingStatus.valueOf(criteria.getStatusFilter()));
            }
            
            if (criteria.getDateFilter() != null && !criteria.getDateFilter().isEmpty()) {
                conditions.add("DATE(b.bookingDate) = :bookingDate");
                parameters.put("bookingDate", LocalDate.parse(criteria.getDateFilter()));
            }
            
            if (!conditions.isEmpty()) {
                hql.append(" WHERE ").append(String.join(" AND ", conditions));
            }
            
            Query<Long> query = session.createQuery(hql.toString(), Long.class);
            parameters.forEach(query::setParameter);
            
            return query.getSingleResult().intValue();
        } catch (Exception e) {
            logger.error("Error counting filtered bookings", e);
            return 0;
        }
    }
    
    public Bookings getBookingById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT DISTINCT b FROM Bookings b " +
                         "LEFT JOIN FETCH b.users " +
                         "LEFT JOIN FETCH b.flights f " +
                         "LEFT JOIN FETCH f.departureAirport " +
                         "LEFT JOIN FETCH f.destinationAirport " +
                         "LEFT JOIN FETCH b.seats " +
                         "LEFT JOIN FETCH b.meals " +
                         "LEFT JOIN FETCH b.passengers " +
                         "WHERE b.bookingId = :id";
            return session.createQuery(hql, Bookings.class)
                         .setParameter("id", id)
                         .getSingleResult();
        } catch (Exception e) {
            logger.error("Error fetching booking by id: {}", id, e);
            return null;
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
