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
import com.flightreservation.dao.SeatDAO;
import com.flightreservation.model.Bookings;
import com.flightreservation.model.Meals;
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

            // Dummy user (replace with real user logic later)
            Users user = hibernateSession.get(Users.class, 1);

            // Create Booking
            Bookings booking = new Bookings();
            booking.setUsers(user); // Matches setUser
            booking.setFlights(selectedFlight.getFlight()); // Matches setFlight
            booking.setSeats(hibernateSession.get(Seats.class, seatId)); // Matches setSeat
            if (mealId != null) {
                booking.setMeals(null); // Matches setMeal
            }
            booking.setPNR(generatePNR()); // Matches setPnr
            booking.setStatus(BookingStatus.CONFIRMED); // Matches setStatus (String, not enum)
            hibernateSession.persist(booking);

            // Update seat availability
            Seats seat = hibernateSession.get(Seats.class, seatId);
            seat.setAvailable(false);
            hibernateSession.merge(seat);

            // Create Passenger
            Passengers passenger = new Passengers();
            passenger.setBookings(booking); // Matches setBooking
            passenger.setPassengerName(passengerName); // Matches setPassengerName
            passenger.setPassangerAge(passengerAge); // Matches setPassengerAge (fixed typo)
            passenger.setPassangerPassport(passengerPassport); // Matches setPassengerPassport (fixed typo)
            
            hibernateSession.persist(passenger);

            // Create Payment
            Payments payment = new Payments();
            payment.setBookings(booking); // Matches setBooking
            payment.setAmount(amount); // Matches setAmount
            payment.setPaymentStatus(PaymentStatus.COMPLETED); // Matches setPaymentStatus (String, not enum)
            payment.setPaymentDateTime(LocalDateTime.now()); // Matches setPaymentDate
            hibernateSession.persist(payment);

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