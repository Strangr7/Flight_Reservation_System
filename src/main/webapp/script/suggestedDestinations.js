/**
 * Suggested Destinations Handler (Production-Grade)
 * Manages dynamic updates for the "Where to Next?" section in the flight search page.
 */
(() => {
    "use strict";

    /**
     * Waits for a function to be available before executing a callback.
     * @param {Function} callback - Function to execute once available.
     */
    function waitForSetupAutocomplete(callback) {
        if (typeof window.setupAutocomplete === "function") {
            callback();
        } else {
            console.warn("Waiting for setupAutocomplete...");
            setTimeout(() => waitForSetupAutocomplete(callback), 100);
        }
    }

    document.addEventListener("DOMContentLoaded", () => {
        waitForSetupAutocomplete(() => {
            console.info("setupAutocomplete is defined. Initializing suggestedDestinations.js");

            // Element references
            const suggestionDepartureInput = document.getElementById("suggestionDepartureCode");
            const suggestionDepartureHidden = document.getElementById("suggestionDepartureCodeHidden");
            const suggestionsContainer = document.getElementById("suggestionsContainer");
            const departureAutocomplete = document.getElementById("suggestionDepartureAutocomplete");

            if (!suggestionDepartureInput || !suggestionDepartureHidden || !suggestionsContainer || !departureAutocomplete) {
                console.error("Missing required elements:", {
                    suggestionDepartureInput,
                    suggestionDepartureHidden,
                    suggestionsContainer,
                    departureAutocomplete
                });
                return;
            }

            // Initialize autocomplete for suggestionDepartureCode
            window.setupAutocomplete("suggestionDepartureCode", "suggestionDepartureAutocomplete");

            /**
             * Filters an array of destinations to keep only unique entries based on destinationCode.
             * @param {Array} destinations - Array of destination objects from the API.
             * @returns {Array} - Unique destinations.
             */
            const getUniqueDestinations = (destinations) => {
                const seen = new Set();
                return destinations.filter(({ destinationCode }) => !seen.has(destinationCode) && seen.add(destinationCode));
            };

            /**
             * Renders the loading state inside suggestionsContainer.
             */
            function showLoadingState() {
                suggestionsContainer.innerHTML = `
                    <div class="col-12 text-center py-5">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">Loading destinations...</span>
                        </div>
                        <p class="mt-3 text-muted">Loading popular destinations...</p>
                    </div>`;
            }

            /**
             * Displays an error message in suggestionsContainer.
             * @param {string} message - Error message to display.
             */
            function showError(message) {
                suggestionsContainer.innerHTML = `
                    <div class="col-12 text-center py-5">
                        <p class="mt-3 text-danger">${message}</p>
                        <button class="btn btn-secondary mt-2" onclick="updateSuggestions()">Retry</button>
                    </div>`;
            }

            /**
             * Fetches suggested destinations from the backend and updates the UI.
             */
            function updateSuggestions() {
                const departureCode = suggestionDepartureHidden.getAttribute("data-airport-code");
                if (!departureCode) {
                    showError("No departure airport selected.");
                    return;
                }

                showLoadingState();
                console.info(`Fetching suggestions for departureCode: ${departureCode}`);

                let ajaxComplete = false;
                let timeoutId = setTimeout(() => {
                    if (!ajaxComplete) {
                        console.warn("AJAX request timed out.");
                        showError("Request timed out. Please try again later.");
                    }
                }, 11000);

                $.ajax({
                    url: `${window.contextPath}/GetSuggestedDestinations?departureAirportCode=${departureCode}`,
                    method: "GET",
                    dataType: "json",
                    timeout: 10000,
                    success: (data) => {
                        ajaxComplete = true;
                        clearTimeout(timeoutId);
                        suggestionsContainer.innerHTML = "";

                        if (!Array.isArray(data) || data.length === 0) {
                            showError("No popular destinations available.");
                            return;
                        }

                        const validDestinations = data.filter(destination => 
                            destination.destinationCode && destination.destinationName
                        );
                        if (validDestinations.length === 0) {
                            showError("No valid destinations found.");
                            return;
                        }

                        const uniqueDestinations = getUniqueDestinations(validDestinations);
                        if (uniqueDestinations.length === 0) {
                            showError("No unique popular destinations found.");
                            return;
                        }

                        const fragment = document.createDocumentFragment();
                        uniqueDestinations.forEach(({ destinationName = "Unknown", destinationCode = "---", departureDate = "N/A", price = 0.0, airportImage }) => {
                            const card = document.createElement("div");
                            card.className = "col-md-2 mb-4";
                            card.innerHTML = `
                                <div class="card">
                                    <img src="${airportImage || `${window.contextPath}/images/destinations/default-destination.jpg`}" class="card-img-top" alt="${destinationName}">
                                    <div class="card-body">
                                        <h5 class="card-title">${destinationName} (${destinationCode})</h5>
                                        <p class="card-text">Departure: ${departureDate}</p>
                                        <p class="card-text">Price: CAD ${price.toFixed(2)}</p>
                                        <button class="btn btn-primary quick-search"
                                            data-departure-code="${departureCode}"
                                            data-destination-code="${destinationCode}"
                                            data-departure-date="${departureDate}">
                                            Select
                                        </button>
                                    </div>
                                </div>`;
                            fragment.appendChild(card);
                        });

                        suggestionsContainer.appendChild(fragment);
                        attachSelectButtonListeners();
                    },
                    error: (jqXHR, textStatus, errorThrown) => {
                        ajaxComplete = true;
                        clearTimeout(timeoutId);
                        console.error("AJAX error fetching suggestions:", { textStatus, errorThrown, response: jqXHR.responseText });
                        showError("Error loading destinations. Please try again later.");
                    }
                });
            }

            /**
             * Attaches event listeners to "Select" buttons in destination cards.
             */
            function attachSelectButtonListeners() {
                document.querySelectorAll(".quick-search").forEach(button => {
                    button.addEventListener("click", function () {
                        const departureCode = this.dataset.departureCode;
                        const destinationCode = this.dataset.destinationCode;
                        const departureDate = encodeURIComponent(this.dataset.departureDate);
                        const url = `${window.contextPath}/SearchFlights?departureAirportCode=${departureCode}&destinationAirportCode=${destinationCode}&departureDate=${departureDate}&returnDate=`;
                        console.info("Navigating to:", url);
                        window.location.href = url;
                    });
                });
            }

            // Handle autocomplete selection
            departureAutocomplete.addEventListener("click", (e) => {
                if (e.target.classList.contains("autocomplete-item")) {
                    const code = e.target.getAttribute("data-value");
                    const fullName = e.target.textContent.trim(); // Use textContent as fallback
                    suggestionDepartureInput.value = fullName;
                    suggestionDepartureHidden.setAttribute("data-airport-code", code);
                    suggestionDepartureHidden.setAttribute("data-selected", "true");
                    console.info(`Selected departure: ${fullName} (${code})`);
                    updateSuggestions();
                }
            });

            // Initial update with default value
            updateSuggestions();
        });
    });
})();