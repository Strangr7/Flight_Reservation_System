const AutocompleteManager = {
    initialize: function() {
        console.log("Initializing autocomplete functionality");
        window.setupAutocomplete = this.setupAutocomplete;
        this.setupAutocomplete("departureAirportCode", "departureAutocomplete");
        this.setupAutocomplete("destinationAirportCode", "destinationAutocomplete");
        this.setupAutocomplete("suggestionDepartureCode", "suggestionDepartureAutocomplete");
        return Promise.resolve();
    },

    setupAutocomplete: function(inputId, autocompleteId) {
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

        const fetchAirports = Utilities.debounce(function() {
            const query = input.value.trim().toLowerCase();
            if (query.length < 2) {
                autocomplete.style.display = "none";
                return;
            }

            if (isFetching) return;
            isFetching = true;
            input.classList.add("loading");

            Utilities.fetchWithTimeout(`${contextPath}/GetAirports?term=${encodeURIComponent(query)}`, {
                method: "GET",
                headers: { Accept: "application/json", "Cache-Control": "no-cache" },
                credentials: "same-origin"
            }, 10000)
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
                            const airportCode = Utilities.sanitizeText(airport.airportCode || "---");
                            const airportName = Utilities.sanitizeText(airport.airportName || "Unknown Airport");
                            const city = Utilities.sanitizeText(airport.city || "Unknown City");
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
                    AutocompleteManager.selectAirport(input, focusedItem, autocomplete);
                } else if (e.key === "Escape") {
                    autocomplete.style.display = "none";
                    input.focus();
                }
            }
        });

        autocomplete.addEventListener("click", function(e) {
            const item = e.target.closest(".autocomplete-item");
            if (item) {
                AutocompleteManager.selectAirport(input, item, autocomplete);
            }
        });

        document.addEventListener("click", function(e) {
            if (!autocomplete.contains(e.target) && e.target !== input) {
                autocomplete.style.display = "none";
            }
        });
    },

    selectAirport: function(input, item, autocomplete) {
        input.value = item.getAttribute("data-fullname");
        const hiddenInputId = input.id + 'Hidden';
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
};