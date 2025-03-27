const FormManager = {
    initialize: function() {
        console.log("Initializing form validation and submission");

        const form = document.getElementById("flightSearchForm");
        if (!form) {
            console.error("Form with ID 'flightSearchForm' not found.");
            return Promise.resolve();
        }

        form.addEventListener("submit", this.handleSubmit.bind(this));
        return Promise.resolve();
    },

    handleSubmit: function(e) {
        const departureInput = document.getElementById("departureAirportCode");
        const destinationInput = document.getElementById("destinationAirportCode");
        const departureDate = document.getElementById("departureDate").value;
        const returnDate = document.getElementById("returnDate").value;
        const searchBtn = e.target.querySelector(".search-btn");
        const departureError = document.getElementById("departureError");
        const destinationError = document.getElementById("destinationError");
        let dateError = document.getElementById("dateError");

        departureError.textContent = "";
        destinationError.textContent = "";
        if (dateError) dateError.textContent = "";
        let isValid = true;

        if (!departureInput.dataset.selected || !departureInput.dataset.airportCode) {
            departureError.textContent = "Please select a departure airport from the suggestions.";
            departureInput.setAttribute("aria-invalid", "true");
            isValid = false;
        } else {
            departureInput.setAttribute("aria-invalid", "false");
        }

        if (!destinationInput.dataset.selected || !destinationInput.dataset.airportCode) {
            destinationError.textContent = "Please select a destination airport from the suggestions.";
            destinationInput.setAttribute("aria-invalid", "true");
            isValid = false;
        } else {
            destinationInput.setAttribute("aria-invalid", "false");
        }

        if (departureInput.dataset.airportCode && destinationInput.dataset.airportCode &&
            departureInput.dataset.airportCode === destinationInput.dataset.airportCode) {
            destinationError.textContent = "Departure and destination cannot be the same.";
            destinationInput.setAttribute("aria-invalid", "true");
            isValid = false;
        }

        if (!departureDate) {
            dateError = dateError || document.createElement("div");
            dateError.id = "dateError";
            dateError.className = "error-message";
            if (!document.getElementById("dateError")) {
                document.getElementById("departureDate").parentNode.appendChild(dateError);
            }
            dateError.textContent = "Please select a departure date.";
            isValid = false;
        }
        if (returnDate && new Date(returnDate) < new Date(departureDate)) {
            dateError = dateError || document.createElement("div");
            dateError.id = "dateError";
            dateError.className = "error-message";
            if (!document.getElementById("dateError")) {
                document.getElementById("returnDate").parentNode.appendChild(dateError);
            }
            dateError.textContent = "Return date must be after departure date.";
            isValid = false;
        }

        if (!returnDate || returnDate === "") {
            e.target.action = window.contextPath + "/SearchFlights";
        } else {
            e.target.action = window.contextPath + "/SearchRoundTripFlights";
        }

        if (!isValid) {
            e.preventDefault();
            const invalidElement = e.target.querySelector("[aria-invalid='true']");
            if (invalidElement) invalidElement.focus();
            return;
        }

        if (departureInput.dataset.airportCode) {
            departureInput.value = departureInput.dataset.airportCode;
        }
        if (destinationInput.dataset.airportCode) {
            destinationInput.value = destinationInput.dataset.airportCode;
        }

        searchBtn.disabled = true;
        searchBtn.innerHTML = '<i class="fas fa-circle-notch fa-spin me-2"></i> Searching...';
        e.target.classList.add("submitting");

        try {
            const formData = {
                departure: departureInput.dataset.airportCode,
                destination: destinationInput.dataset.airportCode,
                departureDate: departureDate,
                returnDate: returnDate || null,
                tripType: returnDate ? "roundTrip" : "oneWay"
            };
            if (window.dataLayer) {
                window.dataLayer.push({
                    event: 'flightSearch',
                    searchData: formData
                });
            }
        } catch (err) {
            console.error('Analytics error:', err);
        }

        window.addEventListener("unload", () => {
            searchBtn.disabled = false;
            searchBtn.innerHTML = '<i class="fas fa-search me-2"></i> Find Flights';
            e.target.classList.remove("submitting");
        });
    }
};