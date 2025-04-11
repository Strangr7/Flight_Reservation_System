<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.flightreservation.model.Bookings, com.flightreservation.model.Flights" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Check your flight status with our flight reservation system">
    <meta name="robots" content="index, follow">
    <meta name="author" content="Your Company">
    <title>Check Flight Status - Flight Reservation</title>

    <!-- External CSS Libraries -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

    <!-- Custom CSS -->
    <link href="<%=request.getContextPath()%>/style/base.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/style/components/form.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/style/components/buttons.css" rel="stylesheet">

    <link href="<%=request.getContextPath()%>/style/components/navbar.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/style/components/footer.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/style/utilities.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/style/responsive.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="navbar.jsp"></jsp:include>
    <div class="container mt-5">
        
        <div class="search-container">
          
            

            <% 
                String error = (String) request.getAttribute("error");
                if (error != null) { 
            %>
                <div class="alert alert-danger"><%=error%></div>
            <% } %>

            <% 
                Flights flight = (Flights) request.getAttribute("flight");
                Bookings booking = (Bookings) request.getAttribute("booking");
                if (flight != null && booking != null) { 
            %>
                <h3>Flight Status</h3>
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Flight Number</th>
                            <th>PNR</th>
                            <th>Departure Time</th>
                            <th>Arrival Time</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td><%=flight.getFlightNumber() != null ? flight.getFlightNumber() : "N/A"%></td>
                            <td><%=booking.getPNR() != null ? booking.getPNR() : "N/A"%></td>
                            <td><%=flight.getDepartureTime() != null ? flight.getDepartureTime() : "N/A"%></td>
                            <td><%=flight.getArrivalTime() != null ? flight.getArrivalTime() : "N/A"%></td>
                            <td><%=flight.getStatus() != null ? flight.getStatus() : "N/A"%></td>
                        </tr>
                    </tbody>
                </table>
            <% } %>
            <a href="<%=request.getContextPath()%>/view?page=searchFlights" class="btn btn-primary mt-3">Back to Search</a>
        </div>
    </div>
    <jsp:include page="footer.jsp"></jsp:include>

    <!-- External JS Libraries -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" defer></script>
</body>
</html>