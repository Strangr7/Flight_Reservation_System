/**
 * Flight Search Form Handler
 *
 * This script manages the functionality of the flight search form, including date picker setup,
 * airport autocomplete, form validation, and submission handling. It supports one-way and round-trip
 * flight searches by inferring trip type based on the presence of a return date.
 *
 * Dependencies:
 * - Flatpickr: For date picker functionality.
 * - FontAwesome: For icons (e.g., calendar, search, swap).
 * - jQuery: For DOM manipulation (though usage is minimal).
 *
 * @file searchFlights.js
 * @author Your Company
 * @version 1.4.0
 * @lastModified 2025-03-13
 * @requires Flatpickr, FontAwesome, jQuery
 */
document.addEventListener("DOMContentLoaded", function() {
    // SECTION: Date Picker Configuration
    /**
     * Initializes the departure date picker using Flatpickr.
     * - Sets the default date to tomorrow.
     * - Prevents selection of past dates.
     * - Formats dates for user-friendly display and backend submission.
     *
     * @see https://flatpickr.js.org/ for Flatpickr documentation.
     */
    flatpickr("#departureDate", {
        dateFormat: "Y-m-d", // ISO format for backend submission (e.g., 2025-03-14)
        altInput: true, // Enables human-readable display format
        altFormat: "F j, Y", // Display format (e.g., March 14, 2025)
        weekNumbers: true, // Displays week numbers in the calendar
        defaultDate: new Date().setDate(new Date().getDate() + 1), // Default to tomorrow
        minDate: "today", // Disables past dates
    });

    /**
     * Initializes the return date picker using Flatpickr.
     * - Ensures return date is not before the departure date.
     * - Optional field; no default date set to allow one-way trips.
     *
     * @see https://flatpickr.js.org/ for Flatpickr documentation.
     * @note The defaultDate is commented out but can be enabled if a default return date is desired.
     */
    flatpickr("#returnDate", {
        dateFormat: "Y-m-d", // ISO format for backend submission
        altInput: true, // Enables human-readable display format
        altFormat: "F j, Y", // Display format (e.g., March 14, 2025)
        minDate: "today", // Minimum date is today
        weekNumbers: true, // Displays week numbers in the calendar
        /* defaultDate: new Date().setDate(new Date().getDate() + 8), */
        disable: [
            function(date) {
                // Disables dates before the selected departure date
                const departureDate = document.getElementById("departureDate").value;
                return departureDate && date < new Date(departureDate);
            },
        ],
    });

    // SECTION: Utility Functions
    /**
     * Debounces a function to limit the rate of execution, useful for API calls.
     *
     * @param {Function} func - The function to debounce.
     * @param {number} wait - The delay in milliseconds to wait before invoking the function.
     * @returns {Function} - The debounced function.
     */
    function debounce(func, wait) {
        let timeout;
        return function(...args) {
            clearTimeout(timeout);
            timeout = setTimeout(() => func.apply(this, args), wait);
        };
    }

    /**
     * Sanitizes text to prevent XSS attacks by escaping HTML characters.
     *
     * @param {string} text - The text to sanitize.
     * @returns {string} - The sanitized text safe for DOM insertion.
     * @example sanitizeText("<script>alert('XSS')</script>") returns "&lt;script&gt;alert('XSS')&lt;/script&gt;"
     */
    function sanitizeText(text) {
        if (!text) return '';
        const div = document.createElement("div");
        div.textContent = text;
        return div.innerHTML;
    }

    // SECTION: Airport Autocomplete Implementation
    /**
     * Sets up autocomplete functionality for airport selection inputs.
     * - Fetches airport data from the server based on user input.
     * - Supports keyboard navigation for accessibility.
     * - Displays suggestions in a dropdown with city, airport name, and code.
     *
     * @param {string} inputId - The ID of the input element for airport selection.
     * @param {string} autocompleteId - The ID of the container for autocomplete suggestions.
     */
    function setupAutocomplete(inputId, autocompleteId) {
        const input = document.getElementById(inputId);
        const autocomplete = document.getElementById(autocompleteId);
        const contextPath = window.contextPath || '';
        let isFetching = false;

        // Validate that the required DOM elements exist
        if (!input || !autocomplete) {
            console.error("Autocomplete elements not found:", { inputId, autocompleteId });
            return;
        }

        // Add ARIA attributes for accessibility
        input.setAttribute("aria-autocomplete", "list");
        input.setAttribute("aria-controls", autocompleteId);
        autocomplete.setAttribute("role", "listbox");

        /**
         * Fetches airport data from the server based on user input.
         * - Debounced to prevent excessive API calls.
         * - Displays results in an autocomplete dropdown.
         * - Handles errors gracefully with user feedback.
         */
        const fetchAirports = debounce(function() {
            const query = input.value.trim().toLowerCase();

            // Skip short queries to reduce server load
            if (query.length < 2) {
                autocomplete.style.display = "none";
                return;
            }

            // Prevent concurrent requests
            if (isFetching) return;
            isFetching = true;
            input.classList.add("loading");

            fetch(`${contextPath}/GetAirports?term=${encodeURIComponent(query)}`, {
                method: "GET",
                headers: { Accept: "application/json", "Cache-Control": "no-cache" },
                credentials: "same-origin"
            })
                .then(response => {
                    if (!response.ok) throw new Error(`HTTP error ${response.status}`);
                    return response.text();
                })
                .then(text => {
                    try {
                        const data = JSON.parse(text);
                        if (Array.isArray(data) && data.length > 0) {
                            let html = "<ul>";
                            data.forEach((airport, index) => {
                                const airportCode = sanitizeText(airport.airportCode || "---");
                                const airportName = sanitizeText(airport.airportName || "Unknown Airport");
                                const city = sanitizeText(airport.city || "Unknown City");
                                html += `<li class="autocomplete-item" 
                                    role="option" 
                                    id="${inputId}-option-${index}"
                                    data-value="${airportCode}" 
                                    data-fullname="${airportName}, ${city}">
                                    ${city}, ${airportName} - ${airportCode}
                                </li>`;
                            });
                            html += "</ul>";
                            autocomplete.innerHTML = html;
                            autocomplete.style.display = "block";
                        } else {
                            autocomplete.innerHTML = '<p class="error">No airports found</p>';
                            autocomplete.style.display = "block";
                        }
                    } catch (parseError) {
                        console.error("JSON parse error:", parseError);
                        autocomplete.innerHTML = '<p class="error">Error processing response</p>';
                        autocomplete.style.display = "block";
                    }
                })
                .catch(error => {
                    console.error("Autocomplete fetch error:", error);
                    autocomplete.innerHTML = '<p class="error">Error loading airports</p>';
                    autocomplete.style.display = "block";
                })
                .finally(() => {
                    isFetching = false;
                    input.classList.remove("loading");
                });
        }, 300);

        // Attach input event listener for fetching airports
        input.addEventListener("input", fetchAirports);

        // Handle keyboard navigation for accessibility
        input.addEventListener("keydown", function(e) {
            if (autocomplete.style.display === "block") {
                const items = autocomplete.querySelectorAll(".autocomplete-item");
                let focusedItem = autocomplete.querySelector(".autocomplete-item:focus");
                let focusedIndex = -1;

                if (focusedItem) {
                    Array.from(items).forEach((item, idx) => {
                        if (item === focusedItem) focusedIndex = idx;
                    });
                }

                if (e.key === "ArrowDown") {
                    e.preventDefault();
                    if (focusedIndex < items.length - 1) {
                        items[focusedIndex + 1].focus();
                    } else {
                        items[0].focus();
                    }
                } else if (e.key === "ArrowUp") {
                    e.preventDefault();
                    if (focusedIndex > 0) {
                        items[focusedIndex - 1].focus();
                    } else {
                        items[items.length - 1].focus();
                    }
                } else if (e.key === "Enter" && focusedItem) {
                    e.preventDefault();
                    selectAirport(input, focusedItem, autocomplete);
                } else if (e.key === "Escape") {
                    autocomplete.style.display = "none";
                    input.focus();
                }
            }
        });

        // Handle click selection from autocomplete dropdown
        autocomplete.addEventListener("click", function(e) {
            const item = e.target.closest(".autocomplete-item");
            if (item) {
                selectAirport(input, item, autocomplete);
            }
        });

        /**
         * Updates the input field with the selected airport and hides the autocomplete dropdown.
         *
         * @param {HTMLElement} input - The input field to update.
         * @param {HTMLElement} item - The selected autocomplete item.
         * @param {HTMLElement} autocomplete - The autocomplete container to hide.
         */
        function selectAirport(input, item, autocomplete) {
            input.value = item.getAttribute("data-fullname");
            input.dataset.airportCode = item.getAttribute("data-value");
            input.setAttribute("aria-activedescendant", item.id);
            autocomplete.style.display = "none";
            input.dataset.selected = "true";
            const event = new Event('change', { bubbles: true });
            input.dispatchEvent(event);
        }

        // Close autocomplete when clicking outside
        document.addEventListener("click", function(e) {
            if (!autocomplete.contains(e.target) && e.target !== input) {
                autocomplete.style.display = "none";
            }
        });
    }

    // Initialize autocomplete for departure and destination fields
    setupAutocomplete("departureAirportCode", "departureAutocomplete");
    setupAutocomplete("destinationAirportCode", "destinationAutocomplete");

    // SECTION: Form Validation and Submission
    /**
     * Validates and handles the flight search form submission.
     * - Validates required fields (departure airport, destination airport, departure date).
     * - Ensures departure and destination airports are different.
     * - Infers trip type (one-way or round-trip) based on return date presence.
     * - Updates UI to show loading state during submission.
     * - Tracks search event in analytics (if available).
     */
    const form = document.getElementById("flightSearchForm");
    if (!form) {
        console.error("Form with ID 'flightSearchForm' not found.");
        return;
    }
    form.addEventListener("submit", function(e) {
        const departureInput = document.getElementById("departureAirportCode");
        const destinationInput = document.getElementById("destinationAirportCode");
        const departureDate = document.getElementById("departureDate").value;
        const returnDate = document.getElementById("returnDate").value;
        const searchBtn = form.querySelector(".search-btn");
        const departureError = document.getElementById("departureError");
        const destinationError = document.getElementById("destinationError");
        let dateError = document.getElementById("dateError");

        // Clear previous error messages
        departureError.textContent = "";
        destinationError.textContent = "";
        if (dateError) dateError.textContent = "";
        let isValid = true;

        // Validate departure airport selection
        if (!departureInput.dataset.selected || !departureInput.dataset.airportCode) {
            departureError.textContent = "Please select a departure airport from the suggestions.";
            departureInput.setAttribute("aria-invalid", "true");
            isValid = false;
        } else {
            departureInput.setAttribute("aria-invalid", "false");
        }

        // Validate destination airport selection
        if (!destinationInput.dataset.selected || !destinationInput.dataset.airportCode) {
            destinationError.textContent = "Please select a destination airport from the suggestions.";
            destinationInput.setAttribute("aria-invalid", "true");
            isValid = false;
        } else {
            destinationInput.setAttribute("aria-invalid", "false");
        }

        // Ensure departure and destination are different
        if (departureInput.dataset.airportCode && destinationInput.dataset.airportCode &&
            departureInput.dataset.airportCode === destinationInput.dataset.airportCode) {
            destinationError.textContent = "Departure and destination cannot be the same.";
            destinationInput.setAttribute("aria-invalid", "true");
            isValid = false;
        }

        // Validate dates
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

        // Determine trip type and set form action
        if (!returnDate || returnDate === "") {
            form.action = window.contextPath + "/SearchFlights"; // One-way trip
        } else {
            form.action = window.contextPath + "/SearchRoundTripFlights"; // Round-trip
        }

        // Prevent submission if validation fails
        if (!isValid) {
            e.preventDefault();
            const invalidElement = form.querySelector("[aria-invalid='true']");
            if (invalidElement) invalidElement.focus();
            return;
        }

        // Use airport codes for submission
        if (departureInput.dataset.airportCode) {
            departureInput.value = departureInput.dataset.airportCode;
        }
        if (destinationInput.dataset.airportCode) {
            destinationInput.value = destinationInput.dataset.airportCode;
        }

        // Update UI to show loading state
        searchBtn.disabled = true;
        searchBtn.innerHTML = '<i class="fas fa-circle-notch fa-spin me-2"></i> Searching...';
        form.classList.add("submitting");

        // Track search in analytics (e.g., for Google Tag Manager)
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

        // Reset UI on page unload to prevent stale state
        window.addEventListener("unload", () => {
            searchBtn.disabled = false;
            searchBtn.innerHTML = '<i class="fas fa-search me-2"></i> Find Flights';
            form.classList.remove("submitting");
        });
    });

    // SECTION: Swap Functionality
    /**
     * Handles swapping of departure and destination airports.
     * - Swaps the values and associated data attributes.
     * - Updates date constraints to maintain validity.
     * - Announces the swap for accessibility purposes.
     */
    document.querySelector(".swap-btn").addEventListener("click", function() {
        const departureInput = document.getElementById("departureAirportCode");
        const destinationInput = document.getElementById("destinationAirportCode");
        const departureDateInput = document.getElementById("departureDate");
        const returnDateInput = flatpickr("#returnDate");

        // Skip if both fields are empty
        if (!departureInput.value && !destinationInput.value) {
            return;
        }

        // Swap airport values and data attributes
        const tempValue = departureInput.value;
        departureInput.value = destinationInput.value;
        destinationInput.value = tempValue;
        const tempCode = departureInput.dataset.airportCode;
        departureInput.dataset.airportCode = destinationInput.dataset.airportCode;
        destinationInput.dataset.airportCode = tempCode;
        const tempSelected = departureInput.dataset.selected;
        departureInput.dataset.selected = destinationInput.dataset.selected;
        destinationInput.dataset.selected = tempSelected;

        // Swap date values and update constraints
        const tempDate = departureDateInput.value;
        departureDateInput.value = returnDateInput.selectedDates[0] ? returnDateInput.selectedDates[0].toISOString().split('T')[0] : '';
        if (tempDate) {
            returnDateInput.setDate(tempDate);
        } else {
            returnDateInput.clear();
        }
        returnDateInput.set('minDate', departureDateInput.value || "today");

        // Hide autocomplete dropdowns
        document.getElementById("departureAutocomplete").style.display = "none";
        document.getElementById("destinationAutocomplete").style.display = "none";

        // Announce swap for accessibility
        const announcement = document.createElement('div');
        announcement.setAttribute('aria-live', 'polite');
        announcement.textContent = 'Departure and destination airports swapped';
        announcement.className = 'sr-only';
        document.body.appendChild(announcement);
        setTimeout(() => document.body.removeChild(announcement), 3000);
    });

    // SECTION: Error Handling & Recovery
    /**
     * Global error handler to catch and log unexpected errors.
     * - Logs errors to the console and an optional error logging service.
     * - Prevents the application from breaking due to uncaught errors.
     *
     * @param {ErrorEvent} e - The error event object.
     */
    window.addEventListener('error', function(e) {
        console.error('Caught error:', e.error || e.message);
        if (window.errorLogger) {
            window.errorLogger.logError({
                source: 'flight-search.js',
                message: e.message,
                stack: e.error ? e.error.stack : null,
                timestamp: new Date().toISOString()
            });
        }
        return false;
    });

    // SECTION: Performance Monitoring
    /**
     * Marks the script load time for performance monitoring.
     * - Uses the Performance API to track script execution timing.
     *
     * @see https://developer.mozilla.org/en-US/docs/Web/API/Performance for Performance API documentation.
     */
    if (window.performance && window.performance.mark) {
        window.performance.mark('flight-search-js-loaded');
    }
});