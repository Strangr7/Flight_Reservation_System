const SuggestedDestinationsManager = {
	initialize: function() {
		console.log("Initializing SuggestedDestinationsManager");

		this.suggestionDepartureInput = document.getElementById("suggestionDepartureCode");
		this.suggestionDepartureHidden = document.getElementById("suggestionDepartureCodeHidden");
		this.suggestionsContainer = document.getElementById("suggestionsContainer");

		if (!this.suggestionDepartureInput || !this.suggestionDepartureHidden || !this.suggestionsContainer) {
			console.error("Missing required elements:", {
				suggestionDepartureInput: this.suggestionDepartureInput,
				suggestionDepartureHidden: this.suggestionDepartureHidden,
				suggestionsContainer: this.suggestionsContainer
			});
			return;
		}

		window.setupAutocomplete("suggestionDepartureCode", "suggestionDepartureAutocomplete");
		this.suggestionDepartureInput.addEventListener("change", () => this.updateSuggestions());
		this.updateSuggestions();
	},

	getUniqueDestinations: function(destinations) {
		const seen = new Set();
		return destinations.filter(({ destinationCode }) => !seen.has(destinationCode) && seen.add(destinationCode));
	},

	showLoadingState: function() {
		this.suggestionsContainer.innerHTML = `
            <div class="col-12 text-center py-5">
                <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Loading destinations...</span>
                </div>
                <p class="mt-3 text-muted">Loading popular destinations...</p>
            </div>`;
	},

	showError: function(message) {
		this.suggestionsContainer.innerHTML = `
            <div class="col-12 text-center py-5">
                <p class="mt-3 text-danger">${message}</p>
                <button class="btn btn-secondary mt-2" onclick="App.managers.SuggestedDestinations.updateSuggestions()">Retry</button>
            </div>`;
	},

	updateSuggestions: function() {
	    const departureCode = this.suggestionDepartureHidden.getAttribute("data-airport-code");
	    if (!departureCode) {
	        this.showError("No departure airport selected.");
	        return Promise.resolve();
	    }

	    this.showLoadingState();
	    console.info(`Fetching suggestions for departureCode: ${departureCode}`);

	    return Utilities.fetchWithTimeout(`${window.contextPath}/GetSuggestedDestinations?departureAirportCode=${departureCode}`, {
	        method: "GET",
	        headers: { Accept: "application/json" },
	        credentials: "same-origin"
	    }, 10000)
	    .then(response => {
	        if (!response.ok) throw new Error(`HTTP error ${response.status}`);
	        return response.json();
	    })
	    .then(data => {
	        if (!Array.isArray(data) || data.length === 0) {
	            this.showError("No popular destinations available.");
	            return;
	        }

	        const validDestinations = data.filter(d => d.destinationCode && d.destinationName);
	        if (validDestinations.length === 0) {
	            this.showError("No valid destinations found.");
	            return;
	        }

	        const uniqueDestinations = this.getUniqueDestinations(validDestinations);
	        if (uniqueDestinations.length === 0) {
	            this.showError("No unique popular destinations found.");
	            return;
	        }

	        const fragment = document.createDocumentFragment();
	        uniqueDestinations.forEach(({ destinationName = "Unknown", destinationCode = "---", departureDate = "N/A", price = 0.0, airportImage }) => {
	            const card = document.createElement("div");
	            card.className = "col-md-4 col-sm-6 mb-4";
	            card.innerHTML = `
	                <div class="card">
	                    <div class="card-img-wrapper">
	                        <img src="${airportImage || `${window.contextPath}/images/destinations/default-destination.jpg`}" class="card-img-top" alt="${destinationName}">
	                        <div class="card-overlay">
	                            <h5 class="card-title">${destinationName.toUpperCase()}</h5>
	                            <p class="card-text">${departureDate}</p>
	                            <p class="card-text price">Economy from CAD ${price.toFixed(2)}</p>
	                            <button class="book-now-btn quick-search"
	                                data-departure-code="${departureCode}"
	                                data-destination-code="${destinationCode}"
	                                data-departure-date="${departureDate}">
	                                Book Now
	                            </button>
	                        </div>
	                    </div>
	                </div>`;
	            fragment.appendChild(card);
	        });

	        this.suggestionsContainer.innerHTML = "";
	        this.suggestionsContainer.appendChild(fragment);
	        this.attachSelectButtonListeners();
	    })
	    .catch(error => {
	        console.error("Fetch error fetching suggestions:", error);
	        this.showError("Error loading destinations. Please try again later.");
	    });
	},

	attachSelectButtonListeners: function() {
		this.suggestionsContainer.querySelectorAll(".quick-search").forEach(button => {
			button.addEventListener("click", () => {
				const departureCode = button.dataset.departureCode;
				const destinationCode = button.dataset.destinationCode;
				const departureDate = encodeURIComponent(button.dataset.departureDate);
				const url = `${window.contextPath}/SearchFlights?departureAirportCode=${departureCode}&destinationAirportCode=${destinationCode}&departureDate=${departureDate}&returnDate=`;
				console.info("Navigating to:", url);
				window.location.href = url;
			});
		});
	}
};