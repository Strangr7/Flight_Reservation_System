package com.flightreservation.controller.FlightController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightreservation.DTO.SuggetedDestination;
import com.flightreservation.service.FlightService;
import com.flightreservation.util.JsonUtil;

@WebServlet("/GetSuggestedDestinations")
public class GetSuggestedDestinations extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(GetSuggestedDestinations.class);
	private final FlightService flightService = new FlightService();
	private static final ObjectMapper objectMapper = JsonUtil.getObjectMapper();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		String departureAirportCode = request.getParameter("departureAirportCode");
		if (departureAirportCode == null || departureAirportCode.trim().isEmpty()) {
			logger.warn("No departureAirportCode provided in request");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			new ObjectMapper().writeValue(response.getWriter(), Collections.emptyList());
			return;
		}

		try {
			List<SuggetedDestination> suggestions = flightService.fetchSuggestedDestinations(departureAirportCode);
			logger.info("Returning {} suggested destinations for departure={}: {}", suggestions.size(),
					departureAirportCode, suggestions);
			objectMapper.writeValue(response.getWriter(), suggestions);

		} catch (Exception e) {
			logger.error("Error fetching suggested destinations for departure={}: {}", departureAirportCode,
					e.getMessage(), e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			objectMapper.writeValue(response.getWriter(), Collections.emptyList());
		}
	}
}
