package com.flightreservation.controller.PaymentController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flightreservation.DTO.FlightResultOneWay;

@WebServlet("/payment")
public class Payment extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(Payment.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String token = request.getParameter("token");

        if (session == null || token == null || !token.equals(session.getAttribute("bookingToken"))) {
            logger.warn("Invalid or missing booking token");
            response.sendRedirect(request.getContextPath() + "/view?page=searchFlights"); // Use ViewServlet
            return;
        }

        FlightResultOneWay flight = (FlightResultOneWay) session.getAttribute("selectedFlight");
        String className = (String) session.getAttribute("selectedClass");
        String passengerName = (String) session.getAttribute("passengerName");
        Integer passengerAge = (Integer) session.getAttribute("passengerAge");
        String passengerPassport = (String) session.getAttribute("passengerPassport");
        Integer seatId = (Integer) session.getAttribute("seatId");
        Integer mealId = (Integer) session.getAttribute("mealId");

        if (flight == null || className == null || passengerName == null || passengerAge == null || passengerPassport == null || seatId == null) {
            logger.warn("Missing flight or passenger details");
            response.sendRedirect(request.getContextPath() + "/view?page=passengerDetails&token=" + token + "&error=missingData");
            return;
        }

        // Get price
        double amount = flight.getPricesAndClasses().stream()
                .filter(pc -> pc.getClassName().toString().equals(className))
                .map(FlightResultOneWay.PriceClass::getDynamicPrice)
                .findFirst()
                .orElse(0.0);
        session.setAttribute("paymentAmount", amount);

        request.getRequestDispatcher("/WEB-INF/views/payment.jsp").forward(request, response);
    }
}