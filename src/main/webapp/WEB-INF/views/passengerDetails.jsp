<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.flightreservation.DTO.FlightResultOneWay"%>
<%@ page import="com.flightreservation.model.Seats"%>
<%@ page import="com.flightreservation.model.Meals"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Passenger Details</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/passengerDetails.css"
	rel="stylesheet">
<style>
.progress-bar {
	height: 20px;
}
</style>
</head>
<body>
	<div class="container">
		<!-- Progress Bar: Step 2 of 3 -->
		<div class="progress mb-4">
			<div class="progress-bar bg-success" role="progressbar"
				style="width: 66%;" aria-valuenow="66" aria-valuemin="0"
				aria-valuemax="100">Step 2: Passenger Details</div>
		</div>
		<h2 class="header-title">Passenger Details</h2>
		<%
		FlightResultOneWay flight = (FlightResultOneWay) session.getAttribute("selectedFlight");
		String className = (String) session.getAttribute("selectedClass");
		if (flight == null || className == null) {
		
		%>
		
	</div>
</body>
</html>