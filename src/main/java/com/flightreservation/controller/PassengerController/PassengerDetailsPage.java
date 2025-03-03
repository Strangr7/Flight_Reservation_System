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

@WebServlet("/PassengerDetailsPage")
public class PassengerDetailsPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(PassengerDetailsPage.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
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

		request.getRequestDispatcher("/WEB-INF/views/passengerDetails.jsp").forward(request, response);
	}

}
