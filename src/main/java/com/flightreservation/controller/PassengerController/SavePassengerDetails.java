package com.flightreservation.controller.PassengerController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/savePassengerDetails")
public class SavePassengerDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(SavePassengerDetails.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		String token = request.getParameter("token");

		if (session == null || token == null || !token.equals(session.getAttribute("bookingToken"))) {
			logger.warn("Invalid or missing booking token");
			response.sendRedirect(request.getContextPath() + "/searchFlights");
			return;
		}

		// Get passenger details
		String passengerName = request.getParameter("passengerName");
		String passengerAgeStr = request.getParameter("passengerAge");
		String passengerPassport = request.getParameter("passengerPassport");
		String seatIdStr = request.getParameter("seatId");
		String mealIdStr = request.getParameter("mealId");

		// Validate inputs
		if (passengerName == null || passengerAgeStr == null || passengerPassport == null || seatIdStr == null) {
			logger.warn("Missing required passenger details");
			response.sendRedirect(
					request.getContextPath() + "/passengerDetails?token=" + token + "&error=missingDetails");
			return;
		}

		int passengerAge, seatId, mealId = 0;
		try {
			passengerAge = Integer.parseInt(passengerAgeStr);
			seatId = Integer.parseInt(seatIdStr);
			if (mealIdStr != null && !mealIdStr.isEmpty()) {
				mealId = Integer.parseInt(mealIdStr);
			}
		} catch (NumberFormatException e) {
			logger.error("Invalid number format in passenger details", e);
			response.sendRedirect(
					request.getContextPath() + "/passengerDetails?token=" + token + "&error=invalidFormat");
			return;
		}
		// Store in session temporarily
		session.setAttribute("passengerName", passengerName);
		session.setAttribute("passengerAge", passengerAge);
		session.setAttribute("passengerPassport", passengerPassport);
		session.setAttribute("seatId", seatId);
		session.setAttribute("mealId", mealId == 0 ? null : mealId); // Null if no meal
		logger.info("Passenger details saved to session: name={}, age={}, passport={}, seatId={}, mealId={}",
				passengerName, passengerAge, passengerPassport, seatId, mealId);

		// Redirect to payment page
		response.sendRedirect(request.getContextPath() + "/payment?token=" + token);
	}

}
