package com.flightreservation.controller.AirportController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightreservation.model.Airports;
import com.flightreservation.service.AirportService;
import com.flightreservation.util.JsonUtil;



@WebServlet("/GetAirports")
public class GetAirports extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(GetAirports.class);

	private final AirportService airportService = new AirportService();
	 private static final ObjectMapper objectMapper = JsonUtil.getObjectMapper();

	 protected void doGet(HttpServletRequest request, HttpServletResponse response)
		        throws ServletException, IOException {
		    String term = request.getParameter("term");
		    List<Airports> airports;
		    try {
		        if (term != null && !term.isEmpty()) {
		            airports = airportService.searchAirports(term);
		        } else {
		            airports = airportService.fetchAllAirport();
		        }
		        String jsonResponse = objectMapper.writeValueAsString(airports != null ? airports : new ArrayList<>());
		        response.setContentType("application/json");
		        response.setCharacterEncoding("UTF-8");
		        response.getWriter().write(jsonResponse);
		    } catch (Exception e) {
		        logger.error("Error processing airport search: {}", e.getMessage(), e);
		        response.setContentType("application/json");
		        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		        response.getWriter().write("{\"error\": \"An error occurred while fetching airports\"}");
		    }
		}


}
