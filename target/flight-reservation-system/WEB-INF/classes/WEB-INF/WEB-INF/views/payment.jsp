<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment</title>
    <link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h2>Payment Details</h2>
        <% if (request.getParameter("error") != null) { %>
            <div class="alert alert-danger"><%=request.getParameter("error")%></div>
        <% } %>
        <p>Flight: <%=((com.flightreservation.DTO.FlightResultOneWay)session.getAttribute("selectedFlight")).getFlight().getFlightNumber()%></p>
        <p>Class: <%=session.getAttribute("selectedClass")%></p>
        <p>Passenger Name: <%=session.getAttribute("passengerName")%></p>
        <p>Passenger Age: <%=session.getAttribute("passengerAge")%></p>
        <p>Passenger Passport: <%=session.getAttribute("passengerPassport")%></p>
        <p>Seat ID: <%=session.getAttribute("seatId")%></p>
        <p>Meal ID: <%=session.getAttribute("mealId") != null ? session.getAttribute("mealId") : "None"%></p>
        <p>Amount to Pay: $<%=session.getAttribute("paymentAmount")%></p>

        <form action="<%=request.getContextPath()%>/processPayment" method="post">
            <input type="hidden" name="token" value="<%=request.getParameter("token")%>">
            <div class="mb-3">
                <label for="cardNumber" class="form-label">Card Number</label>
                <input type="text" class="form-control" id="cardNumber" name="cardNumber" required>
            </div>
            <div class="mb-3">
                <label for="expiryDate" class="form-label">Expiry Date (MM/YY)</label>
                <input type="text" class="form-control" id="expiryDate" name="expiryDate" required>
            </div>
            <div class="mb-3">
                <label for="cvv" class="form-label">CVV</label>
                <input type="text" class="form-control" id="cvv" name="cvv" required>
            </div>
            <button type="submit" class="btn btn-primary">Pay Now</button>
        </form>
    </div>
    <script src="<%=request.getContextPath()%>/js/bootstrap.bundle.min.js"></script>
</body>
</html>