package com.flightreservation.controller.FlightController;

import com.flightreservation.service.*;
import com.flightreservation.model.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Logger;

@WebServlet(name = "FlightServlet", 
            urlPatterns = {"/flights", "/flights/*"})
public class FlightServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(FlightServlet.class.getName());
    private final FlightService flightService = new FlightService();
    private final AirportService airportService = new AirportService();
    private final AirlineService airlineService = new AirlineService();
    private final AircraftService aircraftService = new AircraftService();
    
    // Constants for pagination
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        System.out.println("*******************Reached here********************");
        
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
            	System.out.println("*******************Reached here********************");
                listFlights(request, response);
            } else if (pathInfo.equals("/new")) {
                showNewForm(request, response);
            } else if (pathInfo.equals("/manage")) {
                showEditForm(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            handleError(request, response, "Operation failed", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@" + pathInfo);
        try {
        	if (pathInfo.equals("/saveFlight")) {
                createFlight(request, response);
            } else if (pathInfo.equals("/updateFlight")) {
                updateFlight(request, response);
            } else if (pathInfo.equals("/delete")) {
                deleteFlight(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            handleError(request, response, "Operation failed", e);
        }
    }

    // ===== CRUD Operations =====
    private void listFlights(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int page = getIntParameter(request, "page", DEFAULT_PAGE);
        int size = getIntParameter(request, "size", DEFAULT_PAGE_SIZE);
        
     // Get search/filter parameters
        String searchQuery = request.getParameter("searchQuery");
        String statusFilter = request.getParameter("statusFilter");
        String dateFilter = request.getParameter("dateFilter");
        
        List<Flights> flights = flightService.getPaginatedFlights(page, size, searchQuery, statusFilter, dateFilter);
        int totalFlights = flightService.getFilteredFlightCount(searchQuery, statusFilter, dateFilter);
        int totalPages = (int) Math.ceil((double) totalFlights / size);
        
        request.setAttribute("flights", flights);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", size);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalItems", totalFlights);
        
     // Pass back the filter parameters to maintain them in the view
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("statusFilter", statusFilter);
        request.setAttribute("dateFilter", dateFilter);

        forwardToView(request, response, "/WEB-INF/views/flights/list.jsp");
    }
    
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        request.setAttribute("airports", airportService.fetchAllAirport());
        request.setAttribute("airlines", airlineService.fetchAllAirlines());
        request.setAttribute("aircrafts", aircraftService.fetchAllAircrafts());
        request.setAttribute("flight", new Flights());
        request.setAttribute("formAction", "saveFlight"); // Different action for new flights
        forwardToView(request, response, "/WEB-INF/views/flights/form.jsp");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int flightId = Integer.parseInt(request.getParameter("id"));
        Flights flight = flightService.getFlightById(flightId);
        if (flight == null) {
            throw new Exception("Flight not found with ID: " + flightId);
        }

        request.setAttribute("airlines", airlineService.fetchAllAirlines());
        request.setAttribute("airports", airportService.fetchAllAirport());
        request.setAttribute("aircrafts", aircraftService.fetchAllAircrafts());
        request.setAttribute("flight", flight);
        request.setAttribute("formAction", "updateFlight");
        forwardToView(request, response, "/WEB-INF/views/flights/form.jsp");
    }

    private void createFlight(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Flights flight = mapRequestToFlight(request);
        
        flightService.createFlight(flight);
        redirectWithMessage(request, response, "/flights", "Flight created successfully");
    }

    private void updateFlight(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        int flightId = Integer.parseInt(request.getParameter("flightId"));
        
        Flights flight = mapRequestToFlight(request);
        flight.setFlightId(flightId);
        
        flightService.updateFlight(flight);
        redirectWithMessage(request, response, "/flights", "Flight updated successfully");
    }

    private void deleteFlight(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int flightId = Integer.parseInt(request.getParameter("id"));
        
        Flights flight = flightService.getFlightById(flightId);
        if (flight == null) {
            request.getSession().setAttribute("errorMessage", "Flight not found with ID: " + flightId);
            response.sendRedirect(request.getContextPath() + "/flights");
            return;
        }
        flightService.deleteFlight(flightId);
        redirectWithMessage(request, response, "/flights", "Flight deleted successfully");
    }

    // ===== Helper Methods =====
    private Flights mapRequestToFlight(HttpServletRequest request) {
        Flights flight = new Flights();
        flight.setFlightNumber(request.getParameter("flightNumber"));
        String airlineId = request.getParameter("airlineId");
        if (airlineId != null && !airlineId.isEmpty()) {
            Airlines airline = new Airlines();
            airline.setAirlineId(Integer.parseInt(airlineId));
            flight.setAirline(airline); // This prevents the NPE
        } else {
            throw new IllegalArgumentException("Airline is required");
        }
        String aircraftId = request.getParameter("aircraftId");
        if (aircraftId != null && !aircraftId.isEmpty()) {
            Aircrafts aircraft = new Aircrafts();
            aircraft.setAircraftId(Integer.parseInt(aircraftId));
            flight.setAircraft(aircraft);
        } else {
            throw new IllegalArgumentException("Aircraft selection is required");
        }
        
        String depAirportId = request.getParameter("departureAirportId");
        if (depAirportId != null && !depAirportId.isEmpty()) {
            Airports departureAirport = new Airports();
            departureAirport.setAirportId(Integer.parseInt(depAirportId));
            flight.setDepartureAirport(departureAirport);
        }
        
        String destAirportId = request.getParameter("destinationAirportId");
        if (destAirportId != null && !destAirportId.isEmpty()) {
            Airports destinationAirport = new Airports();
            destinationAirport.setAirportId(Integer.parseInt(destAirportId));
            flight.setDestinationAirport(destinationAirport);
        }
        try {
            String departureTimeStr = request.getParameter("departureTime");
            if (departureTimeStr != null) {
                LocalDateTime departureTime = LocalDateTime.parse(departureTimeStr.replace(" ", "T"));
                flight.setDepartureTime(departureTime);
            }
            
            String arrivalTimeStr = request.getParameter("arrivalTime");
            if (arrivalTimeStr != null) {
                LocalDateTime arrivalTime = LocalDateTime.parse(arrivalTimeStr.replace(" ", "T"));
                flight.setArrivalTime(arrivalTime);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }
        flight.setStatus(request.getParameter("status"));
        
        try {
            String seatsStr = request.getParameter("seatsAvailable");
            if (seatsStr != null) {
                flight.setSeatsAvailable(Integer.parseInt(seatsStr));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Seats must be a number", e);
        }
        
        System.out.println("Mapped flight: " + flight);
        
        return flight;
    }

    private int getIntParameter(HttpServletRequest request, String name, int defaultValue) {
        String value = request.getParameter(name);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }

    private void forwardToView(HttpServletRequest request, HttpServletResponse response, String viewPath)
            throws ServletException, IOException {
        request.getRequestDispatcher(viewPath).forward(request, response);
    }

    private void redirectWithMessage(HttpServletRequest request, HttpServletResponse response, 
                                   String location, String message) throws IOException {
    	
    	request.getSession().setAttribute("successMessage", message);
    	request.getSession().setAttribute("messageShown", false);
        response.sendRedirect(request.getContextPath() + location);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, 
                           String message, Exception e) throws ServletException, IOException {
        logger.severe(message + ": " + e.getMessage());
        request.setAttribute("errorMessage", message);
        request.setAttribute("errorDetail", e.getMessage());
        forwardToView(request, response, "/WEB-INF/views/error.jsp");
    }
}