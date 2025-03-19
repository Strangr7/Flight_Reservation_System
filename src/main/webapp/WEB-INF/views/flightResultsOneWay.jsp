<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.flightreservation.DTO.FlightResultOneWay"%>
<%@ page import="java.util.List"%>
<%@ page import="java.time.ZoneId"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.stream.Collectors"%>
<%@ page import="java.time.Duration"%>
<%@ page import="java.time.Instant"%>
<%@ page import="com.flightreservation.model.Stops"%>
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
			<form action="<%=request.getContextPath()%>/SearchFlights"
				method="get" class="sort-form mb-3">
				<input type="hidden" name="departureAirportCode"
					value="<%=request.getAttribute("departureAirportCode")%>">
				<input type="hidden" name="destinationAirportCode"
					value="<%=request.getAttribute("destinationAirportCode")%>">
				<input type="hidden" name="departureDate"
					value="<%=request.getAttribute("departureDate")%>"> <label
					for="sortBy" class="me-2">Sort by:</label> <select id="sortBy"
					name="sortBy" class="form-select d-inline-block w-auto"
					onchange="this.form.submit()">
					<option value="best"
						<%="best".equals(request.getParameter("sortBy")) ? "selected" : ""%>>Best
						Flight</option>
					<option value="fastest"
						<%="fastest".equals(request.getParameter("sortBy")) ? "selected" : ""%>>Fastest
						Flight</option>
					<option value="cheapest"
						<%="cheapest".equals(request.getParameter("sortBy")) ? "selected" : ""%>>Cheapest
						Flight</option>
				</select>
			</form>
			<p class="header-subtitle">
				From <strong><%=request.getAttribute("departureAirportCode")%></strong>
				to <strong><%=request.getAttribute("destinationAirportCode")%></strong>
				on <strong><%=request.getAttribute("departureDate")%></strong>
			</p>

			<div class="content-wrapper">
				<div class="row flex-nowrap content-with-sidebar">
					<aside class="col-md-3 filters-sidebar" aria-label="Flight Filters">
						<div class="filter-card card">
							<h3 class="filter-title">Filters</h3>
							<div class="filter-group">
								<label for="priceRange" class="form-label">Price Range</label> <input
									type="range" class="form-range" id="priceRange" min="0"
									max="2000" value="2000">
								<p>
									Max: $<span id="priceValue">2000</span>
								</p>
							</div>
							<div class="filter-group">
								<label class="form-label">Stops</label>
								<div class="form-check">
									<input type="checkbox" class="form-check-input" id="nonStop"
										value="nonStop"> <label class="form-check-label"
										for="nonStop">Non-stop</label>
								</div>
								<div class="form-check">
									<input type="checkbox" class="form-check-input" id="oneStop"
										value="oneStop"> <label class="form-check-label"
										for="oneStop">1 Stop</label>
								</div>
							</div>
						</div>
					</aside>

					<main class="col-md-6 results-main" role="main">
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
							if (flightResult.getFlight() == null)
								continue;
							Instant departureInstant = flightResult.getFlight().getDepartureTime() != null
							? flightResult.getFlight().getDepartureTime().atZone(ZoneId.systemDefault()).toInstant()
							: Instant.now();
							Instant arrivalInstant = flightResult.getFlight().getArrivalTime() != null
							? flightResult.getFlight().getArrivalTime().atZone(ZoneId.systemDefault()).toInstant()
							: Instant.now();
							String departureTime = timeFormat24.format(Date.from(departureInstant));
							String arrivalTime = timeFormat24.format(Date.from(arrivalInstant));
							String stops = (flightResult.getStopCount() > 0)
							? ("Connects in " + flightResult.getStops().stream()
									.map(s -> s != null && s.getStopAirport() != null ? s.getStopAirport().getAirportCode() : "Unknown")
									.filter(code -> code != null && !code.isEmpty()).collect(Collectors.joining(", ")))
							: "Non-stop";
						%>
						<div class="flight-card card"
							data-flight-id="<%=flightResult.getFlight().getFlightId()%>"
							data-price="<%=flightResult.getPricesAndClasses() != null && !flightResult.getPricesAndClasses().isEmpty()
		? String.format("%.2f", flightResult.getPricesAndClasses().get(0).getDynamicPrice())
		: "0.00"%>"
							data-stops="<%=stops%>">
							<div class="row align-items-center">
								<div class="col-md-2 text-center airline-logo">
									<img
										src="<%=flightResult.getFlight().getAirline() != null && flightResult.getFlight().getAirline().getLogoUrl() != null
		? flightResult.getFlight().getAirline().getLogoUrl()
		: ""%>"
										alt="<%=flightResult.getFlight().getAirline() != null
		? flightResult.getFlight().getAirline().getAirlineName()
		: "Airline"%>">
								</div>
								<div class="col-md-6">
									<p class="flight-time mb-1">
										<%=departureTime%>
										➝
										<%=arrivalTime%>
										<span class="stop-info" data-bs-toggle="tooltip"
											data-bs-title="Connects at <%=flightResult.getStops().stream()
		.map(s -> s != null && s.getStopAirport() != null ? s.getStopAirport().getAirportName() : "Unknown")
		.filter(name -> name != null && !name.isEmpty()).collect(Collectors.joining(", "))%>">
											<%=stops%>
										</span>
									</p>
									<p class="flight-details">
										<%=flightResult.getFlight().getDepartureAirport() != null
		? flightResult.getFlight().getDepartureAirport().getAirportCode()
		: "N/A"%>
										(<%=flightResult.getFlight().getDepartureAirport() != null
		? flightResult.getFlight().getDepartureAirport().getAirportName()
		: "Unknown"%>) ➝
										<%=flightResult.getFlight().getDestinationAirport() != null
		? flightResult.getFlight().getDestinationAirport().getAirportCode()
		: "N/A"%>
										(<%=flightResult.getFlight().getDestinationAirport() != null
		? flightResult.getFlight().getDestinationAirport().getAirportName()
		: "Unknown"%>)
									</p>
								</div>
								<div class="col-md-4 text-end">
									<p class="price mb-2">
										CAD
										<%=flightResult.getPricesAndClasses() != null && !flightResult.getPricesAndClasses().isEmpty()
		? String.format("%.2f", flightResult.getPricesAndClasses().get(0).getDynamicPrice())
		: "0.00"%>
										<span class="lowest-price">Lowest price</span>
									</p>
									<a href="#" class="toggle-details"
										onclick="toggleDetails(event)" aria-expanded="false"
										aria-controls="flightDetailsDropdown<%=flightResult.getFlight().getFlightId()%>">
										<span class="arrow">▼</span>
									</a>
									<button
										class="view-timeline-btn btn btn-outline-primary btn-sm mt-2"
										onclick="showTimeline('<%=flightResult.getFlight().getFlightId()%>')"
										aria-controls="timelineSidebar">View Timeline</button>
								</div>
							</div>
							<div class="flight-details-dropdown"
								id="flightDetailsDropdown<%=flightResult.getFlight().getFlightId()%>"
								aria-hidden="true">
								<div class="details-content">
									<div class="row">
										<%
										List<FlightResultOneWay.PriceClass> prices = flightResult.getPricesAndClasses();
										if (prices == null || prices.isEmpty()) {
										%>
										<div class="col-12">
											<p>No fare options available</p>
										</div>
										<%
										} else {
										for (int i = 0; i < prices.size(); i++) {
											FlightResultOneWay.PriceClass pc = prices.get(i);
											boolean isSoldOut = pc.getAvailableSeats() == 0;
										%>
										<div
											class="col-md-4 fare-option <%=isSoldOut ? "sold-out" : ""%>">
											<p>
												<strong><%=pc.getClassName() != null ? pc.getClassName() : "Unknown Class"%></strong>
											</p>
											<p>
												from CAD
												<%=String.format("%.2f", pc.getDynamicPrice())%></p>
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
												%>No baggage info<%
												}
												%>
											</p>
											<p class="seats-info">
												Available seats:
												<%=pc.getAvailableSeats()%></p>
											<%
											if (isSoldOut) {
											%>
											<p class="sold-out-message">Sold Out</p>
											<%
											} else {
											%>
											<button class="select-btn"
												onclick="selectClass('<%=flightResult.getFlight().getFlightId()%>', '<%=pc.getClassName()%>')">Select</button>
											<%
											}
											%>
										</div>
										<%
										}
										%>
										<%
										}
										%>
									</div>
								</div>
							</div>
						</div>
						<%
						}
						%>
						<%
						}
						%>
					</main>

					<aside class="col-md-3 timeline-sidebar" id="timelineSidebar"
						aria-label="Flight Timeline">
						<div class="timeline-card card">
							<h3 class="timeline-title">Flight Timeline</h3>
							<div id="timelineContent">
								<%
								if (flights != null && !flights.isEmpty()) {
									for (FlightResultOneWay flight : flights) {
										if (flight.getFlight() == null)
									continue;
										Instant departureInstant = flight.getFlight().getDepartureTime() != null
										? flight.getFlight().getDepartureTime().atZone(ZoneId.systemDefault()).toInstant()
										: Instant.now();
										Instant arrivalInstant = flight.getFlight().getArrivalTime() != null
										? flight.getFlight().getArrivalTime().atZone(ZoneId.systemDefault()).toInstant()
										: Instant.now();
										String departureTime = timeFormat24.format(Date.from(departureInstant));
										String arrivalTime = timeFormat24.format(Date.from(arrivalInstant));
										long totalDurationMinutes = Duration.between(departureInstant, arrivalInstant).toMinutes();
										String totalDuration = String.format("%dh %dm", totalDurationMinutes / 60, totalDurationMinutes % 60);
								%>
								<div class="timeline-wrapper"
									data-flight-id="<%=flight.getFlight().getFlightId()%>"
									style="display: none;">
									<div class="timeline">
										<div class="timeline-item">
											<div class="timeline-dot"></div>
											<div class="timeline-content">
												<p class="timeline-step">
													Depart:
													<%=flight.getFlight().getDepartureAirport() != null
		? flight.getFlight().getDepartureAirport().getAirportCode()
		: "N/A"%></p>
												<p class="timeline-time"><%=departureTime%></p>
												<p class="timeline-details"><%=flight.getFlight().getDepartureAirport() != null
		? flight.getFlight().getDepartureAirport().getAirportName()
		: "Unknown"%></p>
											</div>
										</div>
										<%
										if (flight.getStopCount() > 0) {
											Instant currentTime = departureInstant;
											for (Stops stop : flight.getStops()) {
												if (stop != null && stop.getStopAirport() != null) {
											Instant stopArrival = currentTime.plus(Duration.ofMinutes(stop.getStopDuration()));
											Instant stopDeparture = stopArrival.plus(Duration.ofMinutes(30));
											String stopArrivalTime = timeFormat24.format(Date.from(stopArrival));
											String stopDepartureTime = timeFormat24.format(Date.from(stopDeparture));
											String layoverDuration = String.format("%dm", stop.getStopDuration());
											currentTime = stopDeparture;
										%>
										<div class="timeline-item">
											<div class="timeline-dot stop-dot"></div>
											<div class="timeline-content">
												<p class="timeline-step">
													Stop:
													<%=stop.getStopAirport().getAirportCode()%></p>
												<p class="timeline-time">
													Arrive:
													<%=stopArrivalTime%>
													| Depart:
													<%=stopDepartureTime%></p>
												<p class="timeline-details">
													Layover:
													<%=layoverDuration%></p>
											</div>
										</div>
										<%
										}
										%>
										<%
										}
										%>
										<%
										}
										%>
										<div class="timeline-item">
											<div class="timeline-dot"></div>
											<div class="timeline-content">
												<p class="timeline-step">
													Arrive:
													<%=flight.getFlight().getDestinationAirport() != null
		? flight.getFlight().getDestinationAirport().getAirportCode()
		: "N/A"%></p>
												<p class="timeline-time"><%=arrivalTime%></p>
												<p class="timeline-details"><%=flight.getFlight().getDestinationAirport() != null
		? flight.getFlight().getDestinationAirport().getAirportName()
		: "Unknown"%></p>
											</div>
										</div>
										<div class="timeline-item">
											<div class="timeline-content">
												<p class="timeline-total-duration">
													Total Duration:
													<%=totalDuration%></p>
											</div>
										</div>
									</div>
									<div class="timeline-details-content"
										data-flight-id="<%=flight.getFlight().getFlightId()%>">
										<p>
											<strong>Airline:</strong>
											<%=flight.getFlight().getAirline() != null ? flight.getFlight().getAirline().getAirlineName() : "Unknown"%></p>
										<p>
											<strong>Flight Number:</strong>
											<%=flight.getFlight().getFlightNumber() != null ? flight.getFlight().getFlightNumber() : "N/A"%></p>
										<%
										if (flight.getStopCount() > 0) {
										%>
										<p>
											<strong>Layover Details:</strong> Connects in
											<%=flight.getStops().stream()
		.map(s -> s != null && s.getStopAirport() != null ? s.getStopAirport().getAirportName() : "Unknown")
		.filter(name -> name != null && !name.isEmpty()).collect(Collectors.joining(", "))%></p>
										<%
										}
										%>
									</div>
								</div>
								<%
								}
								%>
								<%
								} else {
								%>
								<p>No flight selected.</p>
								<%
								}
								%>
							</div>
							<div class="timeline-details-dropdown" id="timelineDetails"
								aria-hidden="true">
								<div class="timeline-details-content-wrapper"></div>
							</div>
						</div>
					</aside>
				</div>
			</div>
		</div>
	</div>
	<script>window.contextPath = '<%=request.getContextPath()%>';
	</script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
	<script
		src="<%=request.getContextPath()%>/script/flightResultsOneWay.js"></script>
</body>
</html>