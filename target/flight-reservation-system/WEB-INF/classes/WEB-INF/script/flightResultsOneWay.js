// Global variable to track selected flight ID
let selectedFlightId = null;

// Loading state
window.onload = function() {
    const loadingElement = document.getElementById('loading');
    const flightContainer = document.getElementById('flightContainer');

    if (loadingElement && flightContainer) {
        loadingElement.style.display = 'block';
        setTimeout(() => {
            loadingElement.style.display = 'none';
            flightContainer.style.display = 'block';
        }, 2000);
    }
};

// Toggle flight details dropdown
function toggleDetails(event) {
    event.preventDefault();
    const flightCard = event.target.closest('.flight-card');
    
    if (flightCard) {
        const dropdown = flightCard.querySelector('.flight-details-dropdown');
        if (dropdown) {
            const isExpanded = dropdown.classList.contains('active');
            dropdown.classList.toggle('active');
            event.target.setAttribute('aria-expanded', !isExpanded);
            dropdown.setAttribute('aria-hidden', isExpanded);

            const arrow = event.target.querySelector('.arrow');
            if (arrow) {
                arrow.textContent = isExpanded ? '▼' : '▲';
            }
        }
    }
}

// Toggle timeline details
function toggleTimelineDetails(event) {
    event.preventDefault();
    const dropdown = document.getElementById('timelineDetails');

    if (dropdown) {
        const isExpanded = dropdown.classList.contains('active');
        dropdown.classList.toggle('active');
        event.target.setAttribute('aria-expanded', !isExpanded);
        dropdown.setAttribute('aria-hidden', isExpanded);

        const arrow = event.target.querySelector('.arrow');
        const text = event.target.querySelector('.view-more-text');

        if (arrow) {
            arrow.textContent = isExpanded ? '▼' : '▲';
        }
        if (text) {
            text.textContent = isExpanded ? 'View More' : 'View Less';
        }

        if (!isExpanded && selectedFlightId) {
            updateTimelineDetails(selectedFlightId);
        }
    }
}

// Handle fare class selection and update selected flight
function selectClass(flightId, className) {
    selectedFlightId = flightId;
    showTimeline(flightId);
    updateTimelineDetails(flightId);

    const form = document.createElement('form');
    form.method = 'post';
    form.action = window.contextPath + '/SelectFlight';

    form.innerHTML = 
        '<input type="hidden" name="flightId" value="' + flightId + '">' +
        '<input type="hidden" name="class" value="' + className + '">';

    document.body.appendChild(form);
    form.submit();
}

// Show timeline for the selected flight
function showTimeline(flightId) {
    selectedFlightId = flightId;
    const timelineSidebar = document.getElementById('timelineSidebar');

    if (timelineSidebar) {
        timelineSidebar.classList.add('visible'); // Show sidebar
    }

    const timelineWrappers = document.querySelectorAll('.timeline-wrapper');
    timelineWrappers.forEach(wrapper => {
        if (wrapper.dataset.flightId) {
            wrapper.style.display = wrapper.dataset.flightId === flightId ? 'block' : 'none';
        }
    });
}

// Update timeline details content
function updateTimelineDetails(flightId) {
    const detailsContent = document.querySelector('.timeline-details-content[data-flight-id="' + flightId + '"]');
    const detailsWrapper = document.querySelector('.timeline-details-content-wrapper');

    if (detailsWrapper) {
        detailsWrapper.innerHTML = detailsContent ? detailsContent.innerHTML : '<p>No details available.</p>';
    }
}

// Refresh timeline
function refreshTimeline() {
    if (selectedFlightId) {
        showTimeline(selectedFlightId);
        updateTimelineDetails(selectedFlightId);
    }
}

// Initialize Bootstrap tooltips and filters
document.addEventListener('DOMContentLoaded', function() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.forEach(function(tooltipTriggerEl) {
        new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Price range filter
    const priceRange = document.getElementById('priceRange');
    const priceValue = document.getElementById('priceValue');
    if (priceRange && priceValue) {
        priceRange.addEventListener('input', function() {
            priceValue.textContent = priceRange.value;
            filterFlights();
        });
    }

    // Total duration filter
    const totalDuration = document.getElementById('totalDuration');
    const durationValue = document.getElementById('durationValue');
    if (totalDuration && durationValue) {
        totalDuration.addEventListener('input', function() {
            durationValue.textContent = totalDuration.value;
            filterFlights();
        });
    }

    // Stops and Airlines filters (checkboxes)
    const filterCheckboxes = document.querySelectorAll('.filter-group input[type="checkbox"]');
    filterCheckboxes.forEach(function(checkbox) {
        checkbox.addEventListener('change', filterFlights);
    });
});

// Filter flights based on price, duration, stops, and airlines
function filterFlights() {
    const priceRangeElement = document.getElementById('priceRange');
    const maxPrice = priceRangeElement ? parseInt(priceRangeElement.value) : 2000;

    const totalDurationElement = document.getElementById('totalDuration');
    const maxDuration = totalDurationElement ? parseInt(totalDurationElement.value) : 50;

    const nonStopElement = document.getElementById('nonStop');
    const nonStopChecked = nonStopElement ? nonStopElement.checked : false;

    const oneStopElement = document.getElementById('oneStop');
    const oneStopChecked = oneStopElement ? oneStopElement.checked : false;

    const selectedAirlines = Array.from(document.querySelectorAll('input[name="airline"]:checked'))
        .map(checkbox => checkbox.value);

    const flightCards = document.querySelectorAll('.flight-card');

    flightCards.forEach(function(card) {
        let show = true;

        // Price filter
        const price = parseFloat(card.dataset.price) || 0;
        if (price > maxPrice) {
            show = false;
        }

        // Duration filter
        const duration = parseInt(card.dataset.duration) || 0;
        if (duration > maxDuration) {
            show = false;
        }

        // Stops filter
        const stops = card.dataset.stops || 'Non-stop';
        if (nonStopChecked || oneStopChecked) { // Only apply stops filter if at least one is checked
            if (nonStopChecked && stops !== 'Non-stop') {
                show = false;
            }
            if (oneStopChecked && !stops.includes('Connects in')) {
                show = false;
            }
        }

        // Airline filter
        const airlineId = card.dataset.airlineId || '';
        if (selectedAirlines.length > 0) { // Only apply airline filter if at least one is selected
            if (!selectedAirlines.includes(airlineId)) {
                show = false;
            }
        }

        // Apply visibility
        card.style.display = show ? 'block' : 'none';
    });
}