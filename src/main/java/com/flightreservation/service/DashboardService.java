package com.flightreservation.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flightreservation.dao.BookingDAO;
import com.flightreservation.dao.FlightDAO;

public class DashboardService {
	private final BookingDAO bookingDao;
    private final FlightDAO flightDao;
	
	public DashboardService() {
        this.bookingDao = new BookingDAO();
        this.flightDao = new FlightDAO();
    }

	public Map<String, Object> getDashboardMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        // Bookings
        long todayBookings = bookingDao.countTodayBookings();
        long yesterdayBookings = bookingDao.countYesterdayBookings();
        System.out.println("**************Yesterday's bookings: " + bookingDao.countYesterdayBookings());
        double bookingChange = 0.0;
        double revenueChange = 0.0;
        bookingChange = yesterdayBookings == 0 ? 0 : 
            ((todayBookings - yesterdayBookings) * 100.0) / yesterdayBookings;
        bookingChange = Math.round(bookingChange * 10) / 10.0; // 1 decimal place
        
        metrics.put("todayBookings", todayBookings);
        metrics.put("bookingChange", bookingChange); 
        
        // Revenue
        double todayRevenue = bookingDao.getTodayRevenue();
        System.out.println("**************Today's Revenue: " + bookingDao.getTodayRevenue());
        metrics.put("revenue", todayRevenue);
        
        // Flights
        long activeFlights = flightDao.countActiveFlights();
        double onTimePercentage = flightDao.getOnTimePercentage();
        double occupancyRate = flightDao.getOccupancyRate();

        metrics.put("activeFlights", activeFlights);
        metrics.put("onTimePercentage", Math.round(onTimePercentage * 10) / 10.0);
        metrics.put("occupancyRate", Math.round(occupancyRate * 10) / 10.0);
        
     // Data for booking trend
        Map<String, Object> bookingTrends = getLast7DaysBookingTrends();
        metrics.put("bookingTrends", bookingTrends);
        System.out.println(metrics + "=========================================================");
        
        
        return metrics;
    }
	
	private Map<String, Object> getLast7DaysBookingTrends() {
	    Map<String, Object> trends = new HashMap<>();
	    
	    // Get data from DAO
	    List<Object[]> rawData = bookingDao.getLast7DaysBookingCounts();
	    
	    // Process dates and counts
	    List<String> dates = new ArrayList<>();
	    List<Integer> counts = new ArrayList<>();
	    
	    // Ensure we have data for all 7 days (even if zero)
	    LocalDate today = LocalDate.now();
	    for (int i = 6; i >= 0; i--) {
	        LocalDate date = today.minusDays(i);
	        String dateStr = date.format(DateTimeFormatter.ofPattern("MMM dd"));

	        int count = rawData.stream()
	            .filter(arr -> ((Date)arr[0]).toLocalDate().equals(date))
	            .findFirst()
	            .map(arr -> ((Number)arr[1]).intValue())
	            .orElse(0);
	        dates.add(dateStr);
	        counts.add(count);
	    }
	    
	    trends.put("dates", dates);
	    trends.put("counts", counts);
	    
	    return trends;
	}
}
