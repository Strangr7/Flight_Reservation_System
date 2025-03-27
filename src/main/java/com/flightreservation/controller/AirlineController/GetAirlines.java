package com.flightreservation.controller.AirlineController;

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
import com.flightreservation.model.Airlines;
import com.flightreservation.service.AirlineService;
import com.flightreservation.util.JsonUtil;

/**
 * Servlet to handle fetching all airlines.
 * This servlet responds with a JSON list of available airlines.
 * 
 * @author Fenil
 * @version 1.0
 */
@WebServlet("/GetAirlines")
public class GetAirlines extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(GetAirlines.class);
    private static final ObjectMapper objectMapper = JsonUtil.getObjectMapper();
    
    private final AirlineService airlineService;

    /**
     * Default constructor initializing AirlineService.
     */
    public GetAirlines() {
        this.airlineService = new AirlineService();
    }

    /**
     * Handles HTTP GET requests to fetch all airlines.
     * Responds with JSON containing airline data.
     *
     * @param request  the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an input/output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            List<Airlines> airlines = airlineService.fetchAllAirlines();
            String jsonResponse = objectMapper.writeValueAsString(airlines);

            response.getWriter().write(jsonResponse);
            logger.info("Successfully fetched {} airlines", airlines.size());
        } catch (Exception e) {
            logger.error("Error fetching airlines: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Failed to fetch airlines\"}");
        }
    }
}
