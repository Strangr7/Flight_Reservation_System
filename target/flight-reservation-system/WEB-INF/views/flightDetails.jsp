<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.flightreservation.DTO.FlightResultOneWay"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.time.ZoneId"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Flight Overview</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/style/flightDetails.css" rel="stylesheet">
    <style>
        .progress-bar { height: 20px; }
    </style>
</head>
<body>
    <div class="container">
        <!-- Progress Bar: Step 1 of 3 -->
        <div class="progress mb-4">
            <div class="progress-bar bg-success" role="progressbar" style="width: 33%;" aria-valuenow="33" aria-valuemin="0" aria-valuemax="100">Step 1: Flight Details</div>
        </div>
        <h2 class="header-title">Flight Overview</h2>
        <%
            FlightResultOneWay flight = (FlightResultOneWay) session.getAttribute("selectedFlight");
            String className = (String) session.getAttribute("selectedClass");
            SimpleDateFormat timeFormat24 = new SimpleDateFormat("HH:mm");
            if (flight == null || className == null) {
        %>
        <div class="alert alert-danger">No flight selected. Please try again.</div>
        <a href="<%=request.getContextPath()%>/SearchFlights" class="btn btn-secondary">Back to Search</a>
        <%
            } else {
                FlightResultOneWay.PriceClass selectedClass = flight.getPricesAndClasses().stream()
                    .filter(pc -> pc.getClassName().toString().equals(className)).findFirst().orElse(null);
        %>
        <div class="card">
            <div class="card-body">
                <h5>Flight ID: <%=flight.getFlight().getFlightId()%></h5>
                <p><strong>Airline:</strong> <%=flight.getFlight().getAirline().getAirlineName()%></p>
                <p><strong>Flight Number:</strong> <%=flight.getFlight().getFlightNumber()%></p>
                <p><strong>Route:</strong> <%=flight.getFlight().getDepartureAirport().getAirportCode()%> ➝ <%=flight.getFlight().getDestinationAirport().getAirportCode()%></p>
                <p><strong>Departure:</strong> <%=timeFormat24.format(java.util.Date.from(flight.getFlight().getDepartureTime().atZone(ZoneId.systemDefault()).toInstant()))%></p>
                <p><strong>Arrival:</strong> <%=timeFormat24.format(java.util.Date.from(flight.getFlight().getArrivalTime().atZone(ZoneId.systemDefault()).toInstant()))%></p>
                <p><strong>Trip Duration:</strong> <%=flight.getFlight().getTripDuration()%> minutes</p>
                <p><strong>Stops:</strong> <%=flight.getStopCount() == 0 ? "Non-stop" : flight.getStopCount() + " stop(s)"%></p>
                <p><strong>Class:</strong> <%=className%></p>
                <p><strong>Price:</strong> CAD <%=selectedClass != null ? String.format("%.2f", selectedClass.getDynamicPrice()) : "N/A"%></p>
                <% if (selectedClass != null && selectedClass.getBaggageRules() != null) { %>
                <p><strong>Baggage:</strong> <%=selectedClass.getBaggageRules().getAllowedCheckedBags()%> checked bags, <%=selectedClass.getBaggageRules().getCarryOnWeightLimit()%> kg carry-on</p>
                <% } %>
                <form action="<%=request.getContextPath()%>/PassengerDetails" method="post">
                    <input type="hidden" name="token" value="<%=session.getAttribute("bookingToken")%>">
                    <button type="submit" class="btn btn-primary mt-3">Continue to Passenger Details</button>
                </form>
            </div>
        </div>
        <a href="<%=request.getContextPath()%>/SearchFlights" class="btn btn-secondary mt-3">Back to Search</a>
        <%
            }
        %>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>