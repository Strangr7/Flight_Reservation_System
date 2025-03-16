document.addEventListener("DOMContentLoaded", function () {
    // Flatpickr Initialization with Emirates theme colors
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
        defaultDate: new Date().setDate(new Date().getDate() + 8),
        disable: [
            function (date) {
                const departureDate = document.getElementById("departureDate").value;
                return departureDate && date < new Date(departureDate);
            },
        ],
    });

    // Trip type logic
    document.getElementById("tripType").addEventListener("change", function () {
        const returnDateDiv = document.querySelector(".return-date");
        const form = document.getElementById("flightSearchForm");
        const returnDateInput = document.getElementById("returnDate");

        if (this.value === "roundTrip") {
            returnDateDiv.style.display = "block";
            returnDateInput.required = true;
            form.action = window.contextPath + "/SearchRoundTripFlights";
        } else {
            returnDateDiv.style.display = "none";
            returnDateInput.required = false;
            form.action = window.contextPath + "/SearchFlights";
        }
    });

    // Set default form action
    document.getElementById("flightSearchForm").action = window.contextPath + "/SearchFlights";

    // Debounce utility to limit fetch calls
    function debounce(func, wait) {
        let timeout;
        return function (...args) {
            clearTimeout(timeout);
            timeout = setTimeout(() => func.apply(this, args), wait);
        };
    }

    // Sanitize text to prevent XSS
    function sanitizeText(text) {
        const div = document.createElement("div");
        div.textContent = text;
        return div.innerHTML;
    }

    // Airport Autocomplete
    function setupAutocomplete(inputId, autocompleteId) {
        const input = document.getElementById(inputId);
        const autocomplete = document.getElementById(autocompleteId);
        const contextPath = window.contextPath;
        let isFetching = false;

        if (!input || !autocomplete) {
            console.error("Elements not found:", { inputId, autocompleteId });
            return;
        }

        // Add ARIA attributes for accessibility
        input.setAttribute("aria-autocomplete", "list");
        input.setAttribute("aria-controls", autocompleteId);
        autocomplete.setAttribute("role", "listbox");

        const fetchAirports = debounce(function () {
            const query = input.value.trim().toLowerCase();
            if (query.length < 2) { // Minimum query length
                autocomplete.style.display = "none";
                return;
            }

            if (isFetching) return;
            isFetching = true;
            input.classList.add("loading"); // CSS class for spinner

            fetch(contextPath + "/GetAirports?term=" + encodeURIComponent(query), {
                method: "GET",
                headers: { Accept: "application/json" },
            })
                .then((response) => {
                    if (!response.ok) throw new Error(`HTTP ${response.status}`);
                    return response.text();
                })
                .then((text) => {
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
                })
                .catch((error) => {
                    console.error("Autocomplete fetch error:", error);
                    autocomplete.innerHTML = '<p class="error">Error loading airports</p>';
                    autocomplete.style.display = "block";
                })
                .finally(() => {
                    isFetching = false;
                    input.classList.remove("loading");
                });
        }, 300); // 300ms debounce

        input.addEventListener("input", fetchAirports);

        // Keyboard navigation for accessibility
        input.addEventListener("keydown", function(e) {
            if (autocomplete.style.display === "block") {
                const items = autocomplete.querySelectorAll(".autocomplete-item");
                let focusedItem = autocomplete.querySelector(".autocomplete-item:focus");
                let focusedIndex = -1;
                
                // Find the currently focused item index
                if (focusedItem) {
                    Array.from(items).forEach((item, idx) => {
                        if (item === focusedItem) focusedIndex = idx;
                    });
                }
                
                // Down arrow
                if (e.key === "ArrowDown") {
                    e.preventDefault();
                    if (focusedIndex < items.length - 1) {
                        items[focusedIndex + 1].focus();
                    } else {
                        items[0].focus(); // Cycle back to first
                    }
                }
                // Up arrow
                else if (e.key === "ArrowUp") {
                    e.preventDefault();
                    if (focusedIndex > 0) {
                        items[focusedIndex - 1].focus();
                    } else {
                        items[items.length - 1].focus(); // Cycle to last
                    }
                }
                // Enter or Tab to select
                else if (e.key === "Enter" && focusedItem) {
                    e.preventDefault();
                    input.value = focusedItem.getAttribute("data-fullname");
                    input.dataset.airportCode = focusedItem.getAttribute("data-value");
                    input.setAttribute("aria-activedescendant", focusedItem.id);
                    autocomplete.style.display = "none";
                    input.dataset.selected = "true";
                }
                // Escape to close
                else if (e.key === "Escape") {
                    autocomplete.style.display = "none";
                }
            }
        });

        autocomplete.addEventListener("click", function (e) {
            const item = e.target.closest(".autocomplete-item");
            if (item) {
                input.value = item.getAttribute("data-fullname");
                input.dataset.airportCode = item.getAttribute("data-value");
                input.setAttribute("aria-activedescendant", item.id); // Accessibility
                autocomplete.style.display = "none";
                input.dataset.selected = "true"; // Mark as selected
            }
        });

        // Close autocomplete when clicking outside
        document.addEventListener("click", function (e) {
            if (!autocomplete.contains(e.target) && e.target !== input) {
                autocomplete.style.display = "none";
            }
        });
    }

    // Setup autocomplete for both departure and destination fields
    setupAutocomplete("departureAirportCode", "departureAutocomplete");
    setupAutocomplete("destinationAirportCode", "destinationAutocomplete");

    // Form submission with validation
    const form = document.getElementById("flightSearchForm");
    form.addEventListener("submit", function (e) {
        const departureInput = document.getElementById("departureAirportCode");
        const destinationInput = document.getElementById("destinationAirportCode");
        const searchBtn = form.querySelector(".search-btn");
        const departureError = document.getElementById("departureError");
        const destinationError = document.getElementById("destinationError");

        // Reset previous error messages
        departureError.textContent = "";
        destinationError.textContent = "";
        
        // Validation checks
        let isValid = true;
        
        // Check if departure is selected properly
        if (!departureInput.dataset.selected) {
            departureError.textContent = "Please select a departure airport from the suggestions.";
            isValid = false;
        }
        
        // Check if destination is selected properly
        if (!destinationInput.dataset.selected) {
            destinationError.textContent = "Please select a destination airport from the suggestions.";
            isValid = false;
        }
        
        // Check that departure and destination are different
        if (departureInput.dataset.airportCode && 
            destinationInput.dataset.airportCode && 
            departureInput.dataset.airportCode === destinationInput.dataset.airportCode) {
            destinationError.textContent = "Departure and destination cannot be the same.";
            isValid = false;
        }

        // Prevent form submission if validation fails
        if (!isValid) {
            e.preventDefault();
            return;
        }

        // Use airport codes for submission
        if (departureInput.dataset.airportCode) {
            departureInput.value = departureInput.dataset.airportCode;
        }
        if (destinationInput.dataset.airportCode) {
            destinationInput.value = destinationInput.dataset.airportCode;
        }

        // Disable form during submission
        searchBtn.disabled = true;
        searchBtn.innerHTML = '<i class="fas fa-circle-notch fa-spin me-2"></i> Searching...';
        form.classList.add("submitting");

        // Re-enable on page unload (if needed)
        window.addEventListener("unload", () => {
            searchBtn.disabled = false;
            searchBtn.innerHTML = '<i class="fas fa-search me-2"></i> Find Flights';
            form.classList.remove("submitting");
        });
    });
});