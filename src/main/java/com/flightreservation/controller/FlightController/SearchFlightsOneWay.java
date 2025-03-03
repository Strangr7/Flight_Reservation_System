package com.flightreservation.controller.FlightController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flightreservation.DTO.FlightResultOneWay;
import com.flightreservation.service.FlightService;

/**
 * Servlet to handle one-way flight search requests via HTTP GET. Mapped to
 * "/SearchFlights" URL. Extracts parameters from the request, searches for
 * flights using FlightService, and forwards results to a JSP page.
 */
@WebServlet("/SearchFlights")
public class SearchFlightsOneWay extends HttpServlet {

	// Unique ID for serialization (required for servlets, though rarely used)
	private static final long serialVersionUID = 1L;

	// Logger instance for tracking events and errors in this servlet
	private static final Logger logger = LoggerFactory.getLogger(SearchFlightsOneWay.class);

	// Instance of FlightService to handle business logic and data retrieval
	private final FlightService flightService = new FlightService();

	/**
	 * Handles HTTP GET requests to search for one-way flights. Extracts
	 * departure/destination airport codes and departure date from the request,
	 * validates them, searches for flights, and forwards to a JSP page for display.
	 * 
	 * @param request  The HTTP request containing search parameters
	 * @param response The HTTP response to send back to the client
	 * @throws ServletException If a servlet-specific error occurs
	 * @throws IOException      If an I/O error occurs (e.g., forwarding fails)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// Step 1: Extract query parameters from the GET request
			// These come from the URL (e.g., /SearchFlights?departureAirportCode=JFK&...)
			String departureAirportCode = request.getParameter("departureAirportCode");
			String destinationAirportCode = request.getParameter("destinationAirportCode");
			String departureDateStr = request.getParameter("departureDate");

			// Step 2: Validate that all required parameters are present
			if (departureAirportCode == null || destinationAirportCode == null || departureDateStr == null) {
				// Send a 400 Bad Request error if any parameter is missing
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Missing required parameters: departureAirportCode, destinationAirportCode, departureDate");
				return; // Exit early since validation failed
			}

			// Step 3: Parse the departure date string into a LocalDate object
			// Expects format YYYY-MM-DD (e.g., "2025-03-01")
			LocalDate departureDate = LocalDate.parse(departureDateStr);

			// Step 4: Search for flights using the FlightService
			List<FlightResultOneWay> flights = flightService.searchFlights(departureAirportCode, destinationAirportCode,
					departureDate);
			// Log the search attempt for debugging/tracking
			logger.info("Searching with departureAirportCode={}, destinationAirportCode={}, departureDate={}",
					departureAirportCode, destinationAirportCode, departureDate);
			HttpSession session = request.getSession();
		
			session.setAttribute("flights", flights);
			session.setAttribute("departureAirportCode", departureAirportCode);
			session.setAttribute("destinationAirportCode", destinationAirportCode);
			session.setAttribute("departureDate", departureDateStr);

			// Debug: Print session attributes
			logger.info("Session attributes after setting:");
			logger.info("flights: {}", session.getAttribute("flights"));
			logger.info("departureAirportCode: {}", session.getAttribute("departureAirportCode"));
			logger.info("destinationAirportCode: {}", session.getAttribute("destinationAirportCode"));
			logger.info("departureDate: {}", session.getAttribute("departureDate"));

			// Step 5: Handle the search results
			if (flights.isEmpty()) {
				// Log that no flights were found
				logger.info("No flights found for departureAirportCode={}, destinationAirportCode={}, departureDate={}",
						departureAirportCode, destinationAirportCode, departureDate);
				// Set an error message attribute for the JSP
				request.setAttribute("error", "No flights found for the given criteria");
				// Forward to a JSP page to display the "no flights" message
				request.getRequestDispatcher("/WEB-INF/views/noFlights.jsp").forward(request, response);
			} else {
				// Log the number of flights found
				logger.info("Found {} flights for departureAirportCode={}, destinationAirportCode={}, departureDate={}",
						flights.size(), departureAirportCode, destinationAirportCode, departureDate);
				// Set attributes for the JSP to display flight results

				request.setAttribute("flights", flights);
				request.setAttribute("departureAirportCode", departureAirportCode);
				request.setAttribute("destinationAirportCode", destinationAirportCode);
				request.setAttribute("departureDate", departureDate);
				// Forward to a JSP page to display the flight results
				request.getRequestDispatcher("/WEB-INF/views/flightResultsOneWay.jsp").forward(request, response);
			}

		} catch (DateTimeParseException e) {
			// Handle invalid date format (e.g., "2025-13-01" or "abc")
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid departure date format (use YYYY-MM-DD)");
			logger.error("Invalid departure date format: {}", e.getMessage(), e);
		} catch (Exception e) {
			// Catch any unexpected errors (e.g., database issues from FlightService)
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
			logger.error("Error searching flights: {}", e.getMessage(), e);
		}
	}
}