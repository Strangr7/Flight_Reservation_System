const UIUtilities = {
    initialize: function() {
        console.log("Initializing UI utilities");
        const swapButton = document.querySelector(".swap-btn");
        if (swapButton) {
            swapButton.addEventListener("click", this.handleSwapAirports.bind(this));
        }
        return Promise.resolve();
    },

    handleSwapAirports: function() {
        const departureInput = document.getElementById("departureAirportCode");
        const destinationInput = document.getElementById("destinationAirportCode");

        if (!departureInput || !destinationInput) {
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
    }
};