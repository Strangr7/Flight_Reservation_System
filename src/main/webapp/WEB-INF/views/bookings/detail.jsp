<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<layout:layout title="Booking Details" showSideNav="${true}">
    <jsp:attribute name="styles">
        <style>
            .booking-detail-card {
                margin-bottom: 2rem;
            }
            .passenger-badge {
                font-size: 0.9rem;
                margin-right: 0.5rem;
            }
        </style>
    </jsp:attribute>

    <jsp:body>
        <div class="container-fluid px-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2><i class="bi bi-ticket-detailed me-2"></i>Booking Details</h2>
                <a href="${pageContext.request.contextPath}/bookings" class="btn btn-outline-secondary">
                    <i class="bi bi-arrow-left"></i> Back to List
                </a>
            </div>

            <!-- Booking Summary Card -->
            <div class="card booking-detail-card shadow-sm">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0">Booking #${booking.bookingId}</h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <dl class="row">
                                <dt class="col-sm-4">PNR:</dt>
                                <dd class="col-sm-8">${booking.PNR}</dd>

                                <dt class="col-sm-4">Status:</dt>
                                <dd class="col-sm-8">
                                    <span class="badge bg-${booking.bookingStatus == 'CONFIRMED' ? 'success' : 
                                                      booking.bookingStatus == 'CANCELLED' ? 'danger' : 'warning'}">
                                        ${booking.bookingStatus.displayName}
                                    </span>
                                </dd>

                                <dt class="col-sm-4">Booking Date:</dt>
                                <dd class="col-sm-8">
                                    ${booking.bookingDate}
                                </dd>
                            </dl>
                        </div>
                        <div class="col-md-6">
                            <dl class="row">
                                <dt class="col-sm-4">User:</dt>
                                <dd class="col-sm-8">${booking.users.name}</dd>

                                <dt class="col-sm-4">Email:</dt>
                                <dd class="col-sm-8">${booking.users.email}</dd>

                                <%-- <dt class="col-sm-4">Total Price:</dt>
                                <dd class="col-sm-8">
                                    <fmt:formatNumber value="${booking.totalPrice}" type="currency"/>
                                </dd> --%>
                            </dl>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Flight Information -->
            <div class="card booking-detail-card shadow-sm">
                <div class="card-header bg-info text-white">
                    <h5 class="mb-0">Flight Information</h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <h6>Outbound Flight</h6>
                            <dl class="row">
                                <dt class="col-sm-4">Flight Number:</dt>
                                <dd class="col-sm-8">${booking.flights.flightNumber}</dd>

                                <dt class="col-sm-4">Departure:</dt>
                                <dd class="col-sm-8">
                                    
                                    <fmt:parseDate value="${flight.departureTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime"/>
									<fmt:formatDate value="${parsedDateTime}" pattern="MMM d, yyyy h:mm a"/>
                                    (${booking.flights.departureAirport.airportCode})
                                </dd>

                                <dt class="col-sm-4">Arrival:</dt>
                                <dd class="col-sm-8">
                                    <fmt:parseDate value="${flight.arrivalTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime"/>
									<fmt:formatDate value="${parsedDateTime}" pattern="MMM d, yyyy h:mm a"/>
                                    (${booking.flights.destinationAirport.airportCode})
                                </dd>
                            </dl>
                        </div>
                        
                    </div>
                </div>
            </div>

            <!-- Passengers -->
            <div class="card booking-detail-card shadow-sm">
                <div class="card-header bg-warning text-dark">
                    <h5 class="mb-0">Passengers (${booking.passengers.size()})</h5>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Age</th>
                                    <th>Passport</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${booking.passengers}" var="passenger">
                                <tr>
                                    <td>${passenger.passengerName}</td>
                                    <td>${passenger.passangerAge}</td>
                                    <td>${passenger.passangerPassport}</td>
                                </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            
            <!-- Seats -->
            <div class="card booking-detail-card shadow-sm">
                <div class="card-header bg-secondary bg-gradient text-dark">
                    <h5 class="mb-0">Seats</h5>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Seat Number</th>
                                    <th>Class</th>
                                </tr>
                            </thead>
                            <tbody>
                                
                                <tr>
                                    <td>${booking.seats.seatNumber}</td>
                                    <td>${booking.seats.seatClass.className}</td>
                                </tr>
                                
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            
            <div class="card booking-detail-card shadow-sm">
                <div class="card-header bg-info bg-gradient text-dark">
                    <h5 class="mb-0">Meals</h5>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Meal Name</th>
                                    <th>Description</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>${booking.meals.mealName}</td>
                                    <td>${booking.meals.description}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Payment & Additional Info -->
            <%-- <div class="card booking-detail-card shadow-sm">
                <div class="card-header bg-success text-white">
                    <h5 class="mb-0">Payment Information</h5>
                </div>
                <div class="card-body">
                    <dl class="row">
                        <dt class="col-sm-3">Payment Status:</dt>
                        <dd class="col-sm-9">${booking.paymentStatus}</dd>

                        <dt class="col-sm-3">Payment Method:</dt>
                        <dd class="col-sm-9">${booking.paymentMethod}</dd>

                        <dt class="col-sm-3">Payment Date:</dt>
                        <dd class="col-sm-9">
                            <fmt:formatDate value="${booking.paymentDate}" pattern="MMM d, yyyy h:mm a"/>
                        </dd>

                        <dt class="col-sm-3">Special Requests:</dt>
                        <dd class="col-sm-9">${not empty booking.specialRequests ? booking.specialRequests : 'None'}</dd>
                    </dl>
                </div>
            </div> --%>
        </div>
    </jsp:body>
</layout:layout>