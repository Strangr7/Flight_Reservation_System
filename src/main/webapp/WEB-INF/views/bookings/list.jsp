<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<layout:layout title="Booking Management" showSideNav="${true}">
    <jsp:attribute name="styles">
        <style>
            .booking-row:hover { background-color: #f8f9fa; }
            .status-confirmed { color: #28a745; }
            .status-cancelled { color: #dc3545; }
            .status-pending { color: #ffc107; }
        </style>
    </jsp:attribute>
    
    <jsp:attribute name="scripts">
        <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Initialize any booking-specific JS
            console.log('Bookings page loaded');
            
            // Example: Add click handler for view details buttons
            document.querySelectorAll('.view-details').forEach(btn => {
                btn.addEventListener('click', function() {
                    const bookingId = this.dataset.bookingId;
                    // Implement modal or redirect logic
                    console.log('View details for booking:', bookingId);
                });
            });
        });
        </script>
    </jsp:attribute>
    
    <jsp:body>
        <div class="container-fluid px-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2><i class="bi bi-ticket-detailed me-2"></i>Booking Management</h2>
                <div>
                    <span class="badge bg-secondary me-2">
                        Total Bookings: ${totalItems}
                    </span>
                    <!-- <a href="export?type=bookings" class="btn btn-outline-success">
                        <i class="bi bi-download"></i> Export
                    </a> -->
                </div>
            </div>

            <div class="card shadow-sm">
                <div class="card-body">
                <!-- Search Form -->
				    <form id="searchForm" action="${pageContext.request.contextPath}/bookings" method="get">
				        <div class="row mb-3">
				            <div class="col-md-3">
				                <input type="text" class="form-control" name="searchQuery" 
				                       placeholder="Search by PNR or ID" value="${param.searchQuery}">
				            </div>
				            <div class="col-md-2">
						        <input type="text" class="form-control" name="flightNumber"
						               placeholder="Flight Number" value="${param.flightNumber}">
						    </div>
				            <div class="col-md-2">
				                <select class="form-select" name="statusFilter">
				                    <option value="">All Statuses</option>
				                    <c:forEach items="${statusOptions}" var="status">
				                        <option value="${status}" 
				                            ${param.statusFilter == status ? 'selected' : ''}>
				                            ${status.displayName}
				                        </option>
				                    </c:forEach>
				                </select>
				            </div>
				            <div class="col-md-3">
				                <input type="date" class="form-control" name="dateFilter" 
				                       value="${param.dateFilter}">
				            </div>
				            <div class="col-md-2">
				                <button type="submit" class="btn btn-primary w-100">
				                    <i class="bi bi-search"></i> Search
				                </button>
				            </div>
				        </div>
				        <input type="hidden" name="page" value="1">
				    </form>

                    <!-- Bookings Table -->
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead class="table-light">
                                <tr>
                                    <th>Booking ID</th>
                                    <th>PNR</th>
                                    <th>Flight</th>
                                    <th>Date</th>
                                    <th>Status</th>
                                    <th class="text-end">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${bookings}" var="booking">
                                    <tr class="booking-row">
                                        <td>${booking.bookingId}</td>
                                        <td>${booking.PNR }</td>
                                        <td>
                                            ${booking.flights.flightNumber}
                                            <div class="text-muted small">
                                                ${booking.flights.departureAirport.airportCode} → 
                                                ${booking.flights.destinationAirport.airportCode}
                                            </div>
                                        </td>
                                        <td>
                                        ${booking.bookingDate}
                                            
                                        </td>
                                        
                                        <td>
                                        <span class="badge bg-${booking.bookingStatus == 'CONFIRMED' ? 'success' : 
                                        						booking.bookingStatus == 'CHECKEDIN' ? 'info' :
                                                                   booking.bookingStatus == 'CANCELLED' ? 'danger' : 'warning'}">
                                                ${booking.bookingStatus.displayName}
                                            </span>
                                            
                                        </td>
                                        <td class="text-end">
                                            <div class="btn-group" role="group">
                                                <button class="btn btn-sm btn-outline-primary view-details"
                                                        data-booking-id="${booking.bookingId}">
                                                    <i class="bi bi-eye"></i> View
                                                </button>
                                                <%-- <a href="bookings/edit?id=${booking.bookingId}" 
                                                   class="btn btn-sm btn-outline-secondary">
                                                    <i class="bi bi-pencil"></i>
                                                </a> --%>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <!-- Pagination -->
                    <nav aria-label="Booking pagination" class="float-end">
                        <ul class="pagination justify-content-center">
                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
							    <a class="page-link" 
							       href="${pageContext.request.contextPath}/bookings?page=${currentPage - 1}&searchQuery=${param.searchQuery}&statusFilter=${param.statusFilter}&dateFilter=${param.dateFilter}&flightNumber=${param.flightNumber}">
							        Previous
							    </a>
							</li>
							
							<c:forEach begin="1" end="${totalPages}" var="i">
							    <li class="page-item ${currentPage == i ? 'active' : ''}">
							        <a class="page-link" 
							           href="${pageContext.request.contextPath}/bookings?page=${i}&searchQuery=${param.searchQuery}&statusFilter=${param.statusFilter}&dateFilter=${param.dateFilter}&flightNumber=${param.flightNumber}">
							            ${i}
							        </a>
							    </li>
							</c:forEach>
							
							<li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
							    <a class="page-link" 
							       href="${pageContext.request.contextPath}/bookings?page=${currentPage + 1}&searchQuery=${param.searchQuery}&statusFilter=${param.statusFilter}&dateFilter=${param.dateFilter}&flightNumber=${param.flightNumber}">
							        Next
							    </a>
							</li>
                        </ul>
                    </nav>
                    
                    
                </div>
            </div>
        </div>
    </jsp:body>
</layout:layout>