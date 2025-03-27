(() => {
    "use strict";

    window.App = window.App || {
        ready: false,
        managers: {},
    };

    document.addEventListener("DOMContentLoaded", () => {
        console.log("Flight search system initialized");

        // Register managers
        App.managers.DatePicker = DatePickerManager;
        App.managers.Autocomplete = AutocompleteManager;
        App.managers.Form = FormManager;
        App.managers.UIUtilities = UIUtilities;
        App.managers.SuggestedDestinations = SuggestedDestinationsManager;

        // Initialize sequentially with promises
        Promise.resolve()
            .then(() => App.managers.DatePicker.initialize())
            .then(() => App.managers.Autocomplete.initialize())
            .then(() => App.managers.Form.initialize())
            .then(() => App.managers.UIUtilities.initialize())
            .then(() => App.managers.SuggestedDestinations.initialize())
            .then(() => {
                App.ready = true;
                console.log("All managers initialized");
                if (window.performance && window.performance.mark) {
                    window.performance.mark('flight-search-system-ready');
                }
            })
            .catch(err => console.error("Initialization failed:", err));

        // Global error handling
        window.addEventListener('error', (e) => {
            console.error('Caught error:', e.error || e.message, e.filename, e.lineno);
            return false;
        });
    });
})();