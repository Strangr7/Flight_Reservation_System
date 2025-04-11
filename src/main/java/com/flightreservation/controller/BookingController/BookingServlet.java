package com.flightreservation.controller.BookingController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import com.flightreservation.controller.FlightController.FlightServlet;
import com.flightreservation.model.BookingSearchCriteria;
import com.flightreservation.model.Bookings;
import com.flightreservation.model.Flights;
import com.flightreservation.model.enums.BookingStatus;
import com.flightreservation.service.AircraftService;
import com.flightreservation.service.AirlineService;
import com.flightreservation.service.AirportService;
import com.flightreservation.service.BookingService;
import com.flightreservation.service.FlightService;

/**
 * Servlet implementation class BookingServlet
 */
@WebServlet(name="BookingServlet", urlPatterns = {"/bookings", "/bookings/*"})
public class BookingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(FlightServlet.class.getName());
    private final BookingService bookingService = new BookingService();
    
 // Constants for pagination
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		try {
			int page = getIntParameter(request, "page", DEFAULT_PAGE);
	        int size = getIntParameter(request, "size", DEFAULT_PAGE_SIZE);
	        
	     // Get search/filter parameters
	        String searchQuery = request.getParameter("searchQuery");
	        String statusFilter = request.getParameter("statusFilter");
	        String dateFilter = request.getParameter("dateFilter");
	        String flightNumber = request.getParameter("flightNumber");
	        
	        // Create search criteria
	        BookingSearchCriteria criteria = new BookingSearchCriteria(
	            searchQuery, 
	            statusFilter, 
	            dateFilter,
	            flightNumber
	        );
	        List<Bookings> bookings = bookingService.searchBookings(criteria, page, size);
	        int totalBookings = bookingService.getFilteredBookingCount(criteria);
	        int totalPages = (int) Math.ceil((double) totalBookings / size);
	        
	        request.setAttribute("bookings", bookings);
	        request.setAttribute("currentPage", page);
	        request.setAttribute("pageSize", size);
	        request.setAttribute("totalPages", totalPages);
	        request.setAttribute("totalItems", totalBookings);
	        request.setAttribute("statusOptions", BookingStatus.values());
	        
	     // Preserve search parameters for pagination
	        request.setAttribute("searchQuery", searchQuery);
	        request.setAttribute("statusFilter", statusFilter);
	        request.setAttribute("dateFilter", dateFilter);
	        request.setAttribute("flightNumber", flightNumber);

	        forwardToView(request, response, "/WEB-INF/views/bookings/list.jsp");
		}
        catch (Exception e) {
            request.setAttribute("error", "An error occurred while processing your request");
            forwardToView(request, response, "/WEB-INF/views/error.jsp");
        }
	}
	
	private int getIntParameter(HttpServletRequest request, String param, int defaultValue) {
        String value = request.getParameter(param);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private void forwardToView(HttpServletRequest request, HttpServletResponse response, String viewPath)
            throws ServletException, IOException {
        request.getRequestDispatcher(viewPath).forward(request, response);
    }

}
