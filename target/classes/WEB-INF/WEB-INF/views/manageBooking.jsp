<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.flightreservation.model.Bookings" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Booking</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h2>Manage Your Bookings</h2>
        <% 
            com.flightreservation.model.Users loggedInUser = (session != null) ? (com.flightreservation.model.Users) session.getAttribute("loggedInUser") : null;
            if (loggedInUser == null) { 
        %>
            <div class="alert alert-warning">Please log in to manage your bookings.</div>
        <% } else { %>
            <p>Welcome, <%=loggedInUser.getEmail()%>! Here are your bookings:</p>
            <% 
                List<Bookings> bookings = (List<Bookings>) request.getAttribute("bookings");
                String error = (String) request.getAttribute("error");
                if (error != null) { 
            %>
                <div class="alert alert-danger"><%=error%></div>
            <% } else if (bookings == null || bookings.isEmpty()) { %>
                <div class="alert alert-info">No bookings found.</div>
            <% } else { %>
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Booking ID</th>
                            <th>PNR</th>
                            <th>Flight Info</th> <!-- Placeholder until we confirm the field -->
                            <th>Status</th> <!-- Placeholder until we confirm the field -->
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Bookings booking : bookings) { %>
                            <tr>
                                <td><%=booking.getBookingId() %></td>
                                <td><%=booking.getPNR() != null ? booking.getPNR() : "N/A"%></td>
                                <td>N/A (Flight info unavailable)</td> <!-- Replace with actual field -->
                                <td>N/A (Status unavailable)</td> <!-- Replace with actual field -->
                                <td>
                                    <a href="<%=request.getContextPath()%>/view?page=bookingDetails&pnr=<%=booking.getPNR() != null ? booking.getPNR() : ""%>" class="btn btn-info btn-sm">View Details</a>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } %>
            <a href="<%=request.getContextPath()%>/view?page=searchFlights" class="btn btn-primary mt-3">Back to Search</a>
        <% } %>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>