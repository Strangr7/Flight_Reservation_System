<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.flightreservation.DTO.FlightResultOneWay"%>
<%@ page import="java.util.List"%>
<%@ page import="java.time.ZoneId"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.stream.Collectors"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Flight Results | <%=request.getAttribute("departureAirportCode")%>
	➝ <%=request.getAttribute("destinationAirportCode")%></title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap"
	rel="stylesheet">
<link href="style/flightResultsOneWay.css" rel="stylesheet">
<style>
.fare-option p {
	margin-bottom: 5px;
}

.baggage-info, .seats-info {
	font-size: 0.9em;
	color: #666;
}

.flight-details-dropdown {
	display: none;
	transition: all 0.3s ease;
}

.flight-details-dropdown.active {
	display: block;
}

.fare-option {
	padding: 10px;
}
</style>
</head>
<body>
	<div id="loading">
		<div class="spinner-border text-primary" role="status">
			<span class="visually-hidden">Loading...</span>
		</div>
		<p>Loading your flight results...</p>
	</div>
	<div id="flightContainer" style="display: none;">
		<div class="container">
			<h2 class="header-title gradient-header">Explore Your Flights</h2>
			<p class="header-subtitle">
				From <strong><%=request.getAttribute("departureAirportCode")%></strong>
				to <strong><%=request.getAttribute("destinationAirportCode")%></strong>
				on <strong><%=request.getAttribute("departureDate")%></strong>
			</p>


			<%
			List<FlightResultOneWay> flights = (List<FlightResultOneWay>) request.getAttribute("flights");
			SimpleDateFormat timeFormat24 = new SimpleDateFormat("HH:mm");
			if (flights == null || flights.isEmpty()) {
			%>
			<div class="alert alert-warning">No flights found for your
				journey. Try adjusting your search!</div>
			<%
			} else {
			for (FlightResultOneWay flightResult : flights) {
			%>
			<div class="flight-card card">
				<div class="row align-items-center">
					<div class="col-md-2 text-center airline-logo">
						<img src="<%=flightResult.getFlight().getAirline().getLogoUrl()%>"
							alt="<%=flightResult.getFlight().getAirline().getAirlineName()%>">
					</div>
					<div class="col-md-6">
						<p class="flight-time mb-1">
							<%=timeFormat24
		.format(Date.from(flightResult.getFlight().getDepartureTime().atZone(ZoneId.systemDefault()).toInstant()))%>
							➝
							<%=timeFormat24
		.format(Date.from(flightResult.getFlight().getArrivalTime().atZone(ZoneId.systemDefault()).toInstant()))%>
							<span class="stop-info" data-bs-toggle="tooltip"
								data-bs-title="Connects at <%=flightResult.getStops().stream()
		.map(s -> s != null && s.getStopAirport() != null ? s.getStopAirport().getAirportName() : "Unknown")
		.filter(name -> name != null && !name.isEmpty()).collect(Collectors.joining(", "))%>">
								<%=(flightResult.getStopCount() > 0)
		? ("Connects in " + flightResult.getStops().stream()
				.map(s -> s != null && s.getStopAirport() != null ? s.getStopAirport().getAirportCode() : "Unknown")
				.filter(code -> code != null && !code.isEmpty()).collect(Collectors.joining(", ")))
		: "Non-stop"%>
							</span>
						</p>
						<p class="flight-details">
							<%=flightResult.getFlight().getDepartureAirport().getAirportCode()%>
							(<%=flightResult.getFlight().getDepartureAirport().getAirportName()%>)
							➝
							<%=flightResult.getFlight().getDestinationAirport().getAirportCode()%>
							(<%=flightResult.getFlight().getDestinationAirport().getAirportName()%>)
						</p>
					</div>
					<div class="col-md-4 text-end">
						<p class="price mb-2">
							CAD
							<%=String.format("%.2f", flightResult.getPricesAndClasses().get(0).getDynamicPrice())%>
							<span class="lowest-price">Lowest price</span>
						</p>
						<a href="#" class="toggle-details" onclick="toggleDetails(event)"
							aria-expanded="false"
							aria-controls="flightDetailsDropdown<%=flightResult.getFlight().getFlightId()%>">
							<span class="arrow">▼</span>
						</a>
					</div>
				</div>
				<!-- Dropdown for fare class details -->
				<div class="flight-details-dropdown"
					id="flightDetailsDropdown<%=flightResult.getFlight().getFlightId()%>"
					aria-hidden="true">
					<div class="details-content">
						<div class="row">
							<%
							List<FlightResultOneWay.PriceClass> prices = flightResult.getPricesAndClasses();
							if (prices.isEmpty()) {
							%>
							<div class="col-12">
								<p>No fare options available</p>
							</div>
							<%
							} else {
							for (int i = 0; i < prices.size(); i++) {
								FlightResultOneWay.PriceClass pc = prices.get(i);
							%>
							<div class="col-md-4 fare-option">
								<p>
									<strong><%=pc.getClassName()%></strong>
								</p>
								<p>
									from CAD
									<%=String.format("%.2f", pc.getDynamicPrice())%>
									<%
									if (i == 0) {
									%><span class="lowest-price">Lowest price</span>
									<%
									}
									%>
								</p>
								<p class="baggage-info">
									<%
									if (pc.getBaggageRules() != null) {
									%>
									<%=pc.getBaggageRules().getAllowedCheckedBags()%>
									checked bags,
									<%=pc.getBaggageRules().getCarryOnWeightLimit()%>
									kg carry-on
									<%
									} else {
									%>
									No baggage info
									<%
									}
									%>
								</p>
								<p class="seats-info">
									Available seats:
									<%=pc.getAvailableSeats()%></p>
								<button class="select-btn"
									onclick="selectClass('<%=flightResult.getFlight().getFlightId()%>', '<%=pc.getClassName()%>')">Select</button>
							</div>
							<%
							}
							}
							%>
						</div>
					</div>
				</div>
			</div>
			<%
			}
			}
			%>
		</div>
	</div>
	<script>window.contextPath = '<%=request.getContextPath()%>';</script>
	<!-- Bootstrap JS -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
	<!-- Set contextPath for JavaScript -->
	
	<script>
        // Loading state
        window.onload = function() {
            document.getElementById('loading').style.display = 'block';
            setTimeout(() => {
                document.getElementById('loading').style.display = 'none';
                document.getElementById('flightContainer').style.display = 'block';
            }, 2000);
        };

        // Toggle dropdown with animation
        function toggleDetails(event) {
            event.preventDefault();
            const flightCard = event.target.closest('.flight-card');
            const dropdown = flightCard.querySelector('.flight-details-dropdown');
            const isExpanded = dropdown.classList.contains('active');
            
            dropdown.classList.toggle('active');
            event.target.setAttribute('aria-expanded', !isExpanded);
            dropdown.setAttribute('aria-hidden', isExpanded);
            const arrow = event.target.querySelector('.arrow');
            arrow.textContent = isExpanded ? '▼' : '▲';
        }

        // Handle fare class selection
      function selectClass(flightId, className) {
            const form = document.createElement('form');
            form.method = 'post';
            form.action = `/flight-reservation-system/SelectFlight`;
            console.log("Context path: " + window.contextPath);
            
            const flightInput = document.createElement('input');
            flightInput.type = 'hidden';
            flightInput.name = 'flightId';
            flightInput.value = flightId;
            form.appendChild(flightInput);
            
            const classInput = document.createElement('input');
            classInput.type = 'hidden';
            classInput.name = 'class';
            classInput.value = className;
            form.appendChild(classInput);
            
            document.body.appendChild(form);
            form.submit();
        }

        // Initialize Bootstrap tooltips
        document.addEventListener('DOMContentLoaded', function() {
            var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
            var tooltipList = tooltipTriggerList.map(function(tooltipTriggerEl) {
                return new bootstrap.Tooltip(tooltipTriggerEl);
            });
        });
    </script>
</body>
</html>