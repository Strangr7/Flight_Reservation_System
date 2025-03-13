package com.flightreservation.controller.ViewController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/view")
public class ViewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String page = request.getParameter("page");
        String dispatchPath = "/WEB-INF/views/";

        if ("login".equals(page)) {
            dispatchPath += "login.jsp";
        } else if ("register".equals(page)) {
            dispatchPath += "register.jsp";
        } else if ("searchFlights".equals(page)) {
            dispatchPath += "searchFlights.jsp";
        } else if ("passengerDetails".equals(page)) {
            dispatchPath += "passengerDetails.jsp";
        } else if ("bookingConfirmation".equals(page)) {
            dispatchPath += "bookingConfirmation.jsp";
        } else if ("manageBooking".equals(page)) {
            dispatchPath += "manageBooking.jsp";
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found");
            return;
        }

        request.getRequestDispatcher(dispatchPath).forward(request, response);
    }
}