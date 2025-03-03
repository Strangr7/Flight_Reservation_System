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

@WebServlet("/PassengerDetails")
public class PassengerDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(PassengerDetails.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		String token = request.getParameter("token");

		if (session == null || token == null || !token.equals(session.getAttribute("bookingToken"))) {
			logger.warn("Invalid or missing booking token");
			response.sendRedirect(request.getContextPath() + "/SearchFlights");
			return;
		}

		if (session.getAttribute("selectedFlight") == null || session.getAttribute("selectedClass") == null) {
			logger.warn("No flight selected");
			response.sendRedirect(request.getContextPath() + "/SearchFlights");
			return;
		}

		response.sendRedirect(request.getContextPath() + "/PassengerDetailsPage?token=" + token);
	}

}
