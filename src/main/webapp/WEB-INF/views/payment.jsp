<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.flightreservation.DTO.FlightResultOneWay"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .progress-bar { height: 20px; }
    </style>
</head>
<body>
    <div class="container">
        <!-- Progress Bar: Step 3 of 3 -->
        <div class="progress mb-4">
            <div class="progress-bar bg-success" role="progressbar" style="width: 100%;" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100">Step 3: Payment</div>
        </div>
        <h2 class="header-title">Payment</h2>
        <%
            FlightResultOneWay flight = (FlightResultOneWay) session.getAttribute("selectedFlight");
            String className = (String) session.getAttribute("selectedClass");
            Double amount = (Double) session.getAttribute("paymentAmount");
            if (flight == null || className == null || amount == null) {
        %>
        <div class="alert alert-danger">Payment details unavailable. Please try again.</div>
        <a href="<%=request.getContextPath()%>/searchFlights" class="btn btn-secondary">Back to Search</a>
        <%
            } else {
        %>
        <div class="card mb-3">
            <div class="card-body">
                <h5>Flight Summary</h5>
                <p><strong>Flight ID:</strong> <%=flight.getFlight().getFlightId()%></p>
                <p><strong>Route:</strong> <%=flight.getFlight().getDepartureAirport().getAirportCode()%> ➝ <%=flight.getFlight().getDestinationAirport().getAirportCode()%></p>
                <p><strong>Class:</strong> <%=className%></p>
                <p><strong>Amount:</strong> CAD <%=String.format("%.2f", amount)%></p>
            </div>
        </div>
        <form action="<%=request.getContextPath()%>/processPayment" method="post">
            <input type="hidden" name="token" value="<%=session.getAttribute("bookingToken")%>">
            <div class="mb-3">
                <label for="cardNumber" class="form-label">Card Number</label>
                <input type="text" class="form-control" id="cardNumber" name="cardNumber" required>
            </div>
            <div class="mb-3">
                <label for="expiry" class="form-label">Expiry Date (MM/YY)</label>
                <input type="text" class="form-control" id="expiry" name="expiry" required>
            </div>
            <div class="mb-3">
                <label for="cvv" class="form-label">CVV</label>
                <input type="text" class="form-control" id="cvv" name="cvv" required>
            </div>
            <button type="submit" class="btn btn-primary">Pay Now</button>
        </form>
        <a href="<%=request.getContextPath()%>/passengerDetails?token=<%=session.getAttribute("bookingToken")%>" class="btn btn-secondary mt-3">Back to Passenger Details</a>
        <%
            }
        %>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>