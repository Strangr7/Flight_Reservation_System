<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Find Your Flight - Flight Reservation</title>

<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<!-- Google Fonts -->
<link
	href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap"
	rel="stylesheet">
<!-- Flatpickr CSS -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
<!-- Font Awesome -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<!-- Custom CSS -->
<link href="<%=request.getContextPath()%>/style/searchFlights.css"
	rel="stylesheet">
</head>
<body>
	<div class="container">
		<div class="search-container">
			<div class="search-bg"></div>
			<div class="search-content">
				<div class="search-header">
					<h1>Book Your Flight</h1>
					<p>Experience world-class service and comfort</p>
				</div>

				<div class="search-card">
					<form id="flightSearchForm" method="get" novalidate>
						<div class="search-options">
							<div class="option-group">
								<label for="tripType" class="visually-hidden">Trip Type</label>
								<select class="form-select" id="tripType" name="tripType"
									aria-label="Trip Type">
									<option value="oneWay">One-way</option>
									<option value="roundTrip">Round-trip</option>
								</select>
							</div>
						</div>

						<div class="form-row">
							<div class="col">
								<div class="form-group">
									<label for="departureAirportCode" class="form-label">From</label>
									<input type="text" class="form-control"
										id="departureAirportCode" name="departureAirportCode"
										placeholder="City or Airport" required
										aria-describedby="departureError" autocomplete="off">
									<i class="fas fa-plane-departure input-icon"></i>
									<div id="departureAutocomplete" class="autocomplete-container"></div>
									<span id="departureError" class="error-text" aria-live="polite"></span>
								</div>
							</div>
							<div class="col">
								<div class="form-group">
									<label for="destinationAirportCode" class="form-label">To</label>
									<input type="text" class="form-control"
										id="destinationAirportCode" name="destinationAirportCode"
										placeholder="City or Airport" required
										aria-describedby="destinationError" autocomplete="off">
									<i class="fas fa-plane-arrival input-icon"></i>
									<div id="destinationAutocomplete"
										class="autocomplete-container"></div>
									<span id="destinationError" class="error-text"
										aria-live="polite"></span>
								</div>
							</div>
							<div class="col">
								<div class="form-group">
									<label for="departureDate" class="form-label">Departure</label>
									<input type="text" class="form-control flatpickr"
										id="departureDate" name="departureDate" required
										aria-label="Departure Date"> <i
										class="far fa-calendar-alt input-icon"></i>
								</div>
							</div>
							<div class="col return-date">
								<div class="form-group">
									<label for="returnDate" class="form-label">Return</label> <input
										type="text" class="form-control flatpickr" id="returnDate"
										name="returnDate" aria-label="Return Date"> <i
										class="far fa-calendar-alt input-icon"></i>
								</div>
							</div>
							<div class="col search-btn-container">
								<button type="submit" class="search-btn"
									aria-label="Search flights">
									<i class="fas fa-search me-2"></i> Find Flights
								</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<!-- Bootstrap JS -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
	<!-- Flatpickr JS -->
	<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
	<!-- jQuery for AJAX -->
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<!-- Pass contextPath to JS -->
	<script>
       <%--  window.contextPath = '<%=request.getContextPath()%>';  --%>// Make contextPath available globally
       window.contextPath = '/flight-reservation-system'
    </script>
	<!-- Custom JS -->
	<script src="<%=request.getContextPath()%>/script/searchFlights.js"></script>
</body>
</html>