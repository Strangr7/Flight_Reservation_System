package com.flightreservation.controller.BookingController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.flightreservation.model.Bookings;
import com.flightreservation.model.Users;
import com.flightreservation.service.BookingService;

@WebServlet("/manageBooking")
public class ManageBooking extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ManageBooking.class);
	private final BookingService bookingService = new BookingService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Users loggedInUser = (session != null) ? (Users) session.getAttribute("loggedInUser") : null;

		if (loggedInUser == null) {
			logger.warn("User not logged in, redirecting to login");
			response.sendRedirect(request.getContextPath() + "/view?page=login");
			return;
		}

		try {
			List<Bookings> bookings = bookingService.getBookingsByUserId(loggedInUser.getUserId());
			request.setAttribute("bookings", bookings);
			request.getRequestDispatcher("/WEB-INF/views/manageBooking.jsp").forward(request, response);
		} catch (Exception e) {
			logger.error("Error fetching bookings for user: {}", loggedInUser.getEmail(), e);
			request.setAttribute("error", "Failed to load bookings. Please try again.");
			request.getRequestDispatcher("/WEB-INF/views/manageBooking.jsp").forward(request, response);
		}
	}
}
