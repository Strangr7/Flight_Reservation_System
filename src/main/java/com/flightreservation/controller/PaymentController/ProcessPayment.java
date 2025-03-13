package com.flightreservation.controller.PaymentController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flightreservation.DTO.FlightResultOneWay;

import com.flightreservation.model.Bookings;

import com.flightreservation.model.Passengers;
import com.flightreservation.model.Payments;
import com.flightreservation.model.Seats;
import com.flightreservation.model.Users;
import com.flightreservation.model.enums.BookingStatus;
import com.flightreservation.model.enums.PaymentStatus;

import com.flightreservation.util.HibernateUtil;

@WebServlet("/processPayment")
public class ProcessPayment extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ProcessPayment.class);

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

		Users loggedInUser = (Users) session.getAttribute("loggedInUser");

		// Simulate payment processing
		boolean paymentSuccess = true; // Replace with real payment gateway logic

		if (!paymentSuccess) {
			logger.info("Payment failed for token={}", token);
			response.sendRedirect(request.getContextPath() + "/payment?token=" + token + "&error=paymentFailed");
			return;
		}

		// Payment succeeded, save data
		try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
			Transaction tx = hibernateSession.beginTransaction();

			// Get data from session
			FlightResultOneWay selectedFlight = (FlightResultOneWay) session.getAttribute("selectedFlight");
			String passengerName = (String) session.getAttribute("passengerName");
			Integer passengerAge = (Integer) session.getAttribute("passengerAge");
			String passengerPassport = (String) session.getAttribute("passengerPassport");
			Integer seatId = (Integer) session.getAttribute("seatId");
			Integer mealId = (Integer) session.getAttribute("mealId");
			Double amount = (Double) session.getAttribute("paymentAmount");

			// Insert booking via native SQL
			logger.info("Inserting booking via native SQL...");
			String pnr = generatePNR();
			String bookingSql = "INSERT INTO bookings (user_id, flight_id, seat_id, meal_id, pnr, status) "
					+ "VALUES (:userId, :flightId, :seatId, :mealId, :pnr, :status)";
			hibernateSession.createNativeQuery(bookingSql, Void.class).setParameter("userId", loggedInUser.getUserId())
					.setParameter("flightId", selectedFlight.getFlight().getFlightId()).setParameter("seatId", seatId)
					.setParameter("mealId", mealId).setParameter("pnr", pnr)
					.setParameter("status", BookingStatus.CONFIRMED.name()).executeUpdate();

			// Fetch the inserted booking
			logger.info("Fetching booking with PNR: {}", pnr);
			Bookings booking = hibernateSession.createQuery("FROM Bookings WHERE PNR = :pnr", Bookings.class)
					.setParameter("pnr", pnr).uniqueResult();
			if (booking == null) {
				logger.error("No booking found for PNR: {}", pnr);
				throw new RuntimeException("Booking not found");
			}
			logger.info("Booking inserted: bookingId={}", booking.getBookingId());

			// Update seat availability
			logger.info("Updating seat availability...");
			Seats seat = hibernateSession.get(Seats.class, seatId);
			seat.setAvailable(false);
			hibernateSession.merge(seat);
			logger.info("Seat updated: seatId={}", seatId);

			// Insert passenger via native SQL
			logger.info("Inserting passenger via native SQL...");
			String passengerSql = "INSERT INTO passengers (booking_id, passenger_name, passenger_age, passenger_passport, flight_id) "
					+ "VALUES (:bookingId, :name, :age, :passport, :flightId)";
			hibernateSession.createNativeQuery(passengerSql, Void.class)
					.setParameter("bookingId", booking.getBookingId()).setParameter("name", passengerName)
					.setParameter("age", passengerAge).setParameter("passport", passengerPassport)
					.setParameter("flightId", selectedFlight.getFlight().getFlightId()).executeUpdate();

			// Fetch the inserted passenger
			Passengers passenger = hibernateSession
					.createQuery("FROM Passengers WHERE bookings = :bookings", Passengers.class)
					.setParameter("bookings", booking).uniqueResult();
			logger.info("Passenger inserted: passengerId={}", passenger.getPassengerId());

			// Insert payment via native SQL
			logger.info("Inserting payment via native SQL...");
			String paymentSql = "INSERT INTO payments (booking_id, amount, payment_status, payment_date) "
					+ "VALUES (:bookingId, :amount, :status, :date)";
			hibernateSession.createNativeQuery(paymentSql, Void.class).setParameter("bookingId", booking.getBookingId())
					.setParameter("amount", amount).setParameter("status", PaymentStatus.COMPLETED.name())
					.setParameter("date", LocalDateTime.now()).executeUpdate();

			// Fetch the inserted payment
			Payments payment = hibernateSession.createQuery("FROM Payments WHERE bookings = :bookings", Payments.class)
					.setParameter("bookings", booking).uniqueResult();
			logger.info("Payment inserted: paymentId={}", payment.getPaymentId());

			tx.commit();
			logger.info("Booking confirmed: bookingId={}, pnr={}", booking.getBookingId(), booking.getPNR());

			// Clear session
			session.removeAttribute("selectedFlight");
			session.removeAttribute("selectedClass");
			session.removeAttribute("passengerName");
			session.removeAttribute("passengerAge");
			session.removeAttribute("passengerPassport");
			session.removeAttribute("seatId");
			session.removeAttribute("mealId");
			session.removeAttribute("paymentAmount");
			session.removeAttribute("bookingToken");
			response.sendRedirect(request.getContextPath() + "/bookingConfirmation?pnr=" + booking.getPNR());

		} catch (Exception e) {
			logger.error("Error processing payment and saving data", e);
			response.sendRedirect(request.getContextPath() + "/payment?token=" + token + "&error=serverError");
		}
	}

	private String generatePNR() {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder(10);
		for (int i = 0; i < 10; i++) {
			sb.append(chars.charAt(rnd.nextInt(chars.length())));
		}
		return sb.toString();
	}
}