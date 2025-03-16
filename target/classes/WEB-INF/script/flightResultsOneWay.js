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

            // Default to first flight if available
           /* const firstFlightCard = document.querySelector('.flight-card');
            if (firstFlightCard) {
                selectedFlightId = firstFlightCard.dataset.flightId;
                showTimeline(selectedFlightId);
            }*/
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
    form.action = '/flight-reservation-system/SelectFlight';

    // Fixed template literal syntax issues by using string concatenation instead
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

    const priceRange = document.getElementById('priceRange');
    const priceValue = document.getElementById('priceValue');

    if (priceRange && priceValue) {
        priceRange.addEventListener('input', function() {
            priceValue.textContent = priceRange.value;
            filterFlights();
        });

        const stopFilters = document.querySelectorAll('.filter-group input[type="checkbox"]');
        stopFilters.forEach(function(checkbox) {
            checkbox.addEventListener('change', filterFlights);
        });
    }
});

function filterFlights() {
    const priceRangeElement = document.getElementById('priceRange');
    const maxPrice = priceRangeElement ? parseInt(priceRangeElement.value) : 2000;
    
    const nonStopElement = document.getElementById('nonStop');
    const nonStop = nonStopElement ? nonStopElement.checked : false;
    
    const oneStopElement = document.getElementById('oneStop');
    const oneStop = oneStopElement ? oneStopElement.checked : false;

    const flightCards = document.querySelectorAll('.flight-card');
    flightCards.forEach(function(card) {
        let show = true;
        const price = parseFloat(card.dataset.price) || 0;
        const stops = card.dataset.stops || 'Non-stop';

        if (price > maxPrice) {
            show = false;
        }
        if (nonStop && stops !== 'Non-stop') {
            show = false;
        }
        if (oneStop && !stops.includes('Connects in')) {
            show = false;
        }

        card.style.display = show ? 'block' : 'none';
    });
}