package com.flightreservation.controller.BookingController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/bookingConfirmation")
public class BookingConfirmation extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(BookingConfirmation.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pnr = request.getParameter("pnr");
		if (pnr == null) {
			logger.warn("Missing PNR");
			response.sendRedirect(request.getContextPath() + "/searchFlights");
			return;
		}

		request.setAttribute("pnr", pnr);
		request.getRequestDispatcher("/WEB-INF/views/bookingConfirmation.jsp").forward(request, response);

	}

}
