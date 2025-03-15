<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description"
	content="Book your flight with our easy-to-use flight reservation system">
<meta name="robots" content="index, follow">
<meta name="author" content="Your Company">
<title>Find Your Flight - Flight Reservation</title>

<!-- External CSS Libraries -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

<!-- Custom CSS -->
<link href="<%=request.getContextPath()%>/style/searchFlights.css"
	rel="stylesheet">
</head>
<body>
	<div class="container">
		<div class="search-header">
			<h1>Book Your Flight</h1>
			<p>Experience world-class service and comfort</p>
		</div>
		<div class="search-container">

			<div class="search-bg"></div>
			<div class="search-content">

				<div class="search-card">
					<form id="flightSearchForm" method="get" novalidate>
						<div class="form-row">
							<div class="col">
								<div class="form-group">
									<label for="departureAirportCode" class="form-label">From</label>
									<input type="text" class="form-control"
										id="departureAirportCode" name="departureAirportCode"
										placeholder="City or Airport" required
										aria-describedby="departureError" autocomplete="off">
									<div id="departureAutocomplete" class="autocomplete-container"></div>
									<span id="departureError" class="error-text" aria-live="polite"></span>
								</div>
							</div>
							<div
								class="col-auto d-flex align-items-center justify-content-center">
								<button type="button"
									class="swap-btn btn btn-outline-secondary rounded-circle"
									aria-label="Swap departure and destination"
									title="Swap departure and destination">
									<i class="fas fa-exchange-alt"></i>
								</button>
							</div>
							<div class="col">
								<div class="form-group">
									<label for="destinationAirportCode" class="form-label">To</label>
									<input type="text" class="form-control"
										id="destinationAirportCode" name="destinationAirportCode"
										placeholder="City or Airport" required
										aria-describedby="destinationError" autocomplete="off">
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
							<div class="col">
								<div class="form-group">
									<label for="returnDate" class="form-label">Return</label> <input
										type="text" class="form-control flatpickr" id="returnDate"
										name="returnDate" placeholder="Return date"
										aria-label="Return Date"> <i
										class="far fa-calendar-alt input-icon"></i>
								</div>
							</div>
							<div class="col-auto search-btn-container">
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
		<div>
			<div class="mt-3 d-flex justify-content-end">
				<a class="btn btn-secondary">Check-In</a> <a
					class="btn btn-secondary">Manage Bookings</a>
			</div>
		</div>
	</div>

	<!-- External JS Libraries -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

	<!-- Pass contextPath to JS -->
	<script>
    window.contextPath = '<%=request.getContextPath()%>';
</script>
	<!-- Custom JS for flight search functionality -->
	<script src="<%=request.getContextPath()%>/script/searchFlights.js"></script>
</body>
</html>