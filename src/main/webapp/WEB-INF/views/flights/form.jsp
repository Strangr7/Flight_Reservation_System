<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<layout:layout title="Flight Management" showSideNav="${true}">
	<jsp:attribute name="styles">
        <!-- CSS here -->
    </jsp:attribute>
    
    <jsp:attribute name="scripts">
        <!-- JS here -->
    </jsp:attribute>
    
    <jsp:body>
    	<div class="container-fluid px-4">
    <div class="card">
        <div class="card-header">
        
            <h4>
                <c:choose>
                    <c:when test="${empty flight.flightId || flight.flightId == 0}">
			        Add New Flight
			    </c:when>
			    <c:otherwise>
			        Edit Flight ${flight.flightNumber}
			    </c:otherwise>
                </c:choose>
            </h4>
        </div>
        <div class="card-body">
        <!-- Use the formAction attribute we set in the servlet -->
            <form action="${pageContext.request.contextPath}/flights/${formAction}" method="POST">
                <input type="hidden" name="flightId" value="${flight.flightId}">
                
                <div class="row g-3">
                    <!-- Flight Number -->
                    <div class="col-md-6">
                        <label class="form-label">Flight Number*</label>
                        <input type="text" class="form-control" 
                               name="flightNumber" value="${flight.flightNumber}" required
                               pattern="[A-Za-z0-9]{2,10}" 
                               title="2-10 alphanumeric characters">
                    </div>
                    
                    <!-- Airline Selection -->
                    <div class="col-md-6">
                        <label class="form-label">Airline*</label>
                        <select class="form-select" name="airlineId" required>
                            <option value="">Select Airline</option>
                            <c:forEach items="${airlines}" var="airline">
                                <option value="${airline.airlineId}" 
                                    ${flight.airline.airlineId == airline.airlineId ? 'selected' : ''}>
                                    ${airline.airlineName} (${airline.airlineCode})
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <div class="col-md-6">
					    <label class="form-label">Aircraft*</label>
					    <select class="form-select" name="aircraftId" required>
					        <option value="">Select Aircraft</option>
					        <c:forEach items="${aircrafts}" var="aircraft">
					            <option value="${aircraft.aircraftId}" 
					                ${flight.aircraft.aircraftId == aircraft.aircraftId ? 'selected' : ''}>
					                ${aircraft.aircraftModel}
					            </option>
					        </c:forEach>
					    </select>
					</div>
                    
                    <!-- Departure Airport -->
                    <div class="col-md-6">
                        <label class="form-label">Departure Airport*</label>
                        <select class="form-select" name="departureAirportId" required>
                            <option value="">Select Airport</option>
                            <c:forEach items="${airports}" var="airport">
                                <option value="${airport.airportId}"
                                    ${flight.departureAirport.airportId == airport.airportId ? 'selected' : ''}>
                                    ${airport.airportCode} - ${airport.airportName}, ${airport.city}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <!-- Destination Airport -->
                    <div class="col-md-6">
                        <label class="form-label">Destination Airport*</label>
                        <select class="form-select" name="destinationAirportId" required>
                            <option value="">Select Airport</option>
                            <c:forEach items="${airports}" var="airport">
                                <option value="${airport.airportId}"
                                    ${flight.destinationAirport.airportId == airport.airportId ? 'selected' : ''}>
                                    ${airport.airportCode} - ${airport.airportName}, ${airport.city}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <!-- Departure Time -->
                    <div class="col-md-6">
                        <label class="form-label">Departure Date & Time*</label>
                        <input type="datetime-local" class="form-control" 
                               name="departureTime" 
                               value="${flight.departureTime != null ? 
                  flight.departureTime.toString().substring(0, 16) : ''}" 
                               required>
                    </div>
                    
                    <!-- Arrival Time -->
                    <div class="col-md-6">
                        <label class="form-label">Arrival Date & Time*</label>
                        <input type="datetime-local" class="form-control" 
                               name="arrivalTime" 
                               value="${flight.arrivalTime != null ? 
                  flight.arrivalTime.toString().substring(0, 16) : ''}" 
                               required>
                    </div>
                    
                    <!-- Status -->
                    <div class="col-md-6">
                        <label class="form-label">Status*</label>
                        <select class="form-select" name="status" required>
                            <option value="SCHEDULED" ${flight.status == 'SCHEDULED' ? 'selected' : ''}>Scheduled</option>
                            <option value="ON_TIME" ${flight.status == 'ON_TIME' ? 'selected' : ''}>On Time</option>
                            <option value="DELAYED" ${flight.status == 'DELAYED' ? 'selected' : ''}>Delayed</option>
                            <option value="CANCELLED" ${flight.status == 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                            <option value="COMPLETED" ${flight.status == 'COMPLETED' ? 'selected' : ''}>Completed</option>
                        </select>
                    </div>
                    
                    <!-- Seats Available -->
                    <div class="col-md-6">
                        <label class="form-label">Seats Available*</label>
                        <input type="number" class="form-control" 
                               name="seatsAvailable" value="${flight.seatsAvailable}" 
                               min="1" max="1000" required>
                    </div>
                </div>
                
                <!-- Form Actions -->
                <div class="mt-4">
                    <button type="submit" class="btn btn-primary me-2">
                        <i class="bi bi-save"></i> 
                        ${(empty flight.flightId || flight.flightId == 0) ? 'Save' : 'Update'} Flight
                        
                    </button>
                    <a href="${pageContext.request.contextPath}/flights" class="btn btn-outline-secondary">
                        <i class="bi bi-x-lg"></i> Cancel
                    </a>
                    
                    
                </div>
            </form>
        </div>
    </div>
</div>




    	
    </jsp:body>
</layout:layout>

