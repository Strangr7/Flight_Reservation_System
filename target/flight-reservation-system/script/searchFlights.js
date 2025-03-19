/**
 * Flight Search Form Handler
 *
 * This script manages the functionality of the flight search form, including date picker setup,
 * airport autocomplete, form validation, and submission handling.
 */
document.addEventListener("DOMContentLoaded", function() {
    console.log("searchFlights.js loaded successfully");

    // SECTION: Date Picker Configuration
    flatpickr("#departureDate", {
        dateFormat: "Y-m-d",
        altInput: true,
        altFormat: "F j, Y",
        weekNumbers: true,
        defaultDate: new Date().setDate(new Date().getDate() + 1),
    });

    flatpickr("#returnDate", {
        dateFormat: "Y-m-d",
        altInput: true,
        altFormat: "F j, Y",
        minDate: "today",
        weekNumbers: true,
        disable: [
            function(date) {
                const departureDate = document.getElementById("departureDate").value;
                return departureDate && date < new Date(departureDate);
            },
        ],
    });

    // SECTION: Utility Functions
    function debounce(func, wait) {
        let timeout;
        return function(...args) {
            clearTimeout(timeout);
            timeout = setTimeout(() => func.apply(this, args), wait);
        };
    }

    function sanitizeText(text) {
        if (!text) return '';
        const div = document.createElement("div");
        div.textContent = text;
        return div.innerHTML;
    }

    // SECTION: Airport Autocomplete Implementation
    // Define setupAutocomplete and attach it to the window object
    window.setupAutocomplete = function(inputId, autocompleteId) {
        const input = document.getElementById(inputId);
        const autocomplete = document.getElementById(autocompleteId);
        const contextPath = window.contextPath || '';
        let isFetching = false;

        if (!input || !autocomplete) {
            console.error("Autocomplete elements not found:", { inputId, autocompleteId });
            return;
        }

        input.setAttribute("aria-autocomplete", "list");
        input.setAttribute("aria-controls", autocompleteId);
        autocomplete.setAttribute("role", "listbox");

        const fetchAirports = debounce(function() {
            const query = input.value.trim().toLowerCase();
            if (query.length < 2) {
                autocomplete.style.display = "none";
                return;
            }

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

        input.addEventListener("input", fetchAirports);

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

        autocomplete.addEventListener("click", function(e) {
            const item = e.target.closest(".autocomplete-item");
            if (item) {
                selectAirport(input, item, autocomplete);
            }
        });

        function selectAirport(input, item, autocomplete) {
            input.value = item.getAttribute("data-fullname");
            const hiddenInputId = inputId + 'Hidden';
            const hiddenInput = document.getElementById(hiddenInputId);
            if (hiddenInput) {
                hiddenInput.setAttribute('data-airport-code', item.getAttribute("data-value"));
                hiddenInput.setAttribute('data-selected', 'true');
            }
            input.dataset.airportCode = item.getAttribute("data-value");
            input.setAttribute("aria-activedescendant", item.id);
            autocomplete.style.display = "none";
            input.dataset.selected = "true";
            const event = new Event('change', { bubbles: true });
            input.dispatchEvent(event);
        }

        document.addEventListener("click", function(e) {
            if (!autocomplete.contains(e.target) && e.target !== input) {
                autocomplete.style.display = "none";
            }
        });
    };

    // Initialize autocomplete for departure, destination, and suggestion fields
    setupAutocomplete("departureAirportCode", "departureAutocomplete");
    setupAutocomplete("destinationAirportCode", "destinationAutocomplete");
    setupAutocomplete("suggestionDepartureCode", "suggestionDepartureAutocomplete");
    console.log("Autocomplete initialized for suggestionDepartureCode");

    // Rest of searchFlights.js (form validation, swap functionality, etc.) remains unchanged...
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
            form.action = window.contextPath + "/SearchFlights";
        } else {
            form.action = window.contextPath + "/SearchRoundTripFlights";
        }

        if (!isValid) {
            e.preventDefault();
            const invalidElement = form.querySelector("[aria-invalid='true']");
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
        form.classList.add("submitting");

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
            form.classList.remove("submitting");
        });
    });

    document.querySelector(".swap-btn").addEventListener("click", function() {
        const departureInput = document.getElementById("departureAirportCode");
        const destinationInput = document.getElementById("destinationAirportCode");

        if (!departureInput.value && !destinationInput.value) {
            return;
        }

        const tempValue = departureInput.value;
        departureInput.value = destinationInput.value;
        destinationInput.value = tempValue;
        const tempCode = departureInput.dataset.airportCode;
        departureInput.dataset.airportCode = destinationInput.dataset.airportCode;
        destinationInput.dataset.airportCode = tempCode;
        const tempSelected = departureInput.dataset.selected;
        departureInput.dataset.selected = destinationInput.dataset.selected;
        destinationInput.dataset.selected = tempSelected;

        document.getElementById("departureAutocomplete").style.display = "none";
        document.getElementById("destinationAutocomplete").style.display = "none";

        const announcement = document.createElement('div');
        announcement.setAttribute('aria-live', 'polite');
        announcement.textContent = 'Departure and destination airports swapped';
        announcement.className = 'sr-only';
        document.body.appendChild(announcement);
        setTimeout(() => document.body.removeChild(announcement), 3000);
    });

    window.addEventListener('error', function(e) {
        console.error('Caught error in searchFlights.js:', e.error || e.message, e.filename, e.lineno);
        return false;
    });

    if (window.performance && window.performance.mark) {
        window.performance.mark('flight-search-js-loaded');
    }
});