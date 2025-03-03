package com.flightreservation.controller.FlightController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flightreservation.DTO.FlightResultOneWay;

@WebServlet("/SelectFlight")
public class SelectFlight extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(SelectFlight.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().write("Test servlet works!");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String flightIdStr = request.getParameter("flightId");
		String className = request.getParameter("class");

		if (flightIdStr == null || className == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing flightId or class");
			return;
		}

		int flightId;
		try {
			flightId = Integer.parseInt(flightIdStr);
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid flightId format");
			logger.error("Invalid flightId: {}", flightIdStr, e);
			return;
		}

		HttpSession session = request.getSession();
		List<FlightResultOneWay> flights = (List<FlightResultOneWay>) session.getAttribute("flights");

		if (flights == null) {
			logger.error("No flights found in session for flightId={}", flightId);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Flight data not available in session");
			return;
		}

		FlightResultOneWay selectedFlight = flights.stream().filter(f -> f.getFlight().getFlightId() == flightId)
				.findFirst().orElse(null);

		if (selectedFlight == null) {
			logger.error("Flight not found in session for flightId={}", flightId);
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Selected flight not found");
			return;
		}

		String bookingToken = UUID.randomUUID().toString();
		session.setAttribute("bookingToken", bookingToken);
		session.setAttribute("selectedFlight", selectedFlight);
		session.setAttribute("selectedClass", className);

		logger.info("Flight selected: flightId={}, class={}", flightId, className);

		response.sendRedirect(request.getContextPath() + "/FlightDetails?token=" + bookingToken);

	}

}
