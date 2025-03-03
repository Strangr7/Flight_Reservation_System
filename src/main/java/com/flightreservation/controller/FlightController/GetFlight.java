package com.flightreservation.controller.FlightController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightreservation.model.Flights;
import com.flightreservation.service.FlightService;
import com.flightreservation.util.ApiResponse;
import com.flightreservation.util.JsonUtil;

@WebServlet("/GetFlight")
public class GetFlight extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(GetFlight.class);
    private final FlightService flightService = new FlightService();
    private static final ObjectMapper objectMapper = JsonUtil.getObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String flightID = request.getParameter("id");
        response.setContentType("application/json");

        try {
            if (flightID != null) {
                // Fetch a single flight by ID
                int flightId = Integer.parseInt(flightID);
                Flights flight = flightService.fetchFlightById(flightId);

                if (flight != null) {
                    response.getWriter()
                            .write(objectMapper.writeValueAsString(new ApiResponse<>(200, "Flight found", flight)));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter()
                            .write(objectMapper.writeValueAsString(new ApiResponse<>(404, "Flight not found", null)));
                }
            } else {
                // Fetch all flights if no ID is provided
                List<Flights> flights = flightService.fetchAllFlights();
                response.getWriter().write(
                        objectMapper.writeValueAsString(new ApiResponse<>(200, "All flights retrieved", flights)));
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter()
                    .write(objectMapper.writeValueAsString(new ApiResponse<>(400, "Invalid flight ID format", null)));
            logger.error("Invalid flight ID format: {}", e.getMessage(), e);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(
                    objectMapper.writeValueAsString(new ApiResponse<>(500, "An unexpected error occurred", null)));
            logger.error("Error fetching flights: {}", e.getMessage(), e);
        }
    }
}