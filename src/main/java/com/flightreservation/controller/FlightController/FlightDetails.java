package com.flightreservation.controller.FlightController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/FlightDetails")
public class FlightDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(FlightDetails.class);

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String token = request.getParameter("token");

        if (session == null || token == null || !token.equals(session.getAttribute("bookingToken"))) {
            logger.warn("Invalid or missing booking token");
            response.sendRedirect(request.getContextPath() + "/SearchFlights");
            return;
        }

        if (session.getAttribute("selectedFlight") == null || session.getAttribute("selectedClass") == null) {
            logger.warn("No flight selected in session");
            response.sendRedirect(request.getContextPath() + "/SearchFlights");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/flightDetails.jsp").forward(request, response);
    }
}
