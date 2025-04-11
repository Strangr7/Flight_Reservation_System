package com.flightreservation.service;

import java.util.List;
import com.flightreservation.dao.BookingDAO;
import com.flightreservation.model.BookingSearchCriteria;
import com.flightreservation.model.Bookings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
    private final BookingDAO bookingDAO = new BookingDAO();

    public List<Bookings> getBookingsByUserId(int userId) {
        try {
            List<Bookings> bookings = bookingDAO.getBookingsByUserId(userId);
            logger.info("Fetched {} bookings for user ID: {}", bookings.size(), userId);
            return bookings;
        } catch (Exception e) {
            logger.error("Service error fetching bookings for userId: {}", userId, e);
            return List.of();
        }
    }
    
    
    public List<Bookings> searchBookings(BookingSearchCriteria criteria, int page, int size) {
        int offset = (page - 1) * size;
        return bookingDAO.searchBookings(criteria, offset, size);
    }

    public int getFilteredBookingCount(BookingSearchCriteria criteria) {
        return bookingDAO.getFilteredBookingCount(criteria);
    }

    public Bookings getBookingById(int id) {
        return bookingDAO.getBookingById(id);
    }
}
