package com.flightreservation.controller.FlightController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flightreservation.DTO.FlightResultRoundTrip;
import com.flightreservation.service.FlightService;

/**
 * Servlet to handle round-trip flight search requests via HTTP GET. Mapped to
 * "/SearchRoundTripFlights". Extracts parameters, validates them, searches for
 * round-trip flights using FlightService, and forwards to a JSP page.
 */
@WebServlet("/SearchRoundTripFlights")
public class SearchFlightsRoundTrip extends HttpServlet {
	// Serialization ID (required for servlets, though rarely used here)
	private static final long serialVersionUID = 1L;

	// Logger for tracking events and errors specific to this servlet
	private static final Logger logger = LoggerFactory.getLogger(SearchFlightsRoundTrip.class);

	// FlightService instance to handle business logic and data access
	private final FlightService flightService = new FlightService();

	/**
	 * Handles HTTP GET requests to search for round-trip flights. Extracts
	 * departure/destination airport codes and departure/return dates, validates
	 * inputs, searches flights, and forwards results to a JSP page.
	 * 
	 * @param request  The HTTP request with search parameters
	 * @param response The HTTP response to send back to the client
	 * @throws ServletException If a servlet-specific error occurs
	 * @throws IOException      If an I/O error occurs (e.g., forwarding fails)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// Step 1: Extract query parameters from the GET request
			// Example URL:
			// /SearchRoundTripFlights?departureAirportCode=JFK&destinationAirportCode=LAX&departureDate=2025-03-01&returnDate=2025-03-05
			String departureAirportCode = request.getParameter("departureAirportCode");
			String destinationAirportCode = request.getParameter("destinationAirportCode");
			String departureDateStr = request.getParameter("departureDate");
			String returnDateStr = request.getParameter("returnDate");

			// Step 2: Validate required parameters
			if (departureAirportCode == null || destinationAirportCode == null || departureDateStr == null
					|| returnDateStr == null) {
				// Send a 400 Bad Request error if any parameter is missing
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Missing required parameters: departureAirportCode, destinationAirportCode, departureDate, returnDate");
				return; // Exit early if validation fails
			}

			// Step 3: Parse date strings into LocalDate objects
			// Expects YYYY-MM-DD format (e.g., "2025-03-01")
			LocalDate departureDate = LocalDate.parse(departureDateStr);
			LocalDate returnDate = LocalDate.parse(returnDateStr);

			// Step 4: Validate date logic
			// Ensure return date isn’t before departure date
			if (returnDate.isBefore(departureDate)) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Return date cannot be earlier than departure date");
				return; // Exit early if dates are invalid
			}

			// Step 5: Search for round-trip flights using FlightService
			FlightResultRoundTrip flightResults = flightService.searchRoundTripFlights(departureAirportCode,
					destinationAirportCode, departureDate, returnDate);
			// Log the search attempt for tracking
			logger.info(
					"Searching round-trip with departureAirportCode={}, destinationAirportCode={}, departureDate={}, returnDate={}",
					departureAirportCode, destinationAirportCode, departureDate, returnDate);

			// Step 6: Handle the search results
			if (flightResults.getOutboundFlights().isEmpty() || flightResults.getReturnFlights().isEmpty()) {
				// Log if no complete round-trip is found
				logger.info("No round-trip flights found for the given criteria");
				// Set an error message for the JSP
				request.setAttribute("error", "No round-trip flights found for the given criteria");
				// Forward to a JSP page to display the "no flights" message
				request.getRequestDispatcher("/WEB-INF/views/noFlights.jsp").forward(request, response);
			} else {
				// Log the number of outbound and return flights found
				logger.info("Found {} outbound and {} return flights", flightResults.getOutboundFlights().size(),
						flightResults.getReturnFlights().size());
				// Set attributes for the JSP to display round-trip results
				request.setAttribute("flightResults", flightResults);
				request.setAttribute("departureAirportCode", departureAirportCode);
				request.setAttribute("destinationAirportCode", destinationAirportCode);
				request.setAttribute("departureDate", departureDate);
				request.setAttribute("returnDate", returnDate);
				// Forward to a JSP page to display the flight results
				request.getRequestDispatcher("/WEB-INF/views/flightResultsRoundTrip.jsp").forward(request, response);
			}
		} catch (DateTimeParseException e) {
			// Handle invalid date formats (e.g., "2025-13-01" or "abc")
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date format (use YYYY-MM-DD)");
			logger.error("Invalid date format: {}", e.getMessage(), e);
		} catch (Exception e) {
			// Catch unexpected errors (e.g., database issues from FlightService)
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
			logger.error("Error searching round-trip flights: {}", e.getMessage(), e);
		}
	}
}