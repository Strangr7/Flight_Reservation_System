<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Book your flight with our easy-to-use flight reservation system">
<meta name="robots" content="index, follow">
<meta name="author" content="Your Company">
<title>Find Your Flight - Flight Reservation</title>

<!-- External CSS Libraries -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

<link href="<%=request.getContextPath()%>/style/base.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/components/form.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/components/buttons.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/components/search-container.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/components/where-to-next.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/components/flatpickr.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/components/why-us.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/components/never-miss-an-offer.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/components/navbar.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/components/footer.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/utilities.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/responsive.css" rel="stylesheet">
</head>

<body>
	<jsp:include page="navbar.jsp"></jsp:include>
	<div class="container">
		<div class="search-header">
			<h1>Book Your Flight</h1>
			<p>Experience world-class service and comfort</p>
		</div>
		<div class="search-container">
			<div class="search-bg"></div>
			<div class="search-nav-tabs">
				<a href="#" class="search-nav-item active" data-tab="book-flight">
					<i class="fas fa-plane"></i> Book a Flight
				</a>
				<a href="#" class="search-nav-item" data-tab="check-in">
					<i class="far fa-calendar-alt"></i> Check In
				</a>
				<a href="#" class="search-nav-item" data-tab="manage-bookings">
					<i class="fas fa-map-marker-alt"></i> Manage Bookings
				</a>
				<a href="#" class="search-nav-item" data-tab="flight-status">
					<i class="fas fa-info-circle"></i> Flight Status
				</a>
			</div>
			<div class="search-content">
				<div class="search-card tab-content active" id="book-flight">
					<form id="flightSearchForm" method="get" novalidate>
						<div class="form-row">
							<div class="col">
								<div class="form-group">
									<label for="departureAirportCode" class="form-label">From</label>
									<input type="text" class="form-control" id="departureAirportCode" name="departureAirportCode"
										placeholder="City or Airport" required aria-describedby="departureError" autocomplete="off">
									<div id="departureAutocomplete" class="autocomplete-container"></div>
									<span id="departureError" class="error-text" aria-live="polite"></span>
								</div>
							</div>
							<div class="col-auto d-flex align-items-center justify-content-center">
								<button type="button" class="swap-btn btn btn-outline-secondary rounded-circle"
									aria-label="Swap departure and destination" title="Swap departure and destination">
									<i class="fas fa-exchange-alt"></i>
								</button>
							</div>
							<div class="col">
								<div class="form-group">
									<label for="destinationAirportCode" class="form-label">To</label>
									<input type="text" class="form-control" id="destinationAirportCode" name="destinationAirportCode"
										placeholder="City or Airport" required aria-describedby="destinationError" autocomplete="off">
									<div id="destinationAutocomplete" class="autocomplete-container"></div>
									<span id="destinationError" class="error-text" aria-live="polite"></span>
								</div>
							</div>
							<div class="col">
								<div class="form-group">
									<label for="departureDate" class="form-label">Departure</label>
									<input type="text" class="form-control flatpickr" id="departureDate" name="departureDate" required
										aria-label="Departure Date">
									<i class="far fa-calendar-alt input-icon"></i>
								</div>
							</div>
							<div class="col">
								<div class="form-group">
									<label for="returnDate" class="form-label">Return</label>
									<input type="text" class="form-control flatpickr" id="returnDate" name="returnDate"
										placeholder="Return date" aria-label="Return Date">
									<i class="far fa-calendar-alt input-icon"></i>
								</div>
							</div>
							<div class="col-auto search-btn-container">
								<button type="submit" class="search-btn" aria-label="Search flights">
									<i class="fas fa-search me-2"></i> Find Flights
								</button>
							</div>
						</div>
					</form>
				</div>
				<!-- Check In Form -->
				<div class="search-card tab-content" id="check-in">
					<form id="checkInForm" method="post" novalidate>
						<div class="form-row">
							<div class="col">
								<div class="form-group">
									<label for="bookingReference" class="form-label">Booking Reference</label>
									<input type="text" class="form-control" id="bookingReference" name="bookingReference"
										placeholder="Enter your booking reference" required>
								</div>
							</div>
							<div class="col">
								<div class="form-group">
									<label for="lastName" class="form-label">Last Name</label>
									<input type="text" class="form-control" id="lastName" name="lastName"
										placeholder="Enter your last name" required>
								</div>
							</div>
							<div class="col-auto search-btn-container">
								<button type="submit" class="search-btn" aria-label="Check in">
									<i class="fas fa-check me-2"></i> Check In
								</button>
							</div>
						</div>
					</form>
				</div>
				<!-- Flight Status Form -->
				<div class="search-card tab-content" id="flight-status">
					<form id="flightStatusForm" action="<%=request.getContextPath()%>/checkFlightStatus" method="post" novalidate>
						<div class="form-row">
							<div class="col">
								<div class="form-group">
									<label for="pnr" class="form-label">PNR</label>
									<input type="text" class="form-control" id="pnr" name="pnr"
										placeholder="Enter your PNR" required>
								</div>
							</div>
							<div class="col">
								<div class="form-group">
									<label for="passport" class="form-label">Passport Number</label>
									<input type="text" class="form-control" id="passport" name="passport"
										placeholder="Enter your passport number" required>
								</div>
							</div>
							<div class="col-auto search-btn-container">
								<button type="submit" class="search-btn" aria-label="Check status">
									<i class="fas fa-info-circle me-2"></i> Check Status
								</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div></div>

		<div class="where-to-next">
			<h2 class="section-title">Where to next?</h2>
			<div class="row mb-3" id="where-to-next-form-row">
				<div class="col-md-3 col-sm-6">
					<div class="departure-input-container">
						<label for="suggestionDepartureCode" class="form-label">From</label>
						<input type="text" class="form-control" id="suggestionDepartureCode" placeholder="City or Airport"
							aria-describedby="suggestionDepartureError" autocomplete="off" value="Toronto, Pearson International - YYZ">
						<input type="hidden" id="suggestionDepartureCodeHidden" data-airport-code="YYZ" data-selected="true">
						<div id="suggestionDepartureAutocomplete" class="autocomplete-container"></div>
						<span id="suggestionDepartureError" class="error-text" aria-live="polite"></span>
					</div>
				</div>
			</div>
			<div class="row suggestion-row" id="suggestionsContainer">
				<div class="col-12 text-center py-5">
					<div class="spinner-border text-primary" role="status">
						<span class="visually-hidden">Loading destinations...</span>
					</div>
					<p class="mt-3 text-muted">Loading popular destinations...</p>
				</div>
			</div>
		</div>
		<div class="why-us">
			<h2 class="section-title">Why Choose Us?</h2>
			<p class="section-subtitle">Experience the difference with our premium service</p>
			<div class="why-us-cards">
				<div class="why-us-card">
					<div class="why-us-icon">
						<i class="fas fa-dollar-sign"></i>
					</div>
					<h3 class="why-us-title">Best Prices</h3>
					<p class="why-us-text">We guarantee the most competitive prices for your flights.</p>
				</div>
				<div class="why-us-card">
					<div class="why-us-icon">
						<i class="fas fa-headset"></i>
					</div>
					<h3 class="why-us-title">24/7 Support</h3>
					<p class="why-us-text">Our support team is available around the clock for you.</p>
				</div>
				<div class="why-us-card">
					<div class="why-us-icon">
						<i class="fas fa-plane"></i>
					</div>
					<h3 class="why-us-title">Wide Selection</h3>
					<p class="why-us-text">Choose from a vast network of airlines and destinations.</p>
				</div>
			</div>
		</div>
		<div class="never-miss-offer">
			<div class="row align-items-center">
				<!-- Left Column: Background Image -->
				<div class="col-md-6 never-miss-image">
					<!-- Image will be set via CSS -->
				</div>
				<!-- Right Column: Subscription Form -->
				<div class="col-md-6 never-miss-content">
					<h2 class="section-title">Never Miss an Offer</h2>
					<p class="section-subtitle">Subscribe and be the first to receive our exclusive offers.</p>
					<form id="subscriptionForm" action="/subscribe" method="post" novalidate>
						<div class="row mb-3">
							<div class="col-6">
								<div class="input-group">
									<span class="input-group-text"><i class="fas fa-envelope"></i></span>
									<input type="email" class="form-control" id="emailAddress" name="emailAddress"
										placeholder="Email address" required pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$">
								</div>
								<div class="invalid-feedback">Please enter a valid email address.</div>
							</div>
						</div>
						<div class="form-check mb-3">
							<input type="checkbox" class="form-check-input" id="privacyConsent" name="privacyConsent" required>
							<label class="form-check-label" for="privacyConsent">
								I would like to get offers and news from Qatar Airways. I have read and understood the
								<a href="/privacy" class="privacy-link">privacy notice</a>.
							</label>
						</div>
						<button type="submit" class="subscribe-btn w-100" aria-label="Subscribe to offers">Subscribe</button>
					</form>
				</div>
			</div>
		</div>
		
	</div>
	<jsp:include page="footer.jsp"></jsp:include>

	<!-- External JS Libraries -->
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" defer></script>
	<script src="https://cdn.jsdelivr.net/npm/flatpickr" defer></script>

	<!-- Pass contextPath to JS -->
	<script>
        window.contextPath = '<%=request.getContextPath()%>';
	</script>

	<!-- Core scripts -->
	<script src="<%=request.getContextPath()%>/script/core/utilities.js" defer></script>
	<script src="<%=request.getContextPath()%>/script/core/autocomplete.js" defer></script>
	<script src="<%=request.getContextPath()%>/script/flightsearch/datePicker.js" defer></script>
	<script src="<%=request.getContextPath()%>/script/flightsearch/formManager.js" defer></script>
	<script src="<%=request.getContextPath()%>/script/flightsearch/uiUtilities.js" defer></script>
	<script src="<%=request.getContextPath()%>/script/suggestedDestinations.js" defer></script>
	<script src="<%=request.getContextPath()%>/script/flightsearch/searchFlights.js" defer></script>
	<script src="<%=request.getContextPath()%>/script/flightsearch/searchNav.js" defer></script>
</body>
</html>