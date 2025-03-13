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

import com.flightreservation.model.Users;

@WebServlet("/savePassengerDetails")
public class SavePassengerDetails extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(SavePassengerDetails.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String token = request.getParameter("token");

        // Check if user is logged in
        Users loggedInUser = (session != null) ? (Users) session.getAttribute("loggedInUser") : null;
        if (loggedInUser == null) {
            logger.warn("User not logged in, saving pending data and forwarding to login page");
            // Store pending data
            session.setAttribute("pendingFlight", session.getAttribute("selectedFlight"));
            session.setAttribute("pendingSelectedClass", session.getAttribute("selectedClass"));
            session.setAttribute("pendingPassengerName", request.getParameter("passengerName"));
            session.setAttribute("pendingPassengerAge", request.getParameter("passengerAge"));
            session.setAttribute("pendingPassengerPassport", request.getParameter("passengerPassport"));
            session.setAttribute("pendingSeatId", request.getParameter("seatId"));
            session.setAttribute("pendingMealId", request.getParameter("mealId"));
            session.setAttribute("pendingToken", token);

            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        // Check session and token validity
        if (session == null || token == null || !token.equals(session.getAttribute("bookingToken"))) {
            logger.warn("Invalid or missing booking token for user: {}", loggedInUser.getEmail());
            response.sendRedirect(request.getContextPath() + "/view?page=searchFlights");
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
            logger.warn("Missing required passenger details for user: {}", loggedInUser.getEmail());
            response.sendRedirect(
                    request.getContextPath() + "/view?page=passengerDetails&token=" + token + "&error=missingDetails");
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
            logger.error("Invalid number format in passenger details for user: {}", loggedInUser.getEmail(), e);
            response.sendRedirect(
                    request.getContextPath() + "/view?page=passengerDetails&token=" + token + "&error=invalidFormat");
            return;
        }

        // Store in session
        session.setAttribute("passengerName", passengerName);
        session.setAttribute("passengerAge", passengerAge);
        session.setAttribute("passengerPassport", passengerPassport);
        session.setAttribute("seatId", seatId);
        session.setAttribute("mealId", mealId == 0 ? null : mealId);

        logger.info("Passenger details saved to session for user {}: name={}, age={}, passport={}, seatId={}, mealId={}",
                loggedInUser.getEmail(), passengerName, passengerAge, passengerPassport, seatId, mealId);

        // Redirect to payment page
        response.sendRedirect(request.getContextPath() + "/payment?token=" + token);
    }
}