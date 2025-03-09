<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.flightreservation.DTO.FlightResultOneWay"%>
<%@ page import="com.flightreservation.model.Seats"%>
<%@ page import="com.flightreservation.model.Meals"%>
<%@ page import="java.util.List"%>
<%@ page import="com.flightreservation.dao.SeatDAO"%>
<%@ page import="com.flightreservation.dao.MealDAO"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Passenger Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/style/passengerDetails.css" rel="stylesheet">
    <style>
        .progress-bar { height: 20px; }
    </style>
</head>
<body>
    <div class="container">
        <!-- Progress Bar: Step 2 of 3 -->
        <div class="progress mb-4">
            <div class="progress-bar bg-success" role="progressbar" style="width: 66%;" aria-valuenow="66" aria-valuemin="0" aria-valuemax="100">Step 2: Passenger Details</div>
        </div>
        <h2 class="header-title">Passenger Details</h2>
        <%
            FlightResultOneWay flight = (FlightResultOneWay) session.getAttribute("selectedFlight");
            String className = (String) session.getAttribute("selectedClass");
            if (flight == null || className == null) {
        %>
        <div class="alert alert-danger">No flight selected. Please try again.</div>
        <a href="<%=request.getContextPath()%>/SearchFlights" class="btn btn-secondary">Back to Search</a>
        <%
            } else {
                SeatDAO seatDAO = new SeatDAO();
                MealDAO mealDAO = new MealDAO();
                List<Seats> availableSeats = seatDAO.getAvailableSeats(flight.getFlight().getFlightId(), className);
                List<Meals> mealOptions = mealDAO.getAllMeals();
        %>
        <div class="card mb-3">
            <div class="card-body">
                <h5>Flight Summary</h5>
                <p><strong>Flight ID:</strong> <%=flight.getFlight().getFlightId()%></p>
                <p><strong>Route:</strong> <%=flight.getFlight().getDepartureAirport().getAirportCode()%> ➝ <%=flight.getFlight().getDestinationAirport().getAirportCode()%></p>
                <p><strong>Class:</strong> <%=className%></p>
            </div>
        </div>
        <form action="<%=request.getContextPath()%>/savePassengerDetails" method="post">
            <input type="hidden" name="token" value="<%=session.getAttribute("bookingToken")%>">
            <div class="mb-3">
                <label for="passengerName" class="form-label">Full Name</label>
                <input type="text" class="form-control" id="passengerName" name="passengerName" required>
            </div>
            <div class="mb-3">
                <label for="passengerAge" class="form-label">Age</label>
                <input type="number" class="form-control" id="passengerAge" name="passengerAge" required>
            </div>
            <div class="mb-3">
                <label for="passengerPassport" class="form-label">Passport Number</label>
                <input type="text" class="form-control" id="passengerPassport" name="passengerPassport" required>
            </div>
            <div class="mb-3">
                <label for="seatId" class="form-label">Select Seat</label>
                <select class="form-select" id="seatId" name="seatId" required>
                    <option value="">Choose a seat</option>
                    <% for (Seats seat : availableSeats) { %>
                    <option value="<%=seat.getSeatId()%>"><%=seat.getSeatNumber()%> (Class: <%=seat.getSeatClass().getClassName()%>)</option>
                    <% } %>
                </select>
            </div>
            <div class="mb-3">
                <label for="mealId" class="form-label">Select Meal (Optional)</label>
                <select class="form-select" id="mealId" name="mealId">
                    <option value="">No meal</option>
                    <% for (Meals meal : mealOptions) { %>
                    <option value="<%=meal.getMealsId() %>"><%=meal.getMealName()%> - <%=meal.getDescription()%></option>
                    <% } %>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Next: Payment</button>
        </form>
        <a href="<%=request.getContextPath()%>/flightDetails?token=<%=session.getAttribute("bookingToken")%>" class="btn btn-secondary mt-3">Back to Flight Details</a>
        <%
            }
        %>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>