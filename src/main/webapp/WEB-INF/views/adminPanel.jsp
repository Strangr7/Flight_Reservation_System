<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.google.gson.Gson"%>
<%@page import="com.flightreservation.service.DashboardService"%>
<!DOCTYPE html>
<html>
<head>
<title>Admin Panel</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
	<link href="<%=request.getContextPath()%>/style/base.css"
	rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/components/form.css"
	rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/components/buttons.css"
	rel="stylesheet">
</head>
<body>
	<jsp:include page="navbar.jsp" />
	<div class="d-flex flex-grow">
		<jsp:include page="sideNav.jsp" />
		<div class="main-content container flex-grow-1">
			<!-- Header -->
			<div class="d-flex justify-content-between align-items-center mb-4">
				<h2>
					<i class="bi bi-speedometer2 me-2"></i>Dashboard Overview
				</h2>
				<div>
					<span class="badge bg-primary"> <i
						class="bi bi-calendar me-1"></i> <%= java.time.LocalDate.now() %>
					</span>
				</div>
			</div>

			<%
			// Initialize service and get real metrics
				DashboardService dashboardService = new DashboardService();
				Map<String, Object> metrics = dashboardService.getDashboardMetrics();
				request.setAttribute("metrics", metrics);
			%>

			<div class="row mb-4">
				<!-- 1. Today's Bookings -->
				<div class="col-md-4 col-lg-3 mb-3">
					<div
						class="card card-hover border-start border-primary border-4 h-100 min-h-300">
						<div class="card-body">
							<div class="d-flex justify-content-between">
								<div>
									<h6 class="text-muted">TODAY'S BOOKINGS</h6>
									<h2>${metrics.todayBookings}</h2>
									<div class="d-flex align-items-center">
										<span
											class="badge bg-${metrics.bookingChange >= 0 ? 'success' : 'danger'}">
											<i
											class="bi bi-arrow-${metrics.bookingChange >= 0 ? 'up' : 'down'}"></i>
											${empty metrics.bookingChange ? 'N/A' : Math.abs(metrics.bookingChange)}%
										</span> <small class="text-muted ms-2">vs yesterday</small>
									</div>
								</div>
								<i class="bi bi-ticket-perforated fs-1 text-primary opacity-25"></i>
							</div>
						</div>
					</div>
				</div>

				<!-- 2. Active Flights -->
				<div class="col-md-4 col-lg-3 mb-3">
					<div
						class="card card-hover border-start border-success border-4 h-100 min-h-300">
						<div class="card-body">
							<div class="d-flex justify-content-between">
								<div>
									<h6 class="text-muted">ACTIVE FLIGHTS</h6>
									<h2>${metrics.activeFlights}</h2>
									<div class="progress" style="height: 6px;">
										<div class="progress-bar bg-success"
											style="width: ${metrics.onTimePercentage}%"></div>
									</div>
									<small class="text-muted">${metrics.onTimePercentage}%
										on-time</small> <small class="text-success">3 departing soon</small>
								</div>
								<i class="bi bi-airplane fs-1 text-success opacity-25"></i>
							</div>
						</div>
					</div>
				</div>

				<!-- 3. Occupancy Rate (Gauge Chart) -->
				<div class="col-md-4 col-lg-3 mb-3">
					<div
						class="card card-hover border-start border-warning border-4 h-100 min-h-300">
						<div class="card-body">
							<h6 class="text-muted">OCCUPANCY RATE</h6>
							<div id="occupancyGauge" style="height: 80px;"></div>
							<small class="text-muted">
								${metrics.bookedSeats}/${metrics.totalSeats} seats </small>
						</div>
					</div>
				</div>

				<!-- 4. Revenue -->
				<div class="col-md-6 col-lg-3 mb-3">
					<div
						class="card card-hover border-start border-info border-4 h-100 min-h-300">
						<div class="card-body">
							<div class="d-flex justify-content-between">
								<div>
									<h6 class="text-muted">TODAY'S REVENUE</h6>
									<h2>
										$${metrics.revenue}
										<%-- <fmt:formatNumber value="${metrics.revenue}" 
                                            type="currency" 
                                            currencySymbol="$"/> --%>
									</h2>
									<%-- <span class="badge bg-${metrics.revenueChange >= 0 ? 'success' : 'danger'}">
                            <i class="bi bi-arrow-${metrics.revenueChange >= 0 ? 'up' : 'down'}"></i> 
                            ${Math.abs(metrics.revenueChange)}%
                        </span> --%>

								</div>
								<i class="bi bi-currency-dollar fs-1 text-info opacity-25"></i>
							</div>
						</div>
					</div>
				</div>
			</div>

			<!-- Charts and Quick Actions -->
			<div class="row">
				<!-- Booking Trends Chart -->
				<div class="col-md-8 mb-4">
					<div class="card shadow-sm">
						<div class="card-header bg-white">
							<h5>
								<i class="bi bi-bar-chart-line me-2"></i>Booking Trends (Last 7
								Days)
							</h5>
						</div>
						<div class="card-body">
							<canvas id="bookingChart" height="300"></canvas>
						</div>
					</div>
				</div>
				<!-- Quick Actions -->
				<div class="col-md-4 mb-4">
					<div class="card shadow-sm">
						<div class="card-header bg-white">
							<h5>
								<i class="bi bi-lightning-charge me-2"></i>Quick Actions
							</h5>
						</div>
						<div class="card-body">
							<div class="d-grid gap-2">
								<a href="admin?action=addFlight"
									class="btn btn-outline-primary quick-action-btn"> <i
									class="bi bi-plus-circle me-2"></i> Add New Flight
								</a> <a href="admin?action=manageDiscounts"
									class="btn btn-outline-success quick-action-btn"> <i
									class="bi bi-percent me-2"></i> Create Discount
								</a> <a href="admin?action=generateReport"
									class="btn btn-outline-info quick-action-btn"> <i
									class="bi bi-file-earmark-text me-2"></i> Generate Report
								</a> <a href="admin?action=viewAlerts"
									class="btn btn-outline-warning quick-action-btn"> <i
									class="bi bi-bell me-2"></i> View Alerts ()
								</a>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- Recent Activity & Flight Status -->
			<div class="row">
				<!-- Recent Bookings -->
				<div class="col-md-6 mb-4">
					<div class="card shadow-sm">
						<div
							class="card-header bg-white d-flex justify-content-between align-items-center">
							<h5>
								<i class="bi bi-clock-history me-2"></i>Recent Bookings
							</h5>
							<a href="admin?page=bookings"
								class="btn btn-sm btn-outline-secondary">View All</a>
						</div>
						<div class="card-body p-0">
							<div class="list-group list-group-flush">
								<c:forEach items="${recentBookings}" var="booking">
									<a href="admin?action=viewBooking&id=${booking.id}"
										class="list-group-item list-group-item-action">
										<div class="d-flex justify-content-between">
											<div>
												<strong>#${booking.bookingNumber}</strong>
												<div class="text-muted small">${booking.passengerName}</div>
											</div>
											<div class="text-end">
												<div>$${booking.amount}</div>
												<div
													class="badge bg-${booking.status == 'CONFIRMED' ? 'success' : 'warning'}">
													${booking.status}</div>
											</div>
										</div>
									</a>
								</c:forEach>
							</div>
						</div>
					</div>
				</div>

				<!-- Flight Status -->
				<div class="col-md-6 mb-4">
					<div class="card shadow-sm">
						<div
							class="card-header bg-white d-flex justify-content-between align-items-center">
							<h5>
								<i class="bi bi-airplane me-2"></i>Flight Status
							</h5>
							<a href="admin?page=flights"
								class="btn btn-sm btn-outline-secondary">View All</a>
						</div>
						<div class="card-body p-0">
							<div class="list-group list-group-flush">
								<c:forEach items="${activeFlights}" var="flight">
									<div class="list-group-item">
										<div class="d-flex justify-content-between align-items-center">
											<div>
												<strong>${flight.flightNumber}</strong>
												<div class="small">${flight.departureAirport.code}→
													${flight.arrivalAirport.code}</div>
											</div>
											<div>
												<span
													class="badge bg-${flight.status == 'ON_TIME' ? 'success' : 
	                                                                       flight.status == 'DELAYED' ? 'warning' : 'danger'}">
													${flight.status} </span>
												<div class="text-muted small mt-1">
													Departs:
													<fmt:formatDate value="${flight.departureTime}"
														pattern="HH:mm" />
												</div>
											</div>
										</div>
									</div>
								</c:forEach>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="footer.jsp"></jsp:include>

	<!-- JavaScript Libraries -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

	<!-- Gauge Chart Script -->
	<script src="https://cdn.jsdelivr.net/npm/apexcharts"></script>
	<script>
    // Occupancy Gauge
    const gaugeOptions = {
        series: [${metrics.occupancyRate}],
        chart: {
            type: 'radialBar',
            height: 150,
            sparkline: { enabled: true }
        },
        plotOptions: {
            radialBar: {
                startAngle: -90,
                endAngle: 90,
                hollow: { size: '60%' },
                dataLabels: {
                    name: { show: false },
                    value: { 
                        offsetY: -2,
                        fontSize: '16px',
                        formatter: function(val) { return val + '%'; }
                    }
                },
                track: { background: '#e0e0e0' }
            }
        },
        colors: ['#FFB020'],
        fill: { type: 'gradient' }
    };
    new ApexCharts(document.querySelector("#occupancyGauge"), gaugeOptions).render();
	</script>

	<%
    Gson gson = new Gson();
    String json = gson.toJson(metrics.get("bookingTrends"));
    %>
	<script>
    
	document.addEventListener('DOMContentLoaded', function() {    
	    // const bookingTrends = {counts:[], dates: []}
	    const bookingTrends = JSON.parse('<%= json.replace("'", "\\'") %>');
		console.log("Parsed data:", bookingTrends);
	
	    const chartElement = document.getElementById('bookingChart');
	    const chartContainer = chartElement.parentElement; // Get parent div
	
	    if (!bookingTrends.dates || 
	        !bookingTrends.counts || 
	        bookingTrends.dates.length === 0 || 
	        bookingTrends.counts.length === 0) {	        
	        // Hide canvas and show message
	        chartElement.style.display = 'none';
	        chartContainer.insertAdjacentHTML('beforeend', 
	            '<p class="text-center text-muted my-5">No booking data available</p>');
	    } else {
    	// Initialize chart
	        const ctx = document.getElementById('bookingChart').getContext('2d');
	        new Chart(ctx, {
	            type: 'line',
	            data: {
	                labels: bookingTrends.dates,
	                datasets: [{
	                    label: 'Daily Bookings',
	                    data: bookingTrends.counts,
	                    borderColor: 'rgba(78, 115, 223, 1)',
	                    backgroundColor: 'rgba(78, 115, 223, 0.1)',
	                    borderWidth: 2,
	                    tension: 0.3,
	                    fill: true
	                }]
	            },
	            options: {
	                responsive: true,
	                maintainAspectRatio: false,
	                plugins: {
	                    legend: {
	                        display: false
	                    },
	                    tooltip: {
	                        mode: 'index',
	                        intersect: false
	                    }
	                },
	                scales: {
	                    y: {
	                        beginAtZero: true,
	                        ticks: {
	                            precision: 0
	                        }
	                    }
	                }
	            }
	        });
	    }
	    
	});
	</script>
</body>
</html>
