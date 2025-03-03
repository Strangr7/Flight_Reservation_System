<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.flightreservation.DTO.FlightResultOneWay"%>
<%@ page import="com.flightreservation.DTO.FlightResultRoundTrip"%>
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
<title>Round-Trip Flight Results | <%=request.getAttribute("departureAirportCode")%> ⇄ <%=request.getAttribute("destinationAirportCode")%></title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
<link href="style/flightResultRoundTrip.css" rel="stylesheet">
<style>
    .fare-option p { margin-bottom: 5px; }
    .baggage-info, .seats-info { font-size: 0.9em; color: #666; }
    .flight-details-dropdown { display: none; transition: all 0.3s ease; }
    .flight-details-dropdown.active { display: block; }
    .fare-option { padding: 10px; }
    .section-title { margin-top: 30px; }
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
            <h2 class="header-title gradient-header">Explore Your Round-Trip Flights</h2>
            <p class="header-subtitle">
                From <strong><%=request.getAttribute("departureAirportCode")%></strong>
                to <strong><%=request.getAttribute("destinationAirportCode")%></strong>
                on <strong><%=request.getAttribute("departureDate")%></strong> |
                Return on <strong><%=request.getAttribute("returnDate")%></strong>
            </p>

            <%
            FlightResultRoundTrip flightResults = (FlightResultRoundTrip) request.getAttribute("flightResults");
            if (flightResults == null || flightResults.getOutboundFlights().isEmpty() || flightResults.getReturnFlights().isEmpty()) {
            %>
            <div class="alert alert-warning">No round-trip flights found for your journey. Try adjusting your search!</div>
            <%
            } else {
                SimpleDateFormat timeFormat24 = new SimpleDateFormat("HH:mm");
            %>
            <!-- Outbound Flights -->
            <h3 class="section-title gradient-header">
                Outbound Flights (<%=request.getAttribute("departureAirportCode")%> ➝ <%=request.getAttribute("destinationAirportCode")%>)
            </h3>
            <%
            for (FlightResultOneWay flightResult : flightResults.getOutboundFlights()) {
            %>
            <div class="flight-card card">
                <div class="row align-items-center">
                    <div class="col-md-2 text-center airline-logo">
                        <img src="<%=flightResult.getFlight().getAirline().getLogoUrl()%>"
                             alt="<%=flightResult.getFlight().getAirline().getAirlineName()%>">
                    </div>
                    <div class="col-md-6">
                        <p class="flight-time mb-1">
                            <%=timeFormat24.format(Date.from(flightResult.getFlight().getDepartureTime().atZone(ZoneId.systemDefault()).toInstant()))%>
                            ➝ <%=timeFormat24.format(Date.from(flightResult.getFlight().getArrivalTime().atZone(ZoneId.systemDefault()).toInstant()))%>
                            <span class="stop-info" data-bs-toggle="tooltip"
                                  data-bs-title="Connects at <%=flightResult.getStops().stream()
                                      .map(s -> s != null && s.getStopAirport() != null ? s.getStopAirport().getAirportName() : "Unknown")
                                      .filter(name -> name != null && !name.isEmpty()).collect(Collectors.joining(", "))%>">
                                <%=flightResult.getStopCount() > 0
                                    ? (flightResult.getStopCount() + " stop" + (flightResult.getStopCount() > 1 ? "s" : "") + " at "
                                        + flightResult.getStops().stream().map(s -> s.getStopAirport().getAirportCode())
                                            .collect(Collectors.joining(", ")))
                                    : "Non-stop"%>
                            </span>
                        </p>
                        <p class="flight-details">
                            <%=flightResult.getFlight().getDepartureAirport().getAirportCode()%>
                            (<%=flightResult.getFlight().getDepartureAirport().getAirportName()%>)
                            ➝ <%=flightResult.getFlight().getDestinationAirport().getAirportCode()%>
                            (<%=flightResult.getFlight().getDestinationAirport().getAirportName()%>)
                        </p>
                    </div>
                    <div class="col-md-4 text-end">
                        <p class="price mb-2">
                            CAD <%=String.format("%.2f", flightResult.getPricesAndClasses().get(0).getDynamicPrice())%>
                            <span class="lowest-price">Lowest price</span>
                        </p>
                        <a href="#" class="toggle-details" onclick="toggleDetails(event)"
                           aria-expanded="false" aria-controls="flightDetailsDropdown<%=flightResult.getFlight().getFlightId()%>">
                            <span class="arrow">▼</span>
                        </a>
                    </div>
                </div>
                <!-- Dropdown for fare class details -->
                <div class="flight-details-dropdown" id="flightDetailsDropdown<%=flightResult.getFlight().getFlightId()%>" aria-hidden="true">
                    <div class="details-content">
                        <div class="row">
                            <%
                            List<FlightResultOneWay.PriceClass> prices = flightResult.getPricesAndClasses();
                            if (prices.isEmpty()) {
                            %>
                            <div class="col-12"><p>No fare options available</p></div>
                            <%
                            } else {
                                for (int i = 0; i < prices.size(); i++) {
                                    FlightResultOneWay.PriceClass pc = prices.get(i);
                            %>
                            <div class="col-md-4 fare-option">
                                <p><strong><%=pc.getClassName()%></strong></p>
                                <p>from CAD <%=String.format("%.2f", pc.getDynamicPrice())%>
                                    <% if (i == 0) { %><span class="lowest-price">Lowest price</span><% } %>
                                </p>
                                <p class="baggage-info">
                                    <% if (pc.getBaggageRules() != null) { %>
                                        <%=pc.getBaggageRules().getAllowedCheckedBags()%> checked bags,
                                        <%=pc.getBaggageRules().getCarryOnWeightLimit()%> kg carry-on
                                    <% } else { %>
                                        No baggage info
                                    <% } %>
                                </p>
                                <p class="seats-info">Available seats: <%=pc.getAvailableSeats()%></p>
                                <button class="select-btn"
                                        onclick="selectClass('<%=flightResult.getFlight().getFlightId()%>', '<%=pc.getClassName()%>', 'outbound')">Select</button>
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
            %>

            <!-- Return Flights -->
            <h3 class="section-title">
                Return Flights (<%=request.getAttribute("destinationAirportCode")%> ➝ <%=request.getAttribute("departureAirportCode")%>)
            </h3>
            <%
            for (FlightResultOneWay flightResult : flightResults.getReturnFlights()) {
            %>
            <div class="flight-card card">
                <div class="row align-items-center">
                    <div class="col-md-2 text-center airline-logo">
                        <img src="<%=flightResult.getFlight().getAirline().getLogoUrl()%>"
                             alt="<%=flightResult.getFlight().getAirline().getAirlineName()%>">
                    </div>
                    <div class="col-md-6">
                        <p class="flight-time mb-1">
                            <%=timeFormat24.format(Date.from(flightResult.getFlight().getDepartureTime().atZone(ZoneId.systemDefault()).toInstant()))%>
                            ➝ <%=timeFormat24.format(Date.from(flightResult.getFlight().getArrivalTime().atZone(ZoneId.systemDefault()).toInstant()))%>
                            <span class="stop-info" data-bs-toggle="tooltip"
                                  data-bs-title="Connects at <%=flightResult.getStops().stream()
                                      .map(s -> s != null && s.getStopAirport() != null ? s.getStopAirport().getAirportName() : "Unknown")
                                      .filter(name -> name != null && !name.isEmpty()).collect(Collectors.joining(", "))%>">
                                <%=flightResult.getStopCount() > 0
                                    ? (flightResult.getStopCount() + " stop" + (flightResult.getStopCount() > 1 ? "s" : "") + " at "
                                        + flightResult.getStops().stream().map(s -> s.getStopAirport().getAirportCode())
                                            .collect(Collectors.joining(", ")))
                                    : "Non-stop"%>
                            </span>
                        </p>
                        <p class="flight-details">
                            <%=flightResult.getFlight().getDepartureAirport().getAirportCode()%>
                            (<%=flightResult.getFlight().getDepartureAirport().getAirportName()%>)
                            ➝ <%=flightResult.getFlight().getDestinationAirport().getAirportCode()%>
                            (<%=flightResult.getFlight().getDestinationAirport().getAirportName()%>)
                        </p>
                    </div>
                    <div class="col-md-4 text-end">
                        <p class="price mb-2">
                            CAD <%=String.format("%.2f", flightResult.getPricesAndClasses().get(0).getDynamicPrice())%>
                            <span class="lowest-price">Lowest price</span>
                        </p>
                        <a href="#" class="toggle-details" onclick="toggleDetails(event)"
                           aria-expanded="false" aria-controls="flightDetailsDropdown<%=flightResult.getFlight().getFlightId()%>">
                            <span class="arrow">▼</span>
                        </a>
                    </div>
                </div>
                <!-- Dropdown for fare class details -->
                <div class="flight-details-dropdown" id="flightDetailsDropdown<%=flightResult.getFlight().getFlightId()%>" aria-hidden="true">
                    <div class="details-content">
                        <div class="row">
                            <%
                            List<FlightResultOneWay.PriceClass> prices = flightResult.getPricesAndClasses();
                            if (prices.isEmpty()) {
                            %>
                            <div class="col-12"><p>No fare options available</p></div>
                            <%
                            } else {
                                for (int i = 0; i < prices.size(); i++) {
                                    FlightResultOneWay.PriceClass pc = prices.get(i);
                            %>
                            <div class="col-md-4 fare-option">
                                <p><strong><%=pc.getClassName()%></strong></p>
                                <p>from CAD <%=String.format("%.2f", pc.getDynamicPrice())%>
                                    <% if (i == 0) { %><span class="lowest-price">Lowest price</span><% } %>
                                </p>
                                <p class="baggage-info">
                                    <% if (pc.getBaggageRules() != null) { %>
                                        <%=pc.getBaggageRules().getAllowedCheckedBags()%> checked bags,
                                        <%=pc.getBaggageRules().getCarryOnWeightLimit()%> kg carry-on
                                    <% } else { %>
                                        No baggage info
                                    <% } %>
                                </p>
                                <p class="seats-info">Available seats: <%=pc.getAvailableSeats()%></p>
                                <button class="select-btn"
                                        onclick="selectClass('<%=flightResult.getFlight().getFlightId()%>', '<%=pc.getClassName()%>', 'return')">Select</button>
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
            %>
            <%
            }
            %>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>window.contextPath = '<%=request.getContextPath()%>';</script>
    <script>
        window.onload = function() {
            document.getElementById('loading').style.display = 'block';
            setTimeout(() => {
                document.getElementById('loading').style.display = 'none';
                document.getElementById('flightContainer').style.display = 'block';
            }, 2000);
        };

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

        function selectClass(flightId, className, leg) {
            alert(`You selected ${className} for Flight ID ${flightId} (${leg}). Redirecting to booking...`);
            window.location.href = `${window.contextPath}/booking?flightId=${flightId}&class=${className}&leg=${leg}`;
        }

        document.addEventListener('DOMContentLoaded', function() {
            var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
            var tooltipList = tooltipTriggerList.map(function(tooltipTriggerEl) {
                return new bootstrap.Tooltip(tooltipTriggerEl);
            });
        });
    </script>
</body>
</html>