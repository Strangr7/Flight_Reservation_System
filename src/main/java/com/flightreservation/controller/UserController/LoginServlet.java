package com.flightreservation.controller.UserController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.hibernate.Session;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flightreservation.model.Users;
import com.flightreservation.util.HibernateUtil;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Users user = session.createQuery("FROM Users WHERE email = :email", Users.class)
                    .setParameter("email", email)
                    .uniqueResult();
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                HttpSession httpSession = request.getSession();
                httpSession.setAttribute("loggedInUser", user);
                httpSession.setAttribute("user", email); // For navbar
                httpSession.setAttribute("role", user.getUserRoles().name()); // For navbar
                
                logger.info("User Logged In: {}", email);

                // Check for pending booking data
                if (httpSession.getAttribute("pendingFlight") != null) {
                    httpSession.setAttribute("selectedFlight", httpSession.getAttribute("pendingFlight"));
                    httpSession.setAttribute("selectedClass", httpSession.getAttribute("pendingSelectedClass"));
                    httpSession.setAttribute("passengerName", httpSession.getAttribute("pendingPassengerName"));
                    httpSession.setAttribute("passengerAge", httpSession.getAttribute("pendingPassengerAge") != null ? Integer.parseInt((String) httpSession.getAttribute("pendingPassengerAge")) : null);
                    httpSession.setAttribute("passengerPassport", httpSession.getAttribute("pendingPassengerPassport"));
                    httpSession.setAttribute("seatId", httpSession.getAttribute("pendingSeatId") != null ? Integer.parseInt((String) httpSession.getAttribute("pendingSeatId")) : null);
                    httpSession.setAttribute("mealId", httpSession.getAttribute("pendingMealId") != null ? Integer.parseInt((String) httpSession.getAttribute("pendingMealId")) : null);
                    httpSession.setAttribute("bookingToken", httpSession.getAttribute("pendingToken"));

                    // Clear pending data
                    httpSession.removeAttribute("pendingFlight");
                    httpSession.removeAttribute("pendingSelectedClass");
                    httpSession.removeAttribute("pendingPassengerName");
                    httpSession.removeAttribute("pendingPassengerAge");
                    httpSession.removeAttribute("pendingPassengerPassport");
                    httpSession.removeAttribute("pendingSeatId");
                    httpSession.removeAttribute("pendingMealId");
                    httpSession.removeAttribute("pendingToken");

                    // Redirect to payment page with token
                    response.sendRedirect(request.getContextPath() + "/payment?token=" + httpSession.getAttribute("bookingToken"));
                } else {
                    response.sendRedirect(request.getContextPath() + "/view?page=searchFlights");
                }
            } else {
                logger.warn("Login failed for email: {}", email);
                request.setAttribute("error", "Invalid email or password.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            logger.error("Error during login", e);
            request.setAttribute("error", "Login failed. Please try again.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/view?page=login");
    }
}